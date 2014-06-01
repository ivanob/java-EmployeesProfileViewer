package model;

import controller.geometry.Point;

/**
 * Representa un punto de coordenadas en el mapa, el cual
 * tiene asociado un nombre.
 * 
 * @author Ivan Obeso Aguera
 */
public class PointGUIBean {
	private Point p;
	private String name;
	
	public PointGUIBean(Point p, String name){
		this.p = p;
		this.name = name;
	}
	
	public Point getPoint(){
		return p;
	}
	
	public String getName(){
		return name;
	}
}
