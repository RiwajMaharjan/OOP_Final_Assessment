package backend;
import java.sql.*;

public abstract class Competitor {
    protected int competitorId;
    protected Name name;
    protected int age;
    private String level;
    protected int[] scoreArray = new int[5]; // Scores for 5 rounds

    public Competitor(int id, Name name, int age, String level) {
        this.competitorId = id;
        this.name = name;
        this.age = age;
        this.setLevel(level);
    }

    public abstract double getOverallScore();

    public String getFullDetails() {
        return "Full Details for CompetitorID " + competitorId + ":\n" +
               "CompetitorID " + competitorId + ", name " + name.getFullName() + ".\n" +
               name.getFirstName() + " is a " + getLevel() + " and received these scores: " + getScoreString() + ".\n" +
               "This gives her an overall score of " + String.format("%.1f", getOverallScore()) + ".";
    }

    public String getShortDetails() {
        return "Short Details for CompetitorID " + competitorId + ":\n" +
               "CN " + competitorId + " (" + name.getInitials() + ") has an overall score of " + 
               String.format("%.1f", getOverallScore()) + ".";
    }

    private String getScoreString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < scoreArray.length; i++) {
            sb.append(scoreArray[i]);
            if (i < scoreArray.length - 1) sb.append(", ");
        }
        return sb.toString();
    }

    public void setScore(int round, int score) {
        if (round >= 0 && round < 5) scoreArray[round] = score;
    }

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

    public int getCompetitorId() { return competitorId; }
    public int[] getScoreArray() { return scoreArray; }
    public Name getName() { return name; }
    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }
}