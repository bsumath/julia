package edu.bsu.julia.gui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import edu.bsu.julia.Julia;
import edu.bsu.julia.output.OutputSet;
import edu.bsu.julia.session.Session;

/**
 * An action to delete the {@link OutputSet} which in turn will cancel its
 * execution
 * 
 * @author Ben Dean
 */
public class CancelOutputAction extends AbstractAction {
	// not used
	private static final long serialVersionUID = 0;

	private final Session session;
	private final OutputSet set;

	public CancelOutputAction(Julia parentFrame, OutputSet s) {
		super("Cancel and Delete", new ImageIcon(Thread.currentThread()
				.getContextClassLoader().getResource("delete.png")));
		putValue("SHORT_DESCRIPTION", "Cancel and Delete");
		putValue("LONG_DESCRIPTION",
				"Cancel the execution of the current Output Set.");

		session = parentFrame.getCurrentSession();
		set = s;
	}

	public void actionPerformed(ActionEvent arg0) {
		session.deleteOutputSet(set);
	}

}
