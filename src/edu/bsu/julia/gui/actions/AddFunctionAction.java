package edu.bsu.julia.gui.actions;

import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import edu.bsu.julia.Julia;
import edu.bsu.julia.gui.CubicDialog;
import edu.bsu.julia.gui.GUIUtil;
import edu.bsu.julia.gui.LinearDialog;
import edu.bsu.julia.gui.MobiusDialog;
import edu.bsu.julia.gui.QuadraticDialog;
import edu.bsu.julia.gui.RealAffineLinearDialog;
import edu.bsu.julia.gui.BinomialDialog;
import edu.bsu.julia.gui.TempDialog;

public class AddFunctionAction extends AbstractAction {

	private Julia parentFrame;
	// for serializable interface: do not use
	public static final long serialVersionUID = 0;

	public AddFunctionAction(Julia f) {
		super("Add Function", new ImageIcon(Thread.currentThread()
				.getContextClassLoader().getResource("addformula.png")));
		parentFrame = f;
		putValue("SHORT_DESCRIPTION", "Add Function");
		putValue("LONG_DESCRIPTION", "Add a new function to the present "
				+ "input function list.");
	}

	public void actionPerformed(ActionEvent arg0) {
		final JDialog addFunctionDialog = new JDialog(parentFrame,
				"Add a Function", false);
		addFunctionDialog.setLocationRelativeTo(parentFrame);
		addFunctionDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		addFunctionDialog.setLayout(new FlowLayout());

		JLabel choiceLabel = new JLabel("Please Choose a Function Type:");
		addFunctionDialog.add(choiceLabel);

		final JButton nextButton = new JButton("Next >>>");

		String[] choices = new String[7];						/**Change the number here.*/
		choices[0] = "<html><h2>az + b</h2></html>";
		choices[1] = "<html><h2>(az + b) / (cz + d)</h2></html>";
		choices[2] = "<html><h2>az<sup>2</sup> + bz + c</h2></html>";
		choices[3] = "<html><h2>az<sup>3</sup> + b</h2></html>";
		choices[4] = "<html><h2>[a, b ; c, d]z + [e ; f]</h2></html>";
		choices[5] = "<html><h2>az<sup>n</sup> + k</h2></html>";
		choices[6] = "<html><h2>az<sup>n</sup> + b/z<sup>n</sup></h2></html>";
		final JList list = new JList(choices);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setVisibleRowCount(choices.length + 1);
		list.setSelectedIndex(0);
		list.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent ev) {
				if (ev.getClickCount() == 2) {
					for (ActionListener listener : nextButton
							.getActionListeners()) {
						listener.actionPerformed(null);
					}
				}
			}
		});
		JScrollPane pane = new JScrollPane(list);
		addFunctionDialog.add(pane);

		JPanel buttonPanel = new JPanel();
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				addFunctionDialog.setVisible(false);
				addFunctionDialog.dispose();
			}
		});
		buttonPanel.add(cancelButton);

		nextButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				int type = list.getSelectedIndex();
				addFunctionDialog.setVisible(false);
				addFunctionDialog.dispose();
				switch (type) {
				case 0:
					new LinearDialog(parentFrame, GUIUtil.NEW_DIALOG, null);
					break;
				case 1:
					new MobiusDialog(parentFrame, GUIUtil.NEW_DIALOG, null);
					break;
				case 2:
					new QuadraticDialog(parentFrame, GUIUtil.NEW_DIALOG, null);
					break;
				case 3:
					new CubicDialog(parentFrame, GUIUtil.NEW_DIALOG, null);
					break;
				case 4:
					new RealAffineLinearDialog(parentFrame, GUIUtil.NEW_DIALOG,
							null);
					break;
				case 5:
					new BinomialDialog(parentFrame, GUIUtil.NEW_DIALOG, null);
					break;
				case 6:
					new TempDialog(parentFrame, GUIUtil.NEW_DIALOG, null);
					break;
				}
			}
		});
		buttonPanel.add(nextButton);
		addFunctionDialog.add(buttonPanel);

		addFunctionDialog.setSize(250, 500);
		Point p = addFunctionDialog.getLocation();
		p.x = p.x - 125;
		p.y = p.y - 215;
		addFunctionDialog.setLocation(p);
		addFunctionDialog.setVisible(true);
	}

}
