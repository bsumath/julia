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
public class FullAttrOutputSetGenerator extends OutputSetGenerator {
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
			boolean specialCase = (option == Options.DISCARD_INTERMEDIATE_POINTS
					&& inputFunctions.length == 1 && seedList.length == 1);
			boolean isDone = false;

			List<ComplexNumber> outputSet = new ArrayList<ComplexNumber>();
			;
			List<ComplexNumber> currentIteration = new ArrayList<ComplexNumber>(
					Arrays.asList(seedList));
			List<ComplexNumber> tempList;
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
				if (option == Options.KEEP_INTERMEDIATE_POINTS) {
					progress = outputSet.size();
					isDone = outputSet.size() >= iterations;
				} else {
					progress = currentIteration.size();
					isDone = currentIteration.size() >= iterations;
				}
				setProgress(Math.min((int) ((progress * 100f) / maxProgress),
						100));

				// check for special case with one function and one point
				if (specialCase) {
					isDone = iterationCounter >= iterations;
				}

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
