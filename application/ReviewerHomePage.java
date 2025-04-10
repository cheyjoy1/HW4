package application;

import java.sql.SQLException;

import databasePart1.DatabaseHelper;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/*******
 * <p> Title: ReviewerHomePage class. </p>
 * 
 * <p> Description: A Page that welcomes Reviewers and allows them to act. </p>
 * 
 */
public class ReviewerHomePage {
	private static final DatabaseHelper databaseHelper = new DatabaseHelper();
	/**
     * Displays the Reviewer page in the provided primary stage.
     * @param primaryStage The primary stage where the scene will be displayed.
     * @param user The user accessing the page
     */
    public void show(Stage primaryStage, User user) {
    	try {
            databaseHelper.connectToDatabase(); // Connect to the database
        } catch (SQLException e) {
        	System.out.println(e.getMessage());
        }
    	VBox layout = new VBox();
	    layout.setStyle("-fx-alignment: center; -fx-padding: 20;");
	    
	    // Label to display Hello user
	    Label userLabel = new Label("Hello, Reviewer!");
	    userLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
	    
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
	    
	    Button reviewQuestion = new Button("Review a Comment");
	    reviewQuestion.setOnAction(e -> {
	    	new CreateReview().show(primaryStage, user);
	    
	    });
	    
	    Button editReview = new Button("Edit a Comment");
	    editReview.setOnAction(e -> {
	    	new EditReview().show(primaryStage, user);
	    
	    });
	    
	    Button deleteReview = new Button("Delete a Comment");
	    deleteReview.setOnAction(e -> {
	    	new DeleteReview().show(primaryStage, user);
	    
	    });
	    
	    Button searchQuestion = new Button("Search a Question");
	    searchQuestion.setOnAction(e -> {
	    	new SearchQuestions().show(primaryStage, user);
	    }); 
	    
	    Button viewReviews = new Button("View Your Reviews");
	    viewReviews.setOnAction(e -> {
	    	new ListReviews().show(primaryStage, user);
	    });
	    
	    Button getMessages = new Button("Get Messages From User");
	    getMessages.setOnAction(e -> {
	    	new GetPrivateMessages().show(primaryStage, user,  "Reviewer");
	    }); 
	    
	    Button sendMessages = new Button("Send Message to User");
	    sendMessages.setOnAction(e -> {
	    	new SendMessage().show(primaryStage, user, "Reviewer");
	    });
	    
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");
        
	    layout.getChildren().addAll(userLabel,logOut,switchRole, reviewQuestion, editReview, deleteReview, searchQuestion,viewReviews, sendMessages, getMessages);
	    Scene userScene = new Scene(layout, 800, 400);

	    // Set the scene to primary stage
	    primaryStage.setScene(userScene);
	    primaryStage.setTitle("Reviewer Page");
    	
    }
}