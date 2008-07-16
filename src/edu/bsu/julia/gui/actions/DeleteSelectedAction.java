package edu.bsu.julia.gui.actions;

import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import edu.bsu.julia.Julia;
import edu.bsu.julia.input.InputFunction;
import edu.bsu.julia.output.OutputSet;
import edu.bsu.julia.session.Session;

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
				Object[] outObjs = parentFrame.getOutputSetList().getSelectedValues();
				OutputSet[] outSets = new OutputSet[outObjs.length];
				for(int j = 0; j<outObjs.length; j++) {
					outSets[j] = (OutputSet)outObjs[j];
				}
				for(OutputSet set : outSets){
					s.deleteOutputSet(set);
				}
			}
			else {
				InputFunction[] inFns = parentFrame.getInputPanel().getSelectedFunctions();
				for(int i = 0; i<inFns.length; i++) {
					List<InputFunction> inList = s.getInputFunctions();
					int index = inList.indexOf(inFns[i]);
					s.deleteInputFunction(index);
				}
			}
		}
	}

}
