package gui.pages;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class ViewFilterResultPanel extends JPanel {
	
	private static final long serialVersionUID = 7444477024611266751L;

	public ViewFilterResultPanel(HashMap<String, ArrayList<String>> table) {
		ArrayList<String> col = new ArrayList<>(table.keySet());
		String[] colNames = {col.get(0), col.get(1), col.get(2), col.get(3)};
		String[][] data = to2DArray(table, colNames);
		
		@SuppressWarnings("serial")
		DefaultTableModel model = new DefaultTableModel(data, colNames) {
			@Override
		    public boolean isCellEditable(int row, int column) {
		       return false;
		    }
			
		};
 		JTable myTable = new JTable();
 		myTable.setModel(model);
 		
 		JScrollPane scrollPane = new JScrollPane(myTable);
 		
 		this.setLayout(new BorderLayout());
 		this.add(scrollPane, BorderLayout.CENTER);
	}
	
	private String[][] to2DArray(HashMap<String, ArrayList<String>> table, String[] colNames) {
		String[][] myArray = new String[table.get(colNames[0]).size()][colNames.length];
		
		for (int i = 0; i < table.get(colNames[0]).size(); i++) {
			for (int j = 0; j < colNames.length; j++) {
				String key = colNames[j];
				myArray[i][j] = table.get(key).get(i);
			}
		}
		
		return myArray;
 	}
}
