package application;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import databasePart1.DatabaseHelper;

import java.sql.SQLException;
import java.util.List;

/*******
 * <p> Title: SearchQuestions class. </p>
 * 
 * <p> Description: A Page that allows users to search for questions. </p>
 * 
 */
public class SearchQuestions {

    private final DatabaseHelper databaseHelper = new DatabaseHelper();

    /**
     * Display the SearchQuestions page and search logic.
     * @param primaryStage The primary stage where the scene will be displayed.
     * @param user The user who is searching
     */
    public void show(Stage primaryStage, User user) {
    	
        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        Label headerLabel = new Label("Search Questions");
        headerLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        TextField searchField = new TextField();
        searchField.setPromptText("Enter search text");
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
                statusLabel.setText("Please enter search text.");
                return;
            }
            statusLabel.setText("");
            try {
                databaseHelper.connectToDatabase();
                // call db function
                List<Question> questions = databaseHelper.searchQuestions(searchText);
                if (questions.isEmpty()) {
                    statusLabel.setText("No questions found matching your search.");
                } else {
                    for (Question q : questions) {
                        // button for each question
                        Button qButton = new Button(q.getTitle() + " #" + q.getIdentity());
                        qButton.setOnAction(ev -> showQuestionDetails(q, primaryStage, user));
                        resultsBox.getChildren().add(qButton);
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                statusLabel.setText("Error searching questions: " + ex.getMessage());
            }
        });

        // back to home
        Button homeButton = new Button("Homepage");
        homeButton.setOnAction(e -> new StudentHomePage().show(primaryStage, user));

        layout.getChildren().addAll(headerLabel, searchField, searchButton, statusLabel, resultsBox, homeButton);

        Scene scene = new Scene(layout, 800, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Search Questions");
        primaryStage.show();
    }

    /**
     * Change the page to show a question, its answers, and reviews
     * @param question The question to start showing
     * @param primaryStage The primary stage where the scene will be displayed.
     * @param user The user who is searching
     */
    private void showQuestionDetails(Question question, Stage primaryStage, User user) {
        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        Label detailsLabel = new Label("Question Details");
        detailsLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Label textLabel = new Label(question.viewQuestion());

        // Container for answers (with their reviews)
        VBox answersBox = new VBox(10);
        answersBox.setStyle("-fx-padding: 10; -fx-alignment: center;");
        Label answersHeader = new Label("Answers:");

        try {
            List<Answer> answers = databaseHelper.getAnswersForQuestion(question.getIdentity());
            if (answers.isEmpty()) {
                answersBox.getChildren().add(new Label("No answers available."));
            } else {
                for (Answer a : answers) {
                    // Container for each answer and its reviews
                    VBox answerContainer = new VBox(5);
                    Label answerLabel = new Label(a.viewFull(10));
                    answerContainer.getChildren().add(answerLabel);
                    
                    // Fetch and display reviews for this answer
                    try {
                        List<Review> reviews = databaseHelper.getReviewsForAnswer(a.getIdentity());
                        if (reviews.isEmpty()) {
                            answerContainer.getChildren().add(new Label("No reviews available for this answer."));
                        } else {
                            for (Review r : reviews) {
                                Label reviewLabel = new Label(r.viewReview());
                                answerContainer.getChildren().add(reviewLabel);
                            }
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        answerContainer.getChildren().add(new Label("Error retrieving reviews: " + ex.getMessage()));
                    }
                    answersBox.getChildren().add(answerContainer);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            answersBox.getChildren().add(new Label("Error retrieving answers: " + ex.getMessage()));
        }

        Button backButton = new Button("Back to Search Results");
        backButton.setOnAction(e -> show(primaryStage, user));

        layout.getChildren().addAll(detailsLabel, textLabel, answersHeader, answersBox, backButton);

        Scene scene = new Scene(layout, 800, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Question Details");
        primaryStage.show();
    }
}
