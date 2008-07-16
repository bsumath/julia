package edu.bsu.julia;

/**
 * 
 * <h3>Description</h3>
 * <p>
 * This class represents a complex number of the form x + bi where x and y are
 * real numbers and i is the square root of -1.
 * </p>
 * <p>
 * Methods are provided for simple mathematical manipulation of complex numbers,
 * as well as get methods.
 * </p>
 * 
 * <p>
 * THIS CLASS MUST BE IMMUTABLE
 * </p>
 */
public final class ComplexNumber {
	/**
	 * The real part of a complex number.
	 */
	private final double x;
	/**
	 * When multiplied by i, the imaginary part of a complex number.
	 */
	private final double y;
	/**
	 * The constant alpha is used in determining the three cube roots of a
	 * number. The primary cube root does not use alpha, but the secondary is
	 * alpha times the primary and the third is alpha squared times the primary.
	 */
	private static final ComplexNumber ALPHA = new ComplexNumber(Math
			.cos(2 * Math.PI / 3), Math.sin(2 * Math.PI / 3));

	/**
	 * The default constructor creates a new complex number of the form: 0 + 0 *
	 * i.
	 */
	public ComplexNumber() {
		this(0, 0);
	}

	/**
	 * This constructor creates a complex number of the form: xValue + yValue *
	 * i.
	 * 
	 * @param xValue
	 * @param yValue
	 */
	public ComplexNumber(double xValue, double yValue) {
		x = xValue;
		y = yValue;
	}

	/**
	 * @return x: The real part of the complex number.
	 */
	public double getX() {
		return x;
	}

	/**
	 * @return y: When multiplied by i, the imaginary part of the complex
	 *         number.
	 */
	public double getY() {
		return y;
	}

	/**
	 * Checks to see if this complex number is of the form 0 + 0 * i. The x and
	 * y values are doubles, so the checking this method does is highly inexact.
	 * It is meant solely for use in checking user input. Do not use on any
	 * complex number that has been mathematically changed in any way.
	 * 
	 * @return A boolean indicating whether this complex number is 0 (true) or
	 *         not (false).
	 */
	public boolean isZero() {
		if (x == 0 && y == 0)
			return true;
		else
			return false;
	}

	/**
	 * Adds two complex numbers.
	 * 
	 * @param a
	 * @return A new complex number representing <b>this</b> + a.
	 */
	public ComplexNumber add(ComplexNumber a) {
		return new ComplexNumber(x + a.getX(), y + a.getY());
	}

	/**
	 * Subtracts the complex number a from <b>this</b>.
	 * 
	 * @param a
	 * @return A new complex number representing <b>this</b> - a.
	 */
	public ComplexNumber subtract(ComplexNumber a) {
		return new ComplexNumber(x - a.getX(), y - a.getY());
	}

	/**
	 * Multiplies <b>this</b> by the complex number a.
	 * 
	 * @param a
	 * @return A new complex number representing <b>this</b> * a.
	 */
	public ComplexNumber multiply(ComplexNumber a) {
		double ax = a.getX();
		double ay = a.getY();
		return new ComplexNumber(x * ax - y * ay, x * ay + y * ax);
	}

	/**
	 * Divides <b>this</b> by the complex number a.
	 * 
	 * @param a
	 * @return A new complex number representing <b>this</b> / a.
	 */
	public ComplexNumber divide(ComplexNumber a) {
		double ax = a.getX();
		double ay = a.getY();
		double divisor = Math.pow(ax, 2) + Math.pow(ay, 2);
		// potential ArithmeticExceptions should be caught at thread level
		double xResult = (x * ax + y * ay) / divisor;
		double yResult = (ax * y - x * ay) / divisor;
		return new ComplexNumber(xResult, yResult);
	}

	/**
	 * Computes the primary and secondary square root of a complex number.
	 * 
	 * @return An array of size 2, with the primary root in position 0 and the
	 *         secondary root in position 1.
	 */
	public ComplexNumber[] squareRoot() {
		ComplexNumber[] result = new ComplexNumber[2];
		double xResult = Math.pow(x * x + y * y, .25) * Math.cos(arg() / 2);
		double yResult = Math.pow(x * x + y * y, .25) * Math.sin(arg() / 2);
		ComplexNumber baseResult = new ComplexNumber(xResult, yResult);
		result[0] = baseResult;
		result[1] = baseResult.multiply(new ComplexNumber(-1, 0));
		return result;
	}

