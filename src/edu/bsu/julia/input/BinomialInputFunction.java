/*
 * ISSUES:
 * 		1. When editing a function an error will show saying "Coefficient values must be real values."  
 * 		This error shows even if a real value is inputed.  Oddly things will work if you retype the b value.
 * 		I think what is going on is b is supposed to be an integer and it's getting angry at b.0 and then
 * 		showing an error that's not correct for what's really going on.  Need to change the default of b
 * 		somehow so that it doesn't show up at b.0 .
 * 
 */

package edu.bsu.julia.input;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.math.complex.Complex;

import edu.bsu.julia.ComplexNumberUtils;

/**
 * 
 * <h3>Description</h3>
 * <p>
 * MonomialInputFunction is a subclass of InputFunction representing a function
 * of the form: a*z^b + c, where a and c are complex coefficients and b is an
 * integer coefficient.
 * </p>
 * 
 */
public class BinomialInputFunction extends InputFunction {

	private Complex aValue;

	private int bValue;

	private Complex cValue;

	/**
	 * Calls the superclass constructor to set up the m value and coefficient
	 * array and then fills that array with the coefficient parameters, a, b, &
	 * c.
	 * 
	 * @param mValue
	 * @param a
	 * @param b
	 * @param c
	 * @throws IllegalArgumentException
	 *             if a is zero
	 * @see InputFunction#InputFunction(int, int)
	 */
	public BinomialInputFunction(int mValue, Complex a, int b, Complex c)
			throws IllegalArgumentException {
		super(3, mValue);
		if (a.equals(Complex.ZERO))
			throw new IllegalArgumentException("a zero");
		Complex temp = new Complex(b, 0);
		if (temp.equals(Complex.ZERO))
			throw new IllegalArgumentException("n zero");
		aValue = a;
		bValue = b;
		cValue = c;
		/*
		 * The following 4 lines are required to do Editing and Cloning on a
		 * function. This is needed to be done because aValue, bValue, and
		 * cValue were created above for convenience when writing code.
		 */
		coefficientArray[0] = a;
		coefficientArray[1] = temp;
		coefficientArray[2] = c;
	}

	public Complex evaluateBackwardsRandom(Complex seed) {
		Random random = new Random();
		Complex w = seed;
		int bAbs = Math.abs(bValue);
		for (int i = 0; i < getM(); i++) {
			w = w.subtract(cValue);
			w = w.divide(aValue);

			List<Complex> roots = w.nthRoot(bAbs);
			w = roots.get(random.nextInt(bAbs));

			if (bValue < 0) {
				w = Complex.ONE.divide(w);
			}
		}
		return w;
	}

	public Complex evaluateForwards(Complex seed) {
		Complex result = seed;
		for (int i = 0; i < getM(); i++) {
			result = result.pow(new Complex(bValue, 0));
			result = result.multiply(aValue);
			result = result.add(cValue);
		}

		return result;
	}

	public Complex evaluateFunction(Complex seed) {
		Complex result = seed;
		result = result.pow(new Complex(bValue, 0));
		result = result.multiply(aValue);
		result = result.add(cValue);
		return result;
	}

	public Complex[] evaluateBackwardsFull(Complex seed) {

		// start with the seed
		List<Complex> result = new ArrayList<Complex>();
		result.add(seed);

		// loop M times
		for (int i = 0; i < getM(); i++) {

			// collect the results of this pass in a temp list
			List<Complex> tempResult = new ArrayList<Complex>();
			for (Complex number : result) {
				number = number.subtract(cValue);
				number = number.divide(aValue);
				List<Complex> roots = number.nthRoot(Math.abs(bValue));

				// if bValue is negative, divide all the roots and add them to
				// the temporary result list, otherwise just add them to the
				// list
				if (bValue < 0) {
					for (Complex root : roots) {
						tempResult.add(Complex.ONE.divide(root));
					}
				} else {
					tempResult.addAll(roots);
				}
			}

			// the temp results become the list to work from for the next pass
			// in the loop
			result = tempResult;
		}

		// return the final results
		return result.toArray(new Complex[] {});
	}

	public String toString() {
		return "f" + getSubscript() + "(z) = "
				+ ComplexNumberUtils.complexToString(aValue) + "z^ " + bValue
				+ " + " + ComplexNumberUtils.complexToString(cValue) + ", m = "
				+ getM();
	}

}
