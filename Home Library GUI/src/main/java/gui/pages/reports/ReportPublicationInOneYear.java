package gui.pages.reports;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import app.HL_xiewen4;
import database.DatabaseConnectionReportApi;
import gui.pages.ViewFilterResultPanel;

public class ReportPublicationInOneYear extends ReportWithOneInput {

	private static final long serialVersionUID = -6841439573710681832L;

	public ReportPublicationInOneYear() {
		super("Publication In One Year Generator:", "Year of Publication:");
	}

	@Override
	protected JButton createSubmitBtn() {
		JButton submitBtn = new JButton("Submit");
		submitBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					String input = textField.getText();

					int year = Integer.valueOf(input);
					
					HashMap<String, ArrayList<String>> table = DatabaseConnectionReportApi.generatePublicationInOneYear(year);
					HL_xiewen4.mainFrame.flipPageTo(new ViewFilterResultPanel(table));
					
				} catch (NullPointerException e1) {
					JOptionPane.showMessageDialog(HL_xiewen4.mainFrame, "Field can not be blank.");
				} catch (NumberFormatException e2) {
					JOptionPane.showMessageDialog(HL_xiewen4.mainFrame, "Year of publication must be a number.");
				}
				
			}
		});
		
		return submitBtn;
	}

}
