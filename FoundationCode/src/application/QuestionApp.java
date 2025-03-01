package application;

import databasePart1.DatabaseHelper;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.List;

public class QuestionApp {

    private DatabaseHelper dbHelper;
    private Question currentQuestion; // Holds the current question for demo

    public QuestionApp(DatabaseHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public void show(Stage stage) {
        VBox root = new VBox(10);
        root.setPadding(new Insets(15));
        
        // Create tabs for different functionalities
        TabPane tabPane = new TabPane();
        Tab managementTab = new Tab("Manage Questions");
        Tab viewAnswerTab = new Tab("View & Answer");
        managementTab.setClosable(false);
        viewAnswerTab.setClosable(false);

        // Original management content
        VBox managementContent = new VBox(10);
        Label titleLabel = new Label("Question Management");
        TextField questionField = new TextField();
        questionField.setPromptText("Enter question content");
        
        Button createButton = new Button("Create Question");
        Button readButton = new Button("Read Question");
        Button updateButton = new Button("Update Question");
        Button deleteButton = new Button("Delete Question");
        TextArea outputArea = new TextArea();
        
        createButton.setOnAction(e -> {
            try {
                String content = questionField.getText();
                currentQuestion = new Question(content);
                currentQuestion.create(dbHelper);
                outputArea.appendText("Created: " + currentQuestion + "\n");
            } catch (Exception ex) {
                outputArea.appendText("Error creating question: " + ex.getMessage() + "\n");
            }
        });
        
        readButton.setOnAction(e -> {
            if (currentQuestion != null) {
                try {
                    Question q = Question.read(dbHelper, currentQuestion.getId());
                    if (q != null) {
                        outputArea.appendText("Read: " + q + "\n");
                    } else {
                        outputArea.appendText("Question not found.\n");
                    }
                } catch (SQLException ex) {
                    outputArea.appendText("Error reading question: " + ex.getMessage() + "\n");
                }
            } else {
                outputArea.appendText("No question created yet.\n");
            }
        });
        
        updateButton.setOnAction(e -> {
            if (currentQuestion != null) {
                try {
                    String newContent = questionField.getText();
                    currentQuestion.update(dbHelper, newContent);
                    outputArea.appendText("Updated: " + currentQuestion + "\n");
                } catch (Exception ex) {
                    outputArea.appendText("Error updating question: " + ex.getMessage() + "\n");
                }
            } else {
                outputArea.appendText("No question created to update.\n");
            }
        });
        
        deleteButton.setOnAction(e -> {
            if (currentQuestion != null) {
                try {
                    currentQuestion.delete(dbHelper);
                    outputArea.appendText("Deleted question with id: " + currentQuestion.getId() + "\n");
                    currentQuestion = null;
                } catch (SQLException ex) {
                    outputArea.appendText("Error deleting question: " + ex.getMessage() + "\n");
                }
            } else {
                outputArea.appendText("No question created to delete.\n");
            }
        });
        
<<<<<<< Updated upstream
        root.getChildren().addAll(titleLabel, questionField, createButton, readButton, updateButton, deleteButton, outputArea);
        Scene scene = new Scene(root, 400, 500);
=======
        backButton.setOnAction(e -> {
            stage.close();
            // If you have a main menu class, you can create and show it here
        });
        
        managementContent.getChildren().addAll(titleLabel, questionField, TopButtons, BotButtons, outputArea);
        managementTab.setContent(managementContent);

        // View and Answer content
        VBox viewAnswerContent = new VBox(10);
        Label viewTitle = new Label("Available Questions");
        ListView<String> questionList = new ListView<>();
        TextArea answerField = new TextArea();
        answerField.setPromptText("Type your answer here");
        Button submitAnswer = new Button("Submit Answer");
        TextArea answersView = new TextArea();
        answersView.setEditable(false);
        answersView.setPrefRowCount(5);

        // Refresh questions list
        Button refreshButton = new Button("Refresh Questions");
        refreshButton.setOnAction(e -> {
            try {
                List<Question> questions = dbHelper.getAllQuestions();
                questionList.getItems().clear();
                for (Question q : questions) {
                    questionList.getItems().add(q.getId() + ": " + q.getContent());
                }
            } catch (SQLException ex) {
                answersView.appendText("Error loading questions: " + ex.getMessage() + "\n");
            }
        });

        // Handle question selection
        questionList.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                try {
                    int questionId = Integer.parseInt(newVal.split(":")[0]);
                    List<String> answers = dbHelper.getAnswers(questionId);
                    answersView.clear();
                    answersView.appendText("Previous Answers:\n");
                    for (String answer : answers) {
                        answersView.appendText("- " + answer + "\n");
                    }
                } catch (SQLException ex) {
                    answersView.appendText("Error loading answers: " + ex.getMessage() + "\n");
                }
            }
        });

        // Handle answer submission
        submitAnswer.setOnAction(e -> {
            String selected = questionList.getSelectionModel().getSelectedItem();
            if (selected != null && !answerField.getText().isEmpty()) {
                try {
                    int questionId = Integer.parseInt(selected.split(":")[0]);
                    dbHelper.addAnswer(questionId, answerField.getText());
                    answerField.clear();
                    // Refresh answers
                    questionList.getSelectionModel().select(selected);
                } catch (SQLException ex) {
                    answersView.appendText("Error submitting answer: " + ex.getMessage() + "\n");
                }
            }
        });

        viewAnswerContent.getChildren().addAll(
            viewTitle, refreshButton, questionList, 
            new Label("Submit New Answer:"), answerField, 
            submitAnswer, new Label("Previous Answers:"), answersView
        );
        viewAnswerTab.setContent(viewAnswerContent);

        tabPane.getTabs().addAll(managementTab, viewAnswerTab);
        
        // Add back button and set up final scene
        HBox bottomBox = new HBox(10, backButton);
        root.getChildren().addAll(tabPane, bottomBox);
        Scene scene = new Scene(root, 600, 800);
>>>>>>> Stashed changes
        stage.setScene(scene);
        stage.setTitle("Question Management System");
        stage.show();
        
        // Initial load of questions
        refreshButton.fire();
    }
}
