package edu.bsu.julia.output;

import java.awt.Color;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import edu.bsu.julia.ComplexNumber;
import edu.bsu.julia.input.InputFunction;
import edu.bsu.julia.session.Session;

public class OutputFunction {
	public static enum Type {
		BASIC("Basic Output Set"),

		FULL_JULIA("Full Composite Julia Set"),

		ERGODIC_JULIA("Random Composite Julia Set"),

		FULL_ATTR("Full Composite Attractor Set"),

		ERGODIC_ATTR("Random Composite Attractor Set"),

		INVERSE_ATTR("Forward Image"),

		INVERSE_ERGODIC_JULIA("Random Inverse Image"),

		INVERSE_FULL_JULIA("Full Inverse Image"),

		IND_FULL_JULIA("Full Individual Julia Set"),

		IND_ERGODIC_JULIA("Random Individual Julia Set"),

		IND_FULL_ATTR("Full Individual Attractor Set"),

		IND_ERGODIC_ATTR("Random Individual Attractor Set"),

		POST_CRITICAL("Post Critical Set");

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
	private InputFunction[] inputFunctions = new InputFunction[] {};
	private ComplexNumber[] points = new ComplexNumber[] {};
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
		String s = "o" + sub + " = " + functionType.description() + " of ";

		for (int x = 0; x < inputFunctions.length; x++) {
			s = s + "f" + inputFunctions[x].getSubscript();
			if (x != (inputFunctions.length - 1))
				s = s + ", ";
		}
		return s;
	}

	public boolean equals(Object obj) {
		try {
			OutputFunction other = (OutputFunction) obj;
			boolean result = iterations == other.iterations;
			result = result && skips == other.skips;
			result = result && seed.equals(other.seed);
			result = result && functionType.equals(other.functionType);
			result = result
					&& inputFunctions.length == other.inputFunctions.length;
			result = result && points.length == other.points.length;

			for (int i = 0; result && i < inputFunctions.length; i++) {
				result = result
						&& inputFunctions[i].equals(other.inputFunctions[i]);
			}

			for (int i = 0; result && i < points.length; i++) {
				result = result && points[i].equals(other.points[i]);
			}

			return result;
		} catch (ClassCastException e) {
			return false;
		}
	}
}
