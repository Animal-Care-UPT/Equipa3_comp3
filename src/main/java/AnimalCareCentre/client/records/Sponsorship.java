package AnimalCareCentre.client.records;

import java.time.LocalDate;

public record Sponsorship(
    Long userId,
    String userName,
    Long animalId,
    String animalName,
    Float amount,
    LocalDate startDate) {
  @Override
  public String toString() {
    return "\nUser ID: " + userId +
        "\nUser: " + (userName != null ? userName : "N/A") +
        "\nAnimal ID: " + animalId +
        "\nAnimal: " + (animalName != null ? animalName : "N/A") +
        "\nAmount: $" + amount +
        "\nStart Date: " + startDate + "\n";
  }
}
