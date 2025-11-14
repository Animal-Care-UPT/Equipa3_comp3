package AnimalCareCentre.server.enums;

/**
 * This enum lists all the possible colors of an Animal.
 *
 */
public enum AnimalColor {

  BROWN("Brown"),
  BLACK("Black"),
  GRAY("Gray"),
  WHITE("White"),
  BEIGE("Beige"),
  GOLDEN("Golden"),
  SPOTTED("Spoted"),
  ALBINO("Albino");

  // many more to come i guess

  private final String animalColor;

  /**
   * Constructor from the AnimalColor
   *
   */
  AnimalColor(String animalColor) {
    this.animalColor = animalColor;
  }

  /**
   * @return the color
   *
   */
  public String toString() {
    return animalColor;
  }

  /**
   * This method reads a string and returns the respective AnimalColor object
   *
   * @param text
   * @return
   */
  public static AnimalColor fromString(String text) {
    for (AnimalColor a : AnimalColor.values()) {
      if (a.animalColor.equals(text)) {
        return a;
      }
    }
    return null;
  }
}


