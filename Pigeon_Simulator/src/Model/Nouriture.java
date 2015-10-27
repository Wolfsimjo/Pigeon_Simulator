package Model;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class Nouriture extends Character implements NotifyNewFood {
	private boolean mange;
	private ArrayList<Pigeon> allPigeon;

	public Nouriture(double coordX, double coordY, ArrayList<Pigeon> list) {
		super(coordX, coordY);
		this.mange = false;
		this.allPigeon = list;
		newFoodApeared();
	}

	@Override
	public void newFoodApeared() {
		if (allPigeon != null) {
			for (Pigeon temp : this.allPigeon) {
				temp.notifyObserver(this);
			}
		}
	}

	@Override
	public void render(Graphics g) {
		g.setColor(Color.BLUE);
		g.drawOval((int) coordX, (int) coordY, 10, 10);
	}

}
