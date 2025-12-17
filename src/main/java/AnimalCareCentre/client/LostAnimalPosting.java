package AnimalCareCentre.client;

import AnimalCareCentre.client.records.LostAnimal;
import javafx.stage.Stage;

public class LostAnimalPosting {

  private final Navigator navigator;
  private final Stage stage;
  private final LostAnimal lostAnimal;

  public LostAnimalPosting(Navigator navigator, Stage stage, LostAnimal lostAnimal) {
    this.navigator = navigator;
    this.stage = stage;
    this.lostAnimal = lostAnimal;
  }
}
