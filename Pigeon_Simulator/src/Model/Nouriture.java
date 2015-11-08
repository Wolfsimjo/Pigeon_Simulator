package Model;

import java.util.concurrent.Semaphore;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import Controler.GameControler;
import View.WindowGame;

public class Nouriture extends Entity {

	private boolean mange;
	
	private Semaphore mutex;
	
	private int height = 32;
	private int width = 32;
	private int sizeX = 64;
	private int sizeY = 64;

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
				img = WindowGame.spriteN.getScaledCopy(sizeX, sizeY).getSubImage(0, 0, width, height);
			} else if (gc.getNouritureFraiche().equals(this)) {
				img = WindowGame.spriteN.getScaledCopy(sizeX, sizeY).getSubImage(0, sizeY/2, width, height);
			} else {
				img = WindowGame.spriteN.getScaledCopy(sizeX, sizeY).getSubImage(sizeX/2, 0, width, height);
			}
		} else {
			img = WindowGame.spriteN.getScaledCopy(sizeX, sizeY).getSubImage(sizeX/2, sizeY/2, width, height);
		}
		g.drawImage(img, (int) super.coordX, (int) super.coordY);
	}

	/**
	 * Retourne vrai si la nouriture n est pas encore mangee 
	 */
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

	/**
	 * Retourne vrai si la nouriture peut etre mangee par le pigeon p
	 */
	public boolean canEat(Pigeon p) {
		boolean tmp = false;
		if (p.coordX + 32 < coordX + width && p.coordX + 32 > coordX && p.coordY + 32 < coordY + height && p.coordY + 32 > coordY)
			tmp = true;
		return tmp;
	}
	
	/**
	 * Retourne l abscisse correspondant au milieu de la nouriture
	 */
	public int getMiddleX() {
		return (int) (coordX + width/2);
	}
	
	/**
	 * Retourne l ordonnee correspondant au milieu de la nouriture
	 */
	public int getMiddleY() {
		return (int) (coordY + height/2);
	}
	
}
