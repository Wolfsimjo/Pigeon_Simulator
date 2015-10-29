package Model;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import Controler.GameControler;

public class LittleBoy extends Entity implements RandomNotifier{

	private boolean bFinal;
	private int minTime, maxTime; //Temps d'attente min et max
	
	public LittleBoy(GameControler gc){
		super(0,0,gc);
		this.bFinal = true;
		this.minTime = 1;
		this.maxTime = 10;
	}
	
	public LittleBoy(double coordX, double coordY, GameControler gc) {
		super(coordX, coordY, gc);
		
	}
	
	public void run()
	{
		while(bFinal)
		{
			try 
			{
				Thread.sleep((this.minTime + (int)(Math.random() * ((this.maxTime - this.minTime) + 1))*1000));
				this.notifyObserver();
			} 
			catch (InterruptedException e) 
			{
				e.printStackTrace();
				this.bFinal = false;
			}
			Thread.yield();
		}
	}
	
	@Override
	public void render(GameContainer container, Graphics g) throws SlickException {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void notifyObserver() {
		for (Pigeon tmp : this.gc.getAlPigeon()) { 		      
	           tmp.fear();		
	    }
		this.gc.deleteAllOldNouriture();
	}
}
