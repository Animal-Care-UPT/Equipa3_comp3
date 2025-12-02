package AnimalCareCentre.server.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class SponsorshipRequestDTO {

  private Long animalId;

  @Positive
  private Float amount;

  public Long getAnimalId() {
    return animalId;
  }

  public void setAnimalId(Long animalId) {
    this.animalId = animalId;
  }

  public Float getAmount() {
    return amount;
  }

  public void setAmount(Float amount) {
    this.amount = amount;
  }
}
