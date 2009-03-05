package edu.bsu.julia;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Event;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Panel;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import org.apache.commons.math.complex.Complex;

public class ComplexCalculator extends JFrame {
	// member
	JTextField firstfield, secondfield;
	JLabel labelI, labelS, labelX, labelY, labelZ, labelA;
	String first, second, third, forth;
	Complex seed1, seed2, seedMem;
	String Operator;
	boolean ClearAll;
	int tab = 0;
	// for serializable interface: do not use
	public static final long serialVersionUID = 0;

	// constructor
	public ComplexCalculator() {
		// construct the frame

		Panel CalculatorFrame = new Panel();
		CalculatorFrame.setLayout(new FlowLayout());
		Panel commentPanel1 = new Panel();
		labelS = new JLabel("Press the equal button after each operation");
		commentPanel1.add(labelS);
		CalculatorFrame.add(commentPanel1);
		Panel commentPanel2 = new Panel();
		labelX = new JLabel(
				"When using the calculator number buttons, the TAB button ");
		commentPanel2.add(labelX);
		CalculatorFrame.add(commentPanel2);
		Panel commentPanel3 = new Panel();
		labelY = new JLabel(
				"         must be used to toggle between the input boxes.            ");
		commentPanel3.add(labelY);
		CalculatorFrame.add(commentPanel3);
		// show every thing on the frame

		// construct the textfields
		firstfield = new JTextField(16);
		firstfield.setForeground(Color.black);
		firstfield.setEditable(true);
		secondfield = new JTextField(16);
		secondfield.setForeground(Color.black);
		secondfield.setEditable(true);
		labelI = new JLabel("+i");
		// the first textfield
		Panel firstfieldpanel = new Panel();
		firstfieldpanel.add(firstfield);
		firstfieldpanel.add(labelI);
		firstfieldpanel.add(secondfield);
		CalculatorFrame.add(firstfieldpanel);
		// construct the keys
		Panel keybutton = new Panel();
		keybutton.setLayout(new GridLayout(6, 5, 5, 5));
		add("Center", keybutton);
		keybutton.add(new Button("C"));
		keybutton.add(new Button("MR"));
		keybutton.add(new Button("M-"));
		keybutton.add(new Button("M+"));
		keybutton.add(new Button("Sqrt"));
		keybutton.add(new Button("7"));
		keybutton.add(new Button("8"));
		keybutton.add(new Button("9"));
		keybutton.add(new Button("/"));
		keybutton.add(new Button("Curt"));
		keybutton.add(new Button("4"));
		keybutton.add(new Button("5"));
		keybutton.add(new Button("6"));
		keybutton.add(new Button("*"));
		keybutton.add(new Button("z^2"));
		keybutton.add(new Button("1"));
		keybutton.add(new Button("2"));
		keybutton.add(new Button("3"));
		keybutton.add(new Button("-"));
		keybutton.add(new Button("z^3"));
		keybutton.add(new Button("0"));
		keybutton.add(new Button("."));
		keybutton.add(new Button("="));
		keybutton.add(new Button("+"));
		keybutton.add(new Button("TAB"));
		CalculatorFrame.add(keybutton);
		Panel commentPanel4 = new Panel();
		labelZ = new JLabel("Sqrt will calculate the Principal Square Root.");
		commentPanel4.add(labelZ);
		CalculatorFrame.add(commentPanel4);
		Panel commentPanel5 = new Panel();
		labelA = new JLabel("Curt will calculate the Principal Cube Root.");
		commentPanel5.add(labelA);
		CalculatorFrame.add(commentPanel5);

		setLayout(new BorderLayout(0, 10));
		add("Center", CalculatorFrame);
		setBackground(Color.darkGray);
		// initialize the textfield
		seed1 = new Complex(0.0d, 0.0d);
		seed2 = new Complex(0.0d, 0.0d);
		seedMem = new Complex(0.0d, 0.0d);
		Operator = "";
		firstfield.setText("" + seed1.getReal());
		secondfield.setText("" + seed1.getImaginary());
		ClearAll = true;
		setSize(400, 420);
		setVisible(true);
	}

