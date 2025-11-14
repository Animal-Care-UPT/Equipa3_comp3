package AnimalCareCentre.client.views;

import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;

public class ACCButton extends Button {

  private static final Font QUICKSAND_BOLD;

  static {
    QUICKSAND_BOLD = Font.loadFont(ACCButton.class.getResourceAsStream("/fonts/Quicksand-Bold.ttf"), 20);
  }

  public ACCButton(String text) {
    setText(text);
    setFont(QUICKSAND_BOLD);
    setStyle(
        "-fx-background-color: transparent; -fx-text-fill: #6F4426;");
    setCursor(Cursor.HAND);
    setHoverStyle();
  }

  private void setHoverStyle() {
    DropShadow glow = new DropShadow();
    glow.setColor(Color.web("#d6c5ab"));
    glow.setRadius(10);
    glow.setSpread(0.5);

    setOnMouseEntered(e -> {
      setEffect(glow);
      setStyle(
          "-fx-background-color: transparent; -fx-text-fill: #6F4426; -fx-underline: true");
      setTextFill(Color.web("#6F4426"));
    });

    setOnMouseExited(e -> {
      setEffect(null);
      setStyle(
          "-fx-background-color: transparent; -fx-text-fill: #6F4426;");
      setTextFill(Paint.valueOf("#6F4426"));
    });
  }
}
