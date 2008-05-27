package edu.bsu.julia.session;

import java.util.Vector;

import edu.bsu.julia.ComplexNumber;
import edu.bsu.julia.InputFunction;
import edu.bsu.julia.LinearInputFunction;
import edu.bsu.julia.OutputFunction;
import edu.bsu.julia.session.Session.Importer;

public class EmptySessionImporter implements Importer {
	private static final int DEFAULT_ITERATIONS = 50000;
	private static final ComplexNumber DEFAULT_SEED = new ComplexNumber(1, 0);
	private static final int DEFAULT_SKIPS = 20;

	private final int iterations;
	private final int skips;
	private final ComplexNumber seed;

	public EmptySessionImporter() {
		iterations = DEFAULT_ITERATIONS;
		skips = DEFAULT_SKIPS;
		seed = DEFAULT_SEED;
	}

	public EmptySessionImporter(int iter, int sk, ComplexNumber sd) {
		iterations = iter;
		skips = sk;
		seed = sd;
	}

	public Vector<InputFunction> provideInputFunctions() {
		Vector<InputFunction> inputFunctions = new Vector<InputFunction>();

		ComplexNumber a = new ComplexNumber(2, 0);
		ComplexNumber b = new ComplexNumber(0, 0);
		ComplexNumber c = new ComplexNumber(-1, 0);
		ComplexNumber d = new ComplexNumber(-.5, -0.866);

		inputFunctions.add(new LinearInputFunction(1, a, b));
		inputFunctions.add(new LinearInputFunction(1, a, c));
		inputFunctions.add(new LinearInputFunction(1, a, d));

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

}
