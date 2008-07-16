package edu.bsu.julia.output;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

import edu.bsu.julia.ComplexNumber;
import edu.bsu.julia.generators.OutputSetGenerator;
import edu.bsu.julia.input.InputFunction;
import edu.bsu.julia.session.Session;

public class InverseOutputFunction extends OutputSet {

	private OutputSet[] outputFunctions = new OutputSet[] {};

	public InverseOutputFunction(Session s, InputFunction[] i, Type type,
			OutputSetGenerator g, OutputSet[] o) {
		super(s, i, type, g);
		outputFunctions = o;
	}

	public OutputSet[] getOutputFunctions() {
		return outputFunctions;
	}

	public String toString() {
		String s = "o" + getSubscript() + " = " + functionType.description()
				+ " of ";

		for (int x = 0; x < outputFunctions.length; x++) {
			s = s + "o" + outputFunctions[x].getSubscript();
			if (x != (outputFunctions.length - 1))
				s = s + ", ";
		}
		s = s + " using ";
		InputFunction[] inList = getInputFunctions();
		for (int x = 0; x < inList.length; x++) {
			s = s + "f" + inList[x].getSubscript();
			if (x != (inList.length - 1))
				s = s + ", ";
		}
		return s;
	}

	public boolean writeToFile(File f) {
		FileOutputStream out;
		PrintStream ps;

		try {
			out = new FileOutputStream(f);
			ps = new PrintStream(out);
			for (int i = 0; i < getPoints().length; i++)
				ps.println(getPoints()[i]);
			ps.println(getIterations());
			ps.println(getSkips());
			ps.println(getSeedValue());
			ps.println(functionType);
			InputFunction[] functions = getInputFunctions();
			for (int j = 0; j < functions.length; j++) {
				ps.println(functions[j].getClass().getName());
				ps.println(functions[j].getM());
				ComplexNumber[] coefficients = functions[j].getCoefficients();
				for (int k = 0; k < coefficients.length; k++)
					ps.println(coefficients[k]);
			}
			ps.close();
		} catch (IOException e) {
			System.err.println(e);
			return false;
		}
		return true;
	}

	public boolean equals(Object obj) {
		try {
			InverseOutputFunction other = (InverseOutputFunction) obj;
			boolean result = super.equals(obj);
			result = result
					&& outputFunctions.length == other.outputFunctions.length;

			for (int i = 0; result && i < outputFunctions.length; i++) {
				result = result
						&& outputFunctions[i].equals(other.outputFunctions[i]);
			}

			return result;
		} catch (ClassCastException e) {
			return false;
		}
	}

	/**
	 * method to access the files this OutputFunction
	 * 
	 * @see OutputSet#getFiles()
	 * @return an array of {@link File} containing two entries, one for point
	 *         data and one for the other information about the
	 *         {@link InverseOutputFunction}
	 */
	@Override
	public File[] getFiles() {
		if (pointsFile == null)
			return null;

		try {
			File info = File.createTempFile("out." + creationTime, ".txt");
			info.deleteOnExit();
			PrintStream out = new PrintStream(new BufferedOutputStream(
					new FileOutputStream(info)));

			for (String s : historyInfo())
				out.println(s);

			out.close();

			return new File[] { info, pointsFile };
		} catch (IOException e) {
			return null;
		}
	}

	/**
	 * @see OutputSet#historyInfo()
	 * @return a {@link String} with the history information about this
	 *         {@link InverseOutputFunction}
	 */
	@Override
	public List<String> historyInfo() {
		List<String> result = super.historyInfo();

		for (OutputSet function : outputFunctions) {
			result.add("begin_output_function: " + function.getOutputID());
			for (String s : function.historyInfo())
				result.add("\t" + s);
			result.add("end_output_function");
		}

		return result;
	}

}
