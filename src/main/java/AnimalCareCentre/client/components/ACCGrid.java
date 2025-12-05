package AnimalCareCentre.client.components;

import java.util.List;
import java.util.function.Consumer;

import AnimalCareCentre.client.records.Displayable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Pagination;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;

public class ACCGrid<T extends Displayable> extends VBox {

  private static final int ITEMS_PER_PAGE = 12;
  private static final int COLUMNS = 3;
  private List<T> lst;
  private Pagination pagination;
  private Consumer<T> cardClick;

  public ACCGrid(Consumer<T> clickHandler) {
    this.cardClick = clickHandler;
    this.setSpacing(20);
    this.setPadding(new Insets(20));
    this.setAlignment(Pos.CENTER);

    pagination = new Pagination();
    pagination.setMaxPageIndicatorCount(5);
    this.getChildren().add(pagination);
  }

  public void add(List<T> lst) {
    this.lst = lst;
    int totalPages = (int) Math.ceil(lst.size() / (double) ITEMS_PER_PAGE);

    pagination.setPageCount(Math.max(1, totalPages));
    pagination.setPageFactory(this::createGrid);
  }

  private TilePane createGrid(int pageIndex) {
    TilePane tilePane = new TilePane();
    tilePane.setHgap(20);
    tilePane.setVgap(20);
    tilePane.setPrefColumns(COLUMNS);
    tilePane.setAlignment(Pos.CENTER);

    int start = pageIndex * ITEMS_PER_PAGE;
    int end = Math.min(start + ITEMS_PER_PAGE, lst.size());

    for (int i = start; i < end; i++) {
      T item = lst.get(i);
      ACCCard<T> card = new ACCCard<>(item, () -> handleCardClick(item));
      tilePane.getChildren().add(card);
    }

    return tilePane;
  }

  private void handleCardClick(T item) {
    if (cardClick != null) {
      cardClick.accept(item);
    }
  }
}
