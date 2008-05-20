package edu.bsu.julia.gui;

import javax.swing.JOptionPane;
import edu.bsu.julia.*;

public class JuliaError {
	
	public static final int M_INTEGER_ERROR = 0;
	public static final int COEFFICIENT_FORMAT_ERROR = 1;
	public static final int CUBIC_ILLEGAL_ARGUMENT = 2;
	public static final int M_NEG_ERROR = 3;
	public static final int LINEAR_ILLEGAL_ARGUMENT = 4;
	public static final int EMPTY_FIELD = 5;
	public static final int DIV_BY_ZERO = 6;
	public static final int ZERO_DETERMINANT = 7;
	public static final int QUADRATIC_ILLEGAL_ARGUMENT = 8;
	public static final int T_INTEGER_ERROR = 9;
	private Julia parentFrame;
	
	public JuliaError(int errorType, Julia f) {
		parentFrame = f;
		
		switch(errorType) {
		case 0: 
			JOptionPane.showMessageDialog(parentFrame, 
					"M values must be positive " + 
					"\ninteger values.", "Number Format Error",
					JOptionPane.ERROR_MESSAGE);
			break;
		case 1:
			JOptionPane.showMessageDialog(parentFrame, 
					"Coefficient values must be" +
					"\nreal numbers.", "Number Format Error",
					JOptionPane.ERROR_MESSAGE);
			break;
		case 2:
			JOptionPane.showMessageDialog(parentFrame, "The 'a' coefficient " 
					+ "of a cubic\nfunction cannot be zero.", 
					"Illegal Coefficient Error", JOptionPane.ERROR_MESSAGE);
			break;
		case 3:
			JOptionPane.showMessageDialog(parentFrame, 
					"M values must be positive.", 
					"Illegal M Value Error", JOptionPane.ERROR_MESSAGE);
			break;
		case 4:
			JOptionPane.showMessageDialog(parentFrame, "The 'a' coefficient " 
					+ "of a linear\nfunction cannot be zero.", 
					"Illegal Coefficient Error", JOptionPane.ERROR_MESSAGE);
			break;
		case 5:
			JOptionPane.showMessageDialog(parentFrame, 
					"Please fill in all fields.", "Empty Field Error",
					JOptionPane.ERROR_MESSAGE);
			break;
		case 6:
			JOptionPane.showMessageDialog(parentFrame, "Division by zero " +
					"has occurred.\nProcessing cannot continue.", 
					"Division By Zero Error", JOptionPane.ERROR_MESSAGE);
			break;
		case 7:
			JOptionPane.showMessageDialog(parentFrame, 
					"To produce an inverse image or julia set using an\naffline " +
					"linear map, the determinant of the matrix\n" +
					"cannot be zero (or very close to zero).", 
					"Zero Determinant Error",
					JOptionPane.ERROR_MESSAGE);
			break;
		case 8:
			JOptionPane.showMessageDialog(parentFrame,
					"The 'a' coefficient of a quadratic\n" +
					"function cannot be zero.", "Illegal Coefficient Error",
					JOptionPane.ERROR_MESSAGE);
			break;
		case 9:
			JOptionPane.showMessageDialog(parentFrame, 
					"T values must be positive " + 
					"\ninteger values.", "Number Format Error",
					JOptionPane.ERROR_MESSAGE);
			break;
		default:
			JOptionPane.showMessageDialog(parentFrame, 
					"An unknown error has occured.", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

}
