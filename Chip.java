package player;

public class Chip {
	Coordinate coord;
	int color;
	
	public Chip(int color) {
		this.color = color;
		coord  = new Coordinate();
	}
	public Chip(int color, int x, int y) {
		this.color = color;
		coord = new Coordinate(x ,y);
	}

	Coordinate getCoordinate() {
		return coord;
	}
	
	int getX() {
		return coord.getX();
	}
	
	int getY() {
		return coord.getY();
	}
	
	int getColor() {
		return color;
	}
	boolean equal(Chip c) {
		if(color == getColor() && getX()==c.getX() && getY() == c.getY()){
			return true;
		}
		return false;
	}
	public String toString() {
		return color+" "+coord.getX()+" "+coord.getY();
	}
	
}
