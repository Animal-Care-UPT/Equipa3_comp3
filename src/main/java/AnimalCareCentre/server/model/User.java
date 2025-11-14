package AnimalCareCentre.server.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import AnimalCareCentre.server.enums.*;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 * This class describes the model of a User from the system, its attributes and
 * what it can do.
 *
 */
@Entity
@Table(name ="Users")
public class User extends Account {

  private int contact;
  private LocalDate birthDate;
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Adoption> adoptions;
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Sponsorship> sponsorships = new ArrayList<>();
  

  /**
   * Constructor for the class User
   *
   * @param name
   * @param email
   * @param password
   * @param location
   * @param securityQuestion
   * @param birthDate
   * @param contact
   */
  public User(String name, String email, String password, String location, SecurityQuestion securityQuestion,
      String answer,
      LocalDate birthDate, int contact) {
    super(name, email, password, location, securityQuestion, answer);
    this.birthDate = birthDate;
    this.contact = contact;
  }

  protected User() {}

  // Getter from the contact of the user
  public int getContact() {
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
  
  
}
