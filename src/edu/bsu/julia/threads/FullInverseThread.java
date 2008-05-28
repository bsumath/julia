package edu.bsu.julia.threads;

import java.util.Vector;

import edu.bsu.julia.ComplexNumber;
import edu.bsu.julia.Julia;
import edu.bsu.julia.gui.JuliaError;
import edu.bsu.julia.input.InputFunction;
import edu.bsu.julia.output.InverseOutputFunction;
import edu.bsu.julia.output.OutputFunction;
import edu.bsu.julia.session.Session;

public class FullInverseThread extends Thread {

	private Julia parentFrame;
	private int progress;
	private volatile boolean stop;
	private InputFunction[] inputFns;
	private OutputFunction[] outputFns;

	public FullInverseThread(Julia f, InputFunction[] inFns,
			OutputFunction[] outFns) {
		parentFrame = f;
		progress = 0;
		stop = false;
		inputFns = inFns;
		outputFns = outFns;
	}

	public void run() {
		Session s = parentFrame.getCurrentSession();
		for (int i = 0; i < inputFns.length; i++) {
			Vector<ComplexNumber> result = new Vector<ComplexNumber>();
			for (int j = 0; j < outputFns.length; j++) {
				ComplexNumber[] points = outputFns[j].getPoints();
				for (int k = 0; k < points.length; k++) {
					ComplexNumber[] tempResult;
					try {
						tempResult = inputFns[i]
								.evaluateBackwardsFull(points[k]);
					} catch (ArithmeticException e) {
						new JuliaError(JuliaError.DIV_BY_ZERO, parentFrame);
						return;
					}
					if (tempResult == null) {
						new JuliaError(JuliaError.ZERO_DETERMINANT, parentFrame);
						return;
					}
					for (int m = 0; m < tempResult.length; m++) {
						result.add(tempResult[m]);
						progress++;
						Thread.yield();
					}

					if (stop)
						return;
					Thread.yield();
				}

				if (stop)
					return;
				Thread.yield();
			}
			InputFunction[] oneInputFn = { inputFns[i] };
			ComplexNumber[] resultPoints = new ComplexNumber[result.size()];
			result.toArray(resultPoints);
			InverseOutputFunction fn = new InverseOutputFunction(s, oneInputFn,
					outputFns, OutputFunction.Type.INVERSE_FULL_JULIA,
					resultPoints);
			s.addOutputFunction(fn);

			if (stop)
				return;
			Thread.yield();
		}
	}

	public int getProgress() {
		return progress;
	}

	public synchronized void setStop() {
		stop = true;
	}

}
