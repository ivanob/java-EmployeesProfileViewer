package controller.employees;

import java.util.LinkedList;

import controller.evaluations.Evaluation;

import rs.ac.ns.ftn.tmd.datatypes.FuzzySet;

/**
 * Esta clase es un contenedor de los datos de un empleado.
 * Se guardan su nombre, lista de evaluaciones y la lista
 * de evaluaciones medias calculada a partir de la lista
 * anterior.
 * 
 * @author Ivan Obeso Aguera
 * 
 */
public class Employee {
	private String name;
	/**
	 * evals es una lista de listas. Cada posicion de
	 * la lista corresponde a una competencia (a
	 * la competencia que tiene esa misma posicion
	 * en el CompetenceFactory) y contiene una lista
	 * con todas las evaluaciones que el empleado
	 * puede tener para una misma competencia.
	 */
	private LinkedList<LinkedList<Evaluation>> evals;
	/**
	 * evalsMeans es una lista resumen de la lista anterior,
	 * y en la que se guarda para cada posicion la media
	 * aritmetica de todas las evaluaciones que existen 
	 * en esa posicion. En otras palabras, cada posicion
	 * de evalsMeans guarda la media de todas las
	 * notas registradas que existen para la competencia
	 * que tiene esa misma posicion.
	 */
	private LinkedList<Evaluation> evalsMeans;
	
	public Employee(String name, int numComp){
		evals = new LinkedList<LinkedList<Evaluation>>();
		for(int i=0; i<numComp; i++){
			evals.add(new LinkedList<Evaluation>());
		}
		evalsMeans = new LinkedList<Evaluation>();
		this.name = name;
	}
	
	public LinkedList<Evaluation> getEvaluations(int compId){
		return evals.get(compId);
	}
	
	/**
	 * Devuelve true si entre todas las evaluaciones
	 * existe alguna que tenga imprecision. Retorna
	 * false en caso contrario.
	 */
	public boolean existsImprecision(){
		for(Evaluation ev : evalsMeans){
			if(ev.existImprecision()){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Devuelve true si existe imprecision borrosa (bien
	 * sea un numero o intervalo borroso) en alguna de
	 * las evaluaciones del empleado y false en caso de
	 * que todas las notas sean precisas (Scalar) o tengan 
	 * imprecision simple (sean CrispSet).
	 */
	public boolean existsFuzzyImprecision(){
		for(Evaluation ev : evalsMeans){
			if(ev.existFuzzyImprecision()){
				return true;
			}
		}
		return false;
	}
	
	public String getName(){
		return name;
	}
	
	public void addEvaluation(Evaluation ev){
		/* Aniado la evaluacion a la lista de evaluaciones,
		 * no es un add de suma de notas. */
		evals.get(ev.getCompetence().getId()).add(ev);
		calculateMeans(); /* Recalculo la media de 
		evaluaciones para esa competencia */
	}
	
	public void removeEvaluation(int compId, int i){
		evals.get(compId).remove(i);
		calculateMeans(); /* Recalculo la media de 
		evaluaciones para esa competencia */
	}
	
	/**
	 * Recalcula la lista evalsMeans a partir de la lista evals
	 * del empleado.
	 */
	private void calculateMeans(){
		evalsMeans=new LinkedList<Evaluation>();
		for(int i=0; i<evals.size(); i++){
			FuzzySet ev=null;
			if(!evals.get(i).isEmpty()){
				int j;
				for(j=0; j<evals.get(i).size(); j++){
					if(j==0){
						ev=evals.get(i).get(j).getMark();
					}else{
						ev=ev.add(evals.get(i).get(j).getMark());
					}
				}
				ev=ev.divideScalar(j);
				evalsMeans.add(new Evaluation(evals.get(i).get(0).getCompetence(),
						evals.get(i).get(0).getSource(), ev, false));
			}
			else{
				evalsMeans.add(null);
			}
		}
	}
	
	public Evaluation getEvaluation(int idComp){
		return evalsMeans.get(idComp);
	}
	
	public String toString(){
		return name;
	}
	
	/**
	 * Este metodo auxiliar recibe como parametro un objeto
	 * empleado y calcula la distancia euclidea entre ese objeto
	 * y el empleado que llama al metodo. La distancia entre
	 * 2 empleados se calcula como la distancia euclidea entre los 2
	 * vectores multidimensionales que representan las evaluaciones 
	 * medias del empleado en cada competencia.
	 */
	public FuzzySet euclideanDistance(Employee other){
		FuzzySet result = null;
		boolean first=false;
		for(Evaluation ev : evalsMeans){
			Evaluation otherEval = other.getEvaluation(ev.getCompetence().getId());
			FuzzySet dist = ev.getMark().distance(otherEval.getMark());
			dist = dist.pow(2);
			if(first){
				result = dist;
				first=true;
			}else{
				result = result.add(dist);
			}
		}
		return result.sqrt();
	}

	/**
	 * Añade una nueva lista a la lista de evaluaciones cuando
	 * se da de alta una nueva competencia en el sistema.
	 */
	public void addNewCompetenceEvaluations() {
		evals.add(new LinkedList<Evaluation>());
	}

}
