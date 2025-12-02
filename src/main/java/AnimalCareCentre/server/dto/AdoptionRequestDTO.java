package AnimalCareCentre.server.dto;

import AnimalCareCentre.server.enums.AdoptionType;
import jakarta.validation.constraints.NotNull;

public class AdoptionRequestDTO {

  private Long animalId;

  private AdoptionType type;

  public Long getAnimalId() {
    return animalId;
  }

  public void setAnimalId(Long animalId) {
    this.animalId = animalId;
  }

  public AdoptionType getType() {
    return type;
  }

  public void setType(AdoptionType type) {
    this.type = type;
  }
}
