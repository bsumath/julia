package edu.bsu.julia.threads;

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
			generator.run();
		}
	}
}
