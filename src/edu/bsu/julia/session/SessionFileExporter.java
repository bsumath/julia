package edu.bsu.julia.session;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Vector;

import edu.bsu.julia.ComplexNumber;
import edu.bsu.julia.CubicInputFunction;
import edu.bsu.julia.InputFunction;
import edu.bsu.julia.LinearInputFunction;
import edu.bsu.julia.MobiusInputFunction;
import edu.bsu.julia.OutputFunction;
import edu.bsu.julia.QuadraticInputFunction;
import edu.bsu.julia.RealAfflineLinearInputFunction;
import edu.bsu.julia.session.Session.Exporter;

public class SessionFileExporter implements Exporter {

	private Vector<InputFunction> inputFunctions;
	private int iterations;
	private Vector<OutputFunction> outputFunctions;
	private ComplexNumber seed;
	private int skips;

	public void addInputFunctions(Vector<InputFunction> i) {
		inputFunctions = i;
	}

	public void addIterations(int i) {
		iterations = i;
	}

	public void addOutputFunctions(Vector<OutputFunction> o) {
		outputFunctions = o;
	}

	public void addSeedValue(ComplexNumber s) {
		seed = s;
	}

	public void addSkips(int s) {
		skips = s;
	}

	public void writeToFile(File f) throws IOException {
		FileOutputStream out = new FileOutputStream(f);
		PrintStream ps = new PrintStream(out);

		ps.println(iterations);
		ps.println(skips);
		ps.println(seed.getX());
		ps.println(seed.getY());
		ps.println(inputFunctions.size());

		for (int i = 0; i < inputFunctions.size(); i++) {
			if (inputFunctions.elementAt(i) instanceof LinearInputFunction) {
				LinearInputFunction lFn = (LinearInputFunction) inputFunctions
						.elementAt(i);
				ps.println("linear");
				ps.println(lFn.getM());
				ComplexNumber[] variables = lFn.getCoefficients();
				for (int j = 0; j < variables.length; j++) {
					ps.println(variables[j].getX());
					ps.println(variables[j].getY());
				}
			} else if (inputFunctions.elementAt(i) instanceof CubicInputFunction) {
				CubicInputFunction cFn = (CubicInputFunction) inputFunctions
						.elementAt(i);
				ps.println("cubic");
				ps.println(cFn.getM());
				ComplexNumber[] variables = cFn.getCoefficients();
				for (int j = 0; j < variables.length; j++) {
					ps.println(variables[j].getX());
					ps.println(variables[j].getY());
				}
			} else if (inputFunctions.elementAt(i) instanceof RealAfflineLinearInputFunction) {
				RealAfflineLinearInputFunction mFn = (RealAfflineLinearInputFunction) inputFunctions
						.elementAt(i);
				ps.println("matrix");
				ps.println(mFn.getM());
				ComplexNumber[] variables = mFn.getCoefficients();
				for (int j = 0; j < variables.length; j++) {
					ps.println(variables[j].getX());
					ps.println(variables[j].getY());
				}
			} else if (inputFunctions.elementAt(i) instanceof MobiusInputFunction) {
				MobiusInputFunction bFn = (MobiusInputFunction) inputFunctions
						.elementAt(i);
				ps.println("mobius");
				ps.println(bFn.getM());
				ComplexNumber[] variables = bFn.getCoefficients();
				for (int j = 0; j < variables.length; j++) {
					ps.println(variables[j].getX());
					ps.println(variables[j].getY());
				}
			} else if (inputFunctions.elementAt(i) instanceof QuadraticInputFunction) {
				QuadraticInputFunction qFn = (QuadraticInputFunction) inputFunctions
						.elementAt(i);
				ps.println("quad");
				ps.println(qFn.getM());
				ComplexNumber[] variables = qFn.getCoefficients();
				for (int j = 0; j < variables.length; j++) {
					ps.println(variables[j].getX());
					ps.println(variables[j].getY());
				}
			}
		}
		ps.close();
		out.close();

	}
}
