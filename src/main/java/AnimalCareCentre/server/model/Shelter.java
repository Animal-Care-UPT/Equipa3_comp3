package AnimalCareCentre.server.model;

import java.util.ArrayList;
import java.util.List;

import AnimalCareCentre.server.enums.*;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 * This class describes the model of a Shelter.
 *
 */
@Entity
@Table(name = "Shelters")
public class Shelter extends Account {

  private Integer foundationYear;
  private Integer contact;
  private Status status;
  @OneToMany(mappedBy = "shelter", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<ShelterAnimal> animals = new ArrayList<>();

  /**
   * Constructor for the class Shelter
   *
   * @param foundationYear
   * @param contact
   */
  public Shelter(String name, String email, String password, String location, SecurityQuestion securityQuestion,
      String answer, int foundationYear, int contact) {
    super(name, email, password, location, securityQuestion, answer);
    this.foundationYear = foundationYear;
    this.contact = contact;
    status = Status.PENDING;
  }

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

  public Integer getContact() {
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
