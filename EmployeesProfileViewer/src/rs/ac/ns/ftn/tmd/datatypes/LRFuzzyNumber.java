/**
 * 
 */
package rs.ac.ns.ftn.tmd.datatypes;

import rs.ac.ns.ftn.tmd.fuzzy.exceptions.FuzzyDivisionByZeroException;
import rs.ac.ns.ftn.tmd.fuzzy.exceptions.FuzzyMultiplicationByZeroException;
import rs.ac.ns.ftn.tmd.fuzzy.exceptions.UncompatibileFuzzyArithmeticsException;
import rs.ac.ns.ftn.tmd.fuzzy.membershipFunction.*;

/**
 * Implementation of L-R fuzzy numbers and arithmetics between them
 * 
 * @author Nikola Kolarovic <nikola.kolarovic@gmail.com>
 *
 */
public class LRFuzzyNumber implements FuzzySet {
		
	/** modal or central value, value in which membership function reaches value 1 */
	protected double modalValue;

	/** left function */
	protected LRMembershipFunction Lfunction = null;
	
	/** right function */
	protected LRMembershipFunction Rfunction = null;
	

	
	/** Constructor for L-R fuzzy numbers
	 * 
	 * @param modalValue central value of a fuzzy number
	 * @param alpha worst-case interval <b>left</b> of modal value
	 * @param beta worst-case interval <b>right</b> of modal value
	 * @param Ltype type of function for Left function
	 * @param Rtype type of function for Right function
	 */ 
	public LRFuzzyNumber(double modalValue, double alpha, double beta, int Ltype, int Rtype) {
		
		this.modalValue = modalValue;
		this.Lfunction = this.getMembershipFunction(alpha, Ltype);
		this.Rfunction = this.getMembershipFunction(beta, Rtype);
		
	}

	
	/** Class-visible constructor for internal use, it sets attributes to proper values
	 * 
	 * @param modalValue modal value
	 * @param Lfunction L-function
	 * @param Rfunction R-function
	 */
	LRFuzzyNumber(double modalValue, LRMembershipFunction Lfunction, LRMembershipFunction Rfunction) {
		assert(Lfunction!=null && Rfunction!=null);
		
		this.modalValue = modalValue;
		this.Lfunction = Lfunction;
		this.Rfunction = Rfunction;
	}
	
	
	// -------------------------------- HELPER FUNCTIONS ---------------------------------
	
	/** Function that instantiates proper membership function depending on its type.<br>
	 * 
	 * @param worstCaseInterval "width" of the function
	 * @param type type of the function. Use static values from <b>FuzzyNumber</b> class.
	 * @return membership function according to parameters
	 */
	protected LRMembershipFunction getMembershipFunction(double worstCaseInterval, int type) {
		
		// correct when adding a new function type
		assert (type>=0 && type<= FuzzySet.FUNCTION_EXPONENTIAL);
		
		switch (type) {
			case FuzzySet.FUNCTION_LINEAR : return new LRMembershipFunctionLinear(worstCaseInterval);
			
			case FuzzySet.FUNCTION_GAUSSIAN : return new LRMembershipFunctionGaussian(worstCaseInterval);
			
			case FuzzySet.FUNCTION_QUADRATIC : return new LRMembershipFunctionQuadratic(worstCaseInterval);

			case FuzzySet.FUNCTION_EXPONENTIAL : return new LRMembershipFunctionExponential(worstCaseInterval);
		}
		return null;
	}
	
	
	/** checks if membership functions are of the same type
	 * @throws <code>UncompatibileFuzzyArithmeticsException</code> if L-R functions are not compatibile
	 * @param f1 first function
	 * @param f2 second function
	 */
	// was : 	 * @return <i>true</i> if types are equal; <i>false</i> otherwise
	protected void checkFunctionCompatibility(LRMembershipFunction f1, LRMembershipFunction f2) {
		assert (f1!=null && f2!=null);
		
		if (!f1.getClass().equals(f2.getClass())) 
			throw new UncompatibileFuzzyArithmeticsException(f1.getClass(), f2.getClass());
		
	}

	
	/** getter for Lfunction
	 * @return Left membership function
	 */
	protected LRMembershipFunction getLfunction() {
		return this.Lfunction;
	}
	
