package AnimalCareCentre.server.dto;

import jakarta.validation.constraints.NotBlank;

public class AdminSecretDTO {

  @NotBlank(message = "Admin secret is mandatory!")
  private String answer;

  public String getAnswer() {
    return answer;
  }

}
