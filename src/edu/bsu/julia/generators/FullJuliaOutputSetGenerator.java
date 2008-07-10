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
 * for julia sets.
 * 
 * @author Ben Dean
 */
public class FullJuliaOutputSetGenerator extends OutputSetGenerator {
	private final JFrame parentFrame;
	private final int iterations;
	private final ComplexNumber seed;
	private final InputFunction[] inputFunctions;

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
	 *            an array of {@link InputFunction}
	 */
	public FullJuliaOutputSetGenerator(JFrame parent, int iter,
			ComplexNumber sd, InputFunction[] inFunc) {
		parentFrame = parent;
		iterations = iter;
		seed = sd;
		inputFunctions = inFunc;
	}

	/**
	 * @see OutputSetGenerator#doInBackground()
	 */
	public ComplexNumber[] doInBackground() {
		try {
			// check that there are input functions
			if (inputFunctions.length == 0) {
				return null;
			}

			int progress = 0;
			int maxProgress = iterations;

			int iterationCounter = 0;
			boolean specialCase = inputFunctions.length == 1;
			boolean isDone = false;

			List<ComplexNumber> currentIteration = new ArrayList<ComplexNumber>();
			currentIteration.add(seed);
			List<ComplexNumber> tempList = new ArrayList<ComplexNumber>();
			do {
				// iterate each point by each of the input functions
				tempList.clear();
				for (ComplexNumber point : currentIteration) {
					for (InputFunction function : inputFunctions) {
						// evaluate backwards with the current function
						ComplexNumber[] temp = function
								.evaluateBackwardsFull(point);
						if (temp == null) {
							JuliaError.ZERO_DETERMINANT.showDialog(parentFrame);
							return null;
						}

						// add all the points from the backwards evaluation
						tempList.addAll(Arrays.asList(temp));
					}
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
				setProgress(Math.min((int) ((progress * 100f) / maxProgress),
						100));
			} while (!isDone);

			// iteration complete, the output set is the most recent iteration
			return currentIteration.toArray(new ComplexNumber[] {});
		} catch (OutOfMemoryError e) {
			JuliaError.OUT_OF_MEMORY.showDialog(parentFrame);
			return null;
		} catch (ArithmeticException e) {
			JuliaError.DIV_BY_ZERO.showDialog(parentFrame);
			return null;
		}
	}
}
