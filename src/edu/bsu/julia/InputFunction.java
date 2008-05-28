package edu.bsu.julia;

/**
 * 
 * <h3>Description</h3>
 * <p>
 * InputFunction is an abstract class providing a template for all the function
 * types used in the program. Any function type (linear, cubic, etc.) that the
 * program uses must be a subclass of this class, as the functions are processed
 * polymorphically within the program.
 * </p>
 * <p>
 * InputFunction also creates and handles the data holders that are common to
 * all function types: the m value and the array of coefficients.
 * </p>
 * <p>
 * InputFunctions are immutable objects. Subclasses have access to the array of
 * coefficients so that they may fill them in when the function is first
 * created, but no changes should be made to an input function after its
 * creation is complete. Editing an input function in the program is actually
 * accomplished by creating a new function and replacing the old function with
 * the new one.
 * </p>
 * 
 */
public abstract class InputFunction {

	public static final int CUBIC_INPUT = 1;
	public static final int LINEAR_INPUT = 3;
	public static final int MOBIUS_INPUT = 4;
	public static final int QUADRATIC_INPUT = 2;
	public static final int REAL_AFFLINE_LINEAR_INPUT = 5;

	/**
	 * m represents the number of times the function is composed with itself.
	 */
	private int m;
	/**
	 * This array contains the coefficients of the function in order from the
	 * beginning of the function, left to right. It is protected so that
	 * subclasses can access it without a function call.
	 */
	protected ComplexNumber[] coefficientArray;
	/**
	 * The subscript number identifying this function within the session.
	 */
	private int sub = 0;

	private int functionType;

	/**
	 * This constructor must be called as the first line of any subclass
	 * constructor. It takes in the m value and the number of coefficients in
	 * the function. It sets up the array of coefficients that subclassing
	 * constructors must fill.
	 * 
	 * @param coefficients
	 * @param mValue
	 * @throws IllegalArgumentException
	 *             if the m value is less than zero.
	 */
	public InputFunction(int coefficients, int mValue)
			throws IllegalArgumentException {
		if (mValue < 0)
			throw new IllegalArgumentException("m neg");
		m = mValue;
		coefficientArray = new ComplexNumber[coefficients];
	}

	public int getType() {
		return functionType;
	}

	public void setType(int type) {
		functionType = type;
	}

	/**
	 * @return m: the number of times the function is composed with itself.
	 */
	public int getM() {
		return m;
	}

	/**
	 * @return An array listing the coefficients of the function in order from
	 *         left to right as the function is written.
	 */
	public ComplexNumber[] getCoefficients() {
		return coefficientArray;
	}

	/**
	 * @return The subscript number identifying this function within the
	 *         session.
	 */
	public int getSubscript() {
		return sub;
	}

	/**
	 * Called by {@link edu.bsu.julia.session.Session} to set the identifying
	 * number for this function when it is first added to the session.
	 * 
	 * @param subscript
	 */
	public void setSubscript(int subscript) {
		sub = subscript;
	}

	/**
	 * Given a starting value, evaluates the inverse of the function composed
	 * with itself m times, and returns a single value. If the function would
	 * naturally return more than one value, the method will randomly choose one
	 * to return. This method must be overridden by subclasses.
	 * 
	 * @param seed
	 *            the starting value
	 * @return a single complex number representing a random inverse value for
	 *         the function.
	 * @throws ArithmeticException
	 *             if division by zero occurs.
	 */
	public abstract ComplexNumber evaluateBackwardsRandom(ComplexNumber seed);

	/**
	 * Given an starting value, evaluates the function composed with itself m
	 * times. This method must be overridden by subclasses.
	 * 
	 * @param seed
	 *            the starting value
	 * @return the value of the function composed with itself m times for the
	 *         given variable value.
	 * @throws ArithmeticException
	 *             if division by zero occurs.
	 */
	public abstract ComplexNumber evaluateForwards(ComplexNumber seed);

	/**
	 * Given a starting value, evaluates the inverse of the function composed
	 * with itself m times. Returns all possible values. This method must be
	 * overridden by subclasses.
	 * 
	 * @param seed
	 *            the starting value
	 * @return An array containing all of the possible values for the inverse of
	 *         this function.
	 * @throws ArithmeticException
	 *             if division by zero occurs.
	 */
	public abstract ComplexNumber[] evaluateBackwardsFull(ComplexNumber seed);

	public abstract ComplexNumber evaluateFunction(ComplexNumber seed);

	/**
	 * @return A string representation of the function as it should appear in
	 *         the input function list (left hand side of the program). This
	 *         method must be overridden by subclasses.
	 */
	public abstract String toString();

}
