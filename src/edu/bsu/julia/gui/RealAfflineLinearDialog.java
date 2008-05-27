package edu.bsu.julia.gui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import edu.bsu.julia.ComplexNumber;
import edu.bsu.julia.Julia;
import edu.bsu.julia.RealAfflineLinearInputFunction;
import edu.bsu.julia.session.Session;
/**
 * 
 * <h3>Description</h3>
 * <p>Creates a dialog box for the function type: [a, b; c, d]z + [e; f].  
 * This dialog is used when creating a new affline linear function, editing an 
 * existing function, or cloneing an existing function.
 *
 */
public class RealAfflineLinearDialog extends JDialog implements ActionListener {
	/**
	 * The main frame over which this dialog sets.
	 */
	private Julia parentFrame;
	/**
	 * One of three types defined in a utility class, GUIUtil:  
	 * {@link GUIUtil#NEW_DIALOG}, 
	 * {@link GUIUtil#EDIT_DIALOG}, and 
	 * {@link GUIUtil#CLONE_DIALOG}. It indicates the use to 
	 * which this dialog is being put.
	 */
	private int dialogType;
	/**
	 * If this dialog is being used to edit or clone an existing function, 
	 * the function that is being modified.  If the dialog is being used 
	 * to create a new function, this field is <b>null</b>.
	 */
	private RealAfflineLinearInputFunction function;
	/**
	 * The text field in which the user enters the function's m value.
	 */
	private JTextField mField = new JTextField(5);
	/**
	 * The text field in which the user enters the function's 'a' 
	 * coefficient.
	 */
	private JTextField aField = new JTextField(3);
	/**
	 * The text field in which the user enters the function's 'b' 
	 * coefficient.
	 */
	private JTextField bField = new JTextField(3);
	/**
	 * The text field in which the user enters the function's 'c' 
	 * coefficient.
	 */
	private JTextField cField = new JTextField(3);
	/**
	 * The text field in which the user enters the function's 'd' 
	 * coefficient.
	 */
	private JTextField dField = new JTextField(3);
	/**
	 * The text field in which the user enters the function's 'e' 
	 * coefficient.
	 */
	private JTextField eField = new JTextField(3);
	/**
	 * The text field in which the user enters the function's 'f' 
	 * coefficient.
	 */
	private JTextField fField = new JTextField(3);
	//for serializable interface: do not use
	public static final long serialVersionUID = 0;
	
	/**
	 * Builds the dialog for a affline linear input function and makes it visible 
	 * on the screen.  'type' indicates whether the dialog will be used for 
	 * editing or cloneing an existing function or creating a new function.  
	 * If editing or cloneing an existing function, that function is passed 
	 * to the constructor as 'fn'.  When creating a new function, 'fn' can be 
	 * <b>null</b>.
	 * @param f reference to the main program and its frame
	 * @param type constant indicating whether the dialog will be used to 
	 * edit, clone, or create a new function
	 * @param fn the function to be edited or cloned
	 */
	public RealAfflineLinearDialog(Julia f, int type, 
			RealAfflineLinearInputFunction fn) {
		super(f, "Create a Real Affline Linear Function", false);
		dialogType = type;
		function = fn;
		parentFrame = f;
		
		ComplexNumber[] coefficients = new ComplexNumber[6];
		if (dialogType == GUIUtil.EDIT_DIALOG||dialogType==GUIUtil.CLONE_DIALOG) 
			coefficients = function.getCoefficients();
		
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setLayout(new FlowLayout());
		
		add(new JLabel("Enter the m value:  "));
		if (dialogType == GUIUtil.EDIT_DIALOG||dialogType==GUIUtil.CLONE_DIALOG) 
			mField.setText(""+function.getM());
		else mField.setText("1");
		mField.addFocusListener(new TextFieldFocusListener(mField, parentFrame));
		add(mField);
		
		add(new JLabel("Please enter the function coefficients:        "));
		JPanel functionPanel = new JPanel();
		//format all labels in HTML
		functionPanel.add(new JLabel("<html>[a=</html>"));
		if (dialogType == GUIUtil.EDIT_DIALOG||dialogType==GUIUtil.CLONE_DIALOG) 
			aField.setText("" + coefficients[0].getX());
		else aField.setText("1");
		aField.addFocusListener(new TextFieldFocusListener(aField, parentFrame));
		functionPanel.add(aField);
		functionPanel.add(new JLabel("<html> ,b=</html>"));
		if (dialogType == GUIUtil.EDIT_DIALOG||dialogType==GUIUtil.CLONE_DIALOG) 
			bField.setText("" + coefficients[1].getX());
		else bField.setText("0");
		bField.addFocusListener(new TextFieldFocusListener(bField, parentFrame));
		functionPanel.add(bField);
		functionPanel.add(new JLabel("<html> ;c=</html>"));
		if (dialogType == GUIUtil.EDIT_DIALOG||dialogType==GUIUtil.CLONE_DIALOG) 
			cField.setText("" + coefficients[2].getX());
		else cField.setText("0");
		cField.addFocusListener(new TextFieldFocusListener(cField, parentFrame));
		functionPanel.add(cField);
		functionPanel.add(new JLabel("<html> ,d=</html>"));
		if (dialogType == GUIUtil.EDIT_DIALOG||dialogType==GUIUtil.CLONE_DIALOG) 
			dField.setText("" + coefficients[3].getX());
		else dField.setText("1");
		dField.addFocusListener(new TextFieldFocusListener(dField, parentFrame));
		functionPanel.add(dField);
		functionPanel.add(new JLabel("<html>]z + [e=</html>"));
		if (dialogType == GUIUtil.EDIT_DIALOG||dialogType==GUIUtil.CLONE_DIALOG) 
			eField.setText("" + coefficients[4].getX());
		else eField.setText("0");
		eField.addFocusListener(new TextFieldFocusListener(eField, parentFrame));
		functionPanel.add(eField);
		functionPanel.add(new JLabel("<html> ;f=</html>"));
		if (dialogType == GUIUtil.EDIT_DIALOG||dialogType==GUIUtil.CLONE_DIALOG) 
			fField.setText("" + coefficients[5].getX());
		else fField.setText("0");
		fField.addFocusListener(new TextFieldFocusListener(fField, parentFrame));
		functionPanel.add(fField);
		functionPanel.add(new JLabel("<html>]</html>"));
		add(functionPanel);
		
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
		
		setSize(425, 155);
		setLocationRelativeTo(parentFrame);
		setVisible(true);	
	}
	
