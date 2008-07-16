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
import edu.bsu.julia.output.OutputSet;
import edu.bsu.julia.output.RecursiveOutputSet;

public class PropertiesAction extends AbstractAction {
	
	private Julia parentFrame;
	private OutputSet set;
	//for serializable interface: do not use
	public static final long serialVersionUID = 0;
	
	public PropertiesAction(Julia f, OutputSet s) {
		super("Properties", new ImageIcon
				(Thread.currentThread().getContextClassLoader().getResource
						("prop.png")));
		putValue("SHORT_DESCRIPTION", "Properties");
		putValue("LONG_DESCRIPTION", "Open the properties tab for the "
				+ "chosen set.");
		parentFrame = f;
		set = s;
	}
	
	public void actionPerformed(ActionEvent arg0) {
		JPanel component = new JPanel();
		component.setLayout(new BorderLayout());
		
		Box panel = Box.createVerticalBox();
		JLabel type = new JLabel(set.toString());
		panel.add(type);
		String iterations = "Points to Plot:  " + set.getIterations();
		JLabel iterLabel = new JLabel(iterations);
		panel.add(iterLabel);
		String skips = "Skips:  " + set.getSkips();
		JLabel skipsLabel = new JLabel(skips);
		panel.add(skipsLabel);
		String seed = "Seed Value:  " + set.getSeedValue();
		JLabel seedLabel = new JLabel(seed);
		panel.add(seedLabel);
		String numOfPoints = "Actual number of points in the set:   "+set.getNumOfPoints();
		JLabel numOfPointsLabel = new JLabel(numOfPoints);
		panel.add(numOfPointsLabel);
		JLabel spacer = new JLabel("    ");
		panel.add(spacer);
		component.add(panel, BorderLayout.NORTH);
		
		InputFunction[] input = set.getInputFunctions();
		List<Object> objList = new ArrayList<Object>(Arrays.asList(input));
		objList.add(" ");
		if (set instanceof RecursiveOutputSet){
			OutputSet[] output = ((RecursiveOutputSet)set).getOutputSets();
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
