package application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * ReviewService class provides functionality for reviewers to scan their reviews,
 * view private messages from authors, and reply to them.
 */
public class ReviewService {
    
    private List<Review> reviews;
    private Map<String, List<Message>> privateMessages;

    /**
     * Constructor initializes the review service with existing reviews and messages.
     */
    public ReviewService() {
        this.reviews = new ArrayList<>();
        this.privateMessages = new HashMap<>();
    }
    
    /**
     * Retrieves the list of reviews written by a specific reviewer.
     * @param username The username of the reviewer.
     * @return List of reviews written by the reviewer.
     */
    public List<Review> getReviewsByReviewer(String username) {
        return reviews.stream().filter(r -> r.getUsername().equals(username)).collect(Collectors.toList());
    }
    
    /**
     * Retrieves private messages sent ovided.
     * @param usernameStudent The username of student ovided.
     * @return List of messages received.
     */
    public List<Message> getMessagesForReviewer(String usernameStudent) {
        return privateMessages.getOrDefault(usernameStudent, new ArrayList<>());
    }
    
    /**
     * Allows a reviewer to reply to a private message.
     * @param usernameReviewer The username of the reviewer sending the reply.
     * @param usernameStudent The username of the author receiving the reply.
     * @param messageContent The content of the reply message.
     */
    public void replyToMessage(String usernameReviewer, String usernameStudent, String messageContent) {
        Message reply = new Message(usernameReviewer, usernameStudent, messageContent);
        privateMessages.computeIfAbsent(usernameStudent, k -> new ArrayList<>()).add(reply);
    }
}

