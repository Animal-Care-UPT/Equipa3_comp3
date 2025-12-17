package AnimalCareCentre.client.components;

import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;

/**
 * ComboBox used throughout the platform
 *
 */
public class ACCComboBox<T> extends ComboBox<T> {

  /**
   * Default constructor
   */
  public ACCComboBox() {
    this(false);
  }

  /**
   * Constructor for ACC Combo Box
   *
   * @param nullable if true it adds the option "Any"
   */
  public ACCComboBox(boolean nullable) {
    super();
    setMaxWidth(250);
    setStyle(
        "-fx-background-color: white; " +
            "-fx-border-color: #CCCCCC; " +
            "-fx-border-radius: 5; " +
            "-fx-background-radius: 5; " +
            "-fx-font-size: 14px;");
    setMinHeight(35);
    setMaxHeight(35);

    if (nullable) {
      addNullOption("Any");
    }
  }

  private void addNullOption(String label) {
    getItems().add(0, null);

    setCellFactory(lv -> new ListCell<T>() {
      @Override
      protected void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);
        setText(empty ? null : (item == null ? label : item.toString()));
      }
    });

    setButtonCell(new ListCell<T>() {
      @Override
      protected void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);
        setText(empty ? null : (item == null ? label : item.toString()));
      }
    });
  }
}
