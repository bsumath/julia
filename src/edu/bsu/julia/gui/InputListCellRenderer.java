package edu.bsu.julia.gui;

import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.ListCellRenderer;
import javax.swing.border.Border;

public class InputListCellRenderer extends JPanel implements ListCellRenderer {

	private Border raised = BorderFactory.createRaisedBevelBorder();
	private Border lowered = BorderFactory.createLoweredBevelBorder();
	private JTextArea textArea = new JTextArea(4, 16);
	// for serializable interface: do not use
	public static final long serialVersionUID = 0;

	public InputListCellRenderer() {
	}

	public Component getListCellRendererComponent(JList list, Object fn,
			int index, boolean selected, boolean focus) {
		textArea.setText(fn.toString());
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setBackground(getBackground());
		this.add(textArea);
		if (selected)
			setBorder(lowered);
		else
			setBorder(raised);

		return this;
	}

}
