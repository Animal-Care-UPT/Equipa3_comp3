package AnimalCareCentre.client.components;

import javafx.scene.control.ComboBox;

public class ACCComboBox<T> extends ComboBox<T> {
  public ACCComboBox() {
    setMaxWidth(250);
    setStyle(
        "-fx-background-color: white; " +
            "-fx-border-color: #CCCCCC; " +
            "-fx-border-radius: 5; " +
            "-fx-background-radius: 5; " +
            "-fx-font-size: 14px;");
    setMinHeight(35);
    setMaxHeight(35);
  }
}
