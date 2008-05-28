package edu.bsu.julia.threads;

import java.util.Vector;

import edu.bsu.julia.ComplexNumber;
import edu.bsu.julia.Julia;
import edu.bsu.julia.gui.JuliaError;
import edu.bsu.julia.input.InputFunction;
import edu.bsu.julia.output.OutputFunction;
import edu.bsu.julia.session.Session;

public class FullAttrThread extends Thread {

	private Julia parentFrame;
	private int progress;
	private boolean stop;

	public FullAttrThread(Julia f) {
		parentFrame = f;
		progress = 0;
		stop = false;
	}

	public void run() {
		InputFunction[] functions = parentFrame.getInputPanel()
				.getSelectedFunctions();
		if (functions.length == 0)
			return;
		int functionsLength = functions.length;
		Session s = parentFrame.getCurrentSession();
		ComplexNumber seed = s.getSeedValue();
		int iterations = s.getIterations();

		if (functionsLength > 1) {
			ComplexNumber start = seed.clone();
			Vector<ComplexNumber> compositePoints = new Vector<ComplexNumber>();
			Vector<ComplexNumber> interimPoints = new Vector<ComplexNumber>();
			interimPoints.add(start);

			do {
				compositePoints.clear();
				for (int i = 0; i < interimPoints.size(); i++) {
					for (int j = 0; j < functionsLength; j++) {
						try {
							compositePoints.add(functions[j]
									.evaluateForwards(interimPoints
											.elementAt(i)));
						} catch (ArithmeticException e) {
							new JuliaError(JuliaError.DIV_BY_ZERO, parentFrame);
							return;
						}
						if (stop)
							return;
						Thread.yield();
					}
					Thread.yield();
				}
				interimPoints.clear();
				for (int i = 0; i < compositePoints.size(); i++){
					interimPoints.add(compositePoints.elementAt(i));
					Thread.yield();
				}
				Thread.yield();
			} while (compositePoints.size() <= iterations);
			progress = progress + compositePoints.size();
			ComplexNumber[] compOutArray = new ComplexNumber[compositePoints
					.size()];
			for (int x = 0; x < compOutArray.length; x++){
				compOutArray[x] = compositePoints.elementAt(x);
				Thread.yield();
			}
			OutputFunction compOutFn = new OutputFunction(s, functions,
					OutputFunction.Type.FULL_ATTR, compOutArray);
			s.addOutputFunction(compOutFn);
		}

		for (int m = 0; m < functionsLength; m++) {
			ComplexNumber w = seed.clone();
			ComplexNumber[] results = new ComplexNumber[iterations];
			for (int n = 0; n < iterations; n++) {
				try {
					w = functions[m].evaluateForwards(w);
				} catch (ArithmeticException e) {
					new JuliaError(JuliaError.DIV_BY_ZERO, parentFrame);
					return;
				}
				results[n] = w;
				progress++;
				if (stop)
					return;
				Thread.yield();
			}
			ComplexNumber finalResults[] = { results[iterations - 1] };
			InputFunction[] in = new InputFunction[1];
			in[0] = functions[m];
			OutputFunction outFn = new OutputFunction(s, in,
					OutputFunction.Type.FULL_ATTR, finalResults);
			s.addOutputFunction(outFn);
			Thread.yield();
		}
	}

	public int getProgress() {
		return progress;
	}

	public void setStop() {
		stop = true;
	}

}
