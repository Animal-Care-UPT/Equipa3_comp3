package AnimalCareCentre.client.views;

import AnimalCareCentre.client.Navigator;
import AnimalCareCentre.client.components.ACCScene;
import AnimalCareCentre.client.components.ACCVBox;
import io.github.makbn.jlmap.JLProperties;
import io.github.makbn.jlmap.fx.JLMapView;
import io.github.makbn.jlmap.map.JLMapProvider;
import io.github.makbn.jlmap.model.JLLatLng;
import io.github.makbn.jlmap.model.JLMapOption;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class LostAndFoundHomepage {

  private Navigator nav;
  private Stage stage;

  public LostAndFoundHomepage(Navigator nav, Stage stage) {
    this.nav = nav;
    this.stage = stage;
    show();
  }

  private void show() {
    ACCScene scene = new ACCScene(stage, new ACCVBox());
    String apiKey = System.getenv("MAP_API_KEY");
    JLMapView map = JLMapView.builder()
              .jlMapProvider(JLMapProvider.MAP_TILER.parameter(new JLMapOption.Parameter("key", "2xU84Sbu0P5P0xGsubkw")).build())
              .startCoordinate(JLLatLng.builder()
                      .lat(39.40)
                      .lng(-8.07)
                      .build())


              .showZoomController(true)
              .build();


      AnchorPane root = new AnchorPane(map);
      scene.addItems(root);
      /*Platform.runLater(() -> {
          try {
              Thread.sleep(5000); // Wait 2 seconds for map to load
              Platform.runLater(() -> {
                  map.getControlLayer().setZoom(20);
              });
          } catch (InterruptedException e) {
              e.printStackTrace();
          }
      });

       */




    new NavBar(nav.getLoggedRole(), nav, scene);
  }

}
