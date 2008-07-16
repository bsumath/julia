package edu.bsu.julia.gui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import edu.bsu.julia.ComplexNumber;
import edu.bsu.julia.Julia;
import edu.bsu.julia.output.OutputSet;

public class GLListener implements GLEventListener, ListSelectionListener,
		PropertyChangeListener, MouseWheelListener, KeyListener,
		MouseMotionListener, MouseListener {

	public final double ZOOM_CONSTANT = 1.25;
	private Julia parentFrame;
	private OutputSet[] sets;
	private double x, y, width, height, paneWidth, paneHeight, dragX, dragY;
	private boolean enabled, unsized, dragInProgress;
	private boolean axisEnabled;
	private PropertyChangeSupport support = new PropertyChangeSupport(this);
	private boolean grilEnabled;

	public GLListener(Julia f) {
		parentFrame = f;
		parentFrame.addListener(this);
		unsized = true;
		axisEnabled = true;
		dragInProgress = false;
		grilEnabled = false;
		enabled = true;
	}

	public void display(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();
		GLU glu = new GLU();
		gl.glClear(GL.GL_COLOR_BUFFER_BIT);
		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glLoadIdentity();
		glu.gluOrtho2D(x, x + width, y, y + height);
		axisEnabled = parentFrame.getAxisTrigger();
		if (axisEnabled) {
			drawAxes(gl);
		}
		grilEnabled = parentFrame.getGrilTrigger();
		if (grilEnabled) {
			drawGril(gl);
		}
		drawFunctions(gl);
		gl.glFlush();
	}

	public void displayChanged(GLAutoDrawable arg0, boolean arg1, boolean arg2) {
	}

	public void init(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();
		gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
	}

	public void reshape(GLAutoDrawable arg0, int xPane, int yPane,
			int widthPane, int heightPane) {
		int newPaneWidth = parentFrame.getTabbedPane().getActivePane()
				.getWidth();
		int newPaneHeight = parentFrame.getTabbedPane().getActivePane()
				.getHeight();
		setX(x + width / 2);
		setY(y + height / 2);

		setHeight((height * newPaneHeight) / paneHeight);
		setWidth((width * newPaneWidth) / paneWidth);
		setPaneHeight(newPaneHeight);
		setPaneWidth(newPaneWidth);

		setX(x - width / 2);
		setY(y - height / 2);
		parentFrame.repaint();
	}

	private void drawAxes(GL gl) {
		gl.glColor3i(0, 0, 0);
		gl.glBegin(GL.GL_LINES);
		gl.glVertex2d(x, 0);
		gl.glVertex2d(x + width, 0);
		gl.glVertex2d(0, y);
		gl.glVertex2d(0, y + height);
		if ((width < 1000) && (height < 1000)) {
			for (int i = 0; i < (x + width); i++) {
				for (int j = 1; j <= 9; j++) {
					gl.glVertex2d(i + j * .1, -height / 200);
					gl.glVertex2d(i + j * .1, height / 200);
				}
				gl.glVertex2d(i + 1, -height / 100);
				gl.glVertex2d(i + 1, height / 100);
			}
			for (int i = 0; i > x; i--) {
				for (int j = 1; j >= -9; j--) {
					gl.glVertex2d(i + j * .1, -height / 200);
					gl.glVertex2d(i + j * .1, height / 200);
				}
				gl.glVertex2d(i - 1, -height / 100);
				gl.glVertex2d(i - 1, height / 100);
			}
			for (int i = 0; i < (y + height); i++) {
				for (int j = 1; j <= 9; j++) {
					gl.glVertex2d(-width / 200, i + j * .1);
					gl.glVertex2d(width / 200, i + j * .1);
				}
				gl.glVertex2d(-width / 100, i + 1);
				gl.glVertex2d(width / 100, i + 1);
			}
			for (int i = 0; i > y; i--) {
				for (int j = 1; j >= -9; j--) {
					gl.glVertex2d(-width / 200, i + j * .1);
					gl.glVertex2d(width / 200, i + j * .1);
				}
				gl.glVertex2d(-width / 100, i - 1);
				gl.glVertex2d(width / 100, i - 1);
			}
		}
		gl.glEnd();
	}

	private void drawGril(GL gl) {
		gl.glColor3f(.92f, .92f, .92f);
		gl.glBegin(GL.GL_LINES);
		int newPaneWidth = parentFrame.getTabbedPane().getActivePane()
				.getWidth();
		int newPaneHeight = parentFrame.getTabbedPane().getActivePane()
				.getHeight();
		gl.glVertex2d(x, 0);
		gl.glVertex2d(x + width, 0);
		gl.glVertex2d(0, y);
		gl.glVertex2d(0, y + height);
		for (int i = 0; i < (x + width); i++) {
			for (int j = 1; j <= 9; j++) {
				gl.glVertex2d(i + j * .1, -newPaneHeight);
				gl.glVertex2d(i + j * .1, newPaneHeight);
			}
			gl.glVertex2d(i + 1, -newPaneHeight);
			gl.glVertex2d(i + 1, newPaneHeight);
		}
		for (int i = 0; i > x; i--) {
			for (int j = 1; j >= -9; j--) {
				gl.glVertex2d(i + j * .1, -newPaneHeight);
				gl.glVertex2d(i + j * .1, newPaneHeight);
			}
			gl.glVertex2d(i - 1, -newPaneHeight);
			gl.glVertex2d(i - 1, newPaneHeight);
		}
		for (int i = 0; i < (y + height); i++) {
			for (int j = 1; j <= 9; j++) {
				gl.glVertex2d(-newPaneWidth, i + j * .1);
				gl.glVertex2d(newPaneWidth, i + j * .1);
			}
			gl.glVertex2d(-newPaneWidth, i + 1);
			gl.glVertex2d(newPaneWidth, i + 1);
		}
		for (int i = 0; i > y; i--) {
			for (int j = 1; j >= -9; j--) {
				gl.glVertex2d(-newPaneWidth, i + j * .1);
				gl.glVertex2d(newPaneWidth, i + j * .1);
			}
			gl.glVertex2d(-newPaneWidth, i - 1);
			gl.glVertex2d(newPaneWidth, i - 1);
		}
		gl.glEnd();
	}

	private void drawFunctions(GL gl) {
		if (sets == null)
			return;
		int dSize = parentFrame.getDotSize();
		gl.glPointSize(dSize);
		for (int i = 0; i < sets.length; i++) {
			OutputSet set = sets[i];
			if (set != null && set.isLoaded()) {
				float[] colorArray = new float[3];
				set.getColor().getColorComponents(colorArray);
				gl.glColor3f(colorArray[0], colorArray[1], colorArray[2]);

				gl.glBegin(GL.GL_POINTS);
				for (ComplexNumber point : set.getPoints())
					if (point != null)
						gl.glVertex2d(point.getX(), point.getY());
				gl.glEnd();
			}
		}
	}

	public void addListener(PropertyChangeListener l) {
		support.addPropertyChangeListener(l);
	}

	public void removeListener(PropertyChangeListener l) {
		support.removePropertyChangeListener(l);
	}

	public double getX() {
		return x;
	}

	public void setX(double xValue) {
		x = xValue;
		support.firePropertyChange("x", null, x);
	}

	public double getY() {
		return y;
	}

	public void setY(double yValue) {
		y = yValue;
		support.firePropertyChange("y", null, y);
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double h) {
		height = h;
		support.firePropertyChange("height", null, height);
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double w) {
		width = w;
		support.firePropertyChange("width", null, width);
	}

	public double getPaneWidth() {
		return paneWidth;
	}

	public void setPaneWidth(int w) {
		paneWidth = w;
		unsized = false;
		support.firePropertyChange("paneWidth", null, paneWidth);
	}

	public double getPaneHeight() {
		return paneHeight;
	}

	public void setPaneHeight(int h) {
		paneHeight = h;
		unsized = false;
		support.firePropertyChange("paneHeight", null, paneHeight);
	}

	public OutputSet[] getSets() {
		return sets;
	}

	public boolean isUnsized() {
		return unsized;
	}

	public void resetZoom() {
		setWidth(paneWidth / 200);
		setHeight(paneHeight / 200);
		setX(-width / 2);
		setY(-height / 2);
		parentFrame.repaint();
	}

	public void zoomIn() {
		x = x + width / 2;
		y = y + height / 2;
		setWidth(width / ZOOM_CONSTANT);
		setHeight(height / ZOOM_CONSTANT);
		setX(x - width / 2);
		setY(y - height / 2);
		parentFrame.repaint();
	}

	public void zoomOut() {
		x = x + width / 2;
		y = y + height / 2;
		setWidth(width * ZOOM_CONSTANT);
		setHeight(height * ZOOM_CONSTANT);
		setX(x - width / 2);
		setY(y - height / 2);
		parentFrame.repaint();
	}

	public void enable() {
		enabled = true;
		JList outList = parentFrame.getOutputSetList();
		DefaultListModel model = (DefaultListModel) outList.getModel();
		if (sets != null) {
			int[] indices = new int[sets.length];
			for (int i = 0; i < sets.length; i++) {
				indices[i] = model.indexOf(sets[i]);
				if (indices[i] == -1)
					sets[i] = null;
			}
			outList.setSelectedIndices(indices);
			for (int j = 0; j < sets.length; j++)
				if (sets[j] != null)
					sets[j].addListener(this);
		} else
			outList.clearSelection();
		outList.addListSelectionListener(this);
	}

	public void disable() {
		enabled = false;
		parentFrame.getOutputSetList().removeListSelectionListener(this);
		if (sets != null)
			for (int j = 0; j < sets.length; j++)
				if (sets[j] != null)
					sets[j].removeListener(this);
	}

	public void mouseWheelMoved(MouseWheelEvent event) {
		if (!enabled)
			return;
		int direction = event.getWheelRotation();
		// + is scrolling towards the user
		// - is scrolling away from the user
		if (direction < 0) {
			zoomIn();
		} else if (direction > 0) {
			zoomOut();
		}
	}

	public void valueChanged(ListSelectionEvent e) {
		JList list = (JList) e.getSource();
		if (sets != null)
			for (int j = 0; j < sets.length; j++)
				sets[j].removeListener(this);
		Object[] objArray = list.getSelectedValues();
		sets = new OutputSet[objArray.length];
		for (int i = 0; i < objArray.length; i++) {
			OutputSet set = (OutputSet) objArray[i];
			sets[i] = set;
			set.addListener(this);
		}
		support.firePropertyChange("sets", null, sets);
		parentFrame.repaint();
	}

	public void propertyChange(PropertyChangeEvent event) {
		String name = event.getPropertyName();
		if (name.equals("Color")) {
			parentFrame.repaint();
			support.firePropertyChange("sets", null, sets);
		} else if (name.equals("dotSize")) {
			parentFrame.repaint();
		}
	}

	public void keyPressed(KeyEvent arg0) {
		int code = arg0.getKeyCode();
		double xMove, yMove;
		switch (code) {
		case KeyEvent.VK_DOWN:
		case KeyEvent.VK_KP_DOWN:
		case KeyEvent.VK_D:
			yMove = height / 10;
			setY(y - yMove);
			break;
		case KeyEvent.VK_RIGHT:
		case KeyEvent.VK_KP_RIGHT:
		case KeyEvent.VK_R:
			xMove = width / 10;
			setX(x + xMove);
			break;
		case KeyEvent.VK_LEFT:
		case KeyEvent.VK_KP_LEFT:
		case KeyEvent.VK_L:
			xMove = width / 10;
			setX(x - xMove);
			break;
		case KeyEvent.VK_UP:
		case KeyEvent.VK_KP_UP:
		case KeyEvent.VK_U:
			yMove = height / 10;
			setY(y + yMove);
		}
		parentFrame.repaint();
	}

	public void keyReleased(KeyEvent e) {
	}

	public void keyTyped(KeyEvent e) {
	}

	public void mouseDragged(MouseEvent e) {
		if (!dragInProgress)
			return;
		double newX = (e.getX() * width / paneWidth) + x;
		double newY = ((paneHeight - e.getY()) * height / paneHeight) + y;
		setX(x - newX + dragX);
		setY(y - newY + dragY);
		parentFrame.repaint();
	}

	public void mouseMoved(MouseEvent e) {
	}

	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2) {
			double newX = (e.getX() * width / paneWidth) + x;
			double newY = ((paneHeight - e.getY()) * height / paneHeight) + y;
			setX(newX - width / 2);
			setY(newY - height / 2);
			parentFrame.repaint();
		}
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
		dragX = (e.getX() * width / paneWidth) + x;
		dragY = ((paneHeight - e.getY()) * height / paneHeight) + y;
		dragInProgress = true;
	}

	public void mouseReleased(MouseEvent e) {
		dragX = 0;
		dragY = 0;
		dragInProgress = false;
	}
}
