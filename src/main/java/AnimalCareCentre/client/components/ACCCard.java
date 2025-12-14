package AnimalCareCentre.client.components;

import AnimalCareCentre.client.records.Displayable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class ACCCard<T extends Displayable> extends ACCVBox {

  public ACCCard(T item, Image image, Runnable onClickHandler) {
    this.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
    this.setPrefWidth(250);
    this.setMaxWidth(250);
    this.setMinHeight(380);
    this.setMaxHeight(380);

    VBox imageContainer = new VBox();
    imageContainer.setAlignment(Pos.CENTER);
    imageContainer.setPrefHeight(250);
    imageContainer.setMinHeight(250);
    imageContainer.setMaxHeight(250);
    imageContainer.setStyle("-fx-background-color: white;");

    if (image != null) {
      ImageView imageView = new ImageView(image);
      imageView.setFitWidth(250);
      imageView.setFitHeight(250);
      imageView.setPreserveRatio(true);
      imageView.setSmooth(true);
      imageContainer.getChildren().add(imageView);
    } else {
      Label placeholder = new Label("No Image");
      placeholder.setStyle("-fx-text-fill: #999; -fx-font-size: 14px;");
      imageContainer.getChildren().add(placeholder);
    }

    VBox infoBox = new VBox(5);
    infoBox.setAlignment(Pos.TOP_LEFT);
    infoBox.setPadding(new Insets(10));
    infoBox.setPrefHeight(150);
    infoBox.setMaxHeight(150);

    Label nameLabel = new Label(item.getDisplayName());
    nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
    nameLabel.setMaxWidth(230);

    Label infoLabel = new Label(item.getDisplayInfo());
    infoLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #555;");
    infoLabel.setMaxWidth(230);

    Label descLabel = new Label(item.getDescription());
    descLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #777;");
    descLabel.setMaxWidth(230);
    descLabel.setMaxHeight(60);

    infoBox.getChildren().addAll(nameLabel, infoLabel, descLabel);
    addItems(imageContainer, infoBox);

    this.setOnMouseClicked(e -> onClickHandler.run());
    this.setCursor(Cursor.HAND);

    this.setOnMouseEntered(e -> {
      this.setStyle("-fx-background-color: #f9f9f9; -fx-background-radius: 10;");
      imageContainer.setStyle("-fx-background-color: #f9f9f9; -fx-background-radius: 10;");
    });

    this.setOnMouseExited(e -> {
      this.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
      imageContainer.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
    });
  }
}
