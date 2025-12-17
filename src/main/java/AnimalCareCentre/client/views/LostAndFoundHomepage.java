package AnimalCareCentre.client.views;

import AnimalCareCentre.client.ApiClient;
import AnimalCareCentre.client.ApiResponse;
import AnimalCareCentre.client.Navigator;
import AnimalCareCentre.client.Utility;
import AnimalCareCentre.client.components.ACCGrid;
import AnimalCareCentre.client.components.ACCScene;
import AnimalCareCentre.client.components.ACCVBox;
import AnimalCareCentre.client.enums.District;
import AnimalCareCentre.client.records.LostAnimal;
import AnimalCareCentre.client.records.ShelterAnimal;
import io.github.makbn.jlmap.JLMap;
import io.github.makbn.jlmap.JLProperties;
import io.github.makbn.jlmap.fx.JLMapView;
import io.github.makbn.jlmap.fx.layer.JLUiLayer;
import io.github.makbn.jlmap.listener.OnJLActionListener;
import io.github.makbn.jlmap.listener.event.ClickEvent;
import io.github.makbn.jlmap.listener.event.Event;
import io.github.makbn.jlmap.map.JLMapProvider;
import io.github.makbn.jlmap.model.JLLatLng;
import io.github.makbn.jlmap.model.JLMapOption;
import io.github.makbn.jlmap.model.JLMarker;
import jakarta.validation.constraints.Null;
import javafx.application.Platform;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LostAndFoundHomepage {

  private Navigator nav;
  private Stage stage;
  private JLMapView map;
  private SplitPane splitPane;
  private int loaded;

  public LostAndFoundHomepage(Navigator nav, Stage stage) {
    this.nav = nav;
    this.stage = stage;
    this.splitPane= new SplitPane();
    splitPane.setOrientation(Orientation.VERTICAL);
    this.map = generateMap();
    loaded = 0;
    show();
  }

  private void show() {
    ACCScene scene = new ACCScene(stage, new ACCVBox());
    ACCVBox mapVbox = new ACCVBox();
    mapVbox.addItems(map);
    mapVbox.setMaxHeight(300);
    splitPane.getItems().add(mapVbox);
    scene.addItems(splitPane);

    new NavBar(nav.getLoggedRole(), nav, scene);
  }

  private JLMapView generateMap(){
      JLMapView map = JLMapView.builder()

              .jlMapProvider(JLMapProvider.MAP_TILER.parameter(new JLMapOption.Parameter("key", "2xU84Sbu0P5P0xGsubkw")).build())
              .startCoordinate(JLLatLng.builder()
                      .lat(39.40)
                      .lng(-8.07)
                      .build())


              .showZoomController(true)
              .build();
      map.setOnActionListener((source, event) -> {
          if (event instanceof ClickEvent) {
              if(loaded == 0){

                  LostAndFoundHomepage.this.bootStrapCheckmarks((JLUiLayer) map.getUiLayer());
                  map.setZoom(7);
              }

              loaded = 1;
          }
      });

      return map;
  }
    private List<LostAnimal> searchAnimalByLocation(District district) {
        ApiResponse response = ApiClient.get("/lostandfound/showByLocation?location=" +district.name());
        if (!response.isSuccess()) {
            Utility.showAlert(Alert.AlertType.ERROR, "Error", response.getBody());
            return null;
        } else {
            List<LostAnimal> animals = Utility.parseList(response.getBody(), LostAnimal.class);
            return animals;
        }
    }

    private List<LostAnimal> getAllLostAnimals() {

      ApiResponse response = ApiClient.get("/lostandfound/showlostanimals");
        if (!response.isSuccess()) {
            Utility.showAlert(Alert.AlertType.ERROR, "Error", response.getBody());
            return null;
        } else {
            return Utility.parseList(response.getBody(), LostAnimal.class);
        }

    };

    private void bootStrapCheckmarks(JLUiLayer uiLayer){

      for(District location: District.values()){
          JLMarker marker =  uiLayer.addMarker(
                  JLLatLng.builder()
                          .lat(location.getLatitude())
                          .lng(location.getLongitude())
                          .build(),
                  location.name(), false
                  );
          marker.setOnActionListener(((jlMarker, event) ->{
              if(event instanceof ClickEvent){
                 List<LostAnimal> animalByLocation =  searchAnimalByLocation(location);


                  ACCGrid<LostAnimal> grid = new ACCGrid<>(e->nav.showLostAnimal(e),this::fetchImagesForPage);
                  if(animalByLocation == null){
                      Utility.showAlert(Alert.AlertType.ERROR,"No animals in this location", location.toString());
                      return;
                  }
                  grid.add(animalByLocation);

                  if(splitPane.getItems().size() >1 ){
                      splitPane.getItems().remove(1);
                  };
                  ACCVBox gridVbox= new ACCVBox();
                  gridVbox.addItems(grid);
                  splitPane.getItems().add(gridVbox);
                 }



              }))
          ;}
    }

    private Map<Long, Image> fetchImagesForPage(List<LostAnimal> lostAnimals) {
        Map<Long, Image> images = new HashMap<>();

        for (LostAnimal item : lostAnimals) {
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
