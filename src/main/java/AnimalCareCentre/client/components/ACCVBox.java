package AnimalCareCentre.client.components;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.VBox;

/**
 * This class creates a customized VBox element used throughout the app.
 *
 */
public class ACCVBox extends VBox {

  /**
   * Constructor for ACCVBox (defines the default settings)
   */
  public ACCVBox() {
    super();
    setSpacing(15);
    setAlignment(Pos.CENTER);
  }

  /**
   * This method adds items to the ACCVBox.
   */
  public void addItems(Node... items) {
    getChildren().addAll(items);
  }

  public void clear() {
    getChildren().clear();
  }
}
