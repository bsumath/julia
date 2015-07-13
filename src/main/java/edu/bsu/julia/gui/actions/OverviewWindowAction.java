package edu.bsu.julia.gui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import edu.bsu.julia.Julia;
import edu.bsu.julia.gui.OverviewListener;

public class OverviewWindowAction extends AbstractAction {
	Julia parentFrame;
	// for serializable interface: do not use
	public static final long serialVersionUID = 0;

	public OverviewWindowAction(Julia f) {
		super("Open Overview Window", new ImageIcon(Thread.currentThread()
				.getContextClassLoader().getResource("overview.png")));
		putValue("SHORT_DESCRIPTION", "Open an Overview Window");
		putValue("LONG_DESCRIPTION", "Add a new window to show an overview "
				+ "of the current graph.");
		parentFrame = f;
	}

	public void actionPerformed(ActionEvent arg0) {
		new OverviewListener(parentFrame);
	}

}
