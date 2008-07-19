package edu.bsu.julia.gui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.ButtonGroup;

import edu.bsu.julia.ComplexNumber;
import edu.bsu.julia.Julia;
import edu.bsu.julia.generators.FullBackwardsOutputSetGenerator;
import edu.bsu.julia.generators.FullForwardsOutputSetGenerator;
import edu.bsu.julia.generators.RandomBackwardsOutputSetGenerator;
import edu.bsu.julia.generators.RandomForwardsOutputSetGenerator;
import edu.bsu.julia.gui.InputPanel;
import edu.bsu.julia.input.InputFunction;
import edu.bsu.julia.output.OutputSet;
import edu.bsu.julia.session.Session;

public class CreateCompositeIndAction extends AbstractAction {
	public enum Mode {
		COMPOSITE, INDIVIDUAL, BOTH;

		/**
		 * @return a String to be used for the AbstractAction super constructor
		 */
		public String toString() {
			switch (this) {
			case COMPOSITE:
				return "Composite";
			case INDIVIDUAL:
				return "Individual";
			case BOTH:
				return "Composite and Individual";
			default:
				return "";
			}
		}

		/**
		 * @return the appropriate String for the long description of the Action
		 */
		public String longDescription() {
			switch (this) {
			case COMPOSITE:
				return "Create a Composite Set "
						+ "from the selected functions.";
			case INDIVIDUAL:
				return "Create Individual Sets "
						+ "from the selected functions.";
			case BOTH:
				return "Create a Composite Set and Individual "
						+ "Sets from the selected functions.";
			default:
				return "";
			}

		}

		/**
		 * @return the appropriate String for the short description of the
		 *         Action
		 */
		public String shortDescription() {
			switch (this) {
			case COMPOSITE:
				return "Create Composite Set";
			case INDIVIDUAL:
				return "Create Individual Sets";
			case BOTH:
				return "Create Composite and Individual Sets";
			default:
				return "";
			}
		}
	}

	private final Julia parentFrame;
	private final ButtonGroup typeGroup;
	private final ButtonGroup methodGroup;
	private final Mode mode;

	// for serializable interface: do not use
	public static final long serialVersionUID = 0;

	public CreateCompositeIndAction(Julia f, ButtonGroup t, ButtonGroup m,
			Mode mode) {
		super(mode.toString());
		parentFrame = f;
		typeGroup = t;
		methodGroup = m;
		putValue("SHORT_DESCRIPTION", mode.shortDescription());
		putValue("LONG_DESCRIPTION", mode.longDescription());

		this.mode = mode;
	}

	public void actionPerformed(ActionEvent arg0) {
		parentFrame.getOutputSetList().clearSelection();
		final Session session = parentFrame.getCurrentSession();

		// create a listener in case the output set creation is canceled
		ActionListener listener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				OutputSet set = (OutputSet) e.getSource();
				session.deleteOutputSet(set);
			}
		};

		// build list of input functions
		InputPanel inputPanel = parentFrame.getInputPanel();
		InputFunction[] inFunc = inputPanel.getSelectedFunctions();

		// create a command string based on which options are selected
		String command = (methodGroup.getSelection().getActionCommand() + "_" + typeGroup
				.getSelection().getActionCommand()).toUpperCase();

		// a list for all the new OutputSets
		List<OutputSet> newSets = new ArrayList<OutputSet>();

		// create an OutputSet based on the OutputSet.Type
		OutputSet.Type type = OutputSet.Type.valueOf(command);
		OutputSet.Info info = OutputSet.Info.sessionToInfo(session);
		switch (type) {
		case RANDOM_JULIA:
			if (mode == Mode.COMPOSITE || mode == Mode.BOTH)
				newSets.add(new OutputSet(info, inFunc, type,
						new RandomBackwardsOutputSetGenerator(parentFrame,
								session.getIterations(), session.getSkips(),
								session.getSeedValue(), inFunc), listener));
			if (mode == Mode.INDIVIDUAL || mode == Mode.BOTH)
				for (InputFunction function : inFunc) {
					InputFunction[] inArray = new InputFunction[] { function };
					newSets.add(new OutputSet(info, inArray, OutputSet.Type
							.valueOf("IND_" + command),
							new RandomBackwardsOutputSetGenerator(parentFrame,
									session.getIterations(),
									session.getSkips(), session.getSeedValue(),
									inArray), listener));
				}
			break;
		case FULL_JULIA:
			if (mode == Mode.COMPOSITE || mode == Mode.BOTH)
				newSets.add(new OutputSet(info, inFunc, type,
						new FullBackwardsOutputSetGenerator(parentFrame,
								session.getIterations(),
								session.getSeedValue(), inFunc), listener));
			if (mode == Mode.INDIVIDUAL || mode == Mode.BOTH)
				for (InputFunction function : inFunc) {
					InputFunction[] inArray = new InputFunction[] { function };
					newSets
							.add(new OutputSet(info, inArray, OutputSet.Type
									.valueOf("IND_" + command),
									new FullBackwardsOutputSetGenerator(
											parentFrame, session
													.getIterations(), session
													.getSeedValue(), inArray),
									listener));
				}
			break;
		case RANDOM_ATTR:
			if (mode == Mode.COMPOSITE || mode == Mode.BOTH)
				newSets.add(new OutputSet(info, inFunc, type,
						new RandomForwardsOutputSetGenerator(parentFrame,
								session.getIterations(), session.getSkips(),
								session.getSeedValue(), inFunc), listener));
			if (mode == Mode.INDIVIDUAL || mode == Mode.BOTH)
				for (InputFunction function : inFunc) {
					InputFunction[] inArray = new InputFunction[] { function };
					newSets.add(new OutputSet(info, inArray, OutputSet.Type
							.valueOf("IND_" + command),
							new RandomForwardsOutputSetGenerator(parentFrame,
									session.getIterations(),
									session.getSkips(), session.getSeedValue(),
									inArray), listener));
				}
			break;
		case FULL_ATTR:
			ComplexNumber[] seedList = new ComplexNumber[] { session
					.getSeedValue() };
			if (mode == Mode.COMPOSITE || mode == Mode.BOTH)
				newSets.add(new OutputSet(info, inFunc, type,
						new FullForwardsOutputSetGenerator(parentFrame, session
								.getIterations(), seedList, inFunc,
								FullForwardsOutputSetGenerator.Mode.DEFAULT),
						listener));
			if (mode == Mode.INDIVIDUAL || mode == Mode.BOTH)
				for (InputFunction function : inFunc) {
					InputFunction[] inArray = new InputFunction[] { function };
					OutputSet set = new OutputSet(
							info,
							inArray,
							OutputSet.Type.valueOf("IND_" + command),
							new FullForwardsOutputSetGenerator(parentFrame,
									session.getIterations(), seedList, inArray,
									FullForwardsOutputSetGenerator.Mode.DEFAULT),
							listener);
					newSets.add(set);
				}
			break;
		default:
			return;
		}

		// add the new OutputSets to the session
		for (OutputSet set : newSets) {
			session.addOutputSet(set);
		}
	}
}