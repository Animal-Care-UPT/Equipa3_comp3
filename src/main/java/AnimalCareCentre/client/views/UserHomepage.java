package AnimalCareCentre.client.views;

import java.lang.module.ModuleDescriptor.Requires;
import java.util.List;

import org.checkerframework.checker.units.qual.A;

import AnimalCareCentre.client.ApiClient;
import AnimalCareCentre.client.ApiResponse;
import AnimalCareCentre.client.Navigator;
import AnimalCareCentre.client.components.*;
import AnimalCareCentre.client.Utility;
import AnimalCareCentre.server.dto.AdoptionDTO;
import AnimalCareCentre.server.model.Adoption;
import AnimalCareCentre.server.model.Sponsorship;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class UserHomepage {

  private Navigator nav;
  private Stage stage;

  public UserHomepage(Navigator nav, Stage stage) {
    this.nav = nav;
    this.stage = stage;
    show();
  }

  private void show() {
    ACCScene scene = new ACCScene(stage, new ACCVBox());
    new NavBar(nav.getLoggedRole(), nav, scene);

    ACCMenuButton seePendingRequests = new ACCMenuButton("My Pending Requests");
    ACCMenuButton seeMyRequests = new ACCMenuButton("My Requests");
    ACCMenuButton seeMySponsorships = new ACCMenuButton("My Sponsorships");

    seePendingRequests.setOnAction(e -> {
    });

    seeMyRequests.setOnAction(e -> {
    });

    seeMySponsorships.setOnAction(e -> {
    });
    scene.addItems(seePendingRequests,seeMyRequests,seeMySponsorships);
  }
}
