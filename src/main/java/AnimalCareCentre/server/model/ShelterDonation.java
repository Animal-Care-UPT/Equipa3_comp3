package AnimalCareCentre.server.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "shelter_donations")
public class ShelterDonation extends Donation {

  @ManyToOne
  @JoinColumn(name = "shelter_id", nullable = false)
  @JsonBackReference("shelter-donations")
  private Shelter shelter;

  public ShelterDonation() {
  }

  public ShelterDonation(User user, Shelter shelter, float amount) {
    super(user, amount);
    this.shelter = shelter;
  }

  public Shelter getShelter() {
    return shelter;
  }

  public void setShelter(Shelter shelter) {
    this.shelter = shelter;
  }

  @Override
  public String getDonationType() {
    return "SHELTER_DONATION";
  }

  @Override
  public String toString() {
    return "ShelterDonation{" +
        "id=" + getId() +
        ", shelter=" + shelter.getName() +
        ", amount=" + getAmount() +
        ", date=" + getDonationDate() +
        '}';
  }
}
