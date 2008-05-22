package edu.bsu.julia.gui;

import java.awt.event.*;
import javax.swing.*;
import edu.bsu.julia.*;

public class AddTabAction extends AbstractAction {
	
	private Julia parentFrame;
	//for serializable interface: do not use
	public static final long serialVersionUID = 0;
	
	public AddTabAction(Julia f) {
		super("Add Graph", new ImageIcon
				(Thread.currentThread().getContextClassLoader().getResource
						("addTab.png")));
		putValue("SHORT_DESCRIPTION", "Add a new graph tab");
		putValue("LONG_DESCRIPTION", "Add a new tab to the central graph " +
				"panel.");
		parentFrame = f;
	}

	public void actionPerformed(ActionEvent arg0) {
		GraphScrollPane scrollPane = new GraphScrollPane(parentFrame);
		GraphTabbedPane.tabNumber++;
		parentFrame.getTabbedPane().addTab
			("Output Graph "+GraphTabbedPane.tabNumber, scrollPane);
	}

}
