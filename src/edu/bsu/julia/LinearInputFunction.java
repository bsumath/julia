package edu.bsu.julia;
/**
 * 
 * <h3>Description</h3>
 * <p>LinearInputFunction is a subclass of InputFunction representing a function
 * of the form:  a*z + b, where a and b are coefficients and all three are 
 * complex numbers.  </p>
 *
 */
public class LinearInputFunction extends InputFunction {
	/**
	 * Calls the superclass constructor to set up the m value and coefficient
	 * array and then fills that array with the coefficient parameters, a and
	 * b.
	 * @param mValue
	 * @param a
	 * @param b
	 * @throws IllegalArgumentException if a is zero.
	 * @see InputFunction#InputFunction(int, int)
	 */
	public LinearInputFunction(int mValue, ComplexNumber a, ComplexNumber b) 
		throws IllegalArgumentException {
		super(2, mValue);
		if (a.isZero()) throw new IllegalArgumentException("a zero");
		coefficientArray[0] = a;
		coefficientArray[1] = b;
		super.setType(3);
	}
	
	/**
	 * This method has no random element as the inverse of a linear function 
	 * is always a single value.  See the superclass method description for 
	 * a more general account.
	 */
	public ComplexNumber evaluateBackwardsRandom(ComplexNumber seed) {
		ComplexNumber a = coefficientArray[0];
		ComplexNumber b = coefficientArray[1];
		ComplexNumber w = seed.clone();
		for(int i = 0; i<getM(); i++) {
			w = w.subtract(b);
			//potential ArithmeticExceptions should be handled at thread level
			w = w.divide(a);
		}
		return w;
	}
	
	public ComplexNumber evaluateForwards(ComplexNumber seed) {
		ComplexNumber a = coefficientArray[0];
		ComplexNumber b = coefficientArray[1];
		ComplexNumber w = seed.clone();
		for(int i = 0; i<getM(); i++) {
			w = a.multiply(w);
			w = w.add(b);
		}
		return w;
	}
	
	public ComplexNumber evaluateFunction(ComplexNumber seed) {
		ComplexNumber a = coefficientArray[0];
		ComplexNumber b = coefficientArray[1];
		ComplexNumber w = seed.clone();
		w = a.multiply(w);
		w = w.add(b);
		
		return w;
	}
	
	/**
	 * This method returns an array of a single value.  See the superclass
	 * method description for a more general account.
	 */
	public ComplexNumber[] evaluateBackwardsFull(ComplexNumber seed) {
		//for a linear function, there is no difference between the 
		//random and full methods.
		ComplexNumber[] result = {evaluateBackwardsRandom(seed)};
		return result;
	}
	
	public String toString() {
		ComplexNumber a = coefficientArray[0];
		ComplexNumber b = coefficientArray[1];
		
		return new String("f" + getSubscript() + "(z) = " + a + "z + " 
				+ b + ", m = " + getM());
	}

}
