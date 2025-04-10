package application;

import java.sql.SQLException;

import databasePart1.DatabaseHelper;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/*******
 * <p> Title: StudentHomePage class. </p>
 * 
 * <p> Description: A Page that welcomes students and allows them to act. </p>
 * 
 */
public class StudentHomePage {
	private static final DatabaseHelper databaseHelper = new DatabaseHelper();
	
	/**
     * Displays the students page in the provided primary stage.
     * @param primaryStage The primary stage where the scene will be displayed.
     * @param user The user accessing the page
     */
    public void show(Stage primaryStage, User user) {
    	VBox layout = new VBox();
	    layout.setStyle("-fx-alignment: center; -fx-padding: 20;");
	    
	    // Label to display Hello user
	    Label userLabel = new Label("Hello, Student!");
	    userLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
	    
	    try {
            databaseHelper.connectToDatabase(); // Connect to the database
        } catch (SQLException e) {
        	System.out.println(e.getMessage());
        }

	  //Button to log out
	    Button logOut = new Button("Log Out");
	    logOut.setOnAction(a -> {
	        new SetupLoginSelectionPage(databaseHelper).show(primaryStage);
	        
	    });
	    //Button to switch role
	    Button switchRole = new Button("Switch Role");
	    switchRole.setOnAction(a -> {
	    	WelcomeLoginPage welcomeLoginPage = new WelcomeLoginPage(databaseHelper);
	    	welcomeLoginPage.show(primaryStage,user);
	    });
	    Button askQuestion = new Button("Ask Question");
	    askQuestion.setOnAction(e -> {
	    	new CreateQuestion().show(primaryStage, user);
	    
	    });
	    Button answerQuestion = new Button("Answer a Question");
	    answerQuestion.setOnAction(e -> {
	    	new CreateAnswer().show(primaryStage, user);
	    
	    });
	    Button searchQuestion = new Button("Search a Question");
	    searchQuestion.setOnAction(e -> {
	    	new SearchQuestions().show(primaryStage, user);
	    
	    }); 
	    Button editBody = new Button("Edit Text of Question/Answer");
	    editBody.setOnAction(e -> {
	    	new EditTextBody().show(primaryStage, user);
	    });
	    Button answered = new Button("Set Answer to Question");
	    answered.setOnAction(e -> {
	    	new setAnswered().show(primaryStage, user);
	    }); 
	    
	    Button requestReviewerRole = new Button("Request Reviewer Role");
	    requestReviewerRole.setOnAction(e -> {
	    	new RequestReviewerRolePage().show(primaryStage, user);
	    });
	    
	    Button rateReviewers = new Button("Rate Reviewers");
	    rateReviewers.setOnAction(e -> {
	    	new RateReviewersPage().show(primaryStage, user);
	    }); 
	    
	    Button getMessages = new Button("Get Messages From User");
	    getMessages.setOnAction(e -> {
	    	new GetPrivateMessages().show(primaryStage, user, "Student");
	    }); 
	    
	    Button sendMessages = new Button("Send Message to User");
	    sendMessages.setOnAction(e -> {
	    	new SendMessage().show(primaryStage, user, "Student");
	    });
	    
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");
        
        layout.getChildren().addAll(userLabel,logOut,switchRole, askQuestion, answerQuestion, searchQuestion, answered, editBody, requestReviewerRole, sendMessages, getMessages, rateReviewers);
	    Scene userScene = new Scene(layout, 800, 400);
	    // Set the scene to primary stage
	    primaryStage.setScene(userScene);
	    primaryStage.setTitle("Student Page");
    	
    }
}