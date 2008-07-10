package edu.bsu.julia.generators;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import edu.bsu.julia.ComplexNumber;
import edu.bsu.julia.gui.JuliaError;
import edu.bsu.julia.input.InputFunction;
import edu.bsu.julia.output.OutputFunction;

/**
 * an {@link OutputSetGenerator} that applies one {@link InputFunction} to the
 * points of a {@link List} of {@link OutputFunction}.
 * 
 * @author Ben Dean
 */
public class InverseOutputSetGenerator extends OutputSetGenerator {
	/**
	 * enum to determine full or ergodic method
	 * 
	 * @author Ben Dean
	 */
	public enum Type {
		ERGODIC, FULL;
	}

	private final JFrame parentFrame;
	private final List<ComplexNumber> seedList;
	private final InputFunction inputFunction;
	private final Type type;

	/**
	 * constructor for {@link InverseOutputSetGenerator}
	 * 
	 * @param parent
	 *            {@link JFrame} where the generator was executed
	 * @param inFunc
	 *            an {@link InputFunction} to apply to all the points
	 * @param outFunc
	 *            an array of {@link OutputFunction} to use as seed points
	 * @param t
	 *            {@link InverseOutputSetGenerator.Type} to determine full or
	 *            ergodic method
	 */
	public InverseOutputSetGenerator(JFrame parent, InputFunction inFunc,
			OutputFunction[] outFunc, Type t) {
		parentFrame = parent;
		inputFunction = inFunc;
		type = t;

		// build a list of starting points from the output functions
		seedList = new ArrayList<ComplexNumber>();
		for (OutputFunction function : outFunc) {
			for (ComplexNumber point : function.getPoints())
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
				// create a temp array using full or ergodic method
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
