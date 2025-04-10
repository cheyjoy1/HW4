package databasePart1;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.List;

import application.Answer;
import application.Review;
import application.AnswerSubset;
import application.Question;
import application.User;
import application.ReviewerRequest;
import application.Message;


/**
 * The DatabaseHelper class is responsible for managing the connection to the database,
 * performing operations such as user registration, login validation, and handling invitation codes.
 */
public class DatabaseHelper {
	// JDBC driver name and database URL 
	static final String JDBC_DRIVER = "org.h2.Driver";   
	static final String DB_URL = "jdbc:h2:~/FoundationDatabase";  

	//  Database credentials 
	static final String USER = "sa"; 
	static final String PASS = ""; 

	private Connection connection = null;
	private Statement statement = null; 
	//	PreparedStatement pstmt

	/**
     * Connects to the database
     * @throws SQLException if a database access error occurs
     */
	public void connectToDatabase() throws SQLException {
		try {
			Class.forName(JDBC_DRIVER); // Load the JDBC driver
			System.out.println("Connecting to database...");
			connection = DriverManager.getConnection(DB_URL, USER, PASS);
			statement = connection.createStatement(); 
			// You can use this command to clear the database and restart from fresh.
			//statement.execute("DROP ALL OBJECTS");

			createTables();  // Create the necessary tables if they don't exist
		} catch (ClassNotFoundException e) {
			System.err.println("JDBC Driver not found: " + e.getMessage());
		}
	}

	/**
     * Creates the SQL tables for users, codes, questions, answers, and reviews
     * @throws SQLException if a database access error occurs or the SQL statement is invalid.
     */
	private void createTables() throws SQLException {
		String userTable = "CREATE TABLE IF NOT EXISTS cse360users ("
				+ "id INT AUTO_INCREMENT PRIMARY KEY, "
				+ "userName VARCHAR(255) UNIQUE, "
				+ "password VARCHAR(255), "
				+ "role VARCHAR(100),"
				+ "email VARCHAR(255),"
				+ "name VARCHAR(255),"
				+ "tempPass VARCHAR(10));";
		statement.execute(userTable);
		
		// Create the invitation codes table
	    String invitationCodesTable = "CREATE TABLE IF NOT EXISTS InvitationCodes ("
	            + "code VARCHAR(4) PRIMARY KEY, "
	            + "expiry TIMESTAMP NOT NULL,"
	    		+ "role VARCHAR(255) NOT NULL,"
	            + "isUsed BOOLEAN DEFAULT FALSE)";
	    statement.execute(invitationCodesTable);
	    
	    // Create the questions table
	    String questions = "CREATE TABLE IF NOT EXISTS Questions ("
	            + "userName VARCHAR(255), "
	            + "title VARCHAR(150),"
	    		+ "text VARCHAR(255) NOT NULL,"
	            + "identityQ INT UNIQUE)";
	    statement.execute(questions);
	    
	 // Create the answers table
	    String answers = "CREATE TABLE IF NOT EXISTS Answers ("
	            + "userName VARCHAR(255),"
	            + "trueAnswer BOOLEAN,"
	    		+ "text VARCHAR(255) NOT NULL,"
	    		+ "questionID INT,"
	            + "identityA INT UNIQUE)";
	    statement.execute(answers);
	    
	 // Create the reviews table
	    String reviews = "CREATE TABLE IF NOT EXISTS Reviews ("
	            + "userName VARCHAR(255),"
	    		+ "text VARCHAR(255) NOT NULL,"
	    		+ "questionID INT,"
	            + "identityA INT UNIQUE)";
	    statement.execute(reviews);
	    
	    String reviewerRequests = "CREATE TABLE IF NOT EXISTS ReviewerRequests ("
	            + "userName VARCHAR(255) PRIMARY KEY, "
	            + "status VARCHAR(20) NOT NULL)";
	    statement.execute(reviewerRequests);
	    
	 // Create the messages table
	    String messages = "CREATE TABLE IF NOT EXISTS Messages ("
	            + "sender VARCHAR(255),"
	    		+ "recipient VARCHAR(255),"
	    		+ "content VARCHAR(255) NOT NULL)";
	    statement.execute(messages);
	}
	

