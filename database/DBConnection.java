package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class DBConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/CompetitionDB";
    private static final String USER = "root";       
    private static final String PASSWORD = "";        

    /**
     * Returns a JDBC connection to the CompetitionDB database.
     * Includes error handling to prevent application crashes.
     */
    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "MySQL Driver not found!");
            return null;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Could not connect to MySQL. Ensure XAMPP is running.");
            return null;
        }
    }
}