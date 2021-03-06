/**
 * 
 */
package rs.ac.ns.ftn.tmd.datatypes;

/**
 * @author Nikola Kolarovic <nikola.kolarovic@gmail.com>
 *
 */
public class FuzzyFactory {

	public static final int LR = 0;
	public static final int DECOMPOSED = 1;
	
	/**
	 * Number of cuts used while creating a DecomposedFuzzyNumber. Should be changed on the beginning, never after.
	 */
	private static int defaultDecomposedCuts = 50;
	
	/** pointer to the factory */
	private static FuzzyFactory pointer = null;
	
	/** type */
	protected int type;
	
	/** instantiates factory of FuzzyNumbers of wanted type ("LR" or "DECOMPOSED").<br\>
	 * If not specified, the default cuts number for decomposed is 50. 
	 */
	public FuzzyFactory(int type) {
		if (FuzzyFactory.pointer != null)
			throw new RuntimeException("Factory should be instantiated only once!"); 
		FuzzyFactory.pointer = this;
		this.type = type;
	}
	
	/** initializes FuzzyFactory for use with decomposed fuzzy numbers and determines number of cuts */
	public FuzzyFactory(int type, int decomposedCutsNumber) {
			this(type);
			FuzzyFactory.defaultDecomposedCuts = decomposedCutsNumber;
	}
	
	
	/** uses singleton, which calls the default constructor if no previous constructor was called */
	public static FuzzyFactory instance() {
		if (FuzzyFactory.pointer == null)
			throw new RuntimeException(FuzzyFactory.class.getCanonicalName()+" not instantiated. You should first call constructor, then use instance!");
		
		return FuzzyFactory.pointer;
	}

//	-------------------------------------- Factory methods  ------------------------------------
	
	
	/** creates a FuzzyNumber with linear functions */
	public FuzzySet createLinearFuzzyNumber(double modal, double alpha, double beta) {
		if (this.type == FuzzyFactory.LR)
			return new LRFuzzyNumber(modal, alpha, beta, FuzzySet.FUNCTION_LINEAR, FuzzySet.FUNCTION_LINEAR);
		else // decomposed
			return new DecomposedFuzzyNumber(modal, alpha, beta, FuzzySet.FUNCTION_LINEAR, FuzzySet.FUNCTION_LINEAR, FuzzyFactory.defaultDecomposedCuts);
	}
	
	
	/** creates a FuzzyNumber with gaussian functions */
	public FuzzySet createGaussianFuzzyNumber(double modal, double alpha, double beta) {
		if (this.type == FuzzyFactory.LR)
			return new LRFuzzyNumber(modal, alpha, beta, FuzzySet.FUNCTION_GAUSSIAN, FuzzySet.FUNCTION_GAUSSIAN);
		else // decomposed
			return new DecomposedFuzzyNumber(modal, alpha, beta, FuzzySet.FUNCTION_GAUSSIAN, FuzzySet.FUNCTION_GAUSSIAN, FuzzyFactory.defaultDecomposedCuts);
	}

	
	/** creates a FuzzyNumber with quadratic functions */
	public FuzzySet createQuadraticFuzzyNumber(double modal, double alpha, double beta) {
		if (this.type == FuzzyFactory.LR)
			return new LRFuzzyNumber(modal, alpha, beta, FuzzySet.FUNCTION_QUADRATIC, FuzzySet.FUNCTION_QUADRATIC);
		else // decomposed
			return new DecomposedFuzzyNumber(modal, alpha, beta, FuzzySet.FUNCTION_QUADRATIC, FuzzySet.FUNCTION_QUADRATIC, FuzzyFactory.defaultDecomposedCuts);
	}

	
	/** creates a FuzzyNumber with exponential functions */
	public FuzzySet createExponentialFuzzyNumber(double modal, double alpha, double beta) {
		if (this.type == FuzzyFactory.LR)
			return new LRFuzzyNumber(modal, alpha, beta, FuzzySet.FUNCTION_EXPONENTIAL, FuzzySet.FUNCTION_EXPONENTIAL);
		else // decomposed
			return new DecomposedFuzzyNumber(modal, alpha, beta, FuzzySet.FUNCTION_EXPONENTIAL, FuzzySet.FUNCTION_EXPONENTIAL, FuzzyFactory.defaultDecomposedCuts);
		
	}
	

	
	
	
	
	
//	-------------------------------------- Static Factory methods  ------------------------------------
//	---------------------------------------- ( for a quickie )  ---------------------------------------
//  ---------------------------------------------------------------------------------------------------	
	
