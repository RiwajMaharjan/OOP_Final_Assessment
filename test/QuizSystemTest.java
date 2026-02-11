package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import backend.*;
import database.DBConnection;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class QuizSystemTest {

    @BeforeEach
    void setup() {
        // Ensures the masterList is synced with the DB before each test
        Manager.connectAndLoadData();
    }

    @Test
    void testDatabaseConnection() throws SQLException {
        try (Connection conn = DBConnection.getConnection()) {
            assertNotNull(conn, "Database connection should not be null");
            assertFalse(conn.isClosed(), "Database connection should be active");
        }
    }

    @Test
    void testGetQuestionsByLevel() {
        // Testing with "Beginner" as defined in your DB/Manager
        ArrayList<Questions> questions = Manager.getQuestionsByLevel("Beginner");
        assertNotNull(questions, "Question list should not be null");
        
        // If your database has questions, verify the difficulty matches
        if (!questions.isEmpty()) {
            assertEquals("Beginner", questions.get(0).getLevel(), "Question level should match requested level");
        }
    }

    @Test
    void testBeginnerScoreCalculation() {
        Name name = new Name("Test", "Beginner");
        BeginnerCompetitor beginner = new BeginnerCompetitor(0, name, 20);
        
        // Setting scores: 5, 4, 3, 2, 1 (Average = 3.0)
        beginner.setScore(0, 5); 
        beginner.setScore(1, 4); 
        beginner.setScore(2, 3); 
        beginner.setScore(3, 2); 
        beginner.setScore(4, 1); 
        
        // Beginner overall score is typically a straight average
        assertEquals(3.0, beginner.getOverallScore(), 0.01);
    }

    @Test
    void testAdvancedScoreCalculation() {
        Name name = new Name("Expert", "User");
        AdvancedCompetitor advanced = new AdvancedCompetitor(0, name, 25);
        
        // Setting perfect scores: 5, 5, 5, 5, 5 (Average = 5.0)
        advanced.setScore(0, 5); 
        advanced.setScore(1, 5);
        advanced.setScore(2, 5);
        advanced.setScore(3, 5);
        advanced.setScore(4, 5);
        
        // Based on your previous logic (5.0 average * 1.5 weighted multiplier)
        assertEquals(7.5, advanced.getOverallScore(), 0.01);
    }

    @Test
    void testFindCompetitorId() {
        int id = Manager.findCompetitorId("NonExistentPlayer", "NoRecordFound");
        
        // Should return 0 if the player does not exist in the database
        assertEquals(0, id, "ID should be 0 for non-existent users");
    }

    @Test
    void testGetCompetitorById() {
        // Searching for an ID that is highly unlikely to exist
        Competitor c = Manager.getCompetitorById(-1);
        assertNull(c, "Should return null for an invalid ID");
    }
}