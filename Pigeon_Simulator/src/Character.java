
public class Character 
{
	protected double coordX;
	protected double coordY;
	
	public Character(double coordX, double coordY) {
		super();
		this.setCoordX(coordX);
		this.setCoordY(coordY);
	}
	
	public void moveTo(double newX, double newY)
	{
		this.coordX = newX;
		this.coordY = newY;
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
}
