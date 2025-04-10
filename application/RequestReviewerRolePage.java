package application;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import databasePart1.DatabaseHelper;

/*******
 * <p> Title: RequestReviewerRolePage class. </p>
 * 
 * <p> Description: A Page that allows users to request the role of Reviewer. </p>
 * 
 */
public class RequestReviewerRolePage {
    private static final DatabaseHelper db = new DatabaseHelper();

    /**
     * Display the RequestReviewerRolePage page.
     * @param primaryStage The primary stage where the scene will be displayed.
     * @param user The user who is requesting
     */
    public void show(Stage primaryStage, User user) {
        Label title = new Label("Request Reviewer Role");
        Label message = new Label();
        Button requestButton = new Button("Request Role");
        Button backButton = new Button("Back");

        requestButton.setOnAction(e -> {
            try {
                db.connectToDatabase();
                db.storeReviewerRequest(new ReviewerRequest(user.getUserName()));
                message.setText("Request Submitted!");
            } catch (Exception ex) {
                message.setText("Error submitting request.");
                ex.printStackTrace();
            }
        });

        backButton.setOnAction(e -> {
            new StudentHomePage().show(primaryStage, user);
        });

        VBox layout = new VBox(10, title, requestButton, backButton, message);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");
        primaryStage.setScene(new Scene(layout, 600, 300));
        primaryStage.setTitle("Request Reviewer Role");
        primaryStage.show();
    }
}
