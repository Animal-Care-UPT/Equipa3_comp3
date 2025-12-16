package AnimalCareCentre.client.views;

import AnimalCareCentre.client.Navigator;
import AnimalCareCentre.client.components.ACCMenuButton;
import AnimalCareCentre.client.components.ACCScene;
import AnimalCareCentre.client.components.ACCVBox;
import javafx.stage.Stage;

public class AdminHomepage {

  private Navigator nav;
  private Stage stage;

  public AdminHomepage(Navigator nav, Stage stage) {
    this.nav = nav;
    this.stage = stage;
    show();
  }

  private void show() {
    ACCScene scene = new ACCScene(stage, new ACCVBox());
    new NavBar(nav.getLoggedRole(), nav, scene);

    ACCMenuButton viewShelterRequests = new ACCMenuButton("Shelter Requests");
    ACCMenuButton viewAllSponsorships = new ACCMenuButton("Sponsorships");
    ACCMenuButton viewAllAdoptions = new ACCMenuButton("Adoptions");
    ACCMenuButton viewAllFosters = new ACCMenuButton("Fosters");

    viewShelterRequests.setOnAction(e -> {

    });

    viewAllSponsorships.setOnAction(e -> {

    });

    viewAllAdoptions.setOnAction(e -> {

    });

    viewAllFosters.setOnAction(e -> {

    });

    scene.addItems(viewShelterRequests,viewAllSponsorships,viewAllAdoptions,viewAllFosters);
  }

}
