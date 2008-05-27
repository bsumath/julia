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

import edu.bsu.julia.ComplexNumber;
import edu.bsu.julia.Julia;
import edu.bsu.julia.QuadraticInputFunction;
import edu.bsu.julia.session.Session;
/**
 * 
 * <h3>Description</h3>
 * <p>Creates a dialog box for the function type: az^2 + bz + c.  
 * This dialog is used when creating a new quadratic function, editing an 
 * existing function, or cloneing an existing function.
 *
 */
public class QuadraticDialog extends JDialog implements ActionListener {
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
	private QuadraticInputFunction function;
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
	 * The text field in which the user enters the number which, multiplied 
	 * by i, is the imaginary portion of the function's 'a' coefficient.
	 */
	private JTextField ayField = new JTextField(3);
	/**
	 * The text field in which the user enters the real portion of the 
	 * function's 'b' coefficient.
	 */
	private JTextField bxField = new JTextField(3);
	/**
	 * The text field in which the user enters the number which, multiplied 
	 * by i, is the imaginary portion of the function's 'b' coefficient.
	 */
	private JTextField byField = new JTextField(3);
	/**
	 * The text field in which the user enters the real portion of the 
	 * function's 'c' coefficient.
	 */
	private JTextField cxField = new JTextField(3);
	/**
	 * The text field in which the user enters the number which, multiplied 
	 * by i, is the imaginary portion of the function's 'c' coefficient.
	 */
	private JTextField cyField = new JTextField(3);
	//for serializable interface: do not use
	public static final long serialVersionUID = 0;
	private Checkbox polarCheckBox;
	
	/**
	 * Builds the dialog for a quadratic input function and makes it visible 
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
	public QuadraticDialog(Julia f, int type, QuadraticInputFunction fn) {
		super(f, "Create a Quadratic Function", false);
		dialogType = type;
		function = fn;
		parentFrame = f;
		
		ComplexNumber[] coefficients = new ComplexNumber[3];
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
		
		add(new JLabel("Please enter the function coefficients:    "));
		JPanel functionPanel = new JPanel();
		//format all labels in HTML
		functionPanel.add(new JLabel("<html>(</html>"));
		if (dialogType == GUIUtil.EDIT_DIALOG||dialogType==GUIUtil.CLONE_DIALOG) 
			{
			String show = String.valueOf(coefficients[0].getX());
			String showShort = show;
			if (show.length()>5) showShort = show.substring(0, 5);
			axField.setText(showShort);
			}
		else axField.setText("1");
		axField.addFocusListener(new TextFieldFocusListener(axField, parentFrame));
		functionPanel.add(axField);
		functionPanel.add(new JLabel(", "));
		if (dialogType == GUIUtil.EDIT_DIALOG||dialogType==GUIUtil.CLONE_DIALOG) 
			{
			String show = String.valueOf(coefficients[0].getY());
			String showShort = show;
			if (show.length()>5) showShort = show.substring(0, 5);
			ayField.setText(showShort);
			}
		else ayField.setText("0");
		ayField.addFocusListener(new TextFieldFocusListener(ayField, parentFrame));
		functionPanel.add(ayField);
		functionPanel.add(new JLabel("<html>) z<sup>2</sup> + (</html>"));
		if (dialogType == GUIUtil.EDIT_DIALOG||dialogType==GUIUtil.CLONE_DIALOG) 
			{
			String show = String.valueOf(coefficients[1].getX());
			String showShort = show;
			if (show.length()>5) showShort = show.substring(0, 5);
			bxField.setText(showShort);
			}
		else bxField.setText("0");
		bxField.addFocusListener(new TextFieldFocusListener(bxField, parentFrame));
		functionPanel.add(bxField);
		functionPanel.add(new JLabel(", "));
		if (dialogType == GUIUtil.EDIT_DIALOG||dialogType==GUIUtil.CLONE_DIALOG) 
			{
			String show = String.valueOf(coefficients[1].getY());
			String showShort = show;
			if (show.length()>5) showShort = show.substring(0, 5);
			byField.setText(showShort);
			}
		else byField.setText("0");
		byField.addFocusListener(new TextFieldFocusListener(byField, parentFrame));
		functionPanel.add(byField);
		functionPanel.add(new JLabel("<html>) z + (</html>"));
		if (dialogType == GUIUtil.EDIT_DIALOG||dialogType==GUIUtil.CLONE_DIALOG) 
			{
			String show = String.valueOf(coefficients[2].getX());
			String showShort = show;
			if (show.length()>5) showShort = show.substring(0, 5);
			cxField.setText(showShort);
			}
		else cxField.setText("0");
		cxField.addFocusListener(new TextFieldFocusListener(cxField, parentFrame));
		functionPanel.add(cxField);
		functionPanel.add(new JLabel(", "));
		if (dialogType == GUIUtil.EDIT_DIALOG||dialogType==GUIUtil.CLONE_DIALOG) 
			{
			String show = String.valueOf(coefficients[2].getY());
			String showShort = show;
			if (show.length()>5) showShort = show.substring(0, 5);
			cyField.setText(showShort);
			}
		else cyField.setText("0");
		cyField.addFocusListener(new TextFieldFocusListener(cyField, parentFrame));
		functionPanel.add(cyField);
		functionPanel.add(new JLabel("<html>)</html>"));
		add(functionPanel);
		
		JLabel polarCheckboxLabel = new JLabel("                   Coefficient Value Use Polar Coordinates",JLabel.LEFT);
		add(polarCheckboxLabel);
		polarCheckBox = new Checkbox("               ",false);
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
		
		setSize(420, 166);
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
		}catch(NumberFormatException e){
			new JuliaError(JuliaError.M_INTEGER_ERROR, parentFrame);
			return;
		}
		double ax = 0;
		double ay = 0;
		double bx = 0;
		double by = 0;
		double cx = 0;
		double cy = 0;
		if(axField.getText().equals("") || ayField.getText().equals("") || 
				bxField.getText().equals("") || byField.getText().equals("")
				|| cxField.getText().equals("") || cyField.getText().equals("")) {
			new JuliaError(JuliaError.EMPTY_FIELD, parentFrame);
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
		try {
			ax = Double.parseDouble(axString);
			ay = Double.parseDouble(ayString);
			bx = Double.parseDouble(bxString);
			by = Double.parseDouble(byString);
			cx = Double.parseDouble(cxString);
			cy = Double.parseDouble(cyString);
		}catch(NumberFormatException e) {
			new JuliaError(JuliaError.COEFFICIENT_FORMAT_ERROR, parentFrame);
			return;
		}
		
		ComplexNumber a = new ComplexNumber(ax, ay);
		ComplexNumber b = new ComplexNumber(bx, by);
		ComplexNumber c = new ComplexNumber(cx, cy);
		
		if(polarCheckBox.getState()){
			a = a.polarConvertion(a);
			b = b.polarConvertion(b);
			c = c.polarConvertion(c);
		}
		
		QuadraticInputFunction newFunction;
		try {
			newFunction = new QuadraticInputFunction(m,a,b,c);
		}catch(IllegalArgumentException e) {
			if(e.getMessage().equals("a zero"))
				new JuliaError(JuliaError.QUADRATIC_ILLEGAL_ARGUMENT, parentFrame);
			else new JuliaError(JuliaError.M_NEG_ERROR, parentFrame);
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
