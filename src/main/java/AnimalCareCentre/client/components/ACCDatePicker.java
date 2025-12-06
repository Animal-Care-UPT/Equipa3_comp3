package AnimalCareCentre.client.components;

import javafx.scene.control.DatePicker;

public class ACCDatePicker extends DatePicker {
  public ACCDatePicker() {
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

  @Override
  protected void layoutChildren() {
    super.layoutChildren();
    if (lookup(".text-field") != null) {
      lookup(".text-field").setStyle(
          "-fx-background-color: transparent; " +
              "-fx-border-color: transparent;");
    }

    if (lookup(".arrow-button") != null) {
      lookup(".arrow-button").setStyle(
          "-fx-background-color: transparent; " +
              "-fx-border-color: transparent;");
    }
  }
}
