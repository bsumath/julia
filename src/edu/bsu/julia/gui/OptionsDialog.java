package edu.bsu.julia.gui;

import java.awt.event.*;
import edu.bsu.julia.*;
import javax.swing.*;
import java.awt.*;

public class OptionsDialog extends JDialog implements ActionListener {
	
	private Julia parentFrame;
	private JSlider dotSizeslider = new JSlider(SwingConstants.HORIZONTAL,
			0, 10, 0);
	private Checkbox axisTriggerCheckBox =  new Checkbox("On/Off",true);
	private Checkbox grilTriggerCheckBox =  new Checkbox("On/Off",false);

	//for serializable interface: do not use
	public static final long serialVersionUID = 0;
	
	public OptionsDialog(Julia f) {
		super(f, "Options", false);
		parentFrame = f;
		
		setLocationRelativeTo(parentFrame);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setLayout(new FlowLayout());
		//Change Dot Size Label
		JLabel sliderLabel = new JLabel("Change Dot Size");
		sliderLabel.setBounds(0, 20 , 10, 55);
		add(sliderLabel);
		//Slider
		dotSizeslider.setMinimum(1);
		dotSizeslider.setMaximum(10);
		dotSizeslider.setMajorTickSpacing(1);
		dotSizeslider.setPaintTicks(true);
		dotSizeslider.setPaintLabels(true);
		dotSizeslider.setSnapToTicks(true);
		dotSizeslider.setValue(parentFrame.getDotSize());
		add(dotSizeslider);
		//Axis checkBox label
		JLabel axisCheckboxLabel = new JLabel("Turn on/off the Axis Bar",JLabel.LEFT);
		add(axisCheckboxLabel);
		//Axis checkBox
		axisTriggerCheckBox.setSize(1,1) ;
		axisTriggerCheckBox.setState(parentFrame.getAxisTrigger());
		add(axisTriggerCheckBox);
		//Gril checkBox label
		JLabel grilCheckboxLabel = new JLabel("Turn on/off the Grid",JLabel.LEFT);
		add(grilCheckboxLabel);
		//Gril checkBox
		grilTriggerCheckBox.setSize(1,1) ;
		grilTriggerCheckBox.setState(parentFrame.getGrilTrigger());
		add(grilTriggerCheckBox);
		

		//Ok Button
		JButton okButton = new JButton("Ok");
		okButton.addActionListener(this);
		okButton.setLocation(0, grilCheckboxLabel.SOUTH);
		add(okButton);
		//Cancel Button
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed (ActionEvent event) {
				setVisible(false);
				dispose();
			}
		});
		add(cancelButton);
		//size and location
		setSize(230, 230);
		Point p = getLocation();
		p.x = p.x - 110;
		p.y = p.y - 125;
		setLocation(p);
		setVisible(true);
	}

	public void actionPerformed(ActionEvent event) {
		int v = 1;
		boolean a = true;
		boolean g = false;
		try {
			v = dotSizeslider.getValue();
			parentFrame.setDotSize(v);
			a = axisTriggerCheckBox.getState();
			parentFrame.setAxisTrigger(a);
			g = grilTriggerCheckBox.getState();
			parentFrame.setGrilTrigger(g);
			setVisible(false);
			dispose();
			return;
			
		}catch(NumberFormatException e) {
			JOptionPane.showMessageDialog(parentFrame, "ERORR!", 
					"Please try again",JOptionPane.ERROR_MESSAGE);
			return;
		}
	}
}
