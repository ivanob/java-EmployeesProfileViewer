package controller.competences;

import java.util.LinkedList;

import controller.employees.EmployeeManager;

/**
 * Esta clase actua como gestor de objetos Competence, de
 * forma que se ocupa de su creacion y borrado. Es un singleton
 * asi que desde cualquier punto de la aplicacion en el que
 * se necesite trabajar con las competencias se puede
 * acceder mediante el mismo gestor CompetenceFactory a traves
 * de su metodo getInstance(), con una llamada:
 * CompetenceFactory.getInstance().
 * 
 * @author Ivan Obeso Aguera
 */
public class CompetenceFactory {
	private static CompetenceFactory INSTANCE = new CompetenceFactory();
	private LinkedList<Competence> listCompetences;
	
	public static CompetenceFactory getInstance(){
		return INSTANCE;
	}
	
	public void removeAllCompetences(){
		listCompetences = new LinkedList<Competence>();
	}
	
	public int getNumCompetences(){
		return this.listCompetences.size();
	}
	
	public void addCompetence(String name){
		this.listCompetences.add(new Competence(name, listCompetences.size()));
		EmployeeManager.getInstance().addCompetenceEvaluations();
	}
	
	public void removeCompetence(String name){
		Competence c = getCompetence(name);
		this.listCompetences.remove(c);
	}
	
	private CompetenceFactory(){
		listCompetences = new LinkedList<Competence>();
	}
	
	/**
	 * 
	 * @param i recibe la posicion desde 0 hasta numCompetences
	 * de la competencia almacenada
	 * @return El objeto Competence que esta en la posicion i
	 */
	public Competence getCompetence(int i){
		Competence res = null;
		if(i>=0 && i<listCompetences.size()){
			res = listCompetences.get(i);
		}
		return res;
	}
	
	public Competence getCompetence(String name){
		Competence res = null;
		for(Competence c : listCompetences){
			if(c.getName().compareTo(name)==0){
				res = c;
			}
		}
		return res;
	}	
	
	public void setListCompetences(LinkedList<Competence> list){
		this.listCompetences=list;
	}
	

}
