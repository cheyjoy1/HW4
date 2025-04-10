package application;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import databasePart1.DatabaseHelper;

import java.sql.SQLException;
import java.util.List;

/*******
 * <p> Title: ListReviews class. </p>
 * 
 * <p> Description: A Page that allows reviewers to see a list of their reviews. </p>
 * 
 */
public class ListReviews {

    private final DatabaseHelper databaseHelper = new DatabaseHelper();

    /**
     * Display the ListReviews page and search logic.
     * @param primaryStage The primary stage where the scene will be displayed.
     * @param user The user who is searching
     */
    public void show(Stage primaryStage, User user) {
    	
    	VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        Button searchButton = new Button("Show Reviews");

        // search result buttons
        VBox resultsBox = new VBox(5);
        resultsBox.setStyle("-fx-padding: 10; -fx-alignment: center;");

        // error or status messages
        Label statusLabel = new Label();
        statusLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
        

        searchButton.setOnAction(e -> {
            resultsBox.getChildren().clear(); // Clear any previous results.
            try {
                databaseHelper.connectToDatabase();
                // call db function
                List<Review> Reviews = databaseHelper.getReviewsForUsername(user.getUserName());
                if (Reviews.isEmpty()) {
                    statusLabel.setText("No Messages found matching your search.");
                } else {
                    for (Review r : Reviews) {
                        Label messageLabel = new Label(r.viewReview());
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
        homeButton.setOnAction(e -> new ReviewerHomePage().show(primaryStage, user));

        layout.getChildren().addAll(searchButton, statusLabel, resultsBox, homeButton);

        Scene scene = new Scene(layout, 800, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Search Questions");
        primaryStage.show();
    }
}
