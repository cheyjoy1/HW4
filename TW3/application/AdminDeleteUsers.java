package application;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.SQLException;

import databasePart1.*;


/*******
 * <p> Title: AdminDeleteUsers class. </p>
 * 
 * <p> Description: A Page that allows admins to delete users. </p>
 * 
 */
public class AdminDeleteUsers {
	
	private final DatabaseHelper databaseHelper;
	
	/**
	 * This is the constructor for the class.
	 * @param databaseHelper Allows access into the database functions
	 */
    public AdminDeleteUsers(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }
    /**
	 * This creates all the buttons and text boxes on the page.
	 * It also handles the logic of the buttons and boxes.
	 * @param primaryStage The primary stage where the scene will be displayed.
     * @param user The user who is deleting a user
	 */
    public void show(Stage primaryStage, User user) {
    	VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");
    	
        //Field to type the username of account to delete
    	TextField userNameField = new TextField();
        userNameField.setPromptText("Enter userName");
        userNameField.setMaxWidth(250);
        
        // Label to display error messages
        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
        
        Button deleteButton = new Button("Delete User");
        
        deleteButton.setOnAction(a -> {
        	// Retrieve user input
        	String userName = userNameField.getText();
            String validUser = UserNameRecognizer.checkForValidUserName(userName);
            if(validUser.equals("") && databaseHelper.doesUserExist(userName)) {
            	//create pop up to confirm the deletion
            	Alert alert = new Alert(AlertType.CONFIRMATION);
            	alert.setTitle("Confirm Action");
                alert.setHeaderText("Are you sure?");
                alert.setContentText("Do you really want to delete User: " + userName +"?");
                
                // Show and wait for the user's response
                ButtonType result = alert.showAndWait().orElse(ButtonType.CANCEL);

                // Handle the user's response
                if (result == ButtonType.OK) {
                	// Remove a User in the database
    				databaseHelper.removeUser(userName);
    				System.out.println("User Removed");
    				new AdminHomePage().show(primaryStage, user);
                } else {
                    System.out.println("User canceled the action.");
                }
			}else if (!validUser.equals("")) {
        		errorLabel.setText("Invalid Username: " + validUser);
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
        
        layout.getChildren().addAll(userNameField, errorLabel, deleteButton, logOut, switchRole);

        primaryStage.setScene(new Scene(layout, 800, 400));
        primaryStage.setTitle("Delete User");
        primaryStage.show();
    }
}