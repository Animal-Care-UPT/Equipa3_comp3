package AnimalCareCentre.server.enums;
/**
 * This enum lists the genders of the animal.
 *
 */
public enum AnimalGender {

    FEMALE("Female"),
    MALE("Male");

    private final String gender;

    /**
     * Constructor from the gender
     *
     */
    AnimalGender(String gender) {this.gender = gender;}

    /**
     * @return the gender
     *
     */
    public String toString() {
        return gender;
    }

    /**
     * This method reads a string and returns the respective gender object
     *
     * @param text
     * @return
     */
    public static AnimalGender fromString(String text) {
        for (AnimalGender g : AnimalGender.values()) {
            if (g.name().equalsIgnoreCase(text)) {
                return g;
            }
        }
        return null;
    }

}
