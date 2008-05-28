package edu.bsu.julia.session;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Vector;

import edu.bsu.julia.ComplexNumber;
import edu.bsu.julia.input.InputFunction;
import edu.bsu.julia.output.OutputFunction;

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

		public Vector<InputFunction> provideInputFunctions();

		public Vector<OutputFunction> provideOutputFunctions();
		
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

		public void addInputFunctions(Vector<InputFunction> i);

		public void addOutputFunctions(Vector<OutputFunction> o);		
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

	private PropertyChangeSupport support = new PropertyChangeSupport(this);
	private int iterations;
	private int skips;
	private ComplexNumber seed;
	private Vector<InputFunction> inputFunctions;
	private Vector<OutputFunction> outputFunctions;
	private int inputsubscript;
	private int outputsubscript;
	private boolean modified;

	public Session(Importer importer) throws InvalidSessionParametersException {
		iterations = importer.provideIterations();
		skips = importer.provideSkips();
		seed = importer.provideSeedValue();
		inputFunctions = importer.provideInputFunctions();
		outputFunctions = importer.provideOutputFunctions();
		inputsubscript = importer.provideInputSubscript();
		outputsubscript = importer.provideOutputSubscript();

		if (iterations <= 0)
			throw new InvalidSessionParametersException(
					"Points to Plot must be\na positive number.");
		if (skips < 0)
			throw new InvalidSessionParametersException(
					"Skips must be\na positive number.");
		if (skips >= iterations)
			throw new InvalidSessionParametersException(
					"Skips must be less than Points to Plot.");
	}

	public int getIterations() {
		return iterations;
	}

	public void setIterations(int iter) throws IllegalArgumentException {
		modified = true;
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
		modified = true;
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

	public void export(Exporter exporter){
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
		modified = false;
	}

}
