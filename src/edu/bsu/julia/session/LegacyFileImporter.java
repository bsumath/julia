package edu.bsu.julia.session;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

import edu.bsu.julia.ComplexNumber;
import edu.bsu.julia.input.CubicInputFunction;
import edu.bsu.julia.input.InputFunction;
import edu.bsu.julia.input.LinearInputFunction;
import edu.bsu.julia.input.MobiusInputFunction;
import edu.bsu.julia.input.QuadraticInputFunction;
import edu.bsu.julia.input.RealAfflineLinearInputFunction;
import edu.bsu.julia.output.OutputFunction;
import edu.bsu.julia.session.Session.Importer;

/**
 * class to provide for importing of old .julia files.
 * 
 * @author Ben Dean
 */
public class LegacyFileImporter implements Importer {
	private int iterations;
	private int skips;
	private ComplexNumber seed;
	private Vector<InputFunction> inputFunctions = new Vector<InputFunction>();

	public LegacyFileImporter(File f) throws NumberFormatException, IOException {
		BufferedReader input = new BufferedReader(new FileReader(f));

		iterations = Integer.parseInt(input.readLine());
		skips = Integer.parseInt(input.readLine());
		double x = Double.parseDouble(input.readLine());
		double y = Double.parseDouble(input.readLine());
		seed = new ComplexNumber(x, y);

		int size = Integer.parseInt(input.readLine());
		int m;
		for (int i = 0; i < size; i++) {
			String type = input.readLine();
			if (type.equals("linear")) {
				m = Integer.parseInt(input.readLine());
				ComplexNumber[] var = new ComplexNumber[2];
				for (int j = 0; j < 2; j++) {
					x = Double.parseDouble(input.readLine());
					y = Double.parseDouble(input.readLine());
					var[j] = new ComplexNumber(x, y);
				}
				inputFunctions.add(new LinearInputFunction(m,
						var[0], var[1]));
			} else if (type.equals("cubic")) {
				m = Integer.parseInt(input.readLine());
				ComplexNumber[] var = new ComplexNumber[2];
				for (int j = 0; j < 2; j++) {
					x = Double.parseDouble(input.readLine());
					y = Double.parseDouble(input.readLine());
					var[j] = new ComplexNumber(x, y);
				}
				inputFunctions.add(new CubicInputFunction(m,
						var[0], var[1]));
			} else if (type.equals("matrix")) {
				m = Integer.parseInt(input.readLine());
				ComplexNumber[] var = new ComplexNumber[6];
				for (int j = 0; j < 6; j++) {
					x = Double.parseDouble(input.readLine());
					y = Double.parseDouble(input.readLine());
					var[j] = new ComplexNumber(x, y);
				}
				inputFunctions.add(new RealAfflineLinearInputFunction(m,
						var[0], var[1], var[2], var[3], var[4], var[5]));
			} else if (type.equals("mobius")) {
				m = Integer.parseInt(input.readLine());
				ComplexNumber[] var = new ComplexNumber[4];
				for (int j = 0; j < 4; j++) {
					x = Double.parseDouble(input.readLine());
					y = Double.parseDouble(input.readLine());
					var[j] = new ComplexNumber(x, y);
				}
				inputFunctions.add(new MobiusInputFunction(m, var[0], var[1],
						var[2], var[3]));
			} else if (type.equals("quad")) {
				m = Integer.parseInt(input.readLine());
				ComplexNumber[] var = new ComplexNumber[3];
				for (int j = 0; j < 3; j++) {
					x = Double.parseDouble(input.readLine());
					y = Double.parseDouble(input.readLine());
					var[j] = new ComplexNumber(x, y);
				}
				inputFunctions.add(new QuadraticInputFunction(m, var[0],
						var[1], var[2]));
			}
		}
	}

	public Vector<InputFunction> provideInputFunctions() {
		return inputFunctions;
	}

	public int provideInputSubscript() {
		return inputFunctions.size();
	}

	public int provideIterations() {
		return iterations;
	}

	public Vector<OutputFunction> provideOutputFunctions() {
		return new Vector<OutputFunction>();
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
}
