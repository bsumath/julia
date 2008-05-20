package edu.bsu.julia.gui;

import javax.swing.*;

import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.*;
import edu.bsu.julia.*;

public class SaveSessionDialog extends JDialog implements ActionListener{
	
	private Julia parentFrame;
	//for serializable interface: do not use
	public static final long serialVersionUID = 0;
	
	public SaveSessionDialog(Julia f) {
		super(f, "Save Current Session?", false);
		parentFrame = f;
		
		setLocationRelativeTo(parentFrame);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setLayout(new FlowLayout());
		add(new JLabel("Would you like to save the current session?"));
		
		JButton finishButton = new JButton("Save");
		finishButton.addActionListener(this);
		add(finishButton);
		
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				setVisible(false);
				dispose();
			}
		});
		add(cancelButton);
		
		setSize(280, 100);
		Point p = getLocation();
		p.x = p.x-140;
		p.y = p.y-50;
		setLocation(p);
		setVisible(true);
	}
	
	public void actionPerformed(ActionEvent event) {
		
	}

}
