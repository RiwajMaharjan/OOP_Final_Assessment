package gui;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import backend.Competitor;
import backend.Manager;

public class PlayerDetails extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtSearchID;
	private JLabel lblDisplay; // Replaced JTextArea with JLabel

	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				PlayerDetails frame = new PlayerDetails();
				frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	public PlayerDetails() {
		setTitle("Competitor Lookup");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 500, 500); 
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null); 

		JLabel lblHeader = new JLabel("Search Player by ID");
		lblHeader.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblHeader.setBounds(140, 20, 250, 30);
		contentPane.add(lblHeader);

		txtSearchID = new JTextField();
		txtSearchID.setBounds(100, 70, 196, 35);
		contentPane.add(txtSearchID);

		JButton btnSearch = new JButton("Search");
		btnSearch.setBounds(329, 70, 100, 35);
		contentPane.add(btnSearch);

		// SIMPLE DISPLAY LABEL
		lblDisplay = new JLabel("");
		lblDisplay.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblDisplay.setVerticalAlignment(SwingConstants.TOP);
		lblDisplay.setBounds(40, 130, 410, 240);
		lblDisplay.setBorder(new LineBorder(Color.LIGHT_GRAY));
		contentPane.add(lblDisplay);

		JButton btnBack = new JButton("Back to Dashboard");
		btnBack.setBounds(150, 400, 180, 40);
		contentPane.add(btnBack);

		btnSearch.addActionListener(e -> {
			try {
				String input = txtSearchID.getText().trim();
				if(input.isEmpty()) return;
				
				int id = Integer.parseInt(input);
				Competitor c = Manager.getCompetitorById(id);
				
				if (c != null) {
					// Use HTML to handle the newlines from your getFullDetails/getShortDetails methods
					String full = c.getFullDetails().replace("\n", "<br>");
					String summary = c.getShortDetails().replace("\n", "<br>");
					
					lblDisplay.setText("<html>" + full + "<br><br>" + summary + "</html>");
				} else {
					lblDisplay.setText("");
					JOptionPane.showMessageDialog(this, "Player not found.");
				}
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(this, "Enter a valid ID.");
			}
		});

		btnBack.addActionListener(e -> {
			new Dashboard().setVisible(true);
			dispose();
		});

		setLocationRelativeTo(null);
	}
}