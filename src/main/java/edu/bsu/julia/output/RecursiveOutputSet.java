package edu.bsu.julia.output;

import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import edu.bsu.julia.generators.OutputSetGenerator;
import edu.bsu.julia.input.InputFunction;

public class RecursiveOutputSet extends OutputSet {

	private OutputSet[] outputSets = new OutputSet[] {};

	public RecursiveOutputSet(OutputSet.Info info, InputFunction[] i,
			OutputSet[] o, Type type, OutputSetGenerator g, ActionListener l) {
		super(info, i, type, g, l);
		outputSets = o;
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

	@Override
	public JComponent[] propertiesComponents() {
		JComponent[] superComponents = super.propertiesComponents();
		JComponent[] components = new JComponent[superComponents.length + 1];
		for (int i = 0; i < superComponents.length; i++)
			components[i] = superComponents[i];

		final JList list = new JList(outputSets);
		list.setVisibleRowCount(6);
		list.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent ev) {
				int index = list.locationToIndex(ev.getPoint());
				OutputSet set = (OutputSet) list.getModel().getElementAt(index);
				JOptionPane.showMessageDialog(list, set.propertiesComponents(),
						"Properties", JOptionPane.INFORMATION_MESSAGE);
			}
		});

		JScrollPane scroller = new JScrollPane(list);
		scroller.setBorder(BorderFactory.createTitledBorder("Output Sets"));
		components[superComponents.length] = scroller;
		return components;
	}

}
