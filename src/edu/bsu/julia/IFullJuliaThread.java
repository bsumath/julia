package edu.bsu.julia;

import java.util.Vector;
import edu.bsu.julia.gui.*;

public class IFullJuliaThread extends Thread {
	
	private Julia parentFrame;
	private int progress;
	private boolean stop;
	
	public IFullJuliaThread(Julia f) {
		parentFrame = f;
		progress = 0;
		stop = false;
	}
	
	public void run() {
		InputFunction[] functions = 
			parentFrame.getInputPanel().getSelectedFunctions();
		int functionsLength = functions.length;
		if(functions.length==0) return;
		Session s = parentFrame.getCurrentSession();
		ComplexNumber seed = s.getSeedValue();
		int iterations = s.getIterations();
		

		
		for(int m = 0; m<functionsLength; m++) {
			ComplexNumber w = seed.clone();
			Vector<ComplexNumber> results = new Vector<ComplexNumber>();
			Vector<ComplexNumber> interimResults = new Vector<ComplexNumber>();
			interimResults.add(w);
			if(functions[m].getClass().getName().equals
					("edu.bsu.julia.CubicInputFunction") || 
					functions[m].getClass().getName().equals
					("edu.bsu.julia.QuadraticInputFunction")) {
				do {
					results.clear();
					for(int n = 0; n<interimResults.size(); n++) {
						ComplexNumber[] tempResults;
						try {
							tempResults = functions[m].evaluateBackwardsFull
								(interimResults.elementAt(n));
						}catch(ArithmeticException e) {
							new JuliaError(JuliaError.DIV_BY_ZERO, parentFrame);
							return;
						}
						for(int p = 0; p<tempResults.length; p++) 
							results.add(tempResults[p]);
						//progress = progress + tempResults.length;
						if(stop) return;
					}
					interimResults.clear();
					for(int i = 0; i<results.size(); i++) 
						interimResults.add(results.elementAt(i));
				}while(results.size()<iterations);
			}
			else {
				int counter = 0;
				do {
					results.clear();
					for(int n = 0; n<interimResults.size(); n++) {
						ComplexNumber[] tempResults;
						try {
							tempResults = functions[m].evaluateBackwardsFull
								(interimResults.elementAt(n));
						}catch(ArithmeticException e) {
							new JuliaError(JuliaError.DIV_BY_ZERO, parentFrame);
							return;
						}
						if (tempResults == null) {
							new JuliaError(JuliaError.ZERO_DETERMINANT, parentFrame);
							return;
						}
						for(int p = 0; p<tempResults.length; p++) 
							results.add(tempResults[p]);
						//progress = progress + tempResults.length;
						if(stop) return;
					}
					interimResults.clear();
					for(int i = 0; i<results.size(); i++) 
						interimResults.add(results.elementAt(i));
					counter++;
				}while(counter<=iterations);
			}
			InputFunction[] in = new InputFunction[1];
			in[0] = functions[m];
			ComplexNumber[] resultsOutArray = new ComplexNumber[results.size()];
			for(int y = 0; y<resultsOutArray.length; y++) 
				resultsOutArray[y] = results.elementAt(y);
			OutputFunction outFn = new OutputFunction(s, in, OutputFunction.FULL_JULIA,
					resultsOutArray);
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
