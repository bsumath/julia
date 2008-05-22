package edu.bsu.julia.gui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import edu.bsu.julia.Julia;
import edu.bsu.julia.gui.OptionsDialog;

public class OptionsAction extends AbstractAction {
	
	private Julia parentFrame;
	//for serializable interface: do not use
	public static final long serialVersionUID = 0;
	
	public OptionsAction(Julia f) {
		super("Options", new ImageIcon
				(Thread.currentThread().getContextClassLoader().getResource
						("configure.png")));
		putValue("SHORT_DESCRIPTION", "Options");
		putValue("LONG_DESCRIPTION", "Change the dot size" +
				"and Axis Trigger");
		parentFrame = f;
	}

	public void actionPerformed(ActionEvent arg0) {
		new OptionsDialog(parentFrame);
	}

}
