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
public class InverseOutputSetGenerator implements OutputSetGenerator {
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
	private final List<ComplexNumber> outputSet;

	private volatile boolean cancelExecution = false;
	private volatile boolean executionComplete = false;
	private volatile int progress = 0;
	private final int maxProgress;

	/**
	 * constructor for {@link InverseOutputSetGenerator}
	 * 
	 * @param parent
	 *            {@link JFrame} where the generator was executed
	 * @param inFunc
	 *            {@link InputFunction} to apply to all the points
	 * @param outFunc
	 *            {@link List} of {@link OutputFunction} to use as seed points
	 * @param t
	 *            {@link InverseOutputSetGenerator.Type} to determine full or
	 *            ergodic method
	 */
	public InverseOutputSetGenerator(JFrame parent, InputFunction inFunc,
			List<OutputFunction> outFunc, Type t) {
		parentFrame = parent;
		inputFunction = inFunc;
		type = t;

		// build a list of starting points from the output functions
		seedList = new ArrayList<ComplexNumber>();
		for (OutputFunction function : outFunc) {
			for (ComplexNumber point : function.getPoints())
				seedList.add(point);
		}

		outputSet = new ArrayList<ComplexNumber>();

		// estimate the maximum progress
		ComplexNumber[] temp = inputFunction
				.evaluateBackwardsFull(new ComplexNumber());
		if (temp != null)
			maxProgress = seedList.size() * temp.length;
		else
			maxProgress = seedList.size();
	}

	/**
	 * @see OutputSetGenerator#run()
	 */
	public synchronized void run() {
		// apply the function to each point in the seedList
		for (ComplexNumber point : seedList) {
			ComplexNumber[] tempResult;
			try {
				// create a temp array using full or ergodic method
				if (type == Type.FULL) {
					tempResult = inputFunction.evaluateBackwardsFull(point);
					if (tempResult == null) {
						JuliaError.ZERO_DETERMINANT.showDialog(parentFrame);
						executionComplete = true;
						return;
					}
				} else {
					ComplexNumber temp = inputFunction
							.evaluateBackwardsRandom(point);
					if (temp == null) {
						JuliaError.ZERO_DETERMINANT.showDialog(parentFrame);
						executionComplete = true;
						return;
					}
					tempResult = new ComplexNumber[] { temp };
				}
			} catch (ArithmeticException e) {
				JuliaError.DIV_BY_ZERO.showDialog(parentFrame);
				executionComplete = true;
				return;
			}

			// add all the points from the temp list to the output set
			for (ComplexNumber pt : tempResult) {
				outputSet.add(pt);
				Thread.yield();
			}
			progress = outputSet.size();

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
