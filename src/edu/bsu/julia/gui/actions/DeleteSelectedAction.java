package edu.bsu.julia.gui.actions;

import java.awt.event.*;
import javax.swing.*;
import edu.bsu.julia.*;
import java.util.*;

public class DeleteSelectedAction extends AbstractAction {
	
	private Julia parentFrame;
	private int type;
	//for serializable interface: do not use
	public static final long serialVersionUID = 0;
	
	public DeleteSelectedAction(Julia f, int t) {
		super("Delete Selected Items");
		putValue("SHORT_DESCRIPTION", "Delete Selected Items");
		putValue("LONG_DESCRIPTION", "Delete all the selected functions or sets.");
		parentFrame = f;
		type = t;
	}

	public void actionPerformed(ActionEvent arg0) {
		Session s = parentFrame.getCurrentSession();
		int choice;
		
		choice = JOptionPane.showConfirmDialog(parentFrame,
				"Are you sure?",
				"Delete All Selected?",JOptionPane.OK_CANCEL_OPTION);
		if (choice == JOptionPane.OK_OPTION){
			if (type == Julia.OUTPUTTYPE) {
				Object[] outObjs = parentFrame.getOutputFunctionList().getSelectedValues();
				OutputFunction[] outFns = new OutputFunction[outObjs.length];
				for(int j = 0; j<outObjs.length; j++) {
					outFns[j] = (OutputFunction)outObjs[j];
				}
				for(int i = 0; i<outFns.length; i++){
					Vector<OutputFunction> outList = s.getOutputFunctions();
					int index = outList.indexOf(outFns[i]);
					s.deleteOutputFunction(index);
				}
			}
			else {
				InputFunction[] inFns = parentFrame.getInputPanel().getSelectedFunctions();
				for(int i = 0; i<inFns.length; i++) {
					Vector<InputFunction> inList = s.getInputFunctions();
					int index = inList.indexOf(inFns[i]);
					s.deleteInputFunction(index);
				}
			}
		}
	}

}
