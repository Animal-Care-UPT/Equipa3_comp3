package AnimalCareCentre.server.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * This class describes the model of a Donation with its attributes and how it
 * works.
 *
 */
@MappedSuperclass
public abstract class Donation {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "donation_id")
  private long id;

  @NotNull(message = "The amount is mandatory")
  @Positive(message = "Amount must be positive!")
  @Min(5)
  @Max(100)
  private float amount;

  @Column(name = "donation_date")
  private LocalDate donationDate;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference("user-sponsorships")
    private User user;

  public Donation() {
    this.donationDate = LocalDate.now();
  }

  public Donation(User user, float amount) {
    this();
    this.user = user;
    this.amount = amount;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public float getAmount() {
    return amount;
  }

  public void setAmount(float amount) {
    this.amount = amount;
  }

  public LocalDate getDonationDate() {
    return donationDate;
  }

  public void setDonationDate(LocalDate donationDate) {
    this.donationDate = donationDate;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public abstract String getDonationType();

}
