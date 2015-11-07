package Model;

import java.util.Observable;
import java.util.Observer;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import Controler.GameControler;
import View.WindowGame;

public class Pigeon extends Entity implements Runnable, Observer {

	private Nouriture nouritureFraiche;
	private Etat etat;
	private Nouriture mangeNouriture;

	private float speed;
	private double dirX;
	private double dirY;

	private double secureX;
	private double secureY;

	public Pigeon(double coordX, double coordY, GameControler gc) throws SlickException {
		super(coordX, coordY, gc);
		etat = Etat.FiniMange;
		speed = 0.5f;
		dirX = 0;
		dirY = 0;
	}

	@Override
	public void render(GameContainer container, Graphics g) throws SlickException {
		if (etat == Etat.EnChemin || etat == Etat.Effraye) {
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
				refreshDirection(nouritureFraiche);
				moveTo(nouritureFraiche);
				canEat(nouritureFraiche);
				break;

			case Effraye:
				refreshDirection(secureX, secureY);
				moveTo(secureX, secureY);
				isSecure();
			default:
				break;
			}
			Thread.yield();
		}
	}

	private void isSecure() {
		if (((int) coordX <= (int) secureX && (int) coordX + 16 > (int) secureX 
				&& (int) coordY <= (int) secureY && (int) coordY + 16 > (int) secureY)) {
			speed = 0.5f;
			etat = Etat.FiniMange;
		}
	}

	private void randSecure() {
		secureX = (int) (Math.random() * 641-64);
		secureY = (int) (Math.random() * 481-64);
		speed = 1f;
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
		double norm = Math.sqrt(dirX * dirX + dirY * dirY);
		dirX = dirX / norm;
		dirY = dirY / norm;

		this.coordX = coordX + dirX * speed;
		this.coordY = coordY + dirY * speed;
	}

	private void moveTo(double x, double y) {
		double norm = Math.sqrt(dirX * dirX + dirY * dirY);
		dirX = dirX / norm;
		dirY = dirY / norm;

		this.coordX = coordX + dirX * speed;
		this.coordY = coordY + dirY * speed;
	}

	private void refreshDirection(Nouriture n) {
		dirX = n.getMiddleX() - coordX - 32;
		dirY = n.getMiddleY() - coordY - 32;
	}

	private void refreshDirection(double x, double y) {
		dirX = x - coordX;
		dirY = y - coordY;
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

	public boolean isInZone(LittleBoy e) {
		int z = e.getZone();
		int x = (int) e.coordX;
		int y = (int) e.coordY;
		if (coordX + 32 > x - z && coordX + 32 < x + z + 32 
				&& coordY + 32 > y - z && coordY + 32 < y + z + 32) {
			return true;
		}
		return false;
	}

	public void fear() {
		if (etat != Etat.Effraye) {
			randSecure();
			etat = Etat.Effraye;
		}

	}
}
