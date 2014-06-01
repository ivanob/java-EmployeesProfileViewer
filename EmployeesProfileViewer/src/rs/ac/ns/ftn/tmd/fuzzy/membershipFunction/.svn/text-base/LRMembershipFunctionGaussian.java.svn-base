/**
 * 
 */
package rs.ac.ns.ftn.tmd.fuzzy.membershipFunction;

/**
 * 
 * Gaussian membership function<br><br>
 * in fact, this is quasi-Gaussian function with cut-off at 3*variance<br>
 * 
 * <b>
 * m_f (X) = exp ( -X^2 / (2* variance^2 )</b>; if 0 < x <= 3*variance <br><b>
 * m_f (X) = 0 </b>;  if x> 3*variance
 * 
 * 
 * @author Nikola Kolarovic <nikola.kolarovic@gmail.com>
 *
 */
public class LRMembershipFunctionGaussian implements LRMembershipFunction {

	private double variance;
	
	
	 /** instantiates a quasi-Gaussian membership function<br>
	   * @param spread variance, variance > 0
	   */
	public LRMembershipFunctionGaussian(double spread) {
		 this.setSpread(spread);
	  }
	
	
	public double getSpread() {
		return this.variance*3d;
	}

	public void setSpread(double spread) {
		assert(spread>=0);
		this.variance = spread/3d;
	}


	/**
	 * Returns membership value if given distance from modal value 
	 * 
	   * m_f (X) = exp ( -X^2 / 2*(variance^2) )
	 */
	public double getValue(double x) {
		assert(x>=0);
		if (this.variance == 0) {
			if (x==0) return 1.0d;
			else return 0;
		}
		
		if (x < 3d*variance)
			return Math.exp(- x*x/(2d*variance*variance));
		else return 0;
	}
	

	
	public Object clone() {
		return new LRMembershipFunctionGaussian(3d*this.variance);
	}


}
