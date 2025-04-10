package application;

import java.sql.SQLException;

import databasePart1.DatabaseHelper;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/*******
 * <p> Title: InstructorHomePage class. </p>
 * 
 * <p> Description: A Page that welcomes Instructor and allows to act. </p>
 * 
 */
public class InstructorHomePage {
	private static final DatabaseHelper databaseHelper = new DatabaseHelper();
	
	/**
     * Display the InstructorHomePage page.
     * @param primaryStage The primary stage where the scene will be displayed.
     * @param user The user who is accessing this page
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
	    Label userLabel = new Label("Hello, Instructor!");
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
	    
	    // button to review requests
	    Button reviewRequests = new Button("Review Reviewer Requests");
	    reviewRequests.setOnAction(e -> {
	        new ReviewReviewerRequestsPage().show(primaryStage, user);
	    });
	    
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");
        
	    layout.getChildren().addAll(userLabel,logOut,switchRole, reviewRequests);
	    Scene userScene = new Scene(layout, 800, 400);

	    // Set the scene to primary stage
	    primaryStage.setScene(userScene);
	    primaryStage.setTitle("Instructor Page");
    	
    }
}