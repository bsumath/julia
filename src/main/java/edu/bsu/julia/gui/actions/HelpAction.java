package edu.bsu.julia.gui.actions;

import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.Stack;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import edu.bsu.julia.Julia;

public class HelpAction extends AbstractAction {
	private static final String HELP_URL_BASE = "http://www.bsu.edu/web/rstankewitz/JuliaHelp2.0/";
	private static final String HELP_URL_START = "JuliaHelp.htm";
	private static final int WIDTH = 835;
	private static final int HEIGHT = 650;

	private final Julia parentFrame;
	private final JDialog dialog;
	private final JEditorPane editor;
	private final JPanel panel;
	private final JOptionPane backupHelp;
	// for serializable interface: do not use
	public static final long serialVersionUID = 0;

	private final Stack<URL> backStack = new Stack<URL>();
	private final Stack<URL> forwardStack = new Stack<URL>();
	private final JButton home;
	private final JButton back;
	private final JButton forward;
	private static final ClassLoader CL = Thread.currentThread()
			.getContextClassLoader();

	public HelpAction(Julia f) {
		super("Help", new ImageIcon(CL.getResource("help.png")));
		putValue("SHORT_DESCRIPTION", "Open Help");
		putValue("LONG_DESCRIPTION",
				"Open the help file in the default web browser.");
		parentFrame = f;

		// create the dialog
		dialog = new JDialog(parentFrame, "Help", true);

		panel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		forward = new JButton(new AbstractAction("Forward") {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent arg0) {
				if (!forwardStack.empty()) {
					try {
						backStack.push(editor.getPage());
						back.setEnabled(true);
						editor.setPage(forwardStack.pop());
						if (forwardStack.empty())
							forward.setEnabled(false);
					} catch (IOException e) {
					}
				}
			}
		});
		forward.setEnabled(false);
		forward.setIcon(new ImageIcon(CL.getResource("forward.png")));

		back = new JButton(new AbstractAction("Back") {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent arg0) {
				if (!backStack.empty()) {
					try {
						forwardStack.push(editor.getPage());
						forward.setEnabled(true);
						editor.setPage(backStack.pop());
						if (backStack.empty())
							back.setEnabled(false);
					} catch (IOException e) {
					}
				}
			}
		});
		back.setEnabled(false);
		back.setIcon(new ImageIcon(CL.getResource("back.png")));

		home = new JButton(new AbstractAction("Home") {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent arg0) {
				try {
					backStack.push(editor.getPage());
					back.setEnabled(true);
					editor.setPage(HELP_URL_BASE + HELP_URL_START);
				} catch (IOException e) {
				}
			}
		});
		home.setIcon(new ImageIcon(CL.getResource("home.png")));

		JButton external = null;
		if (Desktop.isDesktopSupported()) {
			final Desktop desktop = Desktop.getDesktop();
			if (desktop.isSupported(Desktop.Action.BROWSE)) {
				external = new JButton(new AbstractAction(
						"Open help in web browser") {
					private static final long serialVersionUID = 2278765590225068684L;

					@Override
					public void actionPerformed(ActionEvent arg0) {
						try {
							desktop.browse(new URI(HELP_URL_BASE
									+ HELP_URL_START));
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
		}
		editor = new JEditorPane();
		editor.setEditable(false);
		editor.setContentType("text/html");
		editor.addHyperlinkListener(new HyperlinkListener() {
			public void hyperlinkUpdate(HyperlinkEvent e) {
				if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
					try {
						URL url = e.getURL();
						if (url.toString().startsWith(HELP_URL_BASE)) {
							backStack.push(editor.getPage());
							back.setEnabled(true);
							editor.setPage(url);
						}
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}
		});

		final JScrollPane scrollPane = new JScrollPane(editor);
		scrollPane.setPreferredSize(new Dimension(WIDTH, HEIGHT));

		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 1;
		c.gridheight = 1;
		panel.add(home, c);
		c.gridx = 1;
		c.gridy = 0;
		panel.add(back, c);
		c.gridx = 2;
		c.gridy = 0;
		panel.add(forward, c);
		c.gridx = 3;
		c.gridy = 0;
		panel.add(new JLabel(), c);
		if (external != null) {
			c.gridx = 4;
			c.gridy = 0;
			panel.add(external, c);
		} else {
			c.gridx = 4;
			c.gridy = 0;
			panel.add(new JLabel(), c);
		}
		c.fill = GridBagConstraints.NONE;
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 5;
		panel.add(scrollPane, c);

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
			dialog.setContentPane(panel);
		} catch (IOException e) {
			dialog.setContentPane(backupHelp);
		}
		dialog.pack();
		dialog.setVisible(true);
	}
}
