package edu.bsu.julia.output;

import java.awt.Color;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import edu.bsu.julia.ComplexNumber;
import edu.bsu.julia.input.InputFunction;
import edu.bsu.julia.session.Session;

public class OutputFunction {
	public static enum Type {
		BASIC("Basic Output Set of "), FULL_JULIA(
				"Full Composite Julia Set of "), ERGODIC_JULIA(
				"Random Composite Julia Set of "), FULL_ATTR(
				"Full Composite Attractor Set of "), ERGODIC_ATTR(
				"Random Composite Attractor Set of "), INVERSE_ATTR(
				"Forward Image of "), INVERSE_ERGODIC_JULIA(
				"Inverse Random Julia Set of "), INVERSE_FULL_JULIA(
				"Inverse Full Julia Set of "), POST_CRITICAL(
				"Post Critical Set of ");
		private String description;

		private Type(String d) {
			description = d;
		}

		public String description() {
			return description;
		}
	}

	private int sub = 0;
	private int iterations;
	private int skips;
	private ComplexNumber seed;
	protected Type functionType;
	private InputFunction[] inputFunctions;
	private ComplexNumber[] points;
	private Color c;
	private final static Color[] colorSet = { Color.BLACK, Color.BLUE,
			Color.RED, Color.DARK_GRAY, Color.GREEN, Color.ORANGE,
			Color.MAGENTA };
	private static int colorIndex = 0;
	private PropertyChangeSupport support = new PropertyChangeSupport(this);

	public OutputFunction(Session s, InputFunction[] i, Type type,
			ComplexNumber[] p) {
		iterations = s.getIterations();
		skips = s.getSkips();
		seed = s.getSeedValue();
		functionType = type;
		if (functionType == Type.POST_CRITICAL)
			skips = 0;

		inputFunctions = i;
		points = p;
		c = getNextColor();
	}

	public OutputFunction(int sk, ComplexNumber sd, Type type, ComplexNumber[] p) {
		iterations = p.length;
		skips = sk;
		seed = sd;
		functionType = type;
		if (functionType == Type.POST_CRITICAL)
			skips = 0;
		iterations += skips;
		points = p;
		c = getNextColor();
	}

	public Type getType() {
		return functionType;
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

	public int getNumOfPoints() {
		return points.length;
	}

	public Color getColor() {
		return c;
	}

	public static Color getNextColor() {
		Color color = colorSet[colorIndex];
		if (colorIndex < colorSet.length - 1)
			colorIndex++;
		else
			colorIndex = 0;
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
		String s = "o" + sub + " = " + functionType.description();

		for (int x = 0; x < inputFunctions.length; x++) {
			s = s + "f" + inputFunctions[x].getSubscript();
			if (x != (inputFunctions.length - 1))
				s = s + ", ";
		}
		return s;
	}

	public int hashCode(){
		int hash = 1;
		hash *= 31 + points.hashCode();
		hash *= 29 + inputFunctions.hashCode();
		hash *= 23 + functionType.hashCode();
		hash *= 19 + seed.hashCode();
		hash *= 17 + skips;
		hash *= 13 + iterations;
		return hash;
	}
	
	public boolean writeToFile(File f) {
		FileOutputStream out;
		PrintStream ps;

		try {
			out = new FileOutputStream(f);
			ps = new PrintStream(out);
			for (int i = 0; i < points.length; i++)
				ps.println(points[i]);
			ps.println(iterations);
			ps.println(skips);
			ps.println(seed);
			ps.println(functionType);
			for (int j = 0; j < inputFunctions.length; j++) {
				ps.println(inputFunctions[j].getClass().getName());
				ps.println(inputFunctions[j].getM());
				ComplexNumber[] coefficients = inputFunctions[j]
						.getCoefficients();
				for (int k = 0; k < coefficients.length; k++)
					ps.println(coefficients[k]);
			}
			ps.close();
		} catch (IOException e) {
			System.err.println(e);
			return false;
		}
		return true;
	}

}
