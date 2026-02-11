package backend;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import database.DBConnection;

/**
 * Controller class to manage database connections and business logic.
 */
public class Manager {

    private static CompetitorList masterList = new CompetitorList();

    public static ArrayList<Competitor> getAllCompetitors() {
        connectAndLoadData();
        return masterList.getCompetitors();
    }

    public static Competitor getCompetitorById(int id) {
        connectAndLoadData(); 
        return masterList.getCompetitorById(id);
    }

    /**
     * Saves or updates a competitor. 
     * Returns true if brand new, false if updated.
     */
    public static boolean saveOrUpdateCompetitor(String fName, String lName, int age, String level, int[] scores) {
        // Direct DB Check
        int existingId = findCompetitorId(fName, lName); 
        boolean isNewPlayer = (existingId == 0);
        
        Name tempName = new Name(fName, lName);
        Competitor tempComp;
        
        // Ensure "Advanced" is handled as "Advance" for database consistency
        String normalizedLevel = level.equalsIgnoreCase("Advanced") ? "Advance" : level;
        
        if (normalizedLevel.equalsIgnoreCase("Advance")) {
            tempComp = new AdvancedCompetitor(0, tempName, age);
        } else if (normalizedLevel.equalsIgnoreCase("Intermediate")) {
            tempComp = new IntermediateCompetitor(0, tempName, age);
        } else {
            tempComp = new BeginnerCompetitor(0, tempName, age);
        }
        
        for(int i=0; i<5; i++) tempComp.setScore(i, scores[i]);
        double weightedTotal = tempComp.getOverallScore();

        String sql;
        if (!isNewPlayer) {
            sql = "UPDATE competitors SET age=?, score1=?, score2=?, score3=?, score4=?, score5=?, level=?, total_score=? WHERE id=?";
        } else {
            sql = "INSERT INTO competitors (first_name, last_name, age, score1, score2, score3, score4, score5, level, total_score) VALUES (?,?,?,?,?,?,?,?,?,?)";
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            if (!isNewPlayer) {
                pstmt.setInt(1, age);
                for (int i = 0; i < 5; i++) pstmt.setInt(i + 2, scores[i]);
                pstmt.setString(7, normalizedLevel);
                pstmt.setDouble(8, weightedTotal);
                pstmt.setInt(9, existingId);
            } else {
                pstmt.setString(1, fName.trim()); 
                pstmt.setString(2, lName.trim()); 
                pstmt.setInt(3, age);
                for (int i = 0; i < 5; i++) pstmt.setInt(i + 4, scores[i]);
                pstmt.setString(9, normalizedLevel); 
                pstmt.setDouble(10, weightedTotal);
            }
            pstmt.executeUpdate();
            
            // Sync local memory list after DB write
            connectAndLoadData();
            return isNewPlayer; 
        } catch (SQLException e) { 
            e.printStackTrace(); 
            return false;
        }
    }

    /**
     * Finds ID using a case-insensitive, space-ignoring search.
     * This is called by Login.java to show the "Welcome Back" message.
     */
    public static int findCompetitorId(String fName, String lName) {
        // Refined SQL to ensure it matches regardless of casing or hidden spaces
        String sql = "SELECT id FROM competitors WHERE LOWER(TRIM(first_name)) = LOWER(TRIM(?)) " +
                     "AND LOWER(TRIM(last_name)) = LOWER(TRIM(?))";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, fName.trim());
            pstmt.setString(2, lName.trim());
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    return id; 
                }
            }
        } catch (SQLException e) { 
            e.printStackTrace(); 
        }
        return 0; // Return 0 if no match found
    }

    public static ArrayList<Questions> getQuestionsByLevel(String level) {
        ArrayList<Questions> qList = new ArrayList<>();
        // Fix: Ensure "Advanced" works for question lookup too
        String lookupLevel = level.equalsIgnoreCase("Advanced") ? "Advance" : level;
        
        String sql = "SELECT * FROM questions WHERE Difficulty = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, lookupLevel);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                qList.add(new Questions(
                    rs.getInt("id"), rs.getString("Question"),      
                    rs.getString("Option A"), rs.getString("Option B"),    
                    rs.getString("Option C"), rs.getString("Option D"),    
                    rs.getString("Correct Option"), rs.getString("Difficulty")   
                ));
            }
            Collections.shuffle(qList);
        } catch (SQLException e) { e.printStackTrace(); }
        return qList;
    }

    public static void connectAndLoadData() {
        masterList = new CompetitorList(); 
        String sql = "SELECT * FROM competitors";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Name nameObj = new Name(rs.getString("first_name"), rs.getString("last_name"));
                int id = rs.getInt("id"), age = rs.getInt("age");
                String level = rs.getString("level");
                
                Competitor comp;
                if (level.equalsIgnoreCase("Advance") || level.equalsIgnoreCase("Advanced")) 
                    comp = new AdvancedCompetitor(id, nameObj, age);
                else if (level.equalsIgnoreCase("Intermediate")) 
                    comp = new IntermediateCompetitor(id, nameObj, age);
                else 
                    comp = new BeginnerCompetitor(id, nameObj, age);
                
                for (int i = 0; i < 5; i++) comp.setScore(i, rs.getInt("score" + (i + 1)));
                masterList.addCompetitor(comp);
            }
        } catch (SQLException e) { e.printStackTrace(); }
    }
}