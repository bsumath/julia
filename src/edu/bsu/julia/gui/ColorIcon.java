package edu.bsu.julia.gui;

import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import edu.bsu.julia.output.OutputSet;

public class ColorIcon extends JPanel implements PropertyChangeListener {

	// for serializable interface: do not use
	public static final long serialVersionUID = 0;

	public ColorIcon(Color c) {
		setBackground(c);
		setBorder(BorderFactory.createLoweredBevelBorder());
		setSize(16, 16);
	}

	public void setColor(Color newColor) {
		setBackground(newColor);
	}

	public void setFunction(OutputSet set) {
		set.addListener(this);
	}

	public void propertyChange(PropertyChangeEvent event) {
		if (event.getPropertyName().equals("Color"))
			setBackground((Color) event.getNewValue());
	}

}
