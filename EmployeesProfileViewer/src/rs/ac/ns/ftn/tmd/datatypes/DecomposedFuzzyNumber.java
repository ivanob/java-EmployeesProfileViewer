/**
 * 
 */
package rs.ac.ns.ftn.tmd.datatypes;

import java.text.DecimalFormat;

import rs.ac.ns.ftn.tmd.fuzzy.exceptions.FuzzyDivisionByZeroException;
import rs.ac.ns.ftn.tmd.fuzzy.exceptions.UncompatibileFuzzyArithmeticsException;

/**
 * 
 * Implementation of decomposed fuzzy numbers and fuzzy arithmetics between them
 * 
 * 
 * @author Nikola Kolarovic <nikola.kolarovic@gmail.com>
 *
 */


public class DecomposedFuzzyNumber implements FuzzySet {
	
	protected int CUTS_NUMBER;
	
	protected double left[];
	protected double right[];
	private double alpha, beta;
	private int funcLeft, funcRight;
	

	/** Factory constructor for L-R fuzzy numbers
	 * 
	 * @param modalValue central value of a fuzzy number
	 * @param alpha worst-case interval <b>left</b> of modal value
	 * @param beta worst-case interval <b>right</b> of modal value
	 * @param Ltype type of function for Left function
	 * @param Rtype type of function for Right function
	 * @param cuts_number number of alpha sub-intervals
	 */ 
	public DecomposedFuzzyNumber(double modalValue, double alpha, double beta, int Ltype, int Rtype, int cuts_number ) {
		this.CUTS_NUMBER = cuts_number;

		this.left = new double[this.CUTS_NUMBER];
		this.right = new double[this.CUTS_NUMBER];
		this.alpha=alpha;
		this.beta=beta;
		this.funcRight=Rtype;
		this.funcLeft=Ltype;
		
		this.left[this.CUTS_NUMBER-1] = this.right[this.CUTS_NUMBER-1] = modalValue;
		
		// linear alpha-cutting
		for (int i=0; i<this.CUTS_NUMBER-1; i++) {
			this.left[i] = modalValue-this.getAlphaCut((double)i/(double)this.CUTS_NUMBER, alpha, Ltype);
			this.right[i] = modalValue+this.getAlphaCut((double)i/(double)this.CUTS_NUMBER, beta, Rtype);
		}
	}
	
	public int getLeftFunction(){
		return this.funcLeft;
	}
	
	public int getRightFunction(){
		return this.funcRight;
	}
	
	
	/**
	 * Protected constructor for interior use; to get number from modified alpha-cuts
	 * @param left left points
	 * @param right right points
	 */
	protected DecomposedFuzzyNumber(double[] left, double[] right) {
		assert( left.length == right.length );
		this.CUTS_NUMBER = left.length;
		this.left = left;
		this.right = right;
	}
	
	public double[] getAlphaCut(double cut){
		double[] res = new double[2];
		int pos = (int)(cut*CUTS_NUMBER);
		res[0] = this.left[pos];
		res[1] = this.right[pos];
		return res;
	}
	
	// -------------------------- INTERNAL FUNCTIONS ------------------------------------------
	
