package application;

import java.sql.SQLException;
import java.util.List;

import databasePart1.DatabaseHelper;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/*******
 * <p> Title: AdminListUsers class. </p>
 * 
 * <p> Description: A Page that allows admins to list all the users. </p>
 * 
 */
public class AdminListUsers {
	private DatabaseHelper databaseHelper;
	/**
	 * This is the constructor for the class.
	 * It takes a DatabaseHelper to use during the page
	 * @param databaseHelper Allows access into the database functions
	 */
	public AdminListUsers(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }
	
	/**
	 * This creates all the buttons and text boxes for the page.
	 * It also handles the logic of the buttons and boxes.
	 * @param primaryStage The primary stage where the scene will be displayed.
	 * @param user User that is Listing the all users
	 */
    public void show(Stage primaryStage, User user) {
    	
    	VBox layout = new VBox();
	    layout.setStyle("-fx-alignment: center; -fx-padding: 20;");
	    String contents = "Username Name Email Roles \n";
	    
	    
	    List<User> people = databaseHelper.getAllUsers();   //get a list of all the users with their info (without password)
	    
	    for (int i = 0; i < people.size(); i++) {		//Go through the list and copy it to contents in a string form
	    	String UserName = people.get(i).getUserName();
	    	String Roles = people.get(i).getRole();
	    	Roles = Roles.substring(1,Roles.length()-1);
	    	String Name = people.get(i).getName();
	    	String Email = people.get(i).getEmail();
	    	contents += UserName + " " + Name + " " + Email +" " + Roles + "\n";
	    }
	    contents += "\n"; 
	    
	    // Label to display Hello user
	    Label userList = new Label(contents);
	    userList.setStyle("-fx-font-size: 14px; -fx-font-weight: normal;");
	    
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
	    
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");
        
	    layout.getChildren().addAll(userList,logOut,switchRole);
	    Scene userScene = new Scene(layout, 800, 400);

	    // Set the scene to primary stage
	    primaryStage.setScene(userScene);
	    primaryStage.setTitle("List of Users");
    	
    }
}