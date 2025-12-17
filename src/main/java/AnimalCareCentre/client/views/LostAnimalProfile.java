package AnimalCareCentre.client.views;

import AnimalCareCentre.client.ApiClient;
import AnimalCareCentre.client.ApiResponse;
import AnimalCareCentre.client.Navigator;
import AnimalCareCentre.client.Utility;
import AnimalCareCentre.client.components.*;
import AnimalCareCentre.client.records.Adoption;
import AnimalCareCentre.client.records.LostAnimal;
import AnimalCareCentre.client.records.ShelterAnimal;
import AnimalCareCentre.client.records.Sponsorship;
import io.github.makbn.jlmap.listener.event.ClickEvent;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;

public class LostAnimalProfile {

  private Navigator nav;
  private Stage stage;
  private LostAnimal animal;
  private ACCPopover popover;

  public LostAnimalProfile(Navigator nav, Stage stage, LostAnimal animal) {
    this.nav = nav;
    this.stage = stage;
    this.animal = animal;
    show();
  }

  /**
   * Displays the profile of a lost animal
   */
  private void show() {
    ACCScene scene = new ACCScene(stage, new ACCVBox());
    new NavBar(nav.getLoggedRole(), nav, scene);

    ACCHBox mainBox = new ACCHBox();
    mainBox.setSpacing(0);

    ACCVBox imgContainer = new ACCVBox();
    imgContainer.setMinWidth(420);
    imgContainer.setMinHeight(420);
    ImageView imageView = new ImageView();
    imageView.setFitWidth(400);
    imageView.setFitHeight(400);
    imageView.setPreserveRatio(true);
    imgContainer.addItems(imageView);

    imageView.setOnMouseEntered(event -> {
      imageView.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 15, 0, 0, 3); -fx-cursor: hand;");

      imageView.setFitWidth(420);
      imageView.setFitHeight(420);
    });

    imageView.setOnMouseExited(event -> {
      imageView.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0, 0, 2);");
      imageView.setFitWidth(400);
      imageView.setFitHeight(400);
      imageView.setCursor(Cursor.HAND);
    });

    imageView.setOnMouseClicked(event -> {
      // imgCarousel();
    });

    String imageUrl = animal.getImagePath();
    if (imageUrl != null && !imageUrl.isEmpty()) {
      ApiResponse response = ApiClient.get(imageUrl);
      Image image = Utility.parseImage(response);
      if (image != null) {
        imageView.setImage(image);
        imageView.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0, 0, 2);");
      }
    }

    Label animalProfile = new Label(animal.toString());
    animalProfile.setStyle("-fx-font-size: 16px; -fx-line-spacing: 5px;");

    mainBox.addItems(imgContainer, animalProfile);
    scene.addItems(mainBox);
  }

}
