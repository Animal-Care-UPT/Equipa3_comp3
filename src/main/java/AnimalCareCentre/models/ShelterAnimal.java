package AnimalCareCentre.models;

import java.util.ArrayList;
import java.util.List;

import AnimalCareCentre.enums.*;
import jakarta.persistence.*;

/**
 * This class describes the model of an Animal.
 *
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)

public class ShelterAnimal extends Animal {

  @ManyToOne
  @JoinColumn(name = "shelter_id")
  private Shelter shelter;

  private boolean isVacinated;

  @Enumerated(EnumType.STRING)
  private AdoptionType listedFor;

  @OneToMany(mappedBy = "animal")
  List<Adoption> adoptions = new ArrayList<>();
  @OneToMany(mappedBy = "animal", cascade = CascadeType.ALL, orphanRemoval = true)
  List<Sponsorship> sponsors = new ArrayList<>();

  /**
   * Constructor for the class Animal.
   *
   * @param name
   * @param type
   * @param race
   * @param color
   * @param isVacinated
   * @param size
   * @param listedFor
   * @param description
   */
  public ShelterAnimal(String name, AnimalType type, String race, AnimalColor color, boolean isVacinated,
      AnimalSize size, AnimalGender gender, AdoptionType listedFor, String description, Shelter shelter) {
    super(name, type, race, size, gender, color, description);
    this.isVacinated = isVacinated;
    this.listedFor = listedFor;
    this.shelter = shelter;
  }

  public ShelterAnimal() {
    super();
  }

  public void addSponsor(Sponsorship sponsor) {
    sponsors.add(sponsor);
  }

  // The different getter from the class
  public String getName() {
    return super.getName();
  }

  public AnimalType getType() {
    return super.getType();
  }

  public String getRace() {
    return super.getRace();
  }

  public AnimalColor getColor() {
    return super.getColor();
  }

  public boolean isVacinated() {
    return isVacinated;
  }

  public AnimalSize getSize() {
    return super.getSize();
  }

  public AdoptionType getListedFor() {
    return listedFor;
  }

  public String getDescription() {
    return super.getDescription();
  }

  public Shelter getShelter() {
    return shelter;
  }

  // public List<Adoption> getAdoptions() {
  // return adoptions;
  // }

  public List<Sponsorship> getSponsors() {
    return sponsors;
  }

  public List<Adoption> getAdoptions() {
    return adoptions;
  }

  // The different setters from the class
  public void setVacinated(boolean vacinated) {
    isVacinated = vacinated;
  }

  public void setListedFor(AdoptionType listedFor) {
    this.listedFor = listedFor;
  }

  // toString from the class
  @Override
  public String toString() {
      return super.toString() + "\n" + "Listed for: " + listedFor;
  }
}
