package edu.bsu.julia.gui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import edu.bsu.julia.Julia;
import edu.bsu.julia.gui.PostCriticalDialog;

public class PostCriticalAction extends AbstractAction {
	private static final long serialVersionUID = -2696427897141258816L;
	private Julia parentFrame;


	
	public PostCriticalAction(Julia f) {
		super("Create Post Critical Set");
		putValue("SHORT_DESCRIPTION", "Create Post Critical Set");
		putValue("LONG_DESCRIPTION", "Create a Post Critical Set " +
				"from the selected functions.");
		parentFrame = f;
	}

	public void actionPerformed(ActionEvent arg0) {
		parentFrame.getOutputFunctionList().clearSelection();
		new PostCriticalDialog(parentFrame);

	}

}
