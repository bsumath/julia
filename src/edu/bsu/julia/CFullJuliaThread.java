package edu.bsu.julia;

import java.util.*;

import edu.bsu.julia.gui.*;

public class CFullJuliaThread extends Thread {
	
	private Julia parentFrame;
	private int progress;
	private boolean stop;
	
	public CFullJuliaThread(Julia f) {
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
		

		ComplexNumber start = seed.clone();
		Vector<ComplexNumber> compositePoints = new Vector<ComplexNumber>();
		Vector<ComplexNumber> interimPoints = new Vector<ComplexNumber>();
		interimPoints.add(start);
		do{
			compositePoints.clear();
			for(int i = 0; i<interimPoints.size(); i++) {
				for(int j = 0; j<functionsLength; j++) {
					ComplexNumber[] interResults;
					try {
						interResults = functions[j].evaluateBackwardsFull
							(interimPoints.elementAt(i));
					}catch(ArithmeticException e) {
						new JuliaError(JuliaError.DIV_BY_ZERO, parentFrame);
						return;
					}
					if (interResults == null) {
						new JuliaError(JuliaError.ZERO_DETERMINANT, parentFrame);
						return;
					}
					for(int k = 0; k<interResults.length; k++) 
						compositePoints.add(interResults[k]);
					if(stop) return;
				}
			}
			interimPoints.clear();
			for(int i = 0; i<compositePoints.size(); i++) 
				interimPoints.add(compositePoints.elementAt(i));
		}while(compositePoints.size()<iterations);
		progress = progress + compositePoints.size();
		ComplexNumber[] compOutArray = 
			new ComplexNumber[compositePoints.size()];
		for(int x = 0; x<compOutArray.length; x++) 
			compOutArray[x] = compositePoints.elementAt(x);
		OutputFunction compOutFn = new OutputFunction(s, functions, 
				OutputFunction.FULL_JULIA, compOutArray);
		s.addOutputFunction(compOutFn);
	}
	
	
	public int getProgress() {
		return progress;
	}
	
	public void setStop() {
		stop = true;
	}

}
