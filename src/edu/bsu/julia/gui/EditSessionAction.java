package edu.bsu.julia.gui;

import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
import edu.bsu.julia.*;


public class EditSessionAction extends AbstractAction {
	
	private Julia parentFrame;
	//for serializable interface: do not use
	public static final long serialVersionUID = 0;
	
	public EditSessionAction(Julia f) {
		super("Edit Session", new ImageIcon
				(Thread.currentThread().getContextClassLoader().getResource
						("sessionedit.png")));
		putValue("SHORT_DESCRIPTION", "Edit Session");
		putValue("LONG_DESCRIPTION", "Edit the current session variables.");
		parentFrame = f;
	}

	public void actionPerformed(ActionEvent arg0) {
		new SessionDialog(parentFrame, GUIUtil.EDIT_DIALOG);
	}

}
