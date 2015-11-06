package Controler;

import java.util.ArrayList;
import java.util.Observable;
import java.util.concurrent.Semaphore;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import Model.LittleBoy;
import Model.Nouriture;
import Model.Pigeon;

public class GameControler extends Observable {

	private ArrayList<Pigeon> allPigeon;
	private ArrayList<Nouriture> allNouriture;
	private LittleBoy tommy;

	private Nouriture nouritureFraiche;
	private boolean nouvelleNouriture;

	private Semaphore mutex;
	private Semaphore mutexN;
	private Semaphore mutexP;

	public GameControler() {
		allPigeon = new ArrayList<Pigeon>();
		allNouriture = new ArrayList<Nouriture>();
		nouritureFraiche = null;
		tommy = new LittleBoy(this);
		mutex = new Semaphore(1);
		mutexN = new Semaphore(1);
		mutexP = new Semaphore(1);
		tommy.start();
	}

	public void render(GameContainer container, Graphics g) throws SlickException {
		for (Pigeon p : allPigeon) {
			p.render(container, g);
		}
		tommy.render(container, g);
		try {
			mutex.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		for (Nouriture n : allNouriture) {
			n.render(container, g);
		}
		mutex.release();
	}

	public void addPigeon(Pigeon p) {
		try {
			mutexP.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		allPigeon.add(p);
		mutexP.release();
		p.start();
	}

	public void addNouriture(Nouriture n) {
		try {
			mutex.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		try {
			mutexN.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		allNouriture.add(n);
		mutexN.release();
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
			e.printStackTrace();
		}
		allNouriture.remove(n);
		mutex.release();
	}

	public void deleteAllOldNouriture() {
		try {
			mutexN.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		for (Nouriture tmp : allNouriture) {
			if (!getNouritureFraiche().equals(tmp)) {
				deleteNouriture(tmp);
			}
		}
		mutexN.release();
	}

	public ArrayList<Pigeon> getAllPigeon() {
		return this.allPigeon;
	}

	public void fear(LittleBoy c) {
		try {
			mutexP.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (!(allPigeon.isEmpty())) {
			for (Pigeon tmp : allPigeon) {
				if (tmp.isInZone(c)) {
					tmp.fear();
				}
			}
		}
		mutexP.release();
	}
}
