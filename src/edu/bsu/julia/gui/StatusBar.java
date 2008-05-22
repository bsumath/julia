package edu.bsu.julia.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import javax.media.opengl.*;

public class StatusBar extends JPanel implements MouseListener,
		MouseMotionListener {

	private JLabel helpLabel = new JLabel();
	private String defaultString = "Hover over an object to see a description.";
	private JLabel coordinateLabel = new JLabel();
	// for serializable interface: do not use
	public static final long serialVersionUID = 0;

	public StatusBar() {
		super(new BorderLayout());
		setBorder(BorderFactory.createEtchedBorder());
		helpLabel.setText(defaultString);
		add(helpLabel, BorderLayout.WEST);
		coordinateLabel.setText("x: 0, y: 0");
		add(coordinateLabel, BorderLayout.EAST);
	}

	public void mouseMoved(MouseEvent e) {
		if (e.getSource().getClass().getName().equals(
				edu.bsu.julia.gui.GraphScrollPane.class.getName())) {
			GraphScrollPane panel = (GraphScrollPane) e.getSource();
			GLListener list = panel.getGLListener();
			double xd = (e.getX() * list.getWidth() / panel.getWidth())
					+ list.getX();
			double yd = ((panel.getHeight() - e.getY()) * list.getHeight() / panel
					.getHeight())
					+ list.getY();

			coordinateLabel.setText("x: " + xd + ", y: " + yd);
		}
	}

	public void mouseClicked(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
		Object component = e.getSource();

		if (component.getClass().getName().equals(JList.class.getName())) {
			JList jList = (JList) component;
			helpLabel.setText(jList.getName());
		} else if (component.getClass().getName().equals(
				edu.bsu.julia.gui.GraphScrollPane.class.getName())) {
			GLJPanel graph = (GLJPanel) component;
			helpLabel.setText(graph.getName());
		} else if (component.getClass().getName().equals(
				JToggleButton.class.getName())) {
			JToggleButton jToggleButton = (JToggleButton) component;
			helpLabel.setText(jToggleButton.getName());
		} else if (component.getClass().getName().equals(
				JButton.class.getName())) {
			JButton jButton = (JButton) component;
			helpLabel.setText((String) jButton.getAction().getValue(
					"LONG_DESCRIPTION"));
		} else if (component.getClass().getName().equals(
				"javax.swing.JToolBar$1")) {
			JButton jButton = (JButton) component;
			helpLabel.setText((String) jButton.getAction().getValue(
					"LONG_DESCRIPTION"));
		} else if (component.getClass().getName().equals(
				JMenuItem.class.getName())) {
			JMenuItem jMenuItem = (JMenuItem) component;
			helpLabel.setText((String) (jMenuItem.getAction()
					.getValue("LONG_DESCRIPTION")));
		} else if (component.getClass().getName().equals(JMenu.class.getName())) {
			JMenu jMenu = (JMenu) component;
			helpLabel.setText(jMenu.getActionCommand());
		} else if (component.getClass().getName()
				.equals(JLabel.class.getName())) {
			JLabel jLabel = (JLabel) component;
			helpLabel.setText(jLabel.getName());
		}
	}

	public void mouseExited(MouseEvent e) {
		helpLabel.setText(defaultString);
		coordinateLabel.setText("x: 0, y: 0");
	}

	public void mousePressed(MouseEvent e) {

	}

	public void mouseReleased(MouseEvent e) {

	}

	public void mouseDragged(MouseEvent e) {

	}

}
