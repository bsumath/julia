package edu.bsu.julia.generators;

import java.util.ArrayList;
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

	private List<ComplexNumber> outputSet = new ArrayList<ComplexNumber>();
	private int iterations;
	private List<ComplexNumber> seedList;
	private List<InputFunction> inputFunctions;
	private Options option;
	private JFrame parentFrame;
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
	 *            a {@link List} of {@link InputFunction}
	 * @param opt
	 *            {@link Options} describing whether or not to keep the
	 *            intermediate points at each iteration
	 */
	public FullAttrOutputSetGenerator(JFrame parent, int iter,
			List<ComplexNumber> seed, List<InputFunction> inFunc, Options opt) {
		parentFrame = parent;
		iterations = iter;
		seedList = seed;
		inputFunctions = inFunc;
		option = opt;

		maxProgress = iterations;
	}

	/**
	 * @see OutputSetGenerator#run()
	 */
	public synchronized void run() {
		// check that there are input functions
		if (inputFunctions.size() == 0){
			executionComplete = true;
			return;
		}

		int iterationCounter = 0;
		boolean specialCase = (option == Options.DISCARD_INTERMEDIATE_POINTS
				&& inputFunctions.size() == 1 && seedList.size() == 1);
		boolean isDone = false;

		List<ComplexNumber> currentIteration = seedList;
		List<ComplexNumber> tempList = new ArrayList<ComplexNumber>();
		do {
			if (iterationCounter > 0
					&& option == Options.KEEP_INTERMEDIATE_POINTS)
				outputSet.addAll(currentIteration);

			tempList.clear();
			for (ComplexNumber point : currentIteration) {
				for (InputFunction function : inputFunctions) {
					try {
						tempList.add(function.evaluateForwards(point));
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

			currentIteration = new ArrayList<ComplexNumber>(tempList);
			iterationCounter += 1;

			if (option == Options.KEEP_INTERMEDIATE_POINTS) {
				progress = outputSet.size();
				isDone = outputSet.size() >= iterations;
			} else {
				progress = currentIteration.size();
				isDone = currentIteration.size() >= iterations;
			}

			if (specialCase){
				isDone = iterationCounter >= iterations;
			}
			Thread.yield();
		} while (!isDone);

		outputSet.addAll(currentIteration);
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
	public List<ComplexNumber> getPoints() {
		return outputSet;
	}

	/**
	 * @see OutputSetGenerator#isDone()
	 */
	public synchronized boolean isDone() {
		return executionComplete;
	}
}
