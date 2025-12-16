package AnimalCareCentre.client.views;

import AnimalCareCentre.client.ApiClient;
import AnimalCareCentre.client.ApiResponse;
import AnimalCareCentre.client.Navigator;
import AnimalCareCentre.client.Utility;
import AnimalCareCentre.client.components.*;
import AnimalCareCentre.client.records.Shelter;
import AnimalCareCentre.server.model.ShelterDonation;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.util.List;

public class ShelterProfile {

  private Navigator nav;
  private Stage stage;
  private Shelter shelter;
  private ACCPopover popover;

  public ShelterProfile(Navigator nav, Stage stage, Shelter shelter) {
    this.nav = nav;
    this.stage = stage;
    this.shelter = shelter;
    show();
  }

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

    imageView.setOnMouseClicked(event -> imgCarousel());

    String imageUrl = shelter.getImagePath();
    if (imageUrl != null && !imageUrl.isEmpty()) {
      ApiResponse response = ApiClient.get(imageUrl);
      Image image = Utility.parseImage(response);
      if (image != null) {
        imageView.setImage(image);
        imageView.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0, 0, 2);");
      }
    }

    Label shelterProfile = new Label(
        shelter.toString());
    shelterProfile.setStyle("-fx-font-size: 16px; -fx-line-spacing: 5px;");

    ACCHBox buttonsBox = new ACCHBox();

    ACCMenuButton donationsHistoryButton = new ACCMenuButton("Donations");
    ACCMenuButton donationsButton = new ACCMenuButton("Donate");

    donationsHistoryButton.setOnAction(e ->

    donationsPopover(donationsHistoryButton));
    donationsButton.setOnAction(e -> newDonationPopover(donationsButton));

    if (nav.getLoggedRole().equals("ROLE_USER")) {
      buttonsBox.addItems(donationsButton);
    } else { // missing change status for admin
      buttonsBox.addItems(donationsHistoryButton);
    }

    mainBox.addItems(imgContainer, shelterProfile);
    scene.addItems(mainBox, buttonsBox);
  }

  private void donationsPopover(ACCMenuButton button) {
    ApiResponse response = ApiClient.get("/donations/admin/" + shelter.id());

    ACCVBox content = new ACCVBox();
    content.setPadding(new Insets(15));

    if (!response.isSuccess()) {
      Utility.showAlert(Alert.AlertType.ERROR, "Error", response.getBody());
      return;
    }
    List<ShelterDonation> donations = Utility.parseList(response.getBody(), ShelterDonation.class);

    if (donations == null || donations.isEmpty()) {
      content.addItems(new Label("No donations found!"));
    } else {
      for (ShelterDonation donation : donations) {
        Label label = new Label(donation.toString());
        label.setStyle("-fx-padding: 5; -fx-background-color: #f0f0f0; -fx-background-radius: 5;");
        content.addItems(label);
      }
    }

    ScrollPane scroll = new ScrollPane(content);
    scroll.setFitToWidth(true);
    scroll.setPrefHeight(200);

    popover = new ACCPopover(scroll, "Donations History");
    popover.show(stage);
  }

  private void imgCarousel() {
    ApiResponse response = ApiClient.get("/shelters/" + shelter.id() + "/images");
    if (!response.isSuccess()) {
      Utility.showAlert(Alert.AlertType.ERROR, "Error", response.getBody());
      return;
    }
    List<Image> images = Utility.parseImageList(response);

    ACCCarousel carousel = new ACCCarousel(images);
    carousel.setPadding(new Insets(20));

    popover = new ACCPopover(carousel, shelter.name() + " - Images");
    popover.show(stage);
  }

  private void newDonationPopover(ACCMenuButton button) {
    ACCVBox content = new ACCVBox();
    content.setPadding(new Insets(15));

    Label titleLabel = new Label("Make a donation to " + shelter.name());
    titleLabel.setStyle("-fx-font-weight: bold;");

    Label instructionLabel = new Label("Enter donation amount (€):");
    instructionLabel.setStyle("-fx-font-size: 12px;");

    ACCTextField amountField = new ACCTextField();
    amountField.setPromptText("0.00");

    ACCMenuButton submitButton = new ACCMenuButton("Submit");
    ACCMenuButton cancelButton = new ACCMenuButton("Cancel");

    submitButton.setOnAction(e -> {
      String input = amountField.getText();

      try {
        float amount = Float.parseFloat(input);

        if (amount <= 0) {
          Utility.showAlert(Alert.AlertType.ERROR, "Invalid amount", "Please enter a valid amount");
          return;
        }

        popover.hide();
        createDonation(amount);

      } catch (NumberFormatException ex) {
        Utility.showAlert(Alert.AlertType.ERROR, "Invalid input", "Please enter a valid number");
      }
    });

    cancelButton.setOnAction(e -> popover.hide());

    content.addItems(titleLabel, instructionLabel, amountField, submitButton, cancelButton);

    popover = new ACCPopover(content, "Donate to Shelter");
    popover.show(stage);
  }

  private void createDonation(float amount) {
    String requestBody = Utility.jsonString("Shelter", shelter.name(), "amount", amount);

    ApiResponse response = ApiClient.post("/donations/create", requestBody);

    if (response.isSuccess()) {
      Utility.showAlert(Alert.AlertType.INFORMATION, "Donation Created", "Successfully created donation");
    } else {
      String errorMessage = response.getBody() != null ? response.getBody()
          : "An error occurred while processing your request.";
      Utility.showAlert(Alert.AlertType.ERROR, "Donation failed", errorMessage);
    }
  }

}
