package controller.competences;

import java.util.ArrayList;

/**
 * Esta clase representa un conjunto borroso de etiquetas
 * linguisticas. Actua como contenedor de objetos de tipo
 * FuzzyVariableBean, que son las propias etiquetas.
 * 
 * @author Ivan Obeso Aguera
 */
public class FuzzySetBean {
	private ArrayList<FuzzyVariableBean> fuzzySets;
	
	public FuzzySetBean(){
		fuzzySets = new ArrayList<FuzzyVariableBean>();
	}
	
	public void addFuzzyVariable(FuzzyVariableBean fuzzyvar){
		this.fuzzySets.add(fuzzyvar);
	}
	
	public void removeFuzzyVariable(String name){
		FuzzyVariableBean del=null;
		for(FuzzyVariableBean fvb : fuzzySets){
			if(fvb.getLabel().compareTo(name)==0){
				del=fvb;
			}
		}
		fuzzySets.remove(del);
	}
	
	public FuzzyVariableBean getFuzzyVariable(int i){
		return fuzzySets.get(i);
	}
	
	public int getPositionFuzzyVariable(String label){
		int res = -1;
		for(int i=0; i<fuzzySets.size(); i++){
			FuzzyVariableBean fvb = fuzzySets.get(i);
			if(fvb.getLabel().compareTo(label)==0){
				res = i;
			}
		}
		return res;
	}
	
	
	public FuzzyVariableBean getFuzzyVariable(String label){
		FuzzyVariableBean res = null;
		for(FuzzyVariableBean fvb : fuzzySets){
			if(fvb.getLabel().compareTo(label)==0){
				res = fvb;
			}
		}
		return res;
	}
	
	public int getNumFuzzyVariables(){
		return fuzzySets.size();
	}

	public void modifyFuzzyVariable(String name, FuzzyVariableBean var) {
		int index=-1, i=0;
		for(FuzzyVariableBean fvb : fuzzySets){
			if(fvb.getLabel().compareTo(name)==0){
				index=i;
			}
			i++;
		}
		fuzzySets.set(index, var);
	}
}
