package application;

import databasePart1.DatabaseHelper;
import java.sql.SQLException;

public class MessagesTest {

	static int numPassed = 0; // Counter for passed tests
    static int numFailed = 0; // Counter for failed tests

    public static void main(String[] args) {
        System.out.println("______________________________________");
        System.out.println("\nStudent Messaging Testing Automation");

        // Test case 1: Retrieve unread messages count for a student
        performUnreadMessagesTest(1, 1, 3); // Expecting 3 unread messages
        
        // Test case 2: Retrieve messages for a given question
        performRetrieveMessagesTest(2, 1, true);
        
        // Test case 3: Send a reply message
        performSendMessageTest(3, 1, "teacher1", "Hello, this is a test reply.", true);
        
        System.out.println("____________________________________________________________________________");
        System.out.println("\nNumber of tests passed: " + numPassed);
        System.out.println("Number of tests failed: " + numFailed);
    }

    /**
     * This method tests getting the count of unread messages.
     * 
     * @param testCase       The test case number.
     * @param questionId     The question ID for which unread messages are checked.
     * @param expectedCount  The expected number of unread messages.
     */
    private static void performUnreadMessagesTest(int testCase, int questionId, int expectedCount) {
        System.out.println("____________________________________________________________________________\nTest case: " + testCase + " (Unread Messages Test)");
        System.out.println("Checking unread messages for question ID: " + questionId);

        DatabaseHelper dbHelper = new DatabaseHelper();
        try {
            dbHelper.connectToDatabase();
            Messages messaging = new Messages("testStudent");

            int unreadCount = messaging.getUnread(questionId);

            if (unreadCount == expectedCount) {
                System.out.println("***Success***: Unread message count is correct (" + unreadCount + ").");
                numPassed++;
            } else {
                System.out.println("***Failure***: Expected " + expectedCount + " unread messages but got " + unreadCount + ".");
                numFailed++;
            }
        } catch (SQLException e) {
            System.out.println("***Failure***: SQL error: " + e.getMessage());
            numFailed++;
        } finally {
            dbHelper.closeConnection();
        }
    }

    /**
     * This method tests retrieving messages for a given question.
     * 
     * @param testCase       The test case number.
     * @param questionId     The question ID for which messages are retrieved.
     * @param expectedPass   Whether the retrieval is expected to succeed.
     */
    private static void performRetrieveMessagesTest(int testCase, int questionId, boolean expectedPass) {
        System.out.println("____________________________________________________________________________\nTest case: " + testCase + " (Retrieve Messages Test)");
        System.out.println("Retrieving messages for question ID: " + questionId);

        DatabaseHelper dbHelper = new DatabaseHelper();
        try {
            dbHelper.connectToDatabase();
            Messages messaging = new Messages("testStudent");

            String messages = messaging.readMessages(questionId);

            if (!messages.isEmpty()) {
                System.out.println("***Success***: Messages retrieved successfully.");
                numPassed++;
            } else {
                System.out.println(expectedPass ? "***Failure***: No messages retrieved." : "***Success***: No messages as expected.");
                if (expectedPass) numFailed++; else numPassed++;
            }
        } catch (SQLException e) {
            System.out.println("***Failure***: SQL error: " + e.getMessage());
            numFailed++;
        } finally {
            dbHelper.closeConnection();
        }
    }

    /**
     * This method tests sending a message.
     * 
     * @param testCase       The test case number.
     * @param questionId     The question ID for which the message is sent.
     * @param recipient      The recipient of the message.
     * @param content        The message content.
     * @param expectedPass   Whether the send operation is expected to succeed.
     */
    private static void performSendMessageTest(int testCase, int questionId, String recipient, String content, boolean expectedPass) {
        System.out.println("____________________________________________________________________________\nTest case: " + testCase + " (Send Message Test)");
        System.out.println("Sending message to " + recipient + " for question ID: " + questionId);

        DatabaseHelper dbHelper = new DatabaseHelper();
        try {
            dbHelper.connectToDatabase();
            Messages messaging = new Messages("testStudent");

            messaging.reply(questionId, recipient, content);
            
            System.out.println("***Success***: Message sent successfully.");
            numPassed++;
        } catch (SQLException e) {
            System.out.println(expectedPass ? "***Failure***: SQL error: " + e.getMessage() : "***Success***: SQL failure expected.");
            if (expectedPass) numFailed++; else numPassed++;
        } finally {
            dbHelper.closeConnection();
        }
    }
	
}
