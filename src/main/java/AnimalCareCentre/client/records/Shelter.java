package AnimalCareCentre.client.records;

import AnimalCareCentre.client.enums.SecurityQuestion;
import AnimalCareCentre.client.enums.Status;

public record Shelter(long id, String name, String email, String location, SecurityQuestion securityQuestion, Integer foundationYear, String contact, Status status) {

  @Override
  public final String toString() {
    return "\nName: " + name + "\nLocation: " + location + "\nFoundation Year: " + foundationYear + "\nContact: " + contact + "\n";
  }
}
