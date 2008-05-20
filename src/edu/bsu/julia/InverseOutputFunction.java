package edu.bsu.julia;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

public class InverseOutputFunction extends OutputFunction {
	
	private OutputFunction[] outputFunctions;

	public InverseOutputFunction(Session s, InputFunction[] i, 
			OutputFunction[] o, int type, ComplexNumber[] p) {
		super(s, i, type, p);
		outputFunctions = o;
	}
	
	public OutputFunction[] getOutputFunctions() {
		return outputFunctions;
	}
	
	public String toString() {
		String s = "o" + getSubscript() + " = ";
		if(super.getFunctionType() == OutputFunction.INVERSE_ATTR)
			s= s + "Forward Image of ";
		else if(super.getFunctionType() == OutputFunction.INVERSE_ERGODIC_JULIA)
			s = s + "Inverse Random Julia Set of ";
		else if(super.getFunctionType() == OutputFunction.INVERSE_FULL_JULIA)
			s = s + "Inverse Full Julia Set of ";
		for(int x = 0; x<outputFunctions.length; x++) {
			s = s + "g" + outputFunctions[x].getSubscript();
			if(x!=(outputFunctions.length - 1)) s = s + ", ";
		}
		s = s + " using ";
		InputFunction[] inList = getInputFunctions();
		for(int x = 0; x<inList.length; x++) {
			s = s + "f" + inList[x].getSubscript();
			if(x!=(inList.length - 1)) s = s + ", ";
		}
		return s;
	}
	
	public boolean writeToFile(File f) {
		FileOutputStream out;
		PrintStream ps;
		
		try {
            out = new FileOutputStream(f);
            ps = new PrintStream( out );
            for(int i = 0; i<getPoints().length; i++) ps.println(getPoints()[i]);
            ps.println(getIterations());
            ps.println(getSkips());
            ps.println(getSeedValue());
            ps.println(getFunctionType());
            InputFunction[] functions = getInputFunctions();
            for(int j = 0; j<functions.length; j++){
            	ps.println(functions[j].getType());
            	ps.println(functions[j].getM());
            	ComplexNumber[] coefficients = functions[j].getCoefficients();
            	for(int k = 0; k<coefficients.length; k++) 
            		ps.println(coefficients[k]);
            }
            ps.close();
		}
		catch (IOException e) {
			System.err.println(e);
			return false;
		}
		return true;	
	}

}
