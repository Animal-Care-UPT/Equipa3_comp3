package AnimalCareCentre.server.dto;

import AnimalCareCentre.server.enums.Status;
import jakarta.validation.constraints.NotNull;

public class ChangeStatusDTO {

  @NotNull(message = "Status is mandatory")
  private Status status;

  public Status getStatus() {
    return status;
  }
}