	/** getter for Rfunction
	 * @return Right membership function
	 */
	protected LRMembershipFunction getRfunction() {
		return this.Rfunction;
	}
	

	
	/** checks if two fuzzy number interpretations are same.<br>
	 *  If not, throw a <b>UncompatibileFuzzyArithmeticsException</b> with a message.
	 * @param second compare type of current object with the "second" one.
	 */
	protected void checkImplementationCompatibilityWith(FuzzySet second) {
		if ( ! (second instanceof LRFuzzyNumber) )
			throw new UncompatibileFuzzyArithmeticsException(this.getClass(),second.getClass());
		
	}
	
	
	/** checks if FuzzyNumber is completely positive or negative
	 * 
	 * @return -1 if negative; 1 if positive; 0 if it spreads over 0.
	 */
	protected int isPositive() {
		if (this.modalValue > 0) { // should be positive
			if (this.modalValue - this.Lfunction.getSpread() > 0 ) return 1; // positive
				else return 0; // otherwise it spreads over zero
		}
		else
			if (this.modalValue < 0) { // should be negative
				if (this.modalValue + this.Rfunction.getSpread() < 0) return -1; // negative
					else return 0; // otherwise it spreads over zero
			}
		return 0; // modalvalue == 0
	}
	
	// -------------------------------- ARITHMETIC FUNCTIONS ---------------------------------
	
	
	  /** Fuzzy operation for <b>addition</b> of two fuzzy numbers. <br>
	   * First argument for the operation is the current object, and second argument
	   * is given as a function parameter.<br>
	   * @throws <code>UncompatibileFuzzyArithmeticsException</code> if fuzzy number implementations are not compatibile
	   * @param arg2 second argument for the operation
	   * @return number which is the result of the operation
	   */
	public FuzzySet add(FuzzySet arg2) {
		// if implementations are the same?
		this.checkImplementationCompatibilityWith(arg2);
		
		// direct function compatibility
		LRMembershipFunction l = (LRMembershipFunction) (((LRFuzzyNumber)arg2).getLfunction().clone());
		LRMembershipFunction r = (LRMembershipFunction) (((LRFuzzyNumber)arg2).getRfunction().clone());
		checkFunctionCompatibility(this.Lfunction, l);
		checkFunctionCompatibility(this.Rfunction, r);
		
		l.setSpread(this.Lfunction.getSpread()+l.getSpread());
		r.setSpread(this.Rfunction.getSpread()+r.getSpread());
		
		return new LRFuzzyNumber(this.modalValue+arg2.getModalValue(), l, r);
	}

	


	  /** Fuzzy operation for multiplication between current FuzzyNumber and a crisp (<i>double</i>) value. <br>
	   * First argument for the operation is the current object, and second argument
	   * is given as a function parameter.
	   * @throws <code>UncompatibileFuzzyArithmeticsException</code> if fuzzy number implementations are not compatibile
	   * @param arg2 constant which will be multiplied as a crisp number
	   * @return number which is the result of the operation
	   */	  
	public FuzzySet substract(FuzzySet arg2) {
		
		return this.add(arg2.multiplyScalar(-1d));
		
	}
	
	
	  /** Fuzzy operation for <b>multiplication</b> of two fuzzy numbers. <br>
	   * First argument for the operation is the current object, and second argument
	   * is given as a function parameter.<br>
	   * For this function it is very important that numbers don't have zero in their confidence
	   * intervals, because the multiplication operation is not defined in that case.<br><br>
	   * This operation uses <b>secant approximation</b> to keep the group closed (L-R).<br><br>
	   * 
	   * @throws <code>FuzzyMultiplicaitonByZeroException</code> if one of the interval contains zero (0) value in universal set - operation is not valid.<br><br>
	   * @throws <code>UncompatibileFuzzyArithmeticsException</code> if fuzzy number implementations are not compatibile
	   * @throws <code>UncompatibileFuzzyArithmeticsException</code> if fuzzy number implementations are not compatibile<br><br>
	   * 
	   * @param arg2 second argument for the operation
	   * @return number which is the result of the operation
	   */
	public FuzzySet multiply(FuzzySet arg2) {
		// if implementations are the same?
		this.checkImplementationCompatibilityWith(arg2);
		
		
		// first, if one contains zero in confidence interval, LR-fuzzy numbers don't support that!
		if (((LRFuzzyNumber)this).isPositive()==0 || ((LRFuzzyNumber)arg2).isPositive()==0 ) {
			throw new FuzzyMultiplicationByZeroException();
		}

		// function compatibility depends on number "sign"
		LRMembershipFunction l=(LRMembershipFunction) (((LRFuzzyNumber)arg2).getLfunction().clone());
		LRMembershipFunction r=(LRMembershipFunction) (((LRFuzzyNumber)arg2).getRfunction().clone());
		
		if ( this.isPositive()>0 && ((LRFuzzyNumber)arg2).isPositive()>0  ) {
			// direct compatibility; solution is L,R
			checkFunctionCompatibility(this.Lfunction, l);
			checkFunctionCompatibility(this.Rfunction, r);
			// if BOTH POSITIVE, multiplication is, in secant approximation:
			// q = ( x1*x2,   x1*alpha2 + x2*alpha1 - alpha1*alpha2,  x1*beta2 + x2*beta1 + beta1*beta2 )
			l.setSpread(
					this.getModalValue()*l.getSpread() +
					arg2.getModalValue()*this.getLfunction().getSpread() -
					this.getLfunction().getSpread()*l.getSpread());
			r.setSpread(
					this.getModalValue()*r.getSpread() +
					arg2.getModalValue()*this.getRfunction().getSpread() +
					this.getRfunction().getSpread()*r.getSpread());
			
			return new LRFuzzyNumber(this.modalValue*arg2.getModalValue(), l, r);
			
		}
		else {
			if ( this.isPositive()<0 && ((LRFuzzyNumber)arg2).isPositive()<0  ) {
				// ------------ both negative ------------
				return this.multiplyScalar(-1).multiply(arg2.multiplyScalar(-1));
			}
			else {
				
				// ------ ONE is POSITIVE and ONE is NEGATIVE -------------
				return (this.multiply(arg2.multiplyScalar(-1))).multiplyScalar(-1);

			}
		}
		
	}

