package application;

/*******
 * <p> Title: ReviewerRequest class. </p>
 * 
 * <p> Description: A class that holds request to be Reviewer. </p>
 * 
 */
public class ReviewerRequest {
    private String studentUsername;
    private String status; // "Pending", "Approved", "Denied"

    /**
   	 * This is the constructor to create a brand new ReviewerRequest
   	 * @param studentUsername Username of the person creating the request
   	 */
    public ReviewerRequest(String studentUsername) {
        this.studentUsername = studentUsername;
        this.status = "Pending";
    }

    /**
	 * Return the username of the request maker
	 * @return The username of the request
	 */
    public String getStudentUsername() { return studentUsername; }
    
    /**
	 * Return the status of the request
	 * @return The status of the request
	 */
    public String getStatus() { return status; }

    /**
   	 * Change the status of the request
   	 * @param status The new status
   	 */
    public void setStatus(String status) {
        this.status = status;
    }
}
