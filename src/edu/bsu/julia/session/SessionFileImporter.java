package edu.bsu.julia.session;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Scanner;
import java.util.Vector;
import java.util.zip.ZipInputStream;

import javax.swing.JFrame;

import edu.bsu.julia.ComplexNumber;
import edu.bsu.julia.generators.DummyOutputSetGenerator;
import edu.bsu.julia.generators.OutputSetGenerator;
import edu.bsu.julia.input.InputFunction;
import edu.bsu.julia.output.InverseOutputFunction;
import edu.bsu.julia.output.OutputFunction;
import edu.bsu.julia.session.Session.Importer;
import edu.bsu.julia.session.Session.InvalidSessionParametersException;

public class SessionFileImporter implements Importer {

	private int iterations;
	private int skips;
	private ComplexNumber seed = new ComplexNumber();

	private Vector<InputFunction> inputFunctions = new Vector<InputFunction>();
	private Vector<OutputFunction> outputFunctions = new Vector<OutputFunction>();
	private int inputSubscript = 0;
	private int outputSubscript = 0;

	public SessionFileImporter(File f) throws IOException,
			ClassNotFoundException, IllegalArgumentException,
			InstantiationException, IllegalAccessException,
			InvocationTargetException {

		// open the file and set up a scanner
		ZipInputStream zipStream = new ZipInputStream(new FileInputStream(f));
		zipStream.getNextEntry();
		Scanner in = new Scanner(zipStream);

		while (in.hasNextLine()) {
			// read the next line and ignore any comment lines
			String line = in.nextLine().trim();
			if (line.length() == 0 || line.charAt(0) == '#')
				continue;

			// split the line on the : character and trim the parts
			String[] lineParts = splitLine(line);

			// do something based on what lineParts[0] is
			if (lineParts[0].equalsIgnoreCase("iterations")) {
				iterations = Integer.parseInt(lineParts[1]);
			} else if (lineParts[0].equalsIgnoreCase("skips")) {
				skips = Integer.parseInt(lineParts[1]);
			} else if (lineParts[0].equalsIgnoreCase("seed")) {
				seed = stringToComplexNumber(lineParts[1]);
			} else if (lineParts[0].equalsIgnoreCase("start_input_function")) {
				InputFunction function = readInputFunction(in, lineParts[1]);
				if (function != null) {
					inputFunctions.add(function);
				}
			} else if (lineParts[0].equalsIgnoreCase("start_output_function")) {
				OutputFunction function = readOutputFunction(in, lineParts[1]);
				if (function != null) {
					outputFunctions.add(function);
				}
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

	private InputFunction readInputFunction(Scanner in, String className)
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
			if (line.trim().equalsIgnoreCase("end_input_function"))
				break;

			// split the line on the : character and trim the parts
			String[] lineParts = splitLine(line);

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
				InputFunction function = (InputFunction) c.newInstance(args);
				function.setSubscript(nextInputSubscript());
				return function;
			}
		}
		return null;
	}

	private OutputFunction readOutputFunction(Scanner in, String className)
			throws IOException, ClassNotFoundException,
			IllegalArgumentException, InstantiationException,
			IllegalAccessException, InvocationTargetException {
		int iterations = 0;
		int skips = 0;
		ComplexNumber seed = new ComplexNumber();
		Vector<InputFunction> inFunctions = new Vector<InputFunction>();
		Vector<ComplexNumber> points = new Vector<ComplexNumber>();
		Vector<OutputFunction> outFunctions = new Vector<OutputFunction>();
		OutputFunction.Type type = OutputFunction.Type.BASIC;

		while (in.hasNextLine()) {
			// read the next line, ignore comments,
			// break at the end of output function
			String line = in.nextLine();
			if (line.length() == 0 || line.charAt(0) == '#')
				continue;
			if (line.trim().equalsIgnoreCase("end_output_function"))
				break;

			// split the line
			String[] lineParts = splitLine(line);

			if (lineParts[0].equalsIgnoreCase("iterations")) {
				iterations = Integer.parseInt(lineParts[1]);
			} else if (lineParts[0].equalsIgnoreCase("skips")) {
				skips = Integer.parseInt(lineParts[1]);
			} else if (lineParts[0].equalsIgnoreCase("seed")) {
				seed = stringToComplexNumber(lineParts[1]);
			} else if (lineParts[0].equalsIgnoreCase("start_input_function")) {
				InputFunction function = readInputFunction(in, lineParts[1]);
				if (function != null) {
					int index = inputFunctions.indexOf(function);
					if (index >= 0)
						function = inputFunctions.get(index);
					inFunctions.add(function);
				}
			} else if (lineParts[0].equalsIgnoreCase("type")) {
				type = OutputFunction.Type.valueOf(lineParts[1]);
			} else if (lineParts[0].equalsIgnoreCase("point")) {
				points.add(stringToComplexNumber(lineParts[1]));
			} else if (lineParts[0].equalsIgnoreCase("output_function")) {
				for (OutputFunction function : outputFunctions) {
					int h1 = function.hashCode();
					int h2 = Integer.parseInt(lineParts[1]);
					if (h1 == h2) {
						outFunctions.add(function);
						break;
					}
				}
			}
		}

		// create a dummy session with the iteration, skip, and seed values
		final int iter = iterations;
		final int sk = skips;
		final ComplexNumber sd = seed;
		Session tempSession;
		try {
			tempSession = new Session(new JFrame(), new Session.Importer() {
				public Vector<InputFunction> provideInputFunctions() {
					return null;
				}

				public int provideInputSubscript() {
					return 0;
				}

				public int provideIterations() {
					return iter;
				}

				public Vector<OutputFunction> provideOutputFunctions() {
					return null;
				}

				public int provideOutputSubscript() {
					return 0;
				}

				public ComplexNumber provideSeedValue() {
					return sd;
				}

				public int provideSkips() {
					return sk;
				}
			});
		} catch (InvalidSessionParametersException e) {
			e.printStackTrace();
			return null;
		}

		// create the output function
		OutputFunction function;
		OutputSetGenerator generator = new DummyOutputSetGenerator(points
				.toArray(new ComplexNumber[] {}));
		if (className.endsWith("InverseOutputFunction")) {
			function = new InverseOutputFunction(tempSession, inFunctions
					.toArray(new InputFunction[] {}), type, generator,
					outFunctions.toArray(new OutputFunction[] {}));
		} else {
			function = new OutputFunction(tempSession, inFunctions
					.toArray(new InputFunction[] {}), type, generator);
		}
		function.setSubscript(nextOutputSubscript());
		return function;
	}

	/**
	 * split the line on the : character and trim the parts
	 * 
	 * @param line
	 *            the original unsplit string
	 * @return String array with two values, split on the :
	 */
	private String[] splitLine(String line) {
		String[] lineParts = line.split(":");
		for (int i = 0; i < lineParts.length; i++)
			lineParts[i] = lineParts[i].trim();
		return lineParts;
	}

	private int nextInputSubscript() {
		return inputSubscript++;
	}

	private int nextOutputSubscript() {
		return outputSubscript++;
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

	public int provideInputSubscript() {
		return inputFunctions.size() - 1;
	}

	public int provideOutputSubscript() {
		return outputFunctions.size() - 1;
	}
}
