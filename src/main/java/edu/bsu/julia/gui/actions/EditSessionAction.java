package edu.bsu.julia.gui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import edu.bsu.julia.Julia;
import edu.bsu.julia.gui.GUIUtil;
import edu.bsu.julia.gui.SessionDialog;

public class EditSessionAction extends AbstractAction {

	private Julia parentFrame;
	// for serializable interface: do not use
	public static final long serialVersionUID = 0;

	public EditSessionAction(Julia f) {
		super("Edit Session", new ImageIcon(Thread.currentThread()
				.getContextClassLoader().getResource("sessionedit.png")));
		putValue("SHORT_DESCRIPTION", "Edit Session");
		putValue("LONG_DESCRIPTION", "Edit the current session variables.");
		parentFrame = f;
	}

	public void actionPerformed(ActionEvent arg0) {
		new SessionDialog(parentFrame, GUIUtil.EDIT_DIALOG);
	}

}
