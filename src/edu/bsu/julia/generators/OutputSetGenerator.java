package edu.bsu.julia.generators;

import javax.swing.SwingWorker;

import edu.bsu.julia.ComplexNumber;

/**
 * This abstract class defines the algorithms to be run when creating an
 * OutputFunction. It extends {@link SwingWorker} and returns an array of
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
