package edu.bsu.julia.generators;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFrame;

import edu.bsu.julia.ComplexNumber;
import edu.bsu.julia.gui.JuliaError;
import edu.bsu.julia.input.InputFunction;

/**
 * An {@link OutputSetGenerator} used to generate points using the full method
 * for attractor sets. Also used for forward image sets.
 * 
 * @author Ben Dean
 */
public class FullAttrOutputSetGenerator implements OutputSetGenerator {
	/**
	 * an enum to specify options describing whether or not to keep the
	 * intermediate points at each iteration
	 * 
	 * @author Ben Dean
	 */
	public static enum Options {
		DISCARD_INTERMEDIATE_POINTS, KEEP_INTERMEDIATE_POINTS;
	}

	private final List<ComplexNumber> outputSet;
	private final int iterations;
	private final ComplexNumber[] seedList;
	private final InputFunction[] inputFunctions;
	private final Options option;
	private final JFrame parentFrame;
	private volatile boolean cancelExecution = false;
	private volatile int progress = 0;
	private final int maxProgress;
	private volatile boolean executionComplete = false;

	/**
	 * constructor for {@link FullAttrOutputSetGenerator}
	 * 
	 * @param parent
	 *            the {@link JFrame} this generator is executed from
	 * @param iter
	 *            the number of iterations
	 * @param seed
	 *            a {@link List} of {@link ComplexNumber} to use as the seed
	 * @param inFunc
	 *            an array of {@link InputFunction}
	 * @param opt
	 *            {@link Options} describing whether or not to keep the
	 *            intermediate points at each iteration
	 */
	public FullAttrOutputSetGenerator(JFrame parent, int iter,
			ComplexNumber[] seed, InputFunction[] inFunc, Options opt) {
		parentFrame = parent;
		iterations = iter;
		seedList = seed;
		inputFunctions = inFunc;
		option = opt;

		outputSet = new ArrayList<ComplexNumber>();
		maxProgress = iterations;
	}

	/**
	 * @see OutputSetGenerator#run()
	 */
	public synchronized void run() {
		// reset the output set
		outputSet.clear();

		// check that there are input functions
		if (inputFunctions.length == 0){
			executionComplete = true;
			return;
		}

		int iterationCounter = 0;
		boolean specialCase = (option == Options.DISCARD_INTERMEDIATE_POINTS
				&& inputFunctions.length == 1 && seedList.length == 1);
		boolean isDone = false;

		ComplexNumber[] currentIteration = seedList;
		ComplexNumber[] tempList;
		do {
			if (option == Options.KEEP_INTERMEDIATE_POINTS
					&& iterationCounter > 0)
				outputSet.addAll(Arrays.asList(currentIteration));

			// iterate each point by each function
			tempList = new ComplexNumber[currentIteration.length * inputFunctions.length];
			int index = 0;
			for (ComplexNumber point : currentIteration) {
				for (InputFunction function : inputFunctions) {
					try {
						tempList[index++] = function.evaluateForwards(point);
					} catch (ArithmeticException e) {
						JuliaError.DIV_BY_ZERO.showDialog(parentFrame);
						executionComplete = true;
						return;
					}
					if (cancelExecution){
						executionComplete = true;
						return;
					}
					Thread.yield();
				}
				Thread.yield();
			}

			currentIteration = Arrays.copyOf(tempList, tempList.length);
			iterationCounter += 1;

			// determine if we're done iterating
			if (option == Options.KEEP_INTERMEDIATE_POINTS) {
				progress = outputSet.size();
				isDone = outputSet.size() >= iterations;
			} else {
				progress = currentIteration.length;
				isDone = currentIteration.length >= iterations;
			}

			// check for special case with one function and one point
			if (specialCase){
				isDone = iterationCounter >= iterations;
			}
			Thread.yield();
		} while (!isDone);

		outputSet.addAll(Arrays.asList(currentIteration));
		executionComplete = true;
	}

	/**
	 * @see OutputSetGenerator#cancelExecution()
	 */
	public synchronized void cancelExecution() {
		cancelExecution = true;
	}

	/**
	 * @see OutputSetGenerator#getPercentComplete()
	 */
	public synchronized float getPercentComplete() {
		float percent = progress / maxProgress;
		return (percent > 1) ? 1 : percent;
	}

	/**
	 * @see OutputSetGenerator#getPoints()
	 */
	public ComplexNumber[] getPoints() {
		return outputSet.toArray(new ComplexNumber[]{});
	}

	/**
	 * @see OutputSetGenerator#isDone()
	 */
	public synchronized boolean isDone() {
		return executionComplete;
	}
}
