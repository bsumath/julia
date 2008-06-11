package edu.bsu.julia.threads;

import java.util.Random;

import edu.bsu.julia.ComplexNumber;
import edu.bsu.julia.Julia;
import edu.bsu.julia.gui.JuliaError;
import edu.bsu.julia.input.InputFunction;
import edu.bsu.julia.output.OutputFunction;
import edu.bsu.julia.session.Session;

public class ErgodicAttrThread extends Thread {

	private Julia parentFrame;
	private int progress;
	private boolean stop;

	public ErgodicAttrThread(Julia f) {
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
		int skips = s.getSkips();

		if (functionsLength > 1) {
			ComplexNumber start = seed.clone();
			ComplexNumber[] compositePoints = new ComplexNumber[iterations
					- skips];
			Random generator = new Random();
			for (int k = 0; k < iterations; k++) {
				int randomIndex = generator.nextInt(functionsLength);
				try {
					start = functions[randomIndex].evaluateForwards(start);
				} catch (ArithmeticException e) {
					new JuliaError(JuliaError.DIV_BY_ZERO, parentFrame);
					return;
				}
				if (k >= skips)
					compositePoints[k - skips] = start;
				progress++;
				if (stop)
					return;
				Thread.yield();
			}
			OutputFunction compOutFn = new OutputFunction(s, functions,
					OutputFunction.Type.ERGODIC_ATTR, compositePoints);
			s.addOutputFunction(compOutFn);
		}

		for (int i = 0; i < functions.length; i++) {
			ComplexNumber w = seed.clone();
			ComplexNumber[] points = new ComplexNumber[iterations - skips];
			for (int j = 0; j < iterations; j++) {
				try {
					w = functions[i].evaluateForwards(w);
				} catch (ArithmeticException e) {
					new JuliaError(JuliaError.DIV_BY_ZERO, parentFrame);
					return;
				}
				if (j >= skips)
					points[j - skips] = w;
				progress++;
				if (stop)
					return;
				Thread.yield();
			}
			InputFunction[] in = new InputFunction[1];
			in[0] = functions[i];
			OutputFunction outFn = new OutputFunction(s, in,
					OutputFunction.Type.IND_ERGODIC_ATTR, points);
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
