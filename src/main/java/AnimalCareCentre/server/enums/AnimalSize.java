package AnimalCareCentre.server.enums;

/**
 * This enum lists all the possible sizes of an Animal.
 *
 */
public enum AnimalSize {

  SMALL("Small"),
  MEDIUM("Medium"),
  LARGE("Large");

  private final String animalSize;

  /**
   * Constructor from the AnimalSize
   *
   */
  AnimalSize(String animalSize) {
    this.animalSize = animalSize;
  }

  /**
   * @return the size
   *
   */
  public String toString() {
    return animalSize;
  }

  /**
   * This method reads a string and returns the respective AnimalSize object
   *
   * @param text
   * @return
   */
  public static AnimalSize fromString(String text) {
    for (AnimalSize a : AnimalSize.values()) {
      if (a.animalSize.equals(text)) {
        return a;
      }
    }
    return null;
  }
}
