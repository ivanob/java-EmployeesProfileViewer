package rs.ac.ns.ftn.tmd.datatypes;

import java.text.DecimalFormat;

/**
 * Esta clase es la implementacion de un conjunto numerico y hereda
 * directamente de la clase FuzzySet. Se define por un valor
 * minimo y un maximo, pero contiene cualquier numero real
 * comprendido entre esos dos.
 * 
 * @author Ivan Obeso Aguera
 */
public class CrispSet implements FuzzySet {
	
	protected double minVal, maxVal;
	
	public CrispSet(double min, double max){
		//Lanzar excepcion si min>=max
		this.minVal = min;
		this.maxVal = max;
	}
	
	@Override
	public FuzzySet add(FuzzySet arg2) {
		if(arg2 instanceof CrispSet){
			CrispSet crisp = (CrispSet)arg2;
			double newMin = crisp.minVal + this.minVal;
			double newMax = crisp.maxVal + this.maxVal;
			if(newMin==newMax){
				return new ScalarNumber(newMin);
			}
			return new CrispSet(newMin, newMax);
		}else if(arg2 instanceof FuzzyInterval){
			return arg2.add(this);
		}else if(arg2 instanceof DecomposedFuzzyNumber){
			return arg2.add(this);
		}
		return null;
	}

