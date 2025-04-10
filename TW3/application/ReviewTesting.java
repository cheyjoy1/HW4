package application;

import static org.junit.jupiter.api.Assertions.*;

import databasePart1.DatabaseHelper;

import java.sql.SQLException;
import java.util.List;

import org.junit.jupiter.api.Test;

/*******
 * <p> Title: ReviewTesting </p>
 * 
 * <p> Description: Runs a series of test to ensure program is working </p>
 * 
 */
class ReviewTesting {
	private static final DatabaseHelper databaseHelper = new DatabaseHelper();
	
	/**
	 * Tests that checking for a valid main body returns as valid
	 */
	@Test
	void reviewInputBody() {
		String ans = QATextChecker.checkForValidBodyText("You need to elaborate a little more on what the problem it is you're having");
		assertTrue(ans.equals("Valid"));
	}
	
	/**
	 * Tests that checking for a main body that is to short returns the error that the main body is to short
	 */
	@Test
	void reviewInputBodyFalseShort() {
		String ans = QATextChecker.checkForValidBodyText("JU");
		assertTrue(ans.equals("The Body must be at least 20 characters long"));
	}
	
	/**
	 * Tests that checking for a main body that is only has special characters returns the error that the body must have non-special characters
	 */
	@Test
	void reviewInputBodyFalseLetters() {
		String ans = QATextChecker.checkForValidBodyText("                                                                        ");
		assertTrue(ans.equals("The Body must have some non-special character"));
	}
	
	/**
     * Tests that a review can be created and stored in the database.
     * <p>
     * This test creates an Answer, stores it, then creates a Review associated with that Answer,
     * stores the Review, and finally asserts that the Review object is not null.
     * </p>
     *
     * @throws SQLException if a database access error occurs.
     */
  @Test
    void createReviewTest() throws SQLException {
        databaseHelper.connectToDatabase();
        // Create an answer so that a review can be associated with it.
        Answer answer = new Answer("TestUser", "Test answer for review", 1);
        databaseHelper.storeAnswer(answer);
        // Create and store a review for that answer.
        Review review = new Review("Reviewer", "This is a test review", answer.getIdentity());
        databaseHelper.storeReview(review);
        assertNotNull(review);
    }
  
  /**
     * Tests the display functionality of the Review class.
     * <p>
     * This test creates a Review (without storing it in the database) and verifies that the
     * output from the viewReview() method matches the expected format.
     * </p>
     *
     * @throws SQLException if a database access error occurs.
     */
  @Test
    void viewReviewTest() throws SQLException {
        // Create a review (not stored in the DB) and verify its display output.
        Review review = new Review("Viewer", "This is a review view test", 2);
        String expected = "Review: #" + review.getIdentity()  +" responding to #"+review.getQuestionID()+"\n" +
                          "By: " + review.getUsername() + "\n" +
                          review.getText() + "\n";
        assertTrue(review.viewReview().equals(expected));
    }
    
  /**
     * Tests that a stored review can be retrieved from the database.
     * <p>
     * This test creates an Answer, stores it, then creates and stores a Review associated with that Answer.
     * It then retrieves the review(s) for that Answer and asserts that the retrieved Review matches the expected values.
     * </p>
     *
     * @throws SQLException if a database access error occurs.
     */
    @Test
    void retrieveReviewTest() throws SQLException {
        databaseHelper.connectToDatabase();
        // Create an answer and store it.
        Answer answer = new Answer("TestUser", "Answer for review retrieval", 3);
        databaseHelper.storeAnswer(answer);
        // Create a review and store it.
        Review review = new Review("Reviewer", "Review retrieval test", answer.getIdentity());
        databaseHelper.storeReview(review);
        // Retrieve reviews for this answer.
        List<Review> reviews = databaseHelper.getReviewsForAnswer(answer.getIdentity());
        assertNotNull(reviews);
        assertEquals(1, reviews.size());
        Review retrieved = reviews.get(0);
        assertEquals("Reviewer", retrieved.getUsername());
        assertEquals("Review retrieval test", retrieved.getText());
        assertEquals(answer.getIdentity(), retrieved.getQuestionID());
    }
    
	
	/**
	 * Tests that editing an Review changes the main body of the Review
	 * @throws SQLException if a database access error occurs or the SQL statement is invalid.
	 */
	@Test
	void editReview() throws SQLException {
		databaseHelper.connectToDatabase();
		Review a = databaseHelper.getReview(1);
		a.editReview("Hey, thats a lot better now good job");
		String view = a.viewReview();
		assertTrue(view.equals("Review: #" + a.getIdentity() + " responding to #"+a.getQuestionID()+"\nBy: "+a.getUsername()+"\nHey, thats a lot better now good job\n"));
	}
	
