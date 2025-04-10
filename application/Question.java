package application;

import java.sql.SQLException;

import databasePart1.DatabaseHelper;

/*******
 * <p> Title: Question class. </p>
 * 
 * <p> Description: Represents a Question entity in the system.. </p>
 * 
 */
public class Question {
    private String userName;
    private String title;
    private String text;
    private int identity;
    private AnswerSubset answers = new AnswerSubset();
    private static final DatabaseHelper databaseHelper = new DatabaseHelper();

    /**
	 * This is the constructor to create a brand new question
     * @param userName Username of the author who wrote the question
     * @param title  The title of the question
     * @param text  The main body of text that makes up the question
     * @throws SQLException if a database access error occurs or the SQL statement is invalid.
     */
    public Question(String userName, String title, String text) throws SQLException {
    	databaseHelper.connectToDatabase();
        this.userName = userName;
        this.title = title;
        this.text = text;
        identity = databaseHelper.nextId();
    }
    
    	/**
    	* This is the constructor to recreate a previous question
        * @param userName Username of the author who wrote the question
        * @param title  The title of the question
        * @param text  The main body of text that makes up the question
        * @param id  The id of the question
        */
    public Question(String userName, String title, String text, int id) {
        this.userName = userName;
        this.title = title;
        this.text = text;
        identity = id;
    }
    
    /**
	 * This changes the text of an question
     * @param text  The main body of new text that will make up the question
     * @throws SQLException if a database access error occurs or the SQL statement is invalid.
     */
    public void editQuestion(String text) throws SQLException {
    	databaseHelper.editQuestion(identity, text);
        this.text = text;
    }
    
    /**
	 * This responds to a question with an answer
     * @param a  The answer to be added as a response to question
     */
    public void answerQuestion(Answer a) {
        answers.addAnswer(a);
    }
    
    /**
	 * Creates a string with all the information of the question
	 * @return the question in the form of a string
	 */
    public String viewQuestion() {
        String full = title + " #"+ identity +"\n"
        		+ "By: "+userName +"\n"
        		+ text +"\n";
        return full;
    }
    
    /**
	 * Creates a string with all the information of the question and responses
	 * @return the question and responses in the form of a string
	 */
    public String viewFull() {
    	try {
			databaseHelper.connectToDatabase();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	answers = databaseHelper.getRelated(identity);
    	String full = "";
    	String answerText = answers.readAll(5);
        full = "#"+ identity +"\n"
        		+ "By: "+userName +"\n"
        		+ text +"\n"
        		+ answerText;
        return full;
    }
    
    /**
	 * Returns the username of the question
	 * @return the Username of the question
	 */
    public String getUsername() { return userName; }
    /**
	 * Returns the title of the question
	 * @return The title of the question
	 */
    public String getTitle() { return title; }
    /**
	 * Returns the text of the question
	 * @return The text of the question
	 */
    public String getText() { return text; }
    /**
	 * Returns the id of the question
	 * @return The id of the question
	 */
    public int getIdentity() { return identity; }
    /**
	 * Returns the Answers to the question
	 * @return the list of answer to the question
	 */
    public AnswerSubset getAnswers() { return answers; }

}
