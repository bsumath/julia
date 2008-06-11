package edu.bsu.julia.threads;

import java.util.Vector;

import edu.bsu.julia.ComplexNumber;
import edu.bsu.julia.Julia;
import edu.bsu.julia.gui.JuliaError;
import edu.bsu.julia.input.InputFunction;
import edu.bsu.julia.output.OutputFunction;
import edu.bsu.julia.session.Session;

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
		InputFunction[] functions = parentFrame.getInputPanel()
				.getSelectedFunctions();
		int functionsLength = functions.length;
		if (functions.length == 0)
			return;
		Session s = parentFrame.getCurrentSession();
		ComplexNumber seed = s.getSeedValue();
		int iterations = s.getIterations();

		ComplexNumber start = seed.clone();
		Vector<ComplexNumber> compositePoints = new Vector<ComplexNumber>();
		Vector<ComplexNumber> interimPoints = new Vector<ComplexNumber>();
		interimPoints.add(start);
		
		int iterationCounter = 0;
		do {
			compositePoints.clear();
			for (int i = 0; i < interimPoints.size(); i++) {
				for (int j = 0; j < functionsLength; j++) {
					ComplexNumber[] interResults;
					try {
						interResults = functions[j]
								.evaluateBackwardsFull(interimPoints
										.elementAt(i));
					} catch (ArithmeticException e) {
						new JuliaError(JuliaError.DIV_BY_ZERO, parentFrame);
						return;
					}
					if (interResults == null) {
						new JuliaError(JuliaError.ZERO_DETERMINANT, parentFrame);
						return;
					}
					for (int k = 0; k < interResults.length; k++){
						compositePoints.add(interResults[k]);
						Thread.yield();
					}
					if (stop)
						return;
					Thread.yield();
				}
				Thread.yield();
			}
			interimPoints.clear();
			for (int i = 0; i < compositePoints.size(); i++){
				interimPoints.add(compositePoints.elementAt(i));
				Thread.yield();
			}
			Thread.yield();
			iterationCounter += 1;
		} while (!(functions.length == 1 && iterationCounter >= iterations)
				&& compositePoints.size() < iterations);
		
		progress = progress + compositePoints.size();
		ComplexNumber[] compOutArray = new ComplexNumber[compositePoints.size()];
		for (int x = 0; x < compOutArray.length; x++)
			compOutArray[x] = compositePoints.elementAt(x);
		OutputFunction compOutFn = new OutputFunction(s, functions,
				OutputFunction.Type.FULL_JULIA, compOutArray);
		s.addOutputFunction(compOutFn);
	}

	public int getProgress() {
		return progress;
	}

	public void setStop() {
		stop = true;
	}

}
