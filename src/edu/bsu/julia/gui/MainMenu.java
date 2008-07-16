package edu.bsu.julia.gui;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import edu.bsu.julia.Julia;
import edu.bsu.julia.gui.actions.AboutAction;
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
import edu.bsu.julia.gui.actions.SaveOutputFunctionAction;
import edu.bsu.julia.gui.actions.SaveSessionAction;
import edu.bsu.julia.gui.actions.ZoomInAction;
import edu.bsu.julia.gui.actions.ZoomOutAction;

public class MainMenu extends JMenuBar {
	
	private Julia parentFrame;
	//for serializable interface: do not use
	public static final long serialVersionUID = 0;
	
	public MainMenu(Julia f) {
		super();
		parentFrame = f;
		add(createFileMenu());
		add(createViewMenu());
		add(createToolsMenu());
		add(createHelpMenu());
	}
	
	private JMenu createFileMenu() {
		JMenu fileMenu = new JMenu("File");
		fileMenu.setActionCommand("For Creating and Saving Sessions and Functions.");
		fileMenu.setMnemonic('F');
		fileMenu.addMouseListener(parentFrame.getStatusBar());
		JMenuItem newItem = new JMenuItem (new NewAction(parentFrame));
		newItem.setMnemonic('N');
		newItem.addMouseListener(parentFrame.getStatusBar());
		fileMenu.add(newItem);
		JMenuItem editSessionItem = new JMenuItem
			(new EditSessionAction(parentFrame));
		editSessionItem.setMnemonic('d');
		editSessionItem.addMouseListener(parentFrame.getStatusBar());
		fileMenu.add(editSessionItem);
		JMenuItem saveSessionItem = new JMenuItem (new SaveSessionAction(parentFrame,false));
		saveSessionItem.setMnemonic('v');
		saveSessionItem.addMouseListener(parentFrame.getStatusBar());
		fileMenu.add(saveSessionItem);
		JMenuItem saveSessionAsItem = new JMenuItem (new SaveSessionAction(parentFrame,true));
		saveSessionAsItem.addMouseListener(parentFrame.getStatusBar());
		fileMenu.add(saveSessionAsItem);
		JMenuItem loadSessionItem = new JMenuItem(new LoadSessionAction(parentFrame));
		loadSessionItem.setMnemonic('l');
		loadSessionItem.addMouseListener(parentFrame.getStatusBar());
		fileMenu.add(loadSessionItem);
		JMenuItem addFnItem = new JMenuItem(new AddFunctionAction(parentFrame));
		addFnItem.setMnemonic('A');
		addFnItem.addMouseListener(parentFrame.getStatusBar());
		fileMenu.add(addFnItem);
		fileMenu.addSeparator();
		JMenuItem saveOutput = new JMenuItem(new SaveOutputFunctionAction(parentFrame));
		saveOutput.setMnemonic('U');
		saveOutput.addMouseListener(parentFrame.getStatusBar());
		fileMenu.add(saveOutput);
		JMenuItem saveGraph = new JMenuItem(new SaveImageAction(parentFrame));
		saveGraph.setMnemonic('S');
		saveGraph.addMouseListener(parentFrame.getStatusBar());
		fileMenu.add(saveGraph);
		fileMenu.addSeparator();
		JMenuItem printGraph = new JMenuItem(new PrintImageAction(parentFrame));
		printGraph.setMnemonic('P');
		printGraph.addMouseListener(parentFrame.getStatusBar());
		fileMenu.add(printGraph);
		fileMenu.addSeparator();
		JMenuItem exitItem = new JMenuItem (new ExitAction(parentFrame));
		exitItem.setMnemonic('x');
		exitItem.addMouseListener(parentFrame.getStatusBar());
		fileMenu.add(exitItem);
		return fileMenu;
	}
	
	private JMenu createViewMenu() {
		JMenu viewMenu = new JMenu("View");
		viewMenu.setActionCommand("For Adjusting the View.");
		viewMenu.setMnemonic('V');
		viewMenu.addMouseListener(parentFrame.getStatusBar());
		JMenuItem zoomInItem = new JMenuItem(new ZoomInAction(parentFrame));
		zoomInItem.setMnemonic('I');
		zoomInItem.addMouseListener(parentFrame.getStatusBar());
		viewMenu.add(zoomInItem);
		JMenuItem zoomOutItem = new JMenuItem(new ZoomOutAction(parentFrame));
		zoomOutItem.setMnemonic('O');
		zoomOutItem.addMouseListener(parentFrame.getStatusBar());
		viewMenu.add(zoomOutItem);
		JMenuItem resetZoomItem = 
			new JMenuItem(new ResetZoomAction(parentFrame));
		resetZoomItem.setMnemonic('R');
		resetZoomItem.addMouseListener(parentFrame.getStatusBar());
		viewMenu.add(resetZoomItem);
		viewMenu.addSeparator();
		JMenuItem addTabItem = new JMenuItem(new AddTabAction(parentFrame));
		addTabItem.setMnemonic('A');
		addTabItem.addMouseListener(parentFrame.getStatusBar());
		viewMenu.add(addTabItem);
		JMenuItem removeTabItem = new JMenuItem(new RemoveTabAction(parentFrame));
		removeTabItem.setMnemonic('e');
		removeTabItem.addMouseListener(parentFrame.getStatusBar());
		viewMenu.add(removeTabItem);
		viewMenu.addSeparator();
		JMenuItem overviewItem = new JMenuItem(new OverviewWindowAction(parentFrame));
		overviewItem.setMnemonic('v');
		overviewItem.addMouseListener(parentFrame.getStatusBar());
		viewMenu.add(overviewItem);
		return viewMenu;
	}
	
	private JMenu createToolsMenu() {
		JMenu toolsMenu = new JMenu("Tools");
		toolsMenu.setActionCommand("Tools and Program Options.");
		toolsMenu.setMnemonic('O');
		toolsMenu.addMouseListener(parentFrame.getStatusBar());
		JMenuItem calc = new JMenuItem (new ComplexCalculatorAction());
		calc.setMnemonic('L');
		calc.addMouseListener(parentFrame.getStatusBar());
		toolsMenu.add(calc);
		toolsMenu.addSeparator();
		JMenuItem options = new JMenuItem (new OptionsAction(parentFrame));
		options.setMnemonic('C');
		options.addMouseListener(parentFrame.getStatusBar());
		toolsMenu.add(options);
		return toolsMenu;
	}
	
	private JMenu createHelpMenu() {
		JMenu helpMenu = new JMenu("Help");
		helpMenu.setActionCommand("Program Help and Information.");
		helpMenu.setMnemonic('H');
		helpMenu.addMouseListener(parentFrame.getStatusBar());
		JMenuItem aboutItem = new JMenuItem(new AboutAction(parentFrame));
		aboutItem.setMnemonic('a');
		aboutItem.addMouseListener(parentFrame.getStatusBar());
		helpMenu.add(aboutItem);
		JMenuItem helpItem = new JMenuItem(new HelpAction(parentFrame));
		helpItem.setMnemonic('e');
		helpItem.addMouseListener(parentFrame.getStatusBar());
		helpMenu.add(helpItem);
		return helpMenu;
	}

}
