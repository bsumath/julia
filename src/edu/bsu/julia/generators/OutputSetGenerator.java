package edu.bsu.julia.generators;

import javax.swing.SwingWorker;

import edu.bsu.julia.ComplexNumber;
import edu.bsu.julia.output.OutputSet;

/**
 * This abstract class defines the algorithms to be run when creating an
 * {@link OutputSet}. It extends {@link SwingWorker} and returns an array of
 * {@link ComplexNumber} after the worker has finished
 * 
 * @author Ben Dean
 */
public abstract class OutputSetGenerator extends
		SwingWorker<ComplexNumber[], Void> {

	/**
	 * the method that runs on a background thread to generate an array of
	 * {@link ComplexNumber}
	 * 
	 * @return the array of {@link ComplexNumber} that was generated
	 * @see SwingWorker#doInBackground()
	 */
	public abstract ComplexNumber[] doInBackground();
}
