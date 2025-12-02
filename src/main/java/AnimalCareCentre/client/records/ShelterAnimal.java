package AnimalCareCentre.client.records;

import java.util.List;

import AnimalCareCentre.client.enums.*;

public record ShelterAnimal(long id, String name, AnimalType type, String race, AnimalSize size, AnimalGender gender,
    AnimalColor color, String description, Shelter shelter, boolean vacinated, int age, Status status,
    AdoptionType adoptionType, List<Adoption> adoptions, List<Sponsorship> sponsors) {

  @Override
  public String toString() {
    return "\nName: " + name + "\nType: " + type + "\nRace: " + race + "\nSize: " + size + "\nGender: " + gender
        + "\nColor: " + color + "\nDescription: " + description + "\nListed For: " + adoptionType
        + "\nVacination Status: " + vacinated + "\nAge: " + age + "\nShelter: " + shelter.name() + "\n";
  }

    public Long getId() {
      return id;
    }
}
