package edu.bsu.julia.gui;

import javax.swing.JOptionPane;

import edu.bsu.julia.Julia;
import edu.bsu.julia.gui.actions.SaveSessionAction;

public class SaveSessionDialog {
	public static final int SESSION_SAVED = 0;
	public static final int SESSION_DISCARDED = 1;
	public static final int CANCELED = 2;

	private Julia parentFrame;

	public SaveSessionDialog(Julia f) {
		parentFrame = f;
	}

	public int showSaveDialog() {
		Object[] options = { "Save", "Discard", "Cancel" };
		int choice = JOptionPane.showOptionDialog(parentFrame,
				"The current session has been modified.\n"
						+ "Do you want to save the session?",
				"Session Modified", JOptionPane.YES_NO_CANCEL_OPTION,
				JOptionPane.QUESTION_MESSAGE, null, options, options[2]);

		switch (choice) {
		case 0: // save
			new SaveSessionAction(parentFrame, false).actionPerformed(null);

			// check to see if the session was actually saved
			if (parentFrame.getCurrentSession().isModified())
				return CANCELED;
			else
				return SESSION_SAVED;
		case 1: // discard
			return SESSION_DISCARDED;
		default: // cancel
			return CANCELED;
		}
	}
}
