package AnimalCareCentre.client;

import java.awt.Toolkit;

import AnimalCareCentre.client.views.AdminHomepage;
import AnimalCareCentre.client.views.MainMenu;
import AnimalCareCentre.client.views.ShelterHomepage;
import AnimalCareCentre.client.views.UserHomepage;
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

  public String getLoggedRole() {
    return loggedRole;
  }
}
