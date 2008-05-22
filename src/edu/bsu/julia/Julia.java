package edu.bsu.julia;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JSplitPane;

import edu.bsu.julia.gui.GLListener;
import edu.bsu.julia.gui.GraphScrollPane;
import edu.bsu.julia.gui.GraphTabbedPane;
import edu.bsu.julia.gui.InputPanel;
import edu.bsu.julia.gui.MainMenu;
import edu.bsu.julia.gui.MainToolBar;
import edu.bsu.julia.gui.OutputPanel;
import edu.bsu.julia.gui.SaveSessionDialog;
import edu.bsu.julia.gui.StatusBar;

public class Julia extends JFrame {

	public static final int INPUTTYPE = 0;
	public static final int OUTPUTTYPE = 1;
	private Session currentSession;
	private InputPanel inputPanel;
	private OutputPanel outputPanel;
	private StatusBar sBar = new StatusBar();
	private GraphTabbedPane tabbedPane;
	private PropertyChangeSupport support = new PropertyChangeSupport(this);
	private int dotSize;
	private boolean axisTrigger;
	private boolean grilTrigger;
	private boolean polarTrigger;
	private String filePath = "";
	// for serializable interface: do not use
	public static final long serialVersionUID = 0;

	public Julia() {
		super("Julia");
		currentSession = new Session();
		dotSize = 1;
		axisTrigger = true;
		grilTrigger = false;
		inputPanel = new InputPanel(this);
		outputPanel = new OutputPanel(this);
		createGUI();
		setFocusable(true);
		ComplexNumber a = new ComplexNumber(2, 0);
		ComplexNumber b = new ComplexNumber(0, 0);
		ComplexNumber c = new ComplexNumber(-1, 0);
		ComplexNumber d = new ComplexNumber(-.5, -0.866);
		LinearInputFunction linear1 = new LinearInputFunction(1, a, b);
		currentSession.addInputFunction(linear1);
		LinearInputFunction linear2 = new LinearInputFunction(1, a, c);
		currentSession.addInputFunction(linear2);
		LinearInputFunction linear3 = new LinearInputFunction(1, a, d);
		currentSession.addInputFunction(linear3);
		currentSession.markUnmodified();
	}

	public static void main(String[] args) {
		final Julia application = new Julia();
		application.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	}

	public Session getCurrentSession() {
		return currentSession;
	}

	public void setCurrentSession(Session s) {
		currentSession = s;
		support.firePropertyChange("session", null, currentSession);
	}

	public InputPanel getInputPanel() {
		return inputPanel;
	}

	public JList getOutputFunctionList() {
		return outputPanel.getOutputList();
	}

	public void addListener(PropertyChangeListener list) {
		support.addPropertyChangeListener(list);
	}

	public StatusBar getStatusBar() {
		return sBar;
	}

	public GraphTabbedPane getTabbedPane() {
		return tabbedPane;
	}

	public int getDotSize() {
		return dotSize;
	}

	public void setDotSize(int v) {
		dotSize = v;
		support.firePropertyChange("dotSize", null, dotSize);
	}

	public boolean getAxisTrigger() {
		return axisTrigger;
	}

	public void setAxisTrigger(boolean a) {
		axisTrigger = a;
		support.firePropertyChange("axisTrigger", null, axisTrigger);
	}

	public boolean getGrilTrigger() {
		return grilTrigger;
	}

	public void setGrilTrigger(boolean a) {
		grilTrigger = a;
		support.firePropertyChange("grilTrigger", null, grilTrigger);
	}

	public boolean getPolarTrigger() {
		return polarTrigger;
	}

	public void setPolarTrigger(boolean a) {
		polarTrigger = a;
		support.firePropertyChange("polarTrigger", null, polarTrigger);
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String path) {
		filePath = path;
	}

