package edu.bsu.julia.gui.actions;

import java.awt.event.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Vector;
import edu.bsu.julia.*;
import javax.swing.*;


public class SaveOutputFunctionAction extends AbstractAction {
	
	private Julia parentFrame;
	private static Vector<OutputFunction> func;
	File file;
	//for serializable interface: do not use
	public static final long serialVersionUID = 0;
	
	public SaveOutputFunctionAction (Julia f) {
		super("Save Output Sets");
		putValue("SHORT_DESCRIPTION", "Save Points");
		putValue("LONG_DESCRIPTION", "Save points of all the output functions");
		parentFrame = f;
	}
	
	public void actionPerformed(ActionEvent event) { 
		boolean status = false;
		status = saveFile ();
        if (!status) {
            JOptionPane.showMessageDialog (null,"IO error in saving file!!",
            		"File Save Error", JOptionPane.ERROR_MESSAGE );
        }				
		Session s = parentFrame.getCurrentSession();
			FileOutputStream out; 
		    PrintStream ps; 
		    func = new Vector<OutputFunction>();
		    func = s.getOutputFunctions();
		    
		    	try {
		            out = new FileOutputStream(file);
		            ps = new PrintStream( out );


		            ps.println (s.getIterations());
				    ps.println (s.getSkips());
				    ps.println (s.getSeedValue());
				    ps.println();
				    
				    
			    	for (int j=0; j<s.getOutputFunctions().size(); j++) {
			    		
			    		if(j<s.getOutputFunctions().size()){
			    			InputFunction in[] = func.elementAt(j).getInputFunctions();
			    			for(int k=0; k<in.length; k++){
			    				ps.println(in[k].getM());
			    				ComplexNumber var[] = in[k].getCoefficients();
			    				ps.println(var.length);
			    				for (int m=0; m<var.length; m++)
			    				ps.println(var[m]);
			    			}
			    		}
			    		else{
			    			ps.println("Composite");
			    		}
			    		ComplexNumber pts[] = func.elementAt(j).getPoints();
			    		ps.print("{");
			    		for(int n=0; n<pts.length; n++)
			    			ps.print(pts[n]);
			    		ps.print("}");
			    		ps.println();
			    		ps.println();
			    	}
	                ps.close();
	            }
	            catch (Exception e) {
		            System.err.println (e.toString());
		        }
	}
	public boolean saveFile(){
		System.out.println("hello2");
		file = null;
		JFileChooser filechooser = new JFileChooser();
		filechooser.setSelectedFile(file);
		int result = filechooser.showSaveDialog(null);
		if(result == JFileChooser.CANCEL_OPTION){
			return false;
		}
		else if(result == JFileChooser.APPROVE_OPTION){
			file = filechooser.getSelectedFile();
			if(file.exists()){
				int response = JOptionPane.showConfirmDialog (null,"Overwrite existing file?","Confirm overwrite",
			                JOptionPane.OK_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE);
				if (response == JOptionPane.CANCEL_OPTION) return false;
				}
			}
		return true;
	}
}