package edu.bsu.julia;

import java.awt.Color;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import edu.bsu.julia.session.Session;

public class OutputFunction {
	
	public static final int BASIC = -1;
	public static final int FULL_JULIA = 0;
	public static final int ERGODIC_JULIA = 1;
	public static final int FULL_ATTR = 2;
	public static final int ERGODIC_ATTR = 3;
	public static final int INVERSE_ATTR = 4;
	public static final int INVERSE_ERGODIC_JULIA = 5;
	public static final int INVERSE_FULL_JULIA = 6;
	public static final int POST_CRITICAL = 7;
	private int sub = 0;
	private int iterations;
	private int skips;
	private ComplexNumber seed;
	private int functionType;
	private InputFunction[] inputFunctions;
	private ComplexNumber[] points;
	private Color c;
	private final static Color[] colorSet = {Color.BLACK, 
		Color.BLUE, Color.RED, Color.DARK_GRAY, 
		Color.GREEN, Color.ORANGE, Color.MAGENTA};
	private static int colorIndex = 0;
	private PropertyChangeSupport support = 
		new PropertyChangeSupport(this);
	
	public OutputFunction(Session s, InputFunction[] i,
			int type, ComplexNumber[] p) {
		//*********************new line
		iterations = s.getIterations();
		//************************
		skips = s.getSkips();
		seed = s.getSeedValue();
		functionType = type;
		if(functionType==OutputFunction.POST_CRITICAL) skips = 0;
		//iterations += skips;
		inputFunctions = i;
		points = p;
		c = getNextColor();
	}
	
	public OutputFunction(int sk, ComplexNumber sd, int type,
			ComplexNumber[] p) {
		iterations = p.length;
		skips = sk;
		seed = sd;
		functionType = type;
		if(functionType==OutputFunction.POST_CRITICAL) skips = 0;
		iterations += skips;
		points = p;
		c = getNextColor();
	}
	
	public int getSubscript() {
		return sub;
	}
	
	public void setSubscript(int subscript) {
		sub = subscript;
	}
	
	public int getIterations() {
		return iterations;
	}
	
	public int getSkips() {
		return skips;
	}
	
	public ComplexNumber getSeedValue() {
		return seed;
	}
	
	public int getNumOfPoints(){
		return points.length;
	}
	
	public Color getColor() {
		return c;
	}
	
	public int getFunctionType() {
		return functionType;
	}
	
	public static Color getNextColor() {
		Color color = colorSet[colorIndex];
		if (colorIndex<colorSet.length-1) colorIndex++;
		else colorIndex = 0;
		return color;
	}
	
	public void setColor(Color newColor) {
		c = newColor;
		support.firePropertyChange("Color", null, newColor);
	}
	
	public InputFunction[] getInputFunctions() {
		return inputFunctions;
	}
	
	public ComplexNumber[] getPoints() {
		return points;
	}
	
	public void addListener(PropertyChangeListener list) {
		support.addPropertyChangeListener(list);
	}
	
	public void removeListener(PropertyChangeListener list) {
		support.removePropertyChangeListener(list);
	}
	
	public String toString() {
		String s = "o" + sub + " = ";
		if(functionType==OutputFunction.ERGODIC_JULIA)
			s = s + "Random Composite Julia Set of ";
		else if(functionType == OutputFunction.FULL_JULIA)
			s = s + "Full Composite Julia Set of ";
		else if(functionType == OutputFunction.ERGODIC_ATTR)
			s = s + "Random Composite Attractor Set of ";
		else if(functionType == OutputFunction.FULL_ATTR)
			s = s + "Full Composite Attractor Set of ";
		else if(functionType == OutputFunction.POST_CRITICAL) 
			s = s + "Post Critical Set of ";
		for(int x = 0; x<inputFunctions.length; x++) {
			s = s + "f" + inputFunctions[x].getSubscript();
			if(x!=(inputFunctions.length - 1)) s = s + ", ";
		}
		return s;
	}
	
	public boolean writeToFile(File f) {
		FileOutputStream out;
		PrintStream ps;
		
		try {
            out = new FileOutputStream(f);
            ps = new PrintStream( out );
            for(int i = 0; i<points.length; i++) ps.println(points[i]);
            ps.println(iterations);
            ps.println(skips);
            ps.println(seed);
            ps.println(functionType);
            for(int j = 0; j<inputFunctions.length; j++){
            	ps.println(inputFunctions[j].getType());
            	ps.println(inputFunctions[j].getM());
            	ComplexNumber[] coefficients = inputFunctions[j].getCoefficients();
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
