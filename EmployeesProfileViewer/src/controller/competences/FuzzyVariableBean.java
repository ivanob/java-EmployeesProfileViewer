package controller.competences;

import rs.ac.ns.ftn.tmd.datatypes.DecomposedFuzzyNumber;
import rs.ac.ns.ftn.tmd.datatypes.FuzzyInterval;
import rs.ac.ns.ftn.tmd.datatypes.FuzzySet;
import rs.ac.ns.ftn.tmd.datatypes.LRFuzzyNumber;

/**
 * Es una etiqueta linguistica borrosa. Una etiqueta tiene
 * un nombre que la identifica y distingue del resto dentro
 * de un conjunto, por ejemplo: "poco", "mucho" o "nivel alto"
 * son etiquetas que describen niveles de conocimiento.
 * Tambien tiene como atributo un objeto FuzzySet, que 
 * almacena el valor del dato borroso.
 * 
 * @author Ivan Obeso Aguera
 */
public class FuzzyVariableBean {
	public static int DECOMPOSED = 1, LRNUMBER = 2, INTERVAL=3;
	private FuzzySet fuzzy;
	private String label;
	
	public FuzzyVariableBean(String label, FuzzySet fuzzy){
		this.label = label;
		this.fuzzy = fuzzy;
	}
	
	/**
	 * Crea una nueva etiqueta linguistica a partir de un nombre y
	 * el conjunto de datos necesarios para crear el numero borroso
	 * asociado a ese nombre.
	 */
	public FuzzyVariableBean(int tipoFuzzy, String label, double modal, double alpha, double beta, int typeL, int typeR){
		this.label = label;
		if(tipoFuzzy == this.DECOMPOSED){
			this.fuzzy = new DecomposedFuzzyNumber(modal, alpha, beta, typeL, typeR, 50);
		}else if(tipoFuzzy == this.LRNUMBER){
			this.fuzzy = new LRFuzzyNumber(modal, alpha, beta, typeL, typeR);
		}
	}
	
	/**
	 * Crea una nueva etiqueta linguistica a partir de un nombre y los
	 * datos que definen el intervalo borroso asociado a ese nombre.
	 */
	public FuzzyVariableBean(int tipoFuzzy, String label, double modalLeft, double modalRight,
			double alpha, double beta, int typeL, int typeR){
		this.label = label;
		if(tipoFuzzy == this.INTERVAL){
			this.fuzzy = new FuzzyInterval(modalLeft, modalRight, alpha, beta, typeL, typeR, 50);
		}
	}
	
	public String getRightFunctionName(){
		switch(fuzzy.getRightFunction()){
			case FuzzySet.FUNCTION_LINEAR:
				return "Linear";
			case FuzzySet.FUNCTION_GAUSSIAN:
				return "Gaussian";
			case FuzzySet.FUNCTION_EXPONENTIAL:
				return "Exponential";
			case FuzzySet.FUNCTION_QUADRATIC:
				return "Quadratic";
		}
		return "";
	}
	
	public String getLeftFunctionName(){
		switch(fuzzy.getLeftFunction()){
			case FuzzySet.FUNCTION_LINEAR:
				return "Linear";
			case FuzzySet.FUNCTION_GAUSSIAN:
				return "Gaussian";
			case FuzzySet.FUNCTION_EXPONENTIAL:
				return "Exponential";
			case FuzzySet.FUNCTION_QUADRATIC:
				return "Quadratic";
		}
		return "";
	}
	
	public String getLabel(){
		return label;
	}
	
	public FuzzySet getFuzzy(){
		return fuzzy;
	}
}
