package application;

import static org.junit.jupiter.api.Assertions.*;

import databasePart1.DatabaseHelper;

import java.sql.SQLException;
import java.util.List;

import org.junit.jupiter.api.Test;

/*******
 * <p> Title: QuestionAnswerTest. </p>
 * 
 * <p> Description: A list of test involving the question and answer class </p>
 */
class QuestionAnswerTest {
	private static final DatabaseHelper databaseHelper = new DatabaseHelper();
	
	/**
	 * Tests that checking for a valid title returns as valid
	 * @throws SQLException if a database access error occurs or the SQL statement is invalid.
	 */
	@Test
	void questionInputTitle() {
		String ans = QATextChecker.checkForValidTitleText("Need Help With JUnit");
		assertTrue(ans.equals("Valid"));
	}
	
	/**
	 * Tests that checking for a title that is to short returns the error that the title is to short
	 * @throws SQLException if a database access error occurs or the SQL statement is invalid.
	 */
	@Test
	void questionInputTitleFalseShort() {
		String ans = QATextChecker.checkForValidTitleText("JU");
		assertTrue(ans.equals("The title must be at least 5 characters long"));
	}
	
	/**
	 * Tests that checking for a title that is to long returns the error that the title is to long
	 * @throws SQLException if a database access error occurs or the SQL statement is invalid.
	 */
	@Test
	void questionInputTitleFalseLong() {
		String ans = QATextChecker.checkForValidTitleText("HELLOHELLOHELLOHELLOHELLOHELLOHELLOHELLOHELLOHELLOHELLOHELLOHELLOHELLOHELLOHELLOHELLOHELLOHELLOHELLOHELLOHELLO");
		assertTrue(ans.equals("The title must be no more than 100 characters long"));
	}
	
	/**
	 * Tests that checking for a valid main body returns as valid
	 * @throws SQLException if a database access error occurs or the SQL statement is invalid.
	 */
	@Test
	void questionInputBody() {
		String ans = QATextChecker.checkForValidBodyText("I haven't been able to get my JUnit to work on my computer");
		assertTrue(ans.equals("Valid"));
	}
	
	/**
	 * Tests that checking for a main body that is to short returns the error that the main body is to short
	 * @throws SQLException if a database access error occurs or the SQL statement is invalid.
	 */
	@Test
	void questionInputBodyFalseShort() {
		String ans = QATextChecker.checkForValidBodyText("JU");
		assertTrue(ans.equals("The Body must be at least 20 characters long"));
	}
	
	/**
	 * Tests that checking for a main body that is only has special characters returns the error that the body must have non-special characters
	 * @throws SQLException if a database access error occurs or the SQL statement is invalid.
	 */
	@Test
	void questionInputBodyFalseLetters() {
		String ans = QATextChecker.checkForValidBodyText("                                                                        ");
		assertTrue(ans.equals("The Body must have some non-special character"));
	}
	
	/**
	 * Tests that creating a question works
	 * @throws SQLException if a database access error occurs or the SQL statement is invalid.
	 */
	@Test
	void createQuestion() throws SQLException {
		databaseHelper.connectToDatabase();
		Question q = new Question("AustKears", "Need Help With JUnit", "I haven't been able to get my JUnit to work on my computer");
		databaseHelper.storeQuestion(q);
		q = new Question("AustKears", "Need Help With JUnit", "I haven't been able to get my JUnit to work on my computer");
		databaseHelper.storeQuestion(q);
		q = new Question("AustKears", "Need Help With JUnit", "I haven't been able to get my JUnit to work on my computer");
		databaseHelper.storeQuestion(q);
		q = new Question("AustKears", "Need Help With JUnit", "I haven't been able to get my JUnit to work on my computer");
		databaseHelper.storeQuestion(q);
		q = new Question("AustKears", "Need Help With JUnit", "I haven't been able to get my JUnit to work on my computer");
		databaseHelper.storeQuestion(q);
		assertNotNull(q);
	}
	
