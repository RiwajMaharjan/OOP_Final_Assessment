package backend;

public class AdvancedCompetitor extends Competitor {
    public AdvancedCompetitor(int id, Name name, int age) { super(id, name, age, "Advance"); }
    @Override
    public double getOverallScore() {
        int total = 0;
        for (int s : scoreArray) total += s;
        return (total / 5.0) * 1.5; 
    }
}