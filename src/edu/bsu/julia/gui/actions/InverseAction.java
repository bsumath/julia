package edu.bsu.julia.gui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.ButtonGroup;

import edu.bsu.julia.ComplexNumber;
import edu.bsu.julia.Julia;
import edu.bsu.julia.generators.RecursiveOutputSetGenerator;
import edu.bsu.julia.generators.OutputSetGenerator;
import edu.bsu.julia.gui.InputPanel;
import edu.bsu.julia.input.InputFunction;
import edu.bsu.julia.output.RecursiveOutputSet;
import edu.bsu.julia.output.OutputSet;
import edu.bsu.julia.session.Session;

public class InverseAction extends AbstractAction {
	private final Julia parentFrame;
	private final ButtonGroup methodGroup;

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
		Object[] objArray = parentFrame.getOutputSetList().getSelectedValues();
		if (objArray.length == 0)
			return;
		OutputSet[] outFunc = new OutputSet[objArray.length];
		for (int i = 0; i < objArray.length; i++)
			outFunc[i] = (OutputSet) objArray[i];
		parentFrame.getOutputSetList().clearSelection();

		// get the current session
		final Session session = parentFrame.getCurrentSession();

		// create a listener in case the output set creation is canceled
		ActionListener listener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				OutputSet set = (OutputSet) e.getSource();
				session.deleteOutputSet(set);
			}
		};

		// create data for the set
		OutputSet.Info info = new OutputSet.Info() {
			@Override
			public Integer iterations() {
				return null;
			}

			@Override
			public ComplexNumber seed() {
				return null;
			}

			@Override
			public Integer skips() {
				return null;
			}
		};

		// create and add the Output Functions
		String command = methodGroup.getSelection().getActionCommand()
				.toUpperCase();
		for (InputFunction function : inFunc) {
			OutputSetGenerator generator = new RecursiveOutputSetGenerator(
					parentFrame, function, outFunc,
					RecursiveOutputSetGenerator.Type.valueOf(command));
			OutputSet outputSet = new RecursiveOutputSet(info,
					new InputFunction[] { function }, outFunc,
					RecursiveOutputSetGenerator.Type.valueOf(command)
							.outputType(), generator, listener);
			session.addOutputSet(outputSet);
		}
	}
}
