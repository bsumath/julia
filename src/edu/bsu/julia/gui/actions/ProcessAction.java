package edu.bsu.julia.gui.actions;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

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

public class ProcessAction extends AbstractAction {
	private final Julia parentFrame;
	private final ButtonGroup typeGroup;
	private final ButtonGroup methodGroup;
	private List<OutputSetGenerator> generators;

	// for serializable interface: do not use
	public static final long serialVersionUID = 0;

	public ProcessAction(Julia f, ButtonGroup t, ButtonGroup m) {
		super("Composite and Individual");
		parentFrame = f;
		typeGroup = t;
		methodGroup = m;
		putValue("SHORT_DESCRIPTION", "Create Composite and Individual Sets");
		putValue("LONG_DESCRIPTION", "Create a Composite Set and Individual "
				+ "Sets from the selected functions.");
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
		List<OutputFunction.Type> types = new ArrayList<OutputFunction.Type>();
		generators = new ArrayList<OutputSetGenerator>();
		if (command.equals("julia+ergodic")) {
			generators.add(new ErgodicJuliaOutputSetGenerator(parentFrame,
					session.getIterations(), session.getSkips(), session
							.getSeedValue(), inFunc));
			types.add(OutputFunction.Type.ERGODIC_JULIA);
			for (int i = 0; i < inFunc.length; i++) {
				generators.add(new ErgodicJuliaOutputSetGenerator(parentFrame,
						session.getIterations(), session.getSkips(), session
								.getSeedValue(),
						new InputFunction[] { inFunc[i] }));
				types.add(OutputFunction.Type.IND_ERGODIC_JULIA);
			}
		} else if (command.equals("julia+full")) {
			generators.add(new FullJuliaOutputSetGenerator(parentFrame, session
					.getIterations(), session.getSeedValue(), inFunc));
			types.add(OutputFunction.Type.FULL_JULIA);
			for (int i = 0; i < inFunc.length; i++) {
				generators.add(new FullJuliaOutputSetGenerator(parentFrame,
						session.getIterations(), session.getSeedValue(),
						new InputFunction[] { inFunc[i] }));
				types.add(OutputFunction.Type.IND_FULL_JULIA);
			}

		} else if (command.equals("attr+ergodic")) {
			generators.add(new ErgodicAttrOutputSetGenerator(parentFrame,
					session.getIterations(), session.getSkips(), session
							.getSeedValue(), inFunc));
			types.add(OutputFunction.Type.ERGODIC_ATTR);
			for (int i = 0; i < inFunc.length; i++) {
				generators.add(new ErgodicAttrOutputSetGenerator(parentFrame,
						session.getIterations(), session.getSkips(), session
								.getSeedValue(),
						new InputFunction[] { inFunc[i] }));
				types.add(OutputFunction.Type.IND_ERGODIC_ATTR);
			}
		} else if (command.equals("attr+full")) {
			ComplexNumber[] seedList = new ComplexNumber[] { session
					.getSeedValue() };
			generators.add(new FullAttrOutputSetGenerator(parentFrame, session
					.getIterations(), seedList, inFunc,
					Options.DISCARD_INTERMEDIATE_POINTS));
			types.add(OutputFunction.Type.FULL_ATTR);

			for (int i = 0; i < inFunc.length; i++) {
				generators.add(new FullAttrOutputSetGenerator(parentFrame,
						session.getIterations(), seedList,
						new InputFunction[] { inFunc[i] },
						Options.DISCARD_INTERMEDIATE_POINTS));
				types.add(OutputFunction.Type.IND_FULL_ATTR);
			}
		} else
			return;

		// check to make sure generators and types are same size
		if (generators.size() != types.size())
			return;

		// create and add the OutputFunctions
		for (int i = 0; i < generators.size(); i++) {
			OutputFunction function = new OutputFunction(session,
					inFunc, types.get(i), generators.get(i));
				session.addOutputFunction(function);
		}
	}
}