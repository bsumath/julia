package edu.bsu.julia.output;

import java.util.List;

import edu.bsu.julia.generators.OutputSetGenerator;
import edu.bsu.julia.input.InputFunction;
import edu.bsu.julia.session.Session;

public class RecursiveOutputSet extends OutputSet {

	private OutputSet[] outputSets = new OutputSet[] {};

	public RecursiveOutputSet(Session s, InputFunction[] i, Type type,
			OutputSetGenerator g, OutputSet[] o) {
		super(s, i, type, g);
		outputSets = o;
	}

	public OutputSet[] getOutputSets() {
		return outputSets;
	}

	public String toString() {
		String s = "o" + getSubscript() + " = " + functionType.description()
				+ " of ";

		for (int x = 0; x < outputSets.length; x++) {
			s = s + "o" + outputSets[x].getSubscript();
			if (x != (outputSets.length - 1))
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

	public boolean equals(Object obj) {
		try {
			RecursiveOutputSet other = (RecursiveOutputSet) obj;
			boolean result = super.equals(obj);
			result = result && outputSets.length == other.outputSets.length;

			for (int i = 0; result && i < outputSets.length; i++) {
				result = result && outputSets[i].equals(other.outputSets[i]);
			}

			return result;
		} catch (ClassCastException e) {
			return false;
		}
	}

	/**
	 * @see OutputSet#historyInfo()
	 * @return a {@link String} with the history information about this
	 *         {@link RecursiveOutputSet}
	 */
	@Override
	public List<String> historyInfo() {
		List<String> result = super.historyInfo();

		for (OutputSet set : outputSets) {
			result.add("begin_output_set: " + set.getOutputID());
			for (String s : set.historyInfo())
				result.add("\t" + s);
			result.add("end_output_set");
			result.add("\r\n \n \n\r");
		}

		return result;
	}

}
