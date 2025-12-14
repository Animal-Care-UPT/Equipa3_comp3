package AnimalCareCentre.server.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import AnimalCareCentre.server.enums.Status;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

/**
 * This class describes the model of a sponsorhip to the animals and how it
 * works.
 *
 */

@Entity
@Table(name = "Sponsorships")
public class Sponsorship extends Donation {

  @ManyToOne
  @JoinColumn(name = "animal_id")
  @JsonBackReference("animal-sponsorships")
  private ShelterAnimal animal;

  @Enumerated(EnumType.STRING)
  private Status status;

  @Column(name = "start_date")
  private LocalDate startDate;

  @Column(name = "end_date")
  private LocalDate endDate;

  @Column(name = "total_donated")
  private float totalDonated = 0;


  public Sponsorship() {
  }

  public Sponsorship(User user, ShelterAnimal animal, float amount) {
      super(user, amount);
      this.animal = animal;
      this.status = Status.ACTIVE;
      this.startDate = LocalDate.now();
      this.totalDonated = amount;
    }

  public long getId() {
    return super.getId();
  }

  public User getUser() {
    return super.getUser();
  }

  public void setUser(User user) {
    super.setUser(user);
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


    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public float getTotalDonated() {
        return totalDonated;
    }

    public void setTotalDonated(float totalDonated) {
        this.totalDonated = totalDonated;
    }


    @Override
    public String getDonationType() {
        return "SPONSORSHIP";
    }

    public int getPaymentsMade() {
        if (startDate == null) return 0;

        long months = ChronoUnit.MONTHS.between(startDate, LocalDate.now());
        return (int) months + 1;
    }

    public float getCalculatedTotalDonated() {
        return getPaymentsMade() * getAmount();
    }

    @Override
    public String toString() {
        return "Sponsorship{" +
                "id=" + getId() +
                ", animal=" + animal.getName() +
                ", amount=" + getAmount() +
                ", status=" + status +
                ", totalDonated=" + totalDonated +
                '}';
    }
}
