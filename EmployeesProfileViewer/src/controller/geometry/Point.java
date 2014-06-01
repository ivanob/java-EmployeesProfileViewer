package controller.geometry;

public class Point {
	protected float x,y;
	
	public Point(float x, float y){
		this.setX(x);
		this.setY(y);
	}
	
	public float get(int i){
		switch(i){
			case 0:
				return x;
			case 1:
				return y;
		}
		return -1;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}
	
	public Point calculateVector(Point origen){
		Point p = new Point(x-origen.x, y-origen.y);
		return p;
	}
	
	public String toString(){
		return "(" + x + ", " + y +")";
	}
}
