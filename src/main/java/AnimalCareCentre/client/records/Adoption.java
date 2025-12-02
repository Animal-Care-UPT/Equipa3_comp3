package AnimalCareCentre.client.records;

import java.time.LocalDate;
import AnimalCareCentre.client.enums.*;

public record Adoption(
    Shelter shelter,
    ShelterAnimal animal,
    User user,
    AdoptionType type,
    Long id,
    Status status,
    LocalDate adoptionDate,
    LocalDate requestDate) {

  @Override
  public String toString() {
    return "\nAdoption ID: " + id +
        "\nAnimal: " + (animal != null ? animal.name() : "N/A") +
        "\nUser: " + (user != null ? user.name() : "N/A") +
        "\nType: " + type +
        "\nStatus: " + status +
        "\nRequest Date: " + requestDate +
        "\nAdoption Date: " + (adoptionDate != null ? adoptionDate : "N/A") + "\n";
  }
}
