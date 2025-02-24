package lms;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

public class overdueFilter extends FilterSearchGUI implements ActionListener {
	
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
	public overdueFilter() {
		super(options);
		
		
		
	}
	
	/**
	 * Overriding the abstract actionPerformed method. Handles the connection to database and keyword recognition 
	 */
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
				    Librarian.getOverdueList(connection, "checkoutsview", "patron_ID", super.criterion.getText());
				    break;
				case "author":
					Librarian.getOverdueList(connection, "checkoutsview", "author", super.criterion.getText());
					break;
				
			}
		}
		DatabaseConnection.closeDatabase(connection);
		
			  	

	}

}
