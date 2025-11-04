package AnimalCareCentre.models;

import java.time.LocalDate;
import AnimalCareCentre.enums.*;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * This class describes the model of an Adoption.
 *
 */
@Entity
@Table(name = "Adoptions")
public class Adoption {

  @Id
  @Column(name = "adoption_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;
  @ManyToOne
  @JoinColumn(name = "animal_id")
  private ShelterAnimal animal;
  private LocalDate date;
  @Enumerated(EnumType.STRING)
  private AdoptionType type;
  @Column (nullable = false)
  private boolean acceptance;

  /**
   * Constructor of class Adoption.
   *
   * @param user
   * @param animal
   * @param type
   */
  public Adoption(User user, ShelterAnimal animal, AdoptionType type) {

    this.user = user;
    this.animal = animal;
    this.type = type;
    date = LocalDate.now();
    acceptance = false;
  }

  public Adoption() {}

  // Getters area
  public User getUser() {
    return user;
  }

  public ShelterAnimal getAnimal() {
    return animal;
  }

  public LocalDate getDate() {
    return date;
  }

  public AdoptionType getType() {
    return type;
  }

  public boolean isAcceptance() {
    return acceptance;
  }

  public void setAcceptance(boolean acceptance) {
    this.acceptance = acceptance;
  }

    @Override
    public String toString() {
        return "Adoption{" +
                "id=" + id +
                ", user=" + user +
                ", animal=" + animal.getName() +
                ", date=" + date +
                ", status=" + acceptance +
                '}';
    }
}
