package controller.evaluations;

import controller.competences.Competence;
import rs.ac.ns.ftn.tmd.datatypes.FuzzySet;
import rs.ac.ns.ftn.tmd.datatypes.ScalarNumber;

/**
 * Es la evaluacion de un empleado en una competencia. Este objeto se
 * almacena como atributo de la clase Employee. Guarda una referencia a
 * la competencia evaluada, el valor de esa evaluacion (un objeto 
 * FuzzySet) y una fuente de origen de la nota.
 * 
 * @author Ivan Obeso Aguera
 */
public class Evaluation {
	private Competence comp;
	private FuzzySet eval;
	private int numFuzzyVar;
	private Source s;
	
	public String toString(){
		String str="(source: " + s.getName() + "): ";
		str += eval.toString();
		return str;
	}
	
	public String getStrValue(){
		if(eval.existsFuzzyImprecision()){
			return comp.getFuzzyVariable(numFuzzyVar).getLabel();
		}else{
			return eval.toString();
		}
	}
	
	public Evaluation(Competence comp, Source s, FuzzySet eval){
		this.comp = comp;
		this.eval = eval;
		this.s = s;
		this.numFuzzyVar = -1;
		applyDistrust();
	}
	
	public Evaluation(Competence comp, Source s, FuzzySet eval, boolean applyDistrust){
		this.comp = comp;
		this.eval = eval;
		this.s = s;
		this.numFuzzyVar = -1;
		if(applyDistrust){
			applyDistrust();
		}
	}
	
	/**
	 * Este metodo aplica el modificador que afecta a la evaluacion
	 * en funcion de la confianza que tengamos en su fuente de 
	 * origen. Se invocara cuando se crea una nueva evaluacion.
	 */
	private void applyDistrust(){
		eval=eval.distrust(s.getMinDistrust(), s.getMaxDistrust());
	}
	
	/**
	 * Recibe el numero de variable fuzzy que tiene registrada
	 * la competencia, desde 0...n.
	 * @param comp
	 * @param numFuzzyVar
	 */
	public Evaluation(Competence comp, Source s, int numFuzzyVar){
		this.comp = comp;
		this.eval = comp.getFuzzyVariable(numFuzzyVar).getFuzzy();
		this.s = s;
		this.numFuzzyVar=numFuzzyVar;
		applyDistrust();
	}

	public Evaluation(Competence c, Source source, FuzzySet fuzzy, int pos) {
		this.comp = c;
		this.eval = fuzzy;//comp.getFuzzyVariable(numFuzzyVar).getFuzzy();
		this.s = source;
		this.numFuzzyVar=pos;
		applyDistrust();		
	}

	public Competence getCompetence(){
		return comp;
	}
	
	public FuzzySet getMark(){
		return eval;
	}

	public boolean existImprecision() {
		return eval.existsImprecision();
	}
	
	public boolean existFuzzyImprecision() {
		return eval.existsFuzzyImprecision();
	}

	public Source getSource() {
		return s;
	}
}
