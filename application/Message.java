package application;


/*******
 * <p> Title: Message class. </p>
 * 
 * <p> Description: Represents a Message to reviewer. </p>
 * 
 */
public class Message {
    private String sender;
    private String recipient;
    private String content;

    /**
     * Constructor for Message.
     * @param usernameReviewer The sender of the message.
     * @param usernameStudent The recipient of the message.
     * @param content The content of the message.
     */
    public Message(String usernameReviewer, String usernameStudent, String content) {
        this.sender = usernameReviewer;
        this.recipient = usernameStudent;
        this.content = content;
    }
    
    /**
	 * Creates a string with all the information of the Message
	 * @return The message in a string format
	 */
    public String viewMessage() {
    	String full = "";
        full = "From: "+ sender +" To: "+recipient+"\n"
        		+ content +"\n";
        return full;
    }
    
    /**
   	 * Gets the Sender of a message
   	 * @return The Sender
   	 */
    public String getSender() {
        return sender;
    }

    /**
   	 * Gets the Recipient of a message
   	 * @return The Recipient
   	 */
    public String getRecipient() {
        return recipient;
    }

    /**
   	 * Gets the text of a message
   	 * @return The text
   	 */
    public String getContent() {
        return content;
    }
}

