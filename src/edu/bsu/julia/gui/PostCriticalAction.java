package edu.bsu.julia.gui;

import java.awt.event.*;
import javax.swing.*;

import java.awt.*;
import java.util.*;
import edu.bsu.julia.*;

import java.beans.*;

public class PostCriticalAction extends AbstractAction {
	
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
