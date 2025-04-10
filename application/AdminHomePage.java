package application;

import java.sql.SQLException;

import databasePart1.DatabaseHelper;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


/*******
 * <p> Title: AdminHomePage class. </p>
 * 
 * <p> Description: A Page that welcomes admins and allows them to act. </p>
 * 
 */
public class AdminHomePage {
	private static final DatabaseHelper databaseHelper = new DatabaseHelper();
	/**
     * Displays the admin page in the provided primary stage.
     * @param primaryStage The primary stage where the scene will be displayed.
     * @param user The user accessing the page
     */
    public void show(Stage primaryStage, User user) {
    	VBox layout = new VBox();
    	try {
            databaseHelper.connectToDatabase(); // Connect to the database
        } catch (SQLException e) {
        	System.out.println(e.getMessage());
        }
	    layout.setStyle("-fx-alignment: center; -fx-padding: 20;");
	    
	    // label to display the welcome message for the admin
	    Label adminLabel = new Label("Hello, Admin!");
	    
	    adminLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
	    
	    //Button to go to a page that list the users with their information
	    Button ListUsersButton = new Button("List of Users");
	    ListUsersButton.setOnAction(e -> {
	    	new AdminListUsers(databaseHelper).show(primaryStage, user);
	    
	    });
	    
	    //Edit roles button
	    Button EditRolesButton = new Button("Edit Roles");
		EditRolesButton.setOnAction(a -> {			
		new AdminEditRoles(databaseHelper).show(primaryStage, user);
		});
		
		//invite users Button 
	    Button inviteUsersButton = new Button("Invite Users");
	    inviteUsersButton.setOnAction(e -> {
	    	new InvitationPage(databaseHelper).show(primaryStage, user);
	    
	    });
	    
	    //Button to setup a temp pass for user
		Button ForgotPassButton = new Button("Forgot Password");
		ForgotPassButton.setOnAction(a -> {
		new AdminTempPass(databaseHelper).show(primaryStage, user);
		});
		
		//Button to start deleting a user
		Button DeleteButton = new Button("Delete Users");
		DeleteButton.setOnAction(a -> {			
			new AdminDeleteUsers(databaseHelper).show(primaryStage, user);
		});
		
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
	    
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");
        
        
	    layout.getChildren().addAll(adminLabel,ListUsersButton,EditRolesButton,inviteUsersButton,ForgotPassButton,DeleteButton,logOut,switchRole);
	    Scene adminScene = new Scene(layout, 800, 400);

	    // Set the scene to primary stage
	    primaryStage.setScene(adminScene);
	    primaryStage.setTitle("Admin Page");
    }
}