package AnimalCareCentre.client.views;

import AnimalCareCentre.client.ApiClient;
import AnimalCareCentre.client.ApiResponse;
import AnimalCareCentre.client.Navigator;
import AnimalCareCentre.client.Utility;
import AnimalCareCentre.client.components.*;
import AnimalCareCentre.client.enums.*;
import AnimalCareCentre.client.records.ShelterAnimal;
import AnimalCareCentre.server.model.LostAnimal;
import javafx.geometry.Pos;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;

public class RegisterLostAnimal {

  private Navigator nav;
  private Stage stage;

  public RegisterLostAnimal(Navigator nav, Stage stage) {
    this.nav = nav;
    this.stage = stage;
    show();
  }

  private void show() {
    ACCScene scene = new ACCScene(stage, new ACCVBox());
    new NavBar(nav.getLoggedRole(), nav, scene);

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

    Label locationLabel= new Label("Location: ");
    ACCComboBox<District> location= new ACCComboBox<>();
    location.getItems().addAll(District.values());

    Label contactLabel = new Label("Contact: ");
    ACCTextField contact = new ACCTextField();
      contact.setTextFormatter(new TextFormatter<>(change -> {
          String num = change.getControlNewText();
          if (num.matches("\\d{0,9}")) {
              return change;
          }
          return null;
      }));


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
          "age", age.getText(), "color", color.getValue(), "location", location.getValue(),"contact",contact.getText(), "description", desc.getText());
      registerAnimal(json, image);
      nav.lostAndFoundMenu();
    });

    back.setOnAction(e -> nav.lostAndFoundMenu());

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
        colorLabel, color, locationLabel, location,contactLabel,contact, descLabel, desc, uploadBox);
    vbox.setMaxWidth(250);
    vbox.setSpacing(10);
    scene.addItems(vbox, register, back);
  }

  private File uploadImage() {
    return Utility.selectImageFile(stage);
  }

  private void registerAnimal(String json, File[] image) {
    ApiResponse response = ApiClient.post("/lostandfound/create", json);
    if (response.isSuccess()) {
      LostAnimal animal = Utility.parseResponse(response.getBody(), LostAnimal.class);
      ApiResponse imageResponse = ApiClient.postWithFile("/lostandfound/" + animal.getId() + "/images", image[0]);
      if (!imageResponse.isSuccess()) {
        Utility.showAlert(AlertType.ERROR, "Error", imageResponse.getBody());
      }
    } else {
      Utility.showAlert(AlertType.ERROR, "Error", response.getBody());
    }
  }
}
