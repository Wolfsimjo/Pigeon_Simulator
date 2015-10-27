package model;

import java.util.Observable;
import java.util.Observer;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import Controler.GameControler;
import view.WindowGame;

public class Pigeon extends Entity implements Runnable, Observer {

	private Nouriture nouritureFraiche;
	private Etat etat;
	private Nouriture mangeNouriture;
	
	public Pigeon(double coordX, double coordY, GameControler gc) throws SlickException {
		super(coordX, coordY, gc);
		etat = Etat.FiniMange;
	}

	@Override
	public void render(GameContainer container, Graphics g) throws SlickException {
		g.drawImage(WindowGame.spriteP.getScaledCopy(64, 64), (int) super.coordX, (int) super.coordY);
	}

	@Override
	public void run() {
		while (true) {
			switch (etat) {
			case FiniMange:
				if (gc.isNouvelleNouriture()) {
					nouritureFraiche = gc.getNouritureFraiche();
					etat = Etat.EnChemin;
				}
				else {
					etat = Etat.Attente;
				}
				break;

			case Mange:
				try {
					sleep(6000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				gc.deleteNouriture(mangeNouriture);
				etat = Etat.FiniMange;
				break;
				
			case EnChemin:
				moveTo(nouritureFraiche);
				canEat(nouritureFraiche);
				break;

			default:
				break;
			}
		}
	}

	private void canEat(Nouriture n) {
		if (n.coordX == coordX && n.coordY == coordY) {
			if (n.eat()) {
				System.out.println("mange");
				mangeNouriture = n;
				etat = Etat.Mange;
			}
			else {
				System.out.println("attente");
				etat = Etat.Attente;
			}
		}
	}

	private void moveTo(Nouriture n) {
		this.coordX = n.coordX;
		this.coordY = n.coordY;
	}

	@Override
	public void update(Observable obs, Object obj) {
		nouritureFraiche = (Nouriture) obj;
		etat = Etat.EnChemin;
	}
}
