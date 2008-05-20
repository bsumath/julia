package edu.bsu.julia.gui;

import java.awt.event.ActionEvent;
import javax.swing.*;
import java.awt.*;
import java.awt.print.*;
import edu.bsu.julia.Julia;


public class PrintImageAction extends AbstractAction implements Printable {
	
	private Julia parentFrame;
	PrinterJob job = PrinterJob.getPrinterJob();
	//for serializable interface: do not use
	public static final long serialVersionUID = 0;
	
	
	public PrintImageAction (Julia f) {
		super("Print Image", new ImageIcon
				(Thread.currentThread().getContextClassLoader().getResource
						("print.png")));
		putValue("SHORT_DESCRIPTION", "Print Image");
		putValue("LONG_DESCRIPTION", "Print the visible graph");
		parentFrame = f;
		
	}
	
	public void actionPerformed(ActionEvent event) { 
        PrinterJob pj = PrinterJob.getPrinterJob();
        pj.setPrintable(this);
        boolean response = pj.printDialog();
        if (response) {
            try {
                 pj.print();
            } catch (PrinterException e){
            	System.err.println (e);
            }
        }
	}
	
    public int print(Graphics g, PageFormat pf, int page) throws PrinterException {
    	if (page > 0) {
    		return NO_SUCH_PAGE;
    	}	
    	Graphics g2d = (Graphics)g;
    	g2d.translate((int)pf.getImageableX(), (int)pf.getImageableY());
    	JPanel frame = parentFrame.getTabbedPane().getActivePane();
    	frame.printAll(g2d);
    	return PAGE_EXISTS;
    }
}
