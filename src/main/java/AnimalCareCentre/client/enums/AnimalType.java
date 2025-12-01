package AnimalCareCentre.client.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * This enum lists all the possible types Animals.
 *
 */
public enum AnimalType {

  CAT("Cat",
      List.of("Siamês", "Persa", "Maine Coon", "Bengal", "Sphynx", "British Shorthair", "Ragdoll", "Scottish Fold",
          "Abyssinian", "Norwegian Forest Cat")),
  DOG("Dog", List.of("Labrador Retriever", "Golden Retriever", "Pastor Alemão", "Poodle", "Bulldog Francês", "Beagle",
      "Chihuahua", "Husky Siberiano", "Dachshund", "Border Collie", "Boxer", "Rottweiler", "Cocker Spaniel",
      "Shih Tzu", "Pug", "Doberman", "Schnauzer", "Maltês", "Akita Inu", "Samoyed", "Mixed breed", "Doesn't apply")),
  RABBIT("Rabbit", List.of("Holland Lop", "Netherland Dwarf", "Lionhead", "Mini Rex", "Flemish Giant", "English Angora",
      "French Lop", "Californian", "New Zealand White", "Havana"));

  private final String animalType;
  private final List<String> breeds;

  /**
   * Constructor from the AnimalType
   *
   */
  AnimalType(String animalType, List<String> breeds) {

    this.animalType = animalType;
    this.breeds = new ArrayList<>(breeds);
  }

  /**
   * @return the animal type
   *
   */
  public String toString() {
    return animalType;
  }

  /**
   * @return the animal race
   *
   */
  public List<String> getBreeds() {
    return breeds;
  }

  /**
   * This method reads a string and returns the respective AnimalType object
   *
   * @param text
   * @return
   */
  public static AnimalType fromString(String text) {
    for (AnimalType a : values()) {
      if (a.name().equalsIgnoreCase(text) || a.animalType.equalsIgnoreCase(text)) {
        return a;
      }
    }
    return null;
  }
}
