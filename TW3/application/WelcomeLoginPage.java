package application;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.application.Platform;
import databasePart1.*;

/**
 * The WelcomeLoginPage class displays a welcome screen for authenticated users.
 * It allows users to navigate to their respective pages based on their role or quit the application.
 */
public class WelcomeLoginPage {
	
	private final DatabaseHelper databaseHelper;

	/**
	 * This is the constructor for the class.
	 * @param databaseHelper Allows access into the database functions
	 */
    public WelcomeLoginPage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }
    
    /**
     * Display the WelcomeLoginPage page.
     * @param primaryStage The primary stage where the scene will be displayed.
     * @param user The user who is searching
     */
    public void show(Stage primaryStage, User user) {
    	
    	VBox layout = new VBox(5);
	    layout.setStyle("-fx-alignment: center; -fx-padding: 20;");
	    
	    Label welcomeLabel = new Label("Welcome!!");
	    welcomeLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

	    // Button to quit the application
	    Button quitButton = new Button("Quit");
	    quitButton.setOnAction(a -> {
	    	databaseHelper.closeConnection();
	    	Platform.exit(); // Exit the JavaFX application
	    });
	
	    
	    layout.getChildren().add(welcomeLabel);
	    
	 // Buttons to navigate to the user's respective page based on their role
	    String role = user.getRole();
    	if(role!=null) {
    		if(role.contains("admin")) {
    			Button AdminButton = new Button("Continue to Admin");
    			AdminButton.setOnAction(a -> {
    				new AdminHomePage().show(primaryStage, user);
                });
    			layout.getChildren().add(AdminButton);
    		}if(role.contains("student")) {
    			Button StudentButton = new Button("Continue to Student");
    			StudentButton.setOnAction(a -> {
    				new StudentHomePage().show(primaryStage, user);
                });
    			layout.getChildren().add(StudentButton);
    		}if(role.contains("instructor")) {
    			Button InstructorButton = new Button("Continue to Instructor");
    			InstructorButton.setOnAction(a -> {
    				new InstructorHomePage().show(primaryStage,user);
                });
    			layout.getChildren().add(InstructorButton);
    		}if(role.contains("staff")) {
    			Button StaffButton = new Button("Continue to Staff");
    			StaffButton.setOnAction(a -> {
    				new StaffHomePage().show(primaryStage,user);
                });
    			layout.getChildren().add(StaffButton);
    		}if(role.contains("reviewer")) {
    			Button ReveiewerButton = new Button("Continue to Reviewer");
    			ReveiewerButton.setOnAction(a -> {
    				new ReviewerHomePage().show(primaryStage,user);
                });
    			layout.getChildren().add(ReveiewerButton);
    		}
    	}

	    layout.getChildren().add(quitButton);
	    Scene welcomeScene = new Scene(layout, 800, 400);

	    // Set the scene to primary stage
	    primaryStage.setScene(welcomeScene);
	    primaryStage.setTitle("Welcome Page");
    }
}