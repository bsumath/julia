package edu.bsu.julia.session;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Vector;

import edu.bsu.julia.ComplexNumber;
import edu.bsu.julia.InputFunction;
import edu.bsu.julia.OutputFunction;
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

		ps.println("iterations: " + iterations);
		ps.println("skips: " + skips);
		ps.println("seed: " + seed.getX() + ", " + seed.getY());
		ps.println();

		for (InputFunction function : inputFunctions) {
			ps.print("start_input_function: ");
			ps.println(function.getClass().getName());
			ps.println("\tm:" + function.getM());
			for (ComplexNumber var : function.getCoefficients()) {
				ps.println("\tcoefficient: " + var.getX() + ", " + var.getY());
			}
			ps.println("end_input_function");
			ps.println();
		}
		
		for (OutputFunction function : outputFunctions){
			ps.print("start_output_function");
			ps.println(function.getClass().getName());
			// TODO output the information about the output function
			ps.println("end_output_function");
			ps.println();
		}

		ps.close();
		out.close();

	}
}
