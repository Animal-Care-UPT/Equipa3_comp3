package AnimalCareCentre.server.model;

import java.time.LocalDate;
import AnimalCareCentre.server.enums.*;
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
  private LocalDate requestDate;
  private LocalDate adoptionDate;
  @Enumerated(EnumType.STRING)
  private AdoptionType type;
  @Enumerated(EnumType.STRING)
  private Status status;

  public Adoption() {}

  // Getters and setters area
  public User getUser() {
    return user;
  }

  public ShelterAnimal getAnimal() {
    return animal;
  }

  public LocalDate getRequestDate() { return requestDate; }

  public LocalDate getAdoptionDate() { return adoptionDate; }

  public void setAdoptionDate(LocalDate adoptionDate) { this.adoptionDate = adoptionDate; }

  public Status getStatus() { return status; }

  public void setStatus(Status status) { this.status = status; }

  public AdoptionType getType() {
    return type;
  }



    @Override
    public String toString() {
        return "Adoption{" +
                "id=" + id +
                ", user=" + user +
                ", animal=" + animal.getName() +
                ", date of request =" + requestDate +
                ", status=" + status +
                '}';
    }
}
