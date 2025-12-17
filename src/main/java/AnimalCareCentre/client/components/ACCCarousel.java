package AnimalCareCentre.client.components;

import javafx.animation.TranslateTransition;
import javafx.scene.effect.MotionBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Button;
import javafx.scene.shape.SVGPath;
import javafx.util.Duration;

import java.util.List;

/**
 * Carousel used in profiles to display all the item's images
 *
 */
public class ACCCarousel extends ACCVBox {
  private List<Image> images;
  private int currentIndex = 0;
  private StackPane stackPane;
  private Button prevButton;
  private Button nextButton;
  private ImageView currentImageView;

  public ACCCarousel(List<Image> images) {
    this.images = images;

    if (images == null || images.isEmpty()) {
      return;
    }

    stackPane = new StackPane();
    stackPane.setMinWidth(600);
    stackPane.setMinHeight(600);
    stackPane.setMaxWidth(600);
    stackPane.setMaxHeight(600);

    currentImageView = createImageView(images.get(0));
    stackPane.getChildren().add(currentImageView);

    ACCHBox buttonBox = new ACCHBox();

    prevButton = createArrowButton(true);
    nextButton = createArrowButton(false);

    prevButton.setOnAction(e -> navigateImages(-1));
    nextButton.setOnAction(e -> navigateImages(1));

    updateButtonStates();

    buttonBox.getChildren().addAll(prevButton, nextButton);

    this.getChildren().addAll(stackPane, buttonBox);
  }

  private ImageView createImageView(Image image) {
    ImageView imageView = new ImageView(image);
    imageView.setFitWidth(600);
    imageView.setFitHeight(600);
    imageView.setPreserveRatio(true);
    imageView.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0, 0, 2);");
    return imageView;
  }

  private Button createArrowButton(boolean isLeft) {
    Button button = new Button();
    button.setStyle(
        "-fx-background-color: transparent;" +
            "-fx-border-color: #ccc;" +
            "-fx-border-radius: 50%;" +
            "-fx-background-radius: 50%;" +
            "-fx-padding: 15;" +
            "-fx-cursor: hand;" +
            "-fx-min-width: 50px;" +
            "-fx-min-height: 50px;");

    SVGPath arrow = new SVGPath();
    if (isLeft) {
      arrow.setContent("M 15 5 L 5 15 L 15 25");
    } else {
      arrow.setContent("M 5 5 L 15 15 L 5 25");
    }
    arrow.setStroke(javafx.scene.paint.Color.web("#333"));
    arrow.setStrokeWidth(2);
    arrow.setFill(javafx.scene.paint.Color.TRANSPARENT);

    button.setGraphic(arrow);

    button.setOnMouseEntered(e -> {
      button.setStyle(
          "-fx-background-color: rgba(255, 255, 255, 0.2);" +
              "-fx-border-color: #666;" +
              "-fx-border-radius: 50%;" +
              "-fx-background-radius: 50%;" +
              "-fx-padding: 15;" +
              "-fx-cursor: hand;" +
              "-fx-min-width: 50px;" +
              "-fx-min-height: 50px;" +
              "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 8, 0, 0, 2);");
    });

    button.setOnMouseExited(e -> {
      button.setStyle(
          "-fx-background-color: transparent;" +
              "-fx-border-color: #ccc;" +
              "-fx-border-radius: 50%;" +
              "-fx-background-radius: 50%;" +
              "-fx-padding: 15;" +
              "-fx-cursor: hand;" +
              "-fx-min-width: 50px;" +
              "-fx-min-height: 50px;");
    });

    return button;
  }

  private void navigateImages(int direction) {
    if (images.size() <= 1)
      return;

    int newIndex = currentIndex + direction;
    if (newIndex < 0 || newIndex >= images.size())
      return;

    prevButton.setDisable(true);
    nextButton.setDisable(true);

    ImageView newImageView = createImageView(images.get(newIndex));
    ImageView oldImageView = currentImageView;

    double distance = direction > 0 ? 600 : -600;

    TranslateTransition newImageTransition = new TranslateTransition(Duration.millis(300), newImageView);
    TranslateTransition oldImageTransition = new TranslateTransition(Duration.millis(300), oldImageView);

    newImageTransition.setFromX(distance);
    newImageTransition.setToX(0);

    oldImageTransition.setByX(-distance);

    MotionBlur motionBlur = new MotionBlur();
    motionBlur.setRadius(50);
    motionBlur.setAngle(0);

    stackPane.getChildren().add(newImageView);

    newImageView.setEffect(motionBlur);
    oldImageView.setEffect(motionBlur);

    newImageTransition.setOnFinished(event -> {
      stackPane.getChildren().remove(oldImageView);

      newImageView.setEffect(null);
      oldImageView.setEffect(null);

      currentImageView = newImageView;
      currentIndex = newIndex;

      updateButtonStates();
    });

    oldImageTransition.setOnFinished(event -> {
      oldImageView.setEffect(null);
    });

    newImageTransition.play();
    oldImageTransition.play();
  }

  private void updateButtonStates() {
    prevButton.setDisable(currentIndex == 0);
    nextButton.setDisable(currentIndex == images.size() - 1);
  }
}
