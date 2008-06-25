package edu.bsu.julia.generators;

import java.util.Arrays;
import java.util.Random;

import javax.swing.JFrame;

import edu.bsu.julia.ComplexNumber;
import edu.bsu.julia.gui.JuliaError;
import edu.bsu.julia.input.InputFunction;

/**
 * An {@link OutputSetGenerator} to generate the points of an Attractor Set
 * using the Ergodic method. Chooses a random input function at each iteration.
 * 
 * @author Ben Dean
 */
public class ErgodicAttrOutputSetGenerator implements OutputSetGenerator {
	private final static Random RAND = new Random();

	private final JFrame parentFrame;
	private final int iterations;
	private final int skips;
	private final ComplexNumber seed;
	private final InputFunction[] inputFunctions;
	private volatile boolean cancelExecution = false;
	private volatile boolean executionComplete = false;
	private volatile int progress = 0;
	private final int maxProgress;

	private final ComplexNumber[] outputSet;

	/**
	 * constructor for {@link ErgodicAttrOutputSetGenerator}
	 * 
	 * @param parent
	 *            the {@link JFrame} this generator was executed from
	 * @param iter
	 *            the number of iterations as an int
	 * @param sk
	 *            the number of skips as an int
	 * @param sd
	 *            the {@link ComplexNumber} seed
	 * @param inFunc
	 *            an array of {@link InputFunction}
	 */
	public ErgodicAttrOutputSetGenerator(JFrame parent, int iter, int sk,
			ComplexNumber sd, InputFunction[] inFunc) {
		parentFrame = parent;
		iterations = iter;
		skips = sk;
		seed = sd;
		inputFunctions = inFunc;

		outputSet = new ComplexNumber[iterations];

		maxProgress = iterations + skips;
	}

	/**
	 * @see OutputSetGenerator#run()
	 */
	public synchronized void run() {
		// reset the output set
		Arrays.fill(outputSet, null);
		
		// check that there are input functions
		if (inputFunctions.length == 0) {
			executionComplete = true;
			return;
		}

		ComplexNumber currentPoint = seed;

		// iterate the number of skips + the number of iterations
		for (int k = 0; k < iterations + skips; k++) {
			// iterate the current point using a random input function
			try {
				InputFunction function = inputFunctions[RAND
						.nextInt(inputFunctions.length)];
				currentPoint = function.evaluateForwards(currentPoint);
			} catch (ArithmeticException e) {
				JuliaError.DIV_BY_ZERO.showDialog(parentFrame);
				executionComplete = true;
				return;
			}

			// if we've used up the skips, add the current point
			if (k >= skips)
				outputSet[k - skips] = currentPoint;
			progress += 1;

			// check if execution should be canceled
			if (cancelExecution) {
				executionComplete = true;
				return;
			}
			Thread.yield();
		}
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
		return outputSet;
	}

	/**
	 * @see OutputSetGenerator#isDone()
	 */
	public synchronized boolean isDone() {
		return executionComplete;
	}
}
