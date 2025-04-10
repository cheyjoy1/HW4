package application;

import java.sql.SQLException;

import databasePart1.DatabaseHelper;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/*******
 * <p> Title: StaffHomePage class. </p>
 * 
 * <p> Description: A Page that welcomes staff and allows them to act. </p>
 * 
 */
public class StaffHomePage {
	private static final DatabaseHelper databaseHelper = new DatabaseHelper();
	
	/**
     * Displays the staff page in the provided primary stage.
     * @param primaryStage The primary stage where the scene will be displayed.
     * @param user The user accessing the page
     */
    public void show(Stage primaryStage, User user) {
    	try {
            databaseHelper.connectToDatabase(); // Connect to the database
        } catch (SQLException e) {
        	System.out.println(e.getMessage());
        }
    	VBox layout = new VBox();
	    layout.setStyle("-fx-alignment: center; -fx-padding: 20;");
	    
	    // Label to display Hello user
	    Label userLabel = new Label("Hello, Staff!");
	    userLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

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
        
	    layout.getChildren().addAll(userLabel,logOut,switchRole);
	    Scene userScene = new Scene(layout, 800, 400);

	    // Set the scene to primary stage
	    primaryStage.setScene(userScene);
	    primaryStage.setTitle("Staff Page");
    	
    }
}