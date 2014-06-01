package controller.competences;

/**
 * Esta clase encapsula toda la informacion sobre una competencia.
 * Una competencia tiene un nombre que sirve como su identificador,
 * un identificador numerico y un FuzzySetBean que representa el
 * conjunto borroso de etiquetas asociadas a la competencia. Estas
 * etiquetas son conceptos acerca de los distintos 
 * niveles de conocimiento predefinidos sobre esa competencia.
 * 
 * @author Ivan Obeso Aguera
 */
public class Competence {
	private String name;
	private int id;
	private FuzzySetBean fuzzy;
	
	public Competence(String name, int id){
		this.setName(name);
		this.id = id;
		this.fuzzy = new FuzzySetBean();
	}
	
	/**
	 * @param i Es la posicion de la etiqueta borrosa buscada
	 * @return El objeto FuzzyVariableBean que representa la
	 * etiqueta registrada en la posicion i del conjunto borroso.
	 */
	public FuzzyVariableBean getFuzzyVariable(int i){
		return fuzzy.getFuzzyVariable(i);
	}
	
	/**
	 * @param label Es el nombre de una etiqueta registrada en
	 * el conjunto borroso de la competencia. 
	 * @return La posicion dentro del listado de la etiqueta cuyo
	 * nombre coincide con el parametro que recibe.
	 */
	public int getPositionFuzzyVariable(String label){
		return fuzzy.getPositionFuzzyVariable(label);
	}
	
	public FuzzyVariableBean getFuzzyVariable(String label){
		return fuzzy.getFuzzyVariable(label);
	}
	
	public void removeFuzzyVariable(String name){
		fuzzy.removeFuzzyVariable(name);
	}
	
	public void addFuzzyVariable(FuzzyVariableBean fuzzy){
		this.fuzzy.addFuzzyVariable(fuzzy);
	}
	
	public void addFuzzySet(FuzzySetBean fuzzy){
		this.fuzzy = fuzzy;
	}
	
	public int getId(){
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String toString(){
		return name;
	}
	
	public int getNumFuzzyVariables(){
		return fuzzy.getNumFuzzyVariables();
	}

	/**
	 * Se sustituye la etiqueta del conjunto borroso cuyo nombre
	 * coincide con el primer parametro con el objeto que recibe
	 * como segundo parametro.
	 * 
	 * @param label Es el nombre de una etiqueta registrada en la
	 * competencia.
	 * @param varNew Un objeto correspondiente a una etiqueta borrosa.
	 */
	public void modifyFuzzyVariable(String label, FuzzyVariableBean varNew) {
		this.fuzzy.modifyFuzzyVariable(label,varNew);
	}

}
