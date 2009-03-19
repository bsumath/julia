package edu.bsu.julia.input;

import java.util.Vector;

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
		aValue = a;
		nValue = n;
	}
	
	/**
	 * This method has a random element.
	 */
	public Complex evaluateBackwardsRandom(Complex seed) {
		Complex w = seed;
		for (int i = 0; i < getM(); i++) {
			int randomInt = (int) Math.floor( nValue*(Math.random()) );
			w = aValue.divide(w);
			Complex[] nrt = w.nthRoot(nValue).toArray(new Complex[] {});
			w = nrt[randomInt];
		}
		return w;
	}

	public Complex evaluateForwards(Complex seed) {
		Complex w = seed;
		for (int i = 0; i < getM(); i++) {
			w = w.pow(new Complex(nValue, 0));
			w = aValue.divide(w);
		}
		return w;
	}

	public Complex evaluateFunction(Complex seed) {
		Complex w = seed;
		w = w.pow(new Complex(nValue, 0));
		w = aValue.divide(w);
		return w;
	}

	/**
	 * This method returns an array with n number of values. See the superclass method
	 * description for a more general account.
	 */
	public Complex[] evaluateBackwardsFull(Complex seed) {
		// In this case there is a difference between the random & full backwards evaluations.
		Complex w = seed;
		Vector<Complex> results = new Vector<Complex>();
		results.add(w);
		Complex[] nrt = new Complex[nValue];
		for (int i = 0; i < getM(); i++) {
			for (int j = 0; j < Math.pow(nValue, i); j++) {
				w = (Complex) results.firstElement();
				results.remove(0);
				w = aValue.divide(w);
				nrt = w.nthRoot(nValue).toArray(new Complex[] {});
				for (int k = 0; k < nValue; k++) {
					results.add(nrt[k]);
				}
			}
		}
		results.trimToSize();
		Complex[] finalResults = new Complex[results.size()];
		results.toArray(finalResults);
		return finalResults;
	}

	public String toString() {
		Complex n = new Complex(nValue,0);
		return new String("f" + getSubscript() + "(z) = "
				+ ComplexNumberUtils.complexToString(aValue) + "/z^ " 
				+ ComplexNumberUtils.complexToString(n) + ", m = "
				+ getM());
	}

}
