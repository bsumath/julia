package edu.bsu.julia.gui.actions;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import edu.bsu.julia.Julia;

public class SaveImageAction extends AbstractAction {

	private Julia parentFrame;
	File file;
	Robot robot;
	// for serializable interface: do not use
	public static final long serialVersionUID = 0;

	public SaveImageAction(Julia f) {
		super("Save Image", new ImageIcon(Thread.currentThread()
				.getContextClassLoader().getResource("saveImage.png")));
		putValue("SHORT_DESCRIPTION", "Save Image");
		putValue("LONG_DESCRIPTION", "Save the graph to a .jpeg file");
		parentFrame = f;

	}

	public void actionPerformed(ActionEvent event) {
		saveFile();
		int x = parentFrame.getTabbedPane().getActivePane().getWidth();
		int y = parentFrame.getTabbedPane().getActivePane().getHeight();
		BufferedImage bi = new BufferedImage(x, y, BufferedImage.TYPE_INT_RGB);
		System.out.println(x);
		System.out.println(y);
		Rectangle r = new Rectangle(parentFrame.getTabbedPane().getActivePane()
				.getLocationOnScreen(), new Dimension(x, y));
		try {
			Robot robot = new Robot();
			bi = robot.createScreenCapture(r);
		} catch (AWTException awte) {
			System.err.println(awte);
		}
		try {
			ImageIO.write(bi, "jpg", file);
		} catch (Exception e) {
			System.err.println(e);
		}
	}

	public boolean saveFile() {
		System.out.println("hello2");
		file = null;
		JFileChooser filechooser = new JFileChooser();
		filechooser.setSelectedFile(file);
		int result = filechooser.showSaveDialog(null);
		if (result == JFileChooser.CANCEL_OPTION) {
			return false;
		} else if (result == JFileChooser.APPROVE_OPTION) {
			file = filechooser.getSelectedFile();
			if (file.exists()) {
				int response = JOptionPane.showConfirmDialog(null,
						"Overwrite existing file?", "Confirm overwrite",
						JOptionPane.OK_CANCEL_OPTION,
						JOptionPane.QUESTION_MESSAGE);
				if (response == JOptionPane.CANCEL_OPTION)
					return false;
			}
		}
		return true;
	}
}
