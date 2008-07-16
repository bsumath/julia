package edu.bsu.julia.gui;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JToolBar;

import edu.bsu.julia.Julia;
import edu.bsu.julia.gui.actions.AddFunctionAction;
import edu.bsu.julia.gui.actions.AddTabAction;
import edu.bsu.julia.gui.actions.ComplexCalculatorAction;
import edu.bsu.julia.gui.actions.EditSessionAction;
import edu.bsu.julia.gui.actions.ExitAction;
import edu.bsu.julia.gui.actions.HelpAction;
import edu.bsu.julia.gui.actions.LoadSessionAction;
import edu.bsu.julia.gui.actions.NewAction;
import edu.bsu.julia.gui.actions.OptionsAction;
import edu.bsu.julia.gui.actions.OverviewWindowAction;
import edu.bsu.julia.gui.actions.PrintImageAction;
import edu.bsu.julia.gui.actions.RemoveTabAction;
import edu.bsu.julia.gui.actions.ResetZoomAction;
import edu.bsu.julia.gui.actions.SaveImageAction;
import edu.bsu.julia.gui.actions.SaveSessionAction;
import edu.bsu.julia.gui.actions.ZoomInAction;
import edu.bsu.julia.gui.actions.ZoomOutAction;

public class MainToolBar extends JToolBar {
	
	private Julia parentFrame;
	//for serializable interface: do not use
	public static final long serialVersionUID = 0;
	
	public MainToolBar(Julia f) {
		super();
		parentFrame = f;
		setBorder(BorderFactory.createEtchedBorder());
		createFileSection();
		createViewSection();
		createHelpSection();
		createExitSection();
	}
	
	void createFileSection() {
		NewAction newAction = new NewAction(parentFrame);
		JButton newButton = add(newAction);
		newButton.setFocusable(false);
		newButton.setToolTipText((String)newAction.getValue("SHORT_DESCRIPTION"));
		newButton.addMouseListener(parentFrame.getStatusBar());
		EditSessionAction editSessionAction = new EditSessionAction(parentFrame);
		JButton editButton = add(editSessionAction);
		editButton.setFocusable(false);
		editButton.setToolTipText
			((String)editSessionAction.getValue("SHORT_DESCRIPTION"));
		editButton.addMouseListener(parentFrame.getStatusBar());
		SaveSessionAction saveSessionAction = new SaveSessionAction(parentFrame,false);
		JButton saveSessionButton = add(saveSessionAction);
		saveSessionButton.setFocusable(false);
		saveSessionButton.setToolTipText
			((String)saveSessionAction.getValue("SHORT_DESCRIPTION"));
		saveSessionButton.addMouseListener(parentFrame.getStatusBar());
		LoadSessionAction loadSessionAction = new LoadSessionAction(parentFrame);
		JButton loadButton = add(loadSessionAction);
		loadButton.setFocusable(false);
		loadButton.setToolTipText
			((String)loadSessionAction.getValue("SHORT_DESCRIPTION"));
		loadButton.addMouseListener(parentFrame.getStatusBar());
		AddFunctionAction addFnAction = new AddFunctionAction(parentFrame);
		JButton addFnButton = add(addFnAction);
		addFnButton.setFocusable(false);
		addFnButton.setToolTipText
			((String)addFnAction.getValue("SHORT_DESCRIPTION"));
		addFnButton.addMouseListener(parentFrame.getStatusBar());
		addSeparator();
	}
	
