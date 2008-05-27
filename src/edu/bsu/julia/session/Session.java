package edu.bsu.julia.session;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Vector;

import edu.bsu.julia.ComplexNumber;
import edu.bsu.julia.CubicInputFunction;
import edu.bsu.julia.InputFunction;
import edu.bsu.julia.LinearInputFunction;
import edu.bsu.julia.MobiusInputFunction;
import edu.bsu.julia.OutputFunction;
import edu.bsu.julia.QuadraticInputFunction;
import edu.bsu.julia.RealAfflineLinearInputFunction;

public class Session {
	
	private PropertyChangeSupport support = 
		new PropertyChangeSupport(this);
	private int iterations;
	private int skips;
	private ComplexNumber seed;
	private Vector<InputFunction> inputFunctions;
	private Vector<OutputFunction> outputFunctions;
	private int inputsubscript;
	private int outputsubscript;
	private boolean modified;
	
	public Session() {
		this(50000, 20, new ComplexNumber(1,0));
		modified = false;
	}
	
	public Session(int iter, int sk, ComplexNumber seedValue) throws
		IllegalArgumentException {
		if (iter<=0)throw new IllegalArgumentException("Points to Plot must be" +
				"\na positive number.");
		if (sk<0)throw new IllegalArgumentException("Skips must be" +
				"\na positive number.");
		if (sk>=iter)throw new IllegalArgumentException("Skips must be less" +
				" than Points to Plot.");
		iterations = iter;
		skips = sk;
		seed = seedValue;
		inputFunctions = new Vector<InputFunction>();
		outputFunctions = new Vector<OutputFunction>();
		inputsubscript = 0;
		outputsubscript = 0;
		modified = true;
	}
	
	public int getIterations() {
		return iterations;
	}
	
	public void setIterations(int iter) throws IllegalArgumentException {
		modified = true;
		if (iter<=0)throw new IllegalArgumentException("Iterations must be" +
			"\na positive number.");
		iterations = iter;
		support.firePropertyChange("iterations", null, iterations);
	}
	
	public int getSkips() {
		return skips;
	}
	
	public void setSkips(int sk) throws IllegalArgumentException {
		modified = true;
		if (sk<0)throw new IllegalArgumentException("Skips must be" +
			"\na positive number.");
		skips = sk;
		support.firePropertyChange("skips", null, skips);
	}
	
	public ComplexNumber getSeedValue() {
		return seed;
	}

	public void setSeedValue(ComplexNumber seedValue) {
		modified = true;
		seed = seedValue;
		support.firePropertyChange("seed", null, seed);
	}
	
	public Vector<InputFunction> getInputFunctions() {
		return inputFunctions;
	}
	
	public void replaceInputFunction(InputFunction oldFn, InputFunction newFn) {
		modified = true;
		inputFunctions.set(inputFunctions.indexOf(oldFn), newFn);
		newFn.setSubscript(getNextInSubscript());
		support.firePropertyChange("replaceInputFunction", oldFn, newFn);
	}
	
	public void addInputFunction(InputFunction fn) {
		modified = true;
		inputFunctions.add(fn);
		fn.setSubscript(getNextInSubscript());
		support.firePropertyChange("addInputFunction", null, fn);
	}
	
	public void deleteInputFunction(int fn) {
		modified = true;
		inputFunctions.remove(fn);
		support.firePropertyChange("deleteInputFunction", null, fn);
	}
	
	public Vector<OutputFunction> getOutputFunctions() {
		return outputFunctions;
	}
	
	public void addOutputFunction(OutputFunction fn) {
		modified = true;
		outputFunctions.add(fn);
		fn.setSubscript(getNextOutSubscript());
		support.firePropertyChange("addOutputFunction", null, fn);
	}
	
	public void deleteOutputFunction(int fn) {
		modified = true;
		outputFunctions.remove(fn);
		support.firePropertyChange("deleteOutputFunction", null, fn);
	}
	
	public void addListener(PropertyChangeListener list) {
		support.addPropertyChangeListener(list);
	}
	
	public int getNextInSubscript() {
		return ++inputsubscript;
	}
	
	public int getNextOutSubscript() {
		return ++outputsubscript;
	}
	
	public boolean writeToFile(File f) {
		FileOutputStream out;
		PrintStream ps;
		try {
            out = new FileOutputStream(f);
            ps = new PrintStream( out );
            
            ps.println(iterations);
            ps.println(skips);
            ps.println(seed.getX());
            ps.println(seed.getY());
            ps.println(inputFunctions.size());
            
            for(int i = 0; i<inputFunctions.size(); i++) {
            	if(inputFunctions.elementAt(i).getClass().getName().equals
            			(edu.bsu.julia.LinearInputFunction.class.getName())) {
            		LinearInputFunction lFn = (LinearInputFunction)
            			inputFunctions.elementAt(i);
            		ps.println("linear");
            		ps.println(lFn.getM());
            		ComplexNumber[] variables = lFn.getCoefficients();
            		for(int j = 0; j<variables.length; j++) {
            			ps.println(variables[j].getX());
            			ps.println(variables[j].getY());
            		}
            	}
            	else if(inputFunctions.elementAt(i).getClass().getName().equals
            			(edu.bsu.julia.CubicInputFunction.class.getName())) {
            		CubicInputFunction cFn = (CubicInputFunction)
            			inputFunctions.elementAt(i);
            		ps.println("cubic");
            		ps.println(cFn.getM());
            		ComplexNumber[] variables = cFn.getCoefficients();
            		for(int j = 0; j<variables.length; j++) {
            			ps.println(variables[j].getX());
            			ps.println(variables[j].getY());
            		}
            	}
            	else if(inputFunctions.elementAt(i).getClass().getName().equals
            			(edu.bsu.julia.RealAfflineLinearInputFunction.class.getName())) {
            		RealAfflineLinearInputFunction mFn = (RealAfflineLinearInputFunction)
            			inputFunctions.elementAt(i);
            		ps.println("matrix");
            		ps.println(mFn.getM());
            		ComplexNumber[] variables = mFn.getCoefficients();
            		for(int j = 0; j<variables.length; j++) {
            			ps.println(variables[j].getX());
            			ps.println(variables[j].getY());
            		}
            	}
            	else if(inputFunctions.elementAt(i).getClass().getName().equals
            			(edu.bsu.julia.MobiusInputFunction.class.getName())) {
            		MobiusInputFunction bFn = (MobiusInputFunction)
            			inputFunctions.elementAt(i);
            		ps.println("mobius");
            		ps.println(bFn.getM());
            		ComplexNumber[] variables = bFn.getCoefficients();
            		for(int j = 0; j<variables.length; j++) {
            			ps.println(variables[j].getX());
            			ps.println(variables[j].getY());
            		}
            	}
            	else if(inputFunctions.elementAt(i).getClass().getName().equals
            			(edu.bsu.julia.QuadraticInputFunction.class.getName())) {
            		QuadraticInputFunction qFn = (QuadraticInputFunction)
            			inputFunctions.elementAt(i);
            		ps.println("quad");
            		ps.println(qFn.getM());
            		ComplexNumber[] variables = qFn.getCoefficients();
            		for(int j = 0; j<variables.length; j++) {
            			ps.println(variables[j].getX());
            			ps.println(variables[j].getY());
            		}
            	}
            }
            ps.close();
		}
		catch (IOException e) {
			System.err.println(e);
			return false;
		}
		modified = false;
		return true;	
	}

	public boolean isModified() {
		return modified;
	}

	public void markUnmodified() {
		modified = false;
	}
}
