package edu.bsu.julia.gui;

import java.awt.BorderLayout;
import java.awt.Checkbox;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.commons.math.complex.Complex;

import edu.bsu.julia.Julia;
import edu.bsu.julia.session.EmptySessionImporter;
import edu.bsu.julia.session.Session;
import edu.bsu.julia.session.Session.InvalidSessionParametersException;

public class SessionDialog extends JDialog implements ActionListener {

	private Julia parentFrame;

	private int sessionType;

	private JTextField iterationsField;

	private JTextField skipsField;

	private JTextField seedField1;

	private JTextField seedField2;

	private Checkbox randomSeed;

	private Checkbox polarCheckBox;

	// for serializable interface: do not use
	public static final long serialVersionUID = 0;

	public SessionDialog(Julia f, int type) {
		super(f, "", false);
		sessionType = type;
		if (sessionType == GUIUtil.NEW_DIALOG)
			setTitle("New Session");
		else
			setTitle("Edit Session");
		parentFrame = f;
		Session s = parentFrame.getCurrentSession();
		setLocationRelativeTo(parentFrame);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setLayout(new GridLayout(0, 1));

		JPanel pointsPanel = new JPanel();
		pointsPanel.add(new JLabel("Points to Plot:"));
		iterationsField = new JTextField(8);
		if (sessionType == GUIUtil.EDIT_DIALOG) {
			iterationsField.setText("" + s.getIterations());
		}
		pointsPanel.add(iterationsField);
		add(pointsPanel);

		JPanel skipPanel = new JPanel();
		skipPanel.add(new JLabel("Number of Skips:"));
		skipsField = new JTextField(6);
		if (sessionType == GUIUtil.EDIT_DIALOG) {
			skipsField.setText("" + s.getSkips());
		}
		skipPanel.add(skipsField);

		JPanel seedPanel = new JPanel();
		seedPanel.add(new JLabel("Seed Value:  ("));
		seedField1 = new JTextField(6);
		if (sessionType == GUIUtil.EDIT_DIALOG) {
			String show = String.valueOf(s.getSeedValue().getReal());
			String showShort = show;
			if (show.length() > 10)
				showShort = show.substring(0, 10);
			seedField1.setText(showShort);
		}
		seedPanel.add(seedField1);
		seedPanel.add(new JLabel(","));
		seedField2 = new JTextField(6);
		if (sessionType == GUIUtil.EDIT_DIALOG) {
			String show = String.valueOf(s.getSeedValue().getImaginary());
			String showShort = show;
			if (show.length() > 10)
				showShort = show.substring(0, 10);
			seedField2.setText(showShort);
		}
		seedPanel.add(seedField2);
		seedPanel.add(new JLabel(")"));
		add(seedPanel);

		JPanel randomPanel = new JPanel(new BorderLayout());
		randomSeed = new Checkbox("Select a Random Seed with both");
		randomSeed.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 10));
		randomPanel.add(randomSeed, BorderLayout.NORTH);
		randomPanel.add(new JLabel("x and y coordinates between -10 and 10"),
				BorderLayout.CENTER);
		randomPanel.setPreferredSize(new Dimension(randomSeed.getWidth(),
				randomPanel.getHeight()));
		add(randomPanel);

		polarCheckBox = new Checkbox("Seed Value Use Polar Coordinates");
		polarCheckBox.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 10));
		add(polarCheckBox);

		JPanel buttonPanel = new JPanel();
		JButton okButton = new JButton("OK");
		okButton.addActionListener(this);
		buttonPanel.add(okButton);
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent event) {
				setVisible(false);
				dispose();
			}
		});
		buttonPanel.add(cancelButton);
		add(buttonPanel);

		pack();
		Point p = getLocation();
		p.x = p.x - 115;
		p.y = p.y - 75;
		setLocation(p);
		setVisible(true);
	}

	public void actionPerformed(ActionEvent event) {
		long iterLong = 0;
		int iter = 0;
		try {
			iterLong = Long.parseLong(iterationsField.getText());
			if (iterLong > 10000000) {
				JOptionPane.showMessageDialog(parentFrame, "Points to Plot "
						+ "must be\nless than 10,000,000",
						"Number Format Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			iter = (int) iterLong;
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(parentFrame,
					"Points to Plot must be "
							+ "an integer\nnumber with no commas.",
					"Number Format Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		int sk = 0;
		try {
			sk = Integer.parseInt(skipsField.getText());
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(parentFrame, "Skips must be"
					+ " an integer\nnumber with no commas.",
					"Number Format Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		double seed1 = 0;
		double seed2 = 0;
		boolean a = false, b = false;
		a = randomSeed.getState();
		b = polarCheckBox.getState();
		if (a) {
			Random rand1 = new Random();
			seed1 = rand1.nextDouble();
			seed1 = seed1 * 20 - 10;
			seed1 = (int) (seed1 * 100000);
			seed1 = seed1 / 100000;
			Random rand2 = new Random();
			seed2 = rand2.nextDouble();
			seed2 = seed2 * 20 - 10;
			seed2 = (int) (seed2 * 100000);
			seed2 = seed2 / 100000;
		} else {
			String seed1String = GUIUtil.parsePI(seedField1.getText());
			String seed2String = GUIUtil.parsePI(seedField2.getText());
			try {
				seed1 = Double.parseDouble(seed1String);
				seed2 = Double.parseDouble(seed2String);
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(parentFrame,
						"Seed values must be real"
								+ "\nnumbers with no commas.",
						"Number Format Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			if (b) {
				double r = seed1;
				double theta = seed2;
				seed1 = r * (Math.cos(theta));
				seed2 = r * (Math.sin(theta));
				seed1 = (int) (seed1 * 100000);
				seed1 = seed1 / 100000;
				seed2 = (int) (seed2 * 100000);
				seed2 = seed2 / 100000;
			}
		}
		Complex seedValue = new Complex(seed1, seed2);
		try {
			if (sessionType == GUIUtil.NEW_DIALOG) {
				parentFrame.setCurrentSession(new Session(parentFrame,
						new EmptySessionImporter(iter, sk, seedValue)));
			} else {
				Session s = parentFrame.getCurrentSession();
				s.setIterations(iter);
				s.setSkips(sk);
				s.setSeedValue(seedValue);
			}
		} catch (InvalidSessionParametersException e) {
			JOptionPane.showMessageDialog(parentFrame, e.getMessage(),
					"Number Format Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		setVisible(false);
		dispose();
	}

}
