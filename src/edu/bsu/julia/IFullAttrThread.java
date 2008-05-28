package edu.bsu.julia;

import edu.bsu.julia.gui.JuliaError;
import edu.bsu.julia.session.Session;

public class IFullAttrThread extends Thread {

	private Julia parentFrame;
	private int progress;
	private boolean stop;

	public IFullAttrThread(Julia f) {
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
