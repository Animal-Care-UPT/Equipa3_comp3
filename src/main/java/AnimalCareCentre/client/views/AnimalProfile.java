package AnimalCareCentre.client.views;

import AnimalCareCentre.client.ApiClient;
import AnimalCareCentre.client.ApiResponse;
import AnimalCareCentre.client.Navigator;
import AnimalCareCentre.client.Utility;
import AnimalCareCentre.client.components.*;
import AnimalCareCentre.client.enums.AdoptionType;
import AnimalCareCentre.client.records.Adoption;
import AnimalCareCentre.client.records.ShelterAnimal;
import AnimalCareCentre.client.records.Sponsorship;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import java.util.List;

public class AnimalProfile {

  private Navigator nav;
  private Stage stage;
  private ShelterAnimal animal;
  private ACCPopover popover;

  public AnimalProfile(Navigator nav, Stage stage, ShelterAnimal animal) {
    this.nav = nav;
    this.stage = stage;
    this.animal = animal;
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

    imageView.setOnMouseClicked(event -> {
      // img popover soon
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

    ACCHBox buttonsBox = new ACCHBox();

    ACCMenuButton sponsorshipsButton = new ACCMenuButton("Sponsorships");

    ACCMenuButton historyButton = new ACCMenuButton("History");

    sponsorshipsButton.setOnAction(e -> sponsorshipPopover(sponsorshipsButton));
    historyButton.setOnAction(e -> adoptionHistory(historyButton));

    ACCMenuButton sponsorButton = new ACCMenuButton("Sponsor");
    sponsorButton.setOnAction(e -> newSponsorshipPopover(sponsorButton));

    ACCMenuButton adoptButton = new ACCMenuButton("Adopt");
    adoptButton.setOnAction(e -> requestAdoptionPopover(adoptButton, "ADOPTION"));
    ACCMenuButton fosterButton = new ACCMenuButton("Foster");
    fosterButton.setOnAction(e -> requestAdoptionPopover(fosterButton, "FOSTER"));

    if (nav.getLoggedRole().equals("ROLE_USER")) {
      buttonsBox.addItems(sponsorButton);
      if (animal.adoptionType().equals(AdoptionType.FOR_FOSTER)) {
        buttonsBox.addItems(fosterButton);
      } else {
        buttonsBox.addItems(adoptButton);
      }
    } else {
      buttonsBox.addItems(sponsorshipsButton, historyButton);
    }
    mainBox.addItems(imgContainer, animalProfile);
    scene.addItems(mainBox, buttonsBox);
  }

  private void sponsorshipPopover(ACCMenuButton button) {

    ApiResponse response = ApiClient.get("/sponsorships/animal/" + animal.id());

    ACCVBox content = new ACCVBox();
    content.setPadding(new Insets(15));

    if (!response.isSuccess()) {
      Utility.showAlert(Alert.AlertType.ERROR, "Failed to load sponsorships", response.getBody());
      return;
    } else {
      List<Sponsorship> sponsors = Utility.parseList(response.getBody(), Sponsorship.class);

      if (sponsors.isEmpty()) {
        content.addItems(new Label("This animal has no sponsors :( \n Become a sponsor today and help an animal :)"));
      } else {
        for (Sponsorship sponsor : sponsors) {
          Label label = new Label(
              sponsor.userName() + "\nTotal: " + sponsor.amount() + "€\nSince: " + sponsor.startDate());
          label.setStyle("-fx-padding: 5; -fx-background-color: #f0f0f0; -fx-background-radius: 5;");
          content.addItems(label);
        }
      }
    }
    ScrollPane scroll = new ScrollPane(content);
    scroll.setFitToWidth(true);
    scroll.setPrefHeight(200);

    popover = new ACCPopover(scroll, "Sponsorships");
    popover.show(stage);

  }

  private void adoptionHistory(ACCMenuButton button) {

    ApiResponse response = ApiClient.get("/adoptions/animal/" + animal.id());

    ACCVBox content = new ACCVBox();
    content.setPadding(new Insets(15));

    if (!response.isSuccess()) {
      Utility.showAlert(Alert.AlertType.ERROR, "Error", response.getBody());
      return;
    }
    List<Adoption> adoptions = Utility.parseList(response.getBody(), Adoption.class);
    if (adoptions == null || adoptions.isEmpty()) {
      content.addItems(new Label("No record of adoptions or fosterings"));
    } else {
      for (Adoption adoption : adoptions) {
        Label label = new Label(
            adoption.animal().getDisplayName() + "\n " + adoption.type() + "\n at " + adoption.adoptionDate());
        label.setStyle("-fx-padding: 5; -fx-background-color: #f0f0f0; -fx-background-radius: 5;");
        content.addItems(label);
      }
    }

    ScrollPane scroll = new ScrollPane(content);
    scroll.setFitToWidth(true);
    scroll.setPrefHeight(200);
    popover = new ACCPopover(scroll, "Adoptions/Fosters History");
    popover.show(stage);
  }

  private void requestAdoptionPopover(ACCMenuButton button, String adoptionType) {
    ACCVBox content = new ACCVBox();
    content.setPadding(new Insets(15));

    Label confirmLabel = new Label("Do you confirm your decision? Animals are a great responsibility");
    confirmLabel.setStyle("-fx-font-size: 12px;");

    ACCMenuButton confirmButton = new ACCMenuButton("Confirm");
    ACCMenuButton cancelButton = new ACCMenuButton("Cancel");

    confirmButton.setOnAction(e -> {
      popover.hide();
      requestAdoption(adoptionType);
    });

    cancelButton.setOnAction(e -> popover.hide());

    content.addItems(confirmLabel, confirmButton, cancelButton);

    popover = new ACCPopover(content, "Confirm " + adoptionType);
    popover.show(stage);
  }

  private void requestAdoption(String adoptionType) {

    String url = "/adoptions/request?animalId=" + animal.id() + "&type=" + adoptionType;

    ApiResponse response = ApiClient.post(url, "");

    if (response.isSuccess()) {
      String message = "Your request has been submitted successfully!\nThe shelter will review it soon.";
      Utility.showAlert(Alert.AlertType.INFORMATION, adoptionType + " Request Submitted", message);
    } else {
      String errorMessage = response.getBody() != null ? response.getBody()
          : "An error occurred while processing your request.";
      Utility.showAlert(Alert.AlertType.ERROR, "Request Failed", errorMessage);
    }

  }

  private void newSponsorshipPopover(ACCMenuButton button) {
    ACCVBox content = new ACCVBox();
    content.setPadding(new Insets(15));

    Label titleLabel = new Label("Become a sponsor for " + animal.getDisplayName());
    titleLabel.setStyle("-fx-font-weight: bold;");

    Label amountLabel = new Label("Enter sponsorship amount (€):");
    amountLabel.setStyle("-fx-font-size: 12px;");
    ACCTextField amountField = new ACCTextField();
    amountField.setPromptText("0.00");

    ACCMenuButton submitButton = new ACCMenuButton("Submit");
    ACCMenuButton cancelButton = new ACCMenuButton("Cancel");

    submitButton.setOnAction(e -> {
      String input = amountField.getText();

      try {
        float amount = Float.parseFloat(input);

        if (amount <= 0) {
          Utility.showAlert(Alert.AlertType.ERROR, "Invalid amount", "Please enter an amount greater than 0.");
          return;
        }

        popover.hide();
        createSponsorship(amount);
      } catch (NumberFormatException ex) {
        Utility.showAlert(Alert.AlertType.ERROR, "Invalid amount", "Please enter a valid number");
      }
    });

    cancelButton.setOnAction(e -> {
      popover.hide();
    });
    content.addItems(titleLabel, amountLabel, amountField, submitButton, cancelButton);

    popover = new ACCPopover(content, "Confirm ");
    popover.show(stage);
  }

  private void createSponsorship(float amount) {
    String url = "/sponsorships/create?animalId=" + animal.id() + "&amount=" + amount;

    ApiResponse response = ApiClient.post(url, "");

    if (response.isSuccess()) {
      Utility.showAlert(Alert.AlertType.INFORMATION, "Sponsorship", "Thank you for becoming a sponsor");
    } else {
      Utility.showAlert(Alert.AlertType.ERROR, "Request Failed", response.getBody());
    }

  }

}
