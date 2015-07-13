package edu.bsu.julia.gui.actions;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import edu.bsu.julia.Julia;

public class AboutAction extends AbstractAction {
	private static final String[] developers = new String[] { "Wendy Conatser",
			"Benjamin Dean", "Sida Qiu", "Trey Butz", "Yun Li",
			"Kristopher Hart", "Dr. Richard Stankewitz" };
	private static final String[] license = new String[] {
			"This program is free software: you can redistribute it and/or modify",
			"it under the terms of the GNU General Public License as published by",
			"the Free Software Foundation, either version 3 of the License, or",
			"(at your option) any later version.", " ",
			"This program is distributed in the hope that it will be useful,",
			"but WITHOUT ANY WARRANTY; without even the implied warranty of",
			"MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the",
			"GNU General Public License for more details." };
	private static final String licenseURL = "http://www.gnu.org/licenses/gpl-3.0.txt";

	private Julia parentFrame;
	// for serializable interface: do not use
	public static final long serialVersionUID = 0;

	public AboutAction(Julia f) {
		super("About..", new ImageIcon(Thread.currentThread()
				.getContextClassLoader().getResource("smiley.png")));
		putValue("SHORT_DESCRIPTION", "About this Program");
		putValue("LONG_DESCRIPTION", "General information about the "
				+ "program.");
		parentFrame = f;
	}

	public void actionPerformed(ActionEvent arg0) {
		String buildString;
		Properties properties = new Properties();
		try {
			properties.load(Thread.currentThread().getContextClassLoader()
					.getResourceAsStream("build.properties"));
			buildString = "Built Date: " + properties.getProperty("BuildDate")
					+ "\n";
		} catch (IOException e) {
			buildString = "";
		}

		Box box = Box.createVerticalBox();
		box.add(new JLabel("Julia (Version 2.0)"));
		box.add(new JLabel(buildString));
		box.add(new JLabel(" "));
		box.add(new JLabel("Developed by:"));
		for (String person : developers) {
			box.add(new JLabel("       " + person));
		}

		box.add(new JLabel(" "));
		DateFormat dateFormat = new SimpleDateFormat("yyyy");
		box.add(new JLabel("Copyright \u00A9" + dateFormat.format(new Date())
				+ " Ball State University"));

		box.add(new JLabel(" "));
		for (String line : license) {
			box.add(new JLabel(line));
		}
		JLabel link = new JLabel(licenseURL);
		if (Desktop.isDesktopSupported()) {
			final Desktop desktop = Desktop.getDesktop();
			if (desktop.isSupported(Desktop.Action.BROWSE)) {
				link = new JLabel("<html><u>" + licenseURL + "</u></html>");
				link.setForeground(Color.BLUE);
				link.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent arg0) {
						try {
							desktop.browse(new URI(licenseURL));
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
		}
		box.add(link);

		box.add(new JLabel(" "));
		box.add(new JLabel("\nAll icons by Everaldo, "
				+ "used under GNU Lesser General Public License."));
		box.add(new JLabel("Many thanks."));

		JOptionPane.showMessageDialog(parentFrame, box, "About..",
				JOptionPane.INFORMATION_MESSAGE);
	}

}
