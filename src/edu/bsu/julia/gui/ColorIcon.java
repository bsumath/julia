package edu.bsu.julia.gui;

import javax.swing.*;
import java.awt.*;
import edu.bsu.julia.*;
import edu.bsu.julia.output.OutputFunction;

import java.beans.*;

public class ColorIcon extends JPanel implements PropertyChangeListener{
	
	//for serializable interface: do not use
	public static final long serialVersionUID = 0;
	
	public ColorIcon(Color c) {
		setBackground(c);
		setBorder(BorderFactory.createLoweredBevelBorder());
		setSize(16,16);
	}
	
	public void setColor(Color newColor) {
		setBackground(newColor);
	}
	
	public void setFunction(OutputFunction fn) {
		OutputFunction function = fn;
		function.addListener(this);
	}
	
	public void propertyChange(PropertyChangeEvent event) {
		if(event.getPropertyName().equals("Color")) 
			setBackground((Color)event.getNewValue());
	}

}
