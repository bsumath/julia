package edu.bsu.julia.gui.actions;

import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
import edu.bsu.julia.*;
import edu.bsu.julia.input.InputFunction;
import edu.bsu.julia.output.OutputFunction;

public class PropertiesAction extends AbstractAction {
	
	private Julia parentFrame;
	private OutputFunction function;
	//for serializable interface: do not use
	public static final long serialVersionUID = 0;
	
	public PropertiesAction(Julia f, OutputFunction func) {
		super("Properties", new ImageIcon
				(Thread.currentThread().getContextClassLoader().getResource
						("prop.png")));
		putValue("SHORT_DESCRIPTION", "Properties");
		putValue("LONG_DESCRIPTION", "Open the properties tab for the "
				+ "chosen function.");
		parentFrame = f;
		function = func;
	}
	
	public void actionPerformed(ActionEvent arg0) {
		JPanel component = new JPanel();
		component.setLayout(new BorderLayout());
		
		Box panel = Box.createVerticalBox();
		JLabel type = new JLabel(function.toString());
		panel.add(type);
		String iterations = "Points to Plot:  " + function.getIterations();
		JLabel iterLabel = new JLabel(iterations);
		panel.add(iterLabel);
		String skips = "Skips:  " + function.getSkips();
		JLabel skipsLabel = new JLabel(skips);
		panel.add(skipsLabel);
		String seed = "Seed Value:  " + function.getSeedValue();
		JLabel seedLabel = new JLabel(seed);
		panel.add(seedLabel);
		String numOfPoints = "Actual number of points in the set:   "+function.getNumOfPoints();
		JLabel numOfPointsLabel = new JLabel(numOfPoints);
		panel.add(numOfPointsLabel);
		JLabel spacer = new JLabel("    ");
		panel.add(spacer);
		component.add(panel, BorderLayout.NORTH);
		
		InputFunction[] input = function.getInputFunctions();
		JList list = new JList(input);
		list.setVisibleRowCount(6);
		JScrollPane scroller = new JScrollPane(list);
		scroller.setBorder(BorderFactory.createTitledBorder("Input Functions"));
		component.add(scroller);
		
		JOptionPane.showMessageDialog(parentFrame, component, "Properties",
				JOptionPane.INFORMATION_MESSAGE);
	}

}
