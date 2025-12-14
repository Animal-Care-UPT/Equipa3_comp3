package AnimalCareCentre.client.views;

import AnimalCareCentre.client.ApiClient;
import AnimalCareCentre.client.ApiResponse;
import AnimalCareCentre.client.Navigator;
import AnimalCareCentre.client.Utility;
import AnimalCareCentre.client.components.*;
import AnimalCareCentre.client.records.Account;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;

public class AccountPopover {

  private Navigator nav;
  private ACCPopover popover;

  public AccountPopover(Navigator nav) {
    this.nav = nav;
  }

  public void show(Button button) {
    ACCVBox content = buildContent();
    popover = new ACCPopover(content, "Account");
    popover.setTitle("Account");
    popover.show(button);
  }

  private ACCVBox buildContent() {
    ACCVBox content = new ACCVBox();
    content.setPadding(new Insets(15, 15, 15, 15));

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

    ACCTextButton secQuestion = new ACCTextButton("Change Security Question");
    secQuestion.setOnAction(e -> nav.changeSecurityQuestion());

    ACCButton logout = new ACCButton("Logout");
    logout.setOnAction(e -> logout());

    content.addItems(accInfo, secQuestion, logout);
    return content;
  }

  private void logout() {
    ApiResponse response = ApiClient.post("/accounts/logout", "");
    System.out.println(response.getBody());
    ApiClient.clearSession();
    nav.showMainMenu();
    return;
  }
}
