package backend;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import database.DBConnection;

public class Manager {

    private static CompetitorList masterList = new CompetitorList();

    public static int findCompetitorId(String fName, String lName, int age) {
        String sql = "SELECT id FROM competitors WHERE first_name=? AND last_name=? AND age=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, fName.trim());
            pstmt.setString(2, lName.trim());
            pstmt.setInt(3, age);
            
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return rs.getInt("id");
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static void saveOrUpdateCompetitor(String fName, String lName, int age, String level, int[] scores) {
        int existingId = findCompetitorId(fName, lName, age);
        Name tempName = new Name(fName, lName);
        Competitor tempComp;
        
        if (level.equalsIgnoreCase("Advance")) tempComp = new AdvancedCompetitor(0, tempName, age);
        else if (level.equalsIgnoreCase("Intermediate")) tempComp = new IntermediateCompetitor(0, tempName, age);
        else tempComp = new BeginnerCompetitor(0, tempName, age);
        
        for(int i=0; i<5; i++) tempComp.setScore(i, scores[i]);
        double weightedTotal = tempComp.getOverallScore();

        String sql = (existingId > 0) ? 
            "UPDATE competitors SET score1=?, score2=?, score3=?, score4=?, score5=?, level=?, total_score=? WHERE id=?" :
            "INSERT INTO competitors (first_name, last_name, age, score1, score2, score3, score4, score5, level, total_score) VALUES (?,?,?,?,?,?,?,?,?,?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            if (existingId > 0) {
                for (int i = 0; i < 5; i++) pstmt.setInt(i + 1, scores[i]);
                pstmt.setString(6, level);
                pstmt.setDouble(7, weightedTotal);
                pstmt.setInt(8, existingId);
            } else {
                pstmt.setString(1, fName);
                pstmt.setString(2, lName);
                pstmt.setInt(3, age);
                for (int i = 0; i < 5; i++) pstmt.setInt(i + 4, scores[i]);
                pstmt.setString(9, level);
                pstmt.setDouble(10, weightedTotal);
            }
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Competitor getCompetitorById(int id) {
        connectAndLoadData(); 
        for (Competitor c : masterList.getCompetitors()) {
            if (c.getCompetitorId() == id) return c;
        }
        return null;
    }

    public static ArrayList<Questions> getQuestionsByLevel(String level) {
        ArrayList<Questions> qList = new ArrayList<>();
        String sql = "SELECT * FROM questions WHERE Difficulty = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, level);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                qList.add(new Questions(
                    rs.getInt("id"),
                    rs.getString("Question"),      
                    rs.getString("Option A"),    
                    rs.getString("Option B"),    
                    rs.getString("Option C"),    
                    rs.getString("Option D"),    
                    rs.getString("Correct Option"), 
                    rs.getString("Difficulty")   
                ));
            }
            
            Collections.shuffle(qList);
            if (qList.size() > 25) return new ArrayList<>(qList.subList(0, 25));

        } catch (SQLException e) {
            e.printStackTrace();
        }
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
                if (level.equalsIgnoreCase("Advance")) comp = new AdvancedCompetitor(id, nameObj, age);
                else if (level.equalsIgnoreCase("Intermediate")) comp = new IntermediateCompetitor(id, nameObj, age);
                else comp = new BeginnerCompetitor(id, nameObj, age);

                for (int i = 0; i < 5; i++) {
                    comp.setScore(i, rs.getInt("score" + (i + 1)));
                }
                masterList.addCompetitor(comp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Competitor> getAllCompetitors() {
        connectAndLoadData();
        return masterList.getCompetitors();
    }

    public static void generateFinalReport() {
        connectAndLoadData();
        ArrayList<Competitor> players = masterList.getCompetitors();

        System.out.println("\n========================================================");
        System.out.println("                OFFICIAL COMPETITION REPORT             ");
        System.out.println("========================================================");
        
        for (Competitor c : players) {
            System.out.println(c.getFullDetails());
            System.out.println(c.getShortDetails());
            System.out.println("--------------------------------------------------------");
        }

        System.out.println("Total Participants: " + players.size());
        
        Competitor winner = masterList.getTopPerformer();
        if (winner != null) {
            System.out.println("WINNER: " + winner.getName().getFullName() + " with score " + winner.getOverallScore());
        }
    }
}