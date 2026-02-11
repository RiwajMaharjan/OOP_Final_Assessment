package backend;

import java.util.ArrayList;

/**
 * Manages the collection of Competitor objects.
 * This class fulfills the Part Three requirement for a specialized list manager.
 */
public class CompetitorList {
    private ArrayList<Competitor> competitors;

    /**
     * Initializes a new empty list of competitors.
     */
    public CompetitorList() {
        this.competitors = new ArrayList<>();
    }

    /**
     * Adds a competitor to the master list.
     * @param c The Competitor object to be added.
     */
    public void addCompetitor(Competitor c) {
        competitors.add(c);
    }

    /**
     * Returns the full list of competitors.
     * @return ArrayList of all Competitor objects.
     */
    public ArrayList<Competitor> getCompetitors() {
        return competitors;
    }

    /**
     * Finds a competitor by their unique ID.
     * @param id The ID to search for.
     * @return The Competitor object if found, otherwise null.
     */
    public Competitor getCompetitorById(int id) {
        for (Competitor c : competitors) {
            if (c.getCompetitorId() == id) return c;
        }
        return null;
    }

    /**
     * Finds the competitor with the highest overall score.
     * @return The top-performing Competitor.
     */
    public Competitor getTopPerformer() {
        if (competitors.isEmpty()) return null;
        Competitor top = competitors.get(0);
        for (Competitor c : competitors) {
            if (c.getOverallScore() > top.getOverallScore()) {
                top = c;
            }
        }
        return top;
    }
}