    /**	 
     * <b>Basically, it is fuzzy 1/X function.</b><br>
     * Calculates new FuzzyNumber which represents the multiplication inverse fuzzy number for the current one,
     * using the secant approximation.<br>
	 * @throws <code>FuzzyDivisionByZeroException</code> if number contains 0 value in it's domain
	 * @return fuzzy 1/X number
	 */ 
	public FuzzySet getMultiplicationInverse() {
		
		if (this.isPositive()==0) throw new FuzzyDivisionByZeroException();
		
		LRMembershipFunction l = (LRMembershipFunction)this.getLfunction().clone();
		LRMembershipFunction r = (LRMembershipFunction)this.getRfunction().clone();
		// P^-1 =~ [ 1/X, beta/(X*(X+beta))  , alpha/(X*(X-alpha))  ] R,L
		r.setSpread( this.getRightSpread() / ( this.modalValue*(this.modalValue+this.getRightSpread()) ) );
		l.setSpread( this.getLeftSpread() / (this.modalValue*(this.modalValue-this.getLeftSpread())));
		
		return new LRFuzzyNumber( 1d / this.modalValue, r,l);
	}

	
	
	
	/** Fuzzy operation for <b>division</b> of two fuzzy numbers. <br>
	 * First argument for the operation is the current object, and second argument
	 * is given as a function parameter.<br>
	 * @throws <code>UncompatibileFuzzyArithmeticsException</code> if fuzzy number implementations are not compatibile<br>
	 * @throws <code>FuzzyDivisionByZeroException</code> if number's confidence interval contains value 0<br>
	 * @param arg2 second argument for the operation
	 * @return number which is the result of the operation
	 * 
	 * @see rs.ac.ns.ftn.tmd.datatypes.FuzzySet#divide(rs.ac.ns.ftn.tmd.datatypes.FuzzySet)
	 */
	public FuzzySet divide(FuzzySet arg2) {
		return this.multiply(arg2.getMultiplicationInverse());
	}

	
	
	
	  /** Fuzzy operation for addition between current FuzzyNumber and a scalar (<i>double</i>) value. <br>
	   * First argument for the operation is the current object, and second argument
	   * is given as a function parameter.
	   * @param arg2 constant which will be added as a crisp number
	   * @return number which is the result of the operation
	   */	  
	public FuzzySet addScalar(double arg2) {
		return new LRFuzzyNumber(this.modalValue+arg2, this.Lfunction, this.Rfunction);
	}
	
	
		
	
	
   /** Fuzzy operation for multiplication between current FuzzyNumber and a scalar (<i>double</i>) value. <br>
	* First argument for the operation is the current object, and second argument
	* is given as a function parameter.
	* @param arg2 constant which will be multiplied as a crisp number
	* @return number which is the result of the operation
	*/	  
	public FuzzySet multiplyScalar(double arg2) {
		
		LRMembershipFunction l = (LRMembershipFunction) this.Lfunction.clone();
		LRMembershipFunction r = (LRMembershipFunction) this.Rfunction.clone();
		l.setSpread(l.getSpread()*Math.abs(arg2)); // multiply the spreads
		r.setSpread(r.getSpread()*Math.abs(arg2));

		if (arg2>0) {
			return new LRFuzzyNumber(this.modalValue*arg2, l, r);
		}
		else 
			if (arg2<0) {
			return new LRFuzzyNumber(this.modalValue*arg2, r, l);
			}
		
		// TODO: multiplication with crisp 0??? Sure, it is crisp zero [0,0,0]...
		// Theoretical question. Since multiplication with fuzzy zero is not allowed, I will disallow
		// multiplication with crisp zero also.
		if (arg2==0) throw new FuzzyMultiplicationByZeroException();
		
		return new LRFuzzyNumber(this.modalValue*arg2, this.Lfunction, this.Rfunction);

	}

	
	
