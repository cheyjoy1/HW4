package application;

import java.sql.SQLException;

import databasePart1.DatabaseHelper;

/*******
 * <p> Title: Answer class. </p>
 * 
 * <p> Description: Represents the answers to questions. </p>
 * 
 */
public class Answer {
    private String userName;
    private String text;
    private int questionID;
    private int identity;
    private static final DatabaseHelper databaseHelper = new DatabaseHelper();
    private boolean trueAnswer;
    private AnswerSubset responses = new AnswerSubset();

    /**
	 * This is the constructor to create a brand new answer
     * @param userName Username of the author who wrote the answer
     * @param text  The main body of text that makes up the answer
     * @param questionID  The id of the question being answered
     * @throws SQLException if a database access error occurs or the SQL statement is invalid.
     */
    public Answer(String userName, String text, int questionID) throws SQLException {
    	databaseHelper.connectToDatabase();
        this.userName = userName;
        this.text = text;
        this.questionID = questionID;
        identity = databaseHelper.nextId();
        trueAnswer = false;
    }
    
    /**
	 * This is the constructor to recreate a previous answer
     * @param userName Username of the author who wrote the answer
     * @param text  The main body of text that makes up the answer
     * @param questionID  The id of the question being answered
     * @param id The id of the answer being created
     * @param trueAnswer Whether or not the answer is the main answer for to the question
     */
    public Answer(String userName, String text, int questionID, int id, boolean trueAnswer) {
        this.userName = userName;
        this.text = text;
        this.questionID = questionID;
        identity = id;
        this.trueAnswer = trueAnswer;
    }
    
    /**
	 * This changes the text of an answer
     * @param text  The main body of new text that will make up the answer
     * @throws SQLException if a database access error occurs or the SQL statement is invalid.
     */
    public void editAnswer(String text) throws SQLException {
        databaseHelper.editAnswer(identity, text);
        this.text = text;
    }
    
    /**
	 * Sets the answers as the main answer to the question
	 * @throws SQLException if a database access error occurs or the SQL statement is invalid.
	 */
    public void setToAnswer() throws SQLException {
        databaseHelper.setAnswer(identity);
        trueAnswer = true;
    }
    
    /**
	 * Creates a string with all the information of the answer
	 * @return the answer in a string form
	 */
    public String viewAnswer() {
    	String full = "";
    	if(trueAnswer){
    		full = "#"+ identity +" ✓ \n"
            		+ "By: "+userName +"\n"
            		+ text +"\n";
    	}
    	else {
        full = "#"+ identity +"\n"
        		+ "By: "+userName +"\n"
        		+ text +"\n";
    	}
        return full;
    }
    
    /**
	 * Creates a string with all the information of the answer and the responses 
	 * @param depth  Determines how many layers of responses to go down
	 * @return the answer and its responses in string form
	 */
    public String viewFull(int depth) {
    	if (depth <= 0) {
            return ""; // Stop recursion when depth limit is reached
        }
    	try {
			databaseHelper.connectToDatabase();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	String full = "";
    	responses = databaseHelper.getRelated(identity);
    	String responsesText = responses.readAll(depth-1);
    	if(trueAnswer){
    		full = "#"+ identity +" By: "+userName +" Responding to "+questionID+" ✓\n"
            		+ text +"\n"
            		+ responsesText;
    	}
    	else {
        full = "#"+ identity +" By: "+userName +" Responding to "+questionID+"\n"
        		+ text +"\n"
        		+ responsesText;
    	}
        return full;
    }
    
    /**
	 * Returns the username of answer
	 * @return the Username of an answer
	 */
    public String getUsername() { return userName; }
    
    /**
	 * Returns the text of answer
	 * @return The text in answer
	 */
    public String getText() { return text; }

    /**
	 * Returns the id of the question or answer it is responding to
	 * @return The id of the question or answer
	 */
    public int getQuestionID() { return questionID; }

    /**
	 * Returns the id of the answer
	 * @return The id of the answer being used
	 */
    public int getIdentity() { return identity; }

    /**
	 * Returns whether answer is the main answer to the question
	 * @return True if this in the main answer, False if not the main answer
	 */
    public boolean getAnswered() { return trueAnswer; }

    /**
	 * Returns the list of responses to answer
	 * @return The list of responses to answer
	 */
    public AnswerSubset getResponses() { return responses; }


}
