package controller.genetic.genes;

import controller.geometry.Point;

public class GeneEllipseConcentric extends GeneEllipse {
	
	private float[] div; //De 0 a 1
	
	public GeneEllipseConcentric(float x, float y, float a, float b,
			float rad, float[] div){
		super(x,y,a,b,rad);
		this.div=div;
		super.ptosGenerados=null;
	}
	
	public GeneEllipseConcentric(String str, int numDiv){
		super();
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
		this.div=new float[numDiv];
		for(int i=0; i<numDiv; i++){
			div[i] = binaryToFloat(str.substring(index, index+31));
			index+=31;
		}
	}
	
	public GeneEllipseConcentric(int numAlphacuts){
		super();
		float d=-1;
		div=new float[numAlphacuts];
		/* Restriccion: los alphacuts son concentricos decrecientes, es decir, 
		 * los divisores de escala de una serie de alphacuts sobre los ejes
		 * de la elipse seran decrecientes */
		for(int i=0; i<numAlphacuts; i++){
			/*if(i==0){
				d=this.floatAleatorio(0, 1);
			}else{
				d=this.floatAleatorio(0, div[i-1]);
			}
			div[i]=d;*/
			div[i]=this.floatAleatorio(0, 1);
		}
	}
	
	public Point getPointAtDiv(int numDiv, Point ref){
		float newX=x+(ref.getX()-x)*(1-div[numDiv]);
		float newY=y+(ref.getY()-y)*(1-div[numDiv]);
		return new Point(newX,newY);
	}
	
	public float getDivision(int i){
		return div[i];
	}
	
	public String geneToString(){
		String str="";
		float[] attr=new float[5+div.length];
		attr[0]=getX();
		attr[1]=getY();
		attr[2]=getA();
		attr[3]=getB();
		attr[4]=getRad();
		for(int i=0; i<div.length; i++){
			attr[5+i]=div[i];
		}		
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
	
	public Point[] getPointsCut(int numCut){
		int numPoints = this.ptosGenerados.length;
		Point[] resultPoints = new Point[numPoints];
		for(int i=0; i<numPoints; i++){
			resultPoints[i] = this.getPointAtDiv(numCut, this.ptosGenerados[i]);
		}
		return resultPoints;
	}
	
	public int getCodedSize(){
		return 31*5+31*div.length;
	}
}
