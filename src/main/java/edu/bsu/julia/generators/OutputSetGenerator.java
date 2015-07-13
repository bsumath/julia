package edu.bsu.julia.generators;

import javax.swing.SwingWorker;

import org.apache.commons.math.complex.Complex;

import edu.bsu.julia.output.OutputSet;

/**
 * This abstract class defines the algorithms to be run when creating an
 * {@link OutputSet}. It extends {@link SwingWorker} and returns an array of
 * {@link Complex} after the worker has finished
 * 
 * @author Ben Dean
 */
public abstract class OutputSetGenerator extends
		SwingWorker<Complex[], Void> {

	/**
	 * the method that runs on a background thread to generate an array of
	 * {@link Complex}
	 * 
	 * @return the array of {@link Complex} that was generated
	 * @see SwingWorker#doInBackground()
	 */
	public abstract Complex[] doInBackground();
}
