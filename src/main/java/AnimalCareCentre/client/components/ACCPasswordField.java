package AnimalCareCentre.client.components;

import javafx.scene.control.PasswordField;

/**
 * PasswordFields used throughout the platform
 *
 */
public class ACCPasswordField extends PasswordField {

  public ACCPasswordField() {
    setMaxWidth(250);
    setStyle(
        "-fx-background-color: white; " +
            "-fx-border-color: #CCCCCC; " +
            "-fx-border-radius: 5; " +
            "-fx-background-radius: 5; " +
            "-fx-padding: 8 12 8 12; " +
            "-fx-font-size: 14px;");
    setMinHeight(35);
    setMaxHeight(35);
  }

}
