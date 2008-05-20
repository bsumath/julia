package edu.bsu.julia;

import java.util.Random;
import java.util.Vector;
import javax.swing.JOptionPane;

import edu.bsu.julia.gui.*;



public class PostCriticalThread extends Thread {
	
	private static final ComplexNumber NEG_ONE = new ComplexNumber(-1, 0);
	private static final ComplexNumber TWO = new ComplexNumber(2, 0);
	
	private Julia parentFrame;
	private int progress;
	private boolean stop;
	private int tValue;
	
	public PostCriticalThread(Julia f, int t) {
		parentFrame = f;
		progress = 0;
		stop = false;
		tValue = t;
	}
	
	public void run() {
		ComplexNumber seed=new ComplexNumber(0,0);
		int flag=0, rFlag=0;
		InputFunction[] functions = 
			parentFrame.getInputPanel().getSelectedFunctions();
		if(functions.length==0) return;
		int functionsLength = functions.length;
		Session s = parentFrame.getCurrentSession();


		
		if(functionsLength>0) {
			Vector<ComplexNumber> compositePoints = new Vector<ComplexNumber>();
			Vector<ComplexNumber> interimPoints = new Vector<ComplexNumber>();

			Vector<ComplexNumber> startVector = new Vector<ComplexNumber>();
			for(int vi=0; vi<functions.length; vi++){
				if (functions[vi].getType()==1) {
					flag = 1;
					rFlag = 1;
				}
				else if (functions[vi].getType()==2) {
					ComplexNumber[] coefficients= functions[vi].getCoefficients();
					seed= (coefficients[1].multiply(NEG_ONE)).divide(coefficients[0].multiply(TWO));
					flag = 1;
					rFlag = 1;
				}
				if (flag == 1) {
					ComplexNumber start = seed.clone();
					for(int seedi=0; seedi<functions[vi].getM(); seedi++){			
						start=functions[vi].evaluateFunction(start);
						interimPoints.add(start);
						startVector.add(start);
				
					}
					flag = 0;
				}

			}
			if (rFlag == 0) {
				JOptionPane.showMessageDialog(parentFrame, "No critical Points exist");
				return;
			
			}
	
			for (int ti=0; ti<tValue-1; ti++)
			{
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
				for(int i = 0; i<compositePoints.size(); i++) {
					interimPoints.add(compositePoints.elementAt(i));
					startVector.add(compositePoints.elementAt(i));
				}
			}
			progress = progress + startVector.size();
			ComplexNumber[] compOutArray = 
				new ComplexNumber[startVector.size()];
			for(int x = 0; x<compOutArray.length; x++) 
				compOutArray[x] = startVector.elementAt(x);
			OutputFunction compOutFn = new OutputFunction(s, functions, 
					OutputFunction.POST_CRITICAL, compOutArray);
			s.addOutputFunction(compOutFn);
		}
		

	}
	
	public int getProgress() {
		return progress;
	}
	
	public void setStop() {
		stop = true;
	}

}
