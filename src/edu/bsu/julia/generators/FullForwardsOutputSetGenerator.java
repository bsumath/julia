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
	public static enum Options {
		DISCARD_INTERMEDIATE_POINTS, KEEP_INTERMEDIATE_POINTS;
	}

	private final int iterations;
	private final ComplexNumber[] seedList;
	private final InputFunction[] inputFunctions;
	private final Options option;
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
	 *            {@link Options} describing whether or not to keep the
	 *            intermediate points at each iteration
	 */
	public FullForwardsOutputSetGenerator(JFrame parent, int iter,
			ComplexNumber[] seed, InputFunction[] inFunc, Options opt) {
		parentFrame = parent;
		iterations = iter;
		seedList = seed;
		inputFunctions = inFunc;
		option = opt;
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
			boolean specialCase = inputFunctions.length == 1
					&& seedList.length == 1;
			boolean isDone = false;

			List<ComplexNumber> outputSet = new ArrayList<ComplexNumber>();
			;
			List<ComplexNumber> currentIteration = new ArrayList<ComplexNumber>(
					Arrays.asList(seedList));
			List<ComplexNumber> tempList;

			// check for case where the iterations are done before starting this
			// is rare. namely a post critical set with t = 1
			if (option == Options.KEEP_INTERMEDIATE_POINTS && iterations == 0) {
				return currentIteration.toArray(new ComplexNumber[] {});
			}

			do {
				if (option == Options.KEEP_INTERMEDIATE_POINTS)
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

				// determine if we're done iterating

				if (specialCase || option == Options.KEEP_INTERMEDIATE_POINTS) {
					progress = iterationCounter;
					isDone = iterationCounter >= iterations;
				} else {
					progress = currentIteration.size();
					isDone = currentIteration.size() >= iterations;
				}
				setProgress(Math.min((int) ((progress * 100f) / maxProgress),
						100));

			} while (!isDone);

			outputSet.addAll(currentIteration);
			return outputSet.toArray(new ComplexNumber[] {});
		} catch (OutOfMemoryError e) {
			JuliaError.OUT_OF_MEMORY.showDialog(parentFrame);
			return null;
		} catch (ArithmeticException e) {
			JuliaError.DIV_BY_ZERO.showDialog(parentFrame);
			return null;
		}
	}
}
