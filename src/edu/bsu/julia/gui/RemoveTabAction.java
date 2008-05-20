package edu.bsu.julia.gui;

import java.awt.event.*;
import javax.swing.*;
import edu.bsu.julia.*;
import javax.swing.event.*;

public class RemoveTabAction extends AbstractAction {
	
	private Julia parentFrame;
	//for serializable interface: do not use
	public static final long serialVersionUID = 0;
	
	public RemoveTabAction(Julia f) {
		super("Remove Active Graph", new ImageIcon
				(Thread.currentThread().getContextClassLoader().getResource
						("Resources/removeTab.png")));
		putValue("SHORT_DESCRIPTION", "Remove the active tab");
		putValue("LONG_DESCRIPTION", "Remove the active tab from the " +
				"center graph panel.");
		parentFrame = f;
	}

	public void actionPerformed(ActionEvent event) {
		GraphTabbedPane pane = parentFrame.getTabbedPane();
		if(pane.getTabCount()>1) {
			pane.removeTabAt(pane.getSelectedIndex());
			pane.stateChanged(new ChangeEvent(pane));
		}
	}

}