	void createViewSection() {
		SaveImageAction saveImageAction = new SaveImageAction(parentFrame);
		JButton saveImageButton = add(saveImageAction);
		saveImageButton.setFocusable(false);
		saveImageButton.setToolTipText
			((String)saveImageAction.getValue("SHORT_DESCRIPTION"));
		saveImageButton.addMouseListener(parentFrame.getStatusBar());
		PrintImageAction printImageAction = new PrintImageAction(parentFrame);
		JButton printImageButton = add(printImageAction);
		printImageButton.setFocusable(false);
		printImageButton.setToolTipText
			((String)printImageAction.getValue("SHORT_DESCRIPTION"));
		printImageButton.addMouseListener(parentFrame.getStatusBar());
		addSeparator();
		ZoomInAction zoomInAction = new ZoomInAction(parentFrame);
		JButton zoomInButton = add(zoomInAction);
		zoomInButton.setFocusable(false);
		zoomInButton.setToolTipText
			((String)zoomInAction.getValue("SHORT_DESCRIPTION"));
		zoomInButton.addMouseListener(parentFrame.getStatusBar());
		ZoomOutAction zoomOutAction = new ZoomOutAction(parentFrame);
		JButton zoomOutButton = add(zoomOutAction);
		zoomOutButton.setFocusable(false);
		zoomOutButton.setToolTipText
			((String)zoomOutAction.getValue("SHORT_DESCRIPTION"));
		zoomOutButton.addMouseListener(parentFrame.getStatusBar());
		ResetZoomAction resetZoomAction = new ResetZoomAction(parentFrame);
		JButton resetZoomButton = add(resetZoomAction);
		resetZoomButton.setFocusable(false);
		resetZoomButton.setToolTipText
			((String)resetZoomAction.getValue("SHORT_DESCRIPTION"));
		resetZoomButton.addMouseListener(parentFrame.getStatusBar());
		addSeparator();
		AddTabAction addTabAction = new AddTabAction(parentFrame);
		JButton addTabButton = add(addTabAction);
		addTabButton.setFocusable(false);
		addTabButton.setToolTipText
			((String)addTabAction.getValue("SHORT_DESCRIPTION"));
		addTabButton.addMouseListener(parentFrame.getStatusBar());
		RemoveTabAction removeTabAction = new RemoveTabAction(parentFrame);
		JButton removeTabButton = add(removeTabAction);
		removeTabButton.setFocusable(false);
		removeTabButton.setToolTipText
			((String)removeTabAction.getValue("SHORT_DESCRIPTION"));
		removeTabButton.addMouseListener(parentFrame.getStatusBar());
		addSeparator();
		OverviewWindowAction overviewWindowAction = new OverviewWindowAction(parentFrame);
		JButton overviewButton = add(overviewWindowAction);
		overviewButton.setFocusable(false);
		overviewButton.setToolTipText
			((String)overviewWindowAction.getValue("SHORT_DESCRIPTION"));
		overviewButton.addMouseListener(parentFrame.getStatusBar());
	}
	
	
	
	void createHelpSection() {
		ComplexCalculatorAction complexCalculator = new ComplexCalculatorAction();
		JButton calcButton = add(complexCalculator);
		calcButton.setFocusable(false);
		calcButton.setToolTipText((String)complexCalculator.getValue("SHORT_DESCRIPTION"));
		calcButton.addMouseListener(parentFrame.getStatusBar());
		OptionsAction optionsAction = new OptionsAction(parentFrame);
		JButton optionsButton = add(optionsAction);
		optionsButton.setFocusable(false);
		optionsButton.setToolTipText
			((String)optionsAction.getValue("SHORT_DESCRIPTION"));
		optionsButton.addMouseListener(parentFrame.getStatusBar());
		addSeparator();
		HelpAction helpAction = new HelpAction(parentFrame);
		JButton helpButton = add(helpAction);
		helpButton.setFocusable(false);
		helpButton.setToolTipText((String)helpAction.getValue("SHORT_DESCRIPTION"));
		helpButton.addMouseListener(parentFrame.getStatusBar());
		addSeparator();
	}
	
	void createExitSection() {
		ExitAction exitAction = new ExitAction(parentFrame);
		JButton exitButton = add(exitAction);
		exitButton.setFocusable(false);
		exitButton.setToolTipText
			((String)exitAction.getValue("SHORT_DESCRIPTION"));
		exitButton.addMouseListener(parentFrame.getStatusBar());
	}

}
