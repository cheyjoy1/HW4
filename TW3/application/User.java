package application;

/**
 * The User class represents a user entity in the system.
 * It contains the user's details such as userName, password, and role.
 */
public class User {
    private String userName;
    private String password;
    private String role;
    private String email;
    private String name;

    /**
     * Constructor to initialize a new User object with userName, password, and role.
     * @param userName The user's username
     * @param password The user's password
     * @param role The user's roles
     * @param email The user's email
     * @param name The user's name
     */
    public User(String userName, String password, String role, String email, String name) {
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.name = name;
        this.role = role;  //admin, student, instructor, staff, and reviewer
    }
    
    /**
     * Sets the role of the user.
     * @param role The user's new roles
     */
    public void setRole(String role) {
    	this.role=role;
    }
    
    /**
	 * Returns the user's username
	 * @return the user's username
	 */
    public String getUserName() { return userName; }
    
    /**
	 * Returns the user's password
	 * @return the user's password
	 */
    public String getPassword() { return password; }
    
    /**
	 * Returns the user's role
	 * @return the user's role
	 */
    public String getRole() { return role; }
    
    /**
	 * Returns the user's email
	 * @return the user's email
	 */
    public String getEmail() { return email; }
    
    /**
	 * Returns the user's name
	 * @return the user's name
	 */
    public String getName() { return name; }

}
