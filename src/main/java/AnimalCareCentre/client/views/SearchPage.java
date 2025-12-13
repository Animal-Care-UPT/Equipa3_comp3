package AnimalCareCentre.client.views;

import java.util.List;

import AnimalCareCentre.client.Navigator;
import AnimalCareCentre.client.components.ACCGrid;
import AnimalCareCentre.client.components.ACCScene;
import AnimalCareCentre.client.components.ACCVBox;
import AnimalCareCentre.client.records.Displayable;
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
    new NavBar(nav.getLoggedRole(), nav, scene);

    ACCGrid<T> grid = new ACCGrid<>(e -> {
      nav.showAnimal(e);
      System.out.println("Test");
    });

    grid.add(lst);
    scene.addItems(grid);
  }
}
