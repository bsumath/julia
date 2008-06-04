package edu.bsu.julia.threads;

import java.util.Vector;

import javax.swing.JList;

import edu.bsu.julia.ComplexNumber;
import edu.bsu.julia.Julia;
import edu.bsu.julia.gui.JuliaError;
import edu.bsu.julia.input.InputFunction;
import edu.bsu.julia.output.InverseOutputFunction;
import edu.bsu.julia.output.OutputFunction;

public class ForwardImageThread extends Thread {

	private Julia parentFrame;
	private int progress;
	private volatile boolean stop;

	public ForwardImageThread(Julia f) {
		parentFrame = f;
		progress = 0;
		stop = false;
	}

	public void run() {
		InputFunction[] inputFns = parentFrame.getInputPanel()
				.getSelectedFunctions();
		JList outList = parentFrame.getOutputFunctionList();
		Object[] outObjs = outList.getSelectedValues();
		outList.clearSelection();
		OutputFunction[] outputFns = new OutputFunction[outObjs.length];
		for (int i = 0; i < outputFns.length; i++) {
			outputFns[i] = (OutputFunction) outObjs[i];
			Thread.yield();
		}

		ComplexNumber x;
		for (int i = 0; i < inputFns.length; i++) {
			Vector<ComplexNumber> result = new Vector<ComplexNumber>();
			for (int j = 0; j < outputFns.length; j++) {
				ComplexNumber[] points = outputFns[j].getPoints();
				for (int k = 0; k < points.length; k++) {
					try {
						x = inputFns[i].evaluateForwards(points[k]);
					} catch (ArithmeticException e) {
						new JuliaError(JuliaError.DIV_BY_ZERO, parentFrame);
						return;
					}
					result.add(x);
					progress++;
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
			InverseOutputFunction fn = new InverseOutputFunction(parentFrame
					.getCurrentSession(), oneInputFn,
					OutputFunction.Type.INVERSE_ATTR, resultPoints, outputFns);
			parentFrame.getCurrentSession().addOutputFunction(fn);
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
