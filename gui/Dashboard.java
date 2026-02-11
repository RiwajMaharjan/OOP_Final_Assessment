package gui;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

public class Dashboard extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				Dashboard frame = new Dashboard();
				frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	public Dashboard() {
		setTitle("Quiz Management System");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Main Window Size
		setBounds(100, 100, 800, 550); // Reduced height slightly since one button is gone
		contentPane = new JPanel();
		contentPane.setBackground(new Color(245, 245, 245)); 
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		// Welcome Label
		JLabel lblWelcome = new JLabel("Welcome to the Quiz");
		lblWelcome.setHorizontalAlignment(SwingConstants.CENTER);
		lblWelcome.setFont(new Font("Serif", Font.BOLD, 48)); 
		lblWelcome.setBounds(100, 30, 600, 80);
		contentPane.add(lblWelcome);

		// Common button settings
		int btnWidth = 300;
		int btnHeight = 55;
		int startX = 250; 

		// 1. Start Quiz Button
		JButton btnStartQuiz = new JButton("Start Quiz");
		btnStartQuiz.setFont(new Font("Tahoma", Font.PLAIN, 18));
		btnStartQuiz.setBounds(startX, 140, btnWidth, btnHeight);
		btnStartQuiz.addActionListener(e -> {
			new Login().setVisible(true);
			dispose();
		});
		contentPane.add(btnStartQuiz);

		// 2. Leaderboard Button
		JButton btnLeaderboard = new JButton("Leaderboard");
		btnLeaderboard.setFont(new Font("Tahoma", Font.PLAIN, 18));
		btnLeaderboard.setBounds(startX, 215, btnWidth, btnHeight);
		btnLeaderboard.addActionListener(e -> {
			new Leaderboard().setVisible(true);
			dispose();
		});
		contentPane.add(btnLeaderboard);

		// 3. Player Details Button
		JButton btnPlayerDetails = new JButton("Player Details");
		btnPlayerDetails.setFont(new Font("Tahoma", Font.PLAIN, 18));
		btnPlayerDetails.setBounds(startX, 290, btnWidth, btnHeight);
		btnPlayerDetails.addActionListener(e -> {
			new PlayerDetails().setVisible(true);
			dispose();
		});
		contentPane.add(btnPlayerDetails);

	
		JButton btnQuit = new JButton("Quit System");
		btnQuit.setFont(new Font("Tahoma", Font.PLAIN, 18));
		btnQuit.setForeground(new Color(150, 0, 0)); 
		btnQuit.setBounds(startX, 365, btnWidth, btnHeight);
		btnQuit.addActionListener(e -> {
			int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit?", "Exit", JOptionPane.YES_NO_OPTION);
			if (confirm == JOptionPane.YES_OPTION) {
				System.exit(0);
			}
		});
		contentPane.add(btnQuit);
		
		setLocationRelativeTo(null);
	}
}