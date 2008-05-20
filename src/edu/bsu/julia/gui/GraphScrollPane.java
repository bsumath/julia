package edu.bsu.julia.gui;

import java.awt.*;
import javax.media.opengl.*;
import edu.bsu.julia.*;

public class GraphScrollPane extends GLJPanel{
	
	private GLListener listener;
	private Julia parentFrame;
	//for serializable interface: do not use
	public static final long serialVersionUID = 0;
	
	public GraphScrollPane(Julia f) {
		super(new GLCapabilities());
		parentFrame = f;
		setName("Graphical display of output functions.");
		setPreferredSize(new Dimension(1000, 1000));
		addMouseListener(parentFrame.getStatusBar());
		addMouseMotionListener(parentFrame.getStatusBar());
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
