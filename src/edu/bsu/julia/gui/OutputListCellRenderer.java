package edu.bsu.julia.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.ListCellRenderer;
import javax.swing.border.Border;

import edu.bsu.julia.output.OutputSet;

public class OutputListCellRenderer implements ListCellRenderer {
	private Border raised = BorderFactory.createRaisedBevelBorder();
	private Border lowered = BorderFactory.createLoweredBevelBorder();
	private JTextArea functionDescription = new JTextArea(3, 13);
	private ColorIcon icon = new ColorIcon(Color.WHITE);
	private final JPanel panel = new JPanel();
	private final JPanel loadingPanel = new JPanel();

	// for serializable interface: do not use
	public static final long serialVersionUID = 0;

	public OutputListCellRenderer() {
		panel.setLayout(new BorderLayout());
		functionDescription.setEditable(false);
		functionDescription.setLineWrap(true);
		functionDescription.setWrapStyleWord(true);
		functionDescription.setBackground(panel.getBackground());
		panel.add(functionDescription, BorderLayout.CENTER);
		loadingPanel.setPreferredSize(functionDescription.getPreferredSize());
		JPanel iconPanel = new JPanel();
		iconPanel.add(icon);
		panel.add(iconPanel, BorderLayout.WEST);
	}

	public Component getListCellRendererComponent(JList list, Object obj,
			int index, boolean selected, boolean focus) {
		OutputSet set = (OutputSet) obj;
		functionDescription.setText(set.toString() + ". Points:"
				+ set.getNumOfPoints());
		icon.setFunction(set);
		icon.setColor(set.getColor());

		if (set.isLoaded()) {
			panel.remove(loadingPanel);
			panel.add(functionDescription, BorderLayout.CENTER);
		} else {
			loadingPanel.removeAll();
			loadingPanel.add(set.getLoadingComponent());
			panel.remove(functionDescription);
			panel.add(loadingPanel, BorderLayout.CENTER);
		}

		if (selected)
			panel.setBorder(lowered);
		else
			panel.setBorder(raised);
		return panel;
	}
}