	/**
     * Checks if the database is empty
     * @throws SQLException if a database access error occurs or the SQL statement is invalid.
     * @return whether or not the database is empty
     */
	public boolean isDatabaseEmpty() throws SQLException {
		String query = "SELECT COUNT(*) AS count FROM cse360users";
		ResultSet resultSet = statement.executeQuery(query);
		if (resultSet.next()) {
			return resultSet.getInt("count") == 0;
		}
		return true;
	}
	
	/**
     * Creates a list of all the users in the database
     * @return A list of all the user in database
     */
	public List<User> getAllUsers() {
	    List<User> users = new ArrayList<>();
	    String query = "SELECT userName, role, name, email FROM cse360users";
	    
	    try (PreparedStatement pstmt = connection.prepareStatement(query);
		         ResultSet rs = pstmt.executeQuery()) {
		        while (rs.next()) {
		            String userName = rs.getString("userName");
		            String role = rs.getString("role");
		            String name = rs.getString("name");
		            String email = rs.getString("email");
		            users.add(new User(userName, "", role, email, name));  // Password is not needed
		        }
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }
		    return users;
	}
	

	/**
     * Adds a user to the database
     * @param user The user who is being added
     * @throws SQLException if a database access error occurs or the SQL statement is invalid.
     */
	public void register(User user) throws SQLException {
		String insertUser = "INSERT INTO cse360users (userName, password, role, email, name, tempPass) VALUES (?, ?, ?, ?, ?, false)";
		try (PreparedStatement pstmt = connection.prepareStatement(insertUser)) {
			pstmt.setString(1, user.getUserName());
			pstmt.setString(2, user.getPassword());
			pstmt.setString(3, user.getRole());
			pstmt.setString(4, user.getEmail());
			pstmt.setString(5, user.getName());
			pstmt.executeUpdate();
		}
	}
	