	/**
	 * Creates new triangular L-R fuzzy number
	 * @param stringRepresentation string in format <b>[modalValue, leftSpread, rightSpread]</b>
	 * @return triangular fuzzy number with L-R implementation
	 */
	public static FuzzySet createTriangularLRFuzzyNumber(String stringRepresentation) throws FuzzyFactory.InvalidStringRepresentationException {
		return new LRFuzzyNumber(FuzzyFactory.getModalValue(stringRepresentation),
								 FuzzyFactory.getAlphaValue(stringRepresentation),
								 FuzzyFactory.getBetaValue(stringRepresentation),
								 FuzzySet.FUNCTION_LINEAR,
								 FuzzySet.FUNCTION_LINEAR);
		
	}

	public static FuzzySet createArbitraryLRFuzzyNumber(String stringRepresentation, int Ltype, int Rtype) throws FuzzyFactory.InvalidStringRepresentationException {
		return new LRFuzzyNumber(FuzzyFactory.getModalValue(stringRepresentation),
								 FuzzyFactory.getAlphaValue(stringRepresentation),
								 FuzzyFactory.getBetaValue(stringRepresentation),
								 Ltype,
								 Rtype);
		
	}

	public static FuzzySet createArbitraryDecomposedFuzzyNumber(String stringRepresentation, int Ltype, int Rtype) throws FuzzyFactory.InvalidStringRepresentationException {
		return new DecomposedFuzzyNumber(FuzzyFactory.getModalValue(stringRepresentation),
								 FuzzyFactory.getAlphaValue(stringRepresentation),
								 FuzzyFactory.getBetaValue(stringRepresentation),
								 Ltype,
								 Rtype, FuzzyFactory.defaultDecomposedCuts);
		
	}


//-------------------------------------- HELPER FUNCTIONS ------------------------------------
	/** extracts modal value from string representation
	 * 
	 * @param stringRepresentation whole string
	 * @return modal value
	 * @throws FuzzyFactory.InvalidStringRepresentationException
	 */
	protected static double getModalValue(String stringRepresentation) throws FuzzyFactory.InvalidStringRepresentationException {
		try {
			String substring = stringRepresentation.substring(1,stringRepresentation.indexOf(","));
			return Double.parseDouble(substring);
		} catch (Exception ex) {
			throw new InvalidStringRepresentationException("Could not parse modal value from string from string "+stringRepresentation);
		}
		 
	}

	/** extracts alpha value from string representation
	 * 
	 * @param stringRepresentation whole string
	 * @return alpha spread
	 * @throws FuzzyFactory.InvalidStringRepresentationException
	 */
	protected static double getAlphaValue(String stringRepresentation) throws FuzzyFactory.InvalidStringRepresentationException {
		try {
			int index = stringRepresentation.indexOf(",")+1;
			String substring = stringRepresentation.substring(index,stringRepresentation.indexOf(",", index));
			return Double.parseDouble(substring);
		} catch (Exception ex) {
			throw new InvalidStringRepresentationException("Could not parse alpha spread value from string "+stringRepresentation);
		}
		 
	}
	
	/** extracts beta value from string representation
	 * 
	 * @param stringRepresentation whole string
	 * @return beta spread
	 * @throws FuzzyFactory.InvalidStringRepresentationException
	 */
	protected static double getBetaValue(String stringRepresentation) throws FuzzyFactory.InvalidStringRepresentationException {
		try {
			int index = stringRepresentation.indexOf(",", stringRepresentation.indexOf(",")+1)+1;
			int lastComma = stringRepresentation.lastIndexOf(",");
			String substring;
			if (lastComma==index-1) { // beta is last number, just ignore the closing brackets
				 substring = stringRepresentation.substring(index,stringRepresentation.length()-1);
			}
			else { // there is something after beta!
			    substring = stringRepresentation.substring(index,stringRepresentation.indexOf(",", index));
			}
			return Double.parseDouble(substring);
		} catch (Exception ex) {
			throw new InvalidStringRepresentationException("Could not parse beta spread value from string "+stringRepresentation);
		}
		 
	}


	/**
	 * Class which is thrown in case of a parsing exception 
	 */
	public static class InvalidStringRepresentationException extends Exception {
		private static final long serialVersionUID = 5201050961201220107L;
		
		public InvalidStringRepresentationException(String message) {
			super(message);
		}
		
	}
}
