package AnimalCareCentre.client.records;

import java.util.List;

import AnimalCareCentre.client.enums.SecurityQuestion;
import AnimalCareCentre.client.enums.Status;

public record Shelter(long id, String name, String email, String location, SecurityQuestion securityQuestion,
    Integer foundationYear, String contact, Status status, List<String> images) implements Displayable {

  @Override
  public String toString() {
    return "\nName: " + name +
        "\nEmail: " + email +
        "\nLocation: " + location +
        "\nFoundation Year: " + foundationYear +
        "\nContact: " + contact +
        "\nStatus: " + status + "\n";
  }

  @Override
  public String getDisplayName() {
    return "Name: " + name;
  }

  @Override
  public String getDisplayInfo() {
    return "Location: " + location;
  }

  @Override
  public String getDescription() {
    return "Contact: " + contact;
  }

  @Override
  public String getImagePath() {
    return (images != null && !images.isEmpty()) ? images.get(0) : null;
  }

  @Override
  public Long getId() {
    return id;
  }

}
