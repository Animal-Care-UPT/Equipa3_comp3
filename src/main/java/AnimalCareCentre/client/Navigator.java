package AnimalCareCentre.client;

import java.awt.Toolkit;

import AnimalCareCentre.client.views.MainMenu;
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
    stage.setTitle("AnimalCareCentre");
    stage.setWidth(Toolkit.getDefaultToolkit().getScreenSize().getWidth());
    stage.setHeight(Toolkit.getDefaultToolkit().getScreenSize().getHeight());
    stage.show();
    showMainMenu();

  }

  public void showMainMenu() {
    new MainMenu(this, stage);
  }

  public void shelterHomepage() {

  }

  public void userHomepage() {

  }

  public void adminHomepage() {

  }

  public void setLoggedRole(String role) {
    loggedRole = role;
  }

  public String getLoggedRole() {
    return loggedRole;
  }
}