	public void createGUI() {
		tabbedPane = new GraphTabbedPane(this);
		setJMenuBar(new MainMenu(this));
		add(new MainToolBar(this), BorderLayout.NORTH);
		JSplitPane rightSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				tabbedPane, outputPanel);
		rightSplitPane.setOneTouchExpandable(true);
		rightSplitPane.setResizeWeight(1.0);
		JSplitPane leftSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				inputPanel, rightSplitPane);
		leftSplitPane.setOneTouchExpandable(true);
		add(leftSplitPane, BorderLayout.CENTER);
		add(sBar, BorderLayout.SOUTH);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent event) {
				loseFocus();

				// check to see if the current session should be saved
				if (currentSession.isModified()) {
					SaveSessionDialog saveDialog = new SaveSessionDialog(
							Julia.this);
					if (saveDialog.showSaveDialog() == SaveSessionDialog.CANCELED)
						return;
				}

				System.exit(0);
			}
		});

		setSize(800, 650);
		setVisible(true);

		GraphScrollPane aPane = tabbedPane.getActivePane();
		GLListener glList = aPane.getGLListener();
		glList.setPaneHeight(aPane.getHeight());
		glList.setPaneWidth(aPane.getWidth());
		glList.resetZoom();
	}

	public void loadSession(File f) {
		try {
			BufferedReader input = new BufferedReader(new FileReader(f));

			int points = Integer.parseInt(input.readLine());
			int skips = Integer.parseInt(input.readLine());
			double x = Double.parseDouble(input.readLine());
			double y = Double.parseDouble(input.readLine());
			ComplexNumber seed = new ComplexNumber(x, y);
			setCurrentSession(new Session(points, skips, seed));

			int size = Integer.parseInt(input.readLine());
			int m;
			for (int i = 0; i < size; i++) {
				String type = input.readLine();
				if (type.equals("linear")) {
					m = Integer.parseInt(input.readLine());
					ComplexNumber[] var = new ComplexNumber[2];
					for (int j = 0; j < 2; j++) {
						x = Double.parseDouble(input.readLine());
						y = Double.parseDouble(input.readLine());
						var[j] = new ComplexNumber(x, y);
					}
					currentSession.addInputFunction(new LinearInputFunction(m,
							var[0], var[1]));
				} else if (type.equals("cubic")) {
					m = Integer.parseInt(input.readLine());
					ComplexNumber[] var = new ComplexNumber[2];
					for (int j = 0; j < 2; j++) {
						x = Double.parseDouble(input.readLine());
						y = Double.parseDouble(input.readLine());
						var[j] = new ComplexNumber(x, y);
					}
					currentSession.addInputFunction(new CubicInputFunction(m,
							var[0], var[1]));
				} else if (type.equals("matrix")) {
					m = Integer.parseInt(input.readLine());
					ComplexNumber[] var = new ComplexNumber[6];
					for (int j = 0; j < 6; j++) {
						x = Double.parseDouble(input.readLine());
						y = Double.parseDouble(input.readLine());
						var[j] = new ComplexNumber(x, y);
					}
					currentSession
							.addInputFunction(new RealAfflineLinearInputFunction(
									m, var[0], var[1], var[2], var[3], var[4],
									var[5]));
				} else if (type.equals("mobius")) {
					m = Integer.parseInt(input.readLine());
					ComplexNumber[] var = new ComplexNumber[4];
					for (int j = 0; j < 4; j++) {
						x = Double.parseDouble(input.readLine());
						y = Double.parseDouble(input.readLine());
						var[j] = new ComplexNumber(x, y);
					}
					currentSession.addInputFunction(new MobiusInputFunction(m,
							var[0], var[1], var[2], var[3]));
				} else if (type.equals("quad")) {
					m = Integer.parseInt(input.readLine());
					ComplexNumber[] var = new ComplexNumber[3];
					for (int j = 0; j < 3; j++) {
						x = Double.parseDouble(input.readLine());
						y = Double.parseDouble(input.readLine());
						var[j] = new ComplexNumber(x, y);
					}
					currentSession.addInputFunction(new QuadraticInputFunction(
							m, var[0], var[1], var[2]));
				}
			}
		} catch (IOException ioe) {
			JOptionPane.showMessageDialog(this, "Error Reading File",
					"Error Reading File", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * method to force whatever component has focus to think it has lost the
	 * focus
	 */
	public void loseFocus() {
		Component focusOwner = getFocusOwner();
		FocusListener[] listeners = focusOwner.getFocusListeners();
		FocusEvent focusLost = new FocusEvent(focusOwner, FocusEvent.FOCUS_LOST);
		for (FocusListener l : listeners) {
			l.focusLost(focusLost);
		}
	}

}
