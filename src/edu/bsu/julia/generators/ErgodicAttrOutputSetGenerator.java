package edu.bsu.julia.generators;

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
public class ErgodicAttrOutputSetGenerator extends OutputSetGenerator {
	private final static Random RAND = new Random();

	private final JFrame parentFrame;
	private final int iterations;
	private final int skips;
	private final ComplexNumber seed;
	private final InputFunction[] inputFunctions;

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

			ComplexNumber currentPoint = seed;
			ComplexNumber[] outputSet = new ComplexNumber[iterations];
			int progress = 0;
			int maxProgress = iterations + skips;

			// iterate the number of skips + the number of iterations
			for (int k = 0; k < iterations + skips; k++) {
				// iterate the current point using a random input function
				InputFunction function = inputFunctions[RAND
						.nextInt(inputFunctions.length)];
				currentPoint = function.evaluateForwards(currentPoint);

				// if we've used up the skips, add the current point
				if (k >= skips)
					outputSet[k - skips] = currentPoint;
				progress += 1;
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
