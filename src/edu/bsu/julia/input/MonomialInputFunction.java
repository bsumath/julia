package edu.bsu.julia.input;

import edu.bsu.julia.ComplexNumber;

/**
 * 
 * <h3>Description</h3>
 * <p>
 * MonomialInputFunction is a subclass of InputFunction representing a function of
 * the form: a/z^n, where a & n is a coefficient and both are complex numbers.
 * </p>
 * 
 */
public class MonomialInputFunction extends InputFunction {
	
	private int nValue;
	private ComplexNumber aValue;
	
	/**
	 * Calls the superclass constructor to set up the m value and coefficient
	 * array and then fills that array with the coefficient parameters, a & n.
	 * 
	 * @param mValue
	 * @param a
	 * @param n
	 * @throws IllegalArgumentException
	 *             if a is zero.
	 * @see InputFunction#InputFunction(int, int)
	 */
	public MonomialInputFunction(int mValue, ComplexNumber a, int n)
			throws IllegalArgumentException {
		super(2, mValue);												
		if (a.isZero())
			throw new IllegalArgumentException("a zero");
		/**if (n.isZero())
			throw new IllegalArgumentException("n zero");*/
		//coefficientArray[0] = a;
		aValue = a;
		nValue = n;
	}

	/**					!!!!CHANGE!!!!
	 * This method has no random element as the inverse of a monomial function is
	 * always a single value. See the superclass method description for a more
	 * general account.
	 */
	public ComplexNumber evaluateBackwardsRandom(ComplexNumber seed) {
		//ComplexNumber a = coefficientArray[0];
		//double n = coefficientArray[1].getX();
		ComplexNumber w = seed;
		for (int i = 0; i < getM(); i++) {
									
			
			/**w = Math.pow((ComplexNumber)w, n);
			w = Math.pow((ComplexNumber)w,(ComplexNumber)n);
			w = a.divide(w);*/
		}
		return w;
	}

	public ComplexNumber evaluateForwards(ComplexNumber seed) {								/**Check but I think it's good.*/
		ComplexNumber w = seed;
		for(int i = 0; i < getM(); i++) {
			w = w.power(nValue);
			w = aValue.divide(w);
		}
		return w;
	}

	public ComplexNumber evaluateFunction(ComplexNumber seed) {
		ComplexNumber w = seed;
		w = aValue.divide(w);

		return w;
	}

	/**
	 * This method returns an array of a single value. See the superclass method
	 * description for a more general account.
	 */
	public ComplexNumber[] evaluateBackwardsFull(ComplexNumber seed) {
		// for a linear function, there is no difference between the
		// random and full methods.
		ComplexNumber[] result = { evaluateBackwardsRandom(seed) };
		return result;
	}

	public String toString() {
		ComplexNumber a = coefficientArray[0];

		return new String("f" + getSubscript() + "(z) = " + a + "/z " + ", m = " + getM());
	}

}
