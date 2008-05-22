package edu.bsu.julia.gui.actions;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import edu.bsu.julia.Julia;

public class ExitAction extends AbstractAction {
	
	private Julia parentFrame;
	//for serializable interface: do not use
	public static final long serialVersionUID = 0;
	
	public ExitAction(Julia f) {
		super("Exit", new ImageIcon
				(Thread.currentThread().getContextClassLoader().getResource
						("exit.png")));
		putValue("SHORT_DESCRIPTION", "Exit Program");
		putValue("LONG_DESCRIPTION", "Exit the program.  Unsaved " +
				"data will be lost.");
		parentFrame = f;
	}

	public void actionPerformed(ActionEvent arg0) {
		int choice;
		choice = JOptionPane.showConfirmDialog(parentFrame,
				"Are you sure?\nUnsaved data will be lost.",
				"Exit?",JOptionPane.OK_CANCEL_OPTION);
		if (choice == JOptionPane.OK_OPTION) System.exit(0);
	}

}
