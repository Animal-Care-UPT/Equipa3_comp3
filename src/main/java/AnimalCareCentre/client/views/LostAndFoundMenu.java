package AnimalCareCentre.client.views;

import AnimalCareCentre.client.ApiClient;
import AnimalCareCentre.client.ApiResponse;
import AnimalCareCentre.client.Navigator;
import AnimalCareCentre.client.components.ACCMenuButton;
import AnimalCareCentre.client.components.ACCScene;
import AnimalCareCentre.client.components.ACCVBox;
import AnimalCareCentre.client.records.LostAnimal;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

import static AnimalCareCentre.client.Utility.parseList;

public class LostAndFoundMenu {

  private Navigator nav;
  private Stage stage;

  public LostAndFoundMenu(Navigator nav, Stage stage) {
    this.nav = nav;
    this.stage = stage;
    show();
  }

  /**
   * Displays the lost and found menu
   */
  private void show() {
    ACCScene scene = new ACCScene(stage, new ACCVBox());
    new NavBar(Navigator.getLoggedRole(), nav, scene);
    ACCMenuButton register = new ACCMenuButton("Register Lost Animal");
    ACCMenuButton homepage = new ACCMenuButton("Home Page");
    ACCMenuButton myPosting = new ACCMenuButton("My Postings");

    register.setOnAction(e -> nav.registerLostAnimal());
    homepage.setOnAction(e -> nav.lostAndFoundHomepage());
    myPosting.setOnAction(e -> nav.lostAnimalsByAccount(myPostings()));

    scene.addItems(register, homepage, myPosting);
  }

  /**
   * Gets my postings of lost animals
   */
  private List<LostAnimal> myPostings() {

    ApiResponse response = ApiClient.get("/lostandfound/showanimalsbyaccount");
    if (response.isSuccess()) {
      List<LostAnimal> animals = parseList(response.getBody(), LostAnimal.class);
      return animals;
    }
    return new ArrayList<LostAnimal>();
  }

}
