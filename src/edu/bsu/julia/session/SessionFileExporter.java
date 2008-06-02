package edu.bsu.julia.session;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Vector;

import edu.bsu.julia.ComplexNumber;
import edu.bsu.julia.input.InputFunction;
import edu.bsu.julia.output.InverseOutputFunction;
import edu.bsu.julia.output.OutputFunction;
import edu.bsu.julia.session.Session.Exporter;

public class SessionFileExporter implements Exporter {

	private Vector<InputFunction> inputFunctions;
	private int iterations;
	private Vector<OutputFunction> outputFunctions;
	private ComplexNumber seed;
	private int skips;
	private int tabDepth = 0;

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
		tabDepth = 0;

		ps.println("iterations: " + iterations);
		ps.println("skips: " + skips);
		ps.println("seed: " + seed.getX() + ", " + seed.getY());
		ps.println();

		for (InputFunction function : inputFunctions) {
			writeInputFunction(ps, function);
		}

		for (OutputFunction function : outputFunctions) {
			writeOutputFunction(ps, function);
		}

		ps.close();
		out.close();
	}

	private void writeOutputFunction(PrintStream ps, OutputFunction function) {
		ps.println(generateTabs() + "start_output_function: "
				+ function.getClass().getName());
		tabDepth += 1;

		ps.println(generateTabs() + "iterations: " + function.getIterations());
		ps.println(generateTabs() + "skips: " + function.getSkips());
		ps.println(generateTabs() + "seed: " + function.getSeedValue().getX()
				+ ", " + function.getSeedValue().getY());
		ps.println(generateTabs() + "type: " + function.getType());
		for (InputFunction inFunc : function.getInputFunctions()) {
			writeInputFunction(ps, inFunc);
		}
		if (function instanceof InverseOutputFunction) {
			for (OutputFunction outFunc : ((InverseOutputFunction) function)
					.getOutputFunctions()) {
				writeOutputFunction(ps, outFunc);
			}
		}
		for (ComplexNumber point : function.getPoints()) {
			ps.println(generateTabs() + "point: " + point.getX() + ", "
					+ point.getY());
		}

		tabDepth -= 1;
		ps.println(generateTabs() + "end_output_function");
		ps.println();
	}

	private void writeInputFunction(PrintStream ps, InputFunction function) {
		ps.println(generateTabs() + "start_input_function: "
				+ function.getClass().getName());
		tabDepth += 1;
		ps.println(generateTabs() + "m:" + function.getM());
		for (ComplexNumber var : function.getCoefficients()) {
			ps.println(generateTabs() + "coefficient: " + var.getX() + ", "
					+ var.getY());
		}
		tabDepth -= 1;
		ps.println(generateTabs() + "end_input_function");
		ps.println();
	}

	private String generateTabs() {
		String s = "";
		for (int i = 0; i < tabDepth; i++)
			s += "\t";
		return s;
	}
}
