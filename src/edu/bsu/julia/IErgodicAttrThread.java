package edu.bsu.julia;

import edu.bsu.julia.gui.JuliaError;
import edu.bsu.julia.session.Session;

public class IErgodicAttrThread extends Thread {
	
	private Julia parentFrame;
	private int progress;
	private boolean stop;
	
	public IErgodicAttrThread(Julia f) {
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
		

		
		for(int i = 0; i<functions.length; i++) {
			ComplexNumber w = seed.clone();
			ComplexNumber[] points = new ComplexNumber[iterations - skips];
			for(int j = 0; j<iterations; j++) {
				try {
					w = functions[i].evaluateForwards(w);
				}catch(ArithmeticException e) {
					new JuliaError(JuliaError.DIV_BY_ZERO, parentFrame);
					return;
				}
				if(j>=skips) points[j-skips] = w;
				progress++;
				if(stop) return;
			}
			InputFunction[] in = new InputFunction[1];
			in[0] = functions[i];
			OutputFunction outFn = new OutputFunction(s, in, OutputFunction.ERGODIC_ATTR,
					points);
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
