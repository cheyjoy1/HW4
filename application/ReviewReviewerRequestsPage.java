package application;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.layout.Region;

import databasePart1.DatabaseHelper;

import java.util.List;
import java.util.stream.Collectors;

/*******
 * <p> Title: ReviewReviewerRequestsPage class. </p>
 * 
 * <p> Description: A Page that allows instructors to view all request to be reviewers and assign them. </p>
 * 
 */
public class ReviewReviewerRequestsPage {
    private final DatabaseHelper db = new DatabaseHelper();

    /**
     * Display the ReviewReviewerRequests page and handle the logic of changing roles.
     * @param primaryStage The primary stage where the scene will be displayed.
     * @param instructor The instructor who is using the page
     */
    public void show(Stage primaryStage, User instructor) {
        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 20; -fx-alignment: top-left;");
        Label title = new Label("Pending Reviewer Requests");

        try {
            db.connectToDatabase();
            List<ReviewerRequest> pending = db.getPendingReviewerRequests(); 

            if (pending.isEmpty()) {
                layout.getChildren().add(new Label("No pending requests."));
            } else {
                for (ReviewerRequest request : pending) {
                    String studentUsername = request.getStudentUsername();

                    // Show username
                    Label studentLabel = new Label("Student: " + studentUsername);
                    Button viewSubmissions = new Button("View Submissions");
                    Button approve = new Button("Approve");
                    Button deny = new Button("Deny");

                    // View all their questions and answers
                    viewSubmissions.setOnAction(e -> {
                        List<Question> questions = db.getQuestionsByUser(studentUsername);
                        List<Answer> answers = db.getAnswersByUser(studentUsername);

                        Alert submissions = new Alert(Alert.AlertType.INFORMATION);
                        submissions.setTitle("Student Submissions");

                        String content = "Questions:\n" +
                                questions.stream().map(Question::viewQuestion).collect(Collectors.joining("\n")) +
                                "\n\nAnswers:\n" +
                                answers.stream().map(Answer::viewAnswer).collect(Collectors.joining("\n"));

                        submissions.setContentText(content);
                        submissions.setHeaderText("Submissions by " + studentUsername);
                        submissions.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                        submissions.showAndWait();
                    });

                    approve.setOnAction(e -> {
                        db.updateReviewerRequestStatus(studentUsername, "Approved");
                        db.updateUserRole(studentUsername, ",student,,reviewer,"); // You'll implement this too
                        layout.getChildren().removeAll(studentLabel, viewSubmissions, approve, deny);
                    });

                    deny.setOnAction(e -> {
                        db.updateReviewerRequestStatus(studentUsername, "Denied");
                        layout.getChildren().removeAll(studentLabel, viewSubmissions, approve, deny);
                    });

                    layout.getChildren().addAll(studentLabel, viewSubmissions, approve, deny, new Separator());
                }
            }

        } catch (Exception e) {
            layout.getChildren().add(new Label("Error loading requests: " + e.getMessage()));
            e.printStackTrace();
        }

        Button back = new Button("Back");
        back.setOnAction(e -> new InstructorHomePage().show(primaryStage, instructor));
        layout.getChildren().add(back);

        primaryStage.setScene(new Scene(layout, 800, 500));
        primaryStage.setTitle("Review Reviewer Requests");
        primaryStage.show();
    }
}
