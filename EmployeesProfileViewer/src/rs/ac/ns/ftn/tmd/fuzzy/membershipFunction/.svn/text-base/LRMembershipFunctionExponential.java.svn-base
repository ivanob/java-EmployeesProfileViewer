/**
 * 
 */
package rs.ac.ns.ftn.tmd.fuzzy.membershipFunction;

/**
 * Exponential membership function<br><br>
 * In fact, this is quasi-exponential function, with cut-off point at 4.5 * variance<br><br>
 * 
 * <b>
 * m_f (X) = exp ( -X / variance )</b>; if 0 < x <= 4.5*variance <br><b>
 * m_f (X) = 0 </b>;  if x> 4.5*variance
 * 
 * 
 *  
 * @author Nikola Kolarovic <nikola.kolarovic@gmail.com>
 *
 */
public class LRMembershipFunctionExponential implements LRMembershipFunction {

	private double variance;
	
	 /** instantiates a quadratic membership function<br>
	   * m_f (X) = exp ( -X / variance )
	   * @param spread width of a membership function; spread > 0
	   */
	public LRMembershipFunctionExponential(double spread) {
		this.setSpread(spread);
	  }
	

	
	public double getValue(double x) {
		assert (x>=0);
		
		if (this.variance==0) {
			if (x==0) return 1.0d;
			else return 0;
		}
		
		if (x < 4.5d*variance)
			return Math.exp( -x / variance);
		else return 0;
	}

	
	
	public void setSpread(double spread) {
		assert(spread>=0);
		this.variance = spread/4.5;
		
	}
	
	public double getSpread() {
		return variance*4.5d;
	}
	
	
	public Object clone() {
		return new LRMembershipFunctionExponential(this.getSpread());
	}


}
