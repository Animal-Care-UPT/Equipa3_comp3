package AnimalCareCentre.server.model;

import AnimalCareCentre.server.enums.*;

import jakarta.persistence.*;

@MappedSuperclass
abstract class Animal {

  @Id
  @Column(name = "animal_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  private String name;
  @Enumerated(EnumType.STRING)
  private AnimalType type;

  private String race;

  @Enumerated(EnumType.STRING)
  private AnimalSize size;

  @Enumerated(EnumType.STRING)
  private AnimalGender gender;

  @Enumerated(EnumType.STRING)
  private AnimalColor color;

  private String description;

  public Animal() {
  }

  public long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public AnimalType getType() {
    return type;
  }

  public void setType(AnimalType type) {
    this.type = type;
  }

  public String getRace() {
    return race;
  }

  public void setRace(String race) {
    this.race = race;
  }

  public AnimalSize getSize() {
    return size;
  }

  public AnimalGender getGender() {return gender;}

  public Animal(String name, AnimalType type, String race, AnimalSize animalSize, AnimalGender gender, AnimalColor animalColor,
      String description) {
    this.name = name;
    this.type = type;
    this.race = race;
    this.size = animalSize;
    this.color = animalColor;
    this.description = description;
    this.gender = gender;
  }

  public AnimalColor getColor() {
    return color;
  }

  public String getDescription() {
    return description;
  }

  @Override
  public String toString() {
    return "Name: " + name + "\nType: " + type + "\nRace: " + race + "\nSize: " + size +  "\nGender: " + gender +
            "\nColor: " + color + "\nDescription: " + description;
  }

}
