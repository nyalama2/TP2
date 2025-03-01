package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class MessagesApp extends Application{
	
	private Messages studentMessaging;
    private TextField questionIdField, recipientField;
    private TextArea messagesArea, messageField;
    private Button fetchMessagesButton, sendButton;

    // Default constructor
    public MessagesApp(Messages studentMessaging) {
        
    	this.studentMessaging = studentMessaging;
    }
    
    public MessagesApp() {
        
    	this.studentMessaging = new Messages("defaultUser");
    }

    @Override
    public void start(Stage primaryStage) {
        
    	primaryStage.setTitle("Student Messaging System");

        // Layout and controls
        GridPane inputGrid = new GridPane();
        inputGrid.setPadding(new Insets(10));
        inputGrid.setHgap(10);
        inputGrid.setVgap(10);

        // Question ID
        Label questionIdLabel = new Label("Question ID:");
        questionIdField = new TextField();
        inputGrid.add(questionIdLabel, 0, 0);
        inputGrid.add(questionIdField, 1, 0);

        // Recipient
        Label recipientLabel = new Label("Recipient:");
        recipientField = new TextField();
        inputGrid.add(recipientLabel, 0, 1);
        inputGrid.add(recipientField, 1, 1);

        // Message
        Label messageLabel = new Label("Message:");
        messageField = new TextArea();
        messageField.setPrefRowCount(3);
        inputGrid.add(messageLabel, 0, 2);
        inputGrid.add(messageField, 1, 2);

        // Messages display area
        messagesArea = new TextArea();
        messagesArea.setEditable(false);
        messagesArea.setPrefHeight(200);
        messagesArea.setWrapText(true);

        // Buttons
        HBox buttonBox = new HBox(10);
        fetchMessagesButton = new Button("Fetch Messages");
        sendButton = new Button("Send Reply");
        buttonBox.getChildren().addAll(fetchMessagesButton, sendButton);

        // Layout container
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));
        layout.getChildren().addAll(inputGrid, new Label("Conversation:"), messagesArea, buttonBox);

        // Button actions
        fetchMessagesButton.setOnAction(e -> fetchMessages());
        sendButton.setOnAction(e -> sendReply());

        // Set up scene
        Scene scene = new Scene(layout, 500, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    
    private void fetchMessages() {
        
    	try {
            
    		int questionId = Integer.parseInt(questionIdField.getText().trim());
            String messages = studentMessaging.readMessages(questionId);
            int unreadCount = studentMessaging.getUnread(questionId);
            messagesArea.setText("Unread Messages: " + unreadCount + "\n\n" + messages);
            
        } catch (NumberFormatException e) {
            
        	showAlert("Invalid Input", "Please enter a valid Question ID.");
        }
    }

    
    private void sendReply() {
       
    	try {
            
    		int questionId = Integer.parseInt(questionIdField.getText().trim());
            String recipient = recipientField.getText().trim();
            String message = messageField.getText().trim();

            if (recipient.isEmpty() || message.isEmpty()) {
                
            	showAlert("Input Error", "Recipient and message cannot be empty.");
                return;
            }

            studentMessaging.reply(questionId, recipient, message);
            messageField.clear();
            showAlert("Success", "Message sent successfully.");
            
        } catch (NumberFormatException e) {
            
        	showAlert("Invalid Input", "Please enter a valid Question ID.");
        }
    }

    
    private void showAlert(String title, String message) {
        
    	Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
	
}
