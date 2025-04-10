package application;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.SQLException;

import databasePart1.*;

/**
 * SetupAccountPage class handles the account setup process for new users.
 * Users provide their userName, password, and a valid invitation code to register.
 */
public class SetupAccountPage {
	
    private final DatabaseHelper databaseHelper;
    
    /**
	 * This is the constructor for the class.
	 * @param databaseHelper Allows access into the database functions
	 */
    public SetupAccountPage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }
    

    /**
     * Displays the Setup Account page in the provided stage.
     * @param primaryStage The primary stage where the scene will be displayed.
     */
    public void show(Stage primaryStage) {
    	// Input fields for userName, password, and invitation code
    	TextField nameField = new TextField();
    	nameField.setPromptText("Enter Name");
    	nameField.setMaxWidth(250);
        
        TextField emailField = new TextField();
        emailField.setPromptText("Enter Email");
        emailField.setMaxWidth(250);
        
        TextField userNameField = new TextField();
        userNameField.setPromptText("Enter userName");
        userNameField.setMaxWidth(250);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter Password");
        passwordField.setMaxWidth(250);
        
        TextField inviteCodeField = new TextField();
        inviteCodeField.setPromptText("Enter InvitationCode");
        inviteCodeField.setMaxWidth(250);
        
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
            String code = inviteCodeField.getText();
            String validUser = UserNameRecognizer.checkForValidUserName(userName);
            String validPass = PasswordEvaluator.evaluatePassword(password);
            String validName = NameRecognizer.checkForValidName(name);
            String validEmail = EmailRecognizer.checkForValidEmail(email);
            String role = databaseHelper.getRoleFromInvitation(code);
            System.out.print(role+"\n");
            try {
            	// Check if the username and password are valid
            	if(validUser.equals("") && validPass.equals("") && validName.equals("") && validEmail.equals("")) {
            		// Check if the user already exists
            		if(!databaseHelper.doesUserExist(userName)) {
            		
            			// Validate the invitation code
                        System.out.print("FDSFASDFSDFDSFSDFSA##$SFASEARFSAD \n");
            			if(databaseHelper.validateInvitationCode(code)) {
            	            System.out.print("FDSFASDFSDFDSFSDFSA##$SFASEARFSAD \n");
            				// Create a new user and register them in the database as a student
    		            	User user=new User(userName, password, role, name, email);
    		                databaseHelper.register(user);
    		                
    		                // Navigate to the Welcome Login Page
    		                new WelcomeLoginPage(databaseHelper).show(primaryStage,user);
                    
            			}
            			else {
            				errorLabel.setText("Please enter a valid invitation code");
            			}
            		}
            		else {
            			errorLabel.setText("This userName is taken!!.. Please use another to setup an account");
            		}
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

        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");
        layout.getChildren().addAll(nameField, emailField, userNameField, passwordField,inviteCodeField, setupButton, errorLabel);

        primaryStage.setScene(new Scene(layout, 800, 400));
        primaryStage.setTitle("Account Setup");
        primaryStage.show();
    }
}
