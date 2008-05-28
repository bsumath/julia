package edu.bsu.julia.session;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Scanner;
import java.util.Vector;

import edu.bsu.julia.ComplexNumber;
import edu.bsu.julia.InputFunction;
import edu.bsu.julia.OutputFunction;
import edu.bsu.julia.session.Session.Importer;

public class SessionFileImporter implements Importer {

	private int iterations;
	private int skips;
	private ComplexNumber seed = new ComplexNumber();

	private Vector<InputFunction> inputFunctions = new Vector<InputFunction>();
	private Vector<OutputFunction> outputFunctions = new Vector<OutputFunction>();

	public SessionFileImporter(File f) throws IOException,
			ClassNotFoundException, IllegalArgumentException,
			InstantiationException, IllegalAccessException,
			InvocationTargetException {
		
		// open the file and set up a scanner
		Scanner in = new Scanner(new BufferedReader(new FileReader(f)));

		while (in.hasNextLine()) {
			// read the next line and ignore any comment lines
			String line = in.nextLine().trim();
			if (line.length() == 0 || line.charAt(0) == '#')
				continue;

			// split the line on the : character and trim the parts
			String[] lineParts = line.split(":");
			for (int i = 0; i < lineParts.length; i++)
				lineParts[i] = lineParts[i].trim();

			// do something based on what lineParts[0] is
			if (lineParts[0].equalsIgnoreCase("iterations")) {
				iterations = Integer.parseInt(lineParts[1]);
			} else if (lineParts[0].equalsIgnoreCase("skips")) {
				skips = Integer.parseInt(lineParts[1]);
			} else if (lineParts[0].equalsIgnoreCase("seed")) {
				seed = stringToComplexNumber(lineParts[1]);
			} else if (lineParts[0].equalsIgnoreCase("start_input_function")) {
				readInputFunction(in, lineParts[1]);
			} else if (lineParts[0].equalsIgnoreCase("start_output_function")) {
				readOutputFunction(in, lineParts[1]);
			}
		}
	}

	/**
	 * method to convert a string in the form of "x,y" to a ComplexNumber
	 * 
	 * @param string
	 *            the string containing the x and y parts of the ComplexNumber
	 * @return the resulting ComplexNumber
	 */
	private ComplexNumber stringToComplexNumber(String string)
			throws NumberFormatException {
		String[] parts = string.split(",");
		double x = Double.parseDouble(parts[0].trim());
		double y = Double.parseDouble(parts[1].trim());

		return new ComplexNumber(x, y);
	}

	private void readInputFunction(Scanner in, String className)
			throws IOException, ClassNotFoundException,
			IllegalArgumentException, InstantiationException,
			IllegalAccessException, InvocationTargetException {
		int m = 1;
		Vector<ComplexNumber> coefficients = new Vector<ComplexNumber>();

		while (in.hasNextLine()) {
			// read the next line, ignore comments,
			// break at the end of input function
			String line = in.nextLine();
			if (line.length() == 0 || line.charAt(0) == '#')
				continue;
			if (line.equalsIgnoreCase("end_input_function"))
				break;

			// split the line on the : character and trim the parts
			String[] lineParts = line.split(":");
			for (int i = 0; i < lineParts.length; i++)
				lineParts[i] = lineParts[i].trim();

			if (lineParts[0].equalsIgnoreCase("m")) {
				m = Integer.parseInt(lineParts[1]);
			} else if (lineParts[0].equalsIgnoreCase("coefficient")) {
				coefficients.add(stringToComplexNumber(lineParts[1]));
			}
		}

		Class<?> functionClass = Class.forName(className);
		Constructor<?>[] constructors = functionClass.getConstructors();
		for (Constructor<?> c : constructors) {
			if (c.getParameterTypes().length > 0) {
				Object[] args = new Object[coefficients.size() + 1];
				args[0] = m;
				for (int i = 1; i < args.length; i++) {
					args[i] = coefficients.get(i - 1);
				}
				inputFunctions.add((InputFunction) c.newInstance(args));
			}
		}
	}

	private void readOutputFunction(Scanner in, String className)
			throws IOException, ClassNotFoundException,
			IllegalArgumentException, InstantiationException,
			IllegalAccessException, InvocationTargetException {
		while (in.hasNextLine()) {
			// read the next line, ignore comments,
			// break at the end of output function
			String line = in.nextLine();
			if (line.length() == 0 || line.charAt(0) == '#')
				continue;
			if (line.equalsIgnoreCase("end_output_function"))
				break;
			
			// TODO read things about the output function
		}
	}

	public Vector<InputFunction> provideInputFunctions() {
		return inputFunctions;
	}

	public int provideIterations() {
		return iterations;
	}

	public Vector<OutputFunction> provideOutputFunctions() {
		return outputFunctions;
	}

	public ComplexNumber provideSeedValue() {
		return seed;
	}

	public int provideSkips() {
		return skips;
	}
}
