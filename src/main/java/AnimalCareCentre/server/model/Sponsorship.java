package AnimalCareCentre.server.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * This class describes the model of a sponsorhip to the animals and how it
 * works.
 *
 */

@Entity
@Table(name = "Sponsorships")
public class Sponsorship {

  @Id
  @Column(name = "Sponsorship_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @ManyToOne
  @JoinColumn(name = "user_id")
  @JsonBackReference("user-sponsorships")
  private User user;

  @ManyToOne
  @JoinColumn(name = "animal_id")
  @JsonBackReference("animal-sponsorships")
  private ShelterAnimal animal;

  private LocalDate startDate;

  @Min(5)
  @Max(1000)
  @NotNull(message = "The amount is mandatory")
  private Float amount;

  @OneToMany(mappedBy = "sponsorship", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Donation> donations = new ArrayList<>();

  public Sponsorship() {
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
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

  public LocalDate getStartDate() {
    return startDate;
  }

  public void setStartDate(LocalDate startDate) {
    this.startDate = startDate;
  }

  public Float getAmount() {
    return amount;
  }

  public void setAmount(Float amount) {
    this.amount = amount;
  }

  public List<Donation> getDonations() {
    return donations;
  }

  public void setDonations(List<Donation> donations) {
    this.donations = donations;
  }

}
