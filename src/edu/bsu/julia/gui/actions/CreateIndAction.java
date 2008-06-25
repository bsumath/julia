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

public class CreateIndAction extends AbstractAction {
	private final Julia parentFrame;
	private final ButtonGroup typeGroup;
	private final ButtonGroup methodGroup;
	private List<OutputSetGenerator> generators;

	// for serializable interface: do not use
	public static final long serialVersionUID = 0;

	public CreateIndAction(Julia f, ButtonGroup t, ButtonGroup m) {
		super("Individual");
		parentFrame = f;
		typeGroup = t;
		methodGroup = m;
		putValue("SHORT_DESCRIPTION", "Create Individual Sets");
		putValue("LONG_DESCRIPTION", "Create Individual Sets "
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
		generators = new ArrayList<OutputSetGenerator>();
		if (command.equals("julia+ergodic")) {
			for (int i = 0; i < inFunc.length; i++) {
				generators.add(new ErgodicJuliaOutputSetGenerator(parentFrame,
						session.getIterations(), session.getSkips(), session
								.getSeedValue(),
						new InputFunction[] { inFunc[i] }));
			}
			type = OutputFunction.Type.IND_ERGODIC_JULIA;
		} else if (command.equals("julia+full")) {
			for (int i = 0; i < inFunc.length; i++) {
				generators.add(new FullJuliaOutputSetGenerator(parentFrame,
						session.getIterations(), session.getSeedValue(),
						new InputFunction[] { inFunc[i] }));
			}
			type = OutputFunction.Type.IND_FULL_JULIA;
		} else if (command.equals("attr+ergodic")) {
			for (int i = 0; i < inFunc.length; i++) {
				generators.add(new ErgodicAttrOutputSetGenerator(parentFrame,
						session.getIterations(), session.getSkips(), session
								.getSeedValue(),
						new InputFunction[] { inFunc[i] }));
			}
			type = OutputFunction.Type.IND_ERGODIC_ATTR;
		} else if (command.equals("attr+full")) {
			ComplexNumber[] seedList = new ComplexNumber[] { session
					.getSeedValue() };
			for (int i = 0; i < inFunc.length; i++) {
				generators.add(new FullAttrOutputSetGenerator(parentFrame,
						session.getIterations(), seedList,
						new InputFunction[] { inFunc[i] },
						Options.DISCARD_INTERMEDIATE_POINTS));
			}
			type = OutputFunction.Type.IND_FULL_ATTR;
		} else
			return;

		// check to make sure generators and inFunc are same size
		if (generators.size() != inFunc.length)
			return;

		// create and add the OutputFunctions
		for (int i = 0; i < generators.size(); i++) {
			OutputFunction function = new OutputFunction(session,
					new InputFunction[] { inFunc[i] }, type, generators.get(i));
			session.addOutputFunction(function);
		}
	}
}