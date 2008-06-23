package edu.bsu.julia.generators;

import java.util.ArrayList;
import java.util.List;

import edu.bsu.julia.ComplexNumber;

/**
 * a class to allow multiple OutputSetGenerators to be combined.
 * 
 * @author Ben Dean
 */
public final class CompoundOutputSetGenerator implements OutputSetGenerator {
	private final OutputSetGenerator[] generators;
	private volatile boolean cancelExecution = false;
	private volatile boolean isDone = false;

	/**
	 * constructor for {@link CompoundOutputSetGenerator} the order of the
	 * generators in the array determines the order in which they will be run by
	 * a thread.
	 * 
	 * @param generators
	 *            an array of {@link OutputSetGenerator} to use for the
	 *            {@link CompoundOutputSetGenerator}
	 */
	public CompoundOutputSetGenerator(OutputSetGenerator[] generators) {
		this.generators = generators;
	}

	/**
	 * get all the points from the various {@link OutputSetGenerator} objects
	 * 
	 * @see OutputSetGenerator#getPoints()
	 */
	public List<ComplexNumber> getPoints() {
		List<ComplexNumber> list = new ArrayList<ComplexNumber>();
		for (OutputSetGenerator generator : generators) {
			list.addAll(generator.getPoints());
		}
		return list;
	}

	/**
	 * run all the {@link OutputSetGenerator} objects in the order they are
	 * found in the array
	 * 
	 * @see OutputSetGenerator#run()
	 */
	public void run() {
		for (OutputSetGenerator generator : generators) {
			// start execution of the current generator
			Thread currentGenerator = new Thread(generator);
			currentGenerator.start();

			// wait until the current generator is done,
			// canceling execution if necessary.
			while (!generator.isDone()) {
				if (cancelExecution)
					generator.cancelExecution();

				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
				}
			}

			// check if execution was canceled
			if (cancelExecution)
				break;
		}
		isDone = true;
	}

	/**
	 * @see OutputSetGenerator#cancelExecution()
	 */
	public synchronized void cancelExecution() {
		cancelExecution = true;
	}

	/**
	 * find the percent complete of all the generators combined
	 * 
	 * @see OutputSetGenerator#getPercentComplete()
	 */
	public float getPercentComplete() {
		float sum = 0;
		for (OutputSetGenerator generator : generators) {
			sum += generator.getPercentComplete();
		}
		return sum / generators.length;
	}

	/**
	 * @see OutputSetGenerator#isDone()
	 */
	public synchronized boolean isDone() {
		return isDone;
	}
}
