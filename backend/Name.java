package backend;

/**
 * Stores and manages a competitor's name.
 */
public class Name {
    private String firstName;
    private String lastName;

    /**
     * Initializes the Name object.
     */
    public Name(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
    
    /**
     * Returns the first name.
     */
    public String getFirstName() { return firstName; }

    /**
     * Returns the last name.
     */
    public String getLastName() { return lastName; }

    /**
     * Updates the first name.
     */
    public void setFirstName(String firstName) { this.firstName = firstName; }

    /**
     * Updates the last name.
     */
    public void setLastName(String lastName) { this.lastName = lastName; }

    /**
     * Returns the full formatted name.
     */
    public String getFullName() {
        return firstName + " " + lastName;
    }

    /**
     * Returns the initials of the first and last name.
     */
    public String getInitials() {
        if (firstName.isEmpty() || lastName.isEmpty()) return "";
        return "" + firstName.charAt(0) + lastName.charAt(0);
    }
}