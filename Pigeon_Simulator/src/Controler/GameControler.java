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

	/**
	 * Ajoute le pigeon p dans notre scene
	 */
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

	/**
	 * Ajoute la nouriture n dans notre scene
	 * Si il existait deja une nouriture celle ci devient pourrie 
	 * et la nouvelle devient fraiche
	 */
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

	/**
	 * Verifie si la nouriture fraiche est mangee
	 */
	public void estMange(Nouriture n) {
		if (nouritureFraiche.equals(n)) {
			nouvelleNouriture = false;
			setChanged();
			notifyObservers(false);
		}
	}

	/**
	 * Retourne vrai si il y a une nouriture fraiche dans la scene
	 */
	public boolean isNouvelleNouriture() {
		return nouvelleNouriture;
	}

	/**
	 * Retourne la nouriture fraiche de la scene
	 */
	public Nouriture getNouritureFraiche() {
		return nouritureFraiche;
	}

	/**
	 * Supprime la nouriture n de la scene
	 */
	public void deleteNouriture(Nouriture n) {
		try {
			mutex.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		allNouriture.remove(n);
		mutex.release();
	}

	/**
	 * Supprime toutes les nouritures pourries de la scene
	 */
	public void deleteAllOldNouriture() {
		ArrayList<Nouriture> tmpList = new ArrayList<Nouriture>();
		try {
			mutexN.acquire();
			mutex.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		for (Nouriture tmp : allNouriture) {
			if (!getNouritureFraiche().equals(tmp)) {
				tmpList.add(tmp);
			}			
		}
		this.allNouriture.removeAll(tmpList);
		mutex.release();
		mutexN.release();
	}

	/**
	 * Retourne tous les pigeons de la scene
	 */
	public ArrayList<Pigeon> getAllPigeon() {
		return this.allPigeon;
	}

	/**
	 * Effraie les pigeons
	 */
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
