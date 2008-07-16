package edu.bsu.julia.gui;

import javax.swing.*;
import javax.swing.event.*;
import edu.bsu.julia.*;

public class GraphTabbedPane extends JTabbedPane implements ChangeListener{
	
	private GraphScrollPane activePane;
	private Julia parentFrame;
	public static int tabNumber = 1;
	//for serializable interface: do not use
	public static final long serialVersionUID = 0;
	
	public GraphTabbedPane(Julia f) {
		super();
		setFocusable(false);
		parentFrame = f;
		GraphScrollPane scrollPane = new GraphScrollPane(parentFrame);
		addTab("Output Graph "+tabNumber, scrollPane);
		parentFrame.addKeyListener(scrollPane.getGLListener());
		parentFrame.getOutputSetList().addListSelectionListener
			(scrollPane.getGLListener());
		activePane = scrollPane;
		getModel().addChangeListener(this);
	}
	
	public GraphScrollPane getActivePane() {
		return activePane;
	}

	public void stateChanged(ChangeEvent event) {
		if(activePane!=null) activePane.getGLListener().disable();
		if(parentFrame.getKeyListeners().length>0)
			parentFrame.removeKeyListener(activePane.getGLListener());
		activePane = (GraphScrollPane)getComponentAt
			(getModel().getSelectedIndex());
		activePane.getGLListener().enable();
		parentFrame.addKeyListener(activePane.getGLListener());
		if(activePane.getGLListener().isUnsized()) {
			GLListener glList = activePane.getGLListener();
			glList.setPaneHeight(activePane.getHeight());
			glList.setPaneWidth(activePane.getWidth());
			glList.resetZoom();
		}
	}

}
