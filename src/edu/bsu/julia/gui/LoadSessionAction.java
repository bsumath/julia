package edu.bsu.julia.gui;

import java.awt.event.ActionEvent;
import java.io.*;
import javax.swing.*;
import edu.bsu.julia.*;


public class LoadSessionAction extends AbstractAction {
	
	private Julia parentFrame;
	JuliaFileFilter juliafilefilter = new JuliaFileFilter();
	//for serializable interface: do not use
	public static final long serialVersionUID = 0;
	
	public LoadSessionAction(Julia f) {
		super("Open Session", new ImageIcon
				(Thread.currentThread().getContextClassLoader().getResource
						("loadSession.png")));
		putValue("SHORT_DESCRIPTION", "Load a New Session");
		putValue("LONG_DESCRIPTION", "Load a new session.  Data from the current " +
				"session will be lost.");
		parentFrame = f;
	}

	public void actionPerformed(ActionEvent arg0) {
		//incorporate SaveSessionDialog here
		
		File file;
		
		JFileChooser chooser = new JFileChooser();
		String path = parentFrame.getFilePath();
		if(!path.equals(""))chooser.setCurrentDirectory(new File(path));
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setFileFilter(juliafilefilter);
		int result = chooser.showOpenDialog(parentFrame);
		
		if(result == JFileChooser.CANCEL_OPTION) return;
		file = chooser.getSelectedFile();
		
		if(file == null || file.getName().equals("")) JOptionPane.showMessageDialog
			(parentFrame, "Invalid File Name", "Invalid File Name", JOptionPane.ERROR_MESSAGE);
		
		parentFrame.loadSession(file);
		parentFrame.setFilePath(file.getAbsolutePath());
	}

	private class JuliaFileFilter extends javax.swing.filechooser.FileFilter
	{
		public boolean accept(File file) {
			return file.getName().toLowerCase().endsWith(".julia") || file.isDirectory();
		}
		public String getDescription() {
			return "Julia files (*.julia)";
		}
	}

}
