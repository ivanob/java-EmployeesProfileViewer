/**
 */
package rs.ac.ns.ftn.tmd.datatypes;

/**
 * A class which defines operations Fuzzy Numbers have to support, independantly to
 * its inner implementation. Generally operations of fuzzy arithmetics are defined here.
 * 
 * 
 * @author Nikola Kolarovic <nikola.kolarovic@gmail.com>
 * 
 */
public interface FuzzySet {

	/** value which denotes linear membership function */
	public static final int FUNCTION_LINEAR = 0x00;

	/** value which denotes Gaussian membership function */
	public static final int FUNCTION_GAUSSIAN = 0x01;

	/** value which denotes quadratic membership function */
	public static final int FUNCTION_QUADRATIC = 0x02;
	
	/** value which denotes exponential membership function */
	public static final int FUNCTION_EXPONENTIAL = 0x03;
	
	
	  /** Fuzzy operation for <b>addition</b> of two fuzzy numbers. <br>
	   * First argument for the operation is the current object, and second argument
	   * is given as a function parameter.<br><br>
	   * Method can return <b>UncompatibileFuzzyArithmeticsException</b> RuntimeException if fuzzy number implementations are not compatibile!<br>
	   * @param arg2 second argument for the operation
	   * @return number which is the result of the operation
	   */
	  public FuzzySet add(FuzzySet arg2);

	  /** Fuzzy operation for <b>substraction</b> of two fuzzy numbers. <br>
	   * First argument for the operation is the current object, and second argument
	   * is given as a function parameter.<br><br>
	   * Method can return <b>UncompatibileFuzzyArithmeticsException</b> RuntimeException if fuzzy number implementations are not compatibile!<br>

	   * @param arg2 second argument for the operation, which will be substracted from the current
	   * @return number which is the result of the operation
	   */
	  public FuzzySet substract(FuzzySet arg2);


	  /** Fuzzy operation for <b>multiplication</b> of two fuzzy numbers. <br>
	   * First argument for the operation is the current object, and second argument
	   * is given as a function parameter.<br>
	   * @throws <code>UncompatibileFuzzyArithmeticsException</code> if fuzzy number implementations are not compatibile<br>
	   * @param arg2 second argument for the operation
	   * @return number which is the result of the operation
	   */
	  public FuzzySet multiply(FuzzySet arg2);


	  /** Fuzzy operation for <b>division</b> of two fuzzy numbers. <br>
	   * First argument for the operation is the current object, and second argument
	   * is given as a function parameter.<br>
	   * @throws <code>UncompatibileFuzzyArithmeticsException</code> if fuzzy number implementations are not compatibile<br>
	   * @throws <code>FuzzyDivisionByZeroException</code> if number's confidence interval contains value 0<br>
	   * @param arg2 second argument for the operation
	   * @return number which is the result of the operation
	   */
	  public FuzzySet divide(FuzzySet arg2);

	  public FuzzySet divideScalar(double arg2);
	  
	  /** Fuzzy operation for addition between current FuzzyNumber and a scalar (<i>double</i>) value. <br>
	   * First argument for the operation is the current object, and second argument
	   * is given as a function parameter.
	   * @param arg2 constant which will be added as a crisp number
	   * @return number which is the result of the operation
	   */	  
	  public FuzzySet addScalar(double arg2);


	  /** Fuzzy operation for multiplication between current FuzzyNumber and a scalar (<i>double</i>) value. <br>
	   * First argument for the operation is the current object, and second argument
	   * is given as a function parameter.<br>
	   * 
	   * Also for negation of FuzzyNumber, use multiplyScalar(-1).
	   * 
	   * @param arg2 constant which will be multiplied as a crisp number
	   * @return number which is the result of the operation
	   */	  
	  public FuzzySet multiplyScalar(double arg2);

	  
	  
	  /** Calculates new FuzzyNumber which represents the multiplication inverse fuzzy number for the current one.<br>
	   * <b>Basically, it is fuzzy 1/X function.</b><br>
	   * @throws <code>FuzzyDivisionByZeroException</code> if number's confidence interval contains value 0<br>
	   * @return fuzzy 1/X number
	   */ 
	  public FuzzySet getMultiplicationInverse();
	  
	  
	  
	  /** Calculates value of the membership function of a fuzzy number for given value.<br>
	   * 
	   * @param universalValue Value in universal (real) set for which we calculate membership function
	   * @return value of the membership function in given point -> [0,1]
	   */
	  public double getMembershipValue(double universalValue);

	  
	  
