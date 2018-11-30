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

public class ReportAuthorsPublication extends ReportWithOneInput {

	private static final long serialVersionUID = 3439662102512823539L;

	public ReportAuthorsPublication() {
		super("Author's Publication Generator:", "Author's Name:");
	}

	@Override
	protected JButton createSubmitBtn() {
		JButton submitBtn = new JButton("Submit");
		submitBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String authorName = textField.getText();
				
				if (authorName == null) {
					JOptionPane.showMessageDialog(HL_xiewen4.mainFrame, "Please input a name.");
				} else {
					HashMap<String, ArrayList<String>> table = DatabaseConnectionReportApi.generateAuthorsPublication(authorName);
					HL_xiewen4.mainFrame.flipPageTo(new ViewFilterResultPanel(table));
				}
				
			}
		});
		return submitBtn;
	}
}
