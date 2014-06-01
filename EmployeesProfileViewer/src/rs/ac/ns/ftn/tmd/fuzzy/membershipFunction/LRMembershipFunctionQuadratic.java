/**
 * 
 */
package rs.ac.ns.ftn.tmd.fuzzy.membershipFunction;

/**
 * 
 * Quadratic membership function<br>
 * <b>m_f (X) = 1 - X^2 / spread^2</b>
 * @author Nikola Kolarovic <nikola.kolarovic@gmail.com>
 *
 */
public class LRMembershipFunctionQuadratic implements LRMembershipFunction {

	protected double spread;
	
	 /** instantiates a quadratic membership function
	   * @param spread variance > 0
	   */
	public LRMembershipFunctionQuadratic(double spread) {
		this.setSpread(spread);
	  }


	public double getSpread() {
		return this.spread;
	}

	public void setSpread(double spread) {
		assert(spread>=0);
		this.spread = spread;
	}

	
	/** returns membership grade for distance from modal value
	 * <b>m_f (X) = 1 - X^2 / spread^2</b>
	 */
	public double getValue(double x) {
		assert(x>=0);
		
		if (this.spread == 0) {
			if (x==0) return 1.0d;
			else return 0;
		}
		
		if (x<spread)
			return (1 - x*x/(spread*spread));
		
		else return 0;
	}
	
	
	
	public Object clone() {
		return new LRMembershipFunctionQuadratic(this.spread);
	}

}
