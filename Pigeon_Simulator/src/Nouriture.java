import java.util.ArrayList;


public class Nouriture extends Character implements NotifyNewFood
{
	private boolean mange;
	private ArrayList<Pigeon> allPigeon;
	
	public Nouriture(double coordX, double coordY, ArrayList<Pigeon> list) 
	{
		super(coordX, coordY);
		this.mange = false;
		this.allPigeon = list;
		newFoodApeared();
	}

	@Override
	public void newFoodApeared() {
		for (Pigeon temp : this.allPigeon) {
			temp.notifyObserver(this);
		}
	}

}