	/** Calculates alpha-cut value; uses the inverse function of a membership function
	 * 
	 * @param membershipGrade membership function value; "alpha" in alpha-cut
	 * @param spread spread of the function, max value
	 * @param type type of the function
	 * @return left or right member of alpha-cut interval
	 */ 
	public double getAlphaCut(double membershipGrade, double spread, int type) {
		
		// TODO: correct when adding a new function type
		assert (type>=0 && type<= FuzzySet.FUNCTION_EXPONENTIAL);
		
		assert (spread>=0);
		assert (membershipGrade>=0 && membershipGrade<1);
		
		// if there is no spread then every value is modalValue
		if (spread==0) return 0;
		if (membershipGrade==0) return spread;

		// depending on type
		switch (type) {
			case FuzzySet.FUNCTION_LINEAR :
							return (1d-membershipGrade)*spread;
			
			case FuzzySet.FUNCTION_GAUSSIAN :
							// x = SQRT(  - ln(y)* 2* variance^2  )
							double x = Math.sqrt( Math.log(membershipGrade)*(-2d)*(spread*spread/9d));
							// TODO: for some high CUTS_NUMBER value this can get bigger than spread,
							// which is an artificial boundary (because function is infinite)
							if (x>spread) return spread;
								else return x;
			
			case FuzzySet.FUNCTION_QUADRATIC :
							return spread*Math.sqrt(1-membershipGrade);

			case FuzzySet.FUNCTION_EXPONENTIAL :
							// X =  - ln(y)*variance
							double x2 = -1d*Math.log( membershipGrade )*spread/4.5d;
							// TODO: for some high CUTS_NUMBER value this can get bigger than spread,
							// which is an artificial boundary (because function is infinite)
							if (x2>spread) return spread;
								else return x2;
							
							
			default : return -1;
		}
	}
	
	
	/** package-visible getter for left alpha-cuts
	 * 
	 * @return left array
	 */
	double[] getLeftCuts() {
		return this.left;
	}
	
	/** package-visible getter for right alpha-cuts
	 * 
	 * @return right array
	 */
	double[] getRightCuts() {
		return this.right;
	}
	
	
	/** checks if two fuzzy number interpretations are same.<br>
	 *  If not, throw a <b>UncompatibileFuzzyArithmeticsException</b> with a message.
	 * @param second compare type of current object with the "second" one.
	 */
	protected void checkImplementationCompatibilityWith(FuzzySet second) {
		
		if (this.CUTS_NUMBER != ((DecomposedFuzzyNumber)second).CUTS_NUMBER) // check for array size
			throw new UncompatibileFuzzyArithmeticsException("Incompatibile decomposed fuzzy numbers - CUTS number is different: "+this.CUTS_NUMBER+" and "+((DecomposedFuzzyNumber)second).CUTS_NUMBER);
			
		if ( ! this.getClass().equals(second.getClass()))
			throw new UncompatibileFuzzyArithmeticsException(this.getClass(),second.getClass());
		
		
	}
	
	
	//------------------------- ARITHMETIC OPERATORS ---------------------------------------------
	
	
	
	/* (non-Javadoc)
	 * @see yu.ac.ns.ftn.tmd.fuzzy.FuzzyNumber#add(yu.ac.ns.ftn.tmd.fuzzy.FuzzyNumber)
	 */
	public FuzzySet add(FuzzySet arg2) {
		if(arg2 instanceof ScalarNumber){
			ScalarNumber sc = (ScalarNumber)arg2;
			double[] newLeft = this.getLeftCuts().clone();
			double[] newRight = this.getRightCuts().clone();
			for(int i=0; i<this.CUTS_NUMBER; i++){
				newLeft[i] += sc.getModalValue();
				newRight[i] += sc.getModalValue();
			}
			return new DecomposedFuzzyNumber(newLeft, newRight);
		}else if(arg2 instanceof CrispSet){
			CrispSet cs = (CrispSet)arg2;
			double[] newLeft = this.getLeftCuts().clone();
			double[] newRight = this.getRightCuts().clone();
			for(int i=0; i<this.CUTS_NUMBER; i++){
				newLeft[i] += cs.getLeftBoundary();
				newRight[i] += cs.getRightBoundary();
			}
			return new FuzzyInterval(
				(this.getModalValue()+cs.getLeftBoundary()),
				(this.getModalValue()+cs.getRightBoundary()),
				newLeft, newRight);
		}else if(arg2 instanceof DecomposedFuzzyNumber){
			this.checkImplementationCompatibilityWith(arg2);
			
			double[] left2 = ((DecomposedFuzzyNumber)arg2).getLeftCuts();
			double[] right2 = ((DecomposedFuzzyNumber)arg2).getRightCuts();
			
			double[] newLeft = new double[this.CUTS_NUMBER];
			double[] newRight = new double[this.CUTS_NUMBER];
			for (int i=0; i<this.CUTS_NUMBER; i++) {
				newLeft[i] = this.left[i]+left2[i];
				newRight[i] = this.right[i]+right2[i];
			}
			return new DecomposedFuzzyNumber(newLeft, newRight);
		}
		return null;
	}

	
	
