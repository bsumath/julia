package edu.bsu.julia.input;

import java.util.Vector;

import org.apache.commons.math.complex.Complex;

import edu.bsu.julia.ComplexNumberUtils;

/**
 * 
 * <h3>Description</h3>
 * <p>
 * QuadraticInputFunction is a subclass of InputFunction representing a function
 * of the form az^2 + bz + c, where a, b, and c are coefficients and all are
 * complex numbers.
 * 
 */
public class QuadraticInputFunction extends InputFunction {

	/**
	 * A convenience constant representing the real number, -1.
	 */
	private static final Complex NEG_ONE = new Complex(-1, 0);

	/**
	 * A convenience constant representing the real number, 4.
	 */
	private static final Complex FOUR = new Complex(4, 0);

	/**
	 * A convenience constant representing the real number, 2.
	 */
	private static final Complex TWO = new Complex(2, 0);

	/**
	 * Calls the superclass constructor to set up the m value and coefficient
	 * array and then fills that array with the coefficient parameters, a, b,
	 * and c.
	 * 
	 * @param mValue
	 * @param a
	 * @param b
	 * @param c
	 * @throws IllegalArgumentException
	 *             if a is zero
	 * @see InputFunction#InputFunction(int, int)
	 */
	public QuadraticInputFunction(int mValue, Complex a, Complex b, Complex c)
			throws IllegalArgumentException {
		super(3, mValue);
		if (a.equals(Complex.ZERO))
			throw new IllegalArgumentException("a zero");
		coefficientArray[0] = a;
		coefficientArray[1] = b;
		coefficientArray[2] = c;
	}

	public Complex evaluateBackwardsRandom(Complex seed) {
		Complex a = coefficientArray[0];
		Complex b = coefficientArray[1];
		Complex c = coefficientArray[2];
		Complex w = seed;
		Complex temp;
		Complex negB = b.multiply(NEG_ONE);
		Complex sqrB = b.multiply(b);

		for (int i = 0; i < getM(); i++) {
			temp = sqrB.subtract((a.multiply(c.subtract(w))).multiply(FOUR));
			Complex[] sqrt = temp.nthRoot(2).toArray(new Complex[] {});
			double randomNumber = Math.random();
			// potential ArithmeticExceptions should be caught at thread level
			if (randomNumber < .5)
				w = negB.add(sqrt[0]).divide(a.multiply(TWO));
			else
				w = negB.add(sqrt[1]).divide(a.multiply(TWO));
		}
		return w;
	}

	public Complex evaluateForwards(Complex seed) {
		Complex a = coefficientArray[0];
		Complex b = coefficientArray[1];
		Complex c = coefficientArray[2];
		Complex result = Complex.ZERO;
		Complex w = seed;

		for (int i = 0; i < getM(); i++) {
			result = a.multiply(w.multiply(w));
			result = (result.add(b.multiply(w))).add(c);
			w = result;
		}
		return result;
	}

	public Complex evaluateFunction(Complex seed) {
		Complex a = coefficientArray[0];
		Complex b = coefficientArray[1];
		Complex c = coefficientArray[2];
		Complex result = Complex.ZERO;
		Complex w = seed;

		result = a.multiply(w.multiply(w));
		result = (result.add(b.multiply(w))).add(c);
		w = result;

		return result;
	}

	/**
	 * This method returns 2^m complex numbers for each seed. See the superclass
	 * method for a more general account.
	 */
	public Complex[] evaluateBackwardsFull(Complex seed) {
		Complex a = coefficientArray[0];
		Complex b = coefficientArray[1];
		Complex c = coefficientArray[2];
		Complex w = seed;
		Complex temp;
		Complex negB = b.multiply(NEG_ONE);
		Complex sqrB = b.multiply(b);
		Vector<Complex> results = new Vector<Complex>();
		results.add(w);
		Complex[] sqrt = new Complex[2];

		for (int i = 0; i < getM(); i++) {
			for (int j = 0; j < Math.pow(2, i); j++) {
				w = (Complex) results.firstElement();
				results.remove(0);
				temp = sqrB
						.subtract((a.multiply(c.subtract(w))).multiply(FOUR));
				sqrt = temp.nthRoot(2).toArray(new Complex[] {});
				// potential ArithmeticExceptions should be caught at thread
				// level
				results.add(negB.add(sqrt[0]).divide(a.multiply(TWO)));
				results.add(negB.add(sqrt[1]).divide(a.multiply(TWO)));
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
		Complex c = coefficientArray[2];

		return new String("f" + getSubscript() + "(z) = "
				+ ComplexNumberUtils.complexToString(a) + "z^2 + "
				+ ComplexNumberUtils.complexToString(b) + "z + "
				+ ComplexNumberUtils.complexToString(c) + ", m = " + getM());
	}

}
