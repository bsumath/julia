package edu.bsu.julia.gui;

import java.awt.event.ActionEvent;
import javax.swing.*;
import javax.swing.ImageIcon;
import edu.bsu.julia.ComplexCalculator;

public class ComplexCalculatorAction extends AbstractAction {
	
	//for serializable interface: do not use
	public static final long serialVersionUID = 0;
	
	public ComplexCalculatorAction () {
		super("Complex Calculator", new ImageIcon
				(Thread.currentThread().getContextClassLoader().getResource
				("Resources/calc.png")));
		putValue("SHORT_DESCRIPTION", "Open Complex Number Calculator");
		putValue("LONG_DESCRIPTION", "Open a calculator for complex number mathematics.");
	}
	
	public void actionPerformed(ActionEvent event) {
		new ComplexCalculator();
	}
}
