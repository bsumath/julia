package edu.bsu.julia.generators;

import java.util.Random;

import javax.swing.JFrame;

import org.apache.commons.math.complex.Complex;

import edu.bsu.julia.gui.JuliaError;
import edu.bsu.julia.input.InputFunction;

/**
 * An {@link OutputSetGenerator} that generates points of a julia set by
 * evaluating functions backwards using the random method.
 * 
 * @author Ben Dean
 */
public class RandomBackwardsOutputSetGenerator extends OutputSetGenerator {
	private static final Random RAND = new Random();

	private final JFrame parentFrame;
	private final int iterations;
	private final int skips;
	private final Complex seed;
	private final InputFunction[] inputFunctions;

	/**
	 * constructor for {@link RandomBackwardsOutputSetGenerator}
	 * 
	 * @param parent
	 *            the {@link JFrame} this generator was executed from
	 * @param iter
	 *            the number of iterations as an int
	 * @param sk
	 *            the number of skips as an int
	 * @param sd
	 *            the {@link Complex} seed
	 * @param inFunc
	 *            an array of {@link InputFunction}
	 */
	public RandomBackwardsOutputSetGenerator(JFrame parent, int iter, int sk,
			Complex sd, InputFunction[] inFunc) {
		parentFrame = parent;
		iterations = iter;
		skips = sk;
		seed = sd;
		inputFunctions = inFunc;
	}

	/**
	 * @see OutputSetGenerator#doInBackground()
	 */
	public Complex[] doInBackground() {
		try {
			// check that there are input functions
			if (inputFunctions.length == 0) {
				return null;
			}

			Complex currentPoint = seed;
			Complex[] outputSet = new Complex[iterations];
			int progress = 0;
			int maxProgress = iterations + skips;

			// iterate skips + iterations number of times
			for (int k = 0; k < iterations + skips; k++) {
				// find the next iteration of the current point
				InputFunction function = inputFunctions[RAND
						.nextInt(inputFunctions.length)];
				currentPoint = function.evaluateBackwardsRandom(currentPoint);
				if (currentPoint == null) {
					JuliaError.ZERO_DETERMINANT.showDialog(parentFrame);
					return null;
				}

				// after the skips have been used up, add the current point
				if (k >= skips) {
					outputSet[k - skips] = currentPoint;
				}
				progress = k;
				setProgress(Math.min((int) ((progress * 100f) / maxProgress),
						100));
			}

			return outputSet;
		} catch (OutOfMemoryError e) {
			JuliaError.OUT_OF_MEMORY.showDialog(parentFrame);
			return null;
		} catch (ArithmeticException e) {
			JuliaError.DIV_BY_ZERO.showDialog(parentFrame);
			return null;
		}
	}
}
