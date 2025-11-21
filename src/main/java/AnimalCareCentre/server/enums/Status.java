package AnimalCareCentre.server.enums;

/**
 * This enum lists all the possible states a request, shelter or animal can have.
 *
 */
public enum Status {

  PENDING("Pending"),
  ACCEPTED("Accepted"),
  REJECTED("Rejected"),
  CANCELLED("Cancelled"),
  AVAILABLE("Available"),
  UNAVAILABLE("Unavailable"),
  BANNED("BANNED");

  private final String status;

  /**
   * Constructor from the Status
   *
   */
  Status(String status) {
    this.status = status;
  }

  /**
   * @return the status
   *
   */
  public String toString() {
    return status;
  }

  /**
   * This method reads a string and returns the respective Status object
   *
   * @param text
   * @return
   */
  public static Status fromString(String text) {
    for (Status g : Status.values()) {
      if (g.name().equalsIgnoreCase(text)) {
        return g;
      }
    }
    return null;
  }
}
