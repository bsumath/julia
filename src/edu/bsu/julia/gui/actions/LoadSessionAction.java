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
import javax.swing.SwingWorker;
import javax.swing.SwingWorker.StateValue;
import javax.swing.filechooser.FileFilter;

import edu.bsu.julia.Julia;
import edu.bsu.julia.gui.SaveSessionDialog;
import edu.bsu.julia.session.LegacyFileImporter;
import edu.bsu.julia.session.Session;
import edu.bsu.julia.session.SessionFileImporter;
import edu.bsu.julia.session.Session.InvalidSessionParametersException;

public class LoadSessionAction extends AbstractAction {

	private Julia parentFrame;
	JuliaFileFilter juliafilefilter = new JuliaFileFilter();
	private File file;
	// for serializable interface: do not use
	public static final long serialVersionUID = 0;

	private static final String FILE_EXTENSION = ".julia.zip";
	private static final String LEGACY_EXTENSION = ".julia";

	public LoadSessionAction(Julia f) {
		super("Open Session", new ImageIcon(Thread.currentThread()
				.getContextClassLoader().getResource("loadSession.png")));
		putValue("SHORT_DESCRIPTION", "Load a New Session");
		putValue("LONG_DESCRIPTION",
				"Load a new session.  Data from the current "
						+ "session will be lost.");
		parentFrame = f;
	}

	public void actionPerformed(ActionEvent arg0) {
		parentFrame.loseFocus();

		// check to see if the current session should be saved
		if (parentFrame.getCurrentSession().isModified()) {
			SaveSessionDialog saveDialog = new SaveSessionDialog(parentFrame);
			if (saveDialog.showSaveDialog() == SaveSessionDialog.CANCELED)
				return;
		}

		JFileChooser chooser = new JFileChooser();
		String path = parentFrame.getFilePath();
		if (!path.equals(""))
			chooser.setCurrentDirectory(new File(path));
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setFileFilter(juliafilefilter);
		int result = chooser.showOpenDialog(parentFrame);

		if (result == JFileChooser.CANCEL_OPTION)
			return;
		file = chooser.getSelectedFile();

		if (file == null || file.getName().equals(""))
			JOptionPane.showMessageDialog(parentFrame, "Invalid File Name",
					"Invalid File Name", JOptionPane.ERROR_MESSAGE);

		// create a dialog window and progress bar
		final JDialog dialog = new JDialog(parentFrame, "Loading File ...",
				true);
		final JProgressBar bar = new JProgressBar(0, 100);

		// create an importer to load the file
		final SwingWorker<Boolean, Void> importer = (file.getName()
				.endsWith(FILE_EXTENSION)) ? new SessionFileImporter(file)
				: new LegacyFileImporter(file);
		importer.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if ("progress".equals(evt.getPropertyName())) {
					bar.setValue((Integer) evt.getNewValue());
				} else if ("state".equals(evt.getPropertyName())
						&& (StateValue) evt.getNewValue() == StateValue.DONE) {
					boolean result = false;
					try {
						result = importer.get();
					} catch (Exception e) {
						e.printStackTrace();
					}

					if (result) {
						try {
							parentFrame.setCurrentSession(new Session(
									parentFrame, (Session.Importer) importer));
						} catch (InvalidSessionParametersException e) {
							JOptionPane.showMessageDialog(parentFrame,
									"Error Reading File", "Error Reading File",
									JOptionPane.ERROR_MESSAGE);
							e.printStackTrace();
						}
						parentFrame.getCurrentSession().setFile(file);
						parentFrame.setFilePath(file.getAbsolutePath());
					} else {
						JOptionPane.showMessageDialog(parentFrame,
								"Error Reading File", "Error Reading File",
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
					importer.cancel(true);
					dialog.setVisible(false);
					dialog.dispose();
				}
			}
		});
		importer.execute();
		dialog.setVisible(true);
	}

	private final class JuliaFileFilter extends FileFilter {
		public boolean accept(File file) {
			return file.getName().toLowerCase().endsWith(LEGACY_EXTENSION)
					|| file.getName().toLowerCase().endsWith(FILE_EXTENSION)
					|| file.isDirectory();
		}

		public String getDescription() {
			return "Julia files (*" + LEGACY_EXTENSION + ", *" + FILE_EXTENSION
					+ ")";
		}
	}

}
