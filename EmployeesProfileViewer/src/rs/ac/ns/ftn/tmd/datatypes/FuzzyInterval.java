package rs.ac.ns.ftn.tmd.datatypes;

import java.text.DecimalFormat;

/**
 * Esta clase implementa un intervalo borroso. Hereda directamente de 
 * un numero borroso y del interface FuzzySet, por lo que implementa
 * todos sus metodos.
 * Se diferencia en un numero borroso en que en vez de tener un valor
 * modal tiene 2: uno minimo y uno maximo que representan un rango, por
 * lo que todos los valores comprendidos entre esos 2 numeros tienen
 * funcion de pertenencia 1. Estos 2 valores definen la base superior
 * del trapecio.
 * 
 * @author Ivan Obeso Aguera
 */
public class FuzzyInterval extends DecomposedFuzzyNumber implements FuzzySet{
	
	protected double minVal, maxVal; /* Min value and max value which
	membership function is 1 */

	public FuzzyInterval(double minVal, double maxVal, double alpha, double beta,
			int Ltype, int Rtype, int cuts_number) {
		super((maxVal+minVal)/2, alpha, beta, Ltype, Rtype, cuts_number);
		this.minVal=minVal;
		this.maxVal=maxVal;
		this.left = new double[this.CUTS_NUMBER];
		this.right = new double[this.CUTS_NUMBER];
		this.left[this.CUTS_NUMBER-1] = minVal;
		this.right[this.CUTS_NUMBER-1] = maxVal;
		//linear alpha-cutting
		for (int i=0; i<this.CUTS_NUMBER-1; i++) {
			this.left[i] = minVal-this.getAlphaCut((double)i/(double)this.CUTS_NUMBER, alpha, Ltype);
			this.right[i] = maxVal+this.getAlphaCut((double)i/(double)this.CUTS_NUMBER, beta, Rtype);
		}
	}
	
	public double getModalLeft(){
		return minVal;
	}
	
	public double getModalRight(){
		return maxVal;
	}
	
	public FuzzyInterval(double minVal, double maxVal, double[]left, double[]right){
		//super((maxVal+minVal)/2,-1,-1,-1,-1,left.length);
		super(left,right);
		this.minVal = minVal;
		this.maxVal = maxVal;
		this.left = left;
		this.right = right;
	}

