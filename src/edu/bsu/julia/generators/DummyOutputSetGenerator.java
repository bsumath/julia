package edu.bsu.julia.generators;

import edu.bsu.julia.ComplexNumber;

/**
 * An {@link OutputSetGenerator} that is a wrapper around an array of {@link ComplexNumber}
 * @author Ben Dean
 */
public class DummyOutputSetGenerator implements OutputSetGenerator {
	private final ComplexNumber[] points;
	
	public DummyOutputSetGenerator(ComplexNumber[] p){
		points = p;
	}
	
	public void cancelExecution() {
	}

	public float getPercentComplete() {
		return 1;
	}

	public ComplexNumber[] getPoints() {
		return points;
	}

	public boolean isDone() {
		return true;
	}

	public void run() {
	}
}
