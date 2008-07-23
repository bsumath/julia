package edu.bsu.julia.generators;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import edu.bsu.julia.ComplexNumber;
import edu.bsu.julia.gui.JuliaError;
import edu.bsu.julia.input.InputFunction;
import edu.bsu.julia.output.OutputSet;

/**
 * an {@link OutputSetGenerator} that applies one {@link InputFunction} to the
 * points of a {@link List} of {@link OutputSet}.
 * 
 * @author Ben Dean
 */
public class RecursiveOutputSetGenerator extends OutputSetGenerator {
	/**
	 * enum to determine full or random method
	 * 
	 * @author Ben Dean
	 */
	public enum Type {
		RANDOM, FULL;

		public OutputSet.Type outputType() {
			switch (this) {
			case RANDOM:
				return OutputSet.Type.RANDOM_INVERSE_IMAGE;
			case FULL:
				return OutputSet.Type.FULL_INVERSE_IMAGE;
			default:
				return OutputSet.Type.BASIC;
			}
		}
	}

	private final JFrame parentFrame;
	private final List<ComplexNumber> seedList;
	private final InputFunction inputFunction;
	private final Type type;

	/**
	 * constructor for {@link RecursiveOutputSetGenerator}
	 * 
	 * @param parent
	 *            {@link JFrame} where the generator was executed
	 * @param inFunc
	 *            an {@link InputFunction} to apply to all the points
	 * @param outSets
	 *            an array of {@link OutputSet} to use as seed points
	 * @param t
	 *            {@link RecursiveOutputSetGenerator.Type} to determine full or
	 *            random method
	 */
	public RecursiveOutputSetGenerator(JFrame parent, InputFunction inFunc,
			OutputSet[] outSets, Type t) {
		parentFrame = parent;
		inputFunction = inFunc;
		type = t;

		// build a list of starting points from the output sets
		seedList = new ArrayList<ComplexNumber>();
		for (OutputSet set : outSets) {
			for (ComplexNumber point : set.getPoints())
				seedList.add(point);
		}
	}

	/**
	 * @see OutputSetGenerator#doInBackground()
	 */
	public ComplexNumber[] doInBackground() {
		try {
			List<ComplexNumber> outputSet = new ArrayList<ComplexNumber>();

			// estimate the maximum progress
			int progress = 0;
			int maxProgress;
			ComplexNumber[] arr = inputFunction
					.evaluateBackwardsFull(new ComplexNumber());
			if (arr != null)
				maxProgress = seedList.size() * arr.length;
			else
				maxProgress = seedList.size();

			// apply the function to each point in the seedList
			for (ComplexNumber point : seedList) {
				ComplexNumber[] tempResult;
				// create a temp array using full or random method
				if (type == Type.FULL) {
					tempResult = inputFunction.evaluateBackwardsFull(point);
					if (tempResult == null) {
						JuliaError.ZERO_DETERMINANT.showDialog(parentFrame);
						return null;
					}
				} else {
					ComplexNumber temp = inputFunction
							.evaluateBackwardsRandom(point);
					if (temp == null) {
						JuliaError.ZERO_DETERMINANT.showDialog(parentFrame);
						return null;
					}
					tempResult = new ComplexNumber[] { temp };
				}

				// add all the points from the temp list to the output set
				for (ComplexNumber pt : tempResult) {
					outputSet.add(pt);
				}
				progress = outputSet.size();
				setProgress(Math.min((int) ((progress * 100f) / maxProgress),
						100));
			}

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
