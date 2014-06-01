package controller.genetic.genes;

import controller.geometry.Point;

/**
 * Un gen es la informacion de una forma de un empleado
 * proyectada en el mapa resultado. Guarda todos los parametros
 * geometricos necesarios para representar la proyeccion.
 * 
 * @author Ivan Obeso Aguera
 */
public class Gene {
	
	public float x,y;
	
	public Gene(String str){
		int index=0;
		this.x= binaryToFloat(str.substring(index, index+31));
		index+=31;
		this.y= binaryToFloat(str.substring(index, index+31));
		index+=31;
	}
	
	public Gene(float x, float y){
		this.setX(x);
		this.setY(y);
	}
	
	public Point[] getPoints(){
		Point[] p = new Point[1];
		p[0] = new Point(x,y);
		return p;
	}
	
	public Point[] getPoints(int numPoints){
		return getPoints();
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
		return "(" + x + ", " + y + ")";
	}
	
	public boolean containsPoint(Point p){
		if(this.x == p.getX() && this.y == p.getY()){
			return true;
		}
		return false;
	}
	
	protected String floatToBinaryString(float d){
		int intBits = Float.floatToIntBits(d); 
		String binary = Integer.toBinaryString(intBits);
		return binary;
	}
	
	protected float binaryToFloat(String binary){
		int intBits2 = Integer.parseInt(binary, 2);
		float myFloat = Float.intBitsToFloat(intBits2);
		if(Float.isNaN(myFloat)){
			myFloat=0;
		}
		return myFloat;
	}
	
	public String geneToString(){
		String str="";
		for(int j=0; j<2; j++){
			String aux="";
			if(j==0){
				aux = floatToBinaryString(this.getX());
			}else if(j==1){
				aux = floatToBinaryString(this.getY());
			}
			int tam = aux.length();
			for(int k=0; k<31-tam; k++){
				aux = "0" + aux;
			}
			str += aux;
		}
		return str;
	}
	
	public int getCodedSize(){
		return 31*2;
	}
}
