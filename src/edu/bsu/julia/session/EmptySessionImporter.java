package edu.bsu.julia.session;

import java.util.Vector;

import edu.bsu.julia.ComplexNumber;
import edu.bsu.julia.input.InputFunction;
import edu.bsu.julia.input.LinearInputFunction;
import edu.bsu.julia.output.OutputFunction;
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
		function.setSubscript(inputFunctions.size());
		inputFunctions.add(function);

		function = new LinearInputFunction(1, a, c);
		function.setSubscript(inputFunctions.size());
		inputFunctions.add(function);
		
		function = new LinearInputFunction(1, a, d);
		function.setSubscript(inputFunctions.size());
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

	public int provideIterations() {
		return iterations;
	}

	public Vector<OutputFunction> provideOutputFunctions() {
		return new Vector<OutputFunction>();
	}

	public ComplexNumber provideSeedValue() {
		return seed;
	}

	public int provideSkips() {
		return skips;
	}

	public int provideInputSubscript() {
		return inputFunctions.size() - 1;
	}

	public int provideOutputSubscript() {
		return 0;
	}

}
