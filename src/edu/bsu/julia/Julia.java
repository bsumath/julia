package edu.bsu.julia;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;

import edu.bsu.julia.gui.GLListener;
import edu.bsu.julia.gui.GraphScrollPane;
import edu.bsu.julia.gui.GraphTabbedPane;
import edu.bsu.julia.gui.InputPanel;
import edu.bsu.julia.gui.MainMenu;
import edu.bsu.julia.gui.MainToolBar;
import edu.bsu.julia.gui.OutputPanel;
import edu.bsu.julia.gui.SaveSessionDialog;
import edu.bsu.julia.gui.StatusBar;
import edu.bsu.julia.session.EmptySessionImporter;
import edu.bsu.julia.session.Session;
import edu.bsu.julia.session.Session.InvalidSessionParametersException;

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
	private static final String TITLE = "Julia";

	private static final List<Long> timestamps = new ArrayList<Long>();

	public Julia() {
		super(TITLE);

		try {
			currentSession = new Session(this, new EmptySessionImporter());
		} catch (InvalidSessionParametersException e) {
			/* this shouldn't happen */
			e.printStackTrace();
		}

		dotSize = 1;
		axisTrigger = true;
		grilTrigger = false;
		inputPanel = new InputPanel(this);
		outputPanel = new OutputPanel(this);
	}

	/**
	 * method to get the next timestamp. makes sure that all timestamps are
	 * unique.
	 * 
	 * @return the next timestamp
	 */
	public static long nextTimestamp() {
		Long timestamp = System.currentTimeMillis();
		while (timestamps.contains(timestamp)) {
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
			}
			timestamp = System.currentTimeMillis();
		}
		return timestamp;
	}

	public static void main(String[] args) {
		// create the instance of Julia
		final Julia application = new Julia();
		application.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		// start the GUI
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				application.createAndShowGUI();
			}
		});
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

	public JList getOutputSetList() {
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

	private void createAndShowGUI() {
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
		setFocusable(true);

		GraphScrollPane aPane = tabbedPane.getActivePane();
		GLListener glList = aPane.getGLListener();
		glList.setPaneHeight(aPane.getHeight());
		glList.setPaneWidth(aPane.getWidth());
		glList.resetZoom();
	}

	/**
	 * method to force whatever component has focus to think it has lost the
	 * focus
	 */
	public void loseFocus() {
		Component focusOwner = getFocusOwner();
		if (focusOwner != null) {
			FocusListener[] listeners = focusOwner.getFocusListeners();
			FocusEvent focusLost = new FocusEvent(focusOwner,
					FocusEvent.FOCUS_LOST);
			for (FocusListener l : listeners) {
				l.focusLost(focusLost);
			}
		}
	}

	/**
	 * method to reset the title to the default
	 */
	public void resetTitle() {
		setTitle(TITLE);
	}

}
