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
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import backend.Manager;

public class Dashboard extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Dashboard frame = new Dashboard();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Dashboard() {
		setTitle("Quiz Management System");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Canvas size set to 800x600
		setBounds(100, 100, 800, 600); 
		contentPane = new JPanel();
		contentPane.setBackground(new Color(245, 245, 245)); 
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		// Welcome Label
		JLabel lblWelcome = new JLabel("Welcome to the Quiz");
		lblWelcome.setHorizontalAlignment(SwingConstants.CENTER);
		lblWelcome.setFont(new Font("Serif", Font.BOLD, 48)); 
		lblWelcome.setBounds(100, 50, 600, 80);
		contentPane.add(lblWelcome);

		// Button Dimensions
		int btnWidth = 300;
		int btnHeight = 60;
		int centerX = (800 - btnWidth) / 2; 

		// 1. Start Quiz Button - Points to Login
		JButton btnStartQuiz = new JButton("Start Quiz");
		btnStartQuiz.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnStartQuiz.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Login().setVisible(true);
				dispose();
			}
		});
		btnStartQuiz.setBounds(263, 179, btnWidth, btnHeight);
		contentPane.add(btnStartQuiz);

		// 2. Leaderboard Button - Points to Leaderboard GUI
		JButton btnLeaderboard = new JButton("Leaderboard");
		btnLeaderboard.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnLeaderboard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Leaderboard().setVisible(true);
				dispose();
			}
		});
		btnLeaderboard.setBounds(263, 268, btnWidth, btnHeight);
		contentPane.add(btnLeaderboard);

		// 3. Player Details Button - Points to PlayerDetails GUI
		JButton btnPlayerDetails = new JButton("Player Details");
		btnPlayerDetails.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnPlayerDetails.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new PlayerDetails().setVisible(true);
				dispose();
			}
		});
		btnPlayerDetails.setBounds(263, 348, btnWidth, btnHeight);
		contentPane.add(btnPlayerDetails);

		// 4. Quit Button
		JButton btnQuit = new JButton("Quit");
		btnQuit.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnQuit.setForeground(new Color(150, 0, 0)); 
		btnQuit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit?", "Exit Application", JOptionPane.YES_NO_OPTION);
				if (confirm == JOptionPane.YES_OPTION) {
					System.exit(0);
				}
			}
		});
		btnQuit.setBounds(263, 427, btnWidth, btnHeight);
		contentPane.add(btnQuit);
		
		setLocationRelativeTo(null);
	}
}