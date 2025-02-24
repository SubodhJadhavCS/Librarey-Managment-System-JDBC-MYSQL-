package lms;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.sql.Connection;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

public class AddBookGUI implements ActionListener {
	
	//Add various components. Helps to add them here in case we want to use the information in our ActionListener later
	
	
	/**
	 * Text field to input title info
	 */
	public static JTextField title = new JTextField();
	
	/**
	 * Text field to input author info
	 */
	public static JTextField author = new JTextField();
	
	/**
	 * Text field to input genre info
	 */
	public static JTextField genre = new JTextField();
	
	
	/**
	 * Button used to add book
	 */
	public static JButton addBook = new JButton("Add Book");
	    
	/**
	 * Constructor for the window.
	 * Sets sizes, layouts, and action events/listeners for all elements of the page
	 */
	public AddBookGUI(){    
	
	        // Creating instance of JFrame
	        JFrame frame = new JFrame("Welcome to our humble library!");
	        // Set the width and height of frame
	        frame.setSize(800, 400);
	            
	        //Create the panel that contains the "boxes" of content
	        JPanel boxes = new JPanel();
	        boxes.setLayout(new GridLayout(3,1,10,0));
	        
	        //Create the Header for the Sign in section
	        JPanel box1 = new JPanel();
	        JLabel signInHeader = new JLabel("Add Book to Database");
	        signInHeader.setHorizontalAlignment(SwingConstants.CENTER);
	        signInHeader.setFont(new Font("Arial", Font.BOLD, 18));
	        //signInHeader.setVerticalAlignment(SwingConstants.TOP);
	        box1.add(signInHeader);
	        
	        //Create a box that holds a Patron/Librarian radio button and the sign in text field
	        JPanel box2 = new JPanel();
	        box2.setLayout(new BoxLayout(box2, BoxLayout.LINE_AXIS));
	        
	        //Set a border, mainly to squish the components closer to the center
	        box2.setBorder(new EmptyBorder(10, 30, 10, 30));
	        
	        JPanel titlePanel = new JPanel();
	        titlePanel.setPreferredSize(new Dimension(120,25));
	        JLabel titleLabel = new JLabel("Title:");
	        title.setPreferredSize(new Dimension(120,25));
	        titlePanel.add(titleLabel);
	        titlePanel.add(title);
	        box2.add(titlePanel);
	        
	        JPanel authorPanel = new JPanel();
	        author.setPreferredSize(new Dimension(120,25));
	        JLabel authorLabel = new JLabel("Author:");
	        author.setPreferredSize(new Dimension(120,25));
	        authorPanel.add(authorLabel);
	        authorPanel.add(author);
	        box2.add(authorPanel);
	        
	        JPanel genrePanel = new JPanel();
	        genre.setPreferredSize(new Dimension(150,25));
	        JLabel genreLabel = new JLabel("Genre:");
	        genre.setPreferredSize(new Dimension(150,25));
	        genrePanel.add(genreLabel);
	        genrePanel.add(genre);
	        box2.add(genrePanel);
	        
	    
	        //Add addBook Button
	        JPanel box3 = new JPanel();
	        
	        addBook.setMnemonic(KeyEvent.VK_C);
	        addBook.setActionCommand("add");
	        addBook.addActionListener(this);
	        box3.add(addBook);
	        
	        // Use an empty panel to add space between two main sections
	        //JPanel spacer = new JPanel();
	        //spacer.add(Box.createRigidArea(new Dimension(10, 50)));
	      
	        boxes.add(box1);
	        boxes.add(box2);
	        boxes.add(box3);
	        
	        //Give entire window some padding
	        boxes.setBorder(new EmptyBorder(10, 0, 30, 0));
	        
	        //Add boxes to frame
	        frame.add(boxes);
	        //Setting the frame visibility to true
	        frame.setVisible(true);
	        //Close the Window without exiting application on close
	        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	    }

	/**
	 * Overriding the abstract actionPerformed method
	 * Writes logic to handle the actions for various buttons/fields in the window
	 * Detects login info/addBook button clicks to navigate to correct page.   
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if ("add".equals(e.getActionCommand())) {
			Connection connection = DatabaseConnection.openDatabase();
			
			//Ensure no fields are blank before adding
			if(!title.getText().isBlank() && !author.getText().isBlank() && !genre.getText().isBlank()) {
				Librarian.addBook(connection, "books", title.getText(), author.getText(), genre.getText());
				
				//get the book id
				String id = DatabaseQueries.getLastID(connection, "books", "book_ID");
			//Show a positive message if added, and an error message if not.	
				JOptionPane.showMessageDialog(null, "Book successfully added! Its id is" + id, "Success!", JOptionPane.INFORMATION_MESSAGE);
			}else {
				JOptionPane.showMessageDialog(null, "No book was added because one or more fields were left blank.", "Field Blank", JOptionPane.WARNING_MESSAGE);
			}
			
			
			DatabaseConnection.closeDatabase(connection);
		} 
    }

	
}
