package AnimalCareCentre.client.views;

import java.util.List;

import AnimalCareCentre.client.ApiClient;
import AnimalCareCentre.client.ApiResponse;
import AnimalCareCentre.client.Navigator;
import AnimalCareCentre.client.Utility;
import AnimalCareCentre.client.components.*;
import AnimalCareCentre.client.records.Shelter;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Alert.AlertType;

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
    if (Navigator.getLoggedRole().equals("ROLE_ADMIN")) {
      ApiResponse response = ApiClient.get("/shelters/all?keyword=" + keyword);
      if (!response.isSuccess()) {
        Utility.showAlert(AlertType.ERROR, "Error", response.getBody());
      } else {
        List<Shelter> shelters = Utility.parseList(response.getBody(), Shelter.class);
        nav.searchShelter(shelters);
      }
    } else {
      ApiResponse response = ApiClient.get("/shelters/?keyword=" + keyword);
      if (!response.isSuccess()) {
        Utility.showAlert(AlertType.ERROR, "Error", response.getBody());
      } else {
        List<Shelter> shelters = Utility.parseList(response.getBody(), Shelter.class);
        nav.searchShelter(shelters);

      }
    }
  }
}
