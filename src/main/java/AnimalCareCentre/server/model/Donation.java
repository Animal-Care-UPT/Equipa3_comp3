package AnimalCareCentre.server.model;

import java.time.LocalDate;

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
  private User donor;

    public Donation() {
        this.donationDate = LocalDate.now();
    }

    public Donation(User donor, float amount) {
        this(); // chama o construtor vazio → cria a data
        this.donor = donor;
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

    public User getDonor() {
        return donor;
    }

    public void setDonor(User donor) {
        this.donor = donor;
    }

    public abstract String getDonationType();


}
