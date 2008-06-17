package edu.bsu.julia.threads;

import java.util.Random;

import edu.bsu.julia.ComplexNumber;
import edu.bsu.julia.Julia;
import edu.bsu.julia.gui.JuliaError;
import edu.bsu.julia.input.InputFunction;
import edu.bsu.julia.output.OutputFunction;
import edu.bsu.julia.session.Session;

public class CErgodicJuliaThread extends Thread {

	private Julia parentFrame;
	private int progress;
	private boolean stop;

	public CErgodicJuliaThread(Julia f) {
		parentFrame = f;
		progress = 0;
		stop = false;
	}

	public void run() {
		InputFunction[] functions = parentFrame.getInputPanel()
				.getSelectedFunctions();
		if (functions.length == 0)
			return;
		Session s = parentFrame.getCurrentSession();
		ComplexNumber seed = s.getSeedValue();
		int iterations = s.getIterations();
		int skips = s.getSkips();

		ComplexNumber start = seed.clone();
		ComplexNumber[] compositePoints = new ComplexNumber[iterations];
		Random generator = new Random();
		int functionsLength = functions.length;

		for (int k = 0; k < iterations + skips; k++) {
			int randomIndex = generator.nextInt(functionsLength);
			try {
				start = functions[randomIndex].evaluateBackwardsRandom(start);
			} catch (ArithmeticException e) {
				JuliaError.DIV_BY_ZERO.showDialog(parentFrame);
				return;
			}
			if (start == null) {
				JuliaError.ZERO_DETERMINANT.showDialog(parentFrame);
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
				OutputFunction.Type.ERGODIC_JULIA, compositePoints);
		s.addOutputFunction(compOutFn);

	}

	public int getProgress() {
		return progress;
	}

	public void setStop() {
		stop = true;
	}

}
