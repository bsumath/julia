package edu.bsu.julia.threads;

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
	 * @see Runnable#run()
	 */
	public void run();

	/**
	 * Access the points created by the OutputSetGenerator
	 * 
	 * @return a List of ComplexNumbers
	 */
	public List<ComplexNumber> getPoints();
}
