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
import AnimalCareCentre.client.records.Shelter;
import AnimalCareCentre.client.records.ShelterAnimal;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.TilePane;
import javafx.scene.text.Font;

public class SearchShelterPopover {

  private Navigator nav;
  private ACCPopover popover;

  public SearchShelterPopover(Navigator nav) {
    this.nav = nav;
  }

  public void show(Button button) {
    ACCVBox content = buildContent();
    popover = new ACCPopover(content, "Search Shelters");
    popover.setTitle("Search Shelter");
    popover.show(button);
  }

  private ACCVBox buildContent() {
    ACCVBox content = new ACCVBox();
    content.setPadding(new Insets(15, 15, 15, 15));

    ACCTextField searchField = new ACCTextField();
    searchField.setPromptText("Search...");

    ACCButton search = new ACCButton("Search");
    search.setOnAction(e -> searchShelter(searchField.getText()));

    content.addItems(searchField, search);
    return content;
  }

  private void searchShelter(String keyword) {
    if (nav.getLoggedRole().equals("ROLE_ADMIN")) {
      ApiResponse response = ApiClient.get("/shelters/all?keyword+" + keyword);
      if (!response.isSuccess()) {
        Utility.showAlert(AlertType.ERROR, "Error", response.getBody());
      } else {
        List<Shelter> shelters = Utility.parseList(response.getBody(), Shelter.class);
        nav.searchShelter(shelters);
      }
    } else {
      ApiResponse response = ApiClient.get("/shelters/?keyword+" + keyword);
      if (!response.isSuccess()) {
        Utility.showAlert(AlertType.ERROR, "Error", response.getBody());
      } else {
        List<Shelter> shelters = Utility.parseList(response.getBody(), Shelter.class);
        nav.searchShelter(shelters);

      }
    }
  }
}
