/*
 *   1.  HAVE NOT GOT THE BACKWARDS RANDOM OR FULL WORKING YET!
 *   2.  THE CHECK IF THE CVALUE IS ZERO RETURNS THE ERROR MESSAGE FOR THE AVALUE
 *   3.  NEED TO GET THE EDIT, COPY AND WHATEVER ELSE WORKING AS WELL
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
 * of the form: a*z^b + c/z^b, where a and c are complex coefficients and b is an
 * integer coefficient.
 * </p>
 * 
 */
public class TempInputFunction extends InputFunction {

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
	public TempInputFunction(int mValue, Complex a, int b, Complex c)
			throws IllegalArgumentException {
		super(3, mValue);
		Complex temp = new Complex(b, 0);
		Complex test = a.multiply(c);
		/*if (a.equals(Complex.ZERO))
			throw new IllegalArgumentException("a zero");*/
		if (test.equals(Complex.ZERO))
			throw new IllegalArgumentException("coefficient zero");
		if (temp.equals(Complex.ZERO))
			throw new IllegalArgumentException("n zero");
		if (b < 0)
			throw new IllegalArgumentException("n negative");
		/*if (c.equals(Complex.ZERO))
			throw new IllegalArgumentException("c zero");*/
		
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
		Complex four = new Complex(4,0);
		Complex two = new Complex(2,0);
		Complex fourac = four.multiply(aValue).multiply(cValue);
		Complex denominator = two.multiply(aValue);
		Complex result;
		for (int i = 0; i < getM(); i++) {
				//w^2 - 4ac
				result = w.pow(two).subtract(fourac);
				//Finding the +/- value of the square root
				List<Complex> results = result.nthRoot(2);
				//randomly choosing a root
				result = results.get(random.nextInt(2));
				result = w.add(result);
				w = result.divide(denominator);
				//Finding the "nth" roots (here we used b instead of n) & randomly choosing one root
				List<Complex> roots = w.nthRoot(bValue);
				w = roots.get(random.nextInt(bValue));
		}
		return w;
	}

	public Complex evaluateForwards(Complex seed) {
		Complex result = seed;
		Complex w1,w2;
		for (int i = 0; i < getM(); i++) {
			result = result.pow(new Complex(bValue, 0));
			w1 = result.multiply(aValue);
			w2 = cValue.divide(result);
			result = w1.add(w2);
		}

		return result;
	}

	public Complex evaluateFunction(Complex seed) {
		Complex result = seed;
		Complex w1,w2;
		result = result.pow(new Complex(bValue, 0));
		w1 = result.multiply(aValue);
		w2 = cValue.divide(result);
		result = w1.add(w2);
		return result;
	}

	public Complex[] evaluateBackwardsFull(Complex seed) {					/* NOT DONE! ALL OLD CODE FROM BINOMIAL */		
		// start with the seed
		List<Complex> result = new ArrayList<Complex>();
		result.add(seed);
		Complex four = new Complex(4,0);
		Complex two = new Complex(2,0);
		//4ac
		Complex fourac = four.multiply(aValue).multiply(cValue);
		Complex denominator = two.multiply(aValue);

		// loop M times
		for (int i = 0; i < getM(); i++) {

			// collect the results of this pass in a temp list
			List<Complex> tempResult = new ArrayList<Complex>();
			for (Complex number : result) {
				//seed^2 - 4ac
				Complex tempvalue = number.pow(two).subtract(fourac);		//CHECK THIS TO MAKE SURE IT WORKS
				//Finding the +/- value of the square root
				List<Complex> templist = tempvalue.nthRoot(2);
				//There might be a better way to do what is below
				Complex r1 = number.add(templist.get(0));
				Complex r2 = number.add(templist.get(1));
				Complex n1 = r1.divide(denominator);
				Complex n2 = r2.divide(denominator);
				List<Complex> roots1 = n1.nthRoot(bValue);
				List<Complex> roots2 = n2.nthRoot(bValue);
				tempResult.addAll(roots1);
				tempResult.addAll(roots2);												//MAKE SURE LAST 2 LINES TO NOT OVERWRITE ANYTHING
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
				+ " + " + ComplexNumberUtils.complexToString(cValue) + "/" 
				+ ComplexNumberUtils.complexToString(aValue) + "z^ " + bValue + ", m = "
				+ getM();
	}

}
