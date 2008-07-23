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
 * for attractor sets, evaluating the functions forwards. Also used for forward
 * image sets.
 * 
 * @author Ben Dean
 */
public class FullForwardsOutputSetGenerator extends OutputSetGenerator {
	/**
	 * an enum to specify options describing whether or not to keep the
	 * intermediate points at each iteration
	 * 
	 * @author Ben Dean
	 */
	public static enum Mode {
		DEFAULT, POST_CRITICAL;
	}

	private final int iterations;
	private final ComplexNumber[] seedList;
	private final InputFunction[] inputFunctions;
	private final Mode mode;
	private final JFrame parentFrame;

	/**
	 * constructor for {@link FullForwardsOutputSetGenerator}
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
	 *            {@link Mode} describing whether or not to keep the
	 *            intermediate points at each iteration
	 */
	public FullForwardsOutputSetGenerator(JFrame parent, int iter,
			ComplexNumber[] seed, InputFunction[] inFunc, Mode opt) {
		parentFrame = parent;
		iterations = iter;
		seedList = seed;
		inputFunctions = inFunc;
		mode = opt;
	}

	/**
	 * @see OutputSetGenerator#doInBackground()
	 */
	public ComplexNumber[] doInBackground() {
		List<ComplexNumber> currentIteration = new ArrayList<ComplexNumber>();
		try {
			// check that there are input functions
			if (inputFunctions.length == 0) {
				return null;
			}

			int progress = 0;
			int maxProgress = iterations;

			int iterationCounter = 0;
			boolean isDone = false;

			List<ComplexNumber> outputSet = new ArrayList<ComplexNumber>();
			;
			new ArrayList<ComplexNumber>(Arrays.asList(seedList));
			List<ComplexNumber> tempList;

			// check for case where the iterations are done before starting this
			// is rare. namely a post critical set with t = 1
			if (mode == Mode.POST_CRITICAL && iterations == 0) {
				return currentIteration.toArray(new ComplexNumber[] {});
			}

			do {
				if (mode == Mode.POST_CRITICAL)
					outputSet.addAll(currentIteration);

				// iterate each point by each function
				tempList = new ArrayList<ComplexNumber>(currentIteration.size()
						* inputFunctions.length);
				for (ComplexNumber point : currentIteration) {
					for (InputFunction function : inputFunctions) {
						tempList.add(function.evaluateForwards(point));
					}
				}

				// the currentIteration is now the tempList
				currentIteration = tempList;
				iterationCounter += 1;

				// update the progress and isDone condition
				if (mode == Mode.POST_CRITICAL) {
					progress = iterationCounter;
					isDone = iterationCounter >= iterations;
				} else {
					progress = (iterationCounter > currentIteration.size()) ? iterationCounter
							: currentIteration.size();
					isDone = iterationCounter >= iterations
							|| currentIteration.size() >= iterations;
				}

				// set the progress for the SwingWorker
				setProgress(Math.min((int) ((progress * 100f) / maxProgress),
						100));

			} while (!isDone);

			outputSet.addAll(currentIteration);
			return outputSet.toArray(new ComplexNumber[] {});
		} catch (OutOfMemoryError e) {
			currentIteration = null;
			JuliaError.OUT_OF_MEMORY.showDialog(parentFrame);
			return null;
		} catch (ArithmeticException e) {
			JuliaError.DIV_BY_ZERO.showDialog(parentFrame);
			return null;
		}
	}
}
