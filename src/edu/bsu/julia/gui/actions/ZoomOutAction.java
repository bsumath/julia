package edu.bsu.julia.gui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import edu.bsu.julia.Julia;
import edu.bsu.julia.gui.GraphTabbedPane;

public class ZoomOutAction extends AbstractAction {

	private Julia parentFrame;
	private GraphTabbedPane tabbedPane;
	// for serializable interface: do not use
	public static final long serialVersionUID = 0;

	public ZoomOutAction(Julia f) {
		super("Zoom Out", new ImageIcon(Thread.currentThread()
				.getContextClassLoader().getResource("viewmag-.png")));
		putValue("SHORT_DESCRIPTION", "Zoom Out On Graph");
		putValue("LONG_DESCRIPTION", "Zoom out on the visible section of the "
				+ "graph.");
		parentFrame = f;
		tabbedPane = parentFrame.getTabbedPane();
	}

	public void actionPerformed(ActionEvent arg0) {
		tabbedPane.getActivePane().getGLListener().zoomOut();
	}

}