	/* (non-Javadoc)
	 * @see yu.ac.ns.ftn.tmd.fuzzy.FuzzyNumber#addScalar(double)
	 */
	public FuzzySet addScalar(double arg2) {
		double[] newLeft = new double[this.CUTS_NUMBER];
		double[] newRight = new double[this.CUTS_NUMBER];
		for (int i=0; i<this.CUTS_NUMBER; i++) {
			newLeft[i] = this.left[i]+arg2;
			newRight[i] = this.right[i]+arg2;
		}
		return new DecomposedFuzzyNumber(newLeft,newRight);
	}
	
	

	/* (non-Javadoc)
	 * @see yu.ac.ns.ftn.tmd.fuzzy.FuzzyNumber#getMultiplicationInverse()
	 */
	public FuzzySet getMultiplicationInverse() {
		
		// exception for division by zero
		if ((this.getModalValue() >= 0 && this.getLeftBoundary()<=0) || 
				(this.getModalValue() <= 0 && this.getRightBoundary()>=0))
			throw new FuzzyDivisionByZeroException();

		
		double[] newLeft = new double[this.CUTS_NUMBER];
		double[] newRight = new double[this.CUTS_NUMBER];
			
		for (int i=0; i<this.CUTS_NUMBER; i++) {
			
			
			newLeft[i] = Math.min(1/this.left[i],
			        			1/this.right[i]);
	
			newRight[i] = Math.max(
			        		 1/this.left[i],
			        		 1/this.right[i]);
		}
		return new DecomposedFuzzyNumber(newLeft,newRight);
		
	}
	
	
	/* (non-Javadoc)
	 * @see yu.ac.ns.ftn.tmd.fuzzy.FuzzyNumber#divide(yu.ac.ns.ftn.tmd.fuzzy.FuzzyNumber)
	 */
	public FuzzySet divide(FuzzySet arg2) {
		
		this.checkImplementationCompatibilityWith(arg2);
		
		// exception for division by zero
		if ((arg2.getModalValue() >= 0 && arg2.getLeftBoundary()<=0) || 
				(arg2.getModalValue() <= 0 && arg2.getRightBoundary()>=0))
			throw new FuzzyDivisionByZeroException();
		
		
		double[] left2 = ((DecomposedFuzzyNumber)arg2).getLeftCuts();
		double[] right2 = ((DecomposedFuzzyNumber)arg2).getRightCuts();
		
		double[] newLeft = new double[this.CUTS_NUMBER];
		double[] newRight = new double[this.CUTS_NUMBER];
		for (int i=0; i<this.CUTS_NUMBER; i++) {

			newLeft[i] = Math.min(
					        Math.min(this.left[i]/left2[i],
					        		 this.left[i]/right2[i]),
					        Math.min(this.right[i]/left2[i],
					        		 this.right[i]/right2[i]));
			
			newRight[i] = Math.max(
					        Math.max(this.left[i]/left2[i],
					        		 this.left[i]/right2[i]),
					        Math.max(this.right[i]/left2[i],
					        		 this.right[i]/right2[i]));

		}
		
		return new DecomposedFuzzyNumber(newLeft, newRight);
	}

	

	/* (non-Javadoc)
	 * @see yu.ac.ns.ftn.tmd.fuzzy.FuzzyNumber#multiply(yu.ac.ns.ftn.tmd.fuzzy.FuzzyNumber)
	 */
	public FuzzySet multiply(FuzzySet arg2) {
		
		this.checkImplementationCompatibilityWith(arg2);
		
		double[] left2 = ((DecomposedFuzzyNumber)arg2).getLeftCuts();
		double[] right2 = ((DecomposedFuzzyNumber)arg2).getRightCuts();
		
		double[] newLeft = new double[this.CUTS_NUMBER];
		double[] newRight = new double[this.CUTS_NUMBER];
		for (int i=0; i<this.CUTS_NUMBER; i++) {
			newLeft[i] = Math.min(
					        Math.min(this.left[i]*left2[i],
					        		 this.left[i]*right2[i]),
					        Math.min(this.right[i]*left2[i],
					        		 this.right[i]*right2[i]));
					        		 
			newRight[i] = Math.max(
					        Math.max(this.left[i]*left2[i],
					        		 this.left[i]*right2[i]),
					        Math.max(this.right[i]*left2[i],
					        		 this.right[i]*right2[i]));

		}
		
		return new DecomposedFuzzyNumber(newLeft, newRight);
	}

