package edu.bsu.julia.generators;

import java.util.Arrays;
import java.util.Random;

import javax.swing.JFrame;

import edu.bsu.julia.ComplexNumber;
import edu.bsu.julia.gui.JuliaError;
import edu.bsu.julia.input.InputFunction;

/**
 * An {@link OutputSetGenerator} that generates points of a julia set using the
 * ergodic method.
 * 
 * @author Ben Dean
 */
public class ErgodicJuliaOutputSetGenerator implements OutputSetGenerator {
	private static final Random RAND = new Random();

	private final JFrame parentFrame;
	private final int iterations;
	private final int skips;
	private final ComplexNumber seed;
	private final InputFunction[] inputFunctions;
	private final ComplexNumber[] outputSet;

	private volatile boolean cancelExecution = false;
	private volatile boolean executionComplete = false;
	private volatile int progress = 0;
	private final int maxProgress;

	/**
	 * constructor for {@link ErgodicJuliaOutputSetGenerator}
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
	public ErgodicJuliaOutputSetGenerator(JFrame parent, int iter, int sk,
			ComplexNumber sd, InputFunction[] inFunc) {
		parentFrame = parent;
		iterations = iter;
		skips = sk;
		seed = sd;
		inputFunctions = inFunc;

		maxProgress = iterations + skips;

		outputSet = new ComplexNumber[iterations];
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

		// iterate skips + iterations number of times
		for (int k = 0; k < iterations + skips; k++) {
			// find the next iteration of the current point
			try {
				InputFunction function = inputFunctions[RAND
						.nextInt(inputFunctions.length)];
				currentPoint = function.evaluateBackwardsRandom(currentPoint);
				if (currentPoint == null) {
					JuliaError.ZERO_DETERMINANT.showDialog(parentFrame);
					executionComplete = true;
					return;
				}
			} catch (ArithmeticException e) {
				JuliaError.DIV_BY_ZERO.showDialog(parentFrame);
				executionComplete = true;
				return;
			}

			// after the skips have been used up, add the current point
			if (k >= skips) {
				outputSet[k - skips] = currentPoint;
			}
			progress = k;

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
