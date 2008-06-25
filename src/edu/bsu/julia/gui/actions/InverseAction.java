package edu.bsu.julia.gui.actions;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.ButtonGroup;

import edu.bsu.julia.Julia;
import edu.bsu.julia.generators.InverseOutputSetGenerator;
import edu.bsu.julia.generators.OutputSetGenerator;
import edu.bsu.julia.gui.InputPanel;
import edu.bsu.julia.input.InputFunction;
import edu.bsu.julia.output.InverseOutputFunction;
import edu.bsu.julia.output.OutputFunction;
import edu.bsu.julia.session.Session;

public class InverseAction extends AbstractAction {
	private final Julia parentFrame;
	private final ButtonGroup methodGroup;
	private List<OutputSetGenerator> generators;

	// for serializable interface: do not use
	public static final long serialVersionUID = 0;

	public InverseAction(Julia f) {
		super("Inverse Image (Random/Full)");
		parentFrame = f;
		InputPanel inputPanel = parentFrame.getInputPanel();
		methodGroup = inputPanel.getMethodGroup();
		putValue("SHORT_DESCRIPTION", "Process Inverse Image");
		putValue(
				"LONG_DESCRIPTION",
				"Create a (Random/Full) Inverse Image of"
						+ "the selected Output Set(s) using the selected Input Functions");
	}

	public void actionPerformed(ActionEvent arg0) {
		// build list of input functions
		InputFunction[] inFunc = parentFrame.getInputPanel()
				.getSelectedFunctions();

		// build list of output functions
		Object[] objArray = parentFrame.getOutputFunctionList()
				.getSelectedValues();
		if (objArray.length == 0)
			return;
		OutputFunction[] outFunc = new OutputFunction[objArray.length];
		for (int i = 0; i < objArray.length; i++)
			outFunc[i] = (OutputFunction) objArray[i];
		parentFrame.getOutputFunctionList().clearSelection();

		String command = methodGroup.getSelection().getActionCommand();
		generators = new ArrayList<OutputSetGenerator>();
		OutputFunction.Type type;
		if (command.equals("ergodic")) {
			for (InputFunction function : inFunc) {
				generators.add(new InverseOutputSetGenerator(parentFrame,
						function, outFunc,
						InverseOutputSetGenerator.Type.ERGODIC));
			}
			type = OutputFunction.Type.INVERSE_ERGODIC_JULIA;
		} else if (command.equals("julia")) {
			for (InputFunction function : inFunc) {
				generators
						.add(new InverseOutputSetGenerator(parentFrame,
								function, outFunc,
								InverseOutputSetGenerator.Type.FULL));
			}
			type = OutputFunction.Type.INVERSE_FULL_JULIA;
		} else
			return;

		// check to make sure generators and inFunc are same size
		if (generators.size() != inFunc.length)
			return;

		// create and add the OutputFunctions
		Session session = parentFrame.getCurrentSession();
		for (int i = 0; i < generators.size(); i++) {
			OutputFunction function = new InverseOutputFunction(session,
					new InputFunction[] { inFunc[i] }, type, generators.get(i),
					outFunc);
			session.addOutputFunction(function);
		}
	}
}
