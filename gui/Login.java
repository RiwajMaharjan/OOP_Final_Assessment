package gui;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JComboBox;
import backend.Manager;

/**
 * Provides a login and registration interface for competitors.
 * Validates user input and checks for existing records in the database.
 * * @author Riwaj Maharjan
 * @version 1.0
 */
public class Login extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField txtFirstName;
    private JTextField txtLastName;
    private JTextField txtAge;
    private JComboBox<String> comboLevel;

    /**
     * Launches the Login application window.
     * @param args command line arguments
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Login frame = new Login();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Initializes the login frame and its UI components.
     */
    public Login() {
        setTitle("Player Registration");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 450);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblFName = new JLabel("First Name:");
        lblFName.setBounds(50, 40, 100, 25);
        contentPane.add(lblFName);

        txtFirstName = new JTextField();
        txtFirstName.setBounds(180, 40, 180, 25);
        contentPane.add(txtFirstName);

        JLabel lblLName = new JLabel("Last Name:");
        lblLName.setBounds(50, 90, 100, 25);
        contentPane.add(lblLName);

        txtLastName = new JTextField();
        txtLastName.setBounds(180, 90, 180, 25);
        contentPane.add(txtLastName);

        JLabel lblAge = new JLabel("Age:");
        lblAge.setBounds(50, 140, 100, 25);
        contentPane.add(lblAge);

        txtAge = new JTextField();
        txtAge.setBounds(180, 140, 180, 25);
        contentPane.add(txtAge);

        JLabel lblLevel = new JLabel("Select Level:");
        lblLevel.setBounds(50, 190, 100, 25);
        contentPane.add(lblLevel);

        comboLevel = new JComboBox<>(new String[]{"Beginner", "Intermediate", "Advance"});
        comboLevel.setBounds(180, 190, 180, 25);
        contentPane.add(comboLevel);

        JButton btnStart = new JButton("Start Quiz");
        btnStart.setBounds(240, 270, 120, 40);
        contentPane.add(btnStart);

        JButton btnBack = new JButton("Back");
        btnBack.setBounds(70, 270, 120, 40);
        contentPane.add(btnBack);

        btnStart.addActionListener(e -> startQuizProcess());

        btnBack.addActionListener(e -> {
            new Dashboard().setVisible(true);
            dispose();
        });

        setLocationRelativeTo(null);
    }

    /**
     * Processes user input, validates fields, and initiates the quiz session.
     * Checks for existing competitor IDs to distinguish new and returning players.
     */
    private void startQuizProcess() {
        String fName = txtFirstName.getText().trim();
        String lName = txtLastName.getText().trim();
        String ageStr = txtAge.getText().trim();
        String level = (String) comboLevel.getSelectedItem();

        if (fName.isEmpty() || lName.isEmpty() || ageStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields!");
            return;
        }

        try {
            int age = Integer.parseInt(ageStr);
            
            if (age <= 0 ) {
                JOptionPane.showMessageDialog(this, "Please enter a valid age.");
                return;
            }
            
            // Refresh DB data and check for ID
            Manager.connectAndLoadData();
            int existingId = Manager.findCompetitorId(fName, lName);
            
            if (existingId > 0) {
                JOptionPane.showMessageDialog(this, "Welcome back! ID: " + existingId);
            } else {
                JOptionPane.showMessageDialog(this, "New Player Identified! Registering now.");
            }

            // Load questions for the selected level to verify content exists
            if (Manager.getQuestionsByLevel(level).isEmpty()) {
                JOptionPane.showMessageDialog(this, "Error: No questions found for " + level + " level.");
                return;
            }

            Quiz quizFrame = new Quiz(fName, lName, age, level, existingId);
            quizFrame.setVisible(true);
            this.dispose();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Age must be a number!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "An unexpected error occurred: " + e.getMessage());
        }
    }
}