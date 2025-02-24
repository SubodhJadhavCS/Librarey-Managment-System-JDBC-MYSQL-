package lms;
import java.sql.Connection;


/**
 * Collection of functions for librarians
 * @author DannyGalaxy
 *
 */
public class Librarian {
	/*ADDITIONAL FUNCTION
	 * UPDATE PATRON FINE (Manual input value)
	 */
	

	// just a place to organize/store our functions
	
	// no instance variables
	// no static variables
	// no constructors
	
	/**
	 * Calls addToTable() to construct INSERT INTO query to add a book to the 'books' table
	 * @param connection Connection (Object) to use for database
	 * @param table Name of table to be queried (in this case it should always be `books`)
	 * @param title Title of the book to be added
	 * @param author Author of the book to be added
	 * @param genre Genre of the book to be added
	 */
	public static void addBook(Connection connection,  String table, String title, String author, String genre) {
		
		// create String array to pass into the INSERT INTO query as values
		String[] values = {title, author, genre};
		
		// Call addToTable() with value array
		DatabaseQueries.addToTable(connection, table, values);
		
	}
	
	/**
	 * Calls removeFromTable() to construct a DELETE query which removes a book from a table, given an ID
	 * @param connection Connection (Object) to use for database
	 * @param table Name of table to be queried
	 * @param bookID ID of the book to remove. This should be acquired through a separate function that retrieves available IDs
	 * given the composite key of author and title
	 */
	public static void removeBook(Connection connection,  String table, int bookID) {
		
		// Create the keyword, or the column we are matching against our ID
		String keyword = "book_ID";
		
		// Create the criterion, which is the specific ID we are passing in. Cast it to a string to work with the generalized
		// removeFromTable method
		String criterion = Integer.toString(bookID);
		
		// Call removeFromTable to construct the DELETE query
		DatabaseQueries.removeFromTable(connection, table, keyword, criterion);
		
	}
	
	/**
	 * Calls removeBook() to remove book from all tables in database
	 * @param connection Connection (Object) to use for database
	 * @param bookID ID of the book to remove. This should be acquired through a separate function that retrieves available IDs
	 * given the composite key of author and title
	 */
	public static void removeBookFromAllTables(Connection connection, int bookID) {
		
		// Remove book from holds and checkouts tables first, then remove from books table
		removeBook(connection, "holds" , bookID);
		removeBook(connection, "checkouts" , bookID);
		removeBook(connection, "books" , bookID);
		
	}
	
	/**
	 * Calls removeFromTable() to construct a DELETE query which removes a patron from the 'patrons' table, given an ID
	 * Note: This method could be combined with removeBook fairly easily (by just adding a String keyword parameter), but we
	 * thought it was a bit cleaner for LibraryManagementSystem and for JavaDocs if we just had separate methods
	 * @param connection Connection (Object) to use for database
	 * @param table Name of table to be queried (in this case it should always be `patrons`)
	 * @param patronID ID of the patron to remove. This should be acquired through a separate function that retrieves available IDs
	 * given the composite key of first and last name
	 */
	public static void removePatron(Connection connection,  String table, int patronID) {
		
		// Create the keyword, or the column we are matching against our ID
		String keyword = "patron_ID";
		
		// Create the criterion, which is the specific ID we are passing in. Cast it to a string to work with the generalized
		// removeFromTable method
		String criterion = Integer.toString(patronID);
		
		// Call removeFromTable to construct the DELETE query
		DatabaseQueries.removeFromTable(connection, table, keyword, criterion);
	}
	
	/**
	 * Calls removePatron() to remove patron from all tables in database
	 * @param connection Connection (Object) to use for database
	 * @param patronID ID of the patron to remove. This should be acquired through a separate function that retrieves available IDs
	 * given the composite key of first and last name
	 */
	public static void removePatronFromAllTables(Connection connection, int patronID) {
		
		// Remove from holds and checkouts tables first, then remove from patrons table
		removeBook(connection, "holds" , patronID);
		removeBook(connection, "checkouts" , patronID);
		removeBook(connection, "patrons" , patronID);
		
	}
	
