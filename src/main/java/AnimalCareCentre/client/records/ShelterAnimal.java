package AnimalCareCentre.client.records;

import java.util.List;

import AnimalCareCentre.client.enums.*;

public record ShelterAnimal(long id, String name, AnimalType type, String race, AnimalSize size, AnimalGender gender,
    AnimalColor color, String description, Shelter shelter, boolean isVacinated, Status status,
    AdoptionType adoptionType, List<Adoption> adoptions, List<Sponsorship> sponsors) {

  @Override
  public String toString() {
    return "\nName: " + name + "\nType: " + type + "\nRace: " + race + "\nSize: " + size + "\nGender: " + gender
        + "\nColor: " + color + "\nDescription: " + description + "\nListed For: " + adoptionType + "\nShelter: " + shelter.name() + "\n";
  }

}
