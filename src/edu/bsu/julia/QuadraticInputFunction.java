package edu.bsu.julia;

import java.util.Vector;
/**
 * 
 * <h3>Description</h3>
 * <p>QuadraticInputFunction is a subclass of InputFunction representing a 
 * function of the form az^2 + bz + c, where a, b, and c are coefficients and 
 * all are complex numbers.
 *
 */
public class QuadraticInputFunction extends InputFunction {
	
	/**
	 * A convenience constant representing the real number, -1.
	 */
	private static final ComplexNumber NEG_ONE = new ComplexNumber(-1, 0);
	/**
	 * A convenience constant representing the real number, 4.
	 */
	private static final ComplexNumber FOUR = new ComplexNumber(4, 0);
	/**
	 * A convenience constant representing the real number, 2.
	 */
	private static final ComplexNumber TWO = new ComplexNumber(2, 0);
	
	/**
	 * Calls the superclass constructor to set up the m value and coefficient 
	 * array and then fills that array with the coefficient parameters, a, 
	 * b, and c.  
	 * @param mValue
	 * @param a
	 * @param b
	 * @param c
	 * @throws IllegalArgumentException if a is zero
	 * @see InputFunction#InputFunction(int, int)
	 */
	public QuadraticInputFunction(int mValue, ComplexNumber a, ComplexNumber b,
			ComplexNumber c) 
		throws IllegalArgumentException {
		super(3, mValue);
		if (a.isZero()) throw new IllegalArgumentException("a zero");
		coefficientArray[0] = a;
		coefficientArray[1] = b;
		coefficientArray[2] = c;
		super.setType(2);
	}
	
	public ComplexNumber evaluateBackwardsRandom(ComplexNumber seed){
		ComplexNumber a = coefficientArray[0];
		ComplexNumber b = coefficientArray[1];
		ComplexNumber c = coefficientArray[2];
		ComplexNumber w = seed.clone();
		ComplexNumber temp;
		ComplexNumber negB = b.multiply(NEG_ONE);
		ComplexNumber sqrB = b.multiply(b);
		
		for (int i=0; i<getM(); i++){	
			temp = sqrB.subtract((a.multiply(c.subtract(w))).multiply(FOUR));
			ComplexNumber[] sqrt = temp.squareRoot();
			double randomNumber = Math.random();
			//potential ArithmeticExceptions should be caught at thread level
			if (randomNumber<.5) w = negB.add(sqrt[0]).divide(a.multiply(TWO));
			else w = negB.add(sqrt[1]).divide(a.multiply(TWO));
		}
		return w;
	}
	
	public ComplexNumber evaluateForwards(ComplexNumber seed) {
		ComplexNumber a = coefficientArray[0];
		ComplexNumber b = coefficientArray[1];
		ComplexNumber c = coefficientArray[2];
		ComplexNumber result = new ComplexNumber();
		ComplexNumber w = seed.clone();
		
		for(int i = 0; i<getM(); i++) {
			result = a.multiply(w.multiply(w));
			result = (result.add(b.multiply(w))).add(c);
			w = result;
		}
		return result;
	}
	
	
	public ComplexNumber evaluateFunction(ComplexNumber seed) {
		ComplexNumber a = coefficientArray[0];
		ComplexNumber b = coefficientArray[1];
		ComplexNumber c = coefficientArray[2];
		ComplexNumber result = new ComplexNumber();
		ComplexNumber w = seed.clone();
		
		result = a.multiply(w.multiply(w));
		result = (result.add(b.multiply(w))).add(c);
		w = result;
		
		return result;
	}
	/**
	 * This method returns 2^m complex numbers for each seed.  See the 
	 * superclass method for a more general account.
	 */
	public ComplexNumber[] evaluateBackwardsFull(ComplexNumber seed){
		ComplexNumber a = coefficientArray[0];
		ComplexNumber b = coefficientArray[1];
		ComplexNumber c = coefficientArray[2];
		ComplexNumber w = seed.clone();
		ComplexNumber temp;
		ComplexNumber negB = b.multiply(NEG_ONE);
		ComplexNumber sqrB = b.multiply(b);
		Vector<ComplexNumber> results = new Vector<ComplexNumber>();
		results.add(w);
		ComplexNumber[] sqrt = new ComplexNumber[2];
		
		for (int i = 0; i<getM(); i++) {
			for (int j = 0; j<Math.pow(2, i); j++) {
				w = (ComplexNumber)results.firstElement();
				results.remove(0);	
				temp = sqrB.subtract((a.multiply(c.subtract(w))).multiply(FOUR));
				sqrt = temp.squareRoot();
				//potential ArithmeticExceptions should be caught at thread level
				results.add(negB.add(sqrt[0]).divide(a.multiply(TWO)));
				results.add(negB.add(sqrt[1]).divide(a.multiply(TWO)));
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
		ComplexNumber c = coefficientArray[2];
		
		return new String("f" + getSubscript() + "(z) = " +
				a + "z^2 + " + b + "z + " + 
				c + ", m = "+ getM());
	}

}
