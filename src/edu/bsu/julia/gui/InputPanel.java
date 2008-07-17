package edu.bsu.julia.gui;

import java.awt.AWTKeyStroke;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.ListSelectionModel;

import edu.bsu.julia.ComplexNumber;
import edu.bsu.julia.Julia;
import edu.bsu.julia.gui.actions.CopyFunctionAction;
import edu.bsu.julia.gui.actions.CreateCompositeIndAction;
import edu.bsu.julia.gui.actions.DeleteInputAction;
import edu.bsu.julia.gui.actions.DeleteSelectedAction;
import edu.bsu.julia.gui.actions.EditFunctionAction;
import edu.bsu.julia.gui.actions.PostCriticalAction;
import edu.bsu.julia.input.InputFunction;
import edu.bsu.julia.session.Session;

public class InputPanel extends JPanel implements PropertyChangeListener {

	private Julia parentFrame;
	private Session s;
	private JLabel seedLabel;
	private JList inputList;
	private DefaultListModel listModel = new DefaultListModel();
	private ButtonGroup typeGroup;
	private ButtonGroup methodGroup;
	private JToggleButton attrButton;
	private JToggleButton juliaButton;
	private JToggleButton randomButton;
	private JToggleButton fullButton;
	private JButton processButton;
	private JButton postCriticalButton;
	private JButton createCompButton;
	private JButton createIndButton;
	private JTextField iterationsField;
	private JTextField skipsField;
	private JTextField seedField1;
	private JTextField seedField2;

	private final MouseListener mouseListener = new MouseAdapter() {
		public void mousePressed(MouseEvent e) {
			checkPopup(e);
		}

		public void mouseClicked(MouseEvent e) {
			checkPopup(e);
		}

		public void mouseReleased(MouseEvent e) {
			checkPopup(e);
		}

		private void checkPopup(MouseEvent e) {
			if (e.isPopupTrigger()) {
				final int index = inputList.locationToIndex(e.getPoint());
				JPopupMenu popup = new JPopupMenu();
				popup
						.add(new EditFunctionAction(parentFrame, listModel,
								index));
				popup.add(new DeleteInputAction(parentFrame, listModel, index));
				popup
						.add(new CopyFunctionAction(parentFrame, listModel,
								index));
				popup.addSeparator();
				popup
						.add(new DeleteSelectedAction(parentFrame,
								Julia.INPUTTYPE));
				popup.show(inputList, e.getX(), e.getY());
			}
		}
	};

	// for serializable interface: do not use
	public static final long serialVersionUID = 0;

