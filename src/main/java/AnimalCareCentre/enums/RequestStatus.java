package AnimalCareCentre.enums;

/**
 * This enum lists all the possible states a request can have.
 *
 */
public enum RequestStatus {

    PENDING("Pending"),
    ACCEPTED("Accepted"),
    REJECTED("Rejected"),
    CANCELLED("Cancelled");


    private final String requestStatus;

    /**
     * Constructor from the RequestStatus
     *
     */
    RequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }

    /**
     * @return the status of the request
     *
     */
    public String toString() {
        return requestStatus;
    }

    /**
     * This method reads a string and returns the respective General_Request object
     *
     * @param text
     * @return
     */
    public static RequestStatus fromString(String text) {
        for (RequestStatus g : RequestStatus.values()) {
            if (g.name().equalsIgnoreCase(text)) {
                return g;
            }
        }
        return null;
    }
}



