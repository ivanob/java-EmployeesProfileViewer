package rs.ac.ns.ftn.tmd.datatypes;

/**
 * Esta clase implementa un valor escalar simple. Hereda de CrispSet
 * puesto que puede verse como un conjunto cuyo valor minimo es el 
 * mismo que el maximo.
 * 
 * @author Ivan Obeso Aguera
 * 
 */
public class ScalarNumber extends CrispSet {

	private double scalar;
	
	public ScalarNumber(double num){
		super(num, num);
		this.scalar=num;
	}
	
	public double getModalValue(){
		return this.scalar;
	}
	
	public FuzzySet substractScalar(double arg2) {
		return new ScalarNumber(this.scalar-arg2);
	}

	public FuzzySet divideScalar(double arg2) {
		return new ScalarNumber(this.scalar/arg2);
	}
	
	public FuzzySet multiplyScalar(double arg2){
		return new ScalarNumber(this.scalar*arg2);
	}
	
	public String toString(){
		return Double.toString(scalar);
	}
	
	public boolean existsImprecision(){
		return false;
	}
	
	public FuzzySet distrust(float minDistrust, float maxDistrust){
		if(minDistrust==0 && maxDistrust==0){
			return this;
		}
		double min= (scalar-(scalar*minDistrust));
		double max= (scalar+((10-scalar)*maxDistrust));
		return new CrispSet(min, max);
	}
	
	public String getStrType() {
		return "scalar";
	}
	
}