	/**
     * Updates role of user in the database
     * @param userName The username of the user who's roles are updated
     * @param newRole The new set of roles the user will have
     */
	public void updateUserRole(String userName, String newRole) {
	    String query = "UPDATE cse360users SET role = ? WHERE userName = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, newRole);
	        pstmt.setString(2, userName);
	        pstmt.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	/**
     * Removes a user to the database     
     * @param userName The user who is being removed
     */
	public void removeUser(String userName) {
	    String query = "DELETE FROM cse360users WHERE userName = ?;";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, userName);
	        pstmt.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	/**
     * Give user a new temporary password   
     * @param userName The username of the person getting a temporary password
     * @param password The temp password
     */
	public void updateUserTempPass(String userName, String password) {
	    String query = "UPDATE cse360users SET password = ?, tempPass = 'true' WHERE userName = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, password);
	        pstmt.setString(2, userName);
	        pstmt.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	/**
     * Give user a new password after a temporary password
     * @param userName The username of the person setting new password
     * @param password The new password
     */
	public void updateUserPass(String userName, String password) {
	    String query = "UPDATE cse360users SET password = ?, tempPass = 'false' WHERE userName = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, password);
	        pstmt.setString(2, userName);
	        pstmt.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	/**
     * Checks if temporary password used is valid
     * @param userName The userName of the temporary password user
     * @throws SQLException if a database access error occurs or the SQL statement is invalid.
     * @return If temporary password used is valid returns true, otherwise false
     */
	public boolean tempPass(String userName) throws SQLException {
		String query = "SELECT tempPass FROM cse360users WHERE userName = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, userName);
	        ResultSet rs = pstmt.executeQuery();
	        
	        if (rs.next()) {
	            String temp = rs.getString("tempPass"); // Return the tempPass
	            if(temp.equals("true")) { 				//if temp is true return true
	            	return true;
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
		return false;
	}

	/**
     * Checks if login for user is in the database
     * @param user The user class of the login
     * @throws SQLException if a database access error occurs or the SQL statement is invalid.
     * @return Whether the login for a user worked 
     */
	public boolean login(User user) throws SQLException {
		String query = "SELECT * FROM cse360users WHERE userName = ? AND password = ? AND role = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setString(1, user.getUserName());
			pstmt.setString(2, user.getPassword());
			pstmt.setString(3, user.getRole());
			try (ResultSet rs = pstmt.executeQuery()) {
				return rs.next();
			}
		}
	}
	
	/**
     * Checks if username is already in database
     * @param userName The username to check if in database
     * @return Whether or not the user is in the database
     */
	public boolean doesUserExist(String userName) {
	    String query = "SELECT COUNT(*) FROM cse360users WHERE userName = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        
	        pstmt.setString(1, userName);
	        ResultSet rs = pstmt.executeQuery();
	        
	        if (rs.next()) {
	            // If the count is greater than 0, the user exists
	            return rs.getInt(1) > 0;
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return false; // If an error occurs, assume user doesn't exist
	}
	
	/**
     * 	Retrieves the role of a user from the database
     * @param userName The username to get roles from
     * @return The roles of the user
     */
	public String getUserRole(String userName) {
	    String query = "SELECT role FROM cse360users WHERE userName = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, userName);
	        ResultSet rs = pstmt.executeQuery();
	        
	        if (rs.next()) {
	            return rs.getString("role"); // Return the role if user exists
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return null; // If no user exists or an error occurs
	}
	
	/**
     * 	Creates an invite code
     * @param expiryDate Date for when the code should expire
     * @param selectedRole Roles for the invite code to give user
     * @return The new invite code
     */
	public String generateInvitationCode(LocalDateTime expiryDate, String selectedRole) {
		 	String code = UUID.randomUUID().toString().substring(0, 4); // Generate a random 4-character code
		    String query = "INSERT INTO InvitationCodes (code, expiry, role, isUsed) VALUES (?, ?, ?, FALSE)";
		    System.out.println(query);
		    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
		        pstmt.setString(1, code);
		        pstmt.setTimestamp(2, Timestamp.valueOf(expiryDate));
		        pstmt.setString(3, selectedRole);
		        pstmt.executeUpdate();
		        System.out.println("Invitation code generated: " + code);
		        return code;
		    } catch (SQLException e) {
		        e.printStackTrace();
		        return null;
		    }	  
		}
	

	/**
     * 	Validates an invitation code to check if it is unused.
     * @param code The code to check
     * @return Whether the invite code used is valid
     */
	public boolean validateInvitationCode(String code) {
	    String query = "SELECT * FROM InvitationCodes WHERE code = ? AND isUsed = FALSE";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, code);
	        ResultSet rs = pstmt.executeQuery();
	        if(rs.next()) {
	        	LocalDateTime expiryDate = rs.getTimestamp("expiry").toLocalDateTime();
	        	 if (LocalDateTime.now().isBefore(expiryDate)) {
	 	            // Mark the code as used
	 	            markInvitationCodeAsUsed(code);
	 	            return true;
	 	        }else {
	 	        	return false;
	 	        }
	        }
	       
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return false;
	}
	
	/**
     * Gets the role associated with the invite code
     * @param code The code to get role from
     * @return The roles associated with a specific invite code
     */
	public String getRoleFromInvitation(String code) {
	    String query = "SELECT role, expiry FROM InvitationCodes WHERE code = ? AND isUsed = FALSE";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, code);
	        ResultSet rs = pstmt.executeQuery();
	        System.out.println("Before");
	        if (rs.next()) {
	        	System.out.println("After");
	            LocalDateTime expiryDate = rs.getTimestamp("expiry").toLocalDateTime();
	            if (LocalDateTime.now().isBefore(expiryDate)) {
	                return rs.getString("role"); // Return the assigned role
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return null;
	}
	
	/**
     * Marks the invitation code as used in the database.
     * @param code The code to set as used
     */
	private void markInvitationCodeAsUsed(String code) {
	    String query = "UPDATE InvitationCodes SET isUsed = TRUE WHERE code = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, code);
	        pstmt.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	

	/**
     * Check if the Questions table is empty
     * @throws SQLException if a database access error occurs or the SQL statement is invalid.
     * @return Whether or not the question table is empty
     */
	public boolean isQuestionEmpty() throws SQLException {
		String query = "SELECT COUNT(*) AS count FROM Questions";
		ResultSet resultSet = statement.executeQuery(query);
		if (resultSet.next()) {
			return resultSet.getInt("count") == 0;
		}
		return true;
	}
	
	/**
     * Get the next id number for questions and answers
     * @throws SQLException if a database access error occurs or the SQL statement is invalid.
     * @return the next available id for questions and answers
     */
	public int nextId() throws SQLException {
		if(isQuestionEmpty() && isAnswerEmpty()) {
			return 0;
		}
		int currID1 = 0;
		int currID2 = 0;
		String query = "SELECT MAX(identityQ) FROM Questions";
		ResultSet resultSet = statement.executeQuery(query);
		if (resultSet.next()) {
			currID1 = resultSet.getInt("MAX(identityQ)");
		}
		String query1 = "SELECT MAX(identityA) FROM Answers";
		ResultSet resultSet1 = statement.executeQuery(query1);
		if (resultSet1.next()) {
			currID2 = resultSet1.getInt("MAX(identityA)");
		}
		if(currID1 > currID2) {
			return currID1+1;
		}
		return currID2+1;
	}
	
	/**
     * Check if the Answers table is empty
     * @throws SQLException if a database access error occurs or the SQL statement is invalid.
     * @return Whether or not the answers table is empty
     */
	public boolean isAnswerEmpty() throws SQLException {
		String query = "SELECT COUNT(*) AS count FROM Answers";
		ResultSet resultSet = statement.executeQuery(query);
		if (resultSet.next()) {
			return resultSet.getInt("count") == 0;
		}
		return true;
	}

		
	/**
     * Puts a question in the Question table
     * @param q Question to be added to table
     * @throws SQLException if a database access error occurs or the SQL statement is invalid.
     */
	public void storeQuestion(Question q) throws SQLException {
		String storeQuestion = "INSERT INTO Questions (userName, title, text, identityQ) VALUES (?, ?, ?, ?)";
		try (PreparedStatement pstmt = connection.prepareStatement(storeQuestion)) {
			pstmt.setString(1, q.getUsername());
			pstmt.setString(2, q.getTitle());
			pstmt.setString(3, q.getText());
			pstmt.setInt(4, q.getIdentity());
			pstmt.executeUpdate();
		}catch (SQLException e) {
	        e.printStackTrace();
	        throw e; 
	    }
		
		String verifyQuery = "SELECT * FROM Questions WHERE identityQ = ?";
		try (PreparedStatement verifyStmt = connection.prepareStatement(verifyQuery)) {
		    verifyStmt.setInt(1, q.getIdentity());
		    ResultSet rs = verifyStmt.executeQuery();
		    if (rs.next()) {
		        System.out.println("Question successfully stored!");
		        // Optionally print the result to confirm the values
		        System.out.println("Stored Question: " + rs.getString("title") + " - " + rs.getString("text"));
		    } else {
		        System.out.println("Question was not stored.");
		    }
		} catch (SQLException e) {
		    e.printStackTrace();
		}
	}
			
			
	/**
     * Puts an answer in the Answer table
     * @param a Answer to be added to table
     * @throws SQLException if a database access error occurs or the SQL statement is invalid.
     */
	public void storeAnswer(Answer a) throws SQLException {
		String insertUser = "INSERT INTO Answers (userName, trueAnswer, text, questionID, identityA) VALUES (?, ?, ?, ?, ?)";
		try (PreparedStatement pstmt = connection.prepareStatement(insertUser)) {
			pstmt.setString(1, a.getUsername());
			pstmt.setBoolean(2, a.getAnswered());
			pstmt.setString(3, a.getText());
			pstmt.setInt(4, a.getQuestionID());
			pstmt.setInt(5, a.getIdentity());
			pstmt.executeUpdate();
		}catch (SQLException e) {
	        e.printStackTrace();
	        throw e; 
	    }
	}
	
	/**
     * Deletes a question and all associated answers in the Question table
     * @param q Question to be removed from table
     */
	public void removeQuestion(Question q) {
    String query = "DELETE FROM Questions WHERE identityQ = ?;";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, String.valueOf(q.getIdentity()));
	        pstmt.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    query = "DELETE FROM Answers WHERE questionID = ?;";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, String.valueOf(q.getIdentity()));
	        pstmt.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
		

	/**
     * Deletes an answer in the Answer table
     * @param a Answer to be removed from table
     */
	public void removeAnswer(Answer a) {
		String query = "DELETE FROM Answers WHERE identityA = ?;";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setInt(1, a.getIdentity());
	        pstmt.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

	/**
     * Edits a question in the Question table
     * @param identity ID of the question to be edited
     * @param text The new text body to replace the old one
     */
	public void editQuestion(int identity, String text) {
	   String query = "UPDATE Questions SET text = ? WHERE identityQ = ?;";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	    	pstmt.setString(1, text);
	        pstmt.setString(2, String.valueOf(identity));
	        pstmt.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
		
	/**
     * Edits an answer in the Answer table
     * @param identity ID of the answer to be edited
     * @param text The new text body to replace the old one
     */
	public void editAnswer(int identity, String text) {
		String query = "UPDATE Answers SET text = ? WHERE identityA = ?;";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	    	pstmt.setString(1, text);
	        pstmt.setInt(2, identity);
	        pstmt.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
			
	/**
     * Set an answer in the Answer table to the answer to the question
     * @param identity ID of the answer to be set as the answer
     */
	public void setAnswer(int identity) {
		String query = "UPDATE Answers SET trueAnswer = TRUE WHERE identityA = ?;";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setInt(1, identity);
	        pstmt.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	/**
     * Get a question from the database
     * @param id ID of the question to be retrieved
     * @return A question given the id
     */
	public Question getQuestion(int id) {
		String verifyQuery = "SELECT * FROM Questions WHERE identityQ = ?";
		System.out.println(id);
		try (PreparedStatement verifyStmt = connection.prepareStatement(verifyQuery)) {
		    verifyStmt.setInt(1, id);
		    System.out.println(id);
		    ResultSet rs = verifyStmt.executeQuery();
		    if (rs.next()) {
		    	String userName = rs.getString("userName");
                String title = rs.getString("title");
                String text = rs.getString("text");
                Question q = new Question(userName, title, text, id);
                System.out.println(q.viewQuestion());
                return q;
		    } else {
		        System.out.println("Question was not stored.");
		        return null;
		    }
		} catch (SQLException e) {
		    e.printStackTrace();
		    System.out.println("ERROR");
		}
	    return null;
	}
					
				
	/**
     * Get an answer from the database
     * @param id ID of the answer to be retrieved
     * @return The answer of the given id
     */
	public Answer getAnswer(int id) {
		String verifyQuery = "SELECT * FROM Answers WHERE identityA = ?";
		System.out.println(id);
		try (PreparedStatement verifyStmt = connection.prepareStatement(verifyQuery)) {
		    verifyStmt.setInt(1, id);
		    System.out.println(id);
		    ResultSet rs = verifyStmt.executeQuery();
		    if (rs.next()) {
		    	String userName = rs.getString("userName");
		    	boolean trueAnswer = rs.getBoolean("trueAnswer");
                String text = rs.getString("text");
                int questionID = rs.getInt("questionID");
                Answer a = new Answer(userName, text, questionID, id, trueAnswer);
                System.out.println(a.viewAnswer());
                return a;
		    } else {
		        System.out.println("Answer was not stored.");
		        return null;
		    }
		} catch (SQLException e) {
		    e.printStackTrace();
		    System.out.println("ERROR");
		}
	    return null;
	}
	
	/**
     * Get the subset of answers responding to a question or answer
     * @param id ID of the question/answer to get the subset of
     * @return The Answers that respond to that id
     */
	public AnswerSubset getRelated(int id) {
		AnswerSubset answers = new AnswerSubset();
		String verifyQuery = "SELECT * FROM Answers WHERE questionID = ?";
		try (PreparedStatement verifyStmt = connection.prepareStatement(verifyQuery)) {
		    verifyStmt.setInt(1, id);
		    ResultSet rs = verifyStmt.executeQuery();
		    while (rs.next()) {
		    	String userName = rs.getString("userName");
		    	boolean trueAnswer = rs.getBoolean("trueAnswer");
                String text = rs.getString("text");
                int newID = rs.getInt("identityA");
                Answer a = new Answer(userName, text, id, newID, trueAnswer);
                answers.addAnswer(a);
		    }
		    return answers;
		} catch (SQLException e) {
		    e.printStackTrace();
		    System.out.println("ERROR");
		}
		return answers;
	}
	
	/**
     * Gets the subset of questions that have some specific text in them
     * @param searchText The specific text to search for
     * @throws SQLException if a database access error occurs or the SQL statement is invalid.
     * @return The questions that have the specified text in them
     */
	public List<Question> searchQuestions(String searchText) throws SQLException {
	    List<Question> questions = new ArrayList<>();
	    String query = "SELECT * FROM Questions WHERE title LIKE ? OR text LIKE ?";
	    PreparedStatement pstmt = connection.prepareStatement(query);
	    pstmt.setString(1, "%" + searchText + "%");
	    pstmt.setString(2, "%" + searchText + "%");
	    ResultSet rs = pstmt.executeQuery();
	    while(rs.next()){
	        int id = rs.getInt("identityQ");
	        String userName = rs.getString("userName");
	        String title = rs.getString("title");
	        String text = rs.getString("text");
	        questions.add(new Question(userName, title, text, id));
	    }
	    return questions;
	}
	
	/**
     * Get subset of answers for question
     * @param questionID Id of the question to get the subset of
     * @throws SQLException if a database access error occurs or the SQL statement is invalid.
     * @return The answers for a specific question 
     */
	public List<Answer> getAnswersForQuestion(int questionID) throws SQLException {
	    List<Answer> answers = new ArrayList<>();
	    String query = "SELECT * FROM Answers WHERE questionID = ?";
	    PreparedStatement pstmt = connection.prepareStatement(query);
	    pstmt.setInt(1, questionID);
	    ResultSet rs = pstmt.executeQuery();
	    while(rs.next()){
	        int id = rs.getInt("identityA");
	        String userName = rs.getString("userName");
	        boolean trueAnswer = rs.getBoolean("trueAnswer");
	        String text = rs.getString("text");
	        answers.add(new Answer(userName, text, questionID, id, trueAnswer));
	    }
	    return answers;
	}

	/**
     * Get the next id for Review
     * @throws SQLException if a database access error occurs or the SQL statement is invalid.
     * @return The id for a review
     */
	public int nextIdReview() throws SQLException {
		if(isReviewEmpty()) {
			return 0;
		}
		int currID1 = 0;
		String query = "SELECT MAX(identityA) FROM Reviews";
		ResultSet resultSet = statement.executeQuery(query);
		if (resultSet.next()) {
			currID1 = resultSet.getInt("MAX(identityA)");
		}
		return currID1+1;
	}
	
	/**
     * Check if the Review table is empty
     * @throws SQLException if a database access error occurs or the SQL statement is invalid.
     * @return Whether or not the review table is empty
     */
	public boolean isReviewEmpty() throws SQLException {
		String query = "SELECT COUNT(*) AS count FROM Reviews";
		ResultSet resultSet = statement.executeQuery(query);
		if (resultSet.next()) {
			return resultSet.getInt("count") == 0;
		}
		return true;
	}
	
	/**
     * Stores a Review in the database.
     * @param a Review to store in the database
     * @throws SQLException if a database access error occurs or the SQL statement is invalid.
     */
	public void storeReview(Review a) throws SQLException {
		String insertUser = "INSERT INTO Reviews (userName, text, questionID, identityA) VALUES (?, ?, ?, ?)";
		try (PreparedStatement pstmt = connection.prepareStatement(insertUser)) {
			pstmt.setString(1, a.getUsername());
			pstmt.setString(2, a.getText());
			pstmt.setInt(3, a.getQuestionID());
			pstmt.setInt(4, a.getIdentity());
			pstmt.executeUpdate();
		}catch (SQLException e) {
	        e.printStackTrace();
	        throw e; 
	    }
	}	
	
	/**
     * Gets the Review in the database.
     * @param id ID of the Review to be retrieved
     * @return The review with the specified id
     */
	public Review getReview(int id) {
		String verifyQuery = "SELECT * FROM Reviews WHERE identityA = ?";
		System.out.println(id);
		try (PreparedStatement verifyStmt = connection.prepareStatement(verifyQuery)) {
		    verifyStmt.setInt(1, id);
		    System.out.println(id);
		    ResultSet rs = verifyStmt.executeQuery();
		    if (rs.next()) {
		    	String userName = rs.getString("userName");
                String text = rs.getString("text");
                int questionID = rs.getInt("questionID");
                Review a = new Review(userName, text, questionID, id);
                System.out.println(a.viewReview());
                return a;
		    } else {
		        System.out.println("Review was not stored.");
		        return null;
		    }
		} catch (SQLException e) {
		    e.printStackTrace();
		    System.out.println("ERROR");
		}
	    return null;
	}
	
	/**
     * Edits a Review's text in the database.
     * @param identity ID of the Review to be edited
     * @param text The new text to replace the text of the Review
     */
	public void editReview(int identity, String text) {
		String query = "UPDATE Reviews SET text = ? WHERE identityA = ?;";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	    	pstmt.setString(1, text);
	        pstmt.setInt(2, identity);
	        pstmt.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
		
	/**
     * Deletes a Review in the database.
     * @param a The review to be deleted
     */
	public void removeReview(Review a) {
		String query = "DELETE FROM Reviews WHERE identityA = ?;";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setInt(1, a.getIdentity());
	        pstmt.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	/**
     * Store a request to be Reviewer.
     * @param request The request being added
     */
	public void storeReviewerRequest(ReviewerRequest request) {
	    String checkQuery = "SELECT COUNT(*) FROM ReviewerRequests WHERE userName = ?";
	    String insertQuery = "INSERT INTO ReviewerRequests (userName, status) VALUES (?, ?)";
		    try (PreparedStatement checkStmt = connection.prepareStatement(checkQuery)) {
	        checkStmt.setString(1, request.getStudentUsername());
	        ResultSet rs = checkStmt.executeQuery();
	        if (rs.next() && rs.getInt(1) > 0) {
	            return; // Request already exists
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
		    try (PreparedStatement pstmt = connection.prepareStatement(insertQuery)) {
	        pstmt.setString(1, request.getStudentUsername());
	        pstmt.setString(2, request.getStatus());
	        pstmt.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}


	/**
     * Get all pending reviewer requests
     * @return The list of pending reviewer requests
     */
	public List<ReviewerRequest> getPendingReviewerRequests() {
	    List<ReviewerRequest> pending = new ArrayList<>();
	    String query = "SELECT * FROM ReviewerRequests WHERE status = 'Pending'";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        ResultSet rs = pstmt.executeQuery();
	        while (rs.next()) {
	            String username = rs.getString("userName");
	            String status = rs.getString("status");
	            ReviewerRequest req = new ReviewerRequest(username);
	            req.setStatus(status);
	            pending.add(req);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return pending;
	}

	/**
     * Update the request to be a reviewer
     * @param studentUsername The username of the request being updated
     * @param newStatus The change if it is allowed or denied
     */
	public void updateReviewerRequestStatus(String studentUsername, String newStatus) {
	    String updateQuery = "UPDATE ReviewerRequests SET status = ? WHERE userName = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(updateQuery)) {
	        pstmt.setString(1, newStatus);
	        pstmt.setString(2, studentUsername);
	        pstmt.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	

	/**
     * Get all questions from a user
     * @param username The username of the person we want all questions from
     * @return The list of questions from the user
     */
	public List<Question> getQuestionsByUser(String username) {
	    List<Question> questions = new ArrayList<>();
	    String query = "SELECT * FROM Questions WHERE userName = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, username);
	        ResultSet rs = pstmt.executeQuery();
	        while (rs.next()) {
	            String title = rs.getString("title");
	            String text = rs.getString("text");
	            int id = rs.getInt("identityQ");
	            questions.add(new Question(username, title, text, id));
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return questions;
	}
	

	/**
     * Get all answers from a user
     * @param username The username of the person we want all answers from
     * @return The list of answers from the user
     */
	public List<Answer> getAnswersByUser(String username) {
	    List<Answer> answers = new ArrayList<>();
	    String query = "SELECT * FROM Answers WHERE userName = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, username);
	        ResultSet rs = pstmt.executeQuery();
	        while (rs.next()) {
	            boolean trueAnswer = rs.getBoolean("trueAnswer");
	            String text = rs.getString("text");
	            int questionID = rs.getInt("questionID");
	            int id = rs.getInt("identityA");
	            answers.add(new Answer(username, text, questionID, id, trueAnswer));
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return answers;
	}
	
	/**
     * Get all Reviews for an id
     * @param answerId The id of the question or answer to get the reviews for
     * @throws SQLException if a database access error occurs or the SQL statement is invalid.
     * @return The list of reviews for an answer
     */
	public List<Review> getReviewsForAnswer(int answerId) throws SQLException {
	    List<Review> reviews = new ArrayList<>();
	    String query = "SELECT * FROM Reviews WHERE questionID = ?";
	    PreparedStatement pstmt = connection.prepareStatement(query);
	    pstmt.setInt(1, answerId);
	    ResultSet rs = pstmt.executeQuery();
	    while(rs.next()){
	        int reviewId = rs.getInt("identityA");
	        String userName = rs.getString("userName");
	        String text = rs.getString("text");
	        reviews.add(new Review(userName, text, answerId, reviewId));
	    }
	    return reviews;
	}
	
	/**
     * Get all Reviews for a Username
     * @param username The username of the person we want reviews from
     * @throws SQLException if a database access error occurs or the SQL statement is invalid.
     * @return The list of reviews for a username
     */
	public List<Review> getReviewsForUsername(String username) throws SQLException {
	    List<Review> reviews = new ArrayList<>();
	    String query = "SELECT * FROM Reviews WHERE userName = ?";
	    PreparedStatement pstmt = connection.prepareStatement(query);
	    pstmt.setString(1, username);
	    ResultSet rs = pstmt.executeQuery();
	    while(rs.next()){
	        int reviewId = rs.getInt("identityA");
	        int answerId = rs.getInt("questionID");
	        String text = rs.getString("text");
	        reviews.add(new Review(username, text, answerId, reviewId));
	    }
	    return reviews;
	}
	
	/**
     * Stores a Message in the database.
     * @param a Message to store in the database
     * @throws SQLException if a database access error occurs or the SQL statement is invalid.
     */
	public void storeMessage(Message a) throws SQLException {
		String insertUser = "INSERT INTO Messages (sender, recipient, content) VALUES (?, ?, ?)";
		try (PreparedStatement pstmt = connection.prepareStatement(insertUser)) {
			pstmt.setString(1, a.getSender());
			pstmt.setString(2, a.getRecipient());
			pstmt.setString(3, a.getContent());
			pstmt.executeUpdate();
		}catch (SQLException e) {
	        e.printStackTrace();
	        throw e; 
	    }
	}
	
	/**
     * Gets all messages between two users
     * @param senderUser Username of user 1
     * @param recipientUser Username of user 2
     * @throws SQLException if a database access error occurs or the SQL statement is invalid.
     * @return The list of messages between two users
     */
	public List<Message> getMessagesBetween(String senderUser, String recipientUser) throws SQLException {
        List<Message> conversation = new ArrayList<>();
        String query = "SELECT * FROM Messages WHERE sender = ? AND recipient = ?";
	    PreparedStatement pstmt = connection.prepareStatement(query);
	    pstmt.setString(1, senderUser);
	    pstmt.setString(2, recipientUser);
	    ResultSet rs = pstmt.executeQuery();
	    while(rs.next()){
	        String text = rs.getString("content");
	        conversation.add(new Message(senderUser, recipientUser, text));
	    }
	    query = "SELECT * FROM Messages WHERE sender = ? AND recipient = ?";
	    pstmt = connection.prepareStatement(query);
	    pstmt.setString(2, senderUser);
	    pstmt.setString(1, recipientUser);
	    rs = pstmt.executeQuery();
	    while(rs.next()){
	        String text = rs.getString("content");
	        conversation.add(new Message(recipientUser, senderUser, text));
	    }
        return conversation;
    }
	
	/**
     * Disconnects from the database
     */
	public void closeConnection() {
		try{ 
			if(statement!=null) statement.close(); 
		} catch(SQLException se2) { 
			se2.printStackTrace();
		} 
		try { 
			if(connection!=null) connection.close(); 
		} catch(SQLException se){ 
			se.printStackTrace(); 
		} 
	}


}
