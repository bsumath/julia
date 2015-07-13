package edu.bsu.julia.gui;

import java.awt.Checkbox;
import java.awt.Font;
import java.awt.GridLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.commons.math.complex.Complex;
import org.apache.commons.math.complex.ComplexUtils;

import edu.bsu.julia.Julia;
import edu.bsu.julia.input.MobiusInputFunction;
import edu.bsu.julia.session.Session;

/**
 * 
 * <h3>Description</h3>
 * <p>
 * Creates a dialog box for the function type: (az + b)/(cz + d). This dialog is
 * used when creating a new cubic function, editing an existing function, or
 * cloneing an existing function.
 * 
 */
public class MobiusDialog extends JDialog implements ActionListener {

	private static final Complex[] DEFAULT_COEFFICIENTS = new Complex[] {
			Complex.ONE, Complex.ZERO, Complex.ZERO, Complex.ONE };

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
	private MobiusInputFunction function;

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

	/**
	 * The text field in which the user enters the real portion of the
	 * function's 'b' coefficient.
	 */
	private JTextField bxField = new JTextField(3);

	/**
	 * The text field in which the user enters the number which, multiplied by
	 * i, is the imaginary portion of the function's 'b' coefficient.
	 */
	private JTextField byField = new JTextField(3);

	/**
	 * The text field in which the user enters the real portion of the
	 * function's 'c' coefficient.
	 */
	private JTextField cxField = new JTextField(3);

	/**
	 * The text field in which the user enters the number which, multiplied by
	 * i, is the imaginary portion of the function's 'c' coefficient.
	 */
	private JTextField cyField = new JTextField(3);

	/**
	 * The text field in which the user enters the real portion of the
	 * function's 'd' coefficient.
	 */
	private JTextField dxField = new JTextField(3);

	/**
	 * The text field in which the user enters the number which, multiplied by
	 * i, is the imaginary portion of the function's 'd' coefficient.
	 */
	private JTextField dyField = new JTextField(3);

	// for serializable interface: do not use
	private static final long serialVersionUID = 0;

	private Checkbox polarCheckBox;

