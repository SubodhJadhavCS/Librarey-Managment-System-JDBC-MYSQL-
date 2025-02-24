package lms;

import java.sql.Connection;

public class LibraryManagementSystem {
	
	
	
	public static void main(String[] args) {	
		// open db connection
		//Connection connection = DatabaseConnection.openDatabase();
		//Patron pat = new Patron(connection, "10");
		//pat.runPatron();
		//DatabaseConnection.closeDatabase(connection);
		new WelcomeGUI();
	
	}
	
}
