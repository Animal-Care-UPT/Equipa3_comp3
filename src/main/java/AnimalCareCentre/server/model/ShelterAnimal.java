package AnimalCareCentre.server.model;

import java.util.ArrayList;
import java.util.List;

import AnimalCareCentre.server.enums.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

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

  @NotNull
  private Integer age;

  @Enumerated(EnumType.STRING)
  private Status status;

  @Enumerated(EnumType.STRING)
  @NotNull(message = "Adoption Type is required!")
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

  @Override
  public String toString() {
    return super.toString() + "\n" + "Listed for: " + adoptionType;
  }

  public Shelter getShelter() {
    return shelter;
  }

  public void setShelter(Shelter shelter) {
    this.shelter = shelter;
  }

  public boolean isVacinated() {
    return isVacinated;
  }

  public void setVacinated(boolean isVacinated) {
    this.isVacinated = isVacinated;
  }

  public Integer getAge() {
    return age;
  }

  public void setAge(Integer age) {
    this.age = age;
  }

  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = status;
  }

  public AdoptionType getAdoptionType() {
    return adoptionType;
  }

  public void setAdoptionType(AdoptionType adoptionType) {
    this.adoptionType = adoptionType;
  }

  public List<Adoption> getAdoptions() {
    return adoptions;
  }

  public void setAdoptions(List<Adoption> adoptions) {
    this.adoptions = adoptions;
  }

  public List<Sponsorship> getSponsors() {
    return sponsors;
  }

  public void setSponsors(List<Sponsorship> sponsors) {
    this.sponsors = sponsors;
  }
}