	/**
	 * Builds the dialog for a mobius input function and makes it visible on the
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
	public MobiusDialog(Julia f, int type, MobiusInputFunction fn) {
		super(f, "Create a Mobius Function", false);
		dialogType = type;
		function = fn;
		parentFrame = f;

		Complex[] coefficients;
		if (dialogType == GUIUtil.EDIT_DIALOG
				|| dialogType == GUIUtil.CLONE_DIALOG) {
			coefficients = function.getCoefficients();
		} else {
			coefficients = DEFAULT_COEFFICIENTS;
		}

		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setLayout(new GridLayout(0, 1));

		JPanel mPanel = new JPanel();
		mPanel.add(new JLabel("Enter the m value:"));
		if (dialogType == GUIUtil.EDIT_DIALOG
				|| dialogType == GUIUtil.CLONE_DIALOG)
			mField.setText("" + function.getM());
		else
			mField.setText("1");
		mField
				.addFocusListener(new TextFieldFocusListener(mField,
						parentFrame));
		mPanel.add(mField);
		this.add(mPanel);

		this.add(new JLabel("Please enter the function coefficients:"));
		JPanel functionPanel1 = new JPanel();
		// format all labels in HTML
		functionPanel1.add(new JLabel("<html>((</html>"));
		axField.setText(shortString(String.valueOf(coefficients[0].getReal())));
		axField.addFocusListener(new TextFieldFocusListener(axField,
				parentFrame));
		functionPanel1.add(axField);

		functionPanel1.add(new JLabel(", "));
		ayField.setText(shortString(String.valueOf(coefficients[0]
				.getImaginary())));
		ayField.addFocusListener(new TextFieldFocusListener(ayField,
				parentFrame));
		functionPanel1.add(ayField);

		functionPanel1.add(new JLabel("<html>) z + (</html>"));
		bxField.setText(shortString(String.valueOf(coefficients[1].getReal())));
		bxField.addFocusListener(new TextFieldFocusListener(bxField,
				parentFrame));
		functionPanel1.add(bxField);

		functionPanel1.add(new JLabel(", "));
		byField.setText(shortString(String.valueOf(coefficients[1]
				.getImaginary())));
		byField.addFocusListener(new TextFieldFocusListener(byField,
				parentFrame));
		functionPanel1.add(byField);

		functionPanel1.add(new JLabel("<html>))</html>"));
		this.add(functionPanel1);

		JPanel functionPanel2 = new JPanel();
		functionPanel2.add(new JLabel("/ (("));
		cxField.setText(shortString(String.valueOf(coefficients[2].getReal())));
		cxField.addFocusListener(new TextFieldFocusListener(cxField,
				parentFrame));
		functionPanel2.add(cxField);

		functionPanel2.add(new JLabel(", "));
		cyField.setText(shortString(String.valueOf(coefficients[2]
				.getImaginary())));
		cyField.addFocusListener(new TextFieldFocusListener(cyField,
				parentFrame));
		functionPanel2.add(cyField);

		functionPanel2.add(new JLabel("<html>) z + (</html>"));
		dxField.setText(shortString(String.valueOf(coefficients[3].getReal())));
		dxField.addFocusListener(new TextFieldFocusListener(dxField,
				parentFrame));
		functionPanel2.add(dxField);

		functionPanel2.add(new JLabel(", "));
		dyField.setText(shortString(String.valueOf(coefficients[3]
				.getImaginary())));
		dyField.addFocusListener(new TextFieldFocusListener(dyField,
				parentFrame));
		functionPanel2.add(dyField);
		functionPanel2.add(new JLabel("<html>))</html>"));
		this.add(functionPanel2);

		polarCheckBox = new Checkbox("Coefficient Values Use Polar Coordinates");
		polarCheckBox.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 10));
		this.add(polarCheckBox);

		JPanel buttonPanel = new JPanel();
		JButton finishButton = new JButton("Finish");
		finishButton.addActionListener(this);
		buttonPanel.add(finishButton);

		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent event) {
				setVisible(false);
				dispose();
			}
		});
		buttonPanel.add(cancelButton);
		this.add(buttonPanel);

		pack();
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
	public void actionPerformed(ActionEvent arg0) {
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
		double bx = 0;
		double by = 0;
		double cx = 0;
		double cy = 0;
		double dx = 0;
		double dy = 0;
		if (axField.getText().equals("") || ayField.getText().equals("")
				|| bxField.getText().equals("") || byField.getText().equals("")
				|| cxField.getText().equals("") || cyField.getText().equals("")
				|| dxField.getText().equals("") || dyField.getText().equals("")) {
			JuliaError.EMPTY_FIELD.showDialog(parentFrame);
			return;
		}
		String axString = GUIUtil.removeCommas(axField.getText());
		axString = GUIUtil.parsePI(axString);
		String ayString = GUIUtil.removeCommas(ayField.getText());
		ayString = GUIUtil.parsePI(ayString);
		String bxString = GUIUtil.removeCommas(bxField.getText());
		bxString = GUIUtil.parsePI(bxString);
		String byString = GUIUtil.removeCommas(byField.getText());
		byString = GUIUtil.parsePI(byString);
		String cxString = GUIUtil.removeCommas(cxField.getText());
		cxString = GUIUtil.parsePI(cxString);
		String cyString = GUIUtil.removeCommas(cyField.getText());
		cyString = GUIUtil.parsePI(cyString);
		String dxString = GUIUtil.removeCommas(dxField.getText());
		dxString = GUIUtil.parsePI(dxString);
		String dyString = GUIUtil.removeCommas(dyField.getText());
		dyString = GUIUtil.parsePI(dyString);
		try {
			ax = Double.parseDouble(axString);
			ay = Double.parseDouble(ayString);
			bx = Double.parseDouble(bxString);
			by = Double.parseDouble(byString);
			cx = Double.parseDouble(cxString);
			cy = Double.parseDouble(cyString);
			dx = Double.parseDouble(dxString);
			dy = Double.parseDouble(dyString);
		} catch (NumberFormatException e) {
			JuliaError.COEFFICIENT_FORMAT_ERROR.showDialog(parentFrame);
			return;
		}

		Complex a = new Complex(ax, ay);
		Complex b = new Complex(bx, by);
		Complex c = new Complex(cx, cy);
		Complex d = new Complex(dx, dy);

		if (polarCheckBox.getState()) {
			a = ComplexUtils.polar2Complex(ax, ay);
			b = ComplexUtils.polar2Complex(bx, by);
			c = ComplexUtils.polar2Complex(cx, cy);
			d = ComplexUtils.polar2Complex(dx, dy);
		}
		MobiusInputFunction newFunction;
		try {
			newFunction = new MobiusInputFunction(m, a, b, c, d);
		} catch (IllegalArgumentException e) {
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

	private String shortString(String s) {
		if (s.length() > 5)
			return s.substring(0, 5);
		else
			return s;
	}
}
