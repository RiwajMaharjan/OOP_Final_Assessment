package backend;

import java.util.ArrayList;

/**
 * CompetitorList Class
 * Manages a collection of Competitor objects and provides utility methods
 * for searching, retrieving top performers, and generating summary data.
 * * @author YourName
 * @version 1.0
 */
public class CompetitorList {
    
    /** Collection of all competitors loaded from the database */
    private ArrayList<Competitor> competitors;

    /**
     * Constructs a new empty CompetitorList.
     */
    public CompetitorList() {
        this.competitors = new ArrayList<>();
    }

    /**
     * Adds a Competitor object to the internal list.
     * @param c The Competitor object to add.
     */
    public void addCompetitor(Competitor c) {
        if (c != null) {
            competitors.add(c);
        }
    }

    /**
     * Retrieves the full list of competitors.
     * @return ArrayList of Competitor objects.
     */
    public ArrayList<Competitor> getCompetitors() {
        return competitors;
    }

    /**
     * Identifies the competitor with the highest overall weighted score.
     * Fulfills the "Details of the top performer" requirement.
     * @return The Competitor with the maximum score, or null if list is empty.
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

    /**
     * Searches for a competitor by ID and returns condensed details.
     * Fulfills the user interaction requirement for ID searching.
     * @param id The unique database ID to search for.
     * @return A string containing initials and final score, or a not found message.
     */
    public String getShortDetails(int id) {
        for (Competitor c : competitors) {
            if (c.competitorId == id) {
                return "ID " + id + ": " + c.getName().getInitials() + 
                       " | Overall Score: " + String.format("%.2f", c.getOverallScore());
            }
        }
        return "Competitor ID " + id + " not found.";
    }
}