package edu.bsu.julia.gui;

import java.awt.event.*;
import edu.bsu.julia.*;

import javax.swing.*;

import java.awt.*;
import javax.swing.DefaultListModel;

public class ChangeColorAction extends AbstractAction {
	
	private Julia parentFrame;
	private DefaultListModel outputList;
	private int index;
	//for serializable interface: do not use
	public static final long serialVersionUID = 0;
	
	public ChangeColorAction(Julia f, DefaultListModel listModel, int i) {
		super("Change Color", new ImageIcon
				(Thread.currentThread().getContextClassLoader().getResource
						("color.png")));
		putValue("SHORT_DESCRIPTION", "Change Color");
		putValue("LONG_DESCRIPTION", "Change the color used to display " +
				"this function.");
		parentFrame = f;
		outputList = listModel;
		index = i;
	}

	public void actionPerformed(ActionEvent arg0) {
		OutputFunction fn = (OutputFunction)outputList.getElementAt(index);
		
		Color newColor = JColorChooser.showDialog(parentFrame, 
				"New Color for Function Display", fn.getColor());
		if(newColor!=null) fn.setColor(newColor);
	}

}
