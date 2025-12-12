package AnimalCareCentre.client.components;

import javafx.scene.control.Button;

public class ACCTextButton extends Button {
  public ACCTextButton(String text) {
    super(text);
    setStyle(
        "-fx-background-color: transparent; " +
            "-fx-text-fill: #333333; " +
            "-fx-font-size: 16px; " +
            "-fx-padding: 5; " +
            "-fx-cursor: hand; " +
            "-fx-underline: false;");

    setOnMouseEntered(e -> setStyle(
        "-fx-background-color: transparent; " +
            "-fx-text-fill: #69462B; " +
            "-fx-font-size: 16px; " +
            "-fx-padding: 5; " +
            "-fx-cursor: hand; " +
            "-fx-underline: true;"));

    setOnMouseExited(e -> setStyle(
        "-fx-background-color: transparent; " +
            "-fx-text-fill: #333333; " +
            "-fx-font-size: 16px; " +
            "-fx-padding: 5; " +
            "-fx-cursor: hand; " +
            "-fx-underline: false;"));
  }
}
