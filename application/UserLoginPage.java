package application;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.SQLException;

import databasePart1.*;

/**
 * The UserLoginPage class provides a login interface for users to access their accounts.
 * It validates the user's credentials and navigates to the appropriate page upon successful login.
 */
public class UserLoginPage {
	
    private final DatabaseHelper databaseHelper;

    /**
	 * This is the constructor for the class.
	 * @param databaseHelper Allows access into the database functions
	 */
    public UserLoginPage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }
    
    /**
     * Display the UserLoginPage page and login logic.
     * @param primaryStage The primary stage where the scene will be displayed.
     */
    public void show(Stage primaryStage) {
    	// Input field for the user's userName, password
        TextField userNameField = new TextField();
        userNameField.setPromptText("Enter userName");
        userNameField.setMaxWidth(250);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter Password");
        passwordField.setMaxWidth(250);
        
        // Label to display error messages
        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");


        Button loginButton = new Button("Login");
        
        loginButton.setOnAction(a -> {
        	// Retrieve user inputs
            String userName = userNameField.getText();
            String password = passwordField.getText();
            try {
            	User user=new User(userName, password, "", "", "");
            	WelcomeLoginPage welcomeLoginPage = new WelcomeLoginPage(databaseHelper);
            	
            	// Retrieve the user's role from the database using userName
            	String role = databaseHelper.getUserRole(userName);
            	
            	// Take user with one role to their home screen
            	if(role != null && databaseHelper.tempPass(userName)) {          //check if the login is a temp password
            		new ChangePassword(databaseHelper).show(primaryStage,user);
            	}else if((role!=null) && !(role.contains(",,"))) {				 //check if the login only has one role and send them to their home page
            		user.setRole(role);
            		if(role.contains("admin") && databaseHelper.login(user)) {
            			welcomeLoginPage.show(primaryStage,user);
        	    	}else if(role.contains("student")  && databaseHelper.login(user)) {
        	    		new StudentHomePage().show(primaryStage,user);
        	    	}else if(role.contains("instructor")  && databaseHelper.login(user)) {
        	    		new InstructorHomePage().show(primaryStage,user);
        	    	}else if(role.contains("staff")  && databaseHelper.login(user)) {
        	    		new StaffHomePage().show(primaryStage,user);
        	    	}else if(role.contains("reviewer")  && databaseHelper.login(user)) {
        	    		new ReviewerHomePage().show(primaryStage,user);
        	    	}else {
            			// Display an error if the login fails
                        errorLabel.setText("Error logging in");
            		}
            	}else if(role!=null) {							//The login works and has more than one role so send them to choose their role
            		user.setRole(role);
            		if(databaseHelper.login(user)) {
            			welcomeLoginPage.show(primaryStage,user);
            		}
            		else {
            			// Display an error if the login fails
                        errorLabel.setText("Error logging in");
            		}
            	}else {
            		// Display an error if the account does not exist
                    errorLabel.setText("user account doesn't exists");
            	}
            	
            } catch (SQLException e) {
                System.err.println("Database error: " + e.getMessage());
                e.printStackTrace();
            } 
        });

        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");
        layout.getChildren().addAll(userNameField, passwordField, loginButton, errorLabel);

        primaryStage.setScene(new Scene(layout, 800, 400));
        primaryStage.setTitle("User Login");
        primaryStage.show();
    }
}
