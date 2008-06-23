package edu.bsu.julia.gui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.ButtonGroup;
import javax.swing.ProgressMonitor;
import javax.swing.Timer;

import edu.bsu.julia.ComplexNumber;
import edu.bsu.julia.Julia;
import edu.bsu.julia.generators.ErgodicAttrOutputSetGenerator;
import edu.bsu.julia.generators.ErgodicJuliaOutputSetGenerator;
import edu.bsu.julia.generators.FullAttrOutputSetGenerator;
import edu.bsu.julia.generators.FullJuliaOutputSetGenerator;
import edu.bsu.julia.generators.OutputSetGenerator;
import edu.bsu.julia.generators.FullAttrOutputSetGenerator.Options;
import edu.bsu.julia.gui.InputPanel;
import edu.bsu.julia.input.InputFunction;
import edu.bsu.julia.session.Session;

/**
 * an {@link AbstractAction} to create a composite output set
 * @author Ben Dean
 */
public class CreateCompositeAction extends AbstractAction {
	private final Julia parentFrame;
	private final ButtonGroup typeGroup;
	private final ButtonGroup methodGroup;
	private final ProgressMonitor pm;
	private Thread thread;
	private OutputSetGenerator generator;

	// for serializable interface: do not use
	public static final long serialVersionUID = 0;

	public CreateCompositeAction(Julia f, ButtonGroup t, ButtonGroup m) {
		super("Composite");
		parentFrame = f;
		typeGroup = t;
		methodGroup = m;
		putValue("SHORT_DESCRIPTION", "Create Composite Set");
		putValue("LONG_DESCRIPTION", "Create a Composite Set "
				+ "from the selected functions.");
		
		// create the progress monitor
		pm = new ProgressMonitor(parentFrame, "Processing Functions...", "", 0,
				100);
	}

	public void actionPerformed(ActionEvent arg0) {
		parentFrame.getOutputFunctionList().clearSelection();
		Session session = parentFrame.getCurrentSession();
		InputPanel inputPanel = parentFrame.getInputPanel();

		// build list of input functions
		List<InputFunction> inFunc = new ArrayList<InputFunction>();
		for (InputFunction func : inputPanel.getSelectedFunctions()) {
			inFunc.add(func);
		}

		// create a command string based on which options are selected
		String command = typeGroup.getSelection().getActionCommand() + "+"
				+ methodGroup.getSelection().getActionCommand();

		// create a generator for the command selected
		if (command.equals("julia+ergodic")) {
			generator = new ErgodicJuliaOutputSetGenerator(parentFrame, session
					.getIterations(), session.getSkips(), session
					.getSeedValue(), inFunc);
		} else if (command.equals("julia+full")) {
			generator = new FullJuliaOutputSetGenerator(parentFrame, session
					.getIterations(), session.getSeedValue(), inFunc);
		} else if (command.equals("attr+ergodic")) {
			generator = new ErgodicAttrOutputSetGenerator(parentFrame, session
					.getIterations(), session.getSkips(), session
					.getSeedValue(), inFunc);
		} else if (command.equals("attr+full")) {
			List<ComplexNumber> seedList = new ArrayList<ComplexNumber>();
			seedList.add(session.getSeedValue());
			generator = new FullAttrOutputSetGenerator(parentFrame, session
					.getIterations(), seedList, inFunc,
					Options.DISCARD_INTERMEDIATE_POINTS);
		}

		// start the thread and timer
		new Thread(generator).start();
		new Timer(500, new TimerActionListener()).start();
	}

	private final class TimerActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (!pm.isCanceled() && thread.isAlive()) {
				int progress = (int) (generator.getPercentComplete() * 100);
				progress = (progress < 0) ? 0 : progress;
				progress = (progress > 100) ? 100 : progress;
				pm.setProgress(progress);
			} else {
				generator.cancelExecution();
				pm.close();
			}
		}
	}
}