	@Override
	public FuzzySet add(FuzzySet arg2) {
		if(arg2 instanceof ScalarNumber){
			ScalarNumber sc = (ScalarNumber)arg2;
			double[] newLeft = this.getLeftCuts().clone();
			double[] newRight = this.getRightCuts().clone();
			for(int i=0; i<this.CUTS_NUMBER; i++){
				newLeft[i] += sc.getModalValue();
				newRight[i] += sc.getModalValue();
			}
			return new FuzzyInterval(this.minVal+sc.getModalValue(),
					this.maxVal+sc.getModalValue(),
					newLeft, newRight);
		}else if(arg2 instanceof CrispSet){
			CrispSet cs = (CrispSet)arg2;
			double[] newLeft = this.getLeftCuts().clone();
			double[] newRight = this.getRightCuts().clone();
			for(int i=0; i<this.CUTS_NUMBER; i++){
				newLeft[i] += cs.getLeftBoundary();
				newRight[i] += cs.getRightBoundary();
			}
			return new FuzzyInterval(this.minVal+cs.getLeftBoundary(),
					this.maxVal+cs.getRightBoundary(),
					newLeft, newRight);
		}else if(arg2 instanceof FuzzyInterval){
			//this.checkImplementationCompatibilityWith(arg2);
			FuzzyInterval fint=(FuzzyInterval)arg2;
			
			double[] left2 = fint.getLeftCuts();
			double[] right2 = fint.getRightCuts();
			
			double[] newLeft = new double[this.CUTS_NUMBER];
			double[] newRight = new double[this.CUTS_NUMBER];
			for (int i=0; i<this.CUTS_NUMBER; i++) {
				newLeft[i] = this.left[i]+left2[i];
				newRight[i] = this.right[i]+right2[i];
			}
			return new FuzzyInterval(this.minVal+fint.minVal,
					this.maxVal+fint.maxVal,
					newLeft, newRight);
		}else if(arg2 instanceof DecomposedFuzzyNumber){
			//this.checkImplementationCompatibilityWith(arg2);
			DecomposedFuzzyNumber dfn=(DecomposedFuzzyNumber)arg2;
			
			double[] left2 = dfn.getLeftCuts();
			double[] right2 = dfn.getRightCuts();
			
			double[] newLeft = new double[this.CUTS_NUMBER];
			double[] newRight = new double[this.CUTS_NUMBER];
			for (int i=0; i<this.CUTS_NUMBER; i++) {
				newLeft[i] = this.left[i]+left2[i];
				newRight[i] = this.right[i]+right2[i];
			}
			return new FuzzyInterval(this.minVal+dfn.getModalValue(),
					this.maxVal+dfn.getModalValue(),
					newLeft, newRight);
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
		double[] newLeft = new double[this.CUTS_NUMBER];
		double[] newRight = new double[this.CUTS_NUMBER];
		for (int i=0; i<this.CUTS_NUMBER; i++) {
			newLeft[i] = this.left[i]+arg2;
			newRight[i] = this.right[i]+arg2;
		}
		return new FuzzyInterval(this.minVal+arg2,
				this.maxVal+arg2, newLeft,newRight);
	}

	@Override
	public FuzzySet multiplyScalar(double arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FuzzySet getMultiplicationInverse() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double getMembershipValue(double universalValue) {
		if(universalValue==this.minVal){
			return 1;
		}
		if (universalValue<this.minVal && universalValue>=this.getLeftBoundary()){
			if(this.left[0]==0){ //Para controlar los fuzzy numbers que acaban cortados en el 0
				int i=0;
				while(i<this.CUTS_NUMBER && left[i]==0){
					i++;
				}
				return (double)(i-1)/this.CUTS_NUMBER;
			}
			
		}
		if (universalValue>=this.getRightBoundary() || universalValue<=this.getLeftBoundary()) return 0d;
		if (universalValue>=this.minVal && universalValue<=this.maxVal) return 1d; // modal value
		
		// TODO: nacrtati, proveriti
		if (universalValue< this.minVal) { // left side
			double previous = this.getLeftBoundary();
			for (int i=0; i<this.left.length; i++) {
				if (this.left[i] >= universalValue) {
					return (i==0 ? 0d : (double)(i-1))/(double)this.CUTS_NUMBER + (1d/(double)this.CUTS_NUMBER) *
					(universalValue-previous) / (this.left[i]-previous);
				}
				else
					previous=this.left[i];
			}
			
		}
		else if(universalValue> this.maxVal){ // on the right side
			double previous = this.getModalValue();
			
			for (int i=0; i<this.right.length; i++) {
				if (this.right[i] <= universalValue) {
					// LINEAR APPROXIMATION IN BETWEEN INTERVAL
					// TODO: change here if sampling isn't equidistant anymore
					return (i==0 ? 0d : (double)(i-1))/(double)this.CUTS_NUMBER + (1d/(double)this.CUTS_NUMBER) *
							(universalValue-previous) / (this.right[i]-previous);
				}
				else
					previous= this.right[i];
			}
		
		}
		return -1; // unreachable anyway
	}

	@Override
	public double getModalValue() {
		return (this.minVal+this.maxVal)/2;
	}

	@Override
	public double getLeftBoundary() {
		return this.left[0];
	}

	@Override
	public double getRightBoundary() {
		return this.right[0];
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

	/*@Override
	public FuzzySet distance(FuzzySet arg2) {
		// TODO Auto-generated method stub
		return null;
	}*/
	
	public FuzzySet distance(DecomposedFuzzyNumber arg2){
		if(arg2 instanceof FuzzySet){
			return distance((FuzzySet)arg2);
		}
		return distance(arg2);
	}
	
	public FuzzySet distance(FuzzySet arg2) {
		double minCrisp = -1,maxCrisp = -1;
		if(arg2 instanceof CrispSet){
			minCrisp = arg2.getLeftBoundary();
			maxCrisp = arg2.getRightBoundary();
		}else if(arg2 instanceof FuzzyInterval){
			FuzzyInterval fi = (FuzzyInterval)arg2;
			minCrisp = fi.minVal;
			maxCrisp = fi.maxVal;
		}
		double newMin=-1, newMax=-1;
		if(arg2 instanceof FuzzyInterval || arg2 instanceof CrispSet){
			newMin = this.minVal - maxCrisp;
			newMax = this.maxVal - minCrisp;
 		}else if(arg2 instanceof DecomposedFuzzyNumber){ 
 			DecomposedFuzzyNumber dfn = (DecomposedFuzzyNumber)arg2;
			newMin = this.minVal - dfn.getModalValue();
			newMax = this.maxVal - dfn.getModalValue();
		}
		//Reordenacion y desdoblado
		if(newMin<0 && newMax<0){
			double oldNewMax = newMax;
			newMax = Math.abs(newMin);
			newMin = Math.abs(oldNewMax);
		}else if(newMin<0 && newMax>=0){
			newMax = Math.max(newMax, Math.abs(newMin));
			newMin = 0;
		}
		//Calculo de las ramas izquierda y derecha
		double[] resp_left = new double[CUTS_NUMBER];
		double[] resp_right = new double[CUTS_NUMBER];
		for(int i=0; i<CUTS_NUMBER; i++){
			double ac_left_1 = left[i];
			double ac_right_1 = right[i];
			double ac_left_2=-1, ac_right_2=-1;
			if(arg2 instanceof CrispSet){
				ac_left_2 = minCrisp;
				ac_right_2 = maxCrisp;
			}else if(arg2 instanceof FuzzyInterval){
				FuzzyInterval fi = (FuzzyInterval)arg2;
				ac_left_2 = fi.left[i];
				ac_right_2 = fi.right[i];
			}else if(arg2 instanceof DecomposedFuzzyNumber){
				DecomposedFuzzyNumber dfn = (DecomposedFuzzyNumber)arg2;
				ac_left_2 = dfn.left[i];
				ac_right_2 = dfn.right[i];
			}
			double[] distances = new double[4];
			distances[0] = Math.abs(ac_left_1 - ac_left_2);
			distances[1] = Math.abs(ac_left_1 - ac_right_2);
			distances[2] = Math.abs(ac_right_1 - ac_left_2);
			distances[3] = Math.abs(ac_right_1 - ac_right_2);
			if(super.overlap(ac_left_1, ac_right_1, ac_left_2, ac_right_2)){
				resp_left[i] = 0;
			}else{
				resp_left[i] = getMin(distances);
			}
			resp_right[i] = getMax(distances);
		}
		return new FuzzyInterval(newMin, newMax, resp_left,resp_right);
	}

	@Override
	public FuzzySet substractScalar(double arg2) {
		return this.addScalar(arg2*(-1));
	}
	
	public FuzzySet distrust(float minDistrust, float maxDistrust){
		double newMinVal = this.minVal-(this.minVal*minDistrust);
		double newMaxVal = this.maxVal+((10-this.maxVal)*maxDistrust);
		double[] newLeft = this.left.clone();
		double[] newRight = this.right.clone();
		double incr = ((double)1)/newLeft.length;
		double acum=1;
		for(int i=0; i<newLeft.length; i++){
			newLeft[i] -= (newLeft[i]*minDistrust);
			acum-=incr;
		}
		newLeft[newLeft.length-1] = newMinVal;
		acum=1;
		for(int i=0; i<newRight.length; i++){
			newRight[i] += ((10-newRight[i])*maxDistrust);
			acum-=incr;
		}
		newRight[newRight.length-1] = newMaxVal;
		return new FuzzyInterval(newMinVal, newMaxVal, newLeft, newRight);
	}

	@Override
	public FuzzySet sqrt() {
		double[] newLeft = new double[this.CUTS_NUMBER];
		double[] newRight = new double[this.CUTS_NUMBER];
		for(int i=0; i<this.CUTS_NUMBER; i++){
			newLeft[i] = Math.sqrt(left[i]);
			newRight[i] = Math.sqrt(right[i]);
		}
		return new FuzzyInterval(Math.sqrt(this.minVal), Math.sqrt(this.maxVal), newLeft, newRight);
	}
	
	public FuzzySet divideScalar(double arg2) {
		double[] newLeft = this.left;
		double[] newRight = this.right;
		for(int i=0; i<this.left.length; i++){
			newLeft[i]/=arg2;
		}
		for(int i=0; i<this.right.length; i++){
			newRight[i]/=arg2;
		}
		return new FuzzyInterval(
				this.minVal/arg2,
				this.maxVal/arg2,
				newLeft, newRight);
	}
	
	public String toString() {
		DecimalFormat dec = new DecimalFormat("#.##");
		float right = Float.parseFloat(dec.format(this.getRightBoundary()).replace(',', '.'));
		float left = Float.parseFloat(dec.format(this.getLeftBoundary()).replace(',', '.'));
		//java.text.NumberFormat df = java.text.DecimalFormat.getNumberInstance();
		return "[" +left+", " + 
			dec.format(this.minVal).replace(',', '.')+", "+
			dec.format(this.maxVal).replace(',', '.')+", "+
			right+"]";
	}

}
