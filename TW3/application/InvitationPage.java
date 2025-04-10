package application;


import databasePart1.*;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * InvitePage class represents the page where an admin can generate an invitation code.
 * The invitation code is displayed upon clicking a button.
 */
public class InvitationPage {
	private final DatabaseHelper databaseHelper;
	
	/**
	 * This is the constructor for the class.
	 * @param databaseHelper Allows access into the database functions
	 */
	public InvitationPage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }
	
	/**
     * Displays the Invite Page in the provided primary stage.
     * 
     * @param primaryStage   The primary stage where the scene will be displayed.
     * @param userMain The user that is inviting someone
     */
    public void show(Stage primaryStage, User userMain) {
    	VBox layout = new VBox();
	    layout.setStyle("-fx-alignment: center; -fx-padding: 20;");
	    
	    // Label to display the title of the page
	    Label userLabel = new Label("Invite ");
	    userLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
	    
	    // Role selection
	    ComboBox<String> roleDropdown = new ComboBox<>();
	    roleDropdown.getItems().addAll("admin", "student", "instructor", "staff", "reviewer");
	    roleDropdown.setPromptText("Select Role");
	    
	    
	    // Selecting Date for expiration Date
	    DatePicker expiryPicker = new DatePicker();
	    
	    // Button to generate the invitation code
	    Button showCodeButton = new Button("Generate Invitation Code");
	    
	    // Label to display the generated invitation code
	    Label inviteCodeLabel = new Label(""); ;
        inviteCodeLabel.setStyle("-fx-font-size: 14px; -fx-font-style: italic;");
        
        showCodeButton.setOnAction(a -> {
        	// Generate the invitation code using the databaseHelper and set it to the label
        	String selectedRole = ',' + roleDropdown.getValue() + ",";
        	LocalDateTime expiryDate = expiryPicker.getValue() != null ? expiryPicker.getValue().atStartOfDay(): null;
        	
        	if(expiryDate == null || selectedRole == null) {
        		Alert alert = new Alert(Alert.AlertType.WARNING, "Enter all information before generating");
        		alert.show();
        		return;
        	} 
            String invitationCode = databaseHelper.generateInvitationCode(expiryDate, selectedRole);
            inviteCodeLabel.setText("Invitation Code: " + invitationCode);
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
	    	welcomeLoginPage.show(primaryStage,userMain);
	    });
	    

        layout.getChildren().addAll(userLabel, roleDropdown, expiryPicker, showCodeButton, inviteCodeLabel, logOut, switchRole);
	    Scene inviteScene = new Scene(layout, 800, 400);

	    // Set the scene to primary stage
	    primaryStage.setScene(inviteScene);
	    primaryStage.setTitle("Invite Page");
    	
    }
    
    /** 
     * Generate a unique 6-digit invitation code
     * @return a randomly generated invitation code
    private String generateCode() {
    	Random number = new Random();
    	return Integer.toString(100000 + number.nextInt(900000));
    }
    */
}