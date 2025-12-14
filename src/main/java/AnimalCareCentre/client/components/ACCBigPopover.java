package AnimalCareCentre.client.components;

import org.controlsfx.control.PopOver;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class ACCBigPopover extends PopOver {

  public ACCBigPopover(Node content, String title) {
    setContentNode(setup(content, title));
  }

  private Node setup(Node content, String title) {
    setHeaderAlwaysVisible(false);
    setArrowLocation(PopOver.ArrowLocation.TOP_CENTER);
    setDetachable(false);
    setMinSize(500, 400);
    setHideOnEscape(true);
    setAutoHide(true);
    setArrowSize(0);
    setCloseButtonEnabled(false);

    Text titleText = new Text(title);
    titleText.setStyle("-fx-fill: #FFFAF1; -fx-font-size: 16px; -fx-font-weight: bold;");

    HBox header = new HBox(titleText);
    header.setAlignment(Pos.CENTER);
    header.setStyle("-fx-background-color: #6F4426; -fx-padding: 10;");

    VBox combined = new VBox(header, content);
    combined.setStyle("-fx-background-color: #FFFAF1;");

    return combined;
  }
}
