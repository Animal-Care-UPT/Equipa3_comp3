package AnimalCareCentre.client;

import java.util.List;

import AnimalCareCentre.client.records.ShelterAnimal;
import AnimalCareCentre.client.views.*;
import javafx.stage.Stage;

/**
 * This class is responsible for moving between the different pages of the platform.
 *
 */
public class Navigator {

  private Stage stage;
  private String loggedRole;

  public Navigator(Stage stage) {
    this.stage = stage;
    showMainMenu();
  }

  public void showMainMenu() {
    new MainMenu(this, stage);
  }

  public void shelterHomepage() {
    new ShelterHomepage(this, stage);
  }

  public void userHomepage() {
    new UserHomepage(this, stage);
  }

  public void adminHomepage() {
    new AdminHomepage(this, stage);
  }

  public void setLoggedRole(String role) {
    loggedRole = role;
  }

  public void searchAnimal(List<ShelterAnimal> animals) {}

  public void registerAnimal() {
    new RegisterAnimal(this, stage);
  }

  public String getLoggedRole() {
    return loggedRole;
  }
}
