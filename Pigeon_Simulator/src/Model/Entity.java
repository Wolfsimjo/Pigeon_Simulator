package Model;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import Controler.GameControler;

public abstract class Entity extends Thread {
	protected double coordX;
	protected double coordY;
	protected GameControler gc;

	public Entity(double coordX, double coordY, GameControler gc) {
		super();
		this.setCoordX(coordX);
		this.setCoordY(coordY);
		this.gc = gc;
	}

	public double getCoordX() {
		return coordX;
	}

	public void setCoordX(double coordX) {
		this.coordX = coordX;
	}

	public double getCoordY() {
		return coordY;
	}

	public void setCoordY(double coordY) {
		this.coordY = coordY;
	}
	
	public abstract void render(GameContainer container, Graphics g) throws SlickException;
}
