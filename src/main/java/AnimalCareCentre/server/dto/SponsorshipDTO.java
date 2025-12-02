package AnimalCareCentre.server.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class SponsorshipDTO{
   
  @NotNull
  private Long id;
  @NotNull(message = "The amount is mandatory")
  private Float amount;

  public Long getId() {
    return id;
  }
  public Float getAmount() {
    return amount;
  }
  
  
}
