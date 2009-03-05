package edu.bsu.julia.session;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Vector;

import javax.swing.SwingWorker;

import org.apache.commons.math.complex.Complex;

import edu.bsu.julia.input.CubicInputFunction;
import edu.bsu.julia.input.InputFunction;
import edu.bsu.julia.input.LinearInputFunction;
import edu.bsu.julia.input.MobiusInputFunction;
import edu.bsu.julia.input.QuadraticInputFunction;
import edu.bsu.julia.input.RealAffineLinearInputFunction;
import edu.bsu.julia.output.OutputSet;
import edu.bsu.julia.session.Session.Importer;

/**
 * class to provide for importing of old .julia files.
 * 
 * @author Ben Dean
 */
public class LegacyFileImporter extends SwingWorker<Boolean, Void> implements
		Importer {
	private int iterations;
	private int skips;
	private Complex seed;
	private Vector<InputFunction> inputFunctions = new Vector<InputFunction>();
	private final File file;

	public LegacyFileImporter(File f) {
		file = f;
	}

	protected Boolean doInBackground() throws Exception {
		BufferedReader input = new BufferedReader(new FileReader(file));

		iterations = Integer.parseInt(input.readLine());
		skips = Integer.parseInt(input.readLine());
		double x = Double.parseDouble(input.readLine());
		double y = Double.parseDouble(input.readLine());
		seed = new Complex(x, y);

		int size = Integer.parseInt(input.readLine());
		for (int i = 0; i < size; i++) {
			setProgress((int) ((float) i / size * 100));
			String type = input.readLine();
			if (type.equals("linear")) {
				int m = Integer.parseInt(input.readLine());
				Complex[] var = new Complex[2];
				for (int j = 0; j < 2; j++) {
					x = Double.parseDouble(input.readLine());
					y = Double.parseDouble(input.readLine());
					var[j] = new Complex(x, y);
				}
				inputFunctions.add(new LinearInputFunction(m, var[0], var[1]));
			} else if (type.equals("cubic")) {
				int m = Integer.parseInt(input.readLine());
				Complex[] var = new Complex[2];
				for (int j = 0; j < 2; j++) {
					x = Double.parseDouble(input.readLine());
					y = Double.parseDouble(input.readLine());
					var[j] = new Complex(x, y);
				}
				inputFunctions.add(new CubicInputFunction(m, var[0], var[1]));
			} else if (type.equals("matrix")) {
				int m = Integer.parseInt(input.readLine());
				Complex[] var = new Complex[6];
				for (int j = 0; j < 6; j++) {
					x = Double.parseDouble(input.readLine());
					y = Double.parseDouble(input.readLine());
					var[j] = new Complex(x, y);
				}
				inputFunctions.add(new RealAffineLinearInputFunction(m, var[0],
						var[1], var[2], var[3], var[4], var[5]));
			} else if (type.equals("mobius")) {
				int m = Integer.parseInt(input.readLine());
				Complex[] var = new Complex[4];
				for (int j = 0; j < 4; j++) {
					x = Double.parseDouble(input.readLine());
					y = Double.parseDouble(input.readLine());
					var[j] = new Complex(x, y);
				}
				inputFunctions.add(new MobiusInputFunction(m, var[0], var[1],
						var[2], var[3]));
			} else if (type.equals("quad")) {
				int m = Integer.parseInt(input.readLine());
				Complex[] var = new Complex[3];
				for (int j = 0; j < 3; j++) {
					x = Double.parseDouble(input.readLine());
					y = Double.parseDouble(input.readLine());
					var[j] = new Complex(x, y);
				}
				inputFunctions.add(new QuadraticInputFunction(m, var[0],
						var[1], var[2]));
			}
		}

		for (int i = 0; i < inputFunctions.size(); i++) {
			inputFunctions.get(i).setSubscript(i + 1);
		}
		return true;
	}

	public Vector<InputFunction> provideInputFunctions() {
		return inputFunctions;
	}

	public int provideInputSubscript() {
		return inputFunctions.size();
	}

	public Integer provideIterations() {
		return iterations;
	}

	public Vector<OutputSet> provideOutputSets() {
		return new Vector<OutputSet>();
	}

	public int provideOutputSubscript() {
		return 0;
	}

	public Complex provideSeedValue() {
		return seed;
	}

	public Integer provideSkips() {
		return skips;
	}

	public int[] provideSelectedInputIndices() {
		int[] selected = new int[inputFunctions.size()];
		for (int i = 0; i < selected.length; i++) {
			selected[i] = i;
		}
		return selected;
	}

	public String provideSelectedMethod() {
		return "";
	}

	public int[] provideSelectedOutputIndices() {
		return new int[] {};
	}

	public String provideSelectedType() {
		return "";
	}
}
