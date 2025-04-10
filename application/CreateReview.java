package application;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.SQLException;

import databasePart1.*;

/*******
 * <p> Title: CreateReview class. </p>
 * 
 * <p> Description: A Page that allows students post Review to system. </p>
 * 
 */
public class CreateReview {
	
    private final DatabaseHelper databaseHelper = new DatabaseHelper();;

    /**
     * Display the CreateReview page and create Review logic.
     * @param primaryStage The primary stage where the scene will be displayed.
     * @param user The user who is creating an Review
     */
    public void show(Stage primaryStage, User user) {
    	// Input field for the Id of the Review being Reviewed and Review Text
        TextField ReviewID = new TextField();
        ReviewID.setPromptText("Enter ID of Repsonse");
        ReviewID.setMaxWidth(250);

        TextField reviewText = new TextField();
        reviewText.setPromptText("Enter Text Body");
        reviewText.setMaxWidth(250);
        
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


        Button postButton = new Button("Post");
        
        postButton.setOnAction(a -> {
        	// Retrieve user inputs
            String text = reviewText.getText();
            boolean skip = false;
            int qID = -1;
            try {
            	qID = Integer.parseInt(ReviewID.getText());
            } catch (NumberFormatException e) {
            	errorLabel.setText("Non-number");
            	skip = true;
            }
            String validateID = QATextChecker.checkForValidID(qID);
            String validateText = QATextChecker.checkForValidBodyText(text);
            try {
            	if(validateText.equals("Valid") && validateID.equals("Valid") && !skip) {
            		Review ans = new Review(user.getUserName(), text, qID);
            		databaseHelper.storeReview(ans);
            		errorLabel.setText("Review Posted");
            	}else if(validateText.equals("Valid")&& !skip) {
            		errorLabel.setText(validateID);
            	}else if(!skip){
            		errorLabel.setText(validateText);
            	}
            } catch (SQLException e) {
                System.err.println("Database error: " + e.getMessage());
                e.printStackTrace();
            } 
        });
        
      //Button to log out
	    Button Homepage = new Button("Homepage");
	    Homepage.setOnAction(a -> {
	    	new ReviewerHomePage().show(primaryStage,user);
	        
	    });

        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");
        layout.getChildren().addAll(reviewText, ReviewID, postButton, errorLabel, Homepage);

        primaryStage.setScene(new Scene(layout, 800, 400));
        primaryStage.setTitle("User Login");
        primaryStage.show();
    }
}
