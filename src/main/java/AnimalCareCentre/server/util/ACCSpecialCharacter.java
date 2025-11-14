package AnimalCareCentre.server.util;

import org.passay.CharacterData;

public class ACCSpecialCharacter implements CharacterData {

  /**
   * This class implesments CharacterData and defines the allowed special
   * characters in passwords in AnimalCareCentre
   *
   * @return
   */
  @Override
  public String getErrorCode() {
    return "INVALID_CUSTOM_CHARACTERS";
  }

  @Override
  public String getCharacters() {
    return "!@#$_-";
  }

}
