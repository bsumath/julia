package edu.bsu.julia.gui.actions;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.URL;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import edu.bsu.julia.Julia;

public class HelpAction extends AbstractAction {
	private static final String HELP_URL_BASE = "http://www.bsu.edu/web/rstankewitz/JuliaHelp/";
	private static final String HELP_URL_START = "JuliaHelp.htm";
	private static final int WIDTH = 835;
	private static final int HEIGHT = 650;

	private Julia parentFrame;
	private final JDialog dialog;
	private final JEditorPane editor;
	private JScrollPane scrollPane;
	private JOptionPane backupHelp;
	// for serializable interface: do not use
	public static final long serialVersionUID = 0;

	public HelpAction(Julia f) {
		super("Help", new ImageIcon(Thread.currentThread()
				.getContextClassLoader().getResource("help.png")));
		putValue("SHORT_DESCRIPTION", "Open Help");
		putValue("LONG_DESCRIPTION",
				"Open the help file in the default web browser.");
		parentFrame = f;

		// create the dialog
		dialog = new JDialog(parentFrame, "Help", true);

		editor = new JEditorPane();
		editor.setEditable(false);
		editor.setContentType("text/html");
		editor.addHyperlinkListener(new HyperlinkListener() {
			public void hyperlinkUpdate(HyperlinkEvent e) {
				if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
					try {
						URL url = e.getURL();
						if (url.toString().startsWith(HELP_URL_BASE))
							editor.setPage(url);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}
		});
		scrollPane = new JScrollPane(editor);
		scrollPane.setPreferredSize(new Dimension(WIDTH, HEIGHT));

		// create a backup help in case the html doesn't load
		String output = "Help files are not currently available"
				+ "\nwithin the program, but they may be accessed online at:"
				+ "\n" + HELP_URL_BASE + HELP_URL_START;
		backupHelp = new JOptionPane(output, JOptionPane.INFORMATION_MESSAGE);
		backupHelp.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent e) {
				String prop = e.getPropertyName();

				if (dialog.isVisible() && (e.getSource() == backupHelp)
						&& (prop.equals(JOptionPane.VALUE_PROPERTY))) {
					dialog.setVisible(false);
					backupHelp.setValue(JOptionPane.UNINITIALIZED_VALUE);
				}
			}
		});
	}

	public void actionPerformed(ActionEvent arg0) {
		// try to open the html
		try {
			editor.setPage(HELP_URL_BASE + HELP_URL_START);
			dialog.setContentPane(scrollPane);
		} catch (IOException e) {
			dialog.setContentPane(backupHelp);
		}
		dialog.pack();
		dialog.setVisible(true);
	}
}
