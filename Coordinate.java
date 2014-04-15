package player;

public class Coordinate {
	protected int x;
	protected int y;
	
	/*
	 * constructor
	 */
	public Coordinate(int x, int y) {
		this.x=x;
		this.y=y;
	}
	public Coordinate() {
		x=0;
		y=0;
	}
	public void setX(int x) {
		this.x =x;
	}
	public void setY(int y) {
		this.y=y;
	}
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
}
