package AnimalCareCentre.client.views;

import AnimalCareCentre.client.ApiClient;
import AnimalCareCentre.client.ApiResponse;
import AnimalCareCentre.client.Navigator;
import AnimalCareCentre.client.Utility;
import AnimalCareCentre.client.components.ACCButton;
import AnimalCareCentre.client.components.ACCComboBox;
import AnimalCareCentre.client.components.ACCTextField;
import AnimalCareCentre.client.components.ACCScene;
import AnimalCareCentre.client.components.ACCVBox;
import AnimalCareCentre.client.enums.SecurityQuestion;
import javafx.geometry.Pos;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ChangeSq {

  private Navigator nav;
  private Stage stage;

  public ChangeSq(Navigator nav, Stage stage) {
    this.nav = nav;
    this.stage = stage;
    show();
  }

  /**
   * Shows the change secret info view
   */
  public void show() {
    ACCScene scene = new ACCScene(stage, new ACCVBox());
    Label secLabel = new Label("New Security Question:");
    ACCComboBox<SecurityQuestion> sec = new ACCComboBox<>();
    sec.getItems().addAll(SecurityQuestion.values());
    Label ansLabel = new Label("Answer:");
    ACCTextField answer = new ACCTextField();
    ACCButton enter = new ACCButton("Enter");
    ACCButton back = new ACCButton("Back");

    VBox vbox = new VBox();
    vbox.setAlignment(Pos.CENTER_LEFT);
    vbox.getChildren().addAll(secLabel, sec, ansLabel, answer);
    vbox.setMaxWidth(250);
    vbox.setSpacing(10);
    scene.addItems(vbox, enter, back);

    enter.setOnAction(e -> {
      String json = Utility.jsonString("securityQuestion", sec.getValue(), "answer", answer.getText());
      changeSq(json);
    });

    back.setOnAction(e -> {
      nav.home();
    });

  }

  /**
   * Allows secret info change
   */
  public void changeSq(String json) {
    ApiResponse response = ApiClient.put("/accounts/changesq", json);
    if (response.isSuccess()) {
      Utility.showAlert(AlertType.INFORMATION, "Success", response.getBody());
      nav.home();
    } else {
      Utility.showAlert(AlertType.ERROR, "Error", response.getBody());
    }

  }

}
