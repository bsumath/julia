package edu.bsu.julia.gui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ButtonGroup;

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
import edu.bsu.julia.output.OutputFunction;
import edu.bsu.julia.session.Session;

/**
 * an {@link AbstractAction} to create a composite output set
 * 
 * @author Ben Dean
 */
public class CreateCompositeAction extends AbstractAction {
	private final Julia parentFrame;
	private final ButtonGroup typeGroup;
	private final ButtonGroup methodGroup;
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
	}

	public void actionPerformed(ActionEvent arg0) {
		parentFrame.getOutputFunctionList().clearSelection();
		Session session = parentFrame.getCurrentSession();
		InputPanel inputPanel = parentFrame.getInputPanel();

		// build list of input functions
		InputFunction[] inFunc = inputPanel.getSelectedFunctions();

		// create a command string based on which options are selected
		String command = typeGroup.getSelection().getActionCommand() + "+"
				+ methodGroup.getSelection().getActionCommand();

		// create a generator for the command selected
		OutputFunction.Type type;
		if (command.equals("julia+ergodic")) {
			generator = new ErgodicJuliaOutputSetGenerator(parentFrame, session
					.getIterations(), session.getSkips(), session
					.getSeedValue(), inFunc);
			type = OutputFunction.Type.ERGODIC_JULIA;
		} else if (command.equals("julia+full")) {
			generator = new FullJuliaOutputSetGenerator(parentFrame, session
					.getIterations(), session.getSeedValue(), inFunc);
			type = OutputFunction.Type.FULL_JULIA;
		} else if (command.equals("attr+ergodic")) {
			generator = new ErgodicAttrOutputSetGenerator(parentFrame, session
					.getIterations(), session.getSkips(), session
					.getSeedValue(), inFunc);
			type = OutputFunction.Type.ERGODIC_ATTR;
		} else if (command.equals("attr+full")) {
			ComplexNumber[] seedList = new ComplexNumber[] { session
					.getSeedValue() };
			generator = new FullAttrOutputSetGenerator(parentFrame, session
					.getIterations(), seedList, inFunc,
					Options.DISCARD_INTERMEDIATE_POINTS);
			type = OutputFunction.Type.FULL_ATTR;
		} else
			return;

		// start the thread and timer
		OutputFunction function = new OutputFunction(session, inFunc, type,
				generator);
		session.addOutputFunction(function);
	}
}