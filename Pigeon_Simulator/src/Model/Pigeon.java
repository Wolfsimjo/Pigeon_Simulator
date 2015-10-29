package Model;

import java.util.Observable;
import java.util.Observer;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import Controler.GameControler;
import View.WindowGame;

public class Pigeon extends Entity implements Runnable, Observer,RandomObserver {

	private Nouriture nouritureFraiche;
	private Etat etat;
	private Nouriture mangeNouriture;

	private float speed;
	private double dirX;
	private double dirY;

	public Pigeon(double coordX, double coordY, GameControler gc) throws SlickException {
		super(coordX, coordY, gc);
		etat = Etat.FiniMange;
		speed = 0.5f;
		dirX = 0;
		dirY = 0;
	}

	@Override
	public void render(GameContainer container, Graphics g) throws SlickException {
		if (etat == Etat.EnChemin) {
			if (dirX > 0.5) {
				g.drawAnimation(WindowGame.animations[2], (int) super.coordX, (int) super.coordY);
			} else if (dirX < -0.5) {
				g.drawAnimation(WindowGame.animations[1], (int) super.coordX, (int) super.coordY);
			} else if (dirY > 0.5) {
				g.drawAnimation(WindowGame.animations[0], (int) super.coordX, (int) super.coordY);
			} else {
				g.drawAnimation(WindowGame.animations[3], (int) super.coordX, (int) super.coordY);
			}
		} else {
			g.drawImage(WindowGame.spriteP.getSubImage(0, 0, 64, 64), (int) super.coordX, (int) super.coordY);
		}
	}

	@Override
	public void run() {
		while (true) {
			try {
				sleep(10);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			switch (etat) {
			case FiniMange:
				if (gc.isNouvelleNouriture()) {
					nouritureFraiche = gc.getNouritureFraiche();
					etat = Etat.EnChemin;
				} else {
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
			Thread.yield(); //A voir si sa peut améliorer les perfs ....
		}
	}

	private void canEat(Nouriture n) {
		if (n.canEat(this)) {
			if (n.eat()) {
				System.out.println("mange");
				mangeNouriture = n;
				etat = Etat.Mange;
			} else {
				System.out.println("attente");
				etat = Etat.Attente;
			}
		}
	}

	private void moveTo(Nouriture n) {
		refreshDirection(n);
		double norm = Math.sqrt(dirX * dirX + dirY * dirY);
		dirX = dirX / norm;
		dirY = dirY / norm;
		// System.out.println(Math.sqrt(dirX*dirX + dirY*dirY));
		this.coordX = coordX + dirX * speed;
		this.coordY = coordY + dirY * speed;
	}

	private void refreshDirection(Nouriture n) {
		dirX = n.getMiddleX() - coordX - 32;
		dirY = n.getMiddleY() - coordY - 32;
	}

	@Override
	public void update(Observable obs, Object obj) {
		System.out.println("update" + (boolean) obj);
		if ((boolean) obj) {
			nouritureFraiche = gc.getNouritureFraiche();
			this.etat = Etat.FiniMange;
		} else
			this.etat = Etat.Attente;
	}

	@Override
	public void fear() {
		//TODO Implementer le random pour la position
		
	}
}
