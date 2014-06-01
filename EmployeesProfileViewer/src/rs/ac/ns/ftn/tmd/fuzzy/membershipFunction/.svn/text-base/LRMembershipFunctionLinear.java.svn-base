/**
 * 
 */
package rs.ac.ns.ftn.tmd.fuzzy.membershipFunction;

/**
 * @author Nikola Kolarovic <nikola.kolarovic@gmail.com>
 *
 */
public class LRMembershipFunctionLinear implements LRMembershipFunction {

	protected double spread;
	
	  /** instantiates a linear membership function<br>
	   * m_f (X) = k*X + n
	   * @param spread worst case interval (width) of a function, spread > 0
	   */
	  public LRMembershipFunctionLinear(double spread) {
		  this.setSpread(spread);
	  }
	  
	
	/**
	 * function to get membership value for X value
	 */
	public double getValue(double x) {
		if (this.spread==0) {
			if (x==0) return 1.0d;
			else return 0;
		}
		
		return Math.max(0, 1 - (x / this.spread)  );
	}

	

	public Object clone() {
		return new LRMembershipFunctionLinear(this.spread);
	}


	/** returns spread of a membership function */
	public double getSpread() {
		return this.spread;
	}


	/** sets width of a membership function */
	public void setSpread(double spread) {
		assert(spread>=0);
		this.spread = spread;
	}

}
