package AnimalCareCentre.server.model;

import java.util.ArrayList;
import java.util.List;

import AnimalCareCentre.server.enums.*;
import jakarta.persistence.*;

/**
 * This class describes the model of an Animal.
 *
 */
@Entity
public class ShelterAnimal extends Animal {

  @ManyToOne
  @JoinColumn(name = "shelter_id")
  private Shelter shelter;

  private boolean isVacinated;

  private Status status;

  @Enumerated(EnumType.STRING)
  private AdoptionType adoptionType;

  @OneToMany(mappedBy = "animal")
  List<Adoption> adoptions = new ArrayList<>();
  @OneToMany(mappedBy = "animal", cascade = CascadeType.ALL, orphanRemoval = true)
  List<Sponsorship> sponsors = new ArrayList<>();

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

  public AdoptionType getAdoptionType() {
    return adoptionType;
  }

  public String getDescription() {
    return super.getDescription();
  }

  public Shelter getShelter() {
    return shelter;
  }

  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = status;
  }

  public void setShelter(Shelter shelter) {
    this.shelter = shelter;
  }

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

  public void setAdoptionType(AdoptionType adoptionType) {
    this.adoptionType = adoptionType;
  }

  // toString from the class
  @Override
  public String toString() {
    return super.toString() + "\n" + "Listed for: " + adoptionType;
  }
}
