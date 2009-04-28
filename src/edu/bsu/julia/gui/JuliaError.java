package edu.bsu.julia.gui;

import java.awt.Frame;

import javax.swing.JOptionPane;

/**
 * an enumerated type for julia errors. each error has a message and a title
 * 
 * @author Ben Dean
 */
public enum JuliaError {
	DEFAULT("An unknown error has occured.", "Error"),

	M_INTEGER_ERROR("M values must be positive " + "\ninteger values.",
			"Number Format Error"),

	COEFFICIENT_FORMAT_ERROR("Coefficient values must be" + "\nreal numbers.",
			"Number Format Error"),

	CUBIC_ILLEGAL_ARGUMENT("The 'a' coefficient "
			+ "of a cubic\nfunction cannot be zero.",
			"Illegal Coefficient Error"),

	M_NEG_ERROR("M values must be positive.", "Illegal M Value Error"),
	
	//Added here for the binomial function.  Checks if the exponent is zero.
	N_ZERO_ERROR("The exponent may not be zero.", "Number Format Error"),

	LINEAR_ILLEGAL_ARGUMENT("The 'a' coefficient "										
			+ "of a linear\nfunction cannot be zero.",
			"Illegal Coefficient Error"),

	EMPTY_FIELD("Please fill in all fields.", "Empty Field Error"),

	DIV_BY_ZERO("Division by zero "
			+ "has occurred.\nProcessing cannot continue.",
			"Division By Zero Error"), ZERO_DETERMINANT(
			"To produce an inverse image or julia set using an\naffine "
					+ "linear map, the determinant of the matrix\n"
					+ "cannot be zero (or very close to zero).",
			"Zero Determinant Error"),

	QUADRATIC_ILLEGAL_ARGUMENT("The 'a' coefficient of a quadratic\n"
			+ "function cannot be zero.", "Illegal Coefficient Error"),

	T_INTEGER_ERROR("T values must be positive " + "\ninteger values.",
			"Number Format Error"),

	OUT_OF_MEMORY("Julia is running out of memory.\n"
			+ "Delete any unneeded output sets and try again.",
			"Out Of Memory Error");

	private final String message;
	private final String title;

	/**
	 * constructor for JuliaError types
	 * 
	 * @param message
	 *            the String error message
	 * @param title
	 *            the String error title
	 */
	private JuliaError(String message, String title) {
		this.message = message;
		this.title = title;
	}

	/**
	 * show the error message dialog window
	 * 
	 * @param frame
	 *            the parent Frame in which to show the dialog window
	 */
	public void showDialog(Frame frame) {
		JOptionPane.showMessageDialog(frame, message, title,
				JOptionPane.ERROR_MESSAGE);
	}
}
