package edu.bsu.julia.gui.actions;

import java.awt.Color;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JColorChooser;

import edu.bsu.julia.Julia;
import edu.bsu.julia.output.OutputSet;

public class ChangeColorAction extends AbstractAction {

	private Julia parentFrame;
	private OutputSet set;
	// for serializable interface: do not use
	public static final long serialVersionUID = 0;

	public ChangeColorAction(Julia f, OutputSet s) {
		super("Change Color", new ImageIcon(Thread.currentThread()
				.getContextClassLoader().getResource("color.png")));
		putValue("SHORT_DESCRIPTION", "Change Color");
		putValue("LONG_DESCRIPTION", "Change the color used to display "
				+ "this function.");
		parentFrame = f;
		set = s;
	}

	public void actionPerformed(ActionEvent arg0) {
		Color newColor = JColorChooser.showDialog(parentFrame,
				"New Color for Function Display", set.getColor());
		if (newColor != null)
			set.setColor(newColor);
	}

}
