package edu.bsu.julia.gui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import edu.bsu.julia.ComplexNumber;
import edu.bsu.julia.Julia;
import edu.bsu.julia.generators.FullAttrOutputSetGenerator;
import edu.bsu.julia.generators.OutputSetGenerator;
import edu.bsu.julia.input.InputFunction;
import edu.bsu.julia.output.InverseOutputFunction;
import edu.bsu.julia.output.OutputFunction;
import edu.bsu.julia.session.Session;

public class ForwardImageAction extends AbstractAction {
	private final Julia parentFrame;

	// for serializable interface: do not use
	public static final long serialVersionUID = 0;

	public ForwardImageAction(Julia f) {
		super("Forward Image");
		parentFrame = f;
		putValue("SHORT_DESCRIPTION", "Process Forward Image");
		putValue(
				"LONG_DESCRIPTION",
				"Create a forward image set of the selected"
						+ " Output Set(s) using the selected Input Function(s).");
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

		// build list of points
		int size = 0;
		for (OutputFunction out : outFunc) {
			size += out.getNumOfPoints();
		}
		ComplexNumber[] points = new ComplexNumber[size];
		int index = 0;
		for (OutputFunction out : outFunc) {
			for (ComplexNumber p : out.getPoints())
				points[index++] = p;
		}

		// create and add the OutputFunctions
		Session session = parentFrame.getCurrentSession();
		for (InputFunction function : inFunc) {
			InputFunction[] inArray = new InputFunction[] { function };

			OutputSetGenerator generator = new FullAttrOutputSetGenerator(
					parentFrame,
					points.length,
					points,
					inArray,
					FullAttrOutputSetGenerator.Options.DISCARD_INTERMEDIATE_POINTS);
			OutputFunction outputFunction = new InverseOutputFunction(session,
					inArray, OutputFunction.Type.INVERSE_ATTR, generator, outFunc);
			
			session.addOutputFunction(outputFunction);
		}
	}
}