	public InputPanel(Julia f) {
		parentFrame = f;
		s = parentFrame.getCurrentSession();
		s.addListener(this);
		parentFrame.addListener(this);

		typeGroup = new ButtonGroup();
		methodGroup = new ButtonGroup();

		GridBagLayout layout = new GridBagLayout();
		setLayout(layout);
		GridBagConstraints constraints = new GridBagConstraints();

		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.weightx = 1000;
		constraints.weighty = 1000;
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;

		Set<AWTKeyStroke> upKeys = new HashSet<AWTKeyStroke>(1);
		upKeys.add(AWTKeyStroke.getAWTKeyStroke(KeyEvent.VK_UP, 0));
		setFocusTraversalKeys(KeyboardFocusManager.UP_CYCLE_TRAVERSAL_KEYS,
				upKeys);

		JPanel panel0 = new JPanel(new GridLayout(1, 2));
		JPanel panel1 = new JPanel(new GridLayout(1, 2));
		Session s = parentFrame.getCurrentSession();
		panel0.add(new JLabel("Min Points to Plot:  "));
		iterationsField = new JTextField(8);
		iterationsField.setName("iterationsField");
		iterationsField.setText("" + s.getIterations());
		TextFieldFocusListener iterationListener = new TextFieldFocusListener(
				iterationsField, parentFrame);
		iterationsField.addFocusListener(iterationListener);
		panel0.add(iterationsField);
		panel1.add(new JLabel("Number of Skips:  "));
		skipsField = new JTextField(6);
		skipsField.setName("skipsField");
		skipsField.setText("" + s.getSkips());
		TextFieldFocusListener skipListener = new TextFieldFocusListener(
				skipsField, parentFrame);
		skipsField.addFocusListener(skipListener);

		panel1.add(skipsField);

		JPanel panel2 = new JPanel(new GridLayout(1, 3));
		seedLabel = new JLabel("Seed Value:");
		panel2.add(seedLabel);
		seedField1 = new JTextField(6);
		seedField1.setName("seedField1");
		String show = String.valueOf(s.getSeedValue().getX());
		String showShort = show;
		if (show.length() > 10)
			showShort = show.substring(0, 10);
		seedField1.setText(showShort);
		TextFieldFocusListener seed1Listener = new TextFieldFocusListener(
				seedField1, parentFrame);
		seedField1.addFocusListener(seed1Listener);
		panel2.add(seedField1);

		seedField2 = new JTextField(6);
		seedField2.setName("seedField2");
		show = String.valueOf(s.getSeedValue().getY());
		showShort = show;
		if (show.length() > 10)
			showShort = show.substring(0, 10);
		seedField2.setText(showShort);
		TextFieldFocusListener seed2Listener = new TextFieldFocusListener(
				seedField2, parentFrame);
		seedField2.addFocusListener(seed2Listener);
		panel2.add(seedField2);

		JPanel panel3 = new JPanel(new GridLayout(3, 1));
		layout.setConstraints(panel0, constraints);
		panel3.add(panel0);
		layout.setConstraints(panel1, constraints);
		panel3.add(panel1);
		layout.setConstraints(panel2, constraints);
		panel3.add(panel2);
		add(panel3);

		constraints.gridy = 1;
		constraints.weightx = 1000;
		constraints.weighty = 1000;
		constraints.fill = GridBagConstraints.BOTH;

		List<InputFunction> fns = s.getInputFunctions();
		for (int i = 0; i < fns.size(); i++)
			listModel.addElement(fns.get(i));

		inputList = new JList(listModel);
		inputList
				.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		// inputList.setFocusable(false);
		inputList.setSelectionInterval(0, listModel.size() - 1);
		inputList.setBackground(getBackground());
		inputList
				.setBorder(BorderFactory.createTitledBorder("Input Functions"));
		inputList.setCellRenderer(new InputListCellRenderer());
		inputList.setName("The current input functions.  Ctrl + click "
				+ "selects multiple functions.");

		if (!listModel.isEmpty()) {
			inputList.addMouseListener(mouseListener);
		}
		inputList.addMouseListener(parentFrame.getStatusBar());

		JScrollPane scroller = new JScrollPane(inputList);
		layout.setConstraints(scroller, constraints);
		add(scroller);

		constraints.gridy = 2;
		constraints.weightx = 1;
		constraints.weighty = 0;
		constraints.fill = GridBagConstraints.HORIZONTAL;

		JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 5, 5));
		attrButton = new JToggleButton("Attractor");
		attrButton.setActionCommand("attr");
		attrButton.setName("Attractor set for selected functions.");
		attrButton.addMouseListener(parentFrame.getStatusBar());
		typeGroup.add(attrButton);
		buttonPanel.add(attrButton);
		juliaButton = new JToggleButton("Julia", true);
		juliaButton.setActionCommand("julia");
		juliaButton.setName("Julia set for selected functions.");
		juliaButton.addMouseListener(parentFrame.getStatusBar());
		typeGroup.add(juliaButton);
		buttonPanel.add(juliaButton);
		buttonPanel.setBorder(BorderFactory.createTitledBorder("Set Type"));
		layout.setConstraints(buttonPanel, constraints);
		add(buttonPanel);
		constraints.gridy = 3;
		JPanel buttonPanel2 = new JPanel(new GridLayout(1, 2, 5, 5));
		fullButton = new JToggleButton("Full");
		fullButton.setActionCommand("full");
		fullButton.setName("Process functions using the full method.");
		fullButton.addMouseListener(parentFrame.getStatusBar());
		methodGroup.add(fullButton);
		buttonPanel2.add(fullButton);
		randomButton = new JToggleButton("Random", true);
		randomButton.setActionCommand("random");
		randomButton.setName("Process functions using the random method.");
		randomButton.addMouseListener(parentFrame.getStatusBar());
		methodGroup.add(randomButton);
		buttonPanel2.add(randomButton);
		buttonPanel2
				.setBorder(BorderFactory.createTitledBorder("Process Type"));
		layout.setConstraints(buttonPanel2, constraints);
		add(buttonPanel2);

		constraints.gridy = 4;

		JPanel processButtonPanel = createProcessButtonPanel();
		layout.setConstraints(processButtonPanel, constraints);
		add(processButtonPanel);

		setMinimumSize(new Dimension(220, 600));
	}

	public InputFunction[] getSelectedFunctions() {
		Object[] objects = inputList.getSelectedValues();
		InputFunction[] functions = new InputFunction[objects.length];
		for (int i = 0; i < objects.length; i++) {
			functions[i] = (InputFunction) objects[i];
		}
		return functions;
	}

	public ButtonGroup getTypeGroup() {
		return typeGroup;
	}

	public ButtonGroup getMethodGroup() {
		return methodGroup;
	}

	public void propertyChange(PropertyChangeEvent e) {
		String name = e.getPropertyName();
		if (name.equals("iterations"))
			iterationsField.setText("" + e.getNewValue());
		else if (name.equals("skips"))
			skipsField.setText("" + e.getNewValue());
		else if (name.equals("seed")) {
			String show = String.valueOf(((ComplexNumber) e.getNewValue())
					.getX());
			String showShort = show;
			if (show.length() > 10)
				showShort = show.substring(0, 10);
			seedField1.setText(showShort);
			show = String.valueOf(((ComplexNumber) e.getNewValue()).getY());
			showShort = show;
			if (show.length() > 10)
				showShort = show.substring(0, 10);
			seedField2.setText(showShort);
		} else if (name.equals("addInputFunction")) {
			if (listModel.isEmpty()) {
				processButton.setEnabled(true);
				postCriticalButton.setEnabled(true);
				createCompButton.setEnabled(true);
				createIndButton.setEnabled(true);
				inputList.addMouseListener(mouseListener);
			}
			listModel.addElement(e.getNewValue());
			inputList.addSelectionInterval(listModel.size() - 1, listModel
					.size() - 1);
		} else if (name.equals("replaceInputFunction"))
			listModel.set(listModel.indexOf(e.getOldValue()), e.getNewValue());
		else if (name.equals("session")) {
			s = (Session) e.getNewValue();
			iterationsField.setText("" + s.getIterations());
			skipsField.setText("" + s.getSkips());
			seedField1.setText("" + ((ComplexNumber) s.getSeedValue()).getX());
			seedField2.setText("" + ((ComplexNumber) s.getSeedValue()).getY());

			// turn things off and clear the listModel
			if (!listModel.isEmpty()) {
				processButton.setEnabled(false);
				postCriticalButton.setEnabled(false);
				createCompButton.setEnabled(false);
				createIndButton.setEnabled(false);
				inputList.removeMouseListener(mouseListener);
			}
			listModel.clear();

			// populate the listModel and turn things on
			for (InputFunction f : s.getInputFunctions())
				listModel.addElement(f);
			if (!listModel.isEmpty()) {
				processButton.setEnabled(true);
				postCriticalButton.setEnabled(true);
				createCompButton.setEnabled(true);
				createIndButton.setEnabled(true);
				inputList.addMouseListener(mouseListener);
				inputList.addSelectionInterval(0, listModel.size() - 1);
			}

			inputList.setCellRenderer(new InputListCellRenderer());
			s.addListener(this);
		} else if (name.equals("deleteInputFunction")) {
			Integer i = (Integer) e.getNewValue();
			int index = Integer.valueOf(i);
			listModel.remove(index);
			if (listModel.isEmpty()) {
				processButton.setEnabled(false);
				postCriticalButton.setEnabled(false);
				createCompButton.setEnabled(false);
				createIndButton.setEnabled(false);
				inputList.removeMouseListener(mouseListener);
			}
			inputList.setCellRenderer(new InputListCellRenderer());
			inputList.repaint();
		}
	}

	public JPanel createProcessButtonPanel() {
		JPanel panel = new JPanel(new GridLayout(3, 1, 5, 5));
		postCriticalButton = new JButton(new PostCriticalAction(parentFrame));
		// postCriticalButton.setFocusable(false);
		if (listModel.isEmpty())
			postCriticalButton.setEnabled(false);
		postCriticalButton.addMouseListener(parentFrame.getStatusBar());
		panel.add(postCriticalButton);

		JPanel twoProcessPanel = new JPanel(new GridLayout(1, 2, 5, 5));
		createCompButton = new JButton(new CreateCompositeIndAction(
				parentFrame, getTypeGroup(), getMethodGroup(),
				CreateCompositeIndAction.Mode.COMPOSITE));
		// createCompButton.setFocusable(false);
		if (listModel.isEmpty())
			createCompButton.setEnabled(false);
		createCompButton.addMouseListener(parentFrame.getStatusBar());
		twoProcessPanel.add(createCompButton);
		createIndButton = new JButton(new CreateCompositeIndAction(parentFrame,
				getTypeGroup(), getMethodGroup(),
				CreateCompositeIndAction.Mode.INDIVIDUAL));
		// createIndButton.setFocusable(false);
		if (listModel.isEmpty())
			createIndButton.setEnabled(false);
		createIndButton.addMouseListener(parentFrame.getStatusBar());
		twoProcessPanel.add(createIndButton);
		panel.add(twoProcessPanel);

		processButton = new JButton(new CreateCompositeIndAction(parentFrame,
				getTypeGroup(), getMethodGroup(),
				CreateCompositeIndAction.Mode.BOTH));
		// processButton.setFocusable(false);
		if (listModel.isEmpty())
			processButton.setEnabled(false);
		processButton.addMouseListener(parentFrame.getStatusBar());
		panel.add(processButton);
		panel.setBorder(BorderFactory.createTitledBorder("Create Set"));

		return panel;
	}

	public boolean isFocusCycleRoot() {
		return true;
	}

}
