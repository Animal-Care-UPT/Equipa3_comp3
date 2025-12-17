package AnimalCareCentre.client.records;

import AnimalCareCentre.client.enums.*;

import java.util.List;

public record LostAnimal(long id, String name, AnimalType type, String race, AnimalSize size, AnimalGender gender,
    AnimalColor color, String description, District location, boolean isLost, int contact, List<String> images) implements Displayable {
  @Override
  public String toString() {
    return "Name: " + name + '\n' +
        "Type: " + type + '\n' +
        "Race: " + race + '\n' +
        "Size: " + size + '\n' +
        "Gender: " + gender + '\n' +
        "Color: " + color + '\n' +
        "Description: " + description + '\n' +
        "Location: " + location.name() + '\n' +
        "Contact: " + contact;
  }

  @Override
  public String getDisplayName() {
    return "Name: " + name;
  }

  @Override
  public String getDisplayInfo() {
    return "Type: " + type + "\nLocation: " + location;
  }

  @Override
  public String getDescription() {
    return "Contact: " + contact;
  }

  @Override
  public String getImagePath() {

      return (images != null && !images.isEmpty()) ? images.get(0) : null;
  }
  public District getLocation(){
      return this.location;
  }

  @Override
  public Long getId() {
      return id;
  }
}
