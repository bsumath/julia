package edu.bsu.julia.gui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;

import edu.bsu.julia.Julia;
import edu.bsu.julia.gui.CubicDialog;
import edu.bsu.julia.gui.GUIUtil;
import edu.bsu.julia.gui.LinearDialog;
import edu.bsu.julia.gui.MobiusDialog;
import edu.bsu.julia.gui.QuadraticDialog;
import edu.bsu.julia.gui.RealAfflineLinearDialog;
import edu.bsu.julia.input.CubicInputFunction;
import edu.bsu.julia.input.InputFunction;
import edu.bsu.julia.input.LinearInputFunction;
import edu.bsu.julia.input.MobiusInputFunction;
import edu.bsu.julia.input.QuadraticInputFunction;
import edu.bsu.julia.input.RealAfflineLinearInputFunction;

public class EditFunctionAction extends AbstractAction {

	private Julia parentFrame;
	private DefaultListModel inputList;
	private int index;
	// for serializable interface: do not use
	public static final long serialVersionUID = 0;

	public EditFunctionAction(Julia f, DefaultListModel inList, int i) {
		super("Edit Function", new ImageIcon(Thread.currentThread()
				.getContextClassLoader().getResource("edit.png")));
		parentFrame = f;
		inputList = inList;
		index = i;
		putValue("SHORT_DESCRIPTION", "Edit Function");
		putValue("LONG_DESCRIPTION", "Edit the chosen function.");
	}

	public void actionPerformed(ActionEvent arg0) {
		InputFunction fn;
		if (index < inputList.size())
			fn = (InputFunction) inputList.get(index);
		else
			return;

		if (fn instanceof LinearInputFunction) {
			LinearInputFunction lFn = (LinearInputFunction) fn;
			new LinearDialog(parentFrame, GUIUtil.EDIT_DIALOG, lFn);
		} else if (fn instanceof MobiusInputFunction) {
			MobiusInputFunction mFn = (MobiusInputFunction) fn;
			new MobiusDialog(parentFrame, GUIUtil.EDIT_DIALOG, mFn);
		} else if (fn instanceof QuadraticInputFunction) {
			QuadraticInputFunction qFn = (QuadraticInputFunction) fn;
			new QuadraticDialog(parentFrame, GUIUtil.EDIT_DIALOG, qFn);
		} else if (fn instanceof CubicInputFunction) {
			CubicInputFunction cFn = (CubicInputFunction) fn;
			new CubicDialog(parentFrame, GUIUtil.EDIT_DIALOG, cFn);
		} else if (fn instanceof RealAfflineLinearInputFunction) {
			RealAfflineLinearInputFunction mFn = (RealAfflineLinearInputFunction) fn;
			new RealAfflineLinearDialog(parentFrame, GUIUtil.EDIT_DIALOG, mFn);
		}
	}

}