	/* (non-Javadoc)
	 * @see yu.ac.ns.ftn.tmd.fuzzy.FuzzyNumber#multiplyScalar(double)
	 */
	public FuzzySet multiplyScalar(double arg2) {
		double[] newLeft = new double[this.CUTS_NUMBER];
		double[] newRight = new double[this.CUTS_NUMBER];
		for (int i=0; i<this.CUTS_NUMBER; i++) {
			newLeft[i] = this.left[i]*arg2;
			newRight[i] = this.right[i]*arg2;
		}
		if (arg2>0) // if positive then everything is OK
			return new DecomposedFuzzyNumber(newLeft,newRight);
		else // inverse the sides
			return new DecomposedFuzzyNumber(newRight, newLeft);

	}

	
	
	/* (non-Javadoc)
	 * @see yu.ac.ns.ftn.tmd.fuzzy.FuzzyNumber#substract(yu.ac.ns.ftn.tmd.fuzzy.FuzzyNumber)
	 */
	public FuzzySet substract(FuzzySet arg2) {
		this.checkImplementationCompatibilityWith(arg2);
		
		double[] left2 = ((DecomposedFuzzyNumber)arg2).getLeftCuts();
		double[] right2 = ((DecomposedFuzzyNumber)arg2).getRightCuts();
		
		double[] newLeft = new double[this.CUTS_NUMBER];
		double[] newRight = new double[this.CUTS_NUMBER];
		for (int i=0; i<this.CUTS_NUMBER; i++) {
			newLeft[i] = this.left[i]-right2[i];
			newRight[i] = this.right[i]-left2[i];
		}
		
		return new DecomposedFuzzyNumber(newLeft, newRight);
	}
	
	//------------------------- ADDITIONAL FUNCITONS ---------------------------------------------
	
