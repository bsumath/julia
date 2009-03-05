package edu.bsu.julia.input;

import org.apache.commons.math.complex.Complex;

import edu.bsu.julia.ComplexNumberUtils;

/**
 * 
 * <h3>Description</h3>
 * <p>
 * MonomialInputFunction is a subclass of InputFunction representing a function
 * of the form: a/z^n, where a & n is a coefficient and both are complex
 * numbers.
 * </p>
 * 
 */
public class MonomialInputFunction extends InputFunction {

	private int nValue;

	private Complex aValue;

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
	public MonomialInputFunction(int mValue, Complex a, int n)
			throws IllegalArgumentException {
		super(2, mValue);
		if (a.equals(Complex.ZERO))
			throw new IllegalArgumentException("a zero");
		/**
		 * if (n.isZero()) throw new IllegalArgumentException("n zero");
		 */
		// coefficientArray[0] = a;
		aValue = a;
		nValue = n;
	}

	/**
	 * !!!!CHANGE!!!! This method has no random element as the inverse of a
	 * monomial function is always a single value. See the superclass method
	 * description for a more general account.
	 */
	public Complex evaluateBackwardsRandom(Complex seed) {
		// ComplexNumber a = coefficientArray[0];
		// double n = coefficientArray[1].getX();
		Complex w = seed;
		for (int i = 0; i < getM(); i++) {

			/**
			 * w = Math.pow((ComplexNumber)w, n); w =
			 * Math.pow((ComplexNumber)w,(ComplexNumber)n); w = a.divide(w);
			 */
		}
		return w;
	}

	public Complex evaluateForwards(Complex seed) {
		/** Check but I think it's good. */
		Complex w = seed;
		for (int i = 0; i < getM(); i++) {
			w = w.pow(new Complex(nValue, 0));
			w = aValue.divide(w);
		}
		return w;
	}

	public Complex evaluateFunction(Complex seed) {
		Complex w = seed;
		w = aValue.divide(w);

		return w;
	}

	/**
	 * This method returns an array of a single value. See the superclass method
	 * description for a more general account.
	 */
	public Complex[] evaluateBackwardsFull(Complex seed) {
		// for a linear function, there is no difference between the
		// random and full methods.
		Complex[] result = { evaluateBackwardsRandom(seed) };
		return result;
	}

	public String toString() {
		Complex a = coefficientArray[0];

		return new String("f" + getSubscript() + "(z) = "
				+ ComplexNumberUtils.complexToString(a) + "/z " + ", m = "
				+ getM());
	}

}
