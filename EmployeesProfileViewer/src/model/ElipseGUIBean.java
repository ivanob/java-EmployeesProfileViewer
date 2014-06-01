package model;

import java.util.LinkedList;

import controller.geometry.Point;

/**
 * Clase contenedora de la informacion geometrica necesaria para
 * representar una elipse o elipse concentrica en el mapa. Guarda
 * sus puntos del perimetro, los posibles puntos del perimetro en
 * caso de tener alpha cortes y el nombre de la proyeccion, que
 * sera el nombre del empleado que representa.
 * 
 * @author Ivan Obeso Aguera
 */
public class ElipseGUIBean {
	private LinkedList<Point[]> ellipse;
	private String name;
	private float[] alphacutsLevels;
	
	public ElipseGUIBean(LinkedList<Point[]> ellipse, String name, 
			float[] alphacutsLevels){
		this.ellipse=ellipse;
		this.name=name;
		this.alphacutsLevels=alphacutsLevels;
	}
	
	public Point[] getPerimetro(){
		return ellipse.get(0);
	}
	
	/**
	 * @param level Desde 0 (La mas externa) a n (la mas interna).
	 * @return
	 */
	public Point[] getConcentricElipse(int level){
		return ellipse.get(level+1);
	}
	
	public int getNumConcentricElipse(){
		return ellipse.size()-1;
	}
	
	public String getName(){
		return name;
	}
	
	public String getLevelName(int i){
		return "α-cut " + alphacutsLevels[i];
	}
}