	/**
	 * Tests that deleting a Review removes it from the database
	 * @throws SQLException if a database access error occurs or the SQL statement is invalid.
	 */
	@Test
	void deleteReview() throws SQLException {
		databaseHelper.connectToDatabase();
		Review a = new Review("AustKears", "I haven't been able to get my JUnit to work on my computer", 0, 0);
		databaseHelper.removeReview(a);
		databaseHelper.storeReview(a);
		databaseHelper.removeReview(a);
		a = databaseHelper.getReview(0);
		assertNull(a);
	}
	
	/**
	 * Tests that we can check if the Id of a review is in the system
	 * @throws SQLException if a database access error occurs or the SQL statement is invalid.
	 */
	@Test
	void checkReviewIDExist() throws SQLException {
		databaseHelper.connectToDatabase();
		String a = QATextChecker.checkForValidReviewID(1);
		assertTrue(a.equals("Valid"));
	}
	
	/**
	 * Tests that we can check if the Id of a review is not in the system
	 * @throws SQLException if a database access error occurs or the SQL statement is invalid.
	 */
	@Test
	void checkReviewIDDoesNotExist() throws SQLException {
		databaseHelper.connectToDatabase();
		String a = QATextChecker.checkForValidReviewID(-1);
		assertFalse(a.equals("Valid"));
	}
	
	/**
     * Tests that retrieving reviews for an answer with no reviews returns an empty list.
     * <p>
     * This test creates an Answer (without storing any associated Reviews), stores the Answer,
     * and then asserts that the list of Reviews retrieved for that Answer is empty.
     * </p>
     *
     * @throws SQLException if a database access error occurs.
     */
    @Test
    void noReviewsTest() throws SQLException {
        databaseHelper.connectToDatabase();
        // Create an answer without storing any reviews.
        Answer answer = new Answer("TestUser", "Answer with no reviews", 5);
        databaseHelper.storeAnswer(answer);
        List<Review> reviews = databaseHelper.getReviewsForAnswer(answer.getIdentity());
        assertTrue(reviews.isEmpty());
    }
    
    /**
     * Tests that creating and storing a ReviewerRequest creates and stores
     * @throws SQLException if a database access error occurs.
     */
    @Test
    void storeReviewerRequestTest() throws SQLException {
        databaseHelper.connectToDatabase();
        ReviewerRequest request = new ReviewerRequest("AustKears");
        databaseHelper.storeReviewerRequest(request);
        assertNotNull(request);
    }
    
    /**
     * Tests that getting the pending request returns with a list of request that are pending
     * @throws SQLException if a database access error occurs.
     */
    @Test
    void getPendingReviewerRequestTest() throws SQLException {
        databaseHelper.connectToDatabase();
        List<ReviewerRequest> listing= databaseHelper.getPendingReviewerRequests();
        ReviewerRequest request = listing.get(0);
        assertTrue(request.getStudentUsername().equals("AustKears"));
    }
    
    /**
     * Tests that updating the status to accepted updates the status of a request
     */
    @Test
    void updateReviewerRequestTest() {
    	ReviewerRequest request = new ReviewerRequest("AustKears");
    	request.setStatus("Approved");
        assertTrue(request.getStatus().equals("Approved"));
    }
    
