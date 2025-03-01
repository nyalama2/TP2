package application;

import databasePart1.DatabaseHelper;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.sql.SQLException;

public class QuestionApp {

    private DatabaseHelper dbHelper;
    private Question currentQuestion; // Holds the current question for demo

    public QuestionApp(DatabaseHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public void show(Stage stage) {
        VBox root = new VBox(10);
        root.setPadding(new Insets(15));
        
        Label titleLabel = new Label("Question CRUD Demo");
        TextField questionField = new TextField();
        questionField.setPromptText("Enter question content");
        
        Button createButton = new Button("Create Question");
        Button readButton = new Button("Read Question");
        Button updateButton = new Button("Update Question");
        Button deleteButton = new Button("Delete Question");
        Button backButton = new Button("Back");
        Button toMessages = new Button("Messages");
        TextArea outputArea = new TextArea();
        
        TextField idGet = new TextField();
        idGet.setMaxWidth(50);
        
        HBox TopButtons = new HBox(5, createButton, readButton);
        HBox BotButtons = new HBox(5, idGet, updateButton, deleteButton);
        
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
            if (!idGet.getText().isEmpty()) {
                try {
                	int id = Integer.parseInt(idGet.getText());
                    Question q = Question.read(dbHelper, id);
                    if (q != null) {
                        outputArea.appendText("Read: " + q + "\n");
                    } else {
                        outputArea.appendText("Question not found.\n");
                    }
                } catch (SQLException ex) {
                    outputArea.appendText("Error reading question: " + ex.getMessage() + "\n");
                }
            } else {
                outputArea.appendText("Wrong Question ID to read.\n");
            }
        });
        
        updateButton.setOnAction(e -> {
            if (!idGet.getText().isEmpty()) {
                try {
                	int id = Integer.parseInt(idGet.getText());
                    String newContent = questionField.getText();
                    Question q = Question.read(dbHelper, id);
                    q.update(dbHelper, newContent);
                    outputArea.appendText("Updated: " + q + "\n");
                } catch (Exception ex) {
                    outputArea.appendText("Error updating question: " + ex.getMessage() + "\n");
                }
            } else {
                outputArea.appendText("Incorrect Question ID.\n");
            }
        });
        
        deleteButton.setOnAction(e -> {
            if (!idGet.getText().isEmpty()) {
                try {
                	int id = Integer.parseInt(idGet.getText());
                	Question q = Question.read(dbHelper, id);
                	q.delete(dbHelper);
                    outputArea.appendText("Deleted question with id: " + id + "\n");
                    currentQuestion = null;
                } catch (SQLException ex) {
                    outputArea.appendText("Error deleting question: " + ex.getMessage() + "\n");
                }
            } else {
                outputArea.appendText("Incorrect Question ID to Delete.\n");
            }
        });
        
        backButton.setOnAction(e -> new QuestionsApp().start(stage));
        
        toMessages.setOnAction(e -> new MessagesApp(dbHelper).show(stage));
        
        root.getChildren().addAll(titleLabel, questionField, TopButtons, BotButtons, toMessages, backButton, outputArea);
        Scene scene = new Scene(root, 400, 500);
        stage.setScene(scene);
        stage.setTitle("Question CRUD Demo");
        stage.show();
    }
}
