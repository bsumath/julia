package edu.bsu.julia;

import java.math.RoundingMode;
import java.text.DecimalFormat;

import org.apache.commons.math.complex.Complex;

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
public final class ComplexNumberUtils {

	/**
	 * private constructor so that this class can only be used staticly
	 */
	private ComplexNumberUtils() {

	}

	/**
	 * Provides a string representation of the complex number in the form: "{x,
	 * y}". "Computerized Scientific Notation" (the E form) is replaced with
	 * standard scientific notation.
	 * 
	 * @param complex
	 *            {@link Complex} to convert to a {@link String}
	 * @return the {@link String} representation of the
	 *         {@link ComplexNumberUtils}
	 */
	public static String complexToString(Complex complex) {
		return ("{" + formatDouble(complex.getReal()) + ", "
				+ formatDouble(complex.getImaginary()) + "}").replace("E",
				"*10^");
	}

	/**
	 * creates a {@link String} where the x and y are separated by a space
	 * 
	 * @param complex
	 *            {@link Complex} to export
	 * @return a {@link String} representing the {@link ComplexNumberUtils}.
	 */
	public static String exportString(Complex complex) {
		return (formatDouble(complex.getReal()) + " " + formatDouble(complex
				.getImaginary())).replace("E", "*10^");
	}

	/**
	 * used by {@link #toString()} and {@link #exportString()}
	 * 
	 * @param number
	 *            the double to format
	 * @return the formatted string
	 */
	private static String formatDouble(double number) {
		DecimalFormat df;
		if (number < 0.00001d || number > 99999d) {
			df = new DecimalFormat("0.#####E0");
		} else {
			df = new DecimalFormat("###.#####");
		}

		df.setRoundingMode(RoundingMode.HALF_UP);

		return df.format(number);
	}

	/**
	 * method to create a {@link ComplexNumberUtils} from a {@link String}. Must
	 * be able to parse the {@link String} created by
	 * {@link ComplexNumberUtils#toString()}
	 * 
	 * @param s
	 *            the {@link String} to parse
	 * @return the resulting {@link ComplexNumberUtils} on success, null on error
	 */
	public static Complex parseComplexNumber(String s) {
		// remove the braces and split on the comma
		s = s.replace("{", "").replace("}", "").replace(",", "");
		s = s.replace("\t", " ");
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
			return new Complex(x, y);
		} catch (NumberFormatException e) {
			return null;
		}
	}
}
