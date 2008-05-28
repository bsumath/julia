package edu.bsu.julia.gui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

import edu.bsu.julia.Julia;
import edu.bsu.julia.PostCriticalThread;

/**
 * 
 * <h3>Description</h3>
 * <p>
 * Creates a dialog box for the function create post critical set to input value
 * for variable t. This dialog is used when creating a new post critical set.
 * 
 */
public class PostCriticalDialog extends JDialog implements ActionListener {
	/**
	 * The main frame over which this dialog sets.
	 */
	private Julia parentFrame;
	/**
	 * One of three types defined in a utility class, GUIUtil:
	 * {@link GUIUtil#NEW_DIALOG}, {@link GUIUtil#EDIT_DIALOG}, and
	 * {@link GUIUtil#CLONE_DIALOG}. It indicates the use to which this dialog
	 * is being put.
	 */

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
		tField
				.addFocusListener(new TextFieldFocusListener(tField,
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
	 * Triggered when the user hits "Finish" on the dialog box. Builds the
	 * function and then either adds it to the
	 * {@link edu.bsu.julia.session.Session}'s input function list or replaces
	 * the old function on the list (if it is being used to edit a function).
	 * Input functions are immutable objects.
	 */
	public void actionPerformed(ActionEvent event) {
		if (tField.getText().equals("")) {
			new JuliaError(JuliaError.EMPTY_FIELD, parentFrame);
			return;
		}
		int t = 0;
		try {
			t = Integer.parseInt(GUIUtil.removeCommas(tField.getText()));
		} catch (NumberFormatException e) {
			new JuliaError(JuliaError.T_INTEGER_ERROR, parentFrame);
			return;
		}

		if (t <= 0) {
			new JuliaError(JuliaError.T_INTEGER_ERROR, parentFrame);
			return;
		}

		PostCriticalThread ejThread = new PostCriticalThread(parentFrame, t);
		ejThread.start();

		setVisible(false);
		dispose();
	}

}
