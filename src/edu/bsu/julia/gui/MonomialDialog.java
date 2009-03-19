package edu.bsu.julia.gui;

import java.awt.Checkbox;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.commons.math.complex.Complex;
import org.apache.commons.math.complex.ComplexUtils;

import edu.bsu.julia.Julia;
import edu.bsu.julia.input.MonomialInputFunction;
import edu.bsu.julia.session.Session;

/**
 * 
 * <h3>Description</h3>
 * <p>
 * Creates a dialog box for the function type: a/z^n. This dialog is used when
 * creating a new linear function, editing an existing function, or cloneing an
 * existing function.
 * 
 */
public class MonomialDialog extends JDialog implements ActionListener {
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
	private int dialogType;
	/**
	 * If this dialog is being used to edit or clone an existing function, the
	 * function that is being modified. If the dialog is being used to create a
	 * new function, this field is <b>null</b>.
	 */
	private MonomialInputFunction function;
	/**
	 * The text field in which the user enters the function's m value.
	 */
	private JTextField mField = new JTextField(5);
	/**
	 * The text field in which the user enters the real portion of the
	 * function's 'a' coefficient.
	 */
	private JTextField axField = new JTextField(3);
	/**
	 * The text field in which the user enters the number which, multiplied by
	 * i, is the imaginary portion of the function's 'a' coefficient.
	 */
	private JTextField ayField = new JTextField(3);
	
	private JTextField nxField = new JTextField(3);
	
	public static final long serialVersionUID = 0;
	// for serializable interface: do not use
	private Checkbox polarCheckBox;

	/**
	 * Builds the dialog for a Test input function and makes it visible on the
	 * screen. 'type' indicates whether the dialog will be used for editing or
	 * cloneing an existing function or creating a new function. If editing or
	 * cloneing an existing function, that function is passed to the constructor
	 * as 'fn'. When creating a new function, 'fn' can be <b>null</b>.
	 * 
	 * @param f
	 *            reference to the main program and its frame
	 * @param type
	 *            constant indicating whether the dialog will be used to edit,
	 *            clone, or create a new function
	 * @param fn
	 *            the function to be edited or cloned
	 */
	public MonomialDialog(Julia f, int type, MonomialInputFunction fn) {
		super(f, "Create a Test Function", false);
		parentFrame = f;
		dialogType = type;
		function = fn;

		Complex[] coefficients = new Complex[1];				/**Changed the 2 here to a 1.*/
		if (dialogType == GUIUtil.EDIT_DIALOG
				|| dialogType == GUIUtil.CLONE_DIALOG)
			coefficients = function.getCoefficients();

		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setLayout(new FlowLayout());

		add(new JLabel("Enter the m value:  "));
		if (dialogType == GUIUtil.EDIT_DIALOG
				|| dialogType == GUIUtil.CLONE_DIALOG)
			mField.setText("" + function.getM());
		else
			mField.setText("1");
		mField
				.addFocusListener(new TextFieldFocusListener(mField,
						parentFrame));
		add(mField);

		add(new JLabel("Please enter the function coefficients:"));
		JPanel functionPanel = new JPanel();
		// format all labels in HTML
		functionPanel.add(new JLabel("<html>(</html>"));
		if (dialogType == GUIUtil.EDIT_DIALOG						/**Taking the value axField which is the real part of a.*/
				|| dialogType == GUIUtil.CLONE_DIALOG) {
			String show = String.valueOf(coefficients[0].getReal());
			String showShort = show;
			if (show.length() > 5)
				showShort = show.substring(0, 5);
			axField.setText(showShort);
		} else
			axField.setText("1");
		axField.addFocusListener(new TextFieldFocusListener(axField,
				parentFrame));
		functionPanel.add(axField);
		functionPanel.add(new JLabel(", "));
		if (dialogType == GUIUtil.EDIT_DIALOG						/**Taking the value ayField which is the imaginary part of a.*/
				|| dialogType == GUIUtil.CLONE_DIALOG) {
			String show = String.valueOf(coefficients[0].getImaginary());
			String showShort = show;
			if (show.length() > 5)
				showShort = show.substring(0, 5);
			ayField.setText(showShort);
		} else
			ayField.setText("0");
		ayField.addFocusListener(new TextFieldFocusListener(ayField,
				parentFrame));
		functionPanel.add(ayField);
		functionPanel.add(new JLabel("<html>) /z^</html>"));

	
		if (dialogType == GUIUtil.EDIT_DIALOG						/**Taking the value axField which is the real part of b.*/			
				|| dialogType == GUIUtil.CLONE_DIALOG) {
			String show = String.valueOf(coefficients[1].getReal());
			String showShort = show;
			if (show.length() > 5)
				showShort = show.substring(0, 5);
			nxField.setText(showShort);
		} else
			nxField.setText("0");
		nxField.addFocusListener(new TextFieldFocusListener(nxField,
				parentFrame));
		functionPanel.add(nxField);
		add(functionPanel);											/**Not sure if I should keep this line.  Wait & see I guess*/

		JLabel polarCheckboxLabel = new JLabel(
				"Coefficient Values Use Polar Coordinates", JLabel.LEFT);
		add(polarCheckboxLabel);
		polarCheckBox = new Checkbox("", false);
		add(polarCheckBox);

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

		setSize(280, 200);
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
		if (mField.getText().equals("")) {
			JuliaError.EMPTY_FIELD.showDialog(parentFrame);
			return;
		}
		int m = 0;
		try {
			m = Integer.parseInt(GUIUtil.removeCommas(mField.getText()));
		} catch (NumberFormatException e) {
			JuliaError.M_INTEGER_ERROR.showDialog(parentFrame);
			return;
		}
		double ax = 0;
		double ay = 0;
		int nx = 0;												
		if (axField.getText().equals("") || ayField.getText().equals("")
				|| nxField.getText().equals("") ) {	
			JuliaError.EMPTY_FIELD.showDialog(parentFrame);
			return;
		}
		String axString = GUIUtil.removeCommas(axField.getText());
		axString = GUIUtil.parsePI(axString);
		String ayString = GUIUtil.removeCommas(ayField.getText());
		ayString = GUIUtil.parsePI(ayString);
		String nxString = GUIUtil.removeCommas(nxField.getText());
		nxString = GUIUtil.parsePI(nxString);

		try {
			ax = Double.parseDouble(axString);
			ay = Double.parseDouble(ayString);
			nx = Integer.parseInt(nxString);
		} catch (NumberFormatException e) {
			JuliaError.COEFFICIENT_FORMAT_ERROR.showDialog(parentFrame);
			return;
		}

		Complex a = new Complex(ax, ay);

		if (polarCheckBox.getState()) {
			a = ComplexUtils.polar2Complex(ax, ay);
		}

		MonomialInputFunction newFunction;
		try {
			newFunction = new MonomialInputFunction(m, a, nx);
		} catch (IllegalArgumentException e) {
			if (e.getMessage().equals("a zero"))
				JuliaError.LINEAR_ILLEGAL_ARGUMENT.showDialog(parentFrame);
			else
				JuliaError.M_NEG_ERROR.showDialog(parentFrame);
			return;
		}

		Session s = parentFrame.getCurrentSession();
		if (dialogType == GUIUtil.NEW_DIALOG
				|| dialogType == GUIUtil.CLONE_DIALOG)
			s.addInputFunction(newFunction);
		else
			s.replaceInputFunction(function, newFunction);

		setVisible(false);
		dispose();
	}

}
