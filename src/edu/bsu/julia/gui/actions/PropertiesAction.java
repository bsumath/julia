package edu.bsu.julia.gui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import edu.bsu.julia.Julia;
import edu.bsu.julia.output.OutputSet;

public class PropertiesAction extends AbstractAction {

	private Julia parentFrame;
	private OutputSet set;
	// for serializable interface: do not use
	public static final long serialVersionUID = 0;

	public PropertiesAction(Julia f, OutputSet s) {
		super("Properties", new ImageIcon(Thread.currentThread()
				.getContextClassLoader().getResource("prop.png")));
		putValue("SHORT_DESCRIPTION", "Properties");
		putValue("LONG_DESCRIPTION", "Open the properties tab for the "
				+ "chosen set.");
		parentFrame = f;
		set = s;
	}

	public void actionPerformed(ActionEvent arg0) {
		JOptionPane.showMessageDialog(parentFrame, set.propertiesComponents(),
				"Properties", JOptionPane.INFORMATION_MESSAGE);
	}

}
