package edu.bsu.julia.gui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import edu.bsu.julia.Julia;
import edu.bsu.julia.gui.GraphTabbedPane;

public class ResetZoomAction extends AbstractAction {
	
	private Julia parentFrame;
	private GraphTabbedPane tabbedPane;
	//for serializable interface: do not use
	public static final long serialVersionUID = 0;
	
	public ResetZoomAction(Julia f) {
		super("Reset Zoom", new ImageIcon
				(Thread.currentThread().getContextClassLoader().getResource
						("viewmag.png")));
		putValue("SHORT_DESCRIPTION", "Reset Zoom On Graph");
		putValue("LONG_DESCRIPTION", "Reset the graph to its original size.");
		parentFrame = f;
		tabbedPane = parentFrame.getTabbedPane();
	}

	public void actionPerformed(ActionEvent arg0) {
		tabbedPane.getActivePane().getGLListener().resetZoom();
	}

}
