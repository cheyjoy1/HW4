import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) for handling staff-related operations such as
 * managing reviewer requests and monitoring reviewer activity.
 */
public class StaffDAO {

    private Connection conn;

    /**
     * Constructs a new StaffDAO with the specified database connection.
     *
     * @param conn the database connection to use
     */
    public StaffDAO(Connection conn) {
        this.conn = conn;
    }

    /**
     * Retrieves a list of users who have requested to become reviewers
     * but have not yet been approved.
     *
     * @return a list of User objects representing pending reviewer requests
     * @throws SQLException if a database access error occurs
     */
    public List<User> getReviewerRequests() throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM Users WHERE isReviewerRequested = 1 AND isReviewerApproved = 0";
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            users.add(new User(
                rs.getInt("id"),
                rs.getString("username"),
                rs.getBoolean("isReviewerRequested"),
                rs.getBoolean("isReviewerApproved")
            ));
        }
        return users;
    }

    /**
     * Approves a user to become a reviewer by updating their status in the database.
     *
     * @param userId the ID of the user to approve
     * @throws SQLException if a database access error occurs
     */
    public void approveReviewer(int userId) throws SQLException {
        String sql = "UPDATE Users SET isReviewerApproved = 1 WHERE id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, userId);
        stmt.executeUpdate();
    }

    /**
     * Rejects a user's request to become a reviewer by resetting their request and approval status.
     *
     * @param userId the ID of the user to reject
     * @throws SQLException if a database access error occurs
     */
