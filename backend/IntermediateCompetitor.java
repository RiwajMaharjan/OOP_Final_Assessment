package backend;

public class IntermediateCompetitor extends Competitor {
    public IntermediateCompetitor(int id, Name name, int age) { super(id, name, age, "Intermediate"); }
    @Override
    public double getOverallScore() {
        int total = 0;
        for (int s : scoreArray) total += s;
        return (total / 5.0) * 1.2; // 20% difficulty bonus
    }
}