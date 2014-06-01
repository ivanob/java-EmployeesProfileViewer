package controller.genetic.genes;

import controller.geometry.Point;

public class GeneEllipse extends Gene {
	protected float a,b;
	protected float rad;
	protected int NUM_PUNTOS=16;//8;
	protected float[][] matrixRotacion;
	protected Point[] ptosGenerados=null;
	
	private void rellenarMatrizRotacion(){
		matrixRotacion=new float[2][2];
		matrixRotacion[0][0]=(float) Math.cos(rad);
		matrixRotacion[0][1]=(float) -Math.sin(rad);
		matrixRotacion[1][0]=(float) Math.sin(rad);
		matrixRotacion[1][1]=(float) Math.cos(rad);
	}
	
	public GeneEllipse(String str){
		super(0,0);
		int index=0;
		this.x=binaryToFloat(str.substring(index, index+31));
		index+=31;
		this.y=binaryToFloat(str.substring(index, index+31));
		index+=31;
		this.a=binaryToFloat(str.substring(index, index+31));
		index+=31;
		this.b=binaryToFloat(str.substring(index, index+31));
		index+=31;
		this.rad=binaryToFloat(str.substring(index, index+31));
		index+=31;
		rellenarMatrizRotacion();
	}
	
	protected float floatAleatorio(float ini, float fin){
		return (float)(Math.random()*fin + ini);
	}
	
	public GeneEllipse(){
		super(0,0);
		this.x = floatAleatorio(0, 10);
		this.y = floatAleatorio(0, 10);
		this.a = floatAleatorio(0, 10);
		this.b = floatAleatorio(0, 10);
		this.rad = floatAleatorio(0, (float)Math.PI);
		rellenarMatrizRotacion();
	}
			
	public GeneEllipse(float x, float y, float a, float b, float rad){
		super(x,y);
		this.a=a;
		this.b=b;
		this.rad=rad;
		rellenarMatrizRotacion();
	}
	
	/*private float[] getXFromY(double y){
		float[] res = new float[2];
		//float aux=(float)(Math.sqrt((1-(Math.pow(y-this.y,2)/(Math.pow(b, 2))))*Math.pow(a, 2)));
		double num=y-this.y;
		if(num==0){
			num=0.000001f;
		}
		float segTerm = (float) (Math.pow(num,2)/Math.pow(this.b,2));
		float aux = (float) (Math.sqrt(Math.abs((1-segTerm)*Math.pow(this.a,2))));
		res[0]= this.x-aux;
		res[1]= this.x+aux;
		if(Float.isNaN(res[0])){
			int a=2;
		}
		return res;
	}*/
	
	private float[] getXFromY(double refY){
		float[] res = new float[2];
		float aux=(float) Math.sqrt(Math.abs((1-(Math.pow(refY, 2)/Math.pow(this.b,2))) * Math.pow(this.a,2)));
		res[0]= 0-aux;
		res[1]= 0+aux;
		return res;
	}
	
	/*private Point[] getPointsParallel(int n){
		Point[] ptos = new Point[n];
		float ini=y-b;
		float fin=y+b;
		double incr=(b*2)/(n/2);
		ptos[0]=new Point(x,ini);
		int i=1;
		ini+=incr;
		while(i<n/2){
			float[] res=getXFromY(ini);
			ptos[i]=new Point(res[0],ini);
			ptos[n-i]=new Point(res[1],ini);
			i++;
			ini+=incr;
		}
		ptos[i]=new Point(x,fin);
		return ptos;
	}*/
	
	private Point[] getPointsParallel(int n){
		Point[] ptos = new Point[n];
		float ini=0-b;
		float fin=0+b;
		double incr=(b*2)/(n/2);
		ptos[0]=new Point(0,ini);
		int i=1;
		ini+=incr;
		while(i<n/2){
			float[] res=getXFromY(ini);
			ptos[i]=new Point(res[0],ini);
			ptos[n-i]=new Point(res[1],ini);
			i++;
			ini+=incr;
		}
		ptos[i]=new Point(0,fin);
		return ptos;
	}
	
	private Point[] rotarElipse(Point[] ptos){
		for(int i=0; i<ptos.length; i++){
			float newX = matrixRotacion[0][0]*ptos[i].getX() + matrixRotacion[0][1]*ptos[i].getY();
			float newY = matrixRotacion[1][0]*ptos[i].getX() + matrixRotacion[1][1]*ptos[i].getY();
			ptos[i].setX(newX);
			ptos[i].setY(newY);
		}
		//Tambien rota el centro de la elipse
		//float newXc=matrixRotacion[0][0]*this.x + matrixRotacion[0][1]*this.y;
		//float newYc=matrixRotacion[1][0]*this.x + matrixRotacion[1][1]*this.y;
		//this.y=newYc;
		//this.x=newXc;
		return ptos;
	}
	
	/**
	 * A la hora de pintar las elipses finales, pido un numero
	 * mayor de puntos para que queden lo mejor definidas posibles.
	 * Le paso a este metodo el numero de puntos que quiero, que
	 * sera mucho mayor que la constante NUM_PUNTOS. OJO: No devuelve
	 * el numero de puntos con el que se hacen los calculos en el
	 * proceso genetico.
	 */
	public Point[] getPoints(int numPoints){
		Point[] newPoints = new Point[numPoints];
		if(numPoints!=NUM_PUNTOS){
			newPoints=getPointsParallel(numPoints);
			rotarElipse(newPoints);
			trasladarPuntos(newPoints);
			this.ptosGenerados=newPoints;
		}
		return newPoints;
	}
	
	public int getNumPuntos(){
		return getPoints().length;
	}
	
	private void trasladarPuntos(Point[] ptos){
		for(int i=0; i<ptos.length; i++){
			ptos[i].setX(ptos[i].getX()+this.x);
			ptos[i].setY(ptos[i].getY()+this.y);
		}
	}
	
	public Point[] getPoints(){
		if(ptosGenerados==null){
			ptosGenerados=getPointsParallel(NUM_PUNTOS);
			rotarElipse(ptosGenerados);
			trasladarPuntos(ptosGenerados);
		}
		return ptosGenerados;
	}
	
	public float getX(){
		return x;
	}
	
	public float getY(){
		return y;
	}
	
	public float getA(){
		return a;
	}
	
	public float getB(){
		return b;
	}
	
	public float getRad(){
		return rad;
	}
	
	public boolean containsPoint(Point p){
		double ec = Math.pow((p.getX()-x), 2)/Math.pow(a, 2);
		ec += Math.pow((p.getY()-y), 2)/Math.pow(b, 2);
		if(ec<=1){
			return true;
		}
		return false;
	}
	
	public String geneToString(){
		String str="";
		float[] attr={getX(), getY(), getA(), getB(), getRad()};
		for(int i=0; i<attr.length; i++){
			String aux=floatToBinaryString(attr[i]);
			int tam = aux.length();
			for(int l=0; l<31-tam; l++){
				aux = "0" + aux;
			}
			str += aux;
		}
		return str;
	}
	
	public int getCodedSize(){
		return 31*5;
	}
}
