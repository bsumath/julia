package edu.bsu.julia.threads;

import java.util.Vector;

import edu.bsu.julia.ComplexNumber;
import edu.bsu.julia.Julia;
import edu.bsu.julia.gui.JuliaError;
import edu.bsu.julia.input.InputFunction;
import edu.bsu.julia.output.OutputFunction;
import edu.bsu.julia.session.Session;

public class CFullAttrThread extends Thread {

	private Julia parentFrame;
	private int progress;
	private boolean stop;

	public CFullAttrThread(Julia f) {
		parentFrame = f;
		progress = 0;
		stop = false;
	}

	public void run() {
		try {
			InputFunction[] functions = parentFrame.getInputPanel()
					.getSelectedFunctions();
			if (functions.length == 0)
				return;
			Session s = parentFrame.getCurrentSession();
			ComplexNumber seed = s.getSeedValue();
			int iterations = s.getIterations();

			ComplexNumber start = seed.clone();
			Vector<ComplexNumber> compositePoints = new Vector<ComplexNumber>();
			Vector<ComplexNumber> interimPoints = new Vector<ComplexNumber>();
			interimPoints.add(start);

			int iterationCounter = 0;
			do {
				compositePoints.clear();
				for (ComplexNumber point : interimPoints) {
					for (InputFunction function : functions) {
						try {
							compositePoints.add(function
									.evaluateForwards(point));
						} catch (ArithmeticException e) {
							JuliaError.DIV_BY_ZERO.showDialog(parentFrame);
							return;
						}
						if (stop)
							return;
						Thread.yield();
					}
					Thread.yield();
				}

				interimPoints.clear();
				interimPoints.addAll(compositePoints);
				iterationCounter += 1;
				progress = progress + compositePoints.size();
				Thread.yield();
			} while (!(functions.length == 1 && iterationCounter >= iterations)
					&& compositePoints.size() < iterations);

			ComplexNumber[] compOutArray = compositePoints.toArray(new ComplexNumber[]{});
			OutputFunction compOutFn = new OutputFunction(s, functions,
					OutputFunction.Type.FULL_ATTR, compOutArray);
			s.addOutputFunction(compOutFn);
		} catch (OutOfMemoryError e) {
			JuliaError.OUT_OF_MEMORY.showDialog(parentFrame);
		}
	}

	public int getProgress() {
		return progress;
	}

	public void setStop() {
		stop = true;
	}

}
