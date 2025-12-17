package AnimalCareCentre.client.views;

import java.io.File;

import AnimalCareCentre.client.ApiClient;
import AnimalCareCentre.client.ApiResponse;
import AnimalCareCentre.client.Navigator;
import AnimalCareCentre.client.Utility;
import AnimalCareCentre.client.components.ACCButton;
import AnimalCareCentre.client.components.ACCComboBox;
import AnimalCareCentre.client.components.ACCHBox;
import AnimalCareCentre.client.components.ACCScene;
import AnimalCareCentre.client.components.ACCTextButton;
import AnimalCareCentre.client.components.ACCTextField;
import AnimalCareCentre.client.components.ACCVBox;
import AnimalCareCentre.client.enums.AdoptionType;
import AnimalCareCentre.client.enums.AnimalColor;
import AnimalCareCentre.client.enums.AnimalGender;
import AnimalCareCentre.client.enums.AnimalSize;
import AnimalCareCentre.client.enums.AnimalType;
import AnimalCareCentre.client.records.ShelterAnimal;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class RegisterAnimal {

  private Navigator nav;
  private Stage stage;

  public RegisterAnimal(Navigator nav, Stage stage) {
    this.nav = nav;
    this.stage = stage;
    show();
  }

  /**
   * Displays the register animal page
   */
  private void show() {
    ACCScene scene = new ACCScene(stage, new ACCVBox());
    new NavBar(Navigator.getLoggedRole(), nav, scene);

    final File[] image = { null };
    Label typeLabel = new Label("Animal type:");
    ACCComboBox<AnimalType> type = new ACCComboBox<>();
    type.getItems().addAll(AnimalType.values());
    Label nameLabel = new Label("Name:");
    ACCTextField name = new ACCTextField();
    Label breedLabel = new Label("Breed:");
    ACCComboBox<String> breed = new ACCComboBox<>();
    Label sizeLabel = new Label("Size:");
    ACCComboBox<AnimalSize> size = new ACCComboBox<>();
    size.getItems().addAll(AnimalSize.values());
    Label genderLabel = new Label("Gender:");
    ACCComboBox<AnimalGender> gender = new ACCComboBox<>();
    gender.getItems().addAll(AnimalGender.values());
    Label ageLabel = new Label("Age:");
    ACCTextField age = new ACCTextField();
    Label colorLabel = new Label("Color:");
    ACCComboBox<AnimalColor> color = new ACCComboBox<>();
    color.getItems().addAll(AnimalColor.values());
    Label adoptTypeLabel = new Label("Adoption Type");
    ACCComboBox<AdoptionType> adoptType = new ACCComboBox<>();
    adoptType.getItems().addAll(AdoptionType.values());
    Label descLabel = new Label("Description:");
    ACCTextField desc = new ACCTextField();
    desc.setMinHeight(100);
    Label uploadStatus = new Label("No image selected");
    uploadStatus.setStyle("-fx-text-fill: red;");

    ACCTextButton upload = new ACCTextButton("Upload Image");
    ACCButton register = new ACCButton("Register");
    ACCButton back = new ACCButton("Back");

    upload.setOnAction(e -> {
      image[0] = uploadImage();
      if (image[0] != null) {
        uploadStatus.setText("Image uploaded");
        uploadStatus.setStyle("-fx-text-fill: green;");
      }
    });

    register.setOnAction(e -> {
      String json = Utility.jsonString("type", type.getValue(), "name", name.getText(), "race",
          breed.getValue(), "size", size.getValue(), "gender", gender.getValue(),
          "age", age.getText(), "color", color.getValue(), "adoptionType", adoptType.getValue(), "description",
          desc.getText());
      registerAnimal(json, image);
      nav.shelterHomepage();
    });

    back.setOnAction(e -> nav.shelterHomepage());

    age.setTextFormatter(new TextFormatter<>(change -> {
      String num = change.getControlNewText();
      if (num.matches("\\d{0,2}")) {
        return change;
      }
      return null;
    }));

    type.valueProperty().addListener((obs, old, selected) -> {
      breed.getItems().clear();
      breed.getItems().addAll(selected.getBreeds());
    });

    ACCHBox uploadBox = new ACCHBox();
    uploadBox.addItems(upload, uploadStatus);
    VBox vbox = new VBox();
    vbox.setAlignment(Pos.CENTER_LEFT);
    vbox.getChildren().addAll(typeLabel, type, nameLabel, name, breedLabel, breed, sizeLabel, size, genderLabel,
        gender, ageLabel, age,
        colorLabel, color, adoptTypeLabel, adoptType, descLabel, desc, uploadBox);
    vbox.setMaxWidth(250);
    vbox.setSpacing(10);
    scene.addItems(vbox, register, back);
  }

  /**
   * Uses the utility method to get an image from the file system
   */
  private File uploadImage() {
    return Utility.selectImageFile(stage);
  }

  /**
   * Registers an animal and adds an image
   */
  private void registerAnimal(String json, File[] image) {
    ApiResponse response = ApiClient.post("/shelteranimals/register", json);
    if (response.isSuccess()) {
      ShelterAnimal animal = Utility.parseResponse(response.getBody(), ShelterAnimal.class);
      ApiResponse imageResponse = ApiClient.postWithFile("/shelteranimals/" + animal.id() + "/images", image[0]);
      if (!imageResponse.isSuccess()) {
        Utility.showAlert(AlertType.ERROR, "Error", imageResponse.getBody());
      }
    } else {
      Utility.showAlert(AlertType.ERROR, "Error", response.getBody());
    }
  }
}
