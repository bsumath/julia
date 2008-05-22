package edu.bsu.julia.gui;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import edu.bsu.julia.Julia;

public class NewAction extends AbstractAction {

	private Julia parentFrame;
	// for serializable interface: do not use
	public static final long serialVersionUID = 0;

	public NewAction(Julia f) {
		super("New Session", new ImageIcon(Thread.currentThread()
				.getContextClassLoader().getResource("sessionnew.png")));
		putValue("SHORT_DESCRIPTION", "New Session");
		putValue("LONG_DESCRIPTION", "Creates a new session with "
				+ "an empty function list.");
		parentFrame = f;
	}

	public void actionPerformed(ActionEvent ev) {
		parentFrame.loseFocus();

		// show the save session dialog if necessary
		if (parentFrame.getCurrentSession().isModified()) {
			SaveSessionDialog saveDialog = new SaveSessionDialog(parentFrame);
			if (saveDialog.showSaveDialog() == SaveSessionDialog.CANCELED)
				return;
		}

		// bring up the new session dialog
		new SessionDialog(parentFrame, GUIUtil.NEW_DIALOG);
	}

}
