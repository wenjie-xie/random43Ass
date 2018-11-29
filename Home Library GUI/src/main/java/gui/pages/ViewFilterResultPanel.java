package gui.pages;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class ViewFilterResultPanel extends JPanel {
	
	private static final long serialVersionUID = 7444477024611266751L;

	public ViewFilterResultPanel(HashMap<String, ArrayList<String>> table) {
		
		String[] colNames = {"Product's Name", "Release Year", "Type", "Director/Singer/Author"};
		String[][] data = to2DArray(table);
 		JTable myTable = new JTable(data, colNames);
 		
 		JScrollPane scrollPane = new JScrollPane(myTable);
 		
 		this.setLayout(new BorderLayout());
 		this.add(scrollPane, BorderLayout.CENTER);
	}
	
	private String[][] to2DArray(HashMap<String, ArrayList<String>> table) {
		String[][] myArray = new String[table.get("product name").size()][4];
		
		ArrayList<String> productNameList = table.get("product name");
		ArrayList<String> productTypeList = table.get("product type");
		ArrayList<String> releaseYearList = table.get("release year");
		ArrayList<String> personNameList = table.get("person name");
		
		for (int i = 0; i < productNameList.size(); i++) {
			myArray[i][0] = productNameList.get(i);
			myArray[i][1] = releaseYearList.get(i);
			myArray[i][2] = productTypeList.get(i);
			myArray[i][3] = personNameList.get(i);
		}
		
		return myArray;
 	}
}
