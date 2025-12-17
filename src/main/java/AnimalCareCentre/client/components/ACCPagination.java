package AnimalCareCentre.client.components;

import javafx.scene.control.Pagination;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Button;

public class ACCPagination extends Pagination {

  public ACCPagination() {
    super();
    setMaxPageIndicatorCount(5);

    currentPageIndexProperty().addListener((obs, old, newVal) -> {
      javafx.application.Platform.runLater(() -> applyStyles());
    });

    javafx.application.Platform.runLater(() -> applyStyles());
  }

  @Override
  protected void layoutChildren() {
    super.layoutChildren();
    applyStyles();
  }

  private void applyStyles() {
    if (lookup(".pagination-control") != null) {

      lookup(".pagination-control").setStyle(
          "-fx-padding: 40 0 0 0;");

      lookupAll(".number-button").forEach(node -> {
        if (node instanceof ToggleButton) {
          ToggleButton btn = (ToggleButton) node;

          if (btn.isSelected()) {

            btn.setStyle(
                "-fx-background-color: #69462B; " +
                    "-fx-text-fill: white; " +
                    "-fx-background-radius: 5px; " +
                    "-fx-font-weight: bold; " +
                    "-fx-padding: 6px 10px; " +
                    "-fx-background-insets: 0; " +
                    "-fx-min-width: 30px; " +
                    "-fx-min-height: 30px; " +
                    "-fx-alignment: center;");
          } else {

            btn.setStyle(
                "-fx-background-color: white; " +
                    "-fx-border-color: #69462b; " +
                    "-fx-border-width: 1px; " +
                    "-fx-border-radius: 5px; " +
                    "-fx-background-radius: 5px; " +
                    "-fx-text-fill: #69462B; " +
                    "-fx-padding: 6px 10px; " +
                    "-fx-cursor: hand; " +
                    "-fx-background-insets: 0; " +
                    "-fx-min-width: 30px; " +
                    "-fx-min-height: 30px; " +
                    "-fx-alignment: center;");
          }
        }
      });

      lookupAll(".left-arrow-button").forEach(node -> {
        if (node instanceof Button) {
          node.setStyle(
              "-fx-background-color: white; " +
                  "-fx-border-color: #69462B; " +
                  "-fx-border-width: 1px; " +
                  "-fx-border-radius: 5px; " +
                  "-fx-background-radius: 5px; " +
                  "-fx-cursor: hand; " +
                  "-fx-padding: 6px; " +
                  "-fx-background-insets: 0; " +
                  "-fx-min-width: 30px; " +
                  "-fx-min-height: 30px;");
        }
      });

      lookupAll(".right-arrow-button").forEach(node -> {
        if (node instanceof Button) {
          node.setStyle(
              "-fx-background-color: white; " +
                  "-fx-border-color: #69462B; " +
                  "-fx-border-width: 1px; " +
                  "-fx-border-radius: 5px; " +
                  "-fx-background-radius: 5px; " +
                  "-fx-cursor: hand; " +
                  "-fx-padding: 6px; " +
                  "-fx-background-insets: 0; " +
                  "-fx-min-width: 30px; " +
                  "-fx-min-height: 30px;");
        }
      });

      lookupAll(".left-arrow").forEach(node -> {
        node.setStyle("-fx-background-color: #69462B;");
      });

      lookupAll(".right-arrow").forEach(node -> {
        node.setStyle("-fx-background-color: #69462B;");
      });
    }
  }
}
