package edu.bsu.julia.gui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.ButtonGroup;
import javax.swing.ProgressMonitor;
import javax.swing.Timer;

import edu.bsu.julia.CErgodicAttrThread;
import edu.bsu.julia.CErgodicJuliaThread;
import edu.bsu.julia.CFullAttrThread;
import edu.bsu.julia.CFullJuliaThread;
import edu.bsu.julia.Julia;
import edu.bsu.julia.gui.InputPanel;

public class CreateCompositeAction extends AbstractAction {
	
	private Julia parentFrame;
	private ButtonGroup typeGroup;
	private ButtonGroup methodGroup;
	private InputPanel inputPanel;
	private ProgressMonitor pm;
	private CErgodicJuliaThread ejThread;
	private CFullJuliaThread fjThread;
	private CErgodicAttrThread eaThread;
	private CFullAttrThread faThread;
	//for serializable interface: do not use
	public static final long serialVersionUID = 0;
	
	public CreateCompositeAction(Julia f, ButtonGroup t, ButtonGroup m) {
		super("Composite");
		parentFrame = f;
		typeGroup = t;
		methodGroup = m;
		putValue("SHORT_DESCRIPTION", "Create Composite Set");
		putValue("LONG_DESCRIPTION", "Create a Composite Set " +
				"from the selected functions.");
	}

	public void actionPerformed(ActionEvent arg0) 
	{
		parentFrame.getOutputFunctionList().clearSelection();
		inputPanel = parentFrame.getInputPanel();
		if (typeGroup.getSelection().getActionCommand() == "julia") 
		{
			if(methodGroup.getSelection().getActionCommand() == "ergodic")
				ergodicJulia();
			else fullJulia();
		}
		else 
		{
			if(methodGroup.getSelection().getActionCommand() == "ergodic") 
				ergodicAttr();
			else fullAttr();
		}
	}
	
	private void ergodicJulia() 
	{
		int noOfFns = inputPanel.getSelectedFunctions().length;
		int iterations = parentFrame.getCurrentSession().getIterations();
		pm = new ProgressMonitor(parentFrame,
			"Processing Functions...",
			"", 0, (iterations)*(noOfFns+1));
		ejThread = new CErgodicJuliaThread(parentFrame);
		ejThread.start();
		Timer timer = new Timer(500, new TimerActionListener(0));
		timer.start();
	}
	
	private void fullJulia() 
	{
		int noOfFns = inputPanel.getSelectedFunctions().length;
		int iterations = parentFrame.getCurrentSession().getIterations();
		pm = new ProgressMonitor(parentFrame,
			"Processing Functions...",
			"", 0, (iterations)*(noOfFns+1));
		fjThread = new CFullJuliaThread(parentFrame);
		fjThread.start();
		Timer timer = new Timer(500, new TimerActionListener(1));
		timer.start();
	}
	
	private void ergodicAttr() 
	{
		int noOfFns = inputPanel.getSelectedFunctions().length;
		int iterations = parentFrame.getCurrentSession().getIterations();
		pm = new ProgressMonitor(parentFrame,
			"Processing Functions...",
			"", 0, (iterations)*(noOfFns+1));
		eaThread = new CErgodicAttrThread(parentFrame);
		eaThread.start();
		Timer timer = new Timer(500, new TimerActionListener(2));
		timer.start();
	}
	
	private void fullAttr() 
	{
		int noOfFns = inputPanel.getSelectedFunctions().length;
		int iterations = parentFrame.getCurrentSession().getIterations();
		pm = new ProgressMonitor(parentFrame,
			"Processing Functions...",
			"", 0, (iterations)*(noOfFns+1));
		faThread = new CFullAttrThread(parentFrame);
		faThread.start();
		Timer timer = new Timer(500, new TimerActionListener(3));
		timer.start();
	}
	
	private class TimerActionListener implements ActionListener 
	{
		
		int threadType;
		public static final int EJTHREAD = 0;
		public static final int FJTHREAD = 1;
		public static final int EATHREAD = 2;
		public static final int FATHREAD = 3;
		
		public TimerActionListener(int type) 
		{
			threadType = type;
		}

		public void actionPerformed(ActionEvent e) 
		{
			switch(threadType) 
			{
				case 0:
					if(!pm.isCanceled()&&ejThread.isAlive()) 
					{
						pm.setProgress(ejThread.getProgress());
					}
					else 
					{
						ejThread.setStop();
						pm.close();
					}
					break;
				case 1:
					if(!pm.isCanceled()&&fjThread.isAlive()) 
					{
						pm.setProgress(fjThread.getProgress());
					}
					else 
					{
						fjThread.setStop();
						pm.close();
					}
					break;
				case 2:
					if(!pm.isCanceled()&&eaThread.isAlive()) 
					{
						pm.setProgress(eaThread.getProgress());
					}
					else 
					{
						eaThread.setStop();
						pm.close();
					}
					break;
				case 3:
					if(!pm.isCanceled()&&faThread.isAlive()) 
					{
						pm.setProgress(faThread.getProgress());
					}
					else 
					{
						faThread.setStop();
						pm.close();
					}
			}
		}
		
	}

}