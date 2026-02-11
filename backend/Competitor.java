package backend;

import java.sql.*;

/**
 * Abstract base class representing a competitor in the system.
 * Provides core attributes and methods for managing scores and 
 * database.
 * * @author Riwaj Maharjan
 * @version 1.0
 */
public abstract class Competitor {
    protected int competitorId;
    protected Name name;
    protected int age; 
    private String level;
    protected int[] scoreArray = new int[5];

    /**
     * Initializes a competitor with a unique ID, name, age, and skill level.
     * * @param id the unique ID of the competitor
     * @param name the Name object containing first and last names
     * @param age the age of the competitor
     * @param level the skill category of the competitor
     */
    public Competitor(int id, Name name, int age, String level) {
        this.competitorId = id;
        this.name = name;
        this.age = age;
        this.setLevel(level);
    }

    /**
     * Calculates the overall score for the competitor based on their specific level.
     * * @return the calculated overall score as a double
     */
    public abstract double getOverallScore();

    /**
     * Returns a formatted string containing the full details of the competitor,
     * including ID, name, level, age, and overall score.
     * * @return a multi-line string with competitor information
     */
    public String getFullDetails() {
        return "Competitor number " + competitorId + ", name " + name.getFullName() + ".\n" +
               name.getFirstName() + " is a " + getLevel() + " aged " + age + 
               " and has an overall score of " + String.format("%.1f", getOverallScore()) + ".";
    }

    /**
     * Returns a brief summary of the competitor's performance in a single line.
     * * @return a single-line summary string
     */
    public String getShortDetails() {
        return "CN " + competitorId + " (" + name.getInitials() + ") has overall score " + 
               String.format("%.1f", getOverallScore()) + ".";
    }

    /**
     * Helper method to format the score array as a comma-separated string.
     * * @return a string representation of the round scores
     */
    private String getScoreString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < scoreArray.length; i++) {
            sb.append(scoreArray[i]);
            if (i < scoreArray.length - 1) sb.append(", ");
        }
        return sb.toString();
    }

    /**
     * Gets the array containing individual scores for each of the five rounds.
     * * @return an integer array of scores
     */
    public int[] getScoreArray() {
        return scoreArray;
    }

    /**
     * Updates the score for a specific round index.
     * * @param round the round index (0 to 4)
     * @param score the score achieved in the round
     */
    public void setScore(int round, int score) {
        if (round >= 0 && round < 5) scoreArray[round] = score;
    }

    /**
     * Saves the competitor data to the database. Updates an existing record
     * if the competitor ID is greater than zero, otherwise inserts a new record.
     * * @param conn the active database connection
     * @throws SQLException if a database access error occurs
     */
    public void saveCompetitor(Connection conn) throws SQLException {
        double weightedScore = getOverallScore();
        if (this.competitorId > 0) {
            String sql = "UPDATE competitors SET score1=?, score2=?, score3=?, score4=?, score5=?, total_score=? WHERE id=?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                for (int i = 0; i < 5; i++) pstmt.setInt(i + 1, scoreArray[i]);
                pstmt.setDouble(6, weightedScore);
                pstmt.setInt(7, competitorId);
                pstmt.executeUpdate();
            }
        } else {
            String sql = "INSERT INTO competitors (first_name, last_name, age, level, score1, score2, score3, score4, score5, total_score) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, name.getFirstName());
                pstmt.setString(2, name.getLastName());
                pstmt.setInt(3, age);
                pstmt.setString(4, getLevel());
                for (int i = 0; i < 5; i++) pstmt.setInt(i + 5, scoreArray[i]);
                pstmt.setDouble(10, weightedScore);
                pstmt.executeUpdate();
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) this.competitorId = rs.getInt(1);
            }
        }
    }

    /**
     * Gets the unique identification number of the competitor.
     * * @return the competitor ID
     */
    public int getCompetitorId() { return competitorId; }

    /**
     * Gets the Name object associated with the competitor.
     * * @return the Name object
     */
    public Name getName() { return name; }

    /**
     * Gets the current age of the competitor.
     * * @return the competitor's age
     */
    public int getAge() { return age; }

    /**
     * Sets or updates the age of the competitor.
     * * @param age the new age to set
     */
    public void setAge(int age) { this.age = age; }

    /**
     * Gets the skill level category of the competitor.
     * * @return the skill level string
     */
    public String getLevel() { return level; }

    /**
     * Sets or updates the skill level category of the competitor.
     * * @param level the new skill level to set
     */
    public void setLevel(String level) { this.level = level; }
}