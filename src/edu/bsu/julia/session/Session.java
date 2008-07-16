package edu.bsu.julia.session;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javax.swing.JFrame;

import edu.bsu.julia.ComplexNumber;
import edu.bsu.julia.Julia;
import edu.bsu.julia.input.InputFunction;
import edu.bsu.julia.output.OutputSet;

public class Session {
	/**
	 * this interface is used by the Session constructor to create a new
	 * session. Anything that wants to be used to create new Sessions must make
	 * use of this interface
	 * 
	 * @author benjamin
	 */
	public interface Importer {
		public int provideIterations();

		public int provideSkips();

		public ComplexNumber provideSeedValue();

		public Collection<InputFunction> provideInputFunctions();

		public Collection<OutputSet> provideOutputFunctions();

		public int provideInputSubscript();

		public int provideOutputSubscript();
	}

	/**
	 * this interface is used to export a session. particularly useful for
	 * saving a session to a file.
	 * 
	 * @author benjamin
	 */
	public interface Exporter {
		public void addIterations(int i);

		public void addSkips(int s);

		public void addSeedValue(ComplexNumber s);

		public void addInputFunctions(Collection<InputFunction> i);

		public void addOutputFunctions(Collection<OutputSet> o);
	}

	/**
	 * this class represents exceptions that occur when a session is created
	 * with invalid parameters
	 * 
	 * @author benjamin
	 */
	public class InvalidSessionParametersException extends Exception {
		private static final long serialVersionUID = 1L;

		public InvalidSessionParametersException(String message) {
			super(message);
		}
	}

	private static final String MODIFIED_TITLE = " *session modified*";

	private PropertyChangeSupport support = new PropertyChangeSupport(this);
	private int iterations;
	private int skips;
	private ComplexNumber seed;
	private List<InputFunction> inputFunctions;
	private List<OutputSet> outputFunctions;
	private Queue<OutputSet> outputQueue;
	private int inputSubscript;
	private int outputSubscript;
	private boolean modified = false;
	private final JFrame parentFrame;

	private File sessionFile;

	public Session(JFrame frame, Importer importer)
			throws InvalidSessionParametersException {
		parentFrame = frame;

		iterations = importer.provideIterations();
		skips = importer.provideSkips();
		seed = importer.provideSeedValue();
		inputFunctions = new ArrayList<InputFunction>(importer
				.provideInputFunctions());
		outputFunctions = new ArrayList<OutputSet>(importer
				.provideOutputFunctions());
		outputQueue = new LinkedList<OutputSet>(outputFunctions);
		inputSubscript = importer.provideInputSubscript();
		outputSubscript = importer.provideOutputSubscript();

		if (iterations <= 0)
			throw new InvalidSessionParametersException(
					"Points to Plot must be\na positive number.");
		if (skips < 0)
			throw new InvalidSessionParametersException(
					"Skips must be\na positive number.");
		if (skips >= iterations)
			throw new InvalidSessionParametersException(
					"Skips must be less than Points to Plot.");

		markUnmodified();
	}

	public int getIterations() {
		return iterations;
	}

	public void setIterations(int iter) throws IllegalArgumentException {
		markModified();
		if (iter <= 0)
			throw new IllegalArgumentException("Iterations must be"
					+ "\na positive number.");
		iterations = iter;
		support.firePropertyChange("iterations", null, iterations);
	}

	public int getSkips() {
		return skips;
	}

	public void setSkips(int sk) throws IllegalArgumentException {
		markModified();
		if (sk < 0)
			throw new IllegalArgumentException("Skips must be"
					+ "\na positive number.");
		skips = sk;
		support.firePropertyChange("skips", null, skips);
	}

	public ComplexNumber getSeedValue() {
		return seed;
	}

	public void setSeedValue(ComplexNumber seedValue) {
		markModified();
		seed = seedValue;
		support.firePropertyChange("seed", null, seed);
	}

	public List<InputFunction> getInputFunctions() {
		return inputFunctions;
	}

	public void replaceInputFunction(InputFunction oldFn, InputFunction newFn) {
		markModified();
		inputFunctions.set(inputFunctions.indexOf(oldFn), newFn);
		newFn.setSubscript(getNextInSubscript());
		support.firePropertyChange("replaceInputFunction", oldFn, newFn);
	}

	public void addInputFunction(InputFunction fn) {
		markModified();
		inputFunctions.add(fn);
		fn.setSubscript(getNextInSubscript());
		support.firePropertyChange("addInputFunction", null, fn);
	}

	public void deleteInputFunction(int fn) {
		markModified();
		inputFunctions.remove(fn);
		support.firePropertyChange("deleteInputFunction", null, fn);
	}

	public List<OutputSet> getOutputFunctions() {
		return outputFunctions;
	}

	public void addOutputFunction(OutputSet fn) {
		markModified();
		outputFunctions.add(fn);
		outputQueue.add(fn);
		fn.setSubscript(getNextOutSubscript());
		support.firePropertyChange("addOutputFunction", null, fn);
	}

	public void deleteOutputFunction(OutputSet function) {
		markModified();
		outputFunctions.remove(function);
		outputQueue.remove(function);
		function.delete();
		support.firePropertyChange("deleteOutputFunction", null, function);
	}

	/**
	 * method to unload an OutputFunction to try and free some heap space
	 */
	public void freeHeapSpace() {
		OutputSet function = outputQueue.poll();
		if (function != null) {
			function.unload();
			outputQueue.add(function);
		}
	}

	public void addListener(PropertyChangeListener list) {
		support.addPropertyChangeListener(list);
	}

	public int getNextInSubscript() {
		return ++inputSubscript;
	}

	public int getNextOutSubscript() {
		return ++outputSubscript;
	}

	public void export(Exporter exporter) {
		exporter.addIterations(iterations);
		exporter.addSkips(skips);
		exporter.addSeedValue(seed);
		exporter.addInputFunctions(inputFunctions);
		exporter.addOutputFunctions(outputFunctions);
	}

	public boolean isModified() {
		return modified;
	}

	public void markUnmodified() {
		if (modified) {
			((Julia)parentFrame).resetTitle();
		}
		modified = false;
	}

	private void markModified() {
		if (!modified)
			parentFrame.setTitle(parentFrame.getTitle() + MODIFIED_TITLE);
		modified = true;
	}

	public void setFile(File file) {
		if (file != null) {
			((Julia)parentFrame).resetTitle();
			parentFrame.setTitle(parentFrame.getTitle() + " - "
					+ file.getName());
		}
		sessionFile = file;
	}

	public File getFile() {
		return sessionFile;
	}
}
