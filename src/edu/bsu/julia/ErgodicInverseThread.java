package edu.bsu.julia;

import java.util.Vector;
import edu.bsu.julia.gui.*;

public class ErgodicInverseThread extends Thread {
	
	private Julia parentFrame;
	private int progress;
	private boolean stop;
	private InputFunction[] inputFns;
	private OutputFunction[] outputFns;
	
	public ErgodicInverseThread(Julia f, InputFunction[] inFns,
			OutputFunction[] outFns) {
		parentFrame = f;
		progress = 0;
		stop = false;
		inputFns = inFns;
		outputFns = outFns;
	}
	
	public void run() {
		ComplexNumber x;
		for(int i = 0; i<inputFns.length; i++) {
			Vector<ComplexNumber> result = new Vector<ComplexNumber>();
			for(int j = 0; j<outputFns.length; j++) {
				ComplexNumber[] points = outputFns[j].getPoints();
				for(int k = 0; k<points.length; k++) {
					try {
						x = inputFns[i].evaluateBackwardsRandom(points[k]);
					}catch(ArithmeticException e) {
						new JuliaError(JuliaError.DIV_BY_ZERO, parentFrame);
						return;
					}
					if (x == null) {
						new JuliaError(JuliaError.ZERO_DETERMINANT, parentFrame);
						return;
					}
					result.add(x);
					progress++;
				}
			}
			InputFunction[] oneInputFn = {inputFns[i]};
			ComplexNumber[] resultPoints = new ComplexNumber[result.size()];
			result.toArray(resultPoints);
			InverseOutputFunction fn = new InverseOutputFunction
				(parentFrame.getCurrentSession(), oneInputFn, outputFns,
				OutputFunction.INVERSE_ERGODIC_JULIA, resultPoints);
			parentFrame.getCurrentSession().addOutputFunction(fn);
		}
	}
	
	public int getProgress() {
		return progress;
	}
	
	public void setStop() {
		stop = true;
	}

}
