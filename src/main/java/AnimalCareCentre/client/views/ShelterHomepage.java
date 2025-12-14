package AnimalCareCentre.client.views;

import AnimalCareCentre.client.Navigator;
import AnimalCareCentre.client.components.*;
import javafx.stage.Stage;

public class ShelterHomepage {

  private Navigator nav;
  private Stage stage;

  public ShelterHomepage(Navigator nav, Stage stage) {
    this.nav = nav;
    this.stage = stage;
    show();
  }

  private void show() {
    ACCScene scene = new ACCScene(stage, new ACCVBox());
    new NavBar(nav.getLoggedRole(), nav, scene);

    ACCMenuButton registerAnimal = new ACCMenuButton ("Register Animal");
    ACCMenuButton viewPendingRequests = new ACCMenuButton("Pending Requests");

    registerAnimal.setOnAction(e -> { nav.registerAnimal();
    });

    viewPendingRequests.setOnAction(e ->{
    });
    
    scene.addItems(registerAnimal, viewPendingRequests);
  }

}

