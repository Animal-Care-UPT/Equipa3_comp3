package AnimalCareCentre.client.records;

import AnimalCareCentre.client.enums.*;

public record LostAnimal(long id, String name, AnimalType type, String race, AnimalSize size, AnimalGender gender,
    AnimalColor color, String description, String location, boolean isLost, int contact) implements Displayable {
  @Override
  public String toString() {
    return "Name: " + name + '\n' +
        "Type: " + type + '\n' +
        "Race: " + race + '\n' +
        "Size: " + size + '\n' +
        "Gender: " + gender + '\n' +
        "Color: " + color + '\n' +
        "Description: " + description + '\n' +
        "Location: " + location + '\n' +
        "Contact: " + contact;
  }

  @Override
  public String getDisplayName() {
    return "Name: " + name;
  }

  @Override
  public String getDisplayInfo() {
    return "Type: " + type + "\nRace: " + race + "\nLocation: " + location;
  }

  @Override
  public String getDescription() {
    return "Contact: " + contact + "\nDescription: " + description;
  }

  @Override
  public String getImagePath() {
    // todo
      return null;
  }
}
