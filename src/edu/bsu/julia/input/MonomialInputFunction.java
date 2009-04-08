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

import java.util.Vector;

import org.apache.commons.math.complex.Complex;

import edu.bsu.julia.ComplexNumberUtils;

/**
 * 
 * <h3>Description</h3>
 * <p>
 * MonomialInputFunction is a subclass of InputFunction representing a function
 * of the form: a*z^b + c, where a and c are complex coefficients and b is an integer coefficient.
 * </p>
 * 
 */
public class MonomialInputFunction extends InputFunction {

	private Complex aValue;
	
	private int bValue;
	
	private Complex cValue;

	/**
	 * Calls the superclass constructor to set up the m value and coefficient
	 * array and then fills that array with the coefficient parameters, a, b, & c.
	 * 
	 * @param mValue
	 * @param a
	 * @param b
	 * @param c
	 * @throws IllegalArgumentException
	 *             if a is zero
	 * @see InputFunction#InputFunction(int, int)
	 */
	public MonomialInputFunction(int mValue, Complex a, int b, Complex c)
			throws IllegalArgumentException {
		super(3, mValue);
		if (a.equals(Complex.ZERO))
			throw new IllegalArgumentException("a zero");
		Complex temp = new Complex(b,0);
		if (temp.equals(Complex.ZERO))
			throw new IllegalArgumentException("n zero");
		aValue = a;
		bValue = b;
		cValue = c;
		/*
		 * The following 4 lines are required to do Editing and Cloning on a function.
		 * This is needed to be done because aValue, bValue, and cValue were created above for 
		 * convenience when writing code.
		 */
		coefficientArray[0] = a;
		coefficientArray[1] = temp;
		coefficientArray[2] = c;
	}
	
	/*
	 * Dr. Stankewitz:  Here is the code where I am trying to fix things!
	 */
	
	public Complex evaluateBackwardsRandom(Complex seed) {
		Complex w = seed;
		Complex a = coefficientArray[0];
		int b = (int) coefficientArray[1].getReal();
		Complex c = coefficientArray[2];
		
		int temp1 = Math.abs(b);
		for (int i = 0; i < getM(); i++) {
			int randomInt = (int) Math.floor( b*(Math.random()) );
			if (b > 0) {
				w = w.subtract(c);
				w = w.divide(a);
			}
			if (b < 0) {
				w = w.subtract(c);
				w = a.divide(w);
			}
			Complex[] nrt = w.nthRoot(temp1).toArray(new Complex[] {});
			w = nrt[randomInt];
		}
		
		/*
		int temp1 = Math.abs(bValue);
		for (int i = 0; i < getM(); i++) {
			int randomInt = (int) Math.floor( bValue*(Math.random()) );
			if (bValue > 0) {
				w = w.subtract(cValue);
				w = w.divide(aValue);
			}
			if (bValue < 0) {
				w = w.subtract(cValue);
				w = aValue.divide(w);
			}
			Complex[] nrt = w.nthRoot(temp1).toArray(new Complex[] {});
			w = nrt[randomInt];
		}
		*/
		return w;
	}


	public Complex evaluateForwards(Complex seed) {
		Complex w = seed;
		for (int i = 0; i < getM(); i++) {
			w = w.pow(new Complex(bValue, 0));
			w = aValue.multiply(w);
			w = w.add(cValue);
		}

		return w;
	}

	public Complex evaluateFunction(Complex seed) {
		Complex w = seed;
		w = w.pow(new Complex(bValue, 0));
		w = aValue.multiply(w);
		w = w.add(cValue);
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
		Complex[] nrt = new Complex[bValue];
		for (int i = 0; i < getM(); i++) {
			for (int j = 0; j < Math.pow(bValue, i); j++) {
				w = (Complex) results.firstElement();
				results.remove(0);
				w = w.subtract(cValue);
				w = w.divide(aValue);
				nrt = w.nthRoot(bValue).toArray(new Complex[] {});
				for (int k = 0; k < bValue; k++) {
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
		return new String("f" + getSubscript() + "(z) = "
				+ ComplexNumberUtils.complexToString(aValue) + "/z^ " 
				+ bValue + " + " + ComplexNumberUtils.complexToString(cValue) 
				+ ", m = " + getM());
	}

}
