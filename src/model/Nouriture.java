package model;

import java.util.concurrent.Semaphore;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import Controler.GameControler;
import view.WindowGame;

public class Nouriture extends Entity {

	private boolean mange;
	private Semaphore mutex;

	public Nouriture(double coordX, double coordY, GameControler gc) throws SlickException {
		super(coordX, coordY, gc);
		this.mange = false;
		mutex = new Semaphore(1);
	}

	@Override
	public void render(GameContainer container, Graphics g) throws SlickException {
		Image img;
		if (gc.getNouritureFraiche() != null) {
			if (mange) {
				img = WindowGame.spriteN.getScaledCopy(128, 128).getSubImage(0, 0, 64, 64);
			} else if (gc.getNouritureFraiche().equals(this)) {
				img = WindowGame.spriteN.getScaledCopy(128, 128).getSubImage(0, 64, 64, 64);
			} else {
				img = WindowGame.spriteN.getScaledCopy(128, 128).getSubImage(64, 0, 64, 64);
			}
		} else {
			img = WindowGame.spriteN.getScaledCopy(128, 128).getSubImage(64, 64, 64, 64);
		}
		g.drawImage(img, (int) super.coordX, (int) super.coordY);
	}

	public boolean eat() {
		boolean tmp;
		try {
			mutex.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (!mange) {
			mange = true;
			gc.estMange(this);
			tmp = true;
		} else {
			tmp = false;
		}
		mutex.release();
		return tmp;
	}
}
