package edu.bsu.julia.input;

import java.util.Vector;

import org.apache.commons.math.complex.Complex;

import edu.bsu.julia.ComplexNumberUtils;

/**
 * 
 * <h3>Description</h3>
 * <p>
 * CubicInputFunction is a subclass of InputFunction representing a function of
 * the form: a*z^3 + b, where a and b are coefficients and all three are complex
 * numbers.
 * </p>
 * 
 */
public class CubicInputFunction extends InputFunction {

	/**
	 * Calls the superclass constructor to set up the m value and coefficient
	 * array and then fills that array with the coefficient parameters, a and b.
	 * 
	 * @param mValue
	 * @param a
	 * @param b
	 * @throws IllegalArgumentException
	 *             if a is zero.
	 * @see InputFunction#InputFunction(int, int)
	 */
	public CubicInputFunction(int mValue, Complex a, Complex b)
			throws IllegalArgumentException {
		super(2, mValue);
		if (a.equals(Complex.ZERO))
			throw new IllegalArgumentException("a zero");
		coefficientArray[0] = a;
		coefficientArray[1] = b;
	}

	public Complex evaluateBackwardsRandom(Complex seed) {
		Complex a = coefficientArray[0];
		Complex b = coefficientArray[1];
		Complex w = seed;
		for (int i = 0; i < getM(); i++) {
			double randomNumber = Math.random();
			// potential ArithmeticExceptions should be handled at thread level
			w = (w.subtract(b)).divide(a);
			Complex[] cbrt = w.nthRoot(3).toArray(new Complex[] {});
			if (randomNumber <= (1.0 / 3.0))
				w = cbrt[0];
			else {
				if (randomNumber <= (2.0 / 3.0))
					w = cbrt[1];
				else
					w = cbrt[2];
			}
		}
		return w;
	}

	public Complex evaluateForwards(Complex seed) {
		Complex a = coefficientArray[0];
		Complex b = coefficientArray[1];
		Complex result = seed;
		for (int i = 0; i < getM(); i++) {
			// the variable z represents z^3 in the equation
			Complex z = (result.multiply(result)).multiply(result);
			result = (a.multiply(z)).add(b);
		}
		return result;
	}

	/**
	 * This method returns 3^m complex numbers for each seed. See the superclass
	 * method description for a more general account.
	 */

	public Complex evaluateFunction(Complex seed) {
		Complex a = coefficientArray[0];
		Complex b = coefficientArray[1];
		Complex result = seed;
		// the variable z represents z^3 in the equation
		Complex z = (result.multiply(result)).multiply(result);
		result = (a.multiply(z)).add(b);

		return result;
	}

	public Complex[] evaluateBackwardsFull(Complex seed) {
		Complex a = coefficientArray[0];
		Complex b = coefficientArray[1];
		Complex w = seed;
		Vector<Complex> results = new Vector<Complex>();
		results.add(w);
		Complex[] cbrt = new Complex[3];
		for (int i = 0; i < getM(); i++) {
			for (int j = 0; j < Math.pow(3, i); j++) {
				w = (Complex) results.firstElement();
				results.remove(0);
				// potential ArithmeticExceptions should be handled at thread
				// level
				w = (w.subtract(b)).divide(a);
				cbrt = w.nthRoot(3).toArray(new Complex[] {});
				results.add(cbrt[0]);
				results.add(cbrt[1]);
				results.add(cbrt[2]);
			}
		}
		results.trimToSize();
		Complex[] finalResults = new Complex[results.size()];
		results.toArray(finalResults);
		return finalResults;
	}

	public String toString() {
		Complex a = coefficientArray[0];
		Complex b = coefficientArray[1];

		return new String("f" + getSubscript() + "(z) = "
				+ ComplexNumberUtils.complexToString(a) + "z^3 + "
				+ ComplexNumberUtils.complexToString(b) + ", m = " + getM());
	}

}
