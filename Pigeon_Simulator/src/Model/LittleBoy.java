package Model;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import Controler.GameControler;
import View.WindowGame;

public class LittleBoy extends Entity {

	private boolean bFinal;
	private int minTime, maxTime; // Temps d'attente min et max
	private int zone;

	private EtatLittleBoy etat;

	private float speed;
	private double dirX;
	private double dirY;

	private double destX;
	private double destY;

	public LittleBoy(GameControler gc) {
		super(-50, -50, gc);
		this.bFinal = true;
		this.minTime = 5;
		this.maxTime = 20;
		this.zone = 50;

		speed = 1f;
		dirX = 0;
		dirY = 0;
		etat = EtatLittleBoy.Sleep;
	}

	public LittleBoy(double coordX, double coordY, GameControler gc) {
		super(coordX, coordY, gc);
		this.bFinal = true;
		this.minTime = 5;
		this.maxTime = 20;
		this.zone = 50;

		speed = 1f;
		dirX = 0;
		dirY = 0;
		etat = EtatLittleBoy.Sleep;
	}
	
	/**
	 * Fonction d execution du garcon
	 */
	public void run() {
		while (bFinal) {
			try {
				sleep(10);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			switch (etat) {
			case Sleep:
				try {
					sleep((this.minTime + (int) (Math.random() * ((this.maxTime - this.minTime) + 1)) * 1000));
				} catch (InterruptedException e1) {
					bFinal = false;
					e1.printStackTrace();
				}
				beginFear();
				break;

			case Chase:
				fear();
				break;

			default:
				break;
			}
			Thread.yield();
		}
	}

	@Override
	public void render(GameContainer container, Graphics g) throws SlickException {
		if (dirX > 0.5) {
			g.drawAnimation(WindowGame.animationsC[2], (int) super.coordX, (int) super.coordY);
		} else if (dirX < -0.5) {
			g.drawAnimation(WindowGame.animationsC[1], (int) super.coordX, (int) super.coordY);
		} else if (dirY > 0.5) {
			g.drawAnimation(WindowGame.animationsC[0], (int) super.coordX, (int) super.coordY);
		} else {
			g.drawAnimation(WindowGame.animationsC[3], (int) super.coordX, (int) super.coordY);
		}
		g.drawOval((int) coordX - zone / 2, (int) coordY - zone / 2, zone + 32, zone + 32);
	}

	/**
	 * Apparition du garcon et suppression de la nourriture pourrie
	 */
	private void beginFear() {
		this.gc.deleteAllOldNouriture();
		randPosition();
		randDestination();
		etat = EtatLittleBoy.Chase;
	}

	/**
	 * Effraie les pigeons autour du garcon
	 */
	private void fear() {
		moveToDestination();
		gc.fear(this);
		if (arrivedToDestination()) {
			etat = EtatLittleBoy.Sleep;
		}
	}

	/**
	 * Retourne la distance de la zone qui effraie les pigeons
	 */
	public int getZone() {
		return this.zone;
	}

	/**
	 * Deplace le garcon
	 */
	private void moveToDestination() {
		refreshDirectionDestination();
		double norm = Math.sqrt(dirX * dirX + dirY * dirY);
		dirX = dirX / norm;
		dirY = dirY / norm;

		this.coordX = coordX + dirX * speed;
		this.coordY = coordY + dirY * speed;
	}

	/**
	 * Tourne le garcon en direction de sa destination
	 */
	private void refreshDirectionDestination() {
		dirX = destX - coordX;
		dirY = destY - coordY;
	}

	/**
	 * Fonction qui instancie la position du garcon
	 */
	private void randPosition() {
		int tmp = (int) (Math.random() * (641) + 1);
		coordX = tmp;
		coordY = -30;
	}

	/**
	 * Fonction qui instancie la destination du garcon
	 */
	private void randDestination() {
		int tmp = (int) (Math.random() * (641) + 1);
		destX = tmp;
		destY = 520;
	}

	/**
	 * Retourne vrai si le garcon est arrive a destination
	 */
	private boolean arrivedToDestination() {
		return ((int) coordX <= (int) destX && (int) coordX + 16 > (int) destX && (int) coordY <= (int) destY
				&& (int) coordY + 16 > (int) destY);
	}

}
