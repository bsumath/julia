package edu.bsu.julia.session;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;

import org.apache.commons.math.complex.Complex;

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
		public Integer provideIterations();

		public Integer provideSkips();

		public Complex provideSeedValue();

		public Collection<InputFunction> provideInputFunctions();

		public Collection<OutputSet> provideOutputSets();

		public int provideInputSubscript();

		public int provideOutputSubscript();

		public String provideSelectedMethod();

		public String provideSelectedType();

		public int[] provideSelectedInputIndices();

		public int[] provideSelectedOutputIndices();
	}

	/**
	 * this interface is used to export a session. particularly useful for
	 * saving a session to a file.
	 * 
	 * @author benjamin
	 */
	public interface Exporter {
		public void addIterations(Integer i);

		public void addSkips(Integer s);

		public void addSeedValue(Complex s);

		public void addInputFunctions(Collection<InputFunction> i);

		public void addOutputSets(Collection<OutputSet> o);

		public void addSelectedMethod(String method);

		public void addSelectedType(String type);

		public void addSelectedInputIndices(int[] indices);

		public void addSelectedOutputIndices(int[] indices);
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
	private Integer iterations;
	private Integer skips;
	private Complex seed;
	private List<InputFunction> inputFunctions;
	private List<OutputSet> outputSets;
	private Queue<OutputSet> outputQueue;
	private int inputSubscript;
	private int outputSubscript;
	private boolean modified = false;
	private final Julia parentFrame;

	private File sessionFile;

	private int[] importedInputIndices;

	private int[] importedOutputIndices;

	private String importedMethod;

	private String importedType;

	public Session(Julia frame, Importer importer)
			throws InvalidSessionParametersException {
		parentFrame = frame;

		iterations = importer.provideIterations();
		skips = importer.provideSkips();
		seed = importer.provideSeedValue();
		inputFunctions = new ArrayList<InputFunction>(importer
				.provideInputFunctions());
		outputSets = new ArrayList<OutputSet>(importer.provideOutputSets());
		outputQueue = new LinkedList<OutputSet>(outputSets);
		inputSubscript = importer.provideInputSubscript();
		outputSubscript = importer.provideOutputSubscript();

		importedInputIndices = importer.provideSelectedInputIndices();
		importedOutputIndices = importer.provideSelectedOutputIndices();
		importedMethod = importer.provideSelectedMethod();
		importedType = importer.provideSelectedType();

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

	public void initializeSelections() {
		parentFrame.getInputPanel().setSelectedIndices(importedInputIndices);
		parentFrame.getOutputSetList()
				.setSelectedIndices(importedOutputIndices);

		ButtonGroup group = parentFrame.getInputPanel().getMethodGroup();
		Enumeration<AbstractButton> buttons = group.getElements();
		while (buttons.hasMoreElements()) {
			ButtonModel model = buttons.nextElement().getModel();
			if (importedMethod.equals(model.getActionCommand()))
				group.setSelected(model, true);
		}

		group = parentFrame.getInputPanel().getTypeGroup();
		buttons = group.getElements();
		while (buttons.hasMoreElements()) {
			ButtonModel model = buttons.nextElement().getModel();
			if (importedType.equals(model.getActionCommand()))
				group.setSelected(model, true);
		}
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

	public Complex getSeedValue() {
		return seed;
	}

	public void setSeedValue(Complex seedValue) {
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

	public List<OutputSet> getOutputSets() {
		return outputSets;
	}

	public void addOutputSet(OutputSet set) {
		markModified();
		outputSets.add(set);
		outputQueue.add(set);
		set.setSubscript(getNextOutSubscript());
		support.firePropertyChange("addOutputSet", null, set);
	}

	public void deleteOutputSet(OutputSet set) {
		markModified();
		outputSets.remove(set);
		outputQueue.remove(set);
		set.delete();
		support.firePropertyChange("deleteOutputSet", null, set);
	}

	/**
	 * method to unload an {@link OutputSet} to try and free some heap space
	 */
	public void freeHeapSpace() {
		OutputSet set = outputQueue.poll();
		if (set != null) {
			set.unload();
			outputQueue.add(set);
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
		exporter.addOutputSets(outputSets);
		exporter.addSelectedInputIndices(parentFrame.getInputPanel()
				.getSelectedIndices());
		exporter.addSelectedOutputIndices(parentFrame.getOutputSetList()
				.getSelectedIndices());
		exporter.addSelectedMethod(parentFrame.getInputPanel().getMethodGroup()
				.getSelection().getActionCommand());
		exporter.addSelectedType(parentFrame.getInputPanel().getTypeGroup()
				.getSelection().getActionCommand());
	}

	public boolean isModified() {
		return modified;
	}

	public void markUnmodified() {
		if (modified) {
			((Julia) parentFrame).resetTitle();
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
			((Julia) parentFrame).resetTitle();
			parentFrame.setTitle(parentFrame.getTitle() + " - "
					+ file.getName());
		}
		sessionFile = file;
	}

	public File getFile() {
		return sessionFile;
	}
}
