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
    setDetachable(false);
    setCornerRadius(10);
    setArrowSize(0);
    setArrowIndent(0);
    setMinSize(1000, 800);
    setHideOnEscape(true);
    setAutoHide(true);
    setAutoFix(false);
    setArrowSize(0);
    setCloseButtonEnabled(false);
    setArrowLocation(PopOver.ArrowLocation.TOP_CENTER);
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
