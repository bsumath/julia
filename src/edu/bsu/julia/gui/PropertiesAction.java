package edu.bsu.julia.gui;

import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
import edu.bsu.julia.*;

public class PropertiesAction extends AbstractAction {
	
	private Julia parentFrame;
	private DefaultListModel outputList;
	private int index;
	//for serializable interface: do not use
	public static final long serialVersionUID = 0;
	
	public PropertiesAction(Julia f, DefaultListModel listModel, int i) {
		super("Properties", new ImageIcon
				(Thread.currentThread().getContextClassLoader().getResource
						("prop.png")));
		putValue("SHORT_DESCRIPTION", "Properties");
		putValue("LONG_DESCRIPTION", "Open the properties tab for the " +
				"chosen function.");
		parentFrame = f;
		outputList = listModel;
		index = i;
	}
	
	public void actionPerformed(ActionEvent arg0) {
		OutputFunction fn = (OutputFunction)outputList.getElementAt(index);
		
		JPanel component = new JPanel();
		component.setLayout(new BorderLayout());
		
		Box panel = Box.createVerticalBox();
		JLabel type = new JLabel(fn.toString());
		panel.add(type);
		String iterations = "Points to Plot:  " + fn.getIterations();
		JLabel iterLabel = new JLabel(iterations);
		panel.add(iterLabel);
		String skips = "Skips:  " + fn.getSkips();
		JLabel skipsLabel = new JLabel(skips);
		panel.add(skipsLabel);
		String seed = "Seed Value:  " + fn.getSeedValue();
		JLabel seedLabel = new JLabel(seed);
		panel.add(seedLabel);
		String numOfPoints = "Actual number of points in the set:   "+fn.getNumOfPoints();
		JLabel numOfPointsLabel = new JLabel(numOfPoints);
		panel.add(numOfPointsLabel);
		JLabel spacer = new JLabel("    ");
		panel.add(spacer);
		component.add(panel, BorderLayout.NORTH);
		
		InputFunction[] input = fn.getInputFunctions();
		JList list = new JList(input);
		list.setVisibleRowCount(6);
		JScrollPane scroller = new JScrollPane(list);
		scroller.setBorder(BorderFactory.createTitledBorder("Input Functions"));
		component.add(scroller);
		
		JOptionPane.showMessageDialog(parentFrame, component, "Properties",
				JOptionPane.INFORMATION_MESSAGE);
	}

}
