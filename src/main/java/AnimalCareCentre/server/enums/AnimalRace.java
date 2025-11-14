package AnimalCareCentre.server.enums;

/**
 * This enum lists all the possible races of an Animal.
 *
 */
public enum AnimalRace {

  GOLDEN_RETRIEVER("Golden Retriever"),
  GERMAN_SHEPHERD("German Shepherd");
  // many more to come i guess

  private final String animalRace;

  /**
   * Constructor from the AnimalRace
   *
   */
  AnimalRace(String animalRace) {
    this.animalRace = animalRace;
  }

  /**
   * @return the race
   *
   */
  public String toString() {
    return animalRace;
  }

  /**
   * This method reads a string and returns the respective AnimalRace object
   *
   * @param text
   * @return
   */
  public static AnimalRace fromString(String text) {
    for (AnimalRace a : AnimalRace.values()) {
      if (a.animalRace.equals(text)) {
        return a;
      }
    }
    return null;
  }
}