	/**
	 * Constructs SELECT query to get a list of holds
	 * @param connection Connection (Object) to use for database
	 * @param holdsView view from which to query (should always be 'holdsview')
	 * @param keyword column on which to match. Leave out this and criterion to select everything.
	 * @param criterion criterion to match on.  Leave out this and keyword to select everything.
	 */
	public static void getHoldsList(Connection connection, String holdsView, String keyword, String criterion) {
		//SELECT (Whateverwesaid) where keyword = criterion
		// default values are for getting all holds or checkouts (not based on a search criterion)
		// if keyword is default, then use a Select * 
		// otherwise
		
		/*
		 LIST HOLDS view: (we want all columns)
		 Title | Book ID | Patron | Patron ID | Request Date
		 */
		
		// 12/07 addition
		// *** SORT by Patron then Author then Title ****
		
		// Construct query string
		String getHoldsQuery = "SELECT *"
				+ " FROM " + holdsView
				+ " WHERE " + keyword + "= \"" + criterion + "\" "
				+ " ORDER BY firstName, lastName, author, title;";
		
		// Create array of columns that will be returned (to eventually pass into printResult())
		String[] returnColumns = {"book_ID", "title", "author", "checkedOut", "requestDate", "patron_ID", "firstName", "lastName"};
				
		// read from db; calls printResults method (results displayed in window)
		DatabaseQueries.printFromDatabase(connection, getHoldsQuery, returnColumns);
	}
	
	/**
	 * Overloaded getHoldsList, with default keyword and criterion (use to select all).
	 * Constructs SELECT query to get a list of holds
	 * @param connection Connection (Object) to use for database
	 * @param holdsView view from which to query (should always be 'holdsview')
	 */
	public static void getHoldsList(Connection connection, String holdsView) {
		//SELECT (Whateverwesaid) where keyword = criterion
		// default values are for getting all holds or checkouts (not based on a search criterion)
		// if keyword is default, then use a Select * 
		// otherwise
		
		/*
		 LIST HOLDS view: (we want all columns)
		 Title | Book ID | Patron | Patron ID | Request Date
		 */
		
		// 12/07 addition
		// *** SORT by Patron then Author then Title ****
		
		// Construct query string
		String getHoldsQuery = "SELECT *"
				+ " FROM " + holdsView
				+ " ORDER BY firstName, lastName, author, title;";
		
		// Create array of columns that will be returned (to eventually pass into printResult())
		String[] returnColumns = {"book_ID", "title", "author", "checkedOut", "requestDate", "patron_ID", "firstName", "lastName"};
				
		// read from db; calls printResults method (results displayed in window)
		DatabaseQueries.printFromDatabase(connection, getHoldsQuery, returnColumns);
	}
	
	/**
	 * Constructs SELECT query to get a list of checked out books, along with their return due dates, potential days late, and fine amounts
	 * @param connection Connection (Object) to use for database
	 * @param checkOutsView view from which to query (should always be 'checkoutsview')
	 */
	public static void getCheckOutsList(Connection connection, String checkOutsView, String keyword, String criterion) {
		// keyword and criterion allow for filtering by title, author, patron
	
		/* *** view--> built in database by double join; select query of view
		 LIST CHECK-OUTS view:
		 1 patron_ID, 2firstName, 3lastName, 4book_ID, 5title, 6onHold, 7checkOutDate, 8dailyFineAmount, 9dueDate, 10daysLate, 11fineAmount
		 
		 desiredColumns = {1,2,3,4,5,6,7,9,10,11}
		 
		 
		// SO, this method may actually end up being the same as the getOverdue where we end up passing the keyword and criterion
		// and for overdues, the keyword is overdues or whwatever the name of the view is; and the criterion is ""/null
		 
		 ^^ when we ask for check outs or overdue, we'll just pass the column numbers we want [hard coded] (see below)
		 
		 
		 
		 */
		
		// Construct query string
		String getCheckoutsQuery = "SELECT *"
				+ " FROM " + checkOutsView
				+ " WHERE " + keyword + " = \"" + criterion + "\" "
				+ " ORDER BY firstName, lastName, author, title;";
		
		// Create array of columns that will be returned (to eventually pass into printResult())
		String[] returnColumns = {"patron_ID", "firstName", "lastName", "book_ID", "title", "author", 
				"onHold", "checkOutDate", "dailyFineAmount", "dueDate", "daysLate", "fineAmount"};
				
		// read from db; calls printResults method (results displayed in window)
		DatabaseQueries.printFromDatabase(connection, getCheckoutsQuery, returnColumns);
	}
	
