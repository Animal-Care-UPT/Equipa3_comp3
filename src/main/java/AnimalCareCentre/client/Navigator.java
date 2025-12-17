package AnimalCareCentre.client;

import java.util.List;

import AnimalCareCentre.client.records.Displayable;
import AnimalCareCentre.client.records.LostAnimal;
import AnimalCareCentre.client.records.Shelter;
import AnimalCareCentre.client.records.ShelterAnimal;
import AnimalCareCentre.client.views.*;
import javafx.stage.Stage;

/**
 * This class is responsible for moving between the different pages of the
 * platform.
 *
 */
public class Navigator {

  private Stage stage;
  private static String loggedRole;

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

  public void lostAndFoundHomepage() {
    new LostAndFoundHomepage(this, stage);
  }

  public void registerLostAnimal() {
    new RegisterLostAnimal(this, stage);
  }

  public void adminHomepage() {
    new AdminHomepage(this, stage);
  }

  public void setLoggedRole(String role) {
    loggedRole = role;
  }

  public void searchAnimal(List<ShelterAnimal> animals) {
    new SearchPage<>(this, stage, animals);
  }

  public void searchLostAnimal(List<LostAnimal> animals) {
    new SearchPage<>(this, stage, animals);
  }

  public void searchShelter(List<Shelter> shelters) {
    new SearchPage<>(this, stage, shelters);
  }

  public void showLostAnimal(Displayable animal) {
    LostAnimal lostAnimal = (LostAnimal) animal;
    new LostAnimalProfile(this, stage, lostAnimal);
  }

  public void showLostAnimalPosting(Displayable animal) {
    LostAnimal lostAnimal = (LostAnimal) animal;
    new LostAnimalProfile(this, stage, lostAnimal);
  }

  public void showAnimal(Displayable animal) {
    ShelterAnimal shelterAnimal = (ShelterAnimal) animal;
    new AnimalProfile(this, stage, shelterAnimal);
  }

  public void showShelter(Displayable shelter) {
    Shelter shelterRecord = (Shelter) shelter;
    new ShelterProfile(this, stage, shelterRecord);

  }

  public void registerAnimal() {
    new RegisterAnimal(this, stage);
  }

  public void changeSecurityQuestion() {
    new ChangeSq(this, stage);
  }

  public void home() {
    if (loggedRole.equals("ROLE_ADMIN")) {
      adminHomepage();
    } else if (loggedRole.equals("ROLE_SHELTER")) {
      shelterHomepage();
    } else if (loggedRole.equals("ROLE_USER")) {
      userHomepage();
    }
  }

  public static String getLoggedRole() {
    return loggedRole;
  }

  public void lostAndFoundMenu() {
    new LostAndFoundMenu(this, stage);
  }

  public void lostAnimalsByAccount(List<LostAnimal> lostAnimals) {
    new SearchPageMyPosting<>(this, stage, lostAnimals);
  }
}
