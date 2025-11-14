package AnimalCareCentre.server.util;

import java.util.Arrays;
import java.util.Properties;

import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.LengthRule;
import org.passay.MessageResolver;
import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.PropertiesMessageResolver;
import org.passay.RuleResult;
import org.passay.WhitespaceRule;

public class ACCPasswordValidator {

  private final PasswordValidator validator;

  /**
   * This class defines the password validation rules in AnimalCareCentre
   */
  public ACCPasswordValidator() {
    ACCSpecialCharacter specialChars = new ACCSpecialCharacter();

    Properties props = new Properties();
    props.put("TOO_SHORT", "Password must be at least 8 characters long.");
    props.put("TOO_LONG", "Password must not exceed 16 characters.");
    props.put("INSUFFICIENT_SPECIAL", "Must include at least one allowed special character (!@#$_-).");
    props.put("INSUFFICIENT_DIGIT", "Must include at least one number.");
    props.put("INSUFFICIENT_UPPERCASE", "Must include at least one uppercase letter.");
    props.put("ILLEGAL_WHITESPACE", "Cannot contain spaces.");
    props.put("ERROR_CODE_CUSTOM", "Contains invalid special characters.");

    MessageResolver resolver = new PropertiesMessageResolver(props);

    validator = new org.passay.PasswordValidator(resolver, Arrays.asList(
        new LengthRule(8, 16),
        new CharacterRule(EnglishCharacterData.UpperCase, 1),
        new CharacterRule(EnglishCharacterData.Digit, 1),
        new CharacterRule(specialChars, 1),
        new WhitespaceRule()));
  }

  /**
   * Validates a password.
   * 
   * @return null if valid, or a readable error message if invalid.
   */
  public String validate(String password) {
    RuleResult result = validator.validate(new PasswordData(password));
    if (result.isValid()) {
      return null;
    }
    return String.join("\n", validator.getMessages(result));
  }
}
