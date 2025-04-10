package application;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import databasePart1.DatabaseHelper;

import java.sql.SQLException;
import java.util.List;

/*******
 * <p> Title: GetPrivateMessages class. </p>
 * 
 * <p> Description: A Page that allows students and reviewer to see messages. </p>
 * 
 */
public class GetPrivateMessages {

    private final DatabaseHelper databaseHelper = new DatabaseHelper();

    /**
     * Display the GetPrivateMessages page and search logic.
     * @param primaryStage The primary stage where the scene will be displayed.
     * @param user The user who is searching
     * @param role The role of the user who is searching
     */
    public void show(Stage primaryStage, User user, String role) {
    	
        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        Label headerLabel = new Label("Get Messages From");
        headerLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        TextField searchField = new TextField();
        searchField.setPromptText("Enter Username");
        searchField.setMaxWidth(250);

        Button searchButton = new Button("Search");

        // search result buttons
        VBox resultsBox = new VBox(5);
        resultsBox.setStyle("-fx-padding: 10; -fx-alignment: center;");

        // error or status messages
        Label statusLabel = new Label();
        statusLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
        

        searchButton.setOnAction(e -> {
            resultsBox.getChildren().clear(); // Clear any previous results.
            String searchText = searchField.getText().trim();
            if (searchText.isEmpty()) {
                statusLabel.setText("Please enter Username to search.");
                return;
            }
            statusLabel.setText("");
            try {
                databaseHelper.connectToDatabase();
                // call db function
                List<Message> Messages = databaseHelper.getMessagesBetween(user.getUserName(), searchText);
                if (Messages.isEmpty()) {
                    statusLabel.setText("No Messages found matching your search.");
                } else {
                    for (Message m : Messages) {
                    	System.out.print(m.viewMessage());
                        Label messageLabel = new Label(m.viewMessage());
                        resultsBox.getChildren().add(messageLabel);
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                statusLabel.setText("Error searching questions: " + ex.getMessage());
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

        layout.getChildren().addAll(headerLabel, searchField, searchButton, statusLabel, resultsBox, homeButton);

        Scene scene = new Scene(layout, 800, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Search Questions");
        primaryStage.show();
    }
}
