package edu.bsu.julia.output;

import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JLabel;

import edu.bsu.julia.generators.OutputSetGenerator;
import edu.bsu.julia.input.InputFunction;
import edu.bsu.julia.session.Session;

/**
 * an {@link OutputSet} class for post critical sets. This is mainly here to be
 * able to keep track of the t value.
 * 
 * @author Ben Dean
 */
public class PostCriticalOutputSet extends OutputSet {
	private final int tValue;

	/**
	 * constructor for {@link PostCriticalOutputSet}
	 * 
	 * @param session
	 *            the {@link Session} to use
	 * @param i
	 *            an array of {@link InputFunction}
	 * @param type
	 *            the type of output set being created
	 * @param gen
	 *            an {@link OutputSetGenerator} to create the data points
	 * @param t
	 *            the t value for the post critical set
	 */
	public PostCriticalOutputSet(OutputSet.Info info, InputFunction[] i,
			OutputSet.Type type, int t, OutputSetGenerator gen, ActionListener l) {
		super(info, i, type, gen, l);

		tValue = t;
	}

	/**
	 * string representation of object
	 */
	public String toString() {
		String s = super.toString();
		s += ". T value: " + tValue;
		return s;
	}

	/**
	 * check if objects are equal
	 */
	public boolean equals(Object obj) {
		try {
			PostCriticalOutputSet other = (PostCriticalOutputSet) obj;
			return super.equals(obj) && this.tValue == other.tValue;
		} catch (ClassCastException e) {
			return false;
		}
	}

	/**
	 * @see OutputSet#historyInfo()
	 * @return a {@link String} with the history information about this
	 *         {@link PostCriticalOutputSet}
	 */
	@Override
	public List<String> historyInfo() {
		List<String> result = super.historyInfo();
		result.add(5, "tvalue: " + tValue);
		return result;
	}

	@Override
	public JComponent[] propertiesComponents() {
		JComponent[] superComponents = super.propertiesComponents();
		((Box) superComponents[0]).add(new JLabel("T value: " + tValue));
		return superComponents;
	}

}
