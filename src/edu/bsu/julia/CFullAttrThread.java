package edu.bsu.julia;

import java.util.Vector;

import edu.bsu.julia.gui.JuliaError;
import edu.bsu.julia.session.Session;

public class CFullAttrThread extends Thread {
	
	private Julia parentFrame;
	private int progress;
	private boolean stop;
	
	public CFullAttrThread(Julia f) {
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
		
	
			ComplexNumber start = seed.clone();
			Vector<ComplexNumber> compositePoints = new Vector<ComplexNumber>();
			Vector<ComplexNumber> interimPoints = new Vector<ComplexNumber>();
			interimPoints.add(start);
			
			do{
				compositePoints.clear();
				for(int i = 0; i<interimPoints.size(); i++) {
					for(int j = 0; j<functionsLength; j++) {
						try {
							compositePoints.add(functions[j].evaluateForwards(interimPoints.elementAt(i)));
						}catch(ArithmeticException e) {
							new JuliaError(JuliaError.DIV_BY_ZERO, parentFrame);
							return;
						}
						if(stop) return;
					}
				}
				interimPoints.clear();
				for(int i = 0; i<compositePoints.size(); i++) 
					interimPoints.add(compositePoints.elementAt(i));
			}while(compositePoints.size()<=iterations);
			progress = progress + compositePoints.size();
			ComplexNumber[] compOutArray = 
				new ComplexNumber[compositePoints.size()];
			for(int x = 0; x<compOutArray.length; x++) 
				compOutArray[x] = compositePoints.elementAt(x);
			OutputFunction compOutFn = new OutputFunction(s, functions, 
					OutputFunction.FULL_ATTR, compOutArray);
			s.addOutputFunction(compOutFn);
		
		
		
	}
	
	public int getProgress() {
		return progress;
	}
	
	public void setStop() {
		stop = true;
	}

}
