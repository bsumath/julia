package edu.bsu.julia.gui.actions;

import java.awt.event.*;
import java.io.File;
import edu.bsu.julia.*;

import javax.swing.*;

public class SaveSetAction extends AbstractAction {
	
	private JSetFileFilter jsetFileFilter = new JSetFileFilter();
	private Julia parentFrame;
	private File file = null;
	private DefaultListModel outputList;
	private int index;
	//for serializable interface: do not use
	public static final long serialVersionUID = 0;
	
	public SaveSetAction(Julia f, DefaultListModel listModel, int i) {
		super("Save Output Set");
		putValue("SHORT_DESCRIPTION", "Save Output Set");
		putValue("LONG_DESCRIPTION", "Save this output set to a file.");
		parentFrame = f;
		outputList = listModel;
		index = i;
	}

	public void actionPerformed(ActionEvent arg0) {
		boolean status = false;
		status = saveFile();
        if (!status) {
            JOptionPane.showMessageDialog (null,"IO error in saving file!!",
            		"File Save Error", JOptionPane.ERROR_MESSAGE );
        }
	}
	
	public boolean saveFile(){
		JFileChooser filechooser = new JFileChooser();
		filechooser.setFileFilter(jsetFileFilter);
		String path = parentFrame.getFilePath();
		if(!path.equals(""))filechooser.setCurrentDirectory(new File(path));
		int result = filechooser.showSaveDialog(parentFrame);
		if(result == JFileChooser.CANCEL_OPTION){
			return true;
		}
		else if(result == JFileChooser.APPROVE_OPTION){
			file = filechooser.getSelectedFile();
			if(!(file.getName().endsWith(".jset"))){
				file = new File(file.getAbsolutePath() + ".jset");
			}
			if(file.exists()){
				int response = JOptionPane.showConfirmDialog (null,
						"Overwrite existing file?","Confirm Overwrite",
			            JOptionPane.OK_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE);
				if (response == JOptionPane.CANCEL_OPTION) return false;
			}
			parentFrame.setFilePath(file.getAbsolutePath());
			OutputFunction fn = (OutputFunction)outputList.getElementAt(index);
			return fn.writeToFile(file);
		}
		else {
			return false;
		}
	}

	private class JSetFileFilter extends javax.swing.filechooser.FileFilter
	{
		public boolean accept(File file) {
			return file.getName().toLowerCase().endsWith(".jset") 
			|| file.isDirectory();
		}
		public String getDescription() {
			return "Julia Output Set (*.jset)";
		}
	}
	
}
