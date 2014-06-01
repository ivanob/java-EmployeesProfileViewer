package controller.geometry;

public class Circle extends Point {
	private float radius;
	
	public Circle(float x, float y, float radius){
		super(x,y);
		this.radius = radius;
	}
	
	public float get(int i){
		switch(i){
			case 0:
				return super.x;
			case 1:
				return super.y;
			case 2:
				return radius;
		}
		return -1;
	}
	
	public float getRadius(){
		return radius;
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
	
	public String toString(){
		return "(" + x + ", " + y +")";
	}
}
