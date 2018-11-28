package gui.functions;

import javax.swing.JPanel;
import javax.swing.JTextArea;

public class GeneralFunctions extends JPanel {
	
	private static final long serialVersionUID = 3521180241214659401L;

	/**
	 * Get data from the text area as Integer
	 * @param textArea
	 * @return a Integer given, otherwise return null
	 */
	protected Integer textAreaToInt(JTextArea textArea) {
		Integer target;
		try {
			target = Integer.parseInt(textArea.getText());
		} catch (Exception e) {
			target = null;
		}
		return target;
	}
	
	/**
	 * Get data from the text area as String
	 * @param textArea
	 * @return a String given, otherwise return null
	 */
	protected String textAreaToString(JTextArea textArea) {
		String target;
		try {
			target = textArea.getText();
		} catch (NullPointerException e) {
			target = null;
		}
		if (textArea.getText().equals("")) {
			target = null;
		}
		return target;
	}
	
	/**
	 * change a integer to a string
	 * @param num
	 * @return
	 */
	protected static String formatIntToStr(Integer num) {
		String result = null;
		if (num != null)
			result = String.valueOf(num);
		return result;
	}
}
