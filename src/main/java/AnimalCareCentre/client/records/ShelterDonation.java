package AnimalCareCentre.client.records;

import java.time.LocalDate;

public record ShelterDonation(
    Long id,
    String userName,
    String shelterName,
    float amount,
    LocalDate donationDate,
    String donationType) {

  @Override
  public String toString() {
    return "\nDonation ID: " + id +
        "\nUser: " + userName +
        "\nShelter: " + shelterName +
        "\nAmount: " + amount +
        "\nDate: " + donationDate +
        "\nType: " + donationType + "\n";
  }
}
