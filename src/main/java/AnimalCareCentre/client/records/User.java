package AnimalCareCentre.client.records;

import java.time.LocalDate;
import java.util.List;

import AnimalCareCentre.client.enums.SecurityQuestion;

public record User(long id, String name, String email, String location, SecurityQuestion securityQuestion,
    String contact, LocalDate birthDate, List<Adoption> adoptions, List<Sponsorship> sponsorships) {

  @Override
  public String toString() {
        return "\nName: " + name +
        "\nEmail: " + email +
        "\nLocation: " + location +
        "\nBirth Date: " + birthDate +
        "\nContact: " + contact + "\n";
  }
}
