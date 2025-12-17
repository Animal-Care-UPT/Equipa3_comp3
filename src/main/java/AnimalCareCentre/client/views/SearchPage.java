package AnimalCareCentre.client.views;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import AnimalCareCentre.client.ApiClient;
import AnimalCareCentre.client.ApiResponse;
import AnimalCareCentre.client.Navigator;
import AnimalCareCentre.client.Utility;
import AnimalCareCentre.client.components.ACCGrid;
import AnimalCareCentre.client.components.ACCScene;
import AnimalCareCentre.client.components.ACCVBox;
import AnimalCareCentre.client.records.Displayable;
import AnimalCareCentre.client.records.LostAnimal;
import AnimalCareCentre.client.records.Shelter;
import AnimalCareCentre.client.records.ShelterAnimal;
import javafx.scene.control.Alert;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class SearchPage<T extends Displayable> {

  private Navigator nav;
  private Stage stage;

  public SearchPage(Navigator nav, Stage stage, List<T> lst) {
    this.nav = nav;
    this.stage = stage;
    show(lst);
  }

  private void show(List<T> lst) {
    ACCScene scene = new ACCScene(stage, new ACCVBox());
    new NavBar(Navigator.getLoggedRole(), nav, scene);

    ACCGrid<T> grid;
    if (lst.get(0) instanceof ShelterAnimal) {
      grid = new ACCGrid<>(e -> nav.showAnimal(e), this::fetchImagesForPage);
    } else if(lst.get(0) instanceof Shelter){
      grid = new ACCGrid<>(e -> nav.showShelter(e), this::fetchImagesForPage);
    }else {
        grid = new ACCGrid<>(e->nav.showLostAnimal(e),this::fetchImagesForPage);
    }

    grid.add(lst);
      SplitPane splitPane = new SplitPane();
      splitPane.getItems().add(grid);
    scene.addItems(grid);
  }

  private Map<Long, Image> fetchImagesForPage(List<T> pageItems) {
    Map<Long, Image> images = new HashMap<>();

    for (T item : pageItems) {
      String imageUrl = item.getImagePath();
      if (imageUrl != null && !imageUrl.isEmpty()) {
        ApiResponse response = ApiClient.get(imageUrl);
        Image image = Utility.parseImage(response);
        if (image != null) {
          images.put(item.getId(), image);
        }
      }
    }

    return images;
  }
}
