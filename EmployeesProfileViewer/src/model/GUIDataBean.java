package model;

import java.util.LinkedList;

import controller.geometry.Point;

/**
 * Es la clase contenedora de toda la informacion que el algoritmo
 * genetico le pasa a la vista para que pueda representar el mejor
 * mapa que ha encontrado. Contiene ademas informacion adicional
 * sobre la ejecucion para mostrarla al usuario en pantalla, como
 * son el valor del fitness del mapa y el tiempo que ha tardado
 * en generarse.
 * @author Ivan Obeso Aguera
 */
public class GUIDataBean {
	private LinkedList<ElipseGUIBean> ellipseEmployers;
	private LinkedList<PointGUIBean> pointsEmployers;
	private LinkedList<PointGUIBean> pointsCP;
	private double fitness;
	private long time;
	
	public GUIDataBean(){
		ellipseEmployers = new LinkedList<ElipseGUIBean>();
		pointsEmployers = new LinkedList<PointGUIBean>();
		pointsCP = new LinkedList<PointGUIBean>();
	}
	
	public void addEllipseEmployer(ElipseGUIBean ellipse){
		this.ellipseEmployers.add(ellipse);
	}

	public void addPointEmployer(PointGUIBean pointBean) {
		this.pointsEmployers.add(pointBean);
	}

	public int getNumPointEmpl(){
		return this.pointsEmployers.size();
	}
	
	public PointGUIBean getPointEmpl(int i){
		return this.pointsEmployers.get(i);
	}
	
	public void addPointCaracteristic(PointGUIBean pointBean) {
		this.pointsCP.add(pointBean);
	}
	
	public int getNumCP(){
		return pointsCP.size();
	}
	
	public int getNumEmplEllipse(){
		return ellipseEmployers.size();
	}
	
	public PointGUIBean getCP(int i){
		return pointsCP.get(i);
	}
	
	public ElipseGUIBean getElipseBean(int i){
		return ellipseEmployers.get(i);
	}
	
	public double getFitness(){
		return fitness;
	}
	
	public void setFitness(double f){
		this.fitness=f;
	}
	
	public long getTime(){
		return time;
	}

	public void setTime(long l) {
		this.time=l;
	}
}
