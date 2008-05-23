package edu.bsu.julia.gui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import edu.bsu.julia.Julia;
import edu.bsu.julia.gui.SaveSessionDialog;

public class ExitAction extends AbstractAction {

	private Julia parentFrame;
	// for serializable interface: do not use
	public static final long serialVersionUID = 0;

	public ExitAction(Julia f) {
		super("Exit", new ImageIcon(Thread.currentThread()
				.getContextClassLoader().getResource("exit.png")));
		putValue("SHORT_DESCRIPTION", "Exit Program");
		putValue("LONG_DESCRIPTION", "Exit the program.  Unsaved "
				+ "data will be lost.");
		parentFrame = f;
	}

	public void actionPerformed(ActionEvent arg0) {
		parentFrame.loseFocus();

		// show the save session dialog if necessary
		if (parentFrame.getCurrentSession().isModified()) {
			SaveSessionDialog saveDialog = new SaveSessionDialog(parentFrame);
			if (saveDialog.showSaveDialog() == SaveSessionDialog.CANCELED)
				return;
		}

		System.exit(0);
	}

}
