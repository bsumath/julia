package edu.bsu.julia.generators;

import java.util.List;

import edu.bsu.julia.ComplexNumber;

/**
 * This interface defines the algorithms to be run when creating an
 * OutputFunction. Since it extends Runnable, it can be used to create a Thread
 * and must implement the run() method.
 * 
 * @author Ben Dean
 */
public interface OutputSetGenerator extends Runnable {
	/**
	 * This method should be synchronized.
	 * 
	 * @see Runnable#run()
	 */
	public void run();

	/**
	 * Access the points created by the OutputSetGenerator
	 * 
	 * @return a List of ComplexNumbers
	 */
	public List<ComplexNumber> getPoints();

	/**
	 * Method to cancel the execution of the {@link OutputSetGenerator}. This
	 * method should be synchronized
	 */
	public void cancelExecution();

	/**
	 * Method to return the current progress of the execution as a percentage
	 * complete. This method should be synchronized
	 * 
	 * @return the percentage complete as a floating point number. should be
	 *         between 0 and 1
	 */
	public float getPercentComplete();

	/**
	 * Method to determine if the generator is done executing. This method
	 * should be synchronized.
	 * 
	 * @return true if done executing, false otherwise
	 */
	public boolean isDone();
}
