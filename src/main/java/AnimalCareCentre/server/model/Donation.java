package AnimalCareCentre.server.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

/**
 * This class describes the model of a Donation with its attributes and how it
 * works.
 *
 */
@Entity
@Table (name = "Donations")
public class Donation {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  @NotNull(message = "The amount is mandatory")
  private Float amount;
  private LocalDate donationDate;
  @ManyToOne
  @JoinColumn(name = "Sponsorship_id")
  private Sponsorship sponsorship;

  public Donation() {  
  }
  
  public float getAmount() {
    return amount;
  }

  public LocalDate getDonationDate() {
    return donationDate;
  }
  
  
  public void setSponsorship(Sponsorship sponsorship) {
	this.sponsorship = sponsorship;
}

// ToString from the class
  @Override
  public String toString() {
    return "Donation{" +
        ", amount=" + amount +
        ", donationDate=" + donationDate +
        '}';
  }
}
