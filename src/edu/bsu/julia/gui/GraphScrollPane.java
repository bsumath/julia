package edu.bsu.julia.gui;

import java.awt.Dimension;

import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLJPanel;

import edu.bsu.julia.Julia;

public class GraphScrollPane extends GLJPanel {

	private GLListener listener;
	private Julia parentFrame;
	// for serializable interface: do not use
	public static final long serialVersionUID = 0;

	public GraphScrollPane(Julia f) {
		super(new GLCapabilities());
		parentFrame = f;
		setName("Graphical display of output sets.");
		setPreferredSize(new Dimension(1000, 1000));
		addMouseListener(parentFrame.getStatusBar());
		addMouseMotionListener(parentFrame.getStatusBar());
		addMouseWheelListener(parentFrame.getStatusBar());
		listener = new GLListener(parentFrame);
		addMouseListener(listener);
		addMouseMotionListener(listener);
		addGLEventListener(listener);
		addMouseWheelListener(listener);
	}

	public GLListener getGLListener() {
		return listener;
	}
}
