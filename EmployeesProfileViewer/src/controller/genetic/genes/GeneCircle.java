package controller.genetic.genes;


public class GeneCircle extends Gene {
	protected float radius;
	
	public GeneCircle(float x, float y, float radius){
		super(x,y);
		this.radius = radius;
	}
	
	public float getModal(){
		return (x+y)/2;
	}
	
	public float getRadius(){
		return radius;
	}
	
	public void setRadius(float radius){
		this.radius = radius;
	}
	
	public String toString(){
		return "(" + super.x + ", " + super.y + ") r: " + this.radius;
	}
}
