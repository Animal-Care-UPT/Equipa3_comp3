package AnimalCareCentre.client.views;

import AnimalCareCentre.client.Navigator;
import AnimalCareCentre.client.components.*;

/**
 * This class defines the navigation bar used throughout the platform
 *
 */
public class NavBar {

  private Navigator nav;
  private ACCScene scene;

  public NavBar(String loggedRole, Navigator nav, ACCScene scene) {
    this.nav = nav;
    this.scene = scene;

    if (loggedRole.equals("ROLE_USER")) {
      userNavButtons();
    } else if (loggedRole.equals("ROLE_SHELTER")) {
      shelterNavButtons();
    } else if (loggedRole.equals("ROLE_ADMIN")) {
      adminNavButtons();
    }
  }

  private void userNavButtons() {
    ACCNavButton home = new ACCNavButton("Home");
    ACCNavButton animals = new ACCNavButton("Search Animals");
    ACCNavButton shelters = new ACCNavButton("Search Shelters");
    ACCNavButton lostFound = new ACCNavButton("Lost and Found");
    ACCNavButton acc = new ACCNavButton("Account");

    home.setOnAction(e -> nav.userHomepage());
    animals.setOnAction(e -> new SearchAnimalPopover(nav).show(animals));
    shelters.setOnAction(e -> nav.userHomepage());
    lostFound.setOnAction(e -> nav.userHomepage());
    acc.setOnAction(e -> new AccountPopover(nav).show(acc));

    scene.setHeader(home, animals, shelters, lostFound, acc);
  }

  private void adminNavButtons() {
    ACCNavButton home = new ACCNavButton("Home");
    ACCNavButton animals = new ACCNavButton("Search Animals");
    ACCNavButton shelters = new ACCNavButton("Search Shelters");
    ACCNavButton lostFound = new ACCNavButton("Lost and Found");
    ACCNavButton acc = new ACCNavButton("Account");

    home.setOnAction(e -> nav.adminHomepage());
    animals.setOnAction(e -> nav.adminHomepage());
    shelters.setOnAction(e -> nav.adminHomepage());
    lostFound.setOnAction(e -> nav.adminHomepage());
    acc.setOnAction(e -> new AccountPopover(nav).show(acc));

    scene.setHeader(home, animals, shelters, lostFound, acc);
  }

  private void shelterNavButtons() {
    ACCNavButton home = new ACCNavButton("Home");
    ACCNavButton animals = new ACCNavButton("My Animals");
    ACCNavButton lostFound = new ACCNavButton("Lost and Found");
    ACCNavButton acc = new ACCNavButton("Account");

    home.setOnAction(e -> nav.shelterHomepage());
    animals.setOnAction(e -> nav.shelterHomepage());
    lostFound.setOnAction(e -> nav.shelterHomepage());
    acc.setOnAction(e -> new AccountPopover(nav).show(acc));

    scene.setHeader(home, animals, lostFound, acc);
  }

}
