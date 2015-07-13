package edu.bsu.julia.gui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import edu.bsu.julia.Julia;
import edu.bsu.julia.session.Session;

public class DeleteInputAction extends AbstractAction {

	private Julia parentFrame;
	private int index;
	// for serializable interface: do not use
	public static final long serialVersionUID = 0;

	public DeleteInputAction(Julia f, DefaultListModel listModel, int i) {
		super("Delete", new ImageIcon(Thread.currentThread()
				.getContextClassLoader().getResource("delete.png")));
		putValue("SHORT_DESCRIPTION", "Delete");
		putValue("LONG_DESCRIPTION", "Delete the chosen function.");
		parentFrame = f;
		index = i;
	}

	public void actionPerformed(ActionEvent arg0) {
		Session s = parentFrame.getCurrentSession();
		int choice;

		choice = JOptionPane.showConfirmDialog(parentFrame, "Are you sure?",
				"Delete?", JOptionPane.OK_CANCEL_OPTION);
		if (choice == JOptionPane.OK_OPTION)
			s.deleteInputFunction(index);
	}
}
