package edu.bsu.julia.gui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;

import edu.bsu.julia.Julia;								/* Import the new Input Function here! */
import edu.bsu.julia.gui.CubicDialog;
import edu.bsu.julia.gui.GUIUtil;
import edu.bsu.julia.gui.LinearDialog;
import edu.bsu.julia.gui.MobiusDialog;
import edu.bsu.julia.gui.BinomialDialog;
import edu.bsu.julia.gui.QuadraticDialog;
import edu.bsu.julia.gui.RealAffineLinearDialog;
import edu.bsu.julia.gui.TempDialog;
import edu.bsu.julia.input.CubicInputFunction;
import edu.bsu.julia.input.InputFunction;
import edu.bsu.julia.input.LinearInputFunction;
import edu.bsu.julia.input.BinomialInputFunction;
import edu.bsu.julia.input.MobiusInputFunction;
import edu.bsu.julia.input.QuadraticInputFunction;
import edu.bsu.julia.input.RealAffineLinearInputFunction;
import edu.bsu.julia.input.TempInputFunction;

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

	public void actionPerformed(ActionEvent arg0) {						/* Add an additional 'else if' statement below! */
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
		} else if (fn instanceof RealAffineLinearInputFunction) {
			RealAffineLinearInputFunction mFn = (RealAffineLinearInputFunction) fn;
			new RealAffineLinearDialog(parentFrame, GUIUtil.EDIT_DIALOG, mFn);
		} else if (fn instanceof BinomialInputFunction) {
			BinomialInputFunction mFn = (BinomialInputFunction) fn;
			new BinomialDialog(parentFrame, GUIUtil.EDIT_DIALOG, mFn);
		} else if (fn instanceof TempInputFunction) {
			TempInputFunction mFn = (TempInputFunction) fn;
			new TempDialog(parentFrame, GUIUtil.EDIT_DIALOG, mFn);
		}
	}

}
