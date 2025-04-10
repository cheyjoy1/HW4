package application;

import java.sql.SQLException;
import java.util.*;

import databasePart1.DatabaseHelper;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/*******
 * <p> Title: RateReviewersPage class. </p>
 * 
 * <p> Description: A Page that allows users to rate and get reviews from trusted reviewers. </p>
 * 
 */
public class RateReviewersPage {
    private Map<String, Integer> reviewerWeights = new HashMap<>();
    private Set<String> trustedReviewers = new HashSet<>();
    private static final DatabaseHelper databaseHelper = new DatabaseHelper();

    private TextField nameField = new TextField();
    private TextField weightageField = new TextField();
    private TextField reviewField = new TextField();
    private VBox layout = new VBox(10);

    
    /**
     * Display the RateReviewersPage page.
     * @param primaryStage The primary stage where the scene will be displayed.
     * @param user The user who is rating
     */
    public void show(Stage primaryStage, User user) {
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-padding: 20;");

        Label title = new Label("Rate Reviewers");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        nameField.setPromptText("Enter reviewer name");
        weightageField.setPromptText("Enter weightage (1-10)");
        reviewField.setPromptText("Enter review");

        // Buttons
        Button addButton = new Button("Add Reviewer");
        addButton.setOnAction(e -> addReviewer(nameField.getText(), weightageField.getText()));

        Button trustButton = new Button("Mark Trusted");
        trustButton.setOnAction(e -> markTrustedReviewer(nameField.getText()));

        Button filterReviewsButton = new Button("Show Trusted Reviews");
        filterReviewsButton.setOnAction(e -> {
			try {
				showTrustedReviews();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		});

        // Add elements to layout
        layout.getChildren().addAll(title, nameField, weightageField, addButton, trustButton, reviewField, filterReviewsButton);

        updateReviewerList();

        Scene scene = new Scene(layout, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Rate Reviewers");
        primaryStage.show();
    }

    /**
     * Adds a Reviewer to the list of reviewers
     * @param name Username of the reviewer
     * @param weightageText Number of their order
     */
    private void addReviewer(String name, String weightageText) {
        if (name.isEmpty() || weightageText.isEmpty()) {
            showAlert("Error", "Reviewer name and weightage cannot be empty.");
            return;
        }

        try {
            int weightage = Integer.parseInt(weightageText);
            if (weightage < 1 || weightage > 10) {
                showAlert("Error", "Weightage must be between 1 and 10.");
                return;
            }

            reviewerWeights.put(name, weightage);
            updateReviewerList();
            showAlert("Success", "Reviewer added successfully.");
        } catch (NumberFormatException e) {
            showAlert("Error", "Invalid weightage. Enter a number between 1 and 10.");
        }
    }

    /**
     * Adds a Reviewer to the list of trusted reviewers
     * @param reviewer Username of the reviewer
     */
    private void markTrustedReviewer(String reviewer) {
        if (!reviewerWeights.containsKey(reviewer)) {
            showAlert("Error", "Reviewer not found.");
            return;
        }

        trustedReviewers.add(reviewer);
        showAlert("Success", reviewer + " is now a trusted reviewer.");
    }

    /**
     * Shows the list of trusted reviewers
     * @param reviewer Username of the reviewer
     * @throws SQLException if a database access error occurs or the SQL statement is invalid.
     */
    private void showTrustedReviews() throws SQLException {
        layout.getChildren().removeIf(node -> node instanceof Label && ((Label) node).getText().startsWith("Review:"));
        databaseHelper.connectToDatabase();

        boolean hasTrustedReviews = false;
        for (String reviewer : trustedReviewers) {
        	try {
        		List<Review> Reviews = databaseHelper.getReviewsForUsername(reviewer);
        		hasTrustedReviews = true;
        		for (Review review : Reviews) {
        			layout.getChildren().add(new Label("Review: " + reviewer + " - " + review.getText()));
        		}
			} catch (SQLException e) {
				e.printStackTrace();
			}
        }

        if (!hasTrustedReviews) {
            showAlert("Info", "No reviews available from trusted reviewers.");
        }
    }

    /**
     * Update the list of reviewers
     */
    private void updateReviewerList() {
        layout.getChildren().removeIf(node -> node instanceof Label && ((Label) node).getText().startsWith("Reviewer:"));

        reviewerWeights.entrySet().stream()
            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed()) // Sort by weightage descending
            .forEach(entry -> layout.getChildren().add(new Label("Reviewer: " + entry.getKey() + " (Weightage: " + entry.getValue() + ")")));
    }

    /**
     * Show info when things don't go as expected 
     * @param title Title for the alert
     * @param message Details about the alert
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
