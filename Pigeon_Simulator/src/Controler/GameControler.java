package Controler;

import java.util.ArrayList;
import java.util.Observable;
import java.util.concurrent.Semaphore;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import Model.Nouriture;
import Model.Pigeon;

public class GameControler extends Observable {

	private ArrayList<Pigeon> allPigeon;
	private ArrayList<Nouriture> allNouriture;

	private Nouriture nouritureFraiche;
	private boolean nouvelleNouriture;

	private Semaphore mutex;

	public GameControler() {
		allPigeon = new ArrayList<Pigeon>();
		allNouriture = new ArrayList<Nouriture>();
		nouritureFraiche = null;
		mutex = new Semaphore(1);
	}

	public void render(GameContainer container, Graphics g) throws SlickException {
		for (Pigeon p : allPigeon) {
			p.render(container, g);
		}
		try {
			mutex.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (Nouriture n : allNouriture) {
			n.render(container, g);
		}
		mutex.release();
	}

	public void addPigeon(Pigeon p) {
		allPigeon.add(p);
		p.start();
	}

	public void addNouriture(Nouriture n) {
		try {
			mutex.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		allNouriture.add(n);
		mutex.release();
		nouritureFraiche = n;
		nouvelleNouriture = true;
		setChanged();
		notifyObservers(true);
	}

	public void estMange(Nouriture n) {
		if (nouritureFraiche.equals(n)) {
			nouvelleNouriture = false;
			setChanged();
			notifyObservers(false);
		}
	}

	public boolean isNouvelleNouriture() {
		return nouvelleNouriture;
	}

	public Nouriture getNouritureFraiche() {
		return nouritureFraiche;
	}

	public void deleteNouriture(Nouriture n) {
		try {
			mutex.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		allNouriture.remove(n);
		mutex.release();
	}
}
