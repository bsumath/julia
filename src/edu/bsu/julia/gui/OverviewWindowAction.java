package edu.bsu.julia.gui;

import java.awt.event.*;
import edu.bsu.julia.*;
import javax.swing.*;

public class OverviewWindowAction extends AbstractAction {
	Julia parentFrame;
	//for serializable interface: do not use
	public static final long serialVersionUID = 0;
	
	public OverviewWindowAction(Julia f) {
		super("Open Overview Window", new ImageIcon
				(Thread.currentThread().getContextClassLoader().getResource
						("Resources/overview.png")));
		putValue("SHORT_DESCRIPTION", "Open an Overview Window");
		putValue("LONG_DESCRIPTION", "Add a new window to show an overview " +
				"of the current graph.");
		parentFrame = f;
	}

	public void actionPerformed(ActionEvent arg0) {
		new OverviewListener(parentFrame);
	}

}
