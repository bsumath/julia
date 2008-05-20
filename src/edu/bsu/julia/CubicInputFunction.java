package edu.bsu.julia;

import java.util.Vector;
/**
 * 
 * <h3>Description</h3>
 * <p>CubicInputFunction is a subclass of InputFunction representing a function
 * of the form: a*z^3 + b, where a and b are coefficients and all three are
 * complex numbers.</p>
 *
 */
public class CubicInputFunction extends InputFunction {
	/**
	 * Calls the superclass constructor to set up the m value and coefficient
	 * array and then fills that array with the coefficient parameters, a 
	 * and b.
	 * @param mValue
	 * @param a
	 * @param b
	 * @throws IllegalArgumentException if a is zero.
	 * @see InputFunction#InputFunction(int, int)
	 */
	public CubicInputFunction(int mValue, ComplexNumber a, ComplexNumber b) 
		throws IllegalArgumentException {
		super(2, mValue);
		if (a.isZero()) throw new IllegalArgumentException("a zero");
		coefficientArray[0] = a;
		coefficientArray[1] = b;
		super.setType(1); 
	}
	
	public ComplexNumber evaluateBackwardsRandom(ComplexNumber seed){
		ComplexNumber a = coefficientArray[0];
		ComplexNumber b = coefficientArray[1];
		ComplexNumber w = seed.clone();
		for (int i=0;i<getM();i++){
			double randomNumber = Math.random();
			//potential ArithmeticExceptions should be handled at thread level
			w = (w.subtract(b)).divide(a);
			ComplexNumber[] cbrt = w.cubeRoot();
			if (randomNumber<=(1.0/3.0)) w = cbrt[0];
			else {
				if (randomNumber<=(2.0/3.0)) w = cbrt[1];
				else w = cbrt[2];
			}
		}
		return w;
	}

	public ComplexNumber evaluateForwards(ComplexNumber seed){
		ComplexNumber a = coefficientArray[0];
		ComplexNumber b = coefficientArray[1];
		ComplexNumber result = seed;
		for (int i=0; i<getM(); i++) {
			//the variable z represents z^3 in the equation
			ComplexNumber z = (result.multiply(result)).multiply(result);
			result = (a.multiply(z)).add(b);
		}
		return result;
	}
	/**
	 * This method returns 3^m complex numbers for each seed.  See the 
	 * superclass method description for a more general account.
	 */
	
	
	public ComplexNumber evaluateFunction(ComplexNumber seed){
		ComplexNumber a = coefficientArray[0];
		ComplexNumber b = coefficientArray[1];
		ComplexNumber result = seed;
			//the variable z represents z^3 in the equation
		ComplexNumber z = (result.multiply(result)).multiply(result);
		result = (a.multiply(z)).add(b);
		
		return result;
	}
	
	
	public ComplexNumber[] evaluateBackwardsFull(ComplexNumber seed){
		ComplexNumber a = coefficientArray[0];
		ComplexNumber b = coefficientArray[1];
		ComplexNumber w = seed.clone();
		Vector<ComplexNumber> results = new Vector<ComplexNumber>();
		results.add(w);
		ComplexNumber[] cbrt = new ComplexNumber[3];
		for (int i = 0; i<getM(); i++) {
			for (int j = 0; j<Math.pow(3, i); j++) {
				w = (ComplexNumber)results.firstElement();
				results.remove(0);
				//potential ArithmeticExceptions should be handled at thread level
				w = (w.subtract(b)).divide(a);
				cbrt = w.cubeRoot();
				results.add(cbrt[0]);
				results.add(cbrt[1]);
				results.add(cbrt[2]);
			}
		}
		results.trimToSize();
		ComplexNumber[] finalResults = new ComplexNumber[results.size()];
		results.toArray(finalResults);
		return finalResults;
	}
	
	public String toString() {
		ComplexNumber a = coefficientArray[0];
		ComplexNumber b = coefficientArray[1];
		
		return new String("f" + getSubscript() + "(z) = " + a + "z^3 + " 
				+ b + ", m = " + getM());
	}

}
