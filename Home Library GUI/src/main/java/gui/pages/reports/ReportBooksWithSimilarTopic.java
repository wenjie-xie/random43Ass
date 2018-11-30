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

public class ReportBooksWithSimilarTopic extends ReportWithOneInput {

	public ReportBooksWithSimilarTopic() {
		super("Books With Similar Topic Table Generator:", "Topic:");
	}

	@Override
	protected JButton createSubmitBtn() {
		JButton submitBtn = new JButton("Submit");
		submitBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					String topic = textField.getText();
					HashMap<String, ArrayList<String>> table = DatabaseConnectionReportApi.generateBooksWithSimilarTopic(topic);
					HL_xiewen4.mainFrame.flipPageTo(new ViewFilterResultPanel(table));
					
				} catch (NullPointerException e1) {
					JOptionPane.showMessageDialog(HL_xiewen4.mainFrame, "Topic can not be blank.");
				}
				
			}
		});
		return submitBtn;
	}

}
