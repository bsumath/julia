package edu.bsu.julia.gui;

import java.awt.event.*;
import javax.swing.*;
import edu.bsu.julia.*;

public class ForwardImageAction extends AbstractAction {
	
	private Julia parentFrame;
	private ProgressMonitor pm;
	private ForwardImageThread fiThread;
	//for serializable interface: do not use
	public static final long serialVersionUID = 0;
	
	public ForwardImageAction(Julia f) {
		super("Forward Image");
		parentFrame = f;
		putValue("SHORT_DESCRIPTION", "Process Forward Image");
		putValue("LONG_DESCRIPTION", "Create a forward image set from the given" +
				" input and output functions.");
	}

	public void actionPerformed(ActionEvent arg0) {
		InputFunction[] inputFns = parentFrame.getInputPanel().getSelectedFunctions();
		if(inputFns.length == 0) return;
		JList outList = parentFrame.getOutputFunctionList();
		Object[] outObjs = outList.getSelectedValues();
		if(outObjs.length == 0) return;
		OutputFunction[] outputFns = new OutputFunction[outObjs.length];
		for(int i = 0;i<outputFns.length;i++) {
			outputFns[i] = (OutputFunction)outObjs[i];
		}
		int noOfInFns = inputFns.length;
		int noOfOutPts = 0;
		for(int i = 0; i<outputFns.length; i++) 
			noOfOutPts += outputFns[i].getPoints().length;
		pm = new ProgressMonitor(parentFrame,
                "Processing Functions...",
                "", 0, noOfInFns*noOfOutPts);
		fiThread = new ForwardImageThread(parentFrame);
		fiThread.start();
		Timer timer = new Timer(500, new TimerActionListener());
		timer.start();
	}
	
private class TimerActionListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
				if(!pm.isCanceled()&&fiThread.isAlive()) {
					pm.setProgress(fiThread.getProgress());
				}
				else {
					fiThread.setStop();
					pm.close();
				}
		}


}
}
