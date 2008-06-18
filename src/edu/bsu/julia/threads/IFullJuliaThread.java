package edu.bsu.julia.threads;

import java.util.Vector;

import edu.bsu.julia.ComplexNumber;
import edu.bsu.julia.Julia;
import edu.bsu.julia.gui.JuliaError;
import edu.bsu.julia.input.CubicInputFunction;
import edu.bsu.julia.input.InputFunction;
import edu.bsu.julia.input.QuadraticInputFunction;
import edu.bsu.julia.output.OutputFunction;
import edu.bsu.julia.session.Session;

public class IFullJuliaThread extends Thread {

	private Julia parentFrame;
	private int progress;
	private boolean stop;

	public IFullJuliaThread(Julia f) {
		parentFrame = f;
		progress = 0;
		stop = false;
	}

	public void run() {
		try {
			InputFunction[] functions = parentFrame.getInputPanel()
					.getSelectedFunctions();
			int functionsLength = functions.length;
			if (functions.length == 0)
				return;
			Session s = parentFrame.getCurrentSession();
			ComplexNumber seed = s.getSeedValue();
			int iterations = s.getIterations();

			for (int m = 0; m < functionsLength; m++) {
				ComplexNumber w = seed.clone();
				Vector<ComplexNumber> results = new Vector<ComplexNumber>();
				Vector<ComplexNumber> interimResults = new Vector<ComplexNumber>();
				interimResults.add(w);
				if (functions[m] instanceof CubicInputFunction
						|| functions[m] instanceof QuadraticInputFunction) {
					do {
						results.clear();
						for (int n = 0; n < interimResults.size(); n++) {
							ComplexNumber[] tempResults;
							try {
								tempResults = functions[m]
										.evaluateBackwardsFull(interimResults
												.elementAt(n));
							} catch (ArithmeticException e) {
								JuliaError.DIV_BY_ZERO.showDialog(parentFrame);
								return;
							}
							for (int p = 0; p < tempResults.length; p++) {
								results.add(tempResults[p]);
								Thread.yield();
							}
							// progress = progress + tempResults.length;
							if (stop)
								return;
							Thread.yield();
						}
						interimResults.clear();
						for (int i = 0; i < results.size(); i++) {
							interimResults.add(results.elementAt(i));
							Thread.yield();
						}
						Thread.yield();
					} while (results.size() < iterations);
				} else {
					int counter = 0;
					do {
						results.clear();
						for (int n = 0; n < interimResults.size(); n++) {
							ComplexNumber[] tempResults;
							try {
								tempResults = functions[m]
										.evaluateBackwardsFull(interimResults
												.elementAt(n));
							} catch (ArithmeticException e) {
								JuliaError.DIV_BY_ZERO.showDialog(parentFrame);
								return;
							}
							if (tempResults == null) {
								JuliaError.ZERO_DETERMINANT
										.showDialog(parentFrame);
								return;
							}
							for (int p = 0; p < tempResults.length; p++) {
								results.add(tempResults[p]);
								Thread.yield();
							}
							// progress = progress + tempResults.length;
							if (stop)
								return;
							Thread.yield();
						}
						interimResults.clear();
						for (int i = 0; i < results.size(); i++) {
							interimResults.add(results.elementAt(i));
							Thread.yield();
						}
						counter++;
						Thread.yield();
					} while (counter <= iterations);
				}
				InputFunction[] in = new InputFunction[1];
				in[0] = functions[m];
				ComplexNumber[] resultsOutArray = new ComplexNumber[results
						.size()];
				for (int y = 0; y < resultsOutArray.length; y++)
					resultsOutArray[y] = results.elementAt(y);
				OutputFunction outFn = new OutputFunction(s, in,
						OutputFunction.Type.IND_FULL_JULIA, resultsOutArray);
				s.addOutputFunction(outFn);
			}
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
