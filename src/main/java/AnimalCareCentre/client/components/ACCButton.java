package AnimalCareCentre.client.components;

import javafx.scene.control.Button;

public class ACCButton extends Button {

  private String style = "-fx-background-color: #6F4426; " +
      "-fx-text-fill: white; " +
      "-fx-font-size: 14px; " +
      "-fx-padding: 9 9 9 9; " +
      "-fx-background-radius: 5; " +
      "-fx-cursor: hand;";

  public ACCButton(String text) {
    super(text);
    setup();
  }

  private void setup() {
    setStyle(style);
    this.setOnMouseEntered(e -> this.setStyle(style + "-fx-opacity: 0.8;"));
    this.setOnMouseExited(e -> this.setStyle(style));
  }
}
