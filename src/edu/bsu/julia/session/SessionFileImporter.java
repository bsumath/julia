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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.swing.JFrame;
import javax.swing.SwingWorker;

import edu.bsu.julia.ComplexNumber;
import edu.bsu.julia.generators.DummyOutputSetGenerator;
import edu.bsu.julia.generators.OutputSetGenerator;
import edu.bsu.julia.input.InputFunction;
import edu.bsu.julia.output.InverseOutputFunction;
import edu.bsu.julia.output.OutputSet;
import edu.bsu.julia.session.Session.Importer;
import edu.bsu.julia.session.Session.InvalidSessionParametersException;

/**
 * this is a {@link Session.Importer} that reads *.julia.zip saved sessions. It
 * does this by extracting the zip file into temporary files and then processing
 * each of the temporary files to get information about the
 * {@link InputFunction} and {@link OutputSet} of the {@link Session}
 * 
 * @author Ben Dean
 */
public class SessionFileImporter extends SwingWorker<Boolean, Void> implements
		Importer {
	private static final int BUFFER_SIZE = 2048;
	private int iterations;
	private int skips;
	private ComplexNumber seed = new ComplexNumber();

	private final Map<Long, InputFunction> inputFunctions = new HashMap<Long, InputFunction>();
	private final Map<Long, OutputSet> outputFunctions = new HashMap<Long, OutputSet>();

	private final List<File> tempFiles = new ArrayList<File>();
	private final Map<Long, File> inputMap;
	private final Map<Long, File> outputInfoMap;
	private final Map<Long, File> outputDataMap;
	private final File sessionFile;
	private float maxProgress = 0;
	private float progress = 0;

	public SessionFileImporter(File f) {
		// maps to keep track of temp files associated with input and output
		inputMap = new HashMap<Long, File>();
		outputInfoMap = new HashMap<Long, File>();
		outputDataMap = new HashMap<Long, File>();

		sessionFile = f;
	}

	protected Boolean doInBackground() throws Exception {
		// open the file and set up a scanner
		ZipInputStream zipStream = new ZipInputStream(new FileInputStream(
				sessionFile));
		ZipEntry entry;

		// count the number of entries
		while ((entry = zipStream.getNextEntry()) != null) {
			maxProgress += 2;
		}
		maxProgress -= 1; //the session.txt is only looked at once
		zipStream.close();
		zipStream = new ZipInputStream(new FileInputStream(sessionFile));

		// read each of the zip entries
		while ((entry = zipStream.getNextEntry()) != null) {
			progress += 1;
			setProgress((int) (progress / maxProgress * 100));

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
			progress += 1;
			setProgress((int) (progress / maxProgress * 100));
			
			File file = item.getValue();
			Long key = item.getKey();
			Scanner in = new Scanner(new BufferedInputStream(
					new FileInputStream(file)));
			readInputFunction(key, in);
			in.close();
		}

		while (!outputInfoMap.isEmpty()) {
			List<Map.Entry<Long, File>> entries = new ArrayList<Map.Entry<Long, File>>(
					outputInfoMap.entrySet());
			Map.Entry<Long, File> item = entries.get(0);
			File infoFile = item.getValue();
			File dataFile = outputDataMap.get(item.getKey());
			Long key = item.getKey();

			Scanner infoScanner = new Scanner(new BufferedInputStream(
					new FileInputStream(infoFile)));
			Scanner dataScanner = new Scanner(new BufferedInputStream(
					new FileInputStream(dataFile)));
			readOutputFunction(key, infoScanner, dataScanner);
			infoScanner.close();
			dataScanner.close();
		}

		clearTempFiles();
		return true;
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

	private void readInputFunction(Long inputID, Scanner in)
			throws IOException, ClassNotFoundException,
			IllegalArgumentException, InstantiationException,
			IllegalAccessException, InvocationTargetException {
		int m = 1;
		List<ComplexNumber> coefficients = new ArrayList<ComplexNumber>();
		String className = "";

		while (in.hasNextLine()) {
			// read the next line, ignore comments,
			// break at the end of input function
			String line = in.nextLine().trim();
			if (line.length() == 0 || line.charAt(0) == '#')
				continue;
			if (line.equalsIgnoreCase("end_input_function"))
				break;

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
				inputFunctions.put(inputID, function);
			}
		}
	}

	private void readOutputFunction(Long outputID, Scanner info, Scanner data)
			throws IOException, ClassNotFoundException,
			IllegalArgumentException, InstantiationException,
			IllegalAccessException, InvocationTargetException {
		progress += 1;
		setProgress((int) (progress / maxProgress * 100));		
		
		int iterations = 0;
		int skips = 0;
		ComplexNumber seed = new ComplexNumber();
		List<InputFunction> inFunctions = new ArrayList<InputFunction>();
		List<ComplexNumber> points = new ArrayList<ComplexNumber>();
		List<OutputSet> outFunctions = new ArrayList<OutputSet>();
		OutputSet.Type type = OutputSet.Type.BASIC;
		String className = "";

		// read all the history information from the txt info file
		while (info.hasNextLine()) {
			// read the next line, ignore comments,
			// break at the end of output function
			String line = info.nextLine().trim();
			if (line.length() == 0 || line.charAt(0) == '#')
				continue;
			if (line.equalsIgnoreCase("end_output_function"))
				break;

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
			} else if (lineParts[0].equalsIgnoreCase("begin_input_function")) {
				// if the key exists, use that input function
				long key = Long.parseLong(lineParts[1]);
				if (inputFunctions.containsKey(key)) {
					inFunctions.add(inputFunctions.get(key));

					// skip all the lines until end_input_function
					while (info.hasNextLine()
							&& !info.nextLine().trim().equalsIgnoreCase(
									"end_input_function"))
						;// do nothing
				} else {
					// the key didn't exist so create a new function
					readInputFunction(key, info);
					inFunctions.add(inputFunctions.remove(key));
				}
			} else if (lineParts[0].equalsIgnoreCase("type")) {
				type = OutputSet.Type.valueOf(lineParts[1]);
			} else if (lineParts[0].equalsIgnoreCase("begin_output_function")) {
				long key = Long.parseLong(lineParts[1]);
				if (outputFunctions.containsKey(key)) {
					// get the output function for the key
					outFunctions.add(outputFunctions.get(key));

					// skip all the lines until end_output_function
					while (info.hasNextLine()
							&& !info.nextLine().trim().equalsIgnoreCase(
									"end_output_function"))
						;// do nothing
				} else if (outputInfoMap.containsKey(key)) {
					// output function exists but hasn't been loaded
					File infoFile = outputInfoMap.get(key);
					File dataFile = outputDataMap.get(key);
					Scanner infoScanner = new Scanner(new BufferedInputStream(
							new FileInputStream(infoFile)));
					Scanner dataScanner = new Scanner(new BufferedInputStream(
							new FileInputStream(dataFile)));
					readOutputFunction(key, infoScanner, dataScanner);
					infoScanner.close();
					dataScanner.close();

					outFunctions.add(outputFunctions.get(key));

					// skip all the lines until end_output_function
					while (info.hasNextLine()
							&& !info.nextLine().trim().equalsIgnoreCase(
									"end_output_function"))
						;// do nothing
				} else {
					// output function does not exist, the data file is a dummy
					// file
					File temp = File.createTempFile("dummy", ".tmp");
					Scanner dataScanner = new Scanner(new FileInputStream(temp));
					readOutputFunction(key, info, dataScanner);
					dataScanner.close();
					temp.delete();

					outFunctions.add(outputFunctions.remove(key));
				}
			}
		}

		// read the points from the data file
		while (data.hasNextLine()) {
			points.add(ComplexNumber.parseComplexNumber(data.nextLine()));
		}

		// create a dummy session with the iteration, skip, and seed values
		Session tempSession;
		try {
			tempSession = new Session(new JFrame(), new DummyImporter(
					iterations, skips, seed));
		} catch (InvalidSessionParametersException e) {
			e.printStackTrace();
			return;
		}

		// create the output function
		OutputSet function;
		OutputSetGenerator generator = new DummyOutputSetGenerator(points
				.toArray(new ComplexNumber[] {}));
		if (className.endsWith("InverseOutputFunction")) {
			function = new InverseOutputFunction(tempSession, inFunctions
					.toArray(new InputFunction[] {}), type, generator,
					outFunctions.toArray(new OutputSet[] {}));
		} else {
			function = new OutputSet(tempSession, inFunctions
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
		// sort the functions by the keys and set the subscripts
		List<Long> keys = new ArrayList<Long>(inputFunctions.keySet());
		Collections.sort(keys);

		List<InputFunction> result = new ArrayList<InputFunction>();
		for (int i = 0; i < keys.size(); i++) {
			InputFunction function = inputFunctions.get(keys.get(i));
			function.setSubscript(i + 1);
			result.add(function);
		}

		return result;
	}

	public int provideIterations() {
		return iterations;
	}

	public Collection<OutputSet> provideOutputFunctions() {
		// sort the functions by the keys and set the subscripts
		List<Long> keys = new ArrayList<Long>(outputFunctions.keySet());
		Collections.sort(keys);

		List<OutputSet> result = new ArrayList<OutputSet>();
		for (int i = 0; i < keys.size(); i++) {
			OutputSet function = outputFunctions.get(keys.get(i));
			function.setSubscript(i + 1);
			result.add(function);
		}

		return result;
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

	/**
	 * a class to use to create dummy {@link Session} objects for constructing
	 * {@link OutputSet}
	 * 
	 * @author Ben Dean
	 */
	private final class DummyImporter implements Session.Importer {
		private final int iterations;
		private final ComplexNumber seed;
		private final int skips;

		public DummyImporter(int iter, int sk, ComplexNumber sd) {
			iterations = iter;
			seed = sd;
			skips = sk;
		}

		public Collection<InputFunction> provideInputFunctions() {
			return new ArrayList<InputFunction>();
		}

		public int provideInputSubscript() {
			return 0;
		}

		public int provideIterations() {
			return iterations;
		}

		public Collection<OutputSet> provideOutputFunctions() {
			return new ArrayList<OutputSet>();
		}

		public int provideOutputSubscript() {
			return 0;
		}

		public ComplexNumber provideSeedValue() {
			return seed;
		}

		public int provideSkips() {
			return skips;
		}
	};
}