	/**
	 * Calculates value of fuzzy number's membership function for wanted universal value
	 * 
	 * @see rs.ac.ns.ftn.tmd.datatypes.FuzzySet#getMembershipValue(double)
	 * @return membership function result for given value
	 */
	public double getMembershipValue(double universalValue) {
		if (universalValue > this.modalValue) {
			// R-Function
			return this.Rfunction.getValue( universalValue - this.modalValue );
		}
		
		if (universalValue < this.modalValue) {
			// L-function
			return this.Lfunction.getValue( this.modalValue - universalValue );
			// we need positive value, because L function is defined for positive values
		}
		
		return 1; // else - it's exactly for modal value -> 1
	}

	
	

	/** 
	 * returns the modal value of current fuzzy number
	 */
	public double getModalValue() {
		return this.modalValue;
	}

	
	/**
	 * String representation of a fuzzy number
	 */
	@Override
	public String toString() {
		java.text.NumberFormat df = java.text.DecimalFormat.getNumberInstance();
		return "["+df.format(this.getModalValue())+", "+df.format(this.getLeftSpread())+", "+df.format(this.getRightSpread())+"]";
	}


	
   /**
	* Calculates left boundary of a fuzzy number. It is value of the worst case interval on the left side, 
	* value in which membership function has value 0+.<b>
	* @return left boundary value in universal set X
	*/
	public double getLeftBoundary() {
		return this.modalValue - this.Lfunction.getSpread();
	}

   /**
	* Calculates value of left spread, which is distance from the modal value to the left boundary.
	* @return distance from modal value to left boundary, which is always >0
	*/
	public double getLeftSpread() {
		return this.Lfunction.getSpread();
	}


  /**
	* Calculates right boundary of a fuzzy number. It is value of the worst case interval on the right side,
	* value in which membership function has value 0+.
	* @return right boundary value in universal set X
	*/
	public double getRightBoundary() {
		return this.modalValue + this.Rfunction.getSpread();
	}


	/**
	 * Calculates value of right spread, which is distance from the modal value to the right boundary.
	 * @return distance from modal value to right boundary, which is always >0
	 */
	public double getRightSpread() {
		return this.Rfunction.getSpread();
	}


	private boolean overlap(double[] fuzzy){
		boolean itOverlap = false;
		if(fuzzy[0]<0 && fuzzy[2]>0){
			itOverlap = true;
		}
		return itOverlap;
	}
	
	@Override
	public FuzzySet distance(FuzzySet arg2) {
		double []fuzz1 = new double[3];
		double []fuzz2 = new double[3];
		double []fuzzRes = new double[3];
		fuzz1[0] = this.getRightBoundary();
		fuzz1[1] = this.getModalValue();
		fuzz1[2] = this.getLeftBoundary();
		//Invertidos left y right
		fuzz2[0] = arg2.getRightBoundary() * (-1);
		fuzz2[1] = arg2.getModalValue() * (-1);
		fuzz2[2] = arg2.getLeftBoundary() * (-1);
		for(int i=0; i<3; i++){
			fuzzRes[i] = fuzz1[i] + fuzz2[i];
		}
		if(overlap(fuzzRes)){
			double aux = fuzzRes[0] = 0;
			aux *= -1;
			if(aux>fuzzRes[2]){
				fuzzRes[2] = aux;
			}
			if(fuzzRes[1]<0){
				fuzzRes[1] *= -1;
			}
		}else if(fuzzRes[2]<0){
			double aux = fuzzRes[2];
			fuzzRes[2] = fuzzRes[0]*(-1);
			fuzzRes[1] *= -1;
			fuzzRes[0] = aux*(-1);
		}
		//double alpha = fuzzRes[1] - fuzzRes[0];
		//double beta = fuzzRes[2] - fuzzRes[1];
		
		LRMembershipFunction l = (LRMembershipFunction) (((LRFuzzyNumber)arg2).getLfunction().clone());
		LRMembershipFunction r = (LRMembershipFunction) (((LRFuzzyNumber)arg2).getRfunction().clone());
		l.setSpread(fuzzRes[0]);
		r.setSpread(fuzzRes[2]);
		LRFuzzyNumber df = new LRFuzzyNumber(fuzzRes[1], l, r);
		return df;
	}


	@Override
	public FuzzySet substractScalar(double arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FuzzySet sqrt() {
		return null;
	}


	@Override
	public FuzzySet pow(int pot) {
		return this.multiply(this);
	}


	@Override
	public FuzzySet divideScalar(double arg2) {
		// TODO Auto-generated method stub
		return null;
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
		return null;
	}


	@Override
	public int getRightFunction() {
		return 0;
	}


	@Override
	public int getLeftFunction() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public String getStrType() {
		return "fuzzy";
	}

}
