package AnimalCareCentre.client.views;

import org.controlsfx.control.PopOver;

import AnimalCareCentre.client.ApiClient;
import AnimalCareCentre.client.ApiResponse;
import AnimalCareCentre.client.Navigator;
import AnimalCareCentre.client.Utility;
import AnimalCareCentre.client.components.ACCVBox;
import AnimalCareCentre.client.records.Account;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;

public class AccountPopover {

  private Navigator nav;
  private PopOver popover;

  public AccountPopover(Navigator nav) {
    this.nav = nav;
  }

  public void show(Button button) {
    ACCVBox content = buildContent();
    popover = new PopOver(content);
    popover.setArrowLocation(PopOver.ArrowLocation.TOP_CENTER);
    popover.setTitle("Account");
    popover.setDetachable(false);
    popover.setMinSize(350, 500);
    popover.setHideOnEscape(true);
    popover.setAutoHide(true);
    popover.setArrowSize(0);
    popover.setHeaderAlwaysVisible(true);
    popover.setCloseButtonEnabled(false);
    popover.show(button);
  }

  private ACCVBox buildContent() {
    ACCVBox content = new ACCVBox();
    content.setPadding(new Insets(15, 15, 15, 15));
    content.setStyle("-fx-background-color: #FFFAF1;");

    ApiResponse response = ApiClient.get("/accounts/self");
    if (!response.isSuccess()) {
      Utility.showAlert(AlertType.ERROR, "Error", response.getBody());
      return null;
    }

    Account acc = Utility.parseResponse(response.getBody(), Account.class);

    Label name = new Label("Name: " + acc.name());
    Label email = new Label("Email: " + acc.email());
    Label location = new Label("Location: " + acc.location());

    VBox accInfo = new VBox(name, email, location);
    accInfo.setSpacing(5);

    Button secQuestion = new Button("Change Security Question");
    secQuestion.setOnAction(e -> changeSecurityQuestion());

    content.addItems(accInfo, secQuestion);
    return content;
  }

  public void changeSecurityQuestion() {}
}
