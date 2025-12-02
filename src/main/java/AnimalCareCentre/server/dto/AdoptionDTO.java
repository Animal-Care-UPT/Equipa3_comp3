package AnimalCareCentre.server.dto;


import java.time.LocalDate;

import AnimalCareCentre.server.enums.*;
import AnimalCareCentre.server.model.*;
import jakarta.validation.constraints.NotNull;

public class AdoptionDTO {

  @NotNull
  private User user;
  @NotNull
  private ShelterAnimal animal;
  @NotNull
  private LocalDate adoptionDate;
  @NotNull
  private AdoptionType type;

  public User getUser() {
    return user;
  }
  public void setUser(User user) {
    this.user = user;
  }
  public ShelterAnimal getAnimal() {
    return animal;
  }
  public void setAnimal(ShelterAnimal animal) {
    this.animal = animal;
  }
  public LocalDate getAdoptionDate() {
    return adoptionDate;
  }
  public void setAdoptionDate(LocalDate adoptionDate) {
    this.adoptionDate = adoptionDate;
  }
  public AdoptionType getType() {
    return type;
  }
  public void setType(AdoptionType type) {
    this.type = type;
  }
}
