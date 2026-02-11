package backend;

public class Questions {
    private int id;
    private String questionText;
    private String[] options = new String[4];
    private String correctAnswer;
    private String level;

    public Questions(int id, String text, String a, String b, String c, String d, String answer, String level) {
        this.id = id;
        this.questionText = text;
        this.options[0] = a;
        this.options[1] = b;
        this.options[2] = c;
        this.options[3] = d;
        this.correctAnswer = answer;
        this.level = level;
    }

    public String getQuestionText() { return questionText; }
    public String[] getOptions() { return options; }
    public String getCorrectAnswer() { return correctAnswer; }
}