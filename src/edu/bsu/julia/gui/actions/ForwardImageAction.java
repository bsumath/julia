package edu.bsu.julia.gui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import edu.bsu.julia.ComplexNumber;
import edu.bsu.julia.Julia;
import edu.bsu.julia.generators.FullForwardsOutputSetGenerator;
import edu.bsu.julia.generators.OutputSetGenerator;
import edu.bsu.julia.input.InputFunction;
import edu.bsu.julia.output.RecursiveOutputSet;
import edu.bsu.julia.output.OutputSet;
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
		Object[] objArray = parentFrame.getOutputSetList()
				.getSelectedValues();
		if (objArray.length == 0)
			return;
		OutputSet[] outFunc = new OutputSet[objArray.length];
		for (int i = 0; i < objArray.length; i++)
			outFunc[i] = (OutputSet) objArray[i];
		parentFrame.getOutputSetList().clearSelection();

		// build list of points
		int size = 0;
		for (OutputSet out : outFunc) {
			size += out.getNumOfPoints();
		}
		ComplexNumber[] points = new ComplexNumber[size];
		int index = 0;
		for (OutputSet out : outFunc) {
			for (ComplexNumber p : out.getPoints())
				points[index++] = p;
		}

		// create and add the OutputSets
		Session session = parentFrame.getCurrentSession();
		for (InputFunction function : inFunc) {
			InputFunction[] inArray = new InputFunction[] { function };

			OutputSetGenerator generator = new FullForwardsOutputSetGenerator(
					parentFrame,
					points.length,
					points,
					inArray,
					FullForwardsOutputSetGenerator.Mode.DEFAULT);
			OutputSet outputSet = new RecursiveOutputSet(session,
					inArray, OutputSet.Type.FORWARD_IMAGE, generator, outFunc);
			
			session.addOutputSet(outputSet);
		}
	}
}
