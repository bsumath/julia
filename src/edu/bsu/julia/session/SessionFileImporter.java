package edu.bsu.julia.session;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.zip.ZipEntry;
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
	private static final int BUFFER_SIZE = 2048;
	private int iterations;
	private int skips;
	private ComplexNumber seed = new ComplexNumber();

	private final Map<Long, InputFunction> inputFunctions = new HashMap<Long, InputFunction>();
	private final Map<Long, OutputFunction> outputFunctions = new HashMap<Long, OutputFunction>();

	private final List<File> tempFiles = new ArrayList<File>();
	private final Map<Long, File> inputMap;
	private final Map<Long, File> outputInfoMap;
	private final Map<Long, File> outputDataMap;

	public SessionFileImporter(File f) throws IOException,
			ClassNotFoundException, IllegalArgumentException,
			InstantiationException, IllegalAccessException,
			InvocationTargetException {

		// open the file and set up a scanner
		ZipInputStream zipStream = new ZipInputStream(new FileInputStream(f));
		ZipEntry entry;

		// maps to keep track of temp files associated with input and output
		inputMap = new HashMap<Long, File>();
		outputInfoMap = new HashMap<Long, File>();
		outputDataMap = new HashMap<Long, File>();

		// read each of the zip entries
		while ((entry = zipStream.getNextEntry()) != null) {
			String[] nameParts = entry.getName().split("\\.");
			if (nameParts[0].equals("session")) {
				readSessionInfo(zipStream);
			} else if (nameParts[0].equals("in")) {
				inputMap.put(Long.parseLong(nameParts[1]),
						createTempFile(zipStream));
			} else if (nameParts[0].equals("out") && nameParts[2].equals("txt")) {
				outputInfoMap.put(Long.parseLong(nameParts[1]),
						createTempFile(zipStream));
			} else if (nameParts[0].equals("out") && nameParts[2].equals("dat")) {
				outputDataMap.put(Long.parseLong(nameParts[1]),
						createTempFile(zipStream));
			}
		}
		zipStream.close();

		for (Map.Entry<Long, File> item : inputMap.entrySet()) {
			InputFunction function = readInputFunction(item.getKey(), item
					.getValue());
			inputFunctions.put(item.getKey(), function);
		}

		while (!outputInfoMap.isEmpty()) {
			List<Map.Entry<Long, File>> entries = new ArrayList<Map.Entry<Long, File>>(
					outputInfoMap.entrySet());
			Map.Entry<Long, File> item = entries.get(0);
			readOutputFunction(item.getKey(), item.getValue(), outputDataMap
					.get(item.getKey()));
		}

		clearTempFiles();
	}

	private void readSessionInfo(ZipInputStream zipStream) {
		Scanner in = new Scanner(zipStream);
		while (in.hasNextLine()) {
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
				seed = ComplexNumber.parseComplexNumber(lineParts[1]);
			}
		}
	}

	private File createTempFile(ZipInputStream zipStream) throws IOException {
		byte data[] = new byte[BUFFER_SIZE];
		File temp = File.createTempFile("import", ".tmp");
		OutputStream out = new BufferedOutputStream(new FileOutputStream(temp));
		int count;
		while ((count = zipStream.read(data, 0, BUFFER_SIZE)) != -1) {
			out.write(data, 0, count);
		}
		out.close();

		tempFiles.add(temp);
		return temp;
	}

	private void clearTempFiles() {
		for (File temp : tempFiles)
			temp.delete();
	}

	private InputFunction readInputFunction(Long inputID, File file)
			throws IOException, ClassNotFoundException,
			IllegalArgumentException, InstantiationException,
			IllegalAccessException, InvocationTargetException {
		int m = 1;
		List<ComplexNumber> coefficients = new ArrayList<ComplexNumber>();
		String className = "";

		Scanner in = new Scanner(new BufferedInputStream(new FileInputStream(
				file)));
		while (in.hasNextLine()) {
			// read the next line, ignore comments,
			// break at the end of input function
			String line = in.nextLine();
			if (line.length() == 0 || line.charAt(0) == '#')
				continue;

			// split the line on the : character and trim the parts
			String[] lineParts = splitLine(line);

			if (lineParts[0].equalsIgnoreCase("class")) {
				className = lineParts[1].trim();
			} else if (lineParts[0].equalsIgnoreCase("m")) {
				m = Integer.parseInt(lineParts[1]);
			} else if (lineParts[0].equalsIgnoreCase("coefficient")) {
				coefficients
						.add(ComplexNumber.parseComplexNumber(lineParts[1]));
			}
		}
		in.close();

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
				return function;
			}
		}
		return null;
	}

	private void readOutputFunction(Long outputID, File infoFile, File dataFile)
			throws IOException, ClassNotFoundException,
			IllegalArgumentException, InstantiationException,
			IllegalAccessException, InvocationTargetException {
		int iterations = 0;
		int skips = 0;
		ComplexNumber seed = new ComplexNumber();
		List<InputFunction> inFunctions = new ArrayList<InputFunction>();
		List<ComplexNumber> points = new ArrayList<ComplexNumber>();
		List<OutputFunction> outFunctions = new ArrayList<OutputFunction>();
		OutputFunction.Type type = OutputFunction.Type.BASIC;
		String className = "";

		Scanner in = new Scanner(new BufferedInputStream(new FileInputStream(
				infoFile)));
		while (in.hasNextLine()) {
			// read the next line, ignore comments,
			// break at the end of output function
			String line = in.nextLine();
			if (line.length() == 0 || line.charAt(0) == '#')
				continue;

			// split the line
			String[] lineParts = splitLine(line);

			if (lineParts[0].equalsIgnoreCase("class")) {
				className = lineParts[1].trim();
			} else if (lineParts[0].equalsIgnoreCase("iterations")) {
				iterations = Integer.parseInt(lineParts[1]);
			} else if (lineParts[0].equalsIgnoreCase("skips")) {
				skips = Integer.parseInt(lineParts[1]);
			} else if (lineParts[0].equalsIgnoreCase("seed")) {
				seed = ComplexNumber.parseComplexNumber(lineParts[1]);
			} else if (lineParts[0].equalsIgnoreCase("input_function")) {
				long key = Long.parseLong(lineParts[1]);
				if (inputFunctions.containsKey(key))
					inFunctions.add(inputFunctions.get(key));
			} else if (lineParts[0].equalsIgnoreCase("type")) {
				type = OutputFunction.Type.valueOf(lineParts[1]);
			} else if (lineParts[0].equalsIgnoreCase("output_function")) {
				long key = Long.parseLong(lineParts[1]);
				if (!outputFunctions.containsKey(key)
						&& outputInfoMap.containsKey(key)) {
					File info = outputInfoMap.get(key);
					File data = outputDataMap.get(key);
					readOutputFunction(key, info, data);
				}
				outFunctions.add(outputFunctions.get(key));
			}
		}
		in.close();

		in = new Scanner(new BufferedInputStream(new FileInputStream(dataFile)));
		while (in.hasNextLine()) {
			points.add(ComplexNumber.parseComplexNumber(in.nextLine()));
		}
		in.close();

		// create a dummy session with the iteration, skip, and seed values
		final int iter = iterations;
		final int sk = skips;
		final ComplexNumber sd = seed;
		Session tempSession;
		try {
			tempSession = new Session(new JFrame(), new Session.Importer() {
				public Collection<InputFunction> provideInputFunctions() {
					return new ArrayList<InputFunction>();
				}

				public int provideInputSubscript() {
					return 0;
				}

				public int provideIterations() {
					return iter;
				}

				public Collection<OutputFunction> provideOutputFunctions() {
					return new ArrayList<OutputFunction>();
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
			return;
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

		// function created successfully, remove it from the maps
		outputDataMap.remove(outputID);
		outputInfoMap.remove(outputID);
		outputFunctions.put(outputID, function);
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

	public Collection<InputFunction> provideInputFunctions() {
		List<InputFunction> temp = new ArrayList<InputFunction>(inputFunctions
				.values());
		for (int i = 0; i < temp.size(); i++)
			temp.get(i).setSubscript(i + 1);
		return temp;
	}

	public int provideIterations() {
		return iterations;
	}

	public Collection<OutputFunction> provideOutputFunctions() {
		List<OutputFunction> temp = new ArrayList<OutputFunction>(
				outputFunctions.values());
		for (int i = 0; i < temp.size(); i++)
			temp.get(i).setSubscript(i + 1);
		return temp;
	}

	public ComplexNumber provideSeedValue() {
		return seed;
	}

	public int provideSkips() {
		return skips;
	}

	public int provideInputSubscript() {
		return inputFunctions.size();
	}

	public int provideOutputSubscript() {
		return outputFunctions.size();
	}
}
