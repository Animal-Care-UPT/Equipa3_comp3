package AnimalCareCentre.server.dto;

import AnimalCareCentre.server.enums.SecurityQuestion;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class SecurityQuestionDTO {

  @NotNull(message = "Security Question is mandatory!")
  private SecurityQuestion securityQuestion;

  @NotBlank(message = "Security Answer is mandatory!")
  private String answer;

  public SecurityQuestion getSecurityQuestion(){
    return securityQuestion;
  }

  public String getAnswer(){
    return answer;
  }
}
