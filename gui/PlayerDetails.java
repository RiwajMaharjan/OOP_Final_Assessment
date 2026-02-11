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

/**
 * Provides a user interface to search for competitors by ID.
 * Displays details in the exact format required by the coursework brief.
 */
public class PlayerDetails extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtSearchID;
	private JLabel lblDisplay;

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
		btnSearch.setBounds(320, 70, 100, 35);
		contentPane.add(btnSearch);

		lblDisplay = new JLabel("");
		lblDisplay.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblDisplay.setVerticalAlignment(SwingConstants.TOP);
		lblDisplay.setBounds(40, 130, 410, 240);
		lblDisplay.setBorder(new LineBorder(Color.LIGHT_GRAY));
		contentPane.add(lblDisplay);

		JButton btnBack = new JButton("Back to Dashboard");
		btnBack.setBounds(150, 400, 180, 40);
		contentPane.add(btnBack);

		// Search logic updated to match exact report formatting
		btnSearch.addActionListener(e -> {
			try {
				String input = txtSearchID.getText().trim();
				if (input.isEmpty()) return;
				
				int id = Integer.parseInt(input);
				Competitor c = Manager.getCompetitorById(id);
				
				if (c != null) {
					// Format details to match the example report requirements
					String full = c.getFullDetails().replace("\n", "<br>");
					String summary = c.getShortDetails().replace("\n", "<br>");
					
					StringBuilder sb = new StringBuilder("<html><div style='padding:10px;'>");
					
					// Full Details Header and Content
					sb.append("Full Details for CompetitorID ").append(id).append(":<br>");
					sb.append(full).append("<br><br>");
					
					// Short Details Header and Content
					sb.append("Short Details for CompetitorID ").append(id).append(":<br>");
					sb.append(summary);
					
					sb.append("</div></html>");
					
					lblDisplay.setText(sb.toString());
				} else {
					lblDisplay.setText("");
					JOptionPane.showMessageDialog(this, "Player not found.");
				}
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(this, "Please enter a numeric ID.");
			}
		});

		btnBack.addActionListener(e -> {
			new Dashboard().setVisible(true);
			dispose();
		});

		setLocationRelativeTo(null);
	}
}