package edu.bsu.julia.gui.actions;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import edu.bsu.julia.Julia;
import edu.bsu.julia.input.InputFunction;
import edu.bsu.julia.output.InverseOutputFunction;
import edu.bsu.julia.output.OutputSet;

public class PropertiesAction extends AbstractAction {
	
	private Julia parentFrame;
	private OutputSet function;
	//for serializable interface: do not use
	public static final long serialVersionUID = 0;
	
	public PropertiesAction(Julia f, OutputSet func) {
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
		List<Object> objList = new ArrayList<Object>(Arrays.asList(input));
		objList.add(" ");
		if (function instanceof InverseOutputFunction){
			OutputSet[] output = ((InverseOutputFunction)function).getOutputFunctions();
			objList.addAll(Arrays.asList(output));
		}
		
		JList list = new JList(objList.toArray());
		list.setVisibleRowCount(6);
		JScrollPane scroller = new JScrollPane(list);
		scroller.setBorder(BorderFactory.createTitledBorder("History"));
		component.add(scroller);
		
		JOptionPane.showMessageDialog(parentFrame, component, "Properties",
				JOptionPane.INFORMATION_MESSAGE);
	}

}
