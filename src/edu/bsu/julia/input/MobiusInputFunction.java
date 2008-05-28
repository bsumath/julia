package edu.bsu.julia.input;

import edu.bsu.julia.ComplexNumber;

/**
 * 
 * <h3>Description</h3>
 * <p>MobiusInputFunction is a subclass of InputFunction representing a 
 * function of the form: (az + b)/(cz +d), where a, b, c, and d are 
 * coefficients, z is the variable, and all are complex numbers.</p>
 *
 */
public class MobiusInputFunction extends InputFunction {
	/**
	 * Calls the superclass constructor to set up the m value and coefficient 
	 * array, then fills that array with the coefficient parameters, a 
	 * through d.
	 * @param mValue
	 * @param a
	 * @param b
	 * @param c
	 * @param d
	 * @see InputFunction#InputFunction(int, int)
	 */
	public MobiusInputFunction(int mValue, ComplexNumber a, ComplexNumber b,
			ComplexNumber c, ComplexNumber d) {
		super(4, mValue);
		coefficientArray[0] = a;
		coefficientArray[1] = b;
		coefficientArray[2] = c;
		coefficientArray[3] = d;
	}
	
	/**
	 * This method has no random element as the inverse of a mobius function 
	 * is a single value.  See the superclass method description for a more 
	 * general account.
	 */
	public ComplexNumber evaluateBackwardsRandom(ComplexNumber seed){
		ComplexNumber a = coefficientArray[0];
		ComplexNumber b = coefficientArray[1];
		ComplexNumber c = coefficientArray[2];
		ComplexNumber d = coefficientArray[3];
		ComplexNumber w = seed.clone();
		ComplexNumber x;
		ComplexNumber y;
		
		for (int i=0;i<getM();i++){
			x = b.subtract(w.multiply(d));
			y = (w.multiply(c)).subtract(a);
			//potential ArithmeticExceptions should be handled at thread level
			w = x.divide(y);
		}
		return w;
	}
	
	public ComplexNumber evaluateForwards(ComplexNumber seed){
		ComplexNumber a = coefficientArray[0];
		ComplexNumber b = coefficientArray[1];
		ComplexNumber c = coefficientArray[2];
		ComplexNumber d = coefficientArray[3];
		ComplexNumber w = seed.clone();
		ComplexNumber x;
		ComplexNumber y;
		
		for (int i = 0; i<getM(); i++) {
			x = a.multiply(w).add(b);
			y = c.multiply(w).add(d);
			//potential ArithmeticExceptions should be handled at thread level
			w = x.divide(y);
		}
		return w;
	}
	
	
	public ComplexNumber evaluateFunction(ComplexNumber seed){
		ComplexNumber a = coefficientArray[0];
		ComplexNumber b = coefficientArray[1];
		ComplexNumber c = coefficientArray[2];
		ComplexNumber d = coefficientArray[3];
		ComplexNumber w = seed.clone();
		ComplexNumber x;
		ComplexNumber y;
		
		x = a.multiply(w).add(b);
		y = c.multiply(w).add(d);
		//potential ArithmeticExceptions should be handled at thread level
		w = x.divide(y);
		
		return w;
	}
	/**
	 * This method returns an array of a single value.  See the superclass 
	 * method description for a more general account.
	 */
	public ComplexNumber[] evaluateBackwardsFull(ComplexNumber seed){
		//for a mobius function, there is no difference between the random
		//and full methods
		ComplexNumber[] result = {evaluateBackwardsRandom(seed)};
		return result;
	}
	
	public String toString() {
		ComplexNumber a = coefficientArray[0];
		ComplexNumber b = coefficientArray[1];
		ComplexNumber c = coefficientArray[2];
		ComplexNumber d = coefficientArray[3];
		
		return new String("f" + getSubscript() + "(z) = " + 
				a + "z + " + b + " / " + c + "z + " +
				d + ", m = " + getM());
	}

}