	/**
	 * Triggered when the user hits "Finish" on the dialog box.  Builds the 
	 * function and then either adds it to the {@link edu.bsu.julia.Session}'s 
	 * input function list or replaces the old function on the list (if it is 
	 * being used to edit a function).  Input functions are immutable objects.
	 */
	public void actionPerformed(ActionEvent arg0) {
		if(mField.getText().equals("")) {
			new JuliaError(JuliaError.EMPTY_FIELD, parentFrame);
			return;
		}
		int m = 0;
		try {
			m = Integer.parseInt(GUIUtil.removeCommas(mField.getText()));
		}catch(NumberFormatException except){
			new JuliaError(JuliaError.M_INTEGER_ERROR, parentFrame);
			return;
		}
		double a = 0;
		double b = 0;
		double c = 0;
		double d = 0;
		double e = 0;
		double f = 0;
		if(aField.getText().equals("") || bField.getText().equals("") || 
				cField.getText().equals("") || dField.getText().equals("") || 
				eField.getText().equals("") || fField.getText().equals("")) {
			new JuliaError(JuliaError.EMPTY_FIELD, parentFrame);
			return;
		}
		try {
			a = Double.parseDouble(GUIUtil.removeCommas(aField.getText()));
			b = Double.parseDouble(GUIUtil.removeCommas(bField.getText()));
			c = Double.parseDouble(GUIUtil.removeCommas(cField.getText()));
			d = Double.parseDouble(GUIUtil.removeCommas(dField.getText()));
			e = Double.parseDouble(GUIUtil.removeCommas(eField.getText()));
			f = Double.parseDouble(GUIUtil.removeCommas(fField.getText()));
		}catch(NumberFormatException except) {
			new JuliaError(JuliaError.COEFFICIENT_FORMAT_ERROR, parentFrame);
			return;
		}
		
		ComplexNumber aComp = new ComplexNumber(a, 0);
		ComplexNumber bComp = new ComplexNumber(b, 0);
		ComplexNumber cComp = new ComplexNumber(c, 0);
		ComplexNumber dComp = new ComplexNumber(d, 0);
		ComplexNumber eComp = new ComplexNumber(e, 0);
		ComplexNumber fComp = new ComplexNumber(f, 0);
		RealAfflineLinearInputFunction newFunction;
		try {
			newFunction = new RealAfflineLinearInputFunction(m,aComp,bComp,
					cComp,dComp,eComp,fComp);
		}catch(IllegalArgumentException except) {
			new JuliaError(JuliaError.M_NEG_ERROR, parentFrame);
			return;
		}

		Session s = parentFrame.getCurrentSession();
		if (dialogType == GUIUtil.NEW_DIALOG||dialogType==GUIUtil.CLONE_DIALOG) 
			s.addInputFunction(newFunction);
		else s.replaceInputFunction(function, newFunction);
		
		setVisible(false);
		dispose();
	}

}
