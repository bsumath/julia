package edu.bsu.julia.generators;

import edu.bsu.julia.ComplexNumber;

/**
 * An {@link OutputSetGenerator} that is a wrapper around an array of
 * {@link ComplexNumber}
 * 
 * @author Ben Dean
 */
public class DummyOutputSetGenerator extends OutputSetGenerator {
	private final ComplexNumber[] points;

	public DummyOutputSetGenerator(ComplexNumber[] p) {
		points = (p.length > 0) ? p : null;
	}

	public ComplexNumber[] doInBackground() {
		return points;
	}
}
