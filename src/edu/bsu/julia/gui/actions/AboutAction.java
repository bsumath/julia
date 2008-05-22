package edu.bsu.julia.gui.actions;

import java.awt.event.*;
import javax.swing.*;
import edu.bsu.julia.*;

public class AboutAction extends AbstractAction {
	
	private Julia parentFrame;
	//for serializable interface: do not use
	public static final long serialVersionUID = 0;
	
	public AboutAction(Julia f) {
		super("About..", new ImageIcon
				(Thread.currentThread().getContextClassLoader().getResource
						("smiley.png")));
		putValue("SHORT_DESCRIPTION", "About this Program");
		putValue("LONG_DESCRIPTION", "General information about the " +
				"program.");
		parentFrame = f;
	}

	public void actionPerformed(ActionEvent arg0) {
		String output = "Julia (Version 1.0)\n\nDeveloped by:\n\t" +
			"Wendy Conatser\n\tTrey Butz\n\tYun Li\n\tKristopher Hart\n\tDr. Richard Stankewitz\n\n" +
			"Ball State University, Muncie, Indiana, 2007\n" +
			"All rights reserved." + 
			"\nAll icons by Everaldo, used under GNU Lesser General" +
			"\nPublic License.  Many thanks.";
		
		JOptionPane.showMessageDialog
			(parentFrame, output, "About..", JOptionPane.INFORMATION_MESSAGE);
	}

}