	  /** Calculates the modal value of a fuzzy number<br>
	   * Modal value is the universal value in which membership function value is reaching 1.
	   * @return value of X where <b><i>membershipFunction(X) == 1</i></b>
	   */
	  public double getModalValue();
	  
	  /**
	   * Calculates left boundary of a fuzzy number. It is value of the worst case interval on the left side, 
	   * value in which membership function has value 0+.<b>
	   * @return left boundary value in universal set X
	   */
	  public double getLeftBoundary();
	  
	  
	  /**
	   * Calculates right boundary of a fuzzy number. It is value of the worst case interval on the right side,
	   * value in which membership function has value 0+.
	   * @return right boundary value in universal set X
	   */
	  public double getRightBoundary();
	  
	  
	  /**
	   * Calculates value of left spread, which is distance from the modal value to the left boundary.
	   * @return distance from modal value to left boundary, which is always >0
	   */
	  public double getLeftSpread();
	  
	  /**
	   * Calculates value of right spread, which is distance from the modal value to the right boundary.
	   * @return distance from modal value to right boundary, which is always >0
	   */
	  public double getRightSpread();
	  
	  /**
	   * Operacion que implementa la distancia numerica entre 2 objetos 
	   * de tipo FuzzySet. Dada la de tipos de datos existentes (cualquier 
	   * dato de este paquete implementa la clase FuzzySet) existiran muchas
	   * combinaciones distintas y el tipo del resultado dependera de ello.
	   * Por ejemplo, la distancia entre un escalar y un conjunto numerico
	   * es otro conjunto numerico; pero la distancia entre un escalar y un
	   * numero borroso es otro numero borroso.
	   * @param arg2
	   * @return Un objeto FuzzySet con la distancia entre los 2 parametros
	   * 
	   */
	  public FuzzySet distance(FuzzySet arg2);
	  
	  /**
	   * Resta un valor real al dato que llama a este metodo.
	   * @param arg2
	   * @return
	   */
	  public FuzzySet substractScalar(double arg2);
	  
	  /**
	   * Aplica una raiz cuadrada al dato que llama a este metodo. El
	   * tipo del resultado sera el mismo que el tipo del dato que hace
	   * la llamada.
	   * @return Objeto FuzzySet con el resultado de aplicar la raiz cuadrada
	   */
	  public FuzzySet sqrt();
	  
	  /**
	   * Eleva el dato que llama a este metodo a la potencia que indica
	   * el parametro recibido. El tipo del dato que retorna sera el
	   * mismo que el tipo del dato que realiza la llamada.
	   * @param pot potencia a la que elevar el dato
	   * @return
	   */
	  public FuzzySet pow(int pot);
	  
	  /**
	   * @return true si el dato que llama a este metodo tiene
	   * imprecision simple (es un conjunto numerico o dato borroso)
	   * y false en caso de que no tenga imprecision (un escalar)
	   */
	  public boolean existsImprecision();
	  
	  /**
	   * @return true si existe incertidumbre de tipo borrosa en el dato
	   * que llama a este metodo y false en caso de que no la tenga (que
	   * sea un dato exacto o con incertidumbre simple).
	   */
	  public boolean existsFuzzyImprecision();
	  
	  /**
	   * Este metodo aplica una desconfianza por debajo y por arriba al
	   * dato que llama a este metodo. Por ejemplo si aplicamos desconfianza
	   * a un escalar exacto se transformara en un conjunto numerico.
	   * @param minDistrust
	   * @param maxDistrust
	   * @return
	   */
	  public FuzzySet distrust(float minDistrust, float maxDistrust);
	  
	  /**
	   * Solo tiene sentido llamar a este metodo desde un dato borroso
	   * (numero o intervalo)
	   * @return El identificador del tipo de funcion de la rama derecha
	   * del dato borroso. 
	   */
	  public int getRightFunction();
	  
	  /**
	   * Solo tiene sentido llamar a este metodo desde un dato borroso
	   * (numero o intervalo)
	   * @return El identificador del tipo de funcion de la rama izquierda
	   * del dato borroso.
	   */
	  public int getLeftFunction();
	  
	  /**
	   * Sirve como complemento del toString().
	   * @return Un String con el nombre del tipo de dato que llama a este
	   * metodo.
	   */
	  public String getStrType();
	  	  
}