package AnimalCareCentre.client.components;

import AnimalCareCentre.client.records.Displayable;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class ACCCard<T extends Displayable> extends VBox {

  public ACCCard(T item, Runnable onClickHandler) {
    this.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
    this.setPadding(new Insets(10));
    this.setSpacing(10);
    this.setMaxWidth(300);

    ImageView imageView = new ImageView(item.getImagePath()); //placeholder for actual image stuff
    imageView.setFitWidth(280);
    imageView.setFitHeight(200);
    imageView.setPreserveRatio(true);

    Label nameLabel = new Label(item.getDisplayName());
    nameLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

    Label infoLabel = new Label(item.getDisplayInfo());
    Label descLabel = new Label(item.getDescription());

    this.getChildren().addAll(imageView, nameLabel, infoLabel, descLabel);

    this.setOnMouseClicked(e -> onClickHandler.run());
    this.setCursor(Cursor.HAND);

    this.setOnMouseEntered(e -> this.setStyle("-fx-background-color: #f0f0f0; -fx-background-radius: 10;"));
    this.setOnMouseExited(e -> this.setStyle("-fx-background-color: white; -fx-background-radius: 10;"));
  }
}
