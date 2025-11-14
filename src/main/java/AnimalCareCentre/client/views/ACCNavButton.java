package AnimalCareCentre.client.views;

import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;

public class ACCNavButton extends Button {

  public ACCNavButton(String text) {
    setText(text);
    setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 16px;");
    setCursor(Cursor.HAND);
    setHoverStyle();
  }

  private void setHoverStyle() {
    DropShadow glow = new DropShadow();
    glow.setColor(Color.web("#ffffff"));
    glow.setRadius(10);
    glow.setSpread(0.5);

    setOnMouseEntered(e -> {
      setEffect(glow);
      setTextFill(Color.web("#ffd27f"));
    });

    setOnMouseExited(e -> {
      setEffect(null);
      setTextFill(Color.WHITE);
    });
  }
}
