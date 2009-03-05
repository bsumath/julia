package edu.bsu.julia.input;

import org.apache.commons.math.complex.Complex;

/**
 * 
 * <h3>Description</h3>
 * 
 * <p>
 * {@link RealAffineLinearInputFunction} is a subclass of {@link InputFunction}
 * representing a function of the form: [a, b; c, d]z + [e, f] where a, b, c, d,
 * e, and f are real numbers and z is a complex number.
 * </p>
 * 
 */
public class RealAffineLinearInputFunction extends InputFunction {
	/**
	 * Calls the superclass constructor to set up the m value and coefficient
	 * array and then fills that array with the coefficient parameters, a
	 * through f. The coefficients, though real numbers, are stored as complex
	 * numbers with an imaginary part of zero to match the rest of the input
	 * functions.
	 * 
	 * @param mValue
	 * @param a
	 * @param b
	 * @param c
	 * @param d
	 * @param e
	 * @param f
	 * @see InputFunction#InputFunction(int, int)
	 */
	public RealAffineLinearInputFunction(int mValue, Complex a,
			Complex b, Complex c, Complex d, Complex e,
			Complex f) {
		super(6, mValue);
		coefficientArray[0] = a;
		coefficientArray[1] = b;
		coefficientArray[2] = c;
		coefficientArray[3] = d;
		coefficientArray[4] = e;
		coefficientArray[5] = f;
	}

	/**
	 * This method has no random element as the inverse of a real affine linear
	 * map is a single value. See the superclass method description for a more
	 * general account. This method returns <b>null</b> if the determinant of
	 * the 2x2 matrix is zero. Calling methods must check the return value for
	 * <b>null</b> and report the error to the user.
	 */
	public Complex evaluateBackwardsRandom(Complex seed) {
		double x = seed.getReal();
		double y = seed.getImaginary();
		double a = coefficientArray[0].getReal();
		double b = coefficientArray[1].getReal();
		double c = coefficientArray[2].getReal();
		double d = coefficientArray[3].getReal();
		double e = coefficientArray[4].getReal();
		double f = coefficientArray[5].getReal();

		double det = a * d - b * c;
		if (det < 0.000000001 && det > -0.000000001)
			return null;

		for (int i = 0; i < getM(); i++) {
			double real = (d * (x - e) - b * (y - f)) / det;
			double im = (-c * (x - e) + a * (y - f)) / det;
			x = real;
			y = im;
		}
		return new Complex(x, y);
	}

	public Complex evaluateForwards(Complex seed) {
		double x = seed.getReal();
		double y = seed.getImaginary();
		double a = coefficientArray[0].getReal();
		double b = coefficientArray[1].getReal();
		double c = coefficientArray[2].getReal();
		double d = coefficientArray[3].getReal();
		double e = coefficientArray[4].getReal();
		double f = coefficientArray[5].getReal();

		for (int i = 0; i < getM(); i++) {
			double real = a * x + b * y + e;
			double im = c * x + d * y + f;
			x = real;
			y = im;
		}
		return new Complex(x, y);
	}

	/**
	 * This method returns an array of a single value. See the superclass method
	 * description for a more general account. This method returns <b>null</b>
	 * if the determinant of the 2x2 matrix is zero. Calling methods must check
	 * the return value for <b>null</b> and report the error to the user.
	 */

	public Complex evaluateFunction(Complex seed) {
		double x = seed.getReal();
		double y = seed.getImaginary();
		double a = coefficientArray[0].getReal();
		double b = coefficientArray[1].getReal();
		double c = coefficientArray[2].getReal();
		double d = coefficientArray[3].getReal();
		double e = coefficientArray[4].getReal();
		double f = coefficientArray[5].getReal();

		double real = a * x + b * y + e;
		double im = c * x + d * y + f;
		x = real;
		y = im;

		return new Complex(x, y);
	}

	public Complex[] evaluateBackwardsFull(Complex seed) {
		// for a real affine linear map, there is no difference between
		// the random and full methods.
		Complex x = evaluateBackwardsRandom(seed);
		if (x == null)
			return null;
		Complex[] result = { x };
		return result;
	}

	public String toString() {
		double a = coefficientArray[0].getReal();
		double b = coefficientArray[1].getReal();
		double c = coefficientArray[2].getReal();
		double d = coefficientArray[3].getReal();
		double e = coefficientArray[4].getReal();
		double f = coefficientArray[5].getReal();

		return new String("f" + getSubscript() + "(z) = [" + a + ", " + b
				+ "; " + c + ", " + d + "]z + [" + e + "; " + f + "], m = "
				+ getM());
	}

}
