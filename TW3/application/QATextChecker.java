package application;

import java.sql.SQLException;

import databasePart1.DatabaseHelper;

/*******
 * <p> Title: QATextChecker class. </p>
 * 
 * <p> Description: Functions that validates the text that will be sent for questions and answers. </p>
 * 
 */
public class QATextChecker {
    private static char currentChar;
    private static final DatabaseHelper databaseHelper = new DatabaseHelper();
    
    /**
     * Validates the body text for questions and answers
     * @param input The body text of a question or answer
     * @return The error if invalid or Valid
     */
	public static String checkForValidBodyText(String input) {
		if(input.length() > 20){
			for(int i = 0; i < input.length(); i++) {
				currentChar = input.charAt(i);
				if((currentChar >= 'A' && currentChar <= 'Z' ) ||		// Check for A-Z
					(currentChar >= 'a' && currentChar <= 'z' )||		// Check for a-z
					(currentChar >= '0' && currentChar <= '9' )) {
					return "Valid";
				}
			}
			return "The Body must have some non-special character";
		}else {
			return "The Body must be at least 20 characters long";
		}
	}
	
	/**
     * Validates the title text for questions
     * @param input The title of a question
     * @return The error if invalid or Valid
     */
	public static String checkForValidTitleText(String input) {
		if(input.length() > 5){
			if(input.length() <= 100){
				return "Valid";
			}
			return "The title must be no more than 100 characters long";
		}else {
			return "The title must be at least 5 characters long";
		}
	}
	
	/**
     * Validates that the id is in the system
     * @param input The id for a question or answer
     * @return The error if invalid or Valid
     */
	public static String checkForValidID(int input) {
		try {
			databaseHelper.connectToDatabase();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Question q = databaseHelper.getQuestion(input);
		Answer a =databaseHelper.getAnswer(input);
		if(q != null || a != null) {
			return "Valid";
		}else {
			return "The ID must exist";
		}
	}
	
	/**
     * Validates that the id is in the system
     * @param input The id for an answer
     * @return The error if invalid or Valid
     */
	public static String checkForValidAnsID(int input) {
		try {
			databaseHelper.connectToDatabase();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Answer a =databaseHelper.getAnswer(input);
		if(a != null) {
			return "Valid";
		}else {
			return "The ID must exist";
		}
	}
	
	/**
     * Validates that the id for a review is in the system
     * @param input The id for an review
     * @return The error if invalid or Valid
     */
	public static String checkForValidReviewID(int input) {
		try {
			databaseHelper.connectToDatabase();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Review a =databaseHelper.getReview(input);
		if(a != null) {
			return "Valid";
		}else {
			return "The ID must exist";
		}
	}

}
