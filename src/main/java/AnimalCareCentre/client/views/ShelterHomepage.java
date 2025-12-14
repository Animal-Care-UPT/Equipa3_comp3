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
    ACCMenuButton register = new ACCMenuButton("Register Animal");


    register.setOnAction(e -> nav.registerAnimal());

    scene.addItems(register);
  }

}
