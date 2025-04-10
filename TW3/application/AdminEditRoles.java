package application;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import databasePart1.*;

import java.util.HashMap;
import java.util.Map;

/*******
 * <p> Title: AdminEditRoles class. </p>
 * 
 * <p> Description: A Page that allows admins to edit user roles. </p>
 * 
 */
public class AdminEditRoles {
	
	private final DatabaseHelper databaseHelper;
	 private final Map<User, Map<String, CheckBox>> userRoleCheckBoxes = new HashMap<>();
	 //private final Map<CheckBox, User> userCheckBoxes = new HashMap<>();
	private final String[] roles = {"admin", "student", "reviewer", "instructor", "staff"};

	/**
	 * This is the constructor for the class.
	 * It takes a DatabaseHelper to use during the page
	 * @param databaseHelper Allows access into the database functions
	 */
    public AdminEditRoles(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }
    
    /**
     * Display the AdminEditRoles page and handle the changing of the roles.
     * @param primaryStage The primary stage where the scene will be displayed.
     * @param userMain The user who is changing roles
     */
    public void show(Stage primaryStage, User userMain) {
    	System.out.print("debug:admineditroles opened");
    	VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        /* Retrieve users from the database
        for (User user : databaseHelper.getAllUsers()) {
            CheckBox checkBox = new CheckBox(user.getUserName() + " (" + user.getRole() + ")");
            userCheckBoxes.put(checkBox, user);
            layout.getChildren().add(checkBox);
        }
        */
        //Platform.runLater(() -> {
            for (User user : databaseHelper.getAllUsers()) {
                System.out.println("Debug: Found user -> " + user.getUserName());

                HBox row = new HBox(10);
                row.setStyle("-fx-padding: 5; -fx-alignment: center-left;");

                Label nameLabel = new Label(user.getUserName());
                row.getChildren().add(nameLabel);
                Map<String, CheckBox> roleCheckBoxes = new HashMap<>();
                String storedRoles = user.getRole();
                if (storedRoles == null) {
                    storedRoles = "";
                }
                if (!storedRoles.startsWith(",")) {
                    storedRoles = "," + storedRoles + ",";
                }

                for (String role : roles) {
                    CheckBox roleCheckBox = new CheckBox(role);
                    
                    // If the user's stored role string contains this role (with surrounding commas), select it.
                    if (storedRoles.contains("," + role + ",")) {
                        roleCheckBox.setSelected(true);
                    }
                    
                    roleCheckBoxes.put(role, roleCheckBox);
                    row.getChildren().add(roleCheckBox);
                }
                
                userRoleCheckBoxes.put(user, roleCheckBoxes);
                layout.getChildren().add(row);

            }
        //});

        Button saveButton = new Button("Save Changes");
        saveButton.setOnAction(event -> updateUserRoles());
        

	  //Button to log out
	    Button logOut = new Button("Log Out");
	    logOut.setOnAction(a -> {
	        new SetupLoginSelectionPage(databaseHelper).show(primaryStage);
	        
	    });
	    //Button to switch role
	    Button switchRole = new Button("Switch Role");
	    switchRole.setOnAction(a -> {
	    	WelcomeLoginPage welcomeLoginPage = new WelcomeLoginPage(databaseHelper);
	    	welcomeLoginPage.show(primaryStage, userMain);
	    });

        layout.getChildren().addAll(saveButton, logOut, switchRole);

        primaryStage.setScene(new Scene(layout, 800, 400));
        primaryStage.setTitle("Edit Roles");
        primaryStage.show();
    }
    
    /**
	 * This updates the User Roles in the database
	 */
    private void updateUserRoles() {
        for (Map.Entry<User, Map<String, CheckBox>> entry : userRoleCheckBoxes.entrySet()) {
            User user = entry.getKey();
            Map<String, CheckBox> roleCheckBoxes = entry.getValue();
            
            StringBuilder newRoleBuilder = new StringBuilder();
            for (String role : roles) {
                CheckBox checkBox = roleCheckBoxes.get(role);
                if (checkBox.isSelected()) {
                
                    newRoleBuilder.append(",").append(role).append(",");
                }
            }
            String newRole = newRoleBuilder.toString();
           
            if(newRole.equals(""))
        	{
        		System.out.println("Need to have at least one role for user " + user.getUserName() + ", no changes made");
        		continue;
        	}
            
            else if(user.getRole().contains("admin") == true && newRole.contains("admin") == false)
        	{
        		System.out.println("can't remove admin, no changes made to this user " + user.getUserName());
        		continue;
        	}
            
            System.out.println("Updating " + user.getUserName() + " to roles: " + newRole);
            databaseHelper.updateUserRole(user.getUserName(), newRole);
            
           
        }
    	/*
        for (Map.Entry<CheckBox, User> entry : userCheckBoxes.entrySet()) {
            if (entry.getKey().isSelected()) {
                databaseHelper.updateUserRole(entry.getValue().getUserName(), "admin"); // Example change
            } else {
                databaseHelper.updateUserRole(entry.getValue().getUserName(), "user"); // Default role
            }
        }
        */
    }
}