    /**
     * Tests that we can get the list of questions from a user
     * @throws SQLException if a database access error occurs.
     */
    @Test
    void getQuestionsByUserTest() throws SQLException {
        databaseHelper.connectToDatabase();
        Question q = new Question("Nobody", "Need Help With JUnit", "I haven't been able to get my JUnit to work on my computer");
		databaseHelper.storeQuestion(q);
        List<Question> listing = databaseHelper.getQuestionsByUser("Nobody");
        Question question = listing.get(0);
        assertTrue(question.viewQuestion().equals("Need Help With JUnit #"+ question.getIdentity() +"\nBy: Nobody\nI haven't been able to get my JUnit to work on my computer\n"));
    }
    
    /**
     * Tests that we can get the list of answers from a user
     * @throws SQLException if a database access error occurs.
     */
    @Test
    void getAnswersByUserTest() throws SQLException {
        databaseHelper.connectToDatabase();
        Answer a = new Answer("Nobody", "I haven't been able to get my JUnit to work on my computer", 0);
		databaseHelper.storeAnswer(a);
        List<Answer> listing = databaseHelper.getAnswersByUser("Nobody");
        Answer answer = listing.get(0);
        assertTrue(answer.viewAnswer().equals("#"+ answer.getIdentity() +"\nBy: Nobody\nI haven't been able to get my JUnit to work on my computer\n"));
    }
    
    /**
     * Tests that creating and storing a Message works
     * @throws SQLException if a database access error occurs.
     */
    @Test
    void storeMessageTest() throws SQLException {
        databaseHelper.connectToDatabase();
        Message message = new Message("admin", "AustKears", "I was wondering why you wanted to send that");
        databaseHelper.storeMessage(message);
        assertNotNull(message);
    }
    
    /**
     * Tests that viewing a message returns the expected string
     */
    @Test
    void viewMessageTest(){
        Message message = new Message("admin", "AustKears", "I was wondering why you wanted to send that");
        String done= message.viewMessage();
        assertTrue(done.equals("From: admin To: AustKears\nI was wondering why you wanted to send that\n"));
    }
    
    /**
	 * Tests that we can send message between two people
	 * @throws SQLException if a database access error occurs or the SQL statement is invalid.
	 */
   private PrivateMessagingSystem messagingSystem;
    @Test
    void testSendMessage() throws SQLException {
    	messagingSystem = new PrivateMessagingSystem();
    	User student = new User("Alice", "password", "Student", "", "");
    	User reviewer = new User("Bob", "password", "Reviewer", "", "");
        messagingSystem.sendMessage(student, reviewer, "Hello, I have a question about my answer.");
        List<Message> messages = messagingSystem.getMessagesBetween(student, reviewer);
        assertEquals(1, messages.size());
        assertEquals("Hello, I have a question about my answer.", messages.get(0).getContent());
    }
    
    /**
	 * Tests that we get a messages between two people
     * @throws SQLException if a database access error occurs or the SQL statement is invalid.
	 */
    @Test
    void testRetrieveMessagesBetweenUsers() throws SQLException {
    	messagingSystem = new PrivateMessagingSystem();
    	User student = new User("Alice", "password", "Student", "", "");
    	User reviewer = new User("Bob", "password", "Reviewer", "", "");
        messagingSystem.sendMessage(student, reviewer, "Can you clarify your review?");
        messagingSystem.sendMessage(reviewer, student, "Sure! Your answer needs more details.");
        List<Message> messages = messagingSystem.getMessagesBetween(student, reviewer);
        assertEquals(2, messages.size());
        assertEquals("Can you clarify your review?", messages.get(0).getContent());
        assertEquals("Sure! Your answer needs more details.", messages.get(1).getContent());
    }
    
    private ReviewService reviewService;
    
    @Test
    void testReplyToMessage() {
    	reviewService = new ReviewService();
        reviewService.replyToMessage("admin", "Aust", "Thank you for your feedback.");
        List<Message> messages = reviewService.getMessagesForReviewer("Aust");
        assertFalse(messages.isEmpty());
    }
	
}
