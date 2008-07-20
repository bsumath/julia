package edu.bsu.julia.gui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker.StateValue;
import javax.swing.filechooser.FileFilter;

import edu.bsu.julia.Julia;
import edu.bsu.julia.session.SessionFileExporter;

public class SaveSessionAction extends AbstractAction {
	// for serializable interface: do not use
	public static final long serialVersionUID = 0;
	public static final String FILE_EXTENSION = ".julia.zip";

	private Julia parentFrame;
	private File file;
	private boolean saveAsMode = false;

	public SaveSessionAction(Julia f, boolean alwaysPrompt) {
		super(alwaysPrompt ? "Save Session As..." : "Save Session",
				new ImageIcon(Thread.currentThread().getContextClassLoader()
						.getResource("sessionsave.png")));
		if (alwaysPrompt)
			putValue("SHORT_DESCRIPTION", "Save Session As ...");
		else
			putValue("SHORT_DESCRIPTION", "Save Session");
		putValue("LONG_DESCRIPTION", "Save the current session to disk.");
		parentFrame = f;

		saveAsMode = alwaysPrompt;
	}

	public void actionPerformed(ActionEvent arg0) {
		parentFrame.loseFocus();

		if (!parentFrame.getCurrentSession().isModified() && !saveAsMode)
			return;

		file = parentFrame.getCurrentSession().getFile();
		if (file == null || saveAsMode) {
			JFileChooser filechooser = new JFileChooser();
			filechooser.setFileFilter(new JuliaFileFilter());

			String path = parentFrame.getFilePath();
			if (!path.equals(""))
				filechooser.setCurrentDirectory(new File(path));
			int result = filechooser.showSaveDialog(parentFrame);
			if (result != JFileChooser.APPROVE_OPTION) {
				return;
			}
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
		}

		// check for saving legacy files and rename
		if (file.getName().endsWith("julia")) {
			file = new File(file.getAbsolutePath() + ".zip");
		}

		// create a dialog window and progress bar
		final JDialog dialog = new JDialog(parentFrame, "Saving File ...", true);
		final JProgressBar bar = new JProgressBar(0, 100);

		// create an exporter to save the file
		final SessionFileExporter exporter = new SessionFileExporter(file);
		parentFrame.getCurrentSession().export(exporter);
		exporter.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if ("progress".equals(evt.getPropertyName())) {
					bar.setValue((Integer) evt.getNewValue());
				} else if ("state".equals(evt.getPropertyName())
						&& (StateValue) evt.getNewValue() == StateValue.DONE) {
					boolean result = false;
					try {
						result = exporter.get();
					} catch (Exception e) {
						e.printStackTrace();
					}

					if (result) {
						parentFrame.getCurrentSession().markUnmodified();
						parentFrame.getCurrentSession().setFile(file);
					} else {
						JOptionPane.showMessageDialog(parentFrame,
								"Error Saving Session", "Error Saving Session",
								JOptionPane.ERROR_MESSAGE);
					}

					dialog.setVisible(false);
					dialog.dispose();
				}
			}
		});

		// add the progress bar to the dialog and start saving the file
		bar.setValue(0);
		dialog.add(bar);
		dialog.pack();
		dialog.addWindowStateListener(new WindowStateListener() {
			@Override
			public void windowStateChanged(WindowEvent e) {
				if (e.getNewState() == WindowEvent.WINDOW_CLOSING) {
					exporter.cancel(true);
					dialog.setVisible(false);
					dialog.dispose();
				}
			}
		});
		exporter.execute();
		dialog.setVisible(true);
	}

	private final class JuliaFileFilter extends FileFilter {
		public boolean accept(File file) {
			return file.getName().toLowerCase().endsWith(FILE_EXTENSION)
					|| file.isDirectory();
		}

		public String getDescription() {
			return "Julia files (*" + FILE_EXTENSION + ")";
		}
	}
}
