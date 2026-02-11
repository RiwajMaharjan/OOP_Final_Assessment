package backend;

public class BeginnerCompetitor extends Competitor {
    public BeginnerCompetitor(int id, Name name, int age) { super(id, name, age, "Beginner"); }
    @Override
    public double getOverallScore() {
        int total = 0;
        for (int s : scoreArray) total += s;
        return total / 5.0; // Simple Average
    }
}