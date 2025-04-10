package application;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.SQLException;

import databasePart1.*;

/*******
 * <p> Title: CreateQuestion class. </p>
 * 
 * <p> Description: A Page that allows students post question to system. </p>
 * 
 */
public class CreateQuestion {
	
    private final DatabaseHelper databaseHelper = new DatabaseHelper();

    /**
     * Display the CreateQuestion page and create question logic.
     * @param primaryStage The primary stage where the scene will be displayed.
     * @param user The user who is creating an question
     */
    public void show(Stage primaryStage, User user) {
    	// Input field for the Question Title and Text
        TextField questionTitle = new TextField();
        questionTitle.setPromptText("Enter Title");
        questionTitle.setMaxWidth(250);

        TextField questionText = new TextField();
        questionText.setPromptText("Enter Text Body");
        questionText.setMaxWidth(250);
        
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
            String title = questionTitle.getText();
            String text = questionText.getText();
            String validateTitle = QATextChecker.checkForValidTitleText(title);
            String validateText = QATextChecker.checkForValidBodyText(text);
            try {
            	if(validateTitle.equals("Valid") && validateText.equals("Valid")) {
            		Question q = new Question(user.getUserName(), title, text);
            		databaseHelper.storeQuestion(q);
            		errorLabel.setText("Question Posted");
            	}else if(validateTitle.equals("Valid")) {
            		errorLabel.setText(validateText);
            	}else{
            		errorLabel.setText(validateTitle);
            	}
            } catch (SQLException e) {
                System.err.println("Database error: " + e.getMessage());
                e.printStackTrace();
            } 
        });
        
      //Button to log out
	    Button Homepage = new Button("Homepage");
	    Homepage.setOnAction(a -> {
	    	new StudentHomePage().show(primaryStage,user);
	        
	    });

        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");
        layout.getChildren().addAll(questionTitle, questionText, postButton, errorLabel, Homepage);

        primaryStage.setScene(new Scene(layout, 800, 400));
        primaryStage.setTitle("User Login");
        primaryStage.show();
    }
}
