package AnimalCareCentre.server.model;

import java.util.ArrayList;
import java.util.List;

import AnimalCareCentre.server.enums.*;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * This class describes the model of a Shelter.
 *
 */
@Entity
@Table(name = "Shelters")
public class Shelter extends Account {

  @NotNull(message = "Foundation Year is mandatory!")
  @Min(value = 1900, message = "Must enter a valid year!")
  private Integer foundationYear;
  @NotNull(message = "Contact is mandatory!")
  @Size(min = 9, max = 9)
  private String contact;
  private Status status;
  @OneToMany(mappedBy = "shelter", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<ShelterAnimal> animals = new ArrayList<>();

  protected Shelter() {
  }

  // Getters
  public Integer getFoundationYear() {
    return foundationYear;
  }

  public Status getStatus() {
    return status;
  }

  public long getId() {
    return id;
  }

  public String getContact() {
    return contact;
  }

  public void setStatus(Status status) {
    this.status = status;
  }

  @Override
  public String toString() {
    return super.toString() + "\nFoundation Year: " + foundationYear + "\nContact: " + contact;
  }

}
