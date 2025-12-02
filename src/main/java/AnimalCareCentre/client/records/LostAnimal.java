package AnimalCareCentre.client.records;

import AnimalCareCentre.client.enums.*;

public record LostAnimal(long id, String name, AnimalType type, String race, AnimalSize size, AnimalGender gender,
    AnimalColor color, String description, String location, boolean isLost, int contact) {
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
}
