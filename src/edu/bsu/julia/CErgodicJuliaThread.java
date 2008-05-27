package edu.bsu.julia;

import java.util.Random;

import edu.bsu.julia.gui.JuliaError;
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
		InputFunction[] functions = 
			parentFrame.getInputPanel().getSelectedFunctions();
		if(functions.length==0) return;
		Session s = parentFrame.getCurrentSession();
		ComplexNumber seed = s.getSeedValue();
		int iterations = s.getIterations();
		int skips = s.getSkips();
		
			
		ComplexNumber start = seed.clone();
		ComplexNumber[] compositePoints = 
				new ComplexNumber[iterations];
		Random generator = new Random();
		int functionsLength = functions.length;

		for(int k = 0; k<iterations+skips; k++) {
			int randomIndex = generator.nextInt(functionsLength);
			try {
				start = functions[randomIndex].evaluateBackwardsRandom(start);
			}catch(ArithmeticException e) {
				new JuliaError(JuliaError.DIV_BY_ZERO, parentFrame);
				return;
			}
			if (start == null) {
				new JuliaError(JuliaError.ZERO_DETERMINANT, parentFrame);
				return;
			}
			if(k>=skips) compositePoints[k-skips] = start;
			progress++;
			if(stop) return;
		}
			
		OutputFunction compOutFn = new OutputFunction(s, functions, 
				OutputFunction.ERGODIC_JULIA, compositePoints);
		s.addOutputFunction(compOutFn);
		
		
		
	}
	
	public int getProgress() {
		return progress;
	}
	
	public void setStop() {
		stop = true;
	}

}
