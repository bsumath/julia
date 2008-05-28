package edu.bsu.julia.gui;

import java.awt.*;
import java.beans.*;
import javax.media.opengl.*;
import javax.media.opengl.glu.GLU;
import javax.swing.*;
import java.awt.event.*;
import edu.bsu.julia.*;
import edu.bsu.julia.output.OutputFunction;

public class OverviewListener implements GLEventListener, PropertyChangeListener,
	MouseListener, MouseMotionListener {
	private Julia parentFrame;
	private double x, y, width, height, boxX, boxY, boxWidth, boxHeight,
		paneHeight, paneWidth, boxPHeight, boxPWidth, dragX, dragY;
	private OutputFunction fns[];
	private JDialog frame;
	private GLListener list;
	private boolean dragInProgress;
	
	public OverviewListener(Julia f) {
		parentFrame = f;
		list = parentFrame.getTabbedPane().getActivePane().getGLListener();
		list.addListener(this);
		dragInProgress = false;
		boxX = list.getX();
		boxY = list.getY();
		boxWidth = list.getWidth();
		boxHeight = list.getHeight();
		x = boxX + boxWidth/2;
		y = boxY + boxHeight/2;
		width = boxWidth*5;
		height = boxHeight*5;
		x -= width/2;
		y -= height/2;
		fns = list.getFns();
		boxPHeight = list.getPaneHeight();
		boxPWidth = list.getPaneWidth();
		if(boxPHeight >= boxPWidth) {
			paneWidth = 250;
			paneHeight = (boxPHeight/boxPWidth)*250;
		}
		else {
			paneHeight = 250;
			paneWidth = (boxPWidth/boxPHeight)*250;
		}
		
		frame = new JDialog(parentFrame, "Overview", false);
		frame.setResizable(false);
		GLJPanel panel = new GLJPanel(new GLCapabilities());
		panel.addGLEventListener(this);
		frame.addMouseListener(this);
		frame.addMouseMotionListener(this);
		frame.setContentPane(panel);
		frame.setSize(new Dimension((int)paneWidth, (int)paneHeight));
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setVisible(true);
	}

	public void display(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();
		GLU glu = new GLU();
		gl.glClear(GL.GL_COLOR_BUFFER_BIT);
		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glLoadIdentity();
		glu.gluOrtho2D(x, x+width, y, y+height);
		
		gl.glColor3i(0, 0, 0);
		gl.glBegin(GL.GL_LINES);
			gl.glVertex2d(x, 0);
			gl.glVertex2d(x+width, 0);
			gl.glVertex2d(0, y);
			gl.glVertex2d(0, y+height);
		gl.glEnd();
		
		gl.glBegin(GL.GL_LINE_LOOP);
		gl.glVertex2d(boxX, boxY);
		gl.glVertex2d(boxX + boxWidth, boxY);
		gl.glVertex2d(boxX + boxWidth, boxY + boxHeight);
		gl.glVertex2d(boxX, boxY + boxHeight);
		gl.glEnd();
		
		if(fns==null) return;
		gl.glPointSize(1);
		for(int i=0; i<fns.length; i++) {
			if(fns[i]!=null) {
				OutputFunction function = fns[i];
				float[] cArray = new float[3];
				function.getColor().getColorComponents(cArray);
				gl.glColor3f(cArray[0], cArray[1], cArray[2]);
				ComplexNumber[] points = function.getPoints();
				gl.glBegin(GL.GL_POINTS);
				for(int j=0; j<points.length; j++)
					gl.glVertex2d(points[j].getX(), points[j].getY());
				gl.glEnd();
			}
		}
		
		gl.glFlush();
	}

	public void displayChanged(GLAutoDrawable arg0, boolean arg1, boolean arg2) {
	}

	public void init(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();
		gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
	}

	public void reshape(GLAutoDrawable arg0, int arg1, int arg2, int arg3,
			int arg4) {
	}

	public void propertyChange(PropertyChangeEvent e) {
		String name = e.getPropertyName();
		if(name.equals("x")) {
			double newX = (Double)e.getNewValue();
			x += newX - boxX;
			boxX = newX;
			frame.repaint();
		}
		else if(name.equals("y")) {
			double newY = (Double)e.getNewValue();
			y += newY - boxY;
			boxY = newY;
			frame.repaint();
		}
		else if(name.equals("width")) {
			boxWidth = (Double)e.getNewValue();
			x = boxX + boxWidth/2;
			width = boxWidth*5;
			x -= width/2;
			frame.repaint();
		}
		else if(name.equals("height")) {
			boxHeight = (Double)e.getNewValue();
			y = boxY + boxHeight/2;
			height = boxHeight*5;
			y -= height/2;
			frame.repaint();
		}
		else if(name.equals("fns")) {
			fns = (OutputFunction[])e.getNewValue();
			frame.repaint();
		}
		else if(name.equals("paneHeight")) {
			boxPHeight = (Double)e.getNewValue();
			if(boxPHeight >= boxPWidth) {
				paneWidth = 250;
				paneHeight = (boxPHeight/boxPWidth)*250;
			}
			else {
				paneHeight = 250;
				paneWidth = (boxPWidth/boxPHeight)*250;
			}
			frame.setSize(new Dimension((int)paneWidth, (int)paneHeight));
			frame.repaint();
		}
		else if(name.equals("paneWidth")) {
			boxPWidth = (Double)e.getNewValue();
			if(boxPHeight >= boxPWidth) {
				paneWidth = 250;
				paneHeight = (boxPHeight/boxPWidth)*250;
			}
			else {
				paneHeight = 250;
				paneWidth = (boxPWidth/boxPHeight)*250;
			}
			frame.setSize(new Dimension((int)paneWidth, (int)paneHeight));
			frame.repaint();
		}
	}

	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount()==2){
			double newX = (e.getX()*width/paneWidth)+x;
			double newY = ((paneHeight - e.getY())*height/paneHeight)+y;
			list.setX(newX - boxWidth/2);
			list.setY(newY - boxHeight/2);
			parentFrame.repaint();
		}	
	}

	public void mouseEntered(MouseEvent arg0) {	
	}

	public void mouseExited(MouseEvent arg0) {	
	}

	public void mousePressed(MouseEvent e) {	
		dragX = (e.getX()*width/paneWidth)+x;
		dragY = ((paneHeight - e.getY())*height/paneHeight)+y;
		dragInProgress = true;
	}

	public void mouseReleased(MouseEvent arg0) {
		if(dragInProgress) {
			dragX = 0;
			dragY = 0;
			dragInProgress = false;
		}
	}

	public void mouseDragged(MouseEvent e) {
		if(!dragInProgress) return;
		double newX = (e.getX()*width/paneWidth)+x;
		double newY = ((paneHeight - e.getY())*height/paneHeight)+y;
		list.setX(boxX - newX + dragX);
		list.setY(boxY - newY + dragY);
		parentFrame.repaint();
		
	}

	public void mouseMoved(MouseEvent arg0) {
	}

}
