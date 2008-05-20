package edu.bsu.julia.gui;

import java.awt.event.*;
import javax.swing.*;
import edu.bsu.julia.*;

public class DeleteAction extends AbstractAction{

	private Julia parentFrame;
	private int index;
	private int type;
	//for serializable interface: do not use
	public static final long serialVersionUID = 0;
	
	public DeleteAction(Julia f, DefaultListModel listModel, int i, int t) {
		super("Delete", new ImageIcon
				(Thread.currentThread().getContextClassLoader().getResource
						("resources/delete.png")));
		putValue("SHORT_DESCRIPTION", "Delete");
		putValue("LONG_DESCRIPTION", "Delete the chosen function.");
		parentFrame = f;
		index = i;
		type = t;
	}
	
	public void actionPerformed(ActionEvent arg0) {
		Session s = parentFrame.getCurrentSession();
		int choice;
		
		choice = JOptionPane.showConfirmDialog(parentFrame,
				"Are you sure?",
				"Delete?",JOptionPane.OK_CANCEL_OPTION);
		if (choice == JOptionPane.OK_OPTION){
			if (type == Julia.OUTPUTTYPE) s.deleteOutputFunction(index);
			else s.deleteInputFunction(index);
		}
		
	}
}
