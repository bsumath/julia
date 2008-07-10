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

import edu.bsu.julia.output.OutputFunction;

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

	public Component getListCellRendererComponent(JList list, Object fn,
			int index, boolean selected, boolean focus) {
		OutputFunction function = (OutputFunction) fn;
		functionDescription.setText(function.toString() + ". Points:"
				+ function.getNumOfPoints());
		icon.setFunction(function);
		icon.setColor(function.getColor());

		if (function.isLoaded()) {
			panel.remove(loadingPanel);
			panel.add(functionDescription, BorderLayout.CENTER);
		} else {
			loadingPanel.removeAll();
			loadingPanel.add(function.getLoadingComponent());
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
