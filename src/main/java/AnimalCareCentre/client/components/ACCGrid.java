package AnimalCareCentre.client.components;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import AnimalCareCentre.client.records.Displayable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Pagination;
import javafx.scene.image.Image;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Region;

public class ACCGrid<T extends Displayable> extends VBox {
  private static final int ITEMS_PER_PAGE = 6;

  private List<T> lst;
  private ACCPagination pagination;
  private Consumer<T> cardClick;
  private Function<List<T>, Map<Long, Image>> imageFetcher;

  public ACCGrid(Consumer<T> clickHandler, Function<List<T>, Map<Long, Image>> imageFetcher) {
    this.cardClick = clickHandler;
    this.imageFetcher = imageFetcher;

    this.setSpacing(20);
    this.setPadding(new Insets(20));
    this.setAlignment(Pos.CENTER);

    pagination = new ACCPagination();
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
    tilePane.setAlignment(Pos.CENTER);
    tilePane.setPrefColumns(2);
    tilePane.setMaxWidth(600);

    int start = pageIndex * ITEMS_PER_PAGE;
    int end = Math.min(start + ITEMS_PER_PAGE, lst.size());
    List<T> pageItems = lst.subList(start, end);
    Map<Long, Image> images = imageFetcher.apply(pageItems);

    for (T item : pageItems) {
      Image image = images.get(item.getId());
      ACCCard<T> card = new ACCCard<>(item, image, () -> handleCardClick(item));
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
