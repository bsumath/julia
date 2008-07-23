package edu.bsu.julia.gui.actions;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import edu.bsu.julia.Julia;
import edu.bsu.julia.output.OutputSet;

public class SaveSetAction extends AbstractAction {

	private DatFileFilter jsetFileFilter = new DatFileFilter();
	private Julia parentFrame;
	private File file = null;
	private OutputSet set;

	// for serializable interface: do not use
	public static final long serialVersionUID = 0;

	private static final String FILE_EXTENSION = ".dat";

	public SaveSetAction(Julia f, OutputSet s) {
		super("Save Output Set");
		putValue("SHORT_DESCRIPTION", "Save Output Set");
		putValue("LONG_DESCRIPTION", "Save this output set to a file.");

		parentFrame = f;
		set = s;
	}

	public void actionPerformed(ActionEvent arg0) {
		JFileChooser filechooser = new JFileChooser();
		filechooser.setFileFilter(jsetFileFilter);

		String path = parentFrame.getFilePath();
		if (!path.equals(""))
			filechooser.setCurrentDirectory(new File(path));

		switch (filechooser.showSaveDialog(parentFrame)) {
		case JFileChooser.CANCEL_OPTION:
			return;
		case JFileChooser.APPROVE_OPTION:
			file = filechooser.getSelectedFile();
			if (!(file.getName().endsWith(FILE_EXTENSION))) {
				file = new File(file.getAbsolutePath() + FILE_EXTENSION);
			}
			if (file.exists()) {
				int response = JOptionPane.showConfirmDialog(null,
						"Overwrite existing file?", "Confirm Overwrite",
						JOptionPane.OK_CANCEL_OPTION,
						JOptionPane.QUESTION_MESSAGE);
				if (response == JOptionPane.CANCEL_OPTION)
					return;
			}
			parentFrame.setFilePath(file.getAbsolutePath());

			File datFile = set.getFiles()[1];

			try {
				FileInputStream in = new FileInputStream(datFile);
				FileOutputStream out = new FileOutputStream(file);
				byte[] buf = new byte[1024];
				int len = 0;
				while ((len = in.read(buf)) != -1) {
					out.write(buf, 0, len);
				}
				in.close();
				out.close();
				return;
			} catch (IOException e) {
				e.printStackTrace();
				// since there is no break or return, this will show the error
				// message for the default case
			}
		default:
			JOptionPane.showMessageDialog(null, "IO error in saving file!!",
					"File Save Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private class DatFileFilter extends FileFilter {
		public boolean accept(File file) {
			return file.getName().toLowerCase().endsWith(FILE_EXTENSION)
					|| file.isDirectory();
		}

		public String getDescription() {
			return "Data File (*" + FILE_EXTENSION + ")";
		}
	}

}
