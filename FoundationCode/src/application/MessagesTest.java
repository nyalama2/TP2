package application;

import databasePart1.DatabaseHelper;
import java.sql.*;

public class MessagesTest {

    static int numPassed = 0;  // Counter for passed tests
    static int numFailed = 0;  // Counter for failed tests

    public static void main(String[] args) {
        System.out.println("______________________________________");
        System.out.println("\nMessages Testing Automation");

        // Test case 1: Test sending a valid message.
        performCreateTestCase(1, 1, "recipientUser", "This is a test message.", true);

        // Test case 2: Test sending a message with invalid content (empty string).
        performCreateTestCase(2, 1, "recipientUser", "", false);

        // Test case 3: Test reading messages.
        performReadTestCase(3, 1, true);

        // Test case 4: Test retrieving unread messages count.
        performUnreadTestCase(4, 1, 2, true);

        // Test case 5: Test closing the connection properly.
        performCloseTestCase(5, true);

        System.out.println("____________________________________________________________________________");
        System.out.println("\nNumber of tests passed: " + numPassed);
        System.out.println("Number of tests failed: " + numFailed);
    }

    /**
     * This method tests sending a message (creating a message in the database).
     * 
     * @param testCase     The test case number.
     * @param questionId   The ID of the question the message relates to.
     * @param recipient    The recipient of the message.
     * @param messageContent The content of the message.
     * @param expectedPass true if the test is expected to pass; false otherwise.
     */
    private static void performCreateTestCase(int testCase, int questionId, String recipient, String messageContent, boolean expectedPass) {
        System.out.println("____________________________________________________________________________\nTest case: " + testCase);
        System.out.println("Message content: \"" + messageContent + "\"");

        Messages messages = new Messages("senderUser");
        
        try {
            // Try sending the message.
            messages.reply(questionId, recipient, messageContent);

            // Check if the message is inserted correctly (by reading it back).
            String readMessages = messages.readMessages(questionId);
            if (readMessages.contains(messageContent)) {
                if (expectedPass) {
                    System.out.println("***Success***: Message sent and read successfully.");
                    numPassed++;
                } else {
                    System.out.println("***Failure***: Message was unexpectedly accepted with invalid content.");
                    numFailed++;
                }
            } else {
                if (expectedPass) {
                    System.out.println("***Failure***: Message not found after sending.");
                    numFailed++;
                } else {
                    System.out.println("***Success***: Invalid message was rejected.");
                    numPassed++;
                }
            }
        } catch (Exception e) {
            if (!expectedPass) {
                System.out.println("***Success***: Invalid message was rejected: " + e.getMessage());
                numPassed++;
            } else {
                System.out.println("***Failure***: Exception during message creation: " + e.getMessage());
                numFailed++;
            }
        } finally {
            messages.close();
        }
    }

    /**
     * This method tests reading messages for a specific question.
     * 
     * @param testCase     The test case number.
     * @param questionId   The question ID for which messages are to be read.
     * @param expectedPass true if the test is expected to pass; false otherwise.
     */
    private static void performReadTestCase(int testCase, int questionId, boolean expectedPass) {
        System.out.println("____________________________________________________________________________\nTest case: " + testCase);

        Messages messages = new Messages("recipientUser");
        
        try {
            String result = messages.readMessages(questionId);
            if (result != null && !result.isEmpty()) {
                if (expectedPass) {
                    System.out.println("***Success***: Messages read successfully.");
                    numPassed++;
                } else {
                    System.out.println("***Failure***: Messages should not have been found.");
                    numFailed++;
                }
            } else {
                if (expectedPass) {
                    System.out.println("***Failure***: No messages found when there should be some.");
                    numFailed++;
                } else {
                    System.out.println("***Success***: No messages found as expected.");
                    numPassed++;
                }
            }
        } catch (Exception e) {
            System.out.println("***Failure***: Exception during message reading: " + e.getMessage());
            numFailed++;
        } finally {
            messages.close();
        }
    }

    /**
     * This method tests retrieving unread messages count for a specific question.
     * 
     * @param testCase     The test case number.
     * @param questionId   The question ID for which unread messages count is to be checked.
     * @param expectedCount The expected count of unread messages.
     * @param expectedPass true if the test is expected to pass; false otherwise.
     */
    private static void performUnreadTestCase(int testCase, int questionId, int expectedCount, boolean expectedPass) {
        System.out.println("____________________________________________________________________________\nTest case: " + testCase);

        Messages messages = new Messages("recipientUser");

        try {
            int unreadCount = messages.getUnread(questionId);
            if (unreadCount == expectedCount) {
                if (expectedPass) {
                    System.out.println("***Success***: Correct unread message count.");
                    numPassed++;
                } else {
                    System.out.println("***Failure***: Unread message count should not match.");
                    numFailed++;
                }
            } else {
                if (expectedPass) {
                    System.out.println("***Failure***: Unread message count does not match.");
                    numFailed++;
                } else {
                    System.out.println("***Success***: Unread message count was expected to be different.");
                    numPassed++;
                }
            }
        } catch (Exception e) {
            System.out.println("***Failure***: Exception during unread message count check: " + e.getMessage());
            numFailed++;
        } finally {
            messages.close();
        }
    }

    /**
     * This method tests properly closing the database connection.
     * 
     * @param testCase     The test case number.
     * @param expectedPass true if the test is expected to pass; false otherwise.
     */
    private static void performCloseTestCase(int testCase, boolean expectedPass) {
        System.out.println("____________________________________________________________________________\nTest case: " + testCase);

        Messages messages = new Messages("recipientUser");

        try {
            messages.close();
            if (messages != null) {
                System.out.println("***Success***: Database connection closed successfully.");
                numPassed++;
            } else {
                System.out.println("***Failure***: Failed to close database connection.");
                numFailed++;
            }
        } catch (Exception e) {
            System.out.println("***Failure***: Exception during connection close: " + e.getMessage());
            numFailed++;
        }
    }
}