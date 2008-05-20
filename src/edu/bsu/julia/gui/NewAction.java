package edu.bsu.julia.gui;

import java.awt.event.*;
import javax.swing.*;
import edu.bsu.julia.*;

public class NewAction extends AbstractAction {
	
	private Julia parentFrame;
	//for serializable interface: do not use
	public static final long serialVersionUID = 0;
	
	public NewAction(Julia f) {
		super("New Session", new ImageIcon
				(Thread.currentThread().getContextClassLoader().getResource
						("sessionnew.png")));
		putValue("SHORT_DESCRIPTION", "New Session");
		putValue("LONG_DESCRIPTION", "Creates a new session with " +
				"an empty function list.");
		parentFrame = f;
	}

	public void actionPerformed(ActionEvent arg0) {
		new SessionDialog(parentFrame, GUIUtil.NEW_DIALOG);
	}

}
