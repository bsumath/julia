package edu.bsu.julia.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import edu.bsu.julia.*;

public class AddFunctionAction extends AbstractAction {
	
	private Julia parentFrame;
	//for serializable interface: do not use
	public static final long serialVersionUID = 0;
	
	public AddFunctionAction(Julia f) {
		super("Add Function", new ImageIcon
				(Thread.currentThread().getContextClassLoader().getResource
						("addformula.png")));
		parentFrame = f;
		putValue("SHORT_DESCRIPTION", "Add Function");
		putValue("LONG_DESCRIPTION", "Add a new function to the present " +
				"input function list.");
	}

	public void actionPerformed(ActionEvent arg0) {
		final JDialog addFunctionDialog = new JDialog(parentFrame,
				"Add a Function", false);
		addFunctionDialog.setLocationRelativeTo(parentFrame);
		addFunctionDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		addFunctionDialog.setLayout(new FlowLayout());
		
		JLabel choiceLabel = new JLabel("Please Choose a Function Type:");
		addFunctionDialog.add(choiceLabel);

		String[] choices = new String[5];
		choices[0] = "<html><h2>az + b</h2></html>";
		choices[1] = "<html><h2>(az + b) / (cz + d)</h2></html>";
		choices[2] = "<html><h2>az<sup>2</sup> + bz + c</h2></html>";	
		choices[3] = "<html><h2>az<sup>3</sup> + b</h2></html>";
		choices[4] = "<html><h2>[a, b ; c, d]z + [e ; f]</h2></html>";
		final JList list = new JList(choices);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setVisibleRowCount(6);
		list.setSelectedIndex(0);
		JScrollPane pane = new JScrollPane(list);
		addFunctionDialog.add(pane);
		
		JPanel buttonPanel = new JPanel();
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				addFunctionDialog.setVisible(false);
				addFunctionDialog.dispose();
			}
		});
		buttonPanel.add(cancelButton);
		
		JButton nextButton = new JButton("Next >>>");
		nextButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				int type = list.getSelectedIndex();
				addFunctionDialog.setVisible(false);
				addFunctionDialog.dispose();
				switch(type) {
				case 0:
					new LinearDialog(parentFrame, GUIUtil.NEW_DIALOG, null);
					break;
				case 1:
					new MobiusDialog(parentFrame, GUIUtil.NEW_DIALOG, null);
					break;
				case 2:
					new QuadraticDialog(parentFrame, GUIUtil.NEW_DIALOG, null);
					break;
				case 3:
					new CubicDialog(parentFrame, GUIUtil.NEW_DIALOG, null);
					break;
				case 4:
					new RealAfflineLinearDialog(parentFrame, GUIUtil.NEW_DIALOG, null);
				}
			}
		});
		buttonPanel.add(nextButton);
		addFunctionDialog.add(buttonPanel);
		
		addFunctionDialog.setSize(250,430);
		Point p = addFunctionDialog.getLocation();
		p.x = p.x-125;
		p.y = p.y-215;
		addFunctionDialog.setLocation(p);
		addFunctionDialog.setVisible(true);
	}

}
