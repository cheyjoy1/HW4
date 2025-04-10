package application;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.SQLException;

import databasePart1.*;

/*******
 * <p> Title: SendMessage class. </p>
 * 
 * <p> Description: A Page that allows students and reviewers to send messages. </p>
 * 
 */
public class SendMessage {

    private final DatabaseHelper databaseHelper = new DatabaseHelper();

    /**
     * Display the SendMessage page and logic.
     * @param primaryStage The primary stage where the scene will be displayed.
     * @param user The user who is sending a message
     * @param role The role the user who is creating an answer
     */
    public void show(Stage primaryStage, User user, String role) {
    	// Input field for the Id of the Question being answered and Answer Text
    	TextField searchField = new TextField();
        searchField.setPromptText("Enter Username");
        searchField.setMaxWidth(250);

        TextField answerText = new TextField();
        answerText.setPromptText("Enter Text Body");
        answerText.setMaxWidth(250);
        
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

        Button postButton = new Button("Send");
        
        postButton.setOnAction(a -> {
        	// Retrieve user inputs
            String text = answerText.getText();
            String userName = searchField.getText();
            String validateText = QATextChecker.checkForValidBodyText(text);
            try {
            	if(validateText.equals("Valid")) {
            		Message ans = new Message(user.getUserName(), userName, text);
            		databaseHelper.storeMessage(ans);
            		errorLabel.setText("Message Sent");
            	}else{
            		errorLabel.setText(validateText);
            	}
            } catch (SQLException e) {
                System.err.println("Database error: " + e.getMessage());
                e.printStackTrace();
            } 
        });
        
        // back to home
        Button homeButton = new Button("Homepage");
        homeButton.setOnAction(e -> {
        if(role.equals("Reviewer")) {
        	new ReviewerHomePage().show(primaryStage, user);
        }else {
        	new StudentHomePage().show(primaryStage, user);
        }});

        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");
        layout.getChildren().addAll(answerText, searchField, postButton, errorLabel, homeButton);

        primaryStage.setScene(new Scene(layout, 800, 400));
        primaryStage.setTitle("User Login");
        primaryStage.show();
    }
}
