package edu.bsu.julia.input;

import org.apache.commons.math.complex.Complex;

import edu.bsu.julia.ComplexNumberUtils;

/**
 * 
 * <h3>Description</h3>
 * <p>
 * MobiusInputFunction is a subclass of InputFunction representing a function of
 * the form: (az + b)/(cz +d), where a, b, c, and d are coefficients, z is the
 * variable, and all are complex numbers.
 * </p>
 * 
 */
public class MobiusInputFunction extends InputFunction {

	/**
	 * Calls the superclass constructor to set up the m value and coefficient
	 * array, then fills that array with the coefficient parameters, a through
	 * d.
	 * 
	 * @param mValue
	 * @param a
	 * @param b
	 * @param c
	 * @param d
	 * @see InputFunction#InputFunction(int, int)
	 */
	public MobiusInputFunction(int mValue, Complex a, Complex b, Complex c,
			Complex d) {
		super(4, mValue);
		coefficientArray[0] = a;
		coefficientArray[1] = b;
		coefficientArray[2] = c;
		coefficientArray[3] = d;
	}

	/**
	 * This method has no random element as the inverse of a mobius function is
	 * a single value. See the superclass method description for a more general
	 * account.
	 */
	public Complex evaluateBackwardsRandom(Complex seed) {
		Complex a = coefficientArray[0];
		Complex b = coefficientArray[1];
		Complex c = coefficientArray[2];
		Complex d = coefficientArray[3];
		Complex w = seed;
		Complex x;
		Complex y;

		for (int i = 0; i < getM(); i++) {
			x = b.subtract(w.multiply(d));
			y = (w.multiply(c)).subtract(a);
			// potential ArithmeticExceptions should be handled at thread level
			w = x.divide(y);
		}
		return w;
	}

	public Complex evaluateForwards(Complex seed) {
		Complex a = coefficientArray[0];
		Complex b = coefficientArray[1];
		Complex c = coefficientArray[2];
		Complex d = coefficientArray[3];
		Complex w = seed;
		Complex x;
		Complex y;

		for (int i = 0; i < getM(); i++) {
			x = a.multiply(w).add(b);
			y = c.multiply(w).add(d);
			// potential ArithmeticExceptions should be handled at thread level
			w = x.divide(y);
		}
		return w;
	}

	public Complex evaluateFunction(Complex seed) {
		Complex a = coefficientArray[0];
		Complex b = coefficientArray[1];
		Complex c = coefficientArray[2];
		Complex d = coefficientArray[3];
		Complex w = seed;
		Complex x;
		Complex y;

		x = a.multiply(w).add(b);
		y = c.multiply(w).add(d);
		// potential ArithmeticExceptions should be handled at thread level
		w = x.divide(y);

		return w;
	}

	/**
	 * This method returns an array of a single value. See the superclass method
	 * description for a more general account.
	 */
	public Complex[] evaluateBackwardsFull(Complex seed) {
		// for a mobius function, there is no difference between the random
		// and full methods
		Complex[] result = { evaluateBackwardsRandom(seed) };
		return result;
	}

	public String toString() {
		Complex a = coefficientArray[0];
		Complex b = coefficientArray[1];
		Complex c = coefficientArray[2];
		Complex d = coefficientArray[3];

		return new String("f" + getSubscript() + "(z) = "
				+ ComplexNumberUtils.complexToString(a) + "z + "
				+ ComplexNumberUtils.complexToString(b) + " / "
				+ ComplexNumberUtils.complexToString(c) + "z + "
				+ ComplexNumberUtils.complexToString(d) + ", m = " + getM());
	}

}
