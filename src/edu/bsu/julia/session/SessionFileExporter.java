package edu.bsu.julia.session;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import edu.bsu.julia.ComplexNumber;
import edu.bsu.julia.input.InputFunction;
import edu.bsu.julia.output.OutputFunction;
import edu.bsu.julia.session.Session.Exporter;

public class SessionFileExporter implements Exporter {
	private static final int BUFFER_SIZE = 2048;
	private List<InputFunction> inputFunctions;
	private int iterations;
	private List<OutputFunction> outputFunctions;
	private ComplexNumber seed;
	private int skips;

	public void addInputFunctions(Collection<InputFunction> i) {
		inputFunctions = new ArrayList<InputFunction>(i);
	}

	public void addIterations(int i) {
		iterations = i;
	}

	public void addOutputFunctions(Collection<OutputFunction> o) {
		outputFunctions = new ArrayList<OutputFunction>(o);
	}

	public void addSeedValue(ComplexNumber s) {
		seed = s;
	}

	public void addSkips(int s) {
		skips = s;
	}

	public void writeToFile(File f) throws IOException {
		// create a new zip file output stream
		ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(
				new FileOutputStream(f)));

		// write the session info the zip
		writeFileToZip(createSessionInfoFile(), "session.txt", out);

		for (InputFunction function : inputFunctions) {
			File temp = function.getFile();
			String name = "in." + function.getInputID() + ".txt";
			if (temp != null) {
				writeFileToZip(temp, name, out);
			}
		}

		for (OutputFunction function : outputFunctions) {
			File[] temp = function.getFiles();
			String name = "out." + function.getOutputID();
			if (temp != null && temp.length == 2) {
				writeFileToZip(temp[0], name + ".txt", out);
				writeFileToZip(temp[1], name + ".dat", out);
			}
		}

		out.close();
	}

	private File createSessionInfoFile() throws IOException {
		File info = File.createTempFile("session", ".txt");
		info.deleteOnExit();
		PrintStream out = new PrintStream(new BufferedOutputStream(
				new FileOutputStream(info)));

		out.println("iterations: " + iterations);
		out.println("skips: " + skips);
		out.println("seed: " + seed);
		out.println();

		out.close();
		return info;
	}

	private void writeFileToZip(File file, String zipEntryName,
			ZipOutputStream out) throws IOException {
		byte data[] = new byte[BUFFER_SIZE];
		InputStream in = new BufferedInputStream(new FileInputStream(file),
				BUFFER_SIZE);
		ZipEntry entry = new ZipEntry(zipEntryName);
		out.putNextEntry(entry);
		int count;
		while ((count = in.read(data, 0, BUFFER_SIZE)) != -1) {
			out.write(data, 0, count);
		}
		in.close();
	}
}
