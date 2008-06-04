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

public class OutputListCellRenderer extends JPanel implements ListCellRenderer {
	
	private Border raised = BorderFactory.createRaisedBevelBorder();
	private Border lowered = BorderFactory.createLoweredBevelBorder();
	private JTextArea functionDescription = new JTextArea(3, 13);
	private ColorIcon icon = new ColorIcon(Color.WHITE);
	//for serializable interface: do not use
	public static final long serialVersionUID = 0;
	
	public OutputListCellRenderer() {
		setLayout(new BorderLayout());
		functionDescription.setEditable(false);
		functionDescription.setLineWrap(true);
		functionDescription.setWrapStyleWord(true);
		functionDescription.setBackground(getBackground());
		add(functionDescription, BorderLayout.CENTER);
		JPanel iconPanel = new JPanel();
		iconPanel.add(icon);
		add(iconPanel, BorderLayout.WEST);
	}
	
	public Component getListCellRendererComponent(JList list, Object fn,
			int index, boolean selected, boolean focus) {
		OutputFunction function = (OutputFunction)fn;
		functionDescription.setText(function.toString());
		icon.setFunction(function);
		icon.setColor(function.getColor());
		if(selected) setBorder(lowered);
		else setBorder(raised);
		return this;
	}

}
