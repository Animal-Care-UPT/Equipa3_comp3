package AnimalCareCentre.server.enums;

/**
 * This enum lists all the districts of Portugal alphabetically with their
 * coordinates.
 *
 */
public enum District {

  AVEIRO("Aveiro", 40.6405, -8.6538),
  BEJA("Beja", 38.0150, -7.8650),
  BRAGA("Braga", 41.5454, -8.4265),
  BRAGANCA("Bragança", 41.8058, -6.7570),
  CASTELO_BRANCO("Castelo Branco", 39.8187, -7.4922),
  COIMBRA("Coimbra", 40.2033, -8.4103),
  EVORA("Évora", 38.5714, -7.9093),
  FARO("Faro", 37.0194, -7.9304),
  GUARDA("Guarda", 40.5381, -7.2678),
  LEIRIA("Leiria", 39.7437, -8.8071),
  LISBOA("Lisboa", 38.7223, -9.1393),
  PORTALEGRE("Portalegre", 39.2967, -7.4286),
  PORTO("Porto", 41.1579, -8.6291),
  SANTAREM("Santarém", 39.2369, -8.6867),
  SETUBAL("Setúbal", 38.5244, -8.8882),
  VIANA_DO_CASTELO("Viana do Castelo", 41.6938, -8.8344),
  VILA_REAL("Vila Real", 41.3005, -7.7443),
  VISEU("Viseu", 40.6566, -7.9122);

  private final String districtName;
  private final double latitude;
  private final double longitude;

  /**
   * Constructor from the PortugalDistrict
   *
   */
  District(String districtName, double latitude, double longitude) {
    this.districtName = districtName;
    this.latitude = latitude;
    this.longitude = longitude;
  }

  /**
   * @return the district name
   *
   */
  public String toString() {
    return districtName;
  }

  /**
   * @return the latitude
   *
   */
  public double getLatitude() {
    return latitude;
  }

  /**
   * @return the longitude
   *
   */
  public double getLongitude() {
    return longitude;
  }

  /**
   * This method reads a string and returns the respective PortugalDistrict object
   *
   * @param text
   * @return
   */
  public static District fromString(String text) {
    for (District d : District.values()) {
      if (d.districtName.equals(text)) {
        return d;
      }
    }
    return null;
  }
}
