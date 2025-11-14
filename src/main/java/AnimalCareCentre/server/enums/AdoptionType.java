package AnimalCareCentre.server.enums;

/**
 * This enum lists all the possible types of ways an animal can be rescued.
 *
 */
public enum AdoptionType {

  NOT_AVAILABLE("Unavailable"),
  FOR_ADOPTION("Adoption"),
  FOR_FOSTER("Foster");

  private final String adoptionType;

  /**
   * Constructor from the AdoptionType
   *
   */
  AdoptionType(String adoptionType) {
    this.adoptionType = adoptionType;
  }

  /**
   * @return the adoption type
   *
   */
  public String toString() {
    return adoptionType;
  }

  /**
   * This method reads a string and returns the respective AdoptionType object
   *
   * @param text
   * @return
   */
  public static AdoptionType fromString(String text) {
    for (AdoptionType a : AdoptionType.values()) {
      if (a.name().equalsIgnoreCase(text)) {
        return a;
      }
    }
    return null;
  }
}
