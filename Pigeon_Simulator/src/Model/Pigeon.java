package Model;

import java.awt.Color;
import java.awt.Graphics;

public class Pigeon extends Character implements Runnable, ObserverNewFood {

	public Pigeon(double coordX, double coordY) {
		super(coordX, coordY);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

	@Override
	public void notifyObserver(Nouriture newFood) {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(Graphics g) {
		g.setColor(Color.RED);
		g.drawOval((int) coordX, (int) coordY, 10, 10);
	}
}
