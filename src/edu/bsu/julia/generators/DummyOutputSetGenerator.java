package edu.bsu.julia.generators;

import org.apache.commons.math.complex.Complex;

/**
 * An {@link OutputSetGenerator} that is a wrapper around an array of
 * {@link Complex}
 * 
 * @author Ben Dean
 */
public class DummyOutputSetGenerator extends OutputSetGenerator {
	private final Complex[] points;

	public DummyOutputSetGenerator(Complex[] p) {
		points = (p.length > 0) ? p : null;
	}

	public Complex[] doInBackground() {
		return points;
	}
}