	/**
	 * Tests that viewing a question returns as expected
	 * @throws SQLException if a database access error occurs or the SQL statement is invalid.
	 */
	@Test
	void viewQuestion() throws SQLException {
		databaseHelper.connectToDatabase();
		Question q = databaseHelper.getQuestion(1);
		String view = q.viewQuestion();
		System.out.print(view);
		assertTrue(view.equals("Need Help With JUnit #" + q.getIdentity() + "\nBy: AustKears\nI haven't been able to get my JUnit to work on my computer\n"));
	}
	
	/**
	 * Tests that editing a question edits the main body of the question
	 * @throws SQLException if a database access error occurs or the SQL statement is invalid.
	 */
	@Test
	void editQuestion() throws SQLException {
		databaseHelper.connectToDatabase();
		Question q = databaseHelper.getQuestion(2);
		q.editQuestion("It is working now!");
		String view = q.viewQuestion();
		assertTrue(view.equals("Need Help With JUnit #" + q.getIdentity() + "\nBy: AustKears\nIt is working now!\n"));
	}
	
	/**
	 * Tests that deleting a question removes it from the database
	 * @throws SQLException if a database access error occurs or the SQL statement is invalid.
	 */
	@Test
	void deleteQuestion() throws SQLException {
		databaseHelper.connectToDatabase();
		Question q = new Question("AustKears", "Need Help With JUnit", "I haven't been able to get my JUnit to work on my computer", 0);
		databaseHelper.removeQuestion(q);
		databaseHelper.storeQuestion(q);
		databaseHelper.removeQuestion(q);
		q = databaseHelper.getQuestion(0);
		assertNull(q);
	}
	
	/**
	 * Tests that creating an answer works
	 * @throws SQLException if a database access error occurs or the SQL statement is invalid.
	 */
	@Test
	void createAnswer() throws SQLException {
		databaseHelper.connectToDatabase();
		Answer a = new Answer("AustKears", "I haven't been able to get my JUnit to work on my computer", 1);
		databaseHelper.storeAnswer(a);
		a = new Answer("AustKears", "I haven't been able to get my JUnit to work on my computer", 1);
		databaseHelper.storeAnswer(a);
		a = new Answer("AustKears", "I haven't been able to get my JUnit to work on my computer", 1);
		databaseHelper.storeAnswer(a);
		a = new Answer("AustKears", "I haven't been able to get my JUnit to work on my computer", 1);
		databaseHelper.storeAnswer(a);
		assertNotNull(a);
	}
	
	/**
	 * Tests that viewing an answer returns as expected
	 * @throws SQLException if a database access error occurs or the SQL statement is invalid.
	 */
	@Test
	void viewAnswer() throws SQLException {
		databaseHelper.connectToDatabase();
		Answer a = databaseHelper.getAnswer(6);
		String view = a.viewAnswer();
		System.out.println(view + "THIS ONE");
		assertTrue(view.equals("#" + a.getIdentity() + "\nBy: AustKears\nI haven't been able to get my JUnit to work on my computer\n"));
	}
	
	/**
	 * Tests that editing an answer changes the main body of the answer
	 * @throws SQLException if a database access error occurs or the SQL statement is invalid.
	 */
	@Test
	void editAnswer() throws SQLException {
		databaseHelper.connectToDatabase();
		Answer a = databaseHelper.getAnswer(5);
		a.editAnswer("It is working now!");
		String view = a.viewAnswer();
		assertTrue(view.equals("#" + a.getIdentity() + "\nBy: AustKears\nIt is working now!\n"));
	}
	
	/**
	 * Tests that deleting an answer removes it from the database
	 * @throws SQLException if a database access error occurs or the SQL statement is invalid.
	 */
	@Test
	void deleteAnswer() throws SQLException {
		databaseHelper.connectToDatabase();
		Answer a = new Answer("AustKears", "I haven't been able to get my JUnit to work on my computer", 0, 0, false);
		databaseHelper.removeAnswer(a);
		databaseHelper.storeAnswer(a);
		databaseHelper.removeAnswer(a);
		a = databaseHelper.getAnswer(0);
		assertNull(a);
	}
	
