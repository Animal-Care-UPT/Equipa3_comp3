package AnimalCareCentre.client.views;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.HBox;

/**
 * This class creates a customized HBox element used throughout the app.
 *
 */
public class ACCHBox extends HBox {

  /**
   * Constructor for ACCHBox (defines the default settings)
   */
  public ACCHBox() {
    super();
    setSpacing(5);
    setAlignment(Pos.CENTER);
  }

  /**
   * This method adds items to the ACCVBox.
   */
  public void addItems(Node... items) {
    getChildren().addAll(items);
  }
}

