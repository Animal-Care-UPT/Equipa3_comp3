package AnimalCareCentre.server.dto;

import jakarta.validation.constraints.NotBlank;

public class ChangePasswordDTO {

  @NotBlank(message = "Email is mandatory!")
  private String email;
  @NotBlank(message = "Security Answer is mandatory!")
  private String answer;
  @NotBlank(message = "New password is mandatory!")
  private String newPassword;

  public String getEmail() {
    return email;
  }
  public String getAnswer() {
    return answer;
  }
  public String getNewPassword() {
    return newPassword;
  }
}
