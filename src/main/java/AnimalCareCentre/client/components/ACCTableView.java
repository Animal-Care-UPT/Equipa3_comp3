package AnimalCareCentre.client.components;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class ACCTableView<S> extends TableView<S> {

  public ACCTableView() {
    super();
    applyStyles();
    disableColumnReordering();
  }

  private void disableColumnReordering() {
    getColumns().addListener((javafx.collections.ListChangeListener<TableColumn<S, ?>>) change -> {
      while (change.next()) {
        if (change.wasAdded()) {
          for (TableColumn<S, ?> column : change.getAddedSubList()) {
            column.setReorderable(false);
          }
        }
      }
    });
  }

  private void applyStyles() {
    setStyle(
        "-fx-background-color: #FFFAF1; " +
            "-fx-border-color: #CCCCCC; " +
            "-fx-border-radius: 8; " +
            "-fx-background-radius: 8; " +
            "-fx-border-width: 1px;");

    setFocusTraversable(false);

    String css = """
        .table-view .column-header-background {
            -fx-background-color: #F5EFE6;
            -fx-background-radius: 8 8 0 0;
        }

        .table-view .column-header {
            -fx-background-color: #F5EFE6;
            -fx-border-color: transparent transparent #CCCCCC transparent;
            -fx-border-width: 0 0 2 0;
        }

        .table-view .column-header .label {
            -fx-text-fill: #69462B;
            -fx-font-size: 14px;
            -fx-font-weight: bold;
            -fx-alignment: CENTER;
            -fx-padding: 10 10 10 10;
        }

        .table-view .column-header-background .filler {
            -fx-background-color: #F5EFE6;
        }

        .table-view .table-row-cell {
            -fx-min-height: 45px;
        }

        .table-view .table-cell {
            -fx-border-color: transparent transparent #EEEEEE transparent;
            -fx-border-width: 0 0 1 0;
            -fx-background-color: #FFFAF1;
            -fx-alignment: CENTER;
        }

        .table-view .table-row-cell:hover .table-cell {
            -fx-background-color: #FFF8E7;
        }

        .table-view .table-row-cell:selected .table-cell {
            -fx-background-color: #F5EFE6;
            -fx-text-fill: #69462B;
            -fx-font-weight: bold;
        }

        .table-view .table-row-cell:selected:hover .table-cell {
            -fx-background-color: #EBE3D5;
        }

        .table-view .scroll-bar .thumb {
            -fx-background-color: #CCCCCC;
            -fx-background-radius: 5;
        }

        .table-view .scroll-bar .thumb:hover {
            -fx-background-color: #999999;
        }
        """;

    getStylesheets().add("data:text/css;base64," +
        java.util.Base64.getEncoder().encodeToString(css.getBytes()));
  }
}