	/**
	 * This is a test that runs the constructor for the class QuestionList.
	 * After running it asserts that the constructor created an QuestionList instance.
	 * @throws SQLException if a database access error occurs or the SQL statement is invalid.
	 */
	@Test
	void createQuestionList() throws SQLException {
		QuestionSubset q = new QuestionSubset();
		assertNotNull(q);
	}
	
	/**
	 * Tests that adding a question to an QuestionList adds that question to the list.
	 * @throws SQLException if a database access error occurs or the SQL statement is invalid.
	 */
	@Test
	void addQuestionList() throws SQLException {
		QuestionSubset q = new QuestionSubset();
		Question q1 = new Question("AustKears", "Need Help With JUnit", "I haven't been able to get my JUnit to work on my computer");
		Question q2 = new Question("AustKears", "Need Help With JUnit5", "I haven't been able to get my JUnit to work on my computer5");
		q.addQuestion(q1);
		q.addQuestion(q2);
		assertFalse(q.readAll().equals(""));
	}
	
	/**
	 * Tests that removing a question to an QuestionList removes that question from the list.
	 * @throws SQLException if a database access error occurs or the SQL statement is invalid.
	 */
	@Test
	void removeQuestionList() throws SQLException {
		QuestionSubset q = new QuestionSubset();
		Question q1 = new Question("AustKears", "Need Help With JUnit", "I haven't been able to get my JUnit to work on my computer");
		Question q2 = new Question("AustKears", "Need Help With JUnit5", "I haven't been able to get my JUnit to work on my computer5");
		q.addQuestion(q1);
		q.addQuestion(q2);
		q.removeQuestion(q2);
		q.removeQuestion(q1);
		assertTrue(q.readAll().equals(""));
	}
	
	/**
	 * Tests that reading a QuestionList gives a string that is formated as expected.
	 * @throws SQLException if a database access error occurs or the SQL statement is invalid.
	 */
	@Test
	void readQuestionList() throws SQLException {
		QuestionSubset q = new QuestionSubset();
		Question q1 = new Question("AustKears", "Need Help With JUnit", "I haven't been able to get my JUnit to work on my computer", 0);
		Question q2 = new Question("AustKears", "Need Help With JUnit5", "I haven't been able to get my JUnit to work on my computer5", 1);
		q.addQuestion(q1);
		q.addQuestion(q2);
		System.out.println("Thing: " +q.readAll());
		System.out.println();
		assertTrue(q.readAll().equals("#0 Need Help With JUnit\n"
				+ "#1 Need Help With JUnit5\n"));
	}
	
	/**
	 * Tests that deleting a QuestionList deletes everything inside the QuestionList.
	 * @throws SQLException if a database access error occurs or the SQL statement is invalid.
	 */
	@Test
	void deleteQuestionList() throws SQLException {
		QuestionSubset q = new QuestionSubset();
		Question q1 = new Question("AustKears", "Need Help With JUnit", "I haven't been able to get my JUnit to work on my computer");
		Question q2 = new Question("AustKears", "Need Help With JUnit5", "I haven't been able to get my JUnit to work on my computer5");
		q.addQuestion(q1);
		q.addQuestion(q2);
		q.deleteAll();
		assertTrue(q.readAll().equals(""));
	}
	
	/**
	 * This is a test that runs the constructor for the class AnswerSubset.
	 * After running it asserts that the constructor created an AnswerSubset instance.
	 * @throws SQLException if a database access error occurs or the SQL statement is invalid.
	 */
	@Test
	public void createAnswerList() throws SQLException {
		AnswerSubset q = new AnswerSubset();
		assertNotNull(q);
	}
	
