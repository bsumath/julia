package edu.bsu.julia.gui;

import java.awt.event.*;
import javax.swing.*;
import edu.bsu.julia.*;

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
