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

public class FullJuliaThread extends Thread {

	private Julia parentFrame;
	private int progress;
	private boolean stop;

	public FullJuliaThread(Julia f) {
		parentFrame = f;
		progress = 0;
		stop = false;
	}

	public void run() {
		InputFunction[] functions = parentFrame.getInputPanel()
				.getSelectedFunctions();
		int functionsLength = functions.length;
		if (functions.length == 0)
			return;
		Session s = parentFrame.getCurrentSession();
		ComplexNumber seed = s.getSeedValue();
		int iterations = s.getIterations();

		if (functions.length > 1) {
			ComplexNumber start = seed.clone();
			Vector<ComplexNumber> compositePoints = new Vector<ComplexNumber>();
			Vector<ComplexNumber> interimPoints = new Vector<ComplexNumber>();
			interimPoints.add(start);
			do {
				compositePoints.clear();
				for (int i = 0; i < interimPoints.size(); i++) {
					for (int j = 0; j < functionsLength; j++) {
						ComplexNumber[] interResults;
						try {
							interResults = functions[j]
									.evaluateBackwardsFull(interimPoints
											.elementAt(i));
						} catch (ArithmeticException e) {
							JuliaError.DIV_BY_ZERO.showDialog(parentFrame);
							return;
						}
						if (interResults == null) {
							JuliaError.ZERO_DETERMINANT.showDialog(parentFrame);
							return;
						}
						for (int k = 0; k < interResults.length; k++) {
							compositePoints.add(interResults[k]);
							Thread.yield();
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
			} while (compositePoints.size() < iterations);
			progress = progress + compositePoints.size();
			ComplexNumber[] compOutArray = new ComplexNumber[compositePoints
					.size()];
			for (int x = 0; x < compOutArray.length; x++) {
				compOutArray[x] = compositePoints.elementAt(x);
				Thread.yield();
			}
			OutputFunction compOutFn = new OutputFunction(s, functions,
					OutputFunction.Type.FULL_JULIA, compOutArray);
			s.addOutputFunction(compOutFn);
		}

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
							JuliaError.ZERO_DETERMINANT.showDialog(parentFrame);
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
					counter++;
				} while (counter <= iterations);
			}
			InputFunction[] in = new InputFunction[1];
			in[0] = functions[m];
			ComplexNumber[] resultsOutArray = new ComplexNumber[results.size()];
			for (int y = 0; y < resultsOutArray.length; y++) {
				resultsOutArray[y] = results.elementAt(y);
				Thread.yield();
			}
			OutputFunction outFn = new OutputFunction(s, in,
					OutputFunction.Type.IND_FULL_JULIA, resultsOutArray);
			s.addOutputFunction(outFn);
		}
	}

	public int getProgress() {
		return progress;
	}

	public void setStop() {
		stop = true;
	}

}
