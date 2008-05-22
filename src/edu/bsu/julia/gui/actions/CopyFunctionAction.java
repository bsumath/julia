package edu.bsu.julia.gui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;

import edu.bsu.julia.CubicInputFunction;
import edu.bsu.julia.InputFunction;
import edu.bsu.julia.Julia;
import edu.bsu.julia.LinearInputFunction;
import edu.bsu.julia.MobiusInputFunction;
import edu.bsu.julia.QuadraticInputFunction;
import edu.bsu.julia.RealAfflineLinearInputFunction;
import edu.bsu.julia.gui.CubicDialog;
import edu.bsu.julia.gui.GUIUtil;
import edu.bsu.julia.gui.LinearDialog;
import edu.bsu.julia.gui.MobiusDialog;
import edu.bsu.julia.gui.QuadraticDialog;
import edu.bsu.julia.gui.RealAfflineLinearDialog;

public class CopyFunctionAction extends AbstractAction {
	
	private Julia parentFrame;
	private DefaultListModel inputList;
	private int index;
	//for serializable interface: do not use
	public static final long serialVersionUID = 0;
	
	public CopyFunctionAction(Julia f, DefaultListModel inList, int i) {
		super("Clone Function", new ImageIcon
				(Thread.currentThread().getContextClassLoader().getResource
						("copyFn.png")));
		parentFrame = f;
		inputList = inList;
		index = i;
		putValue("SHORT_DESCRIPTION", "Copy Function");
		putValue("LONG_DESCRIPTION", "Copy the chosen function.");
	}

	public void actionPerformed(ActionEvent arg0) {
		InputFunction fn = (InputFunction)inputList.get(index);
		try {
			if(fn.getClass() == Class.forName
					("edu.bsu.julia.LinearInputFunction")) {
				LinearInputFunction lFn = (LinearInputFunction)fn;
				new LinearDialog(parentFrame, GUIUtil.CLONE_DIALOG, lFn);
			}
			else if(fn.getClass() == Class.forName
					("edu.bsu.julia.MobiusInputFunction")) {
				MobiusInputFunction mFn = (MobiusInputFunction)fn;
				new MobiusDialog(parentFrame, GUIUtil.CLONE_DIALOG, mFn);
			}
			else if(fn.getClass() == Class.forName
					("edu.bsu.julia.QuadraticInputFunction")) {
				QuadraticInputFunction qFn= (QuadraticInputFunction)fn;
				new QuadraticDialog(parentFrame, GUIUtil.CLONE_DIALOG, qFn);
			}
			else if(fn.getClass() == Class.forName
					("edu.bsu.julia.CubicInputFunction")) {
				CubicInputFunction cFn = (CubicInputFunction)fn;
				new CubicDialog(parentFrame, GUIUtil.CLONE_DIALOG, cFn);
			}
			else if(fn.getClass() == Class.forName
					("edu.bsu.julia.RealAfflineLinearInputFunction")) {
				RealAfflineLinearInputFunction mFn = (RealAfflineLinearInputFunction)fn;
				new RealAfflineLinearDialog(parentFrame, GUIUtil.CLONE_DIALOG, mFn);
			}
			}catch(ClassNotFoundException e) {
			System.out.println(e.toString());
		}
	}

}
