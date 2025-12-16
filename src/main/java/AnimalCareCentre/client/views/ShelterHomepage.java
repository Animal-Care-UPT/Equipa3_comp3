package AnimalCareCentre.client.views;

import java.io.File;

import AnimalCareCentre.client.ApiClient;
import AnimalCareCentre.client.ApiResponse;
import AnimalCareCentre.client.Navigator;
import AnimalCareCentre.client.Utility;
import AnimalCareCentre.client.components.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class ShelterHomepage {

  private Navigator nav;
  private Stage stage;

  public ShelterHomepage(Navigator nav, Stage stage) {
    this.nav = nav;
    this.stage = stage;
    show();
  }

  private void show() {
    ACCScene scene = new ACCScene(stage, new ACCVBox());
    new NavBar(nav.getLoggedRole(), nav, scene);
    ACCMenuButton register = new ACCMenuButton("Register Animal");
    ACCMenuButton viewPendingRequests = new ACCMenuButton("Pending Requests");
    ACCMenuButton addImage = new ACCMenuButton("Add Image");
    
    register.setOnAction(e -> nav.registerAnimal());
    viewPendingRequests.setOnAction(e ->{});
    addImage.setOnAction(e -> addImage());

    scene.addItems(register,viewPendingRequests ,addImage);
  }

  private void addImage() {
    File image = Utility.selectImageFile(stage);
    ApiResponse response = ApiClient.postWithFile("/shelters/images/self", image);
    if (response.isSuccess()) {
      Utility.showAlert(AlertType.INFORMATION, "Success", response.getBody());
    } else {
      Utility.showAlert(AlertType.ERROR, "Error", response.getBody());
    }
  }

}

