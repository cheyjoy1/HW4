package application;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.SQLException;

import databasePart1.*;

/*******
 * <p> Title: AdminTempPass class. </p>
 * 
 * <p> Description: A Page that allows admins to create temporary passwords for users. </p>
 * 
 */
public class AdminTempPass {
	
	private final DatabaseHelper databaseHelper;
	
	/**
	 * This is the constructor for the class.
	 * It takes a DatabaseHelper to use during the page
	 * @param databaseHelper Allows access into the database functions
	 */
    public AdminTempPass(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }
    
    /**
	 * This creates all the buttons and text boxes for the page.
	 * It also handles the logic of the buttons and boxes.
	 * @param primaryStage The primary stage where the scene will be displayed.
	 * @param user User that is creating a temporary password
	 */
    public void show(Stage primaryStage, User user) {
    	System.out.print("debug:admin pass opened");
    	VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");
        
        //Input for User name that will have the temp password
        TextField userNameField = new TextField();
        userNameField.setPromptText("Enter userName");
        userNameField.setMaxWidth(250);
        
        //Input for the temp password
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter Password");
        passwordField.setMaxWidth(250);
        
        // Label to display error messages
        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");

        //Set new temp password button
        Button setupButton = new Button("Change Password");
        setupButton.setOnAction(a -> {
        	// Retrieve user input
        	String userName = userNameField.getText();
            String password = passwordField.getText();
            String validUser = UserNameRecognizer.checkForValidUserName(userName);
            String validPass = PasswordEvaluator.evaluatePassword(password);
            if(validUser.equals("") && validPass.equals("") && databaseHelper.doesUserExist(userName)) {
				// Create a new User object with admin role and register in the database
				databaseHelper.updateUserTempPass(userName, password);
				System.out.println("Password Changed");
				new AdminHomePage().show(primaryStage, user);
			}else if (!validUser.equals("")) {
        		errorLabel.setText("Invalid Username: " + validUser);
        	}else if(!validPass.equals("")) {
        		errorLabel.setText("Invalid Password: " + validPass);
        	}else if(!databaseHelper.doesUserExist(userName)) {
        		errorLabel.setText("This userName is does not exist!!.. Please use another to change the password");
        	}
        });
        
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

        layout.getChildren().addAll(userNameField, passwordField, errorLabel, setupButton, logOut, switchRole);

        primaryStage.setScene(new Scene(layout, 800, 400));
        primaryStage.setTitle("Temporary Password");
        primaryStage.show();
    }
}