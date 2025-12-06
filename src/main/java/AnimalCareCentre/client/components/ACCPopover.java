package AnimalCareCentre.client.components;

import org.controlsfx.control.PopOver;

import javafx.scene.Node;

public class ACCPopover extends PopOver {

  public ACCPopover(Node content) {
    setContentNode(content);
    setArrowLocation(PopOver.ArrowLocation.TOP_CENTER);
    setTitle("Account");
    setDetachable(false);
    setMinSize(350, 500);
    setHideOnEscape(true);
    setAutoHide(true);
    setArrowSize(0);
    setHeaderAlwaysVisible(true);
    setCloseButtonEnabled(false);
  }
}
