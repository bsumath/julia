package edu.bsu.julia.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import edu.bsu.julia.Julia;
import edu.bsu.julia.gui.actions.ChangeColorAction;
import edu.bsu.julia.gui.actions.DeleteAction;
import edu.bsu.julia.gui.actions.DeleteSelectedAction;
import edu.bsu.julia.gui.actions.ForwardImageAction;
import edu.bsu.julia.gui.actions.InverseAction;
import edu.bsu.julia.gui.actions.PropertiesAction;
import edu.bsu.julia.gui.actions.SaveSetAction;
import edu.bsu.julia.output.OutputFunction;
import edu.bsu.julia.session.Session;

public class OutputPanel extends JPanel implements PropertyChangeListener {

	private Julia parentFrame;
	private Session s;
	private JList outputList;
	private DefaultListModel listModel = new DefaultListModel();
	private JButton inverseButton;
	private JButton forwardButton;
	// for serializable interface: do not use
	public static final long serialVersionUID = 0;

	private final MouseListener mouseListener = new MouseAdapter() {
		public void mousePressed(MouseEvent e) {
			checkPopup(e);
		}

		public void mouseClicked(MouseEvent e) {
			checkPopup(e);
		}

		public void mouseReleased(MouseEvent e) {
			checkPopup(e);
		}

		private void checkPopup(MouseEvent e) {
			if (e.isPopupTrigger()) {
				final int index = outputList.locationToIndex(e.getPoint());
				JPopupMenu popup = new JPopupMenu();
				popup.add(new ChangeColorAction(parentFrame, listModel, index));
				popup.add(new DeleteAction(parentFrame, listModel, index,
						Julia.OUTPUTTYPE));
				popup.add(new SaveSetAction(parentFrame, listModel, index));
				popup.addSeparator();
				popup.add(new PropertiesAction(parentFrame, listModel, index));
				popup.addSeparator();
				popup.add(new DeleteSelectedAction(parentFrame,
						Julia.OUTPUTTYPE));
				popup.show(outputList, e.getX(), e.getY());
			}
		}
	};

	public OutputPanel(Julia f) {
		super();
		parentFrame = f;
		s = parentFrame.getCurrentSession();
		parentFrame.addListener(this);
		s.addListener(this);
		GridBagLayout layout = new GridBagLayout();
		setLayout(layout);
		GridBagConstraints constraints = new GridBagConstraints();

		constraints.fill = GridBagConstraints.BOTH;
		constraints.weightx = 1000;
		constraints.weighty = 1000;
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;

		Vector<OutputFunction> fns = s.getOutputFunctions();
		for (int i = 0; i < fns.size(); i++)
			listModel.addElement(fns.elementAt(i));

		outputList = new JList(listModel);
		outputList
				.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		outputList.setFocusable(false);
		outputList.setBackground(getBackground());
		outputList.setBorder(BorderFactory.createTitledBorder("Output Sets"));
		outputList.setCellRenderer(new OutputListCellRenderer());
		outputList.setName("Sets output by processing.  Ctrl/Shift + click "
				+ "selects multiple sets.");

		if (!listModel.isEmpty()) {
			outputList.addMouseListener(mouseListener);
		}
		outputList.addMouseListener(parentFrame.getStatusBar());

		JScrollPane scroller = new JScrollPane(outputList);
		layout.setConstraints(scroller, constraints);
		add(scroller);

		constraints.gridy = 1;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.weightx = 1;
		constraints.weighty = 1;

		inverseButton = new JButton(new InverseAction(parentFrame));
		inverseButton.setFocusable(false);
		if (listModel.isEmpty())
			inverseButton.setEnabled(false);
		inverseButton.addMouseListener(parentFrame.getStatusBar());
		layout.setConstraints(inverseButton, constraints);
		add(inverseButton);

		constraints.gridy = 2;

		forwardButton = new JButton(new ForwardImageAction(parentFrame));
		forwardButton.setFocusable(false);
		if (listModel.isEmpty())
			forwardButton.setEnabled(false);
		forwardButton.addMouseListener(parentFrame.getStatusBar());
		layout.setConstraints(forwardButton, constraints);
		add(forwardButton);

		setMinimumSize(new Dimension(200, 600));
	}

	public JList getOutputList() {
		return outputList;
	}

	public void propertyChange(PropertyChangeEvent event) {
		String name = event.getPropertyName();
		if (name.equals("addOutputFunction")) {
			OutputFunction fn = (OutputFunction) event.getNewValue();
			if (!fn.isLoaded())
				fn.load();
			if (listModel.isEmpty()) {
				inverseButton.setEnabled(true);
				forwardButton.setEnabled(true);
				outputList.addMouseListener(mouseListener);
			}
			listModel.addElement(fn);
			fn.addListener(this);
			outputList.addSelectionInterval(listModel.size() - 1, listModel
					.size() - 1);
		} else if (name.equals("session")) {
			s = (Session) event.getNewValue();
			outputList.clearSelection();
			listModel.clear();

			// populate the listModel
			for (OutputFunction o : s.getOutputFunctions())
				listModel.addElement(o);

			// see if buttons should be enabled or not
			if (!listModel.isEmpty()) {
				inverseButton.setEnabled(true);
				forwardButton.setEnabled(true);
				outputList.addMouseListener(mouseListener);
			} else {
				inverseButton.setEnabled(false);
				forwardButton.setEnabled(false);
				outputList.removeMouseListener(mouseListener);
			}

			outputList.setCellRenderer(new OutputListCellRenderer());
			s.addListener(this);
		} else if (name.equals("Color") || name.equals("repaint")) {
			outputList.repaint();
		} else if (name.equals("deleteOutputFunction")) {
			Integer i = (Integer) event.getNewValue();
			int index = Integer.valueOf(i);
			listModel.remove(index);
			if (listModel.isEmpty()) {
				inverseButton.setEnabled(false);
				forwardButton.setEnabled(false);
				outputList.removeMouseListener(mouseListener);
			}
			outputList.setCellRenderer(new OutputListCellRenderer());
			outputList.repaint();
		} else if (name.equals("reselect")) {
			outputList.setSelectedIndices(outputList.getSelectedIndices());
		}
	}

}
