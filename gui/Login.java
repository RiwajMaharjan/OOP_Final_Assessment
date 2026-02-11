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

public class Login extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField txtFirstName;
    private JTextField txtLastName;
    private JTextField txtAge;
    private JComboBox<String> comboLevel;

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
            
            // 1. Refresh data to ensure we are searching the latest DB entries
            Manager.connectAndLoadData();

            // 2. Search for existing ID by name
            int existingId = Manager.findCompetitorId(fName, lName);
            
            if (existingId > 0) {
                // 3. SHOW THE WELCOME BACK MESSAGE WITH ID
                JOptionPane.showMessageDialog(this, "Welcome back! ID: " + existingId);
            } else {
                // Optional: Let them know they are new
                JOptionPane.showMessageDialog(this, "New Player Identified! Registering now.");
            }

            // 4. Pass the details to the Quiz
            Quiz quizFrame = new Quiz(fName, lName, age, level, existingId);
            quizFrame.setVisible(true);
            this.dispose();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Age must be a number!");
        }
    }
}