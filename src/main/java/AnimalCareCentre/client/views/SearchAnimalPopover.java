package AnimalCareCentre.client.views;

import java.util.List;

import AnimalCareCentre.client.ApiClient;
import AnimalCareCentre.client.ApiResponse;
import AnimalCareCentre.client.Navigator;
import AnimalCareCentre.client.Utility;
import AnimalCareCentre.client.components.*;
import AnimalCareCentre.client.enums.AdoptionType;
import AnimalCareCentre.client.enums.AnimalGender;
import AnimalCareCentre.client.enums.AnimalType;
import AnimalCareCentre.client.records.ShelterAnimal;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.TilePane;
import javafx.scene.text.Font;

public class SearchAnimalPopover {

  private Navigator nav;
  private ACCPopover popover;

  public SearchAnimalPopover(Navigator nav) {
    this.nav = nav;
  }

  /**
   * Displays the search animal popover
   */
  public void show(Button button) {
    ACCVBox content = buildContent();
    popover = new ACCPopover(content, "Search Animals");
    popover.setTitle("Search Animal");
    popover.show(button);
  }

  /**
   * Populates the popover
   */
  private ACCVBox buildContent() {
    ACCVBox content = new ACCVBox();
    content.setPadding(new Insets(15, 15, 15, 15));

    ACCTextField searchField = new ACCTextField();
    searchField.setPromptText("Search...");

    Label aniType = new Label("Type:");
    aniType.setFont(new Font(12.9));
    ACCComboBox<AnimalType> type = new ACCComboBox<>(true);
    type.getItems().addAll(AnimalType.values());
    Label aniGender = new Label("Gender:");
    aniGender.setFont(new Font(12.9));
    ACCComboBox<AnimalGender> gender = new ACCComboBox<>(true);
    gender.getItems().addAll(AnimalGender.values());
    Label adoType = new Label("Adoption:");
    adoType.setFont(new Font(12.9));
    ACCComboBox<AdoptionType> adoptionType = new ACCComboBox<>(true);
    adoptionType.getItems().addAll(AdoptionType.values());
    TilePane pane = new TilePane();
    pane.setAlignment(Pos.CENTER);
    pane.setPrefColumns(3);
    pane.getChildren().addAll(aniType, aniGender, adoType, type, gender, adoptionType);

    ACCButton search = new ACCButton("Search");
    search.setOnAction(e -> {
      String json = Utility.jsonString("keyword", searchField.getText(), "type", type.getValue(),
          "gender", gender.getValue(), "adoptionType", adoptionType.getValue());
      searchAnimal(json);
    });

    content.addItems(searchField, pane, search);
    return content;
  }

  /**
   * Searches for the animal
   */
  private void searchAnimal(String json) {
    ApiResponse response = ApiClient.post("/shelteranimals/search", json);
    if (!response.isSuccess()) {
      Utility.showAlert(AlertType.ERROR, "Error", response.getBody());
    } else {
      List<ShelterAnimal> animals = Utility.parseList(response.getBody(), ShelterAnimal.class);
      nav.searchAnimal(animals);
    }
  }
}