	/**
	 * Tests that adding an answer to an AnswerList adds that answer to the list.
	 * @throws SQLException if a database access error occurs or the SQL statement is invalid.
	 */
	@Test
	public void addAnswerList() throws SQLException {
		AnswerSubset q = new AnswerSubset();
		Answer a1 = new Answer("AustKears", "I haven't been able to get my JUnit to work on my computer", 1, 0, false);
		Answer a2 = new Answer("AustKears5", "I haven't been able to get my JUnit to work on my computer5", 2, 1, false);
		q.addAnswer(a1);
		q.addAnswer(a2);
		assertTrue(q.quickReadAll().equals("#0 I haven't been able to get my JUnit to work on my computer\n"
				+ "#1 I haven't been able to get my JUnit to work on my computer5\n"));
	}
	
	/**
	 * Tests that removing an answer from an AnswerList removes that answer and only that answer.
	 * @throws SQLException if a database access error occurs or the SQL statement is invalid.
	 */
	@Test
	public void removeAnswerList() throws SQLException {
		AnswerSubset q = new AnswerSubset();
		Answer a1 = new Answer("AustKears", "I haven't been able to get my JUnit to work on my computer", 1, 0, false);
		Answer a2 = new Answer("AustKears5", "I haven't been able to get my JUnit to work on my computer5", 2, 1, false);
		q.addAnswer(a1);
		q.addAnswer(a2);
		q.removeAnswer(a2);
		assertTrue(q.quickReadAll().equals("#0 I haven't been able to get my JUnit to work on my computer\n"));
	}
	
	/**
	 * Tests that reading an AnswerList gives a string that is formated as expected.
	 * @throws SQLException if a database access error occurs or the SQL statement is invalid.
	 */
	@Test
	public void readAnswerList() throws SQLException {
		databaseHelper.connectToDatabase();
		AnswerSubset q = new AnswerSubset();
		Answer a1 = new Answer("AustKears", "I haven't been able to get my JUnit to work on my computer too", 0, 2, false);
		Answer a2 = new Answer("AustKears", "I Got it to work!", 2, 3, true);
		Answer a3 = new Answer("AustKears5", "I haven't been able to get my JUnit to work on my computer5", 0, 5, false);
		q.addAnswer(a1);
		q.addAnswer(a3);
		databaseHelper.removeAnswer(a2);
		databaseHelper.storeAnswer(a2);
		assertTrue(q.readAll(3).equals("#2 By: AustKears Responding to 0\n"
				+ "I haven't been able to get my JUnit to work on my computer too\n"
				+ "#3 By: AustKears Responding to 2 ✓\n"
				+ "I Got it to work!\n"
				+ "#5 By: AustKears5 Responding to 0\n"
				+ "I haven't been able to get my JUnit to work on my computer5\n"));
		databaseHelper.removeAnswer(a2);
	}
	
	/**
	 * Tests that deleting an AnswerList deletes everything inside the AnswerList.
	 * @throws SQLException if a database access error occurs or the SQL statement is invalid.
	 */
	@Test
	public void deleteAnswerList() throws SQLException {
		AnswerSubset a = new AnswerSubset();
		Answer a1 = new Answer("AustKears", "I haven't been able to get my JUnit to work on my computer", 1);
		Answer a2 = new Answer("AustKears5", "I haven't been able to get my JUnit to work on my computer5", 2);
		a.addAnswer(a1);
		a.addAnswer(a2);
		a.deleteAll();
		assertTrue(a.readAll(5).equals(""));
	}
	
	/**
	 * Tests that searching for questions returns the questions it should.
	 * @throws SQLException if a database access error occurs or the SQL statement is invalid.
	 */
	@Test
	void searchQuestions() throws SQLException {
		databaseHelper.connectToDatabase();
		String view = "";
		Question q1 = new Question("AustKears",  "Working on Project", "I dont understand this part of the project", -1);
		databaseHelper.removeQuestion(q1);
		databaseHelper.storeQuestion(q1);
		List<Question> questions = databaseHelper.searchQuestions("project");
		databaseHelper.removeQuestion(q1);
		for (Question q : questions) {
            view = q.getTitle() + " #" + q.getIdentity();
        }
		assertTrue(view.equals("Working on Project #-1"));
	}
	
}
