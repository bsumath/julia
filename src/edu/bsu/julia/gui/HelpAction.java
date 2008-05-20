package edu.bsu.julia.gui;

import java.awt.event.*;
import javax.swing.*;
import edu.bsu.julia.*;

public class HelpAction extends AbstractAction {
	
	private Julia parentFrame;
	//for serializable interface: do not use
	public static final long serialVersionUID = 0;
	
	public HelpAction(Julia f) {
		super("Help", new ImageIcon
				(Thread.currentThread().getContextClassLoader().getResource
						("help.png")));
		putValue("SHORT_DESCRIPTION", "Open Help");
		putValue("LONG_DESCRIPTION",
				"Open the help file in the default web browser.");
		parentFrame = f;
	}

	public void actionPerformed(ActionEvent arg0) {
		String output = "Help files are not currently available" +
		"\nwithin the program, but they may be accessed online at:" +
		"\nhttp://www.cs.bsu.edu/homepages/rstankew/julia/Julia.html";
	
	JOptionPane.showMessageDialog
		(parentFrame, output, "Help", JOptionPane.INFORMATION_MESSAGE);
	}

}
