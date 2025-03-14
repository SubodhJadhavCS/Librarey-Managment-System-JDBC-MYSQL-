package lms;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

public class checkOutFilter extends FilterSearchGUI implements ActionListener {
	
	/**
	 * Default Keyword Option
	 */
	private static String selectedOption = "patron_ID"; // default value
	
	/**
	 * Array of Keyword Options
	 */
	private static final String[] options = {"patron_ID", "author"};
	
	/**
	 * Constructor. Calls Superclass constructor and passes in new array of keywords
	 */
	public checkOutFilter() {
		super(options);
		
		
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		     
		Connection connection = DatabaseConnection.openDatabase();
		// if the action is a dropDown switch, switch our selectedOption. Otherwise, show a table.
		if ("keyword".equals(e.getActionCommand())) {
			selectedOption = String.valueOf(keywordDropDown.getSelectedItem());  
		}else if ("go".equals(e.getActionCommand())) {
			switch (selectedOption) {				
				case "patron_ID":
					System.out.println(super.criterion.getText());
				    Librarian.getCheckOutsList(connection, "checkoutsview", "patron_ID", super.criterion.getText());
				    break;
				case "author":
					Librarian.getCheckOutsList(connection, "checkoutsview", "author", super.criterion.getText());
					break;
				
			}
		}
		DatabaseConnection.closeDatabase(connection);
		
			  	

	}

}
