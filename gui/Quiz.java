package gui;

import java.awt.EventQueue;
import java.awt.Font;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import backend.Manager;
import backend.Questions;

public class Quiz extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	
	private ArrayList<Questions> allQuizQuestions; 
	private int currentRound = 0; 
	private int currentQuestionInRound = 0; 
	private int roundAccumulator = 0; 
	private int[] sessionScores = new int[5]; 
	
	private String playerLevel;
	private String fName, lName;
	private int playerAge, playerId;

	private JLabel lblQuestion, lblRound, lblSubQuestion;
	private JRadioButton rbOption1, rbOption2, rbOption3, rbOption4;
	private ButtonGroup optionsGroup = new ButtonGroup();
	private JButton btnNext;

	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				Quiz frame = new Quiz();
				frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	public Quiz() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 600); 
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null); 
		
		lblRound = new JLabel("Round: 1 / 5");
		lblRound.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblRound.setBounds(50, 20, 150, 25);
		contentPane.add(lblRound);

		lblSubQuestion = new JLabel("Question: 1 / 5");
		lblSubQuestion.setBounds(50, 50, 150, 20);
		contentPane.add(lblSubQuestion);

		lblQuestion = new JLabel("The question text will load here.");
		lblQuestion.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblQuestion.setBounds(50, 80, 700, 40);
		contentPane.add(lblQuestion);

		rbOption1 = new JRadioButton("Option 1");
		rbOption1.setFont(new Font("Tahoma", Font.PLAIN, 16));
		rbOption1.setBounds(70, 150, 297, 30);
		optionsGroup.add(rbOption1);
		contentPane.add(rbOption1);

		rbOption2 = new JRadioButton("Option 2");
		rbOption2.setFont(new Font("Tahoma", Font.PLAIN, 16));
		rbOption2.setBounds(399, 150, 297, 30);
		optionsGroup.add(rbOption2);
		contentPane.add(rbOption2);

		rbOption3 = new JRadioButton("Option 3");
		rbOption3.setFont(new Font("Tahoma", Font.PLAIN, 16));
		rbOption3.setBounds(70, 270, 305, 30);
		optionsGroup.add(rbOption3);
		contentPane.add(rbOption3);

		rbOption4 = new JRadioButton("Option 4");
		rbOption4.setFont(new Font("Tahoma", Font.PLAIN, 16));
		rbOption4.setBounds(399, 270, 297, 30);
		optionsGroup.add(rbOption4);
		contentPane.add(rbOption4);

		btnNext = new JButton("Submit Answer");
		btnNext.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnNext.setBounds(254, 360, 200, 50);
		contentPane.add(btnNext);

		btnNext.addActionListener(e -> handleAnswerSubmission());
		setLocationRelativeTo(null);
	}

	public Quiz(String fName, String lName, int age, String level, int id) {
		this(); 
		this.fName = fName;
		this.lName = lName;
		this.playerAge = age;
		this.playerId = id;

		if (level != null && level.equalsIgnoreCase("Advanced")) {
			this.playerLevel = "Advance";
		} else {
			this.playerLevel = level;
		}
		
		this.allQuizQuestions = Manager.getQuestionsByLevel(this.playerLevel);
		
		if (allQuizQuestions == null || allQuizQuestions.isEmpty()) {
			JOptionPane.showMessageDialog(this, "No questions found for level: " + playerLevel);
		}
		
		displayCurrentQuestion();
	}

	private void displayCurrentQuestion() {
		int overallIndex = (currentRound * 5) + currentQuestionInRound;
		
		if (allQuizQuestions != null && overallIndex < allQuizQuestions.size()) {
			Questions q = allQuizQuestions.get(overallIndex);
			lblRound.setText("Round: " + (currentRound + 1) + " / 5");
			lblSubQuestion.setText("Question: " + (currentQuestionInRound + 1) + " / 5");
			lblQuestion.setText(q.getQuestionText());
			
			String[] opts = q.getOptions();
			rbOption1.setText(opts[0]);
			rbOption2.setText(opts[1]);
			rbOption3.setText(opts[2]);
			rbOption4.setText(opts[3]);
			
			optionsGroup.clearSelection();
		}
	}

	private void handleAnswerSubmission() {
		if (optionsGroup.getSelection() == null) {
			JOptionPane.showMessageDialog(this, "Please select an answer!");
			return;
		}

		int overallIndex = (currentRound * 5) + currentQuestionInRound;
		Questions q = allQuizQuestions.get(overallIndex);
		String selectedText = "";

		if (rbOption1.isSelected()) selectedText = rbOption1.getText();
		else if (rbOption2.isSelected()) selectedText = rbOption2.getText();
		else if (rbOption3.isSelected()) selectedText = rbOption3.getText();
		else if (rbOption4.isSelected()) selectedText = rbOption4.getText();

		if (selectedText.equals(q.getCorrectAnswer())) {
			roundAccumulator++; 
		}

		currentQuestionInRound++;

		if (currentQuestionInRound < 5) {
			displayCurrentQuestion();
		} else {
			sessionScores[currentRound] = roundAccumulator;
			JOptionPane.showMessageDialog(this, "Round " + (currentRound + 1) + " Complete!\nScore: " + roundAccumulator + "/5");
			
			currentRound++;
			currentQuestionInRound = 0; 
			roundAccumulator = 0;      
			
			if (currentRound < 5) {
				displayCurrentQuestion(); 
			} else {
				processFinalResults(); 
			}
		}
	}

	private void processFinalResults() {
		// Logic: Save data and capture if it's a new registration or an update
		boolean isNew = Manager.saveOrUpdateCompetitor(fName, lName, playerAge, playerLevel, sessionScores);
		
		int grandTotal = 0;
		for(int s : sessionScores) grandTotal += s;
		
		String message = isNew ? "New player registered successfully!" : "Welcome back! Your scores have been updated.";
		
		JOptionPane.showMessageDialog(this, "Quiz Complete!\n" + message + "\nTotal Correct: " + grandTotal + " / 25");
		this.dispose();
		new Dashboard().setVisible(true);
	}
}