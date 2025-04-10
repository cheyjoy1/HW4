package application;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.SQLException;

import databasePart1.*;

/*******
 * <p> Title: DeleteReview class. </p>
 * 
 * <p> Description: A Page that allows users to delete their reviews. </p>
 * 
 */
public class DeleteReview {
	
    private final DatabaseHelper databaseHelper = new DatabaseHelper();;

    /**
     * Display the EditReview page and edit logic.
     * @param primaryStage The primary stage where the scene will be displayed.
     * @param user The user who is editing
     */
    public void show(Stage primaryStage, User user) {
        TextField questionID = new TextField();
        questionID.setPromptText("Enter ID");
        questionID.setMaxWidth(250);

        // Label to display error messages
        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
        try {
            databaseHelper.connectToDatabase();
            // Perform database operations
        } catch (SQLException e) {
            e.printStackTrace();
            errorLabel.setText("Database connection failed: " + e.getMessage());
            return;
        } 


        Button postButton = new Button("Change");
        
        postButton.setOnAction(a -> {
        	// Retrieve user inputs
            boolean skip = false;
            int qID = -1;
            try {
            	qID = Integer.parseInt(questionID.getText());
            } catch (NumberFormatException e) {
            	errorLabel.setText("Non-number in ID");
            	skip = true;
            }
            System.out.println(qID + " " + questionID.getText());
            String validateID = QATextChecker.checkForValidReviewID(qID);
            if(validateID.equals("Valid") && !skip) {
				errorLabel.setText("Review Deleted");
				Review ans = databaseHelper.getReview(qID);
				if(user.getUserName().equals(ans.getUsername())) {
					databaseHelper.removeReview(ans);
				}else {
					errorLabel.setText("Not your Review");
				}
			}else if(!skip){
				errorLabel.setText(validateID);
			} 
        });
        
      //Button to log out
	    Button Homepage = new Button("Homepage");
	    Homepage.setOnAction(a -> {
	    	new ReviewerHomePage().show(primaryStage,user);
	        
	    });

        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");
        layout.getChildren().addAll(questionID, postButton, errorLabel, Homepage);

        primaryStage.setScene(new Scene(layout, 800, 400));
        primaryStage.setTitle("User Login");
        primaryStage.show();
    }
}
