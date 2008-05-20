 package edu.bsu.julia.gui;

import java.awt.event.*;
import edu.bsu.julia.*;
import javax.swing.*;
import javax.swing.Timer;

public class InverseAction extends AbstractAction {
	
	private Julia parentFrame;
	private InputPanel inputPanel;
	private ButtonGroup methodGroup;
	private InputFunction[] inputFns;
	private OutputFunction[] outputFns;
	private ProgressMonitor pm;
	private ErgodicInverseThread eThread;
	private FullInverseThread fThread;
	//for serializable interface: do not use
	public static final long serialVersionUID = 0;
	
	public InverseAction(Julia f) {
		super("Inverse Image (Random/Full)");
		parentFrame = f;
		inputPanel = parentFrame.getInputPanel();
		methodGroup = inputPanel.getMethodGroup();
		putValue("SHORT_DESCRIPTION", "Process Inverse Image");
		putValue("LONG_DESCRIPTION", "Create a (Random/Full) Inverse Image of" +
				"the selected Output Set(s) using the selected Input Functions");
	}

	public void actionPerformed(ActionEvent arg0) {
		inputFns = inputPanel.getSelectedFunctions();
		if(inputFns.length == 0) return;
		JList outList = parentFrame.getOutputFunctionList();
		Object[] outObjs = outList.getSelectedValues();
		outList.clearSelection();
		if(outObjs.length == 0) return;
		outputFns = new OutputFunction[outObjs.length];
		for(int i = 0;i<outputFns.length;i++) {
			outputFns[i] = (OutputFunction)outObjs[i];
		}
		if(methodGroup.getSelection().getActionCommand() == "ergodic") 
			ergodicJuliaInv();
		else fullJuliaInv();
	}
	
	public void ergodicJuliaInv() {
		int noOfInFns = inputFns.length;
		int noOfOutPts = 0;
		for(int i = 0; i<outputFns.length; i++) 
			noOfOutPts += outputFns[i].getPoints().length;
		pm = new ProgressMonitor(parentFrame,
                "Processing Functions...",
                "", 0, noOfInFns*noOfOutPts);
		eThread = new ErgodicInverseThread(parentFrame, inputFns, outputFns);
		eThread.start();
		Timer timer = new Timer(500, new TimerActionListener(0));
		timer.start();
	}
	
	public void fullJuliaInv() {
		int noOfInFns = inputFns.length;
		int noOfOutPts = 0;
		for(int i = 0; i<outputFns.length; i++) 
			noOfOutPts += outputFns[i].getPoints().length;
		pm = new ProgressMonitor(parentFrame,
                "Processing Functions...",
                "", 0, noOfInFns*3*noOfOutPts);
		fThread = new FullInverseThread(parentFrame, inputFns, outputFns);
		fThread.start();
		Timer timer = new Timer(500, new TimerActionListener(1));
		timer.start();
	}
	
private class TimerActionListener implements ActionListener {
		
		int threadType;
		public static final int ETHREAD = 0;
		public static final int FTHREAD = 1;
		
		public TimerActionListener(int type) {
			threadType = type;
		}

		public void actionPerformed(ActionEvent e) {
			switch(threadType) {
			case 0:
				if(!pm.isCanceled()&&eThread.isAlive()) {
					pm.setProgress(eThread.getProgress());
				}
				else {
					eThread.setStop();
					pm.close();
				}
				break;
			case 1:
				if(!pm.isCanceled()&&fThread.isAlive()) {
					pm.setProgress(fThread.getProgress());
				}
				else {
					fThread.setStop();
					pm.close();
				}
			}
		}
		
	}

}
