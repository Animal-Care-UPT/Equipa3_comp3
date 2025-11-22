package AnimalCareCentre.server.model;

import AnimalCareCentre.server.enums.*;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@MappedSuperclass
abstract class Animal {

  @Id
  @Column(name = "animal_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @NotBlank(message = "Name is required!")
  private String name;

  @Enumerated(EnumType.STRING)
  @NotNull(message = "Animal Type is required!")
  private AnimalType type;

  @NotBlank(message = "Animal Race is required!")
  private String race;

  @Enumerated(EnumType.STRING)
  @NotNull(message = "Size is required!")
  private AnimalSize size;

  @Enumerated(EnumType.STRING)
  @NotNull(message = "Gender is required!")
  private AnimalGender gender;

  @Enumerated(EnumType.STRING)
  @NotNull(message = "Color is required!")
  private AnimalColor color;

  @NotBlank(message = "Description is required!")
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
