import org.junit.jupiter.api.*;
import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class StaffDAOTest {

    private Connection conn;
    private StaffDAO staffDAO;

    @BeforeEach:
    	
    public void setUp() throws SQLException {
        conn = DriverManager.getConnection("jdbc:sqlite:test.db");
        conn.setAutoCommit(false); // rollback after each test
        staffDAO = new StaffDAO(conn);
    }

    @AfterEach
    public void tearDown() throws SQLException {
        conn.rollback(); // undo changes
        conn.close();
    }

    @Test

    
    public void testGetReviewerRequests() throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.executeUpdate("INSERT INTO Users (id, username, isReviewerRequested, isReviewerApproved) VALUES (101, 'testuser', 1, 0)");

        List<User> requests = staffDAO.getReviewerRequests();

        assertFalse(requests.isEmpty());
        assertEquals("testuser", requests.get(0).getUsername());
    }

    @Test
    public void testApproveReviewer() throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.executeUpdate("INSERT INTO Users (id, username, isReviewerRequested, isReviewerApproved) VALUES (102, 'approveMe', 1, 0)");

        staffDAO.approveReviewer(102);

        PreparedStatement ps = conn.prepareStatement("SELECT isReviewerApproved FROM Users WHERE id = ?");
        ps.setInt(1, 102);
        ResultSet rs = ps.executeQuery();

        assertTrue(rs.next());
        assertEquals(1, rs.getInt("isReviewerApproved"));
    }

    @Test
    public void testRejectReviewer() throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.executeUpdate("INSERT INTO Users (id, username, isReviewerRequested, isReviewerApproved) VALUES (103, 'rejectMe', 1, 0)");

        staffDAO.rejectReviewer(103);

        PreparedStatement ps = conn.prepareStatement("SELECT isReviewerRequested, isReviewerApproved FROM Users WHERE id = ?");
        ps.setInt(1, 103);
        ResultSet rs = ps.executeQuery();

        assertTrue(rs.next());
        assertEquals(0, rs.getInt("isReviewerRequested"));
        assertEquals(0, rs.getInt("isReviewerApproved"));
    }

    @Test
    public void testRevokeReviewer() throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.executeUpdate("INSERT INTO Users (id, username, isReviewerRequested, isReviewerApproved) VALUES (104, 'revokeMe', 1, 1)");

        staffDAO.revokeReviewer(104);

        PreparedStatement ps = conn.prepareStatement("SELECT isReviewerApproved FROM Users WHERE id = ?");
        ps.setInt(1, 104);
        ResultSet rs = ps.executeQuery();

        assertTrue(rs.next());
        assertEquals(0, rs.getInt("isReviewerApproved"));
    }

    @Test
    public void testGetReviewerActivity() throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.executeUpdate("INSERT INTO Users (id, username, isReviewerRequested, isReviewerApproved) VALUES (105, 'activeReviewer', 1, 1)");
        stmt.executeUpdate("INSERT INTO Reviews (id, reviewer_id, content, created_at) VALUES (201, 105, 'Looks good!', CURRENT_TIMESTAMP)");

        List<Review> activity = staffDAO.getReviewerActivity(105);

        assertFalse(activity.isEmpty());
        assertEquals("Looks good!", activity.get(0).getContent());
    }
}
