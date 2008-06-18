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
			int functionsLength = functions.length;
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
				for (int i = 0; i < interimPoints.size(); i++) {
					for (int j = 0; j < functionsLength; j++) {
						try {
							compositePoints.add(functions[j]
									.evaluateForwards(interimPoints
											.elementAt(i)));
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
				for (int i = 0; i < compositePoints.size(); i++) {
					interimPoints.add(compositePoints.elementAt(i));
					Thread.yield();
				}
				Thread.yield();
				iterationCounter += 1;
			} while (!(functions.length == 1 && iterationCounter >= iterations)
					&& compositePoints.size() < iterations);
			progress = progress + compositePoints.size();
			ComplexNumber[] compOutArray = new ComplexNumber[compositePoints
					.size()];
			for (int x = 0; x < compOutArray.length; x++) {
				compOutArray[x] = compositePoints.elementAt(x);
				Thread.yield();
			}
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
