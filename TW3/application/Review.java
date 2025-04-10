package application;

import java.sql.SQLException;

import databasePart1.DatabaseHelper;

/*******
 * <p> Title: Review class. </p>
 * 
 * <p> Description: Represents the Review of questions and answers. </p>
 * 
 */
public class Review {
    private String userName;
    private String text;
    private int questionID;
    private int identity;
    private static final DatabaseHelper databaseHelper = new DatabaseHelper();

    /**
	 * This is the constructor to create a brand new review
     * @param userName Username of the author who wrote the review
     * @param text  The main body of text that makes up the review
     * @param questionID  The id of the question being reviewed
     * @throws SQLException if a database access error occurs or the SQL statement is invalid.
     */
    public Review(String userName, String text, int questionID) throws SQLException {
    	databaseHelper.connectToDatabase();
        this.userName = userName;
        this.text = text;
        this.questionID = questionID;
        identity = databaseHelper.nextIdReview();
    }
    
    /**
	 * This is the constructor to recreate a previous review
     * @param userName Username of the author who wrote the review
     * @param text  The main body of text that makes up the review
     * @param questionID  The id of the question being reviewed
     * @param id The id of the review being created
     */
    public Review(String userName, String text, int questionID, int id) {
        this.userName = userName;
        this.text = text;
        this.questionID = questionID;
        identity = id;
    }
    
    
    /**
	 * This changes the text of an review
     * @param text  The main body of new text that will make up the review
     * @throws SQLException if a database access error occurs or the SQL statement is invalid.
     */
    public void editReview(String text) throws SQLException {
        databaseHelper.editReview(identity, text);
        this.text = text;
    }
        
    /**
	 * Creates a string with all the information of the review
	 * @return The review in a string format
	 */
    public String viewReview() {
    	String full = "";
        full = "Review: #"+ identity +" responding to #"+questionID+"\n"
        		+ "By: "+userName +"\n"
        		+ text +"\n";
        return full;
    }
    
    
    /**
	 * Returns the username of review
	 * @return The username of the author
	 */
    public String getUsername() { return userName; }
    
    /**
	 * Returns the text of review
	 * @return The text of the review
	 */
    public String getText() { return text; }

    /**
	 * Returns the id of that the review is responding to
	 * @return the id of that the review is responding to
	 */
    public int getQuestionID() { return questionID; }

    /**
	 * Returns the id of the review
	 * @return the id of this review
	 */
    public int getIdentity() { return identity; }



}
