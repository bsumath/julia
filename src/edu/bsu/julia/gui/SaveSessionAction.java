package edu.bsu.julia.gui;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import edu.bsu.julia.Julia;

public class SaveSessionAction extends AbstractAction {
	private Julia parentFrame;
	private File file = null;
	// for serializable interface: do not use
	public static final long serialVersionUID = 0;

	public SaveSessionAction(Julia f) {
		super("Save Session", new ImageIcon(Thread.currentThread()
				.getContextClassLoader().getResource("sessionsave.png")));
		putValue("SHORT_DESCRIPTION", "Save Session");
		putValue("LONG_DESCRIPTION", "Save the current session to disk.");
		parentFrame = f;
	}

	public void actionPerformed(ActionEvent arg0) {
		parentFrame.loseFocus();
		
		if (!parentFrame.getCurrentSession().isModified())
			return;
		if (!saveFile()) {
			JOptionPane.showMessageDialog(null, "IO error in saving file!!",
					"File Save Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	public boolean saveFile() {
		JFileChooser filechooser = new JFileChooser();
		filechooser.setFileFilter(new FileFilter() {
			public boolean accept(File file) {
				return file.getName().toLowerCase().endsWith(".julia")
						|| file.isDirectory();
			}

			public String getDescription() {
				return "Julia files (*.julia)";
			}
		});

		String path = parentFrame.getFilePath();
		if (!path.equals(""))
			filechooser.setCurrentDirectory(new File(path));
		int result = filechooser.showSaveDialog(parentFrame);
		if (result == JFileChooser.CANCEL_OPTION) {
			return true;
		} else if (result == JFileChooser.APPROVE_OPTION) {
			file = filechooser.getSelectedFile();
			if (!(file.getName().endsWith(".julia"))) {
				file = new File(file.getAbsolutePath() + ".julia");
			}
			if (file.exists()) {
				int response = JOptionPane.showConfirmDialog(null,
						"Overwrite existing file?", "Confirm Overwrite",
						JOptionPane.OK_CANCEL_OPTION,
						JOptionPane.QUESTION_MESSAGE);
				if (response == JOptionPane.CANCEL_OPTION)
					return false;
			}
			parentFrame.setFilePath(file.getAbsolutePath());
			return parentFrame.getCurrentSession().writeToFile(file);
		} else {
			return false;
		}
	}

}
