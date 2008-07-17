package edu.bsu.julia.gui.actions;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import edu.bsu.julia.ComplexNumber;
import edu.bsu.julia.Julia;
import edu.bsu.julia.generators.FullAttrOutputSetGenerator;
import edu.bsu.julia.generators.FullAttrOutputSetGenerator.Options;
import edu.bsu.julia.gui.GUIUtil;
import edu.bsu.julia.gui.InputPanel;
import edu.bsu.julia.gui.JuliaError;
import edu.bsu.julia.gui.TextFieldFocusListener;
import edu.bsu.julia.input.CubicInputFunction;
import edu.bsu.julia.input.InputFunction;
import edu.bsu.julia.input.QuadraticInputFunction;
import edu.bsu.julia.output.OutputSet;
import edu.bsu.julia.output.PostCriticalOutputSet;
import edu.bsu.julia.session.Session;

public class PostCriticalAction extends AbstractAction {
	private static final long serialVersionUID = -2696427897141258816L;
	private Julia parentFrame;

	public PostCriticalAction(Julia f) {
		super("Create Post Critical Set");
		putValue("SHORT_DESCRIPTION", "Create Post Critical Set");
		putValue("LONG_DESCRIPTION", "Create a Post Critical Set "
				+ "from the selected functions.");
		parentFrame = f;
	}

	public void actionPerformed(ActionEvent arg0) {
		parentFrame.getOutputSetList().clearSelection();
		new PostCriticalDialog(parentFrame);
	}

	/**
	 * <h3>Description</h3>
	 * <p>
	 * Creates a dialog box for the function create post critical set to input
	 * value for variable t. This dialog is used when creating a new post
	 * critical set.
	 * 
	 */
	private final class PostCriticalDialog extends JDialog implements
			ActionListener {
		/**
		 * The main frame over which this dialog sets.
		 */
		private Julia parentFrame;

		/**
		 * The text field in which the user enters the t value.
		 */
		private JTextField tField = new JTextField(5);

		// for serializable interface: do not use
		public static final long serialVersionUID = 0;

		public PostCriticalDialog(Julia f) {
			super(f, "Enter Value for t", false);
			parentFrame = f;

			setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			setLayout(new FlowLayout());

			add(new JLabel("Enter the t value:  "));

			tField.setText("1");
			tField.addFocusListener(new TextFieldFocusListener(tField,
					parentFrame));
			add(tField);

			JButton finishButton = new JButton("Finish");
			finishButton.addActionListener(this);
			add(finishButton);

			JButton cancelButton = new JButton("Cancel");
			cancelButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					setVisible(false);
					dispose();
				}
			});
			add(cancelButton);

			setSize(280, 160);
			setLocationRelativeTo(parentFrame);
			setVisible(true);
		}

		/**
		 * the action to perform when the user clicks the "Finish" button
		 */
		public void actionPerformed(ActionEvent event) {
			if (tField.getText().equals("")) {
				JuliaError.EMPTY_FIELD.showDialog(parentFrame);
				return;
			}

			int t;
			try {
				t = Integer.parseInt(GUIUtil.removeCommas(tField.getText()));
			} catch (NumberFormatException e) {
				JuliaError.T_INTEGER_ERROR.showDialog(parentFrame);
				return;
			}

			if (t <= 0) {
				JuliaError.T_INTEGER_ERROR.showDialog(parentFrame);
				return;
			}

			// get the current session
			Session session = parentFrame.getCurrentSession();

			// build list of input functions
			InputPanel inputPanel = parentFrame.getInputPanel();
			InputFunction[] inFunc = inputPanel.getSelectedFunctions();

			// find the critical points to use as seeds
			List<ComplexNumber> seedList = new ArrayList<ComplexNumber>();
			ComplexNumber negOne = new ComplexNumber(-1, 0);
			ComplexNumber two = new ComplexNumber(2, 0);
			ComplexNumber zero = new ComplexNumber(0, 0);
			for (InputFunction function : inFunc) {
				ComplexNumber seed;
				if (function instanceof CubicInputFunction) {
					seed = zero;
				} else if (function instanceof QuadraticInputFunction) {
					ComplexNumber[] coefficients = function.getCoefficients();
					seed = (coefficients[1].multiply(negOne))
							.divide(coefficients[0].multiply(two));
				} else
					continue;

				for (int seedi = 0; seedi < function.getM(); seedi++) {
					seed = function.evaluateFunction(seed);
					seedList.add(seed);
				}
			}

			// see if any critical points were found
			if (seedList.size() == 0) {
				JOptionPane.showMessageDialog(parentFrame,
						"No critical Points exist");
			} else {
				// create the output function and add it to the session
				session.addOutputSet(new PostCriticalOutputSet(session,
						inFunc, OutputSet.Type.POST_CRITICAL,
						new FullAttrOutputSetGenerator(parentFrame, session
								.getIterations(), seedList
								.toArray(new ComplexNumber[] {}), inFunc,
								Options.KEEP_INTERMEDIATE_POINTS), t));
			}

			// close the dialog window
			setVisible(false);
			dispose();
		}
	}
}