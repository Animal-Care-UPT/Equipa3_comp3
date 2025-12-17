package AnimalCareCentre.server.dto;

import java.time.LocalDate;

import AnimalCareCentre.server.enums.*;
import AnimalCareCentre.server.model.*;

public class AdoptionDTO {

  private Long id;
  private User user;
  private Shelter shelter;
  private ShelterAnimal animal;
  private LocalDate adoptionDate;
  private AdoptionType type;
  private LocalDate requestDate;
  private Status status;

  public Status getStatus() {
    return status;
  }

  public Shelter getShelter() {
      return shelter;
  }

  public void setShelter(Shelter shelter) {
      this.shelter = shelter;
  }

  public void setStatus(Status status) {
    this.status = status;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

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

  public LocalDate getRequestDate() {
    return requestDate;
  }

  public void setRequestDate(LocalDate requestDate) {
    this.requestDate = requestDate;
  }

}
