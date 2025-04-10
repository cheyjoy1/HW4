package application;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


import databasePart1.*;

/*******
 * <p> Title: ChangePassword class. </p>
 * 
 * <p> Description: Change the password after a temp password is used. </p>
 * 
 */
public class ChangePassword {
	// DatabaseHelper to handle database operations.
    private final DatabaseHelper databaseHelper;
    
    /**
	 * This is the constructor for the class.
	 * It takes a DatabaseHelper to use during the page
	 * @param databaseHelper Allows access into the database functions
	 */
    public ChangePassword(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }
    

    /**
     * Display the ChangePassword page and handle the changing of the password.
     * @param primaryStage The primary stage where the scene will be displayed.
     * @param user The user whose password will be changed
     */
    public void show(Stage primaryStage, User user) {
    	// Input fields for userName, password, and invitation code

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter Password");
        passwordField.setMaxWidth(250);
        
        
        // Label to display error messages for invalid input or registration issues
        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
        

        Button setupButton = new Button("Set Password");
        
        setupButton.setOnAction(a -> {
        	// Retrieve user input and username
        	String userName = user.getUserName(); 
            String password = passwordField.getText();
            String validPass = PasswordEvaluator.evaluatePassword(password);
            // Check if the username and password are valid
			if(validPass.equals("")) {
			// Create a new user and register them in the database
			    databaseHelper.updateUserPass(userName, password);
			    System.out.print(user.getRole());
			    // Navigate to the Welcome Login Page
			    new UserLoginPage(databaseHelper).show(primaryStage);
			}else if(!validPass.equals("")) {
				errorLabel.setText("Invalid Password: " + validPass);
			}
        });

        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");
        layout.getChildren().addAll(passwordField, setupButton, errorLabel);

        primaryStage.setScene(new Scene(layout, 800, 400));
        primaryStage.setTitle("Change Password");
        primaryStage.show();
    }
}
