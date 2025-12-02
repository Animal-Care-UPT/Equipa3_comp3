package AnimalCareCentre.server.model;

import java.time.LocalDate;
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
 * This class describes the model of a User from the system, its attributes and
 * what it can do.
 *
 */
@Entity
@Table(name = "Users")
public class User extends Account {

  @Size(min = 9, max = 9)
  @NotNull(message = "Contact is mandatory!")
  private String contact;
  @NotNull(message = "Birthdate is mandatory!")
  private LocalDate birthDate;
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Adoption> adoptions;
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Sponsorship> sponsorships = new ArrayList<>();

  protected User() {
  }

  // Getter from the contact of the user
  public String getContact() {
    return contact;
  }

  public void addSponsor(Sponsorship sponsor) {
    sponsorships.add(sponsor);
    sponsor.setUser(this);
  }

  @Override
  public String toString() {
    return super.toString() + "\nContact: " + contact;
  }

  public long getId() {
    return id;
  }

  public LocalDate getBirthDate() {
    return birthDate;
  }
}
