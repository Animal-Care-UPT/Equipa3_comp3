package AnimalCareCentre.server.model;

import java.util.ArrayList;
import java.util.List;

import AnimalCareCentre.server.enums.*;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * This class describes the model of an Account.
 *
 */
@Entity
@Table(name = "Accounts")
@Inheritance(strategy = InheritanceType.JOINED)
public class Account {

  @Id
  @Column(name = "account_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  protected long id;
  @NotBlank(message = "Name is mandatory!")
  private String name;
  @NotBlank(message = "Email is mandatory!")
  @Email(message = "Please enter a valid email address!")
  private String email;
  @NotBlank(message = "Password is mandatory!")
  @Size(min = 8, max = 16)
  private String password;
  @NotBlank(message = "Location is mandatory!")
  private String location;
  @Enumerated(EnumType.STRING)
  @NotNull(message = "Security Question is mandatory!")
  private SecurityQuestion securityQuestion;
  @NotBlank(message = "Security Answer is mandatory!")
  private String answer;
  @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<LostAnimal> lostAnimals = new ArrayList<>();

  protected Account() {
  }

  // toString from the class Account
  @Override
  public String toString() {
    return "Name: " + name + "\nLocation: " + location;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public SecurityQuestion getSecurityQuestion() {
    return securityQuestion;
  }

  public void setSecurityQuestion(SecurityQuestion securityQuestion) {
    this.securityQuestion = securityQuestion;
  }

  public String getAnswer() {
    return answer;
  }

  public void setAnswer(String answer) {
    this.answer = answer;
  }

}