	@Override
	public FuzzySet substract(FuzzySet arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FuzzySet multiply(FuzzySet arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FuzzySet divide(FuzzySet arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FuzzySet addScalar(double arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FuzzySet multiplyScalar(double arg2) {
		return new CrispSet(this.minVal*arg2,this.maxVal*arg2);
	}

	@Override
	public FuzzySet getMultiplicationInverse() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double getMembershipValue(double universalValue) {
		if(universalValue>=minVal && universalValue<=maxVal){
			return 1;
		}
		return 0;
	}

	@Override
	public double getModalValue() {
		// TODO Auto-generated method stub
		return (maxVal+minVal)/2;
	}

	@Override
	public double getLeftBoundary() {
		return this.minVal;
	}

	@Override
	public double getRightBoundary() {
		return this.maxVal;
	}

	@Override
	public double getLeftSpread() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getRightSpread() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public FuzzySet distance(FuzzySet arg2) {
		// TODO Auto-generated method stub
		if(arg2 instanceof CrispSet){
			return distance((CrispSet)arg2);
		}else if(arg2 instanceof DecomposedFuzzyNumber){
			return ((DecomposedFuzzyNumber)arg2).distance(this);
		}
		return null;
	}
	
	public FuzzySet distance(CrispSet arg2){
		double maxOther = arg2.maxVal;
		double minOther = arg2.minVal;
		double newMin, newMax;
		newMin = this.minVal - maxOther;
		newMax = this.maxVal - minOther;
		if(newMin<0 && newMax<0){
			double oldNewMax = newMax;
			newMax = Math.abs(newMin);
			newMin = Math.abs(oldNewMax);
		}else if(newMin<0 && newMax>=0){
			newMax = Math.max(newMax, Math.abs(newMin));
			newMin = 0;
		}
		if(newMin==newMax){
			return new ScalarNumber(newMin);
		}
		return new CrispSet(newMin, newMax);
	}
	
	protected double getMin(double[] distances){
		double min = distances[0];
		for(int i=0; i<distances.length; i++){
			if(distances[i]<min){
				min=distances[i];
			}
		}
		return min;
	}
	
	protected double getMax(double[] distances){
		double max = distances[0];
		for(int i=0; i<distances.length; i++){
			if(distances[i]>max){
				max=distances[i];
			}
		}
		return max;
	}
	
	protected boolean overlap(double left1, double right1, double left2, double right2){
		boolean itOverlap = false;
		if((left1<=right2 && left1>=left2) || (right1<=right2 && right1>=left2)
				|| (left2<=right1 && left2>=left1) || (right2<=right1 && right2>=left1)){
			itOverlap = true;
		}
		return itOverlap;
	}
	
	/*public FuzzySet distance(FuzzyInterval arg2){
		double newMin, newMax;
		newMin = this.minVal - arg2.maxVal;
		newMax = this.maxVal - arg2.minVal;
		if(newMin<0 && newMax<0){
			double oldNewMax = newMax;
			newMax = Math.abs(newMin);
			newMin = Math.abs(oldNewMax);
		}else if(newMin<0 && newMax>=0){
			newMax = Math.max(newMax, Math.abs(newMin));
			newMin = 0;
		}
		double[] left = arg2.left;
		double[] right = arg2.right;
		double[] resp_left = new double[arg2.CUTS_NUMBER];
		double[] resp_right = new double[arg2.CUTS_NUMBER];
		for(int i=0; i<arg2.CUTS_NUMBER; i++){
			double ac_left_1 = this.minVal;
			double ac_right_1 = this.maxVal;
			double ac_left_2 = left[i];
			double ac_right_2 = right[i];
			double[] distances = new double[4];
			distances[0] = Math.abs(ac_left_1 - ac_left_2);
			distances[1] = Math.abs(ac_left_1 - ac_right_2);
			distances[2] = Math.abs(ac_right_1 - ac_left_2);
			distances[3] = Math.abs(ac_right_1 - ac_right_2);
			if(overlap(ac_left_1, ac_right_1, ac_left_2, ac_right_2)){
				resp_left[i] = 0;
			}else{
				resp_left[i] = getMin(distances);
			}
			resp_right[i] = getMax(distances);
		}
		return new FuzzyInterval(newMin, newMax, resp_left, resp_right);
	}*/
	
	public FuzzySet distance(DecomposedFuzzyNumber arg2){
		double newMin=-1, newMax=-1;
		if(arg2 instanceof FuzzyInterval){
			/* The fuzzyInterval have min value and max value which
			membership values 1 */
			newMin = this.minVal - ((FuzzyInterval)arg2).maxVal;
			newMax = this.maxVal - ((FuzzyInterval)arg2).minVal;
		}else if(arg2 instanceof DecomposedFuzzyNumber){
			/* FuzzyNumbers only have a modal value */
			newMin = this.minVal - arg2.getModalValue();
			newMax = this.maxVal - arg2.getModalValue();
		}
		if(newMin<0 && newMax<0){
			double oldNewMax = newMax;
			newMax = Math.abs(newMin);
			newMin = Math.abs(oldNewMax);
		}else if(newMin<0 && newMax>=0){
			newMax = Math.max(newMax, Math.abs(newMin));
			newMin = 0;
		}
		double[] left = arg2.left;
		double[] right = arg2.right;
		double[] resp_left = new double[arg2.CUTS_NUMBER];
		double[] resp_right = new double[arg2.CUTS_NUMBER];
		for(int i=0; i<arg2.CUTS_NUMBER; i++){
			double ac_left_1 = this.minVal;
			double ac_right_1 = this.maxVal;
			double ac_left_2 = left[i];
			double ac_right_2 = right[i];
			double[] distances = new double[4];
			distances[0] = Math.abs(ac_left_1 - ac_left_2);
			distances[1] = Math.abs(ac_left_1 - ac_right_2);
			distances[2] = Math.abs(ac_right_1 - ac_left_2);
			distances[3] = Math.abs(ac_right_1 - ac_right_2);
			if(overlap(ac_left_1, ac_right_1, ac_left_2, ac_right_2)){
				resp_left[i] = 0;
			}else{
				resp_left[i] = getMin(distances);
			}
			resp_right[i] = getMax(distances);
		}
		return new FuzzyInterval(newMin, newMax, resp_left, resp_right);
	}

	@Override
	public FuzzySet substractScalar(double arg2) {
		return new CrispSet(this.minVal-arg2, this.maxVal-arg2);
	}

	@Override
	public FuzzySet sqrt() {
		this.minVal = Math.sqrt(this.minVal);
		this.maxVal = Math.sqrt(this.maxVal);
		if(minVal==maxVal){
			return new ScalarNumber(minVal);
		}
		return new CrispSet(minVal, maxVal);
	}

	@Override
	public FuzzySet pow(int pot) {
		this.minVal = Math.pow(this.minVal, pot);
		this.maxVal = Math.pow(this.maxVal, pot);
		if(minVal==maxVal){
			return new ScalarNumber(minVal);
		}
		return new CrispSet(minVal, maxVal);
	}

	@Override
	public FuzzySet divideScalar(double arg2) {
		return new CrispSet(this.minVal/arg2,this.maxVal/arg2);
	}

	@Override
	public boolean existsImprecision() {
		return true;
	}

	@Override
	public boolean existsFuzzyImprecision() {
		return false;
	}

	public FuzzySet distrust(float minDistrust, float maxDistrust){
		if(minDistrust==0 && maxDistrust==0){
			return this;
		}
		double min= (this.minVal-(this.minVal*minDistrust));
		double max= (this.maxVal+((10-this.maxVal)*maxDistrust));
		return new CrispSet(min, max);
	}

	@Override
	public int getRightFunction() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getLeftFunction() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getStrType() {
		return "crisp";
	}
	
	public String toString(){
		DecimalFormat dec = new DecimalFormat("#.##");
		return "["+dec.format(this.minVal).replace(',', '.') + ", " + 
				dec.format(this.maxVal).replace(',', '.')+"]";
	}
	
	
}
