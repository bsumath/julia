package edu.bsu.julia.output;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.swing.JProgressBar;
import javax.swing.Timer;

import edu.bsu.julia.ComplexNumber;
import edu.bsu.julia.generators.OutputSetGenerator;
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
	private final int skips;
	private final ComplexNumber seed;
	protected final Type functionType;
	private final InputFunction[] inputFunctions;
	private ComplexNumber[] points;
	private OutputSetGenerator generator;
	private File pointsFile;

	private Color c;
	private final static Color[] colorSet = { Color.BLACK, Color.BLUE,
			Color.RED, Color.DARK_GRAY, Color.GREEN, Color.ORANGE,
			Color.MAGENTA };
	private static int colorIndex = 0;
	private PropertyChangeSupport support = new PropertyChangeSupport(this);
	private final JProgressBar bar = new JProgressBar(0, 100);
	private Timer timer;

	public OutputFunction(Session s, InputFunction[] i, Type type,
			OutputSetGenerator gen) {
		iterations = s.getIterations();
		functionType = type;
		skips = (functionType == Type.POST_CRITICAL) ? 0 : s.getSkips();
		seed = s.getSeedValue();

		inputFunctions = i;
		generator = gen;
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
		if (points == null)
			return 0;
		else
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

	public boolean isLoaded() {
		return points != null;
	}

	public Component getLoadingComponent() {
		bar.setStringPainted(true);
		bar.setValue(50);
		return bar;
	}

	public synchronized void unload() {
		if (pointsFile == null)
			return;
		points = null;
	}

	public synchronized void load() {
		if (pointsFile == null) {
			regenerate();
		} else {
			readPointsTempFile();
		}
	}

	public synchronized void regenerate() {
		// start the thread and timer
		Thread thread = new Thread(generator);
		thread.start();
		timer = new Timer(10, new TimerActionListener(thread));
		timer.start();
	}

	private void writePointsTempFile() {
		if (pointsFile != null)
			return;

		new Thread() {
			public void run() {
				try {
					// create a new temp file and open it.
					pointsFile = File.createTempFile(System.currentTimeMillis()
							+ "", ".tmp.z");
					pointsFile.deleteOnExit();
					ZipOutputStream zipOut = new ZipOutputStream(
							new FileOutputStream(pointsFile));
					zipOut.putNextEntry(new ZipEntry(pointsFile.getName()
							.replaceAll(".tmp.z", ".tmp")));
					PrintStream out = new PrintStream(zipOut);

					for (ComplexNumber p : points)
						out.println(p);

					out.close();
					zipOut.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	private void readPointsTempFile() {
		points = null;
		List<ComplexNumber> tempPoints = new ArrayList<ComplexNumber>();

		try {
			// open the temp file
			ZipInputStream zipIn = new ZipInputStream(new FileInputStream(
					pointsFile));
			zipIn.getNextEntry();
			Scanner in = new Scanner(zipIn);

			// try to read all the points
			while (in.hasNextLine()) {
				String line = in.nextLine();
				tempPoints.add(ComplexNumber.parseComplexNumber(line));
			}

			// create an array
			points = tempPoints.toArray(new ComplexNumber[] {});

			in.close();
			zipIn.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private final class TimerActionListener implements ActionListener {
		private final Thread thread;

		public TimerActionListener(Thread thread) {
			this.thread = thread;
		}

		public void actionPerformed(ActionEvent e) {
			if (thread.isAlive() || !generator.isDone()) {
				int progress = (int) (generator.getPercentComplete() * 100);
				progress = (progress < 0) ? 0 : progress;
				progress = (progress > 100) ? 100 : progress;
				bar.setValue(progress);
				support.firePropertyChange("repaint", null, null);
			} else {
				timer.stop();
				points = generator.getPoints();
				writePointsTempFile();
				support.firePropertyChange("reselect", null, null);
			}
		}
	}
}