	// event handler
	public boolean action(Event evt, Object arg) {
		try {
			// clear all
			if ("C".equals(arg)) {
				seed1 = new Complex(0.0d, 0.0d);
				seed2 = new Complex(0.0d, 0.0d);
				seedMem = new Complex(0.0d, 0.0d);
				Operator = "";
				firstfield.setText("" + seed1.getReal());
				secondfield.setText("" + seed1.getImaginary());
				ClearAll = true;
				tab = 0;
			}
			// tab to swith editing textfield
			else if ("TAB".equals(arg)) {
				tab++;
				if (tab >= 1) {
					ClearAll = true;
				}
			}
			// numeric input
			else if (("0".equals(arg)) | ("1".equals(arg)) | ("2".equals(arg))
					| ("3".equals(arg)) | ("4".equals(arg)) | ("5".equals(arg))
					| ("6".equals(arg)) | ("7".equals(arg)) | ("8".equals(arg))
					| ("9".equals(arg)) | (".".equals(arg))) {
				if (tab % 2 == 0) {
					if (ClearAll) {
						third = (String) arg;
					} else {
						third = firstfield.getText() + arg;
					}
					firstfield.setText(third);
					ClearAll = false;
				}
				if (tab % 2 != 0) {
					if (ClearAll) {
						forth = (String) arg;
					} else {
						forth = secondfield.getText() + arg;
					}
					secondfield.setText(forth);
					ClearAll = false;
				}
			}
			// operations
			else if (("+".equals(arg)) | ("-".equals(arg)) | ("*".equals(arg))
					| ("/".equals(arg)) | ("=".equals(arg))
					| ("Sqrt".equals(arg)) | ("Curt".equals(arg))
					| ("z^2".equals(arg)) | ("z^3".equals(arg))) {
				first = firstfield.getText();
				second = secondfield.getText();
				seed2 = new Complex(
						(Double.valueOf(first)).doubleValue(), (Double
								.valueOf(second)).doubleValue());
				seed1 = Calculation(Operator, seed1, seed2);
				Complex dTemp = seed1;
				third = "" + dTemp.getReal();
				forth = "" + dTemp.getImaginary();
				firstfield.setText(third);
				secondfield.setText(forth);
				Operator = (String) arg;
				ClearAll = true;
			}
			// memory read operation
			else if ("MR".equals(arg)) {
				Complex dTemp = seedMem;
				third = "" + dTemp.getReal();
				forth = "" + dTemp.getImaginary();
				firstfield.setText(third);
				secondfield.setText(forth);
				Operator = "";
				ClearAll = true;
			}
			// memory add operation
			else if ("M+".equals(arg)) {
				first = firstfield.getText();
				second = secondfield.getText();
				seed2 = new Complex(
						(Double.valueOf(first)).doubleValue(), (Double
								.valueOf(second)).doubleValue());
				seed1 = Calculation(Operator, seed1, seed2);
				Complex dTemp = seed1;
				third = "" + dTemp.getReal();
				forth = "" + dTemp.getImaginary();
				firstfield.setText(third);
				secondfield.setText(forth);
				seedMem = seedMem.add(seed1);
				Operator = "";
				ClearAll = true;
			}
			// memory sub operation
			else if ("M-".equals(arg)) {
				first = firstfield.getText();
				second = secondfield.getText();
				seed2 = new Complex(
						(Double.valueOf(first)).doubleValue(), (Double
								.valueOf(second)).doubleValue());
				seed1 = Calculation(Operator, seed1, seed2);
				Complex dTemp = seed1;
				third = "" + dTemp.getReal();
				forth = "" + dTemp.getImaginary();
				firstfield.setText(third);
				secondfield.setText(forth);
				seedMem = seedMem.subtract(seed1);
				Operator = "";
				ClearAll = true;
			}
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(ComplexCalculator.this,
					"The input must be" + " number with no commas.",
					"Number Format Error", JOptionPane.ERROR_MESSAGE);
		}
		return true;
	}

	// Calculation
	private Complex Calculation(String sOperator, Complex dReg1,
			Complex dReg2) {
		if ("+".equals(sOperator))
			return dReg1.add(dReg2);
		else if ("-".equals(sOperator))
			return dReg1.subtract(dReg2);
		else if ("*".equals(sOperator))
			return dReg1.multiply(dReg2);
		else if ("/".equals(sOperator))
			return dReg1.divide(dReg2);
		else if ("Sqrt".equals(sOperator)) {
			return dReg1.sqrt();			
		} else if ("Curt".equals(sOperator)) {
			return dReg1.nthRoot(3).get(0);
		} else if ("z^2".equals(sOperator))
			return dReg1.multiply(dReg1);
		else if ("z^3".equals(sOperator))
			return dReg1.pow(new Complex(3,0));
		else
			return dReg2;
	}
}
