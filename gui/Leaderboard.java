package gui;

import java.awt.*;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import backend.Competitor;
import backend.Manager;

/**
 * Handles the generation of the final report and summary statistics.
 */
public class Leaderboard extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable table;
    private DefaultTableModel model;
    private JComboBox<String> comboFilter;
    private JLabel lblSummary; 

    public Leaderboard() {
        setTitle("Competition Report & Statistics");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 800, 650); 
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null); 

        JLabel lblTitle = new JLabel("Competition Final Report");
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setFont(new Font("Tahoma", Font.BOLD, 28));
        lblTitle.setBounds(12, 12, 764, 40);
        contentPane.add(lblTitle);

        comboFilter = new JComboBox<>(new String[] {"All", "Beginner", "Intermediate", "Advance"});
        comboFilter.setBounds(379, 67, 150, 25);
        contentPane.add(comboFilter);

        JLabel lblFilter = new JLabel("Filter Table:");
        lblFilter.setBounds(280, 67, 100, 25);
        contentPane.add(lblFilter);

        String[] columnNames = {"ID", "Name", "Level", "Scores", "Overall"};
        model = new DefaultTableModel(columnNames, 0);
        
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(40, 110, 710, 200);
        contentPane.add(scrollPane);

        JLabel lblHeading = new JLabel("Statistical Summary:");
        lblHeading.setFont(new Font("Tahoma", Font.BOLD, 15));
        lblHeading.setBounds(40, 320, 200, 25);
        contentPane.add(lblHeading);

        lblSummary = new JLabel();
        lblSummary.setVerticalAlignment(SwingConstants.TOP);
        lblSummary.setFont(new Font("Monospaced", Font.PLAIN, 13)); 
        lblSummary.setBounds(40, 350, 710, 180);
        contentPane.add(lblSummary);

        JButton btnBack = new JButton("Back");
        btnBack.setBounds(340, 540, 120, 40);
        contentPane.add(btnBack);

        comboFilter.addActionListener(e -> updateTableAndStats());
        btnBack.addActionListener(e -> {
            new Dashboard().setVisible(true);
            dispose();
        });

        updateTableAndStats();
        setLocationRelativeTo(null);
    }

    /**
     * Generates report text matching the required Example Report style.
     */
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
                String scoresStr = s[0] + " " + s[1] + " " + s[2] + " " + s[3] + " " + s[4];
                
                model.addRow(new Object[]{
                    c.getCompetitorId(), 
                    c.getName().getFullName(), 
                    c.getLevel(),
                    scoresStr,
                    String.format("%.1f", c.getOverallScore())
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

        // Clean text formatting without bullets
        StringBuilder html = new StringBuilder("<html><body style='font-family:Sans-serif;'>");
        html.append("Total number of competitors: ").append(totalCompetitors).append("<br>");
        
        if (topPerformer != null) {
            html.append("Competitor with the highest score: ")
                .append(topPerformer.getName().getFullName())
                .append(" with an overall score of ")
                .append(String.format("%.1f", topPerformer.getOverallScore())).append("<br>");
        }
        
        html.append("Frequency of individual scores:<br>");
        
        // Formatted table-like text for Score/Frequency
        html.append("<pre>");
        StringBuilder scoresLine = new StringBuilder("Score:     ");
        StringBuilder freqLine = new StringBuilder("Frequency: ");
        
        for (Map.Entry<Integer, Integer> entry : freqMap.entrySet()) {
            scoresLine.append(String.format("%-4d", entry.getKey()));
            freqLine.append(String.format("%-4d", entry.getValue()));
        }

        html.append(scoresLine).append("<br>").append(freqLine).append("</pre>");
        html.append("</body></html>");
        
        lblSummary.setText(html.toString());
    }
}