	/**
	 * Overloaded getCheckOutsList, with default keyword and criterion (use to select all)
	 * Constructs SELECT query to get a list of checked out books, along with their return due dates, potential days late, and fine amounts
	 * @param connection Connection (Object) to use for database
	 * @param checkOutsView view from which to query (should always be 'checkoutsview')
	 * @param keyword column on which to match. Leave out this and criterion to select everything.
	 * @param criterion criterion to match on.  Leave out this and keyword to select everything.
	 */
	public static void getCheckOutsList(Connection connection, String checkOutsView) {
		// keyword and criterion allow for filtering by title, author, patron
	
		/* *** view--> built in database by double join; select query of view
		 LIST CHECK-OUTS view:
		 1 patron_ID, 2firstName, 3lastName, 4book_ID, 5title, 6onHold, 7checkOutDate, 8dailyFineAmount, 9dueDate, 10daysLate, 11fineAmount
		 
		 desiredColumns = {1,2,3,4,5,6,7,9,10,11}
		 
		 
		// SO, this method may actually end up being the same as the getOverdue where we end up passing the keyword and criterion
		// and for overdues, the keyword is overdues or whwatever the name of the view is; and the criterion is ""/null
		 
		 ^^ when we ask for check outs or overdue, we'll just pass the column numbers we want [hard coded] (see below)
		 
		 
		 
		 */
		
		// Construct query string
		String getCheckoutsQuery = "SELECT *"
				+ " FROM " + checkOutsView
				+ " ORDER BY firstName, lastName, author, title;";
		
		// Create array of columns that will be returned (to eventually pass into printResult())
		String[] returnColumns = {"patron_ID", "firstName", "lastName", "book_ID", "title", "author", 
				"onHold", "checkOutDate", "dailyFineAmount", "dueDate", "daysLate", "fineAmount"};
				
		// read from db; calls printResults method (results displayed in window)
		DatabaseQueries.printFromDatabase(connection, getCheckoutsQuery, returnColumns);
	}
	
	/**
	 * Constructs SELECT query to get all overdue books
	 * @param connection Connection (Object) to use for database
	 * @param checkOutsView view from which to query (should always be 'checkoutsview')
	 * @param keyword column on which to match. Leave out this and criterion to select everything.
	 * @param criterion criterion to match on.  Leave out this and keyword to select everything.
	 */
	public static void getOverdueList(Connection connection, String checkOutsView, String keyword, String criterion) {	// special case filtering of above based on date???
		// SELECT from VIEW
		// goal: list of over due books
		/* display the following columns from the CheckOutsList view
		 * where daysLate > 0
		 * desiredColums = all but 6 desiredColumns = {1,2,3,4,5,7,9,10,11}
		 * *** unless we group by patron_ID and just get the total that each patron owes?
		*/
		
		// Construct query string
		String getOverdueQuery = "SELECT patron_ID, firstName, lastName, book_ID, title, author,"
				+ " checkOutDate, dailyFineAmount, dueDate, daysLate, fineAmount"
				+ " FROM " + checkOutsView
				+ " WHERE daysLate > 0 && " + keyword + " = \"" + criterion + "\""
				+ " ORDER BY firstName, lastName, author, title;";
		
		// Create array of columns that will be returned (to eventually pass into printResult())
		String[] returnColumns = {"patron_ID", "firstName", "lastName", "book_ID", "title", "author", 
				"checkOutDate", "dailyFineAmount", "dueDate", "daysLate", "fineAmount"};
				
		// read from db; calls printResults method (results displayed in window)
		DatabaseQueries.printFromDatabase(connection, getOverdueQuery, returnColumns);
		
	}
	
	/**
	 * Overloaded getOverdueList method to get all overdue books
	 * Constructs SELECT query to get all overdue books
	 * @param connection Connection (Object) to use for database
	 * @param checkOutsView view from which to query (should always be 'checkoutsview')
	 */
	public static void getOverdueList(Connection connection, String checkOutsView) {	// special case filtering of above based on date???
		// SELECT from VIEW
		// goal: list of over due books
		/* display the following columns from the CheckOutsList view
		 * where daysLate > 0
		 * desiredColums = all but 6 desiredColumns = {1,2,3,4,5,7,9,10,11}
		 * *** unless we group by patron_ID and just get the total that each patron owes?
		*/
		
		// Construct query string
		String getOverdueQuery = "SELECT patron_ID, firstName, lastName, book_ID, title, author,"
				+ " checkOutDate, dailyFineAmount, dueDate, daysLate, fineAmount"
				+ " FROM " + checkOutsView
				+ " WHERE daysLate > 0" 
				+ " ORDER BY firstName, lastName, author, title;";
		
		// Create array of columns that will be returned (to eventually pass into printResult())
		String[] returnColumns = {"patron_ID", "firstName", "lastName", "book_ID", "title", "author", 
				"checkOutDate", "dailyFineAmount", "dueDate", "daysLate", "fineAmount"};
				
		// read from db; calls printResults method (results displayed in window)
		DatabaseQueries.printFromDatabase(connection, getOverdueQuery, returnColumns);
		
	}
	
}
