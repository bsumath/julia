package edu.bsu.julia.generators;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import edu.bsu.julia.ComplexNumber;
import edu.bsu.julia.gui.JuliaError;
import edu.bsu.julia.input.InputFunction;

public class FullJuliaOutputSetGenerator implements OutputSetGenerator {
	private final JFrame parentFrame;
	private final int iterations;
	private final ComplexNumber seed;
	private final List<InputFunction> inputFunctions;
	private final List<ComplexNumber> outputSet;

	private volatile boolean cancelExecution = false;
	private volatile boolean executionComplete = false;
	private volatile int progress = 0;
	private final int maxProgress;

	/**
	 * constructor for {@link FullJuliaOutputSetGenerator}
	 * 
	 * @param parent
	 *            the {@link JFrame} this generator was executed from
	 * @param iter
	 *            the number of iterations as an int
	 * @param sd
	 *            the {@link ComplexNumber} seed
	 * @param inFunc
	 *            the {@link List} of {@link InputFunction}
	 */
	public FullJuliaOutputSetGenerator(JFrame parent, int iter,
			ComplexNumber sd, List<InputFunction> inFunc) {
		parentFrame = parent;
		iterations = iter;
		seed = sd;
		inputFunctions = inFunc;

		outputSet = new ArrayList<ComplexNumber>();

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
		boolean specialCase = inputFunctions.size() == 1;
		boolean isDone = false;

		List<ComplexNumber> currentIteration = new ArrayList<ComplexNumber>();
		currentIteration.add(seed);
		List<ComplexNumber> tempList = new ArrayList<ComplexNumber>();
		do {
			// iterate each point by each of the input functions
			tempList.clear();
			for (ComplexNumber point : currentIteration) {
				for (InputFunction function : inputFunctions) {
					try {
						// evaluate backwards with the current function
						ComplexNumber[] temp = function
								.evaluateBackwardsFull(point);
						if (temp == null) {
							JuliaError.ZERO_DETERMINANT.showDialog(parentFrame);
							executionComplete = true;
							return;
						}

						// add all the points from the backwards evaluation
						for (ComplexNumber pt : temp){
							tempList.add(pt);
							Thread.yield();
						}
					} catch (ArithmeticException e) {
						JuliaError.DIV_BY_ZERO.showDialog(parentFrame);
						executionComplete = true;
						return;
					}
					
					// check if execution was canceled
					if (cancelExecution){
						executionComplete = true;
						return;
					}
					Thread.yield();
				}
				Thread.yield();
			}

			// the current iteration is a copy of the temp list of points
			currentIteration = new ArrayList<ComplexNumber>(tempList);
			iterationCounter += 1;

			// update the progress and isDone condition
			if (specialCase) {
				progress = iterationCounter;
				isDone = iterationCounter >= iterations;
			} else {
				progress = currentIteration.size();
				isDone = currentIteration.size() >= iterations;
			}

			Thread.yield();
		} while (!isDone);

		// iteration complete, the output set is the most recent iteration
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
