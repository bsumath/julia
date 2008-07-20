package edu.bsu.julia.gui;

public class GUIUtil {
	// private static Julia parentFrame;
	public static final int NEW_DIALOG = 0;
	public static final int EDIT_DIALOG = 1;
	public static final int CLONE_DIALOG = 2;

	public static String removeCommas(String arg) {
		arg.trim();
		String result = "";
		int lastIndex = 0;
		int newIndex = arg.indexOf(',', lastIndex);
		;
		while (newIndex != -1) {
			result += arg.substring(lastIndex, newIndex);
			lastIndex = newIndex + 1;
			newIndex = arg.indexOf(',', lastIndex);
		}
		result += arg.substring(lastIndex, arg.length());
		return result;
	}

	public static String parsePI(String arg) {

		double result = 0;
		String a, s = "";
		if (arg.contains("*PI")) {
			a = arg.replace("*PI", "");
			try {
				result = Math.PI * Double.parseDouble(a);
				s = Double.toString(result);
			} catch (NumberFormatException e) {
				return arg;
			}
		} else if (arg.equalsIgnoreCase("PI")) {
			result = Math.PI;
			s = Double.toString(result);
		}

		else
			s = arg;
		return s;

	}

}
