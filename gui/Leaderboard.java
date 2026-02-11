package gui;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Color;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import backend.Competitor;
import backend.Manager;

public class Leaderboard extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable table;
	private DefaultTableModel model;
	private JComboBox<String> comboFilter;
	private JLabel lblSummary; 
	private JLabel lblSummaryHeading; // New separate label for the heading

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Leaderboard frame = new Leaderboard();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Leaderboard() {
		setTitle("Quiz Leaderboard & Statistics");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 650); 
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null); 

		JLabel lblTitle = new JLabel("Global Leaderboard");
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setFont(new Font("Tahoma", Font.BOLD, 30));
		lblTitle.setBounds(12, 12, 764, 40);
		contentPane.add(lblTitle);

		JLabel lblFilter = new JLabel("Filter by Level:");
		lblFilter.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblFilter.setBounds(261, 67, 100, 25);
		contentPane.add(lblFilter);

		comboFilter = new JComboBox<>(new String[] {"All", "Beginner", "Intermediate", "Advance"});
		comboFilter.setBounds(379, 67, 150, 25);
		contentPane.add(comboFilter);

		String[] columnNames = {"ID", "Full Name", "Level", "S1", "S2", "S3", "S4", "S5", "Weighted Total"};
		model = new DefaultTableModel(columnNames, 0) {
			private static final long serialVersionUID = 1L;
			@Override
			public boolean isCellEditable(int row, int column) {
				return false; 
			}
		};
		
		table = new JTable(model);
		table.getTableHeader().setReorderingAllowed(false);
		table.setFont(new Font("Tahoma", Font.PLAIN, 13));
		table.setRowHeight(22);
		
		table.getColumnModel().getColumn(0).setPreferredWidth(40);
		table.getColumnModel().getColumn(1).setPreferredWidth(160);
		for(int i=3; i<=7; i++) table.getColumnModel().getColumn(i).setPreferredWidth(35);

		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(49, 124, 725, 230);
		contentPane.add(scrollPane);

		// SEPARATE HEADING LABEL
		lblSummaryHeading = new JLabel("Statistical Summary:");
		lblSummaryHeading.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblSummaryHeading.setBounds(166, 366, 200, 25);
		contentPane.add(lblSummaryHeading);

		// STATISTICAL DATA LABEL (Below Heading)
		lblSummary = new JLabel();
		lblSummary.setVerticalAlignment(SwingConstants.TOP);
		lblSummary.setFont(new Font("Tahoma", Font.PLAIN, 14)); 
		lblSummary.setBounds(166, 395, 610, 130);
		contentPane.add(lblSummary);

		JButton btnBack = new JButton("Back to Dashboard");
		btnBack.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnBack.setBounds(329, 534, 200, 45);
		contentPane.add(btnBack);

		comboFilter.addActionListener(e -> updateTableAndStats());
		btnBack.addActionListener(e -> {
			new Dashboard().setVisible(true);
			dispose();
		});

		updateTableAndStats();
		setLocationRelativeTo(null);
	}

	private void updateTableAndStats() {
		model.setRowCount(0);
		String selectedLevel = (String) comboFilter.getSelectedItem();
		ArrayList<Competitor> players = Manager.getAllCompetitors();
		
		int totalCompetitors = 0;
		Competitor topPerformer = null;
		Map<Integer, Integer> freqMap = new TreeMap<>(); 

		for (Competitor c : players) {
			if (selectedLevel.equals("All") || c.getLevel().equalsIgnoreCase(selectedLevel)) {
				int[] s = c.getScoreArray();
				model.addRow(new Object[]{
					c.getCompetitorId(), c.getName().getFullName(), c.getLevel(),
					s[0], s[1], s[2], s[3], s[4], String.format("%.2f", c.getOverallScore())
				});

				totalCompetitors++;
				if (topPerformer == null || c.getOverallScore() > topPerformer.getOverallScore()) {
					topPerformer = c;
				}
				for (int score : s) {
					freqMap.put(score, freqMap.getOrDefault(score, 0) + 1);
				}
			}
		}

		StringBuilder html = new StringBuilder("<html>");
		html.append("Total number of competitors: ").append(totalCompetitors).append("<br>");
		
		if (topPerformer != null) {
			html.append("Competitor with the highest score: ")
			    .append(topPerformer.getName().getFullName())
			    .append(" with an overall score of ")
			    .append(String.format("%.1f", topPerformer.getOverallScore())).append("<br>");
		}

		html.append("Frequency of individual scores:<br>");
		
		StringBuilder scoresStr = new StringBuilder();
		StringBuilder freqStr = new StringBuilder();
		
		for (Map.Entry<Integer, Integer> entry : freqMap.entrySet()) {
			scoresStr.append(entry.getKey()).append("&nbsp;&nbsp;");
			freqStr.append(entry.getValue()).append("&nbsp;&nbsp;");
		}

		html.append("Score: ").append(scoresStr).append("<br>");
		html.append("Frequency: ").append(freqStr).append("</html>");
		
		lblSummary.setText(html.toString());
	}
}