package edu.bsu.julia.gui;

import java.awt.event.*;
import javax.swing.*;
import edu.bsu.julia.*;

public class OptionsAction extends AbstractAction {
	
	private Julia parentFrame;
	//for serializable interface: do not use
	public static final long serialVersionUID = 0;
	
	public OptionsAction(Julia f) {
		super("Options", new ImageIcon
				(Thread.currentThread().getContextClassLoader().getResource
						("Resources/configure.png")));
		putValue("SHORT_DESCRIPTION", "Options");
		putValue("LONG_DESCRIPTION", "Change the dot size" +
				"and Axis Trigger");
		parentFrame = f;
	}

	public void actionPerformed(ActionEvent arg0) {
		new OptionsDialog(parentFrame);
	}

}
