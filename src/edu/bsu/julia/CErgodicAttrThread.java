package edu.bsu.julia;

import java.util.Random;

import edu.bsu.julia.gui.JuliaError;
import edu.bsu.julia.session.Session;

public class CErgodicAttrThread extends Thread {
	
	private Julia parentFrame;
	private int progress;
	private boolean stop;
	
	public CErgodicAttrThread(Julia f) {
		parentFrame = f;
		progress = 0;
		stop = false;
	}
	
	public void run() {
		InputFunction[] functions = 
			parentFrame.getInputPanel().getSelectedFunctions();
		if(functions.length==0) return;
		int functionsLength = functions.length;
		Session s = parentFrame.getCurrentSession();
		ComplexNumber seed = s.getSeedValue();
		int iterations = s.getIterations();
		int skips = s.getSkips();
		

		ComplexNumber start = seed.clone();
		ComplexNumber[] compositePoints = 
			new ComplexNumber[iterations];
		Random generator = new Random();
		for(int k = 0; k<iterations+skips; k++) {
			int randomIndex = generator.nextInt(functionsLength);
			try {
				start = functions[randomIndex].evaluateForwards(start);
			}catch(ArithmeticException e) {
				new JuliaError(JuliaError.DIV_BY_ZERO, parentFrame);
				return;
			}
			if(k>=skips) compositePoints[k-skips] = start;
			progress++;
			if(stop) return;
			Thread.yield();
		}
		OutputFunction compOutFn = new OutputFunction(s, functions, 
				OutputFunction.Type.ERGODIC_ATTR, compositePoints);
		s.addOutputFunction(compOutFn);

		
	}
	
	public int getProgress() {
		return progress;
	}
	
	public void setStop() {
		stop = true;
	}

}