	/**
	 * Computes the primary, secondary and tertiary roots of a complex number.
	 * 
	 * @return An array of size 3, with the primary root in position 0, the
	 *         secondary root in position 1, and the tertiary root in position
	 *         2.
	 */
	public ComplexNumber[] cubeRoot() {
		ComplexNumber[] result = new ComplexNumber[3];
		double xResult = Math.pow(x * x + y * y, (1.0 / 6.0))
				* Math.cos(arg() / 3);
		double yResult = Math.pow(x * x + y * y, (1.0 / 6.0))
				* Math.sin(arg() / 3);
		ComplexNumber baseResult = new ComplexNumber(xResult, yResult);
		result[0] = baseResult;
		result[1] = ALPHA.multiply(baseResult);
		result[2] = (ALPHA.multiply(ALPHA)).multiply(baseResult);
		return result;
	}

	public ComplexNumber polarConvertion(ComplexNumber a) {
		double r = a.getX();
		double theta = a.getY();
		double x = r * (Math.cos(theta));
		double y = r * (Math.sin(theta));
		x = (int) (x * 100000);
		x = x / 100000;
		y = (int) (y * 100000);
		y = y / 100000;
		ComplexNumber result = new ComplexNumber(x, y);
		return result;
	}

	/**
	 * Used by {@link #squareRoot()} and {@link #cubeRoot()} to compute the
	 * mathematical argument of a complex number. This method is sensitive to
	 * division by zero. If the complex number is 0, the argument returned will
	 * be zero.
	 * 
	 * @return A double representing the mathematical argument of the complex
	 *         number.
	 */
	private double arg() {
		double base = 0;
		try {
			base = Math.asin(y / Math.sqrt(x * x + y * y));
		} catch (ArithmeticException e) {
			// if division by zero occurs, base remains zero.
		}
		if (x >= 0)
			return base;
		else {
			if (y >= 0)
				return Math.PI - base;
			else
				return -Math.PI - base;
		}
	}

	/**
	 * Provides a string representation of the complex number in the form: "{x,
	 * y}". "Computerized Scientific Notation" (the E form) is replaced with
	 * standard scientific notation.
	 * @return the {@link String} representation of the {@link ComplexNumber}
	 */
	public String toString() {
		return ("{" + x + ", " + y + "}").replace("E", "*10^");
	}

	/**
	 * creates a {@link String} where the x and y are separated by a space
	 * 
	 * @return a {@link String} representing the {@link ComplexNumber}.
	 */
	public String exportString() {
		return (x + " " + y).replace("E", "*10^");
	}

	/**
	 * object comparison
	 */
	public boolean equals(Object o) {
		try {
			ComplexNumber c = (ComplexNumber) o;
			return x == c.x && y == c.y;
		} catch (ClassCastException e) {
			return false;
		}
	}

	/**
	 * method to create a {@link ComplexNumber} from a {@link String}. Must be
	 * able to parse the {@link String} created by
	 * {@link ComplexNumber#toString()}
	 * 
	 * @param s
	 *            the {@link String} to parse
	 * @return the resulting {@link ComplexNumber} on success, null on error
	 */
	public static ComplexNumber parseComplexNumber(String s) {
		// remove the braces and split on the comma
		s = s.replace("{", "").replace("}", "").replace(",", "");
		String[] parts = s.split(" ");
		if (parts.length != 2)
			return null;

		// trim the strings and fix the scientific notation
		parts[0] = parts[0].trim().replace("*10^", "E");
		parts[1] = parts[1].trim().replace("*10^", "E");

		// try to parse the doubles and create the ComplexNumber
		try {
			double x = Double.parseDouble(parts[0]);
			double y = Double.parseDouble(parts[1]);
			return new ComplexNumber(x, y);
		} catch (NumberFormatException e) {
			return null;
		}
	}
}
