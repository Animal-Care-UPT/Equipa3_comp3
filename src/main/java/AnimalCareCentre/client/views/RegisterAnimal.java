package AnimalCareCentre.client.views;

import AnimalCareCentre.client.Navigator;
import AnimalCareCentre.client.components.ACCButton;
import AnimalCareCentre.client.components.ACCComboBox;
import AnimalCareCentre.client.components.ACCScene;
import AnimalCareCentre.client.components.ACCTextField;
import AnimalCareCentre.client.components.ACCVBox;
import AnimalCareCentre.client.enums.AdoptionType;
import AnimalCareCentre.client.enums.AnimalColor;
import AnimalCareCentre.client.enums.AnimalGender;
import AnimalCareCentre.client.enums.AnimalSize;
import AnimalCareCentre.client.enums.AnimalType;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextFormatter;
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

  private void show() {
    ACCScene scene = new ACCScene(stage, new ACCVBox());
    new NavBar(nav.getLoggedRole(), nav, scene);

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
    ACCButton register = new ACCButton("Register");
    ACCButton back = new ACCButton("Back");

    register.setOnAction(e -> registerAnimal());
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

    VBox vbox = new VBox();
    vbox.setAlignment(Pos.CENTER_LEFT);
    vbox.getChildren().addAll(typeLabel, type, nameLabel, name, breedLabel, breed, sizeLabel, size, genderLabel,
        gender, ageLabel, age,
        colorLabel, color, adoptTypeLabel, adoptType);
    vbox.setMaxWidth(250);
    vbox.setSpacing(10);
    scene.addItems(vbox, register, back);
  }

  private void registerAnimal() {
  }
}
