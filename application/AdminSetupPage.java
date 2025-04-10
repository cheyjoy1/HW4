package application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.SQLException;

import databasePart1.*;

/**
 * The SetupAdmin class handles the setup process for creating an administrator account.
 * This is intended to be used by the first user to initialize the system with admin credentials.
 */
public class AdminSetupPage {
	
    private final DatabaseHelper databaseHelper;

    /**
	 * This is the constructor for the class.
	 * It takes a DatabaseHelper to use during the page
	 * @param databaseHelper Allows access into the database functions
	 */
    public AdminSetupPage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }
    
    /**
	 * This creates all the buttons and text boxes for the page.
	 * It also handles the logic of the buttons and boxes.
	 * @param primaryStage The primary stage where the scene will be displayed.
	 */
    public void show(Stage primaryStage) {
    	// Input fields for userName, password, email, and name
    	TextField nameField = new TextField();
    	nameField.setPromptText("Enter Name");
    	nameField.setMaxWidth(250);
        
        TextField emailField = new TextField();
        emailField.setPromptText("Enter Email");
        emailField.setMaxWidth(250);
        
        TextField userNameField = new TextField();
        userNameField.setPromptText("Enter Admin userName");
        userNameField.setMaxWidth(250);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter Password");
        passwordField.setMaxWidth(250);
        
        // Label to display error messages for invalid input or registration issues
        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");

        Button setupButton = new Button("Setup");
        
        setupButton.setOnAction(a -> {
        	// Retrieve user input
        	String userName = userNameField.getText();
            String password = passwordField.getText();
            String name = nameField.getText();
            String email = emailField.getText();
            String validUser = UserNameRecognizer.checkForValidUserName(userName);
            String validPass = PasswordEvaluator.evaluatePassword(password);
            String validName = NameRecognizer.checkForValidName(name);
            String validEmail = EmailRecognizer.checkForValidEmail(email);
            System.out.println(validUser);
            try {
            	if(validUser.equals("") && validPass.equals("") && validName.equals("") && validEmail.equals("")) {
            		// Create a new User object with admin role and register in the database
            		User user=new User(userName, password, ",admin,", email, name);
            		databaseHelper.register(user);
            		System.out.println("Administrator setup completed.");
                
            		// Navigate to the Welcome Login Page
            		new WelcomeLoginPage(databaseHelper).show(primaryStage,user);
            	}else if (!validName.equals("")) {
            		errorLabel.setText("Invalid Name: " + validName);
            	}else if (!validEmail.equals("")) {
            		errorLabel.setText("Invalid Email: " + validEmail);
            	}else if (!validUser.equals("")) {
            		errorLabel.setText("Invalid Username: " + validUser);
            	}else if(!validPass.equals("")) {
            		errorLabel.setText("Invalid Password: " + validPass);
            	}
            } catch (SQLException e) {
                System.err.println("Database error: " + e.getMessage());
                e.printStackTrace();
            }
        });

        VBox layout = new VBox(10, nameField, emailField, userNameField, passwordField, setupButton);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        primaryStage.setScene(new Scene(layout, 800, 400));
        primaryStage.setTitle("Administrator Setup");
        primaryStage.show();
    }
}
