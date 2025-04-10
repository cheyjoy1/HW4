package application;
import java.sql.SQLException;
/**
 * PrivateMessagingSystem.java
 * Manages private messages between students and reviewers.
 */
import java.util.ArrayList;
import java.util.List;

import databasePart1.DatabaseHelper;

/**
 * Represents a private messaging system allowing students and reviewers to communicate.
 */
public class PrivateMessagingSystem {
    private List<Message> messages;
    private static final DatabaseHelper databaseHelper = new DatabaseHelper();

    /**
     * Constructor to initialize the messaging system.
     */
    public PrivateMessagingSystem() {
        this.messages = new ArrayList<>();
    }

    /**
     * Sends a message from one user to another.
     * @param sender The user sending the message.
     * @param recipient The user receiving the message.
     * @param content The content of the message.
     * @throws SQLException if a database access error occurs or the SQL statement is invalid.
     */
    public void sendMessage(User sender, User recipient, String content) throws SQLException {
    	databaseHelper.connectToDatabase();
    	String senderUser = sender.getUserName();
    	String recipientUser = recipient.getUserName();
        Message newMessage = new Message(senderUser, recipientUser, content);
        messages.add(newMessage);
        databaseHelper.storeMessage(newMessage);
    }

    /**
     * Retrieves all messages between two users.
     * @param user1 The first user.
     * @param user2 The second user.
     * @return List of messages exchanged between user1 and user2.
     */
    public List<Message> getMessagesBetween(User user1, User user2) {
        List<Message> conversation = new ArrayList<>();
        for (Message msg : messages) {
        	String senderUser = user1.getUserName();
        	String recipientUser = user2.getUserName();
            if ((msg.getSender().equals(senderUser) && msg.getRecipient().equals(recipientUser)) ||
                (msg.getSender().equals(recipientUser) && msg.getRecipient().equals(senderUser))) {
                conversation.add(msg);
            }
        }
        return conversation;
    }
}

