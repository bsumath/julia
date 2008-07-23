package edu.bsu.julia.session;

import java.util.Vector;

import edu.bsu.julia.ComplexNumber;
import edu.bsu.julia.input.InputFunction;
import edu.bsu.julia.input.LinearInputFunction;
import edu.bsu.julia.output.OutputSet;
import edu.bsu.julia.session.Session.Importer;

public class EmptySessionImporter implements Importer {
	private static final int DEFAULT_ITERATIONS = 50000;
	private static final ComplexNumber DEFAULT_SEED = new ComplexNumber(1, 0);
	private static final int DEFAULT_SKIPS = 20;

	private final int iterations;
	private final int skips;
	private final ComplexNumber seed;
	private final Vector<InputFunction> inputFunctions = new Vector<InputFunction>();

	public EmptySessionImporter() {
		iterations = DEFAULT_ITERATIONS;
		skips = DEFAULT_SKIPS;
		seed = DEFAULT_SEED;

		ComplexNumber a = new ComplexNumber(2, 0);
		ComplexNumber b = new ComplexNumber(0, 0);
		ComplexNumber c = new ComplexNumber(-1, 0);
		ComplexNumber d = new ComplexNumber(-.5, -0.866);

		InputFunction function = new LinearInputFunction(1, a, b);
		function.setSubscript(1);
		inputFunctions.add(function);

		function = new LinearInputFunction(1, a, c);
		function.setSubscript(2);
		inputFunctions.add(function);

		function = new LinearInputFunction(1, a, d);
		function.setSubscript(3);
		inputFunctions.add(function);
	}

	public EmptySessionImporter(int iter, int sk, ComplexNumber sd) {
		iterations = iter;
		skips = sk;
		seed = sd;
	}

	public Vector<InputFunction> provideInputFunctions() {
		return inputFunctions;
	}

	public Integer provideIterations() {
		return iterations;
	}

	public Vector<OutputSet> provideOutputSets() {
		return new Vector<OutputSet>();
	}

	public ComplexNumber provideSeedValue() {
		return seed;
	}

	public Integer provideSkips() {
		return skips;
	}

	public int provideInputSubscript() {
		return inputFunctions.size();
	}

	public int provideOutputSubscript() {
		return 0;
	}

	public int[] provideSelectedInputIndices() {
		return new int[] {0,1,2};
	}

	public String provideSelectedMethod() {
		return "";
	}

	public int[] provideSelectedOutputIndices() {
		return new int[] {};
	}

	public String provideSelectedType() {
		return "";
	}
}
