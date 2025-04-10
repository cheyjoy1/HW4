package application;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.SQLException;

import databasePart1.*;

/*******
 * <p> Title: setAnswered class. </p>
 * 
 * <p> Description: A Page that allows users to set an answer as the one that answer the question. </p>
 * 
 */
public class setAnswered {
	
    private final DatabaseHelper databaseHelper = new DatabaseHelper();

    /**
     * Display the setAnswered page and setting logic.
     * @param primaryStage The primary stage where the scene will be displayed.
     * @param user The user who is setting the answer
     */
    public void show(Stage primaryStage, User user) {
    	// Input field for the Answer ID that answered the question
        TextField answerID = new TextField();
        answerID.setPromptText("Enter ID of Answer");
        answerID.setMaxWidth(250);

        
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


        Button postButton = new Button("Finalize");
        
        postButton.setOnAction(a -> {
        	// Retrieve user inputs
            boolean skip = false;
            int aID = -1;
            try {
            	aID = Integer.parseInt(answerID.getText());
            } catch (NumberFormatException e) {
            	errorLabel.setText("Non-number");
            	skip = true;
            }
            String validateID = QATextChecker.checkForValidAnsID(aID);
            if(validateID.equals("Valid") && !skip) {
				databaseHelper.setAnswer(aID);
				errorLabel.setText("Answer Set to Right");
			}else if(!skip) {
				errorLabel.setText(validateID);
			} 
        });
        
      //Button to log out
	    Button Homepage = new Button("Homepage");
	    Homepage.setOnAction(a -> {
	    	new StudentHomePage().show(primaryStage,user);
	        
	    });

        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");
        layout.getChildren().addAll(answerID, postButton, errorLabel, Homepage);

        primaryStage.setScene(new Scene(layout, 800, 400));
        primaryStage.setTitle("User Login");
        primaryStage.show();
    }
}