	/* (non-Javadoc)
	 * @see yu.ac.ns.ftn.tmd.fuzzy.FuzzyNumber#getMembershipValue(double)
	 */
	public double getMembershipValue(double universalValue) {
		//if (universalValue >= this.getRightBoundary() || universalValue <= this.getLeftBoundary()) return 0d; // outside of the support
		if (universalValue == this.getModalValue()) return 1d; // modal value
		if (universalValue >= this.getRightBoundary() || universalValue <= this.getLeftBoundary()){
			if(left[1]!=0 || left[this.CUTS_NUMBER-2]==0){
				return 0d; // outside of the support
			}else{ //Para controlar los fuzzy numbers que acaban cortados en el 0
				int i=0;
				while(left[i]==0){
					i++;
				}
				return (double)(i-1)/this.CUTS_NUMBER;
			}
			
		}
		
		
		// TODO: nacrtati, proveriti
		if (universalValue< this.getModalValue()) { // left side
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
		else { // on the right side
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

	
	
	/* (non-Javadoc)
	 * @see yu.ac.ns.ftn.tmd.fuzzy.FuzzyNumber#getModalValue()
	 */
	public double getModalValue() {
		//return this.left[this.left.length-1];
		return this.right[this.right.length-1];
	}


	/* (non-Javadoc)
	 * @see yu.ac.ns.ftn.tmd.fuzzy.FuzzyNumber#getLeftBoundary()
	 */
	public double getLeftBoundary() {
		return this.left[0];
	}

	/* (non-Javadoc)
	 * @see yu.ac.ns.ftn.tmd.fuzzy.FuzzyNumber#getLeftSpread()
	 */
	public double getLeftSpread() {
		return this.getModalValue() - this.getLeftBoundary();
	}
	
	/* (non-Javadoc)
	 * @see yu.ac.ns.ftn.tmd.fuzzy.FuzzyNumber#getRightBoundary()
	 */
	public double getRightBoundary() {
		return this.right[0];
	}

	/* (non-Javadoc)
	 * @see yu.ac.ns.ftn.tmd.fuzzy.FuzzyNumber#getRightSpread()
	 */
	public double getRightSpread() {
		return this.getRightBoundary()-this.getModalValue();
	}

	
	@Override
	public String toString() {
		DecimalFormat dec = new DecimalFormat("#.##");
		float right = Float.parseFloat(dec.format(this.getModalValue()).replace(',', '.')) +
				Float.parseFloat(dec.format(this.getRightSpread()).replace(',', '.'));
		float left = Float.parseFloat(dec.format(this.getModalValue()).replace(',', '.')) -
				Float.parseFloat(dec.format(this.getLeftSpread()).replace(',', '.'));
		//java.text.NumberFormat df = java.text.DecimalFormat.getNumberInstance();
		return "[" +left+", "
		+dec.format(this.getModalValue()).replace(',', '.')+", "+
		right+"]";
	}
	
	/*protected boolean overlap(double left1, double right1, double left2, double right2){
		boolean itOverlap = false;
		if(left1<=right2 && left1>=left2){
			itOverlap = true;
		}
		if(right1<=right2 && right1>=left2){
			itOverlap = true;
		}
		return itOverlap;
	}*/
	
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

	/* Metodo añadido por mi que calcula la distancia entre 2 
	 * DecomposedFuzzyNumber. La distancia entre 2 fuzzyNumbers
	 * da como resultado otro fuzzyNumber en el eje positivo,
	 * porque la distancia entre 2 conjuntos (o entre 2 puntos)
	 * nunca puede ser negativa. Una distancia siempre es un
	 * valor natural.
	 * 
	 * (non-Javadoc)
	 * @see rs.ac.ns.ftn.tmd.fuzzy.FuzzyNumber#distance(rs.ac.ns.ftn.tmd.fuzzy.FuzzyNumber)
	 */
	/*@Override
	public FuzzySet distance(FuzzySet arg2) {
		double[] left2 = ((DecomposedFuzzyNumber)arg2).getLeftCuts();
		double[] right2 = ((DecomposedFuzzyNumber)arg2).getRightCuts();
		double[] resp_left = new double[CUTS_NUMBER];
		double[] resp_right = new double[CUTS_NUMBER];
		for(int i=0; i<CUTS_NUMBER; i++){
			double ac_left_1 = left[i];
			double ac_right_1 = right[i];
			double ac_left_2 = left2[i];
			double ac_right_2 = right2[i];
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
		return new DecomposedFuzzyNumber(resp_left, resp_right);
	}*/
	
	protected boolean overlap(double left1, double right1, double left2, double right2){
		boolean itOverlap = false;
		if((left1<=right2 && left1>=left2) || (right1<=right2 && right1>=left2)
				|| (left2<=right1 && left2>=left1) || (right2<=right1 && right2>=left1)){
			itOverlap = true;
		}
		return itOverlap;
	}
	
	public FuzzySet distance(DecomposedFuzzyNumber arg2){
		double newMin=-1, newMax=-1, modal=-1;
		if(arg2 instanceof FuzzyInterval){
			/* The fuzzyInterval have min value and max value which
			membership values 1 */
			newMin = this.getModalValue() - ((FuzzyInterval)arg2).maxVal;
			newMax = this.getModalValue() - ((FuzzyInterval)arg2).minVal;
			//Hay un intervalo=1
			if(newMin<0 && newMax<0){
				double oldNewMax = newMax;
				newMax = Math.abs(newMin);
				newMin = Math.abs(oldNewMax);
			}else if(newMin<0 && newMax>=0){
				newMax = Math.max(newMax, Math.abs(newMin));
				newMin = 0;
			}
		}else if(arg2 instanceof DecomposedFuzzyNumber){
			/* FuzzyNumbers only have a modal value */
			modal = this.getModalValue() - arg2.getModalValue();
			//Solo un punto=1
			modal = Math.abs(modal);
		}	
		//Calculo de las ramas
		double[] left = arg2.left;
		double[] right = arg2.right;
		double[] resp_left = new double[arg2.CUTS_NUMBER];
		double[] resp_right = new double[arg2.CUTS_NUMBER];
		for(int i=0; i<arg2.CUTS_NUMBER; i++){
			double ac_left_1 = this.left[i];
			double ac_right_1 = this.right[i];
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
		if(arg2 instanceof FuzzyInterval){
			return new FuzzyInterval(newMin, newMax, resp_left, resp_right);
		}else if(arg2 instanceof DecomposedFuzzyNumber){
			return new DecomposedFuzzyNumber(resp_left, resp_right);
		}
		return null;
	}
	
	@Override
	public FuzzySet distance(FuzzySet arg2) {
		if(arg2 instanceof DecomposedFuzzyNumber){
			return this.distance((DecomposedFuzzyNumber)arg2);
		}
		else if(arg2 instanceof ScalarNumber){
			double scalar = ((ScalarNumber)arg2).getModalValue();
			double[] newLeft, newRight;
			newLeft = new double[CUTS_NUMBER];
			newRight = new double[CUTS_NUMBER];
			for(int i=0; i<CUTS_NUMBER; i++){
				double min = Math.abs(left[i]-scalar);
				double max = Math.abs(right[i]-scalar);
				if(min>max){
					newLeft[i] = max;
					newRight[i] = min;
				}else{
					newLeft[i] = min;
					newRight[i] = max;
				}
				/*newLeft[i] = Math.abs(left[i]-scalar);
				newRight[i] = Math.abs(right[i]-scalar);*/
				if(i>0 && newLeft[i-1]>newLeft[i]){
					newLeft[i-1] = 0;
				}
			}
			return new DecomposedFuzzyNumber(newLeft, newRight);
		}
		else if(arg2 instanceof CrispSet){
			return ((CrispSet)arg2).distance(this);
		}
		return null;
	}


	@Override
	public FuzzySet substractScalar(double arg2) {
		return this.addScalar(arg2*(-1));
	}

	@Override
	public FuzzySet sqrt() {
		for(int i=0; i<this.CUTS_NUMBER; i++){
			left[i] = Math.sqrt(left[i]);
			right[i] = Math.sqrt(right[i]);
		}
		return new DecomposedFuzzyNumber(left, right);
	}

	@Override
	public FuzzySet pow(int pot) {
		double[] newLeft = new double[this.CUTS_NUMBER];
		double[] newRight = new double[this.CUTS_NUMBER];
		for(int i=0; i<this.CUTS_NUMBER; i++){
			newLeft[i] = Math.pow(this.left[i], pot);
			newRight[i] = Math.pow(this.right[i], pot);
		}
		return new DecomposedFuzzyNumber(newLeft, newRight);
	}


	@Override
	public FuzzySet divideScalar(double arg2) {
		double[] newLeft = this.left;
		double[] newRight = this.right;
		for(int i=0; i<this.left.length; i++){
			newLeft[i]/=arg2;
		}
		for(int i=0; i<this.right.length; i++){
			newRight[i]/=arg2;
		}
		return new DecomposedFuzzyNumber(newLeft, newRight);
	}


	@Override
	public boolean existsImprecision() {
		return true;
	}

	@Override
	public boolean existsFuzzyImprecision() {
		return true;
	}
	
	public FuzzySet distrust(float minDistrust, float maxDistrust){
		double[] newLeft = this.left.clone();
		double[] newRight = this.right.clone();
		double originModal = right[right.length-1];
		double incr = ((double)1)/newLeft.length;
		double acum=1;
		for(int i=0; i<newLeft.length; i++){
			newLeft[i] = newLeft[i] - (newLeft[i]*minDistrust)*acum;
			acum-=incr;
		}
		newLeft[newLeft.length-1] = originModal;
		acum=1;
		for(int i=0; i<newRight.length; i++){
			newRight[i] = newRight[i] + ((10-newRight[i])*maxDistrust)*acum;
			acum-=incr;
		}
		newRight[newRight.length-1] = originModal;
		return new DecomposedFuzzyNumber(newLeft, newRight);
	}

	@Override
	public String getStrType() {
		return "fuzzy";
	}
}
