package edu.bsu.julia.gui;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JOptionPane;
import javax.swing.JTextField;

import org.apache.commons.math.complex.Complex;

import edu.bsu.julia.Julia;
import edu.bsu.julia.session.Session;

public class TextFieldFocusListener implements FocusListener {

	private JTextField f;
	private Julia parentFrame;

	public TextFieldFocusListener(JTextField field, Julia ju) {
		f = field;
		parentFrame = ju;
	}

	public void focusGained(FocusEvent e) {
		String text = f.getText();
		if (text.equals(""))
			return;
		f.setSelectionStart(0);
		f.setSelectionEnd(text.length());
	}

	public void focusLost(FocusEvent e) {
		Session s = parentFrame.getCurrentSession();
		f.setSelectionEnd(0);
		int oldIter = s.getIterations();
		int oldSkip = s.getSkips();
		Complex oldSeed = s.getSeedValue();
		JTextField textField = (JTextField) e.getComponent();
		if (textField.getName() == "iterationsField") {
			long iterLong = 0;
			int iter = 0;
			try {
				iterLong = Long.parseLong(textField.getText());
				if (iterLong > 10000000) {
					JOptionPane
							.showMessageDialog(parentFrame, "Points to Plot "
									+ "must be\nless than 10,000,000",
									"Number Format Error",
									JOptionPane.ERROR_MESSAGE);
					s.setIterations(oldIter);
					return;
				}
				iter = (int) iterLong;
			} catch (NumberFormatException a) {
				JOptionPane.showMessageDialog(parentFrame,
						"Points to Plot must be "
								+ "an integer\nnumber with no commas.",
						"Number Format Error", JOptionPane.ERROR_MESSAGE);
				s.setIterations(oldIter);
				return;
			}

			try {
				s.setIterations(iter);
			} catch (IllegalArgumentException b) {
				JOptionPane.showMessageDialog(parentFrame, b.getMessage(),
						"Number Format Error", JOptionPane.ERROR_MESSAGE);
				s.setIterations(oldIter);
				return;
			}

		} else if (textField.getName() == "skipsField") {
			int sk = 0;
			try {
				sk = Integer.parseInt(textField.getText());
			} catch (NumberFormatException a) {
				JOptionPane.showMessageDialog(parentFrame, "Skips must be"
						+ " an integer\nnumber with no commas.",
						"Number Format Error", JOptionPane.ERROR_MESSAGE);
				s.setSkips(oldSkip);
				return;
			}
			try {
				s.setSkips(sk);
			} catch (IllegalArgumentException b) {
				JOptionPane.showMessageDialog(parentFrame, b.getMessage(),
						"Number Format Error", JOptionPane.ERROR_MESSAGE);
				s.setSkips(oldSkip);
				return;
			}
		} else if (textField.getName() == "seedField1") {
			double seed1 = 0;
			String seed1String = GUIUtil.parsePI(textField.getText());
			try {
				seed1 = Double.parseDouble(seed1String);
			} catch (NumberFormatException a) {
				JOptionPane.showMessageDialog(parentFrame,
						"Seed values must be real"
								+ "\nnumbers with no commas.",
						"Number Format Error", JOptionPane.ERROR_MESSAGE);
				s.setSeedValue(oldSeed);
				return;
			}

			try {
				double seed2 = oldSeed.getImaginary();
				Complex seed = new Complex(seed1, seed2);
				s.setSeedValue(seed);
			} catch (IllegalArgumentException b) {
				JOptionPane.showMessageDialog(parentFrame, b.getMessage(),
						"Number Format Error", JOptionPane.ERROR_MESSAGE);
				s.setSeedValue(oldSeed);
				return;
			}
		} else if (textField.getName() == "seedField2") {
			double seed2 = 0;
			String seed2String = GUIUtil.parsePI(textField.getText());
			try {
				seed2 = Double.parseDouble(seed2String);
			} catch (NumberFormatException a) {
				JOptionPane.showMessageDialog(parentFrame,
						"Seed values must be real"
								+ "\nnumbers with no commas.",
						"Number Format Error", JOptionPane.ERROR_MESSAGE);
				s.setSeedValue(oldSeed);
				return;
			}

			try {
				double seed1 = oldSeed.getReal();
				Complex seed = new Complex(seed1, seed2);
				s.setSeedValue(seed);
			} catch (IllegalArgumentException b) {
				JOptionPane.showMessageDialog(parentFrame, b.getMessage(),
						"Number Format Error", JOptionPane.ERROR_MESSAGE);
				s.setSeedValue(oldSeed);
				return;
			}
		}
	}

}
