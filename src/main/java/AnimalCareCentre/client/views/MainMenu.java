package AnimalCareCentre.client.views;

import AnimalCareCentre.client.views.*;
import AnimalCareCentre.client.*;
import AnimalCareCentre.client.enums.*;
import AnimalCareCentre.client.records.*;
import AnimalCareCentre.client.components.*;

import javafx.geometry.Pos;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * This class defines the Main Menu of Animal Care Centre
 *
 */
public class MainMenu {

  private Navigator nav;
  private Stage stage;

  public MainMenu(Navigator nav, Stage stage) {
    this.nav = nav;
    this.stage = stage;
    ACCScene scene = new ACCScene(stage, new ACCVBox());

    ACCButton login = new ACCButton("Login");
    ACCButton create = new ACCButton("Create Account");
    ACCButton exit = new ACCButton("Exit");

    login.setOnAction(e -> {
      login();
    });

    create.setOnAction(e -> {
      createAccount();
    });

    exit.setOnAction(e -> {
      System.exit(0);
    });
    scene.addItems(login, create, exit);
  }

  /**
   * This method shows the login screen
   */
  public void login() {
    ACCScene scene = new ACCScene(stage, new ACCVBox());
    Label emailLabel = new Label("Email:");
    ACCTextField email = new ACCTextField();
    Label passLabel = new Label("Password:");
    PasswordField password = new PasswordField();
    password.setMaxWidth(250);
    Button enter = new Button("Enter");
    Button back = new Button("Back");
    Button changePassword = new Button("Forgot Password");
    scene.addItems(emailLabel, email, passLabel, password, enter, back, changePassword);

    enter.setOnAction(e -> {
      String json = Utility.jsonString("email", email.getText(), "password", password.getText());

      ApiResponse response = ApiClient.login("/accounts/login", json);

      if (!response.isSuccess()) {
        Utility.showAlert(AlertType.ERROR, "Error", response.getBody());
        return;
      }

      nav.setLoggedRole(response.getBody());

      if (nav.getLoggedRole().equals("ROLE_USER")) {
        nav.userHomepage();
      } else if (nav.getLoggedRole().equals("ROLE_SHELTER")) {
        nav.shelterHomepage();
      } else {
        nav.adminHomepage();
      }
    });

    back.setOnAction(e -> {
      nav.showMainMenu();
    });

    changePassword.setOnAction(e -> {
      changePassword();
    });
  }

  /**
   * This method shows the change password screen
   */
  public void changePassword() {
    ACCScene scene = new ACCScene(stage, new ACCVBox());
    Label emailLabel = new Label("Email:");
    ACCTextField email = new ACCTextField();
    Button enter = new Button("Enter");
    Label passwordLabel = new Label("New password:");
    PasswordField password = new PasswordField();
    Button confirm = new Button("Confirm");
    Button back = new Button("Back");
    ACCTextField answer = new ACCTextField();

    scene.addItems(emailLabel, email, enter, back);
    enter.setOnAction(e -> {
      ApiResponse response = ApiClient.get("/accounts/secquestion?email=" + email.getText());
      if (response.isSuccess()) {
        scene.clearContent();
        Label question = new Label(response.getBody());
        scene.addItems(question, answer, passwordLabel, password, confirm, back);
      } else {
        Utility.showAlert(AlertType.ERROR, "Error", response.getBody());
      }
    });

    confirm.setOnAction(e -> {
      String json = Utility.jsonString("email", email.getText(), "answer", answer.getText(), "newPassword", password.getText());
      ApiResponse response = ApiClient.put("/accounts/changepw", json);
      if (response.isSuccess()) {
        Utility.showAlert(AlertType.INFORMATION, "Success", response.getBody());
        login();
        return;
      } else {
        Utility.showAlert(AlertType.ERROR, "Error", response.getBody());
        return;
      }

    });

    back.setOnAction(e -> {
      login();
    });
  }

  /**
   * This method shows the create account screen
   */
  public void createAccount() {
    ACCScene scene = new ACCScene(stage, new ACCVBox());
    Label type = new Label("Account type:");
    ComboBox<String> accType = new ComboBox<>();
    accType.getItems().addAll("User", "Admin", "Shelter");
    Label nameLabel = new Label("Name:");
    ACCTextField name = new ACCTextField();
    Label emailLabel = new Label("Email:");
    ACCTextField email = new ACCTextField();
    Label passLabel = new Label("Password:");
    PasswordField password = new PasswordField();
    password.setMaxWidth(250);
    Label locationLabel = new Label("Location:");
    ACCTextField location = new ACCTextField();
    Label secLabel = new Label("Security Question:");
    ComboBox<SecurityQuestion> sec = new ComboBox<>();
    sec.getItems().addAll(SecurityQuestion.values());
    Label answerLabel = new Label("Answer:");
    ACCTextField answer = new ACCTextField();

    VBox vbox = new VBox();
    vbox.setAlignment(Pos.CENTER_LEFT);
    vbox.getChildren().addAll(type, accType, nameLabel, name, emailLabel, email, passLabel, password, locationLabel,
        location, secLabel, sec,
        answerLabel, answer);
    vbox.setMaxWidth(250);
    vbox.setSpacing(10);
    scene.addItems(vbox);

    PasswordField adminCode = new PasswordField();
    adminCode.setMaxWidth(250);
    Label birthLabel = new Label("Birthdate:");
    DatePicker birthDate = new DatePicker();
    birthDate.setMaxWidth(250);
    Label contactLabel = new Label("Contact:");
    ACCTextField contact = new ACCTextField();
    Label foundYear = new Label("Foundation year:");
    ACCTextField year = new ACCTextField();
    Label adminLabel = new Label("Admin code:");

    year.setTextFormatter(new TextFormatter<>(change -> {
      String num = change.getControlNewText();
      if (num.matches("\\d{0,4}")) {
        return change;
      }
      return null;
    }));

    contact.setTextFormatter(new TextFormatter<>(change -> {
      String num = change.getControlNewText();
      if (num.matches("\\d{0,9}")) {
        return change;
      }
      return null;
    }));

    Button create = new Button("Create");
    Button back = new Button("Back");

    create.setOnAction(e -> {

      if (accType.getValue() == null) {
        Utility.showAlert(AlertType.ERROR, "Missing Account Type!", "Please select an account type!");
        return;
      }

      if (accType.getValue().equals("User")) {

        String json = Utility.jsonString("name", name.getText(), "email", email.getText(),
            "password", password.getText(), "location", location.getText(),
            "securityQuestion", sec.getValue(), "answer", answer.getText(), "birthDate",
            birthDate.getValue().toString(), "contact", Integer.parseInt(contact.getText()));

        ApiResponse response = ApiClient.post("/users/create", json);

        if (response.isSuccess()) {
          Utility.showAlert(AlertType.INFORMATION, "Success", "Account created with success!");
          System.out.println(response.getBody());
          nav.showMainMenu();
        } else {
          Utility.showAlert(AlertType.ERROR, "Error", response.getBody());
        }

      } else if (accType.getValue().equals("Admin")) {

        String json = Utility.jsonString("name", name.getText(), "email", email.getText(),
            "password", password.getText(), "location", location.getText(),
            "securityQuestion", sec.getValue(), "answer", answer.getText(), "secret", adminCode.getText());

        ApiResponse response = ApiClient.post("/accounts/create", json);

        if (response.isSuccess()) {
          Utility.showAlert(AlertType.INFORMATION, "Success", "Account created with success!");
          System.out.println(response.getBody());
          nav.showMainMenu();
        } else {
          Utility.showAlert(AlertType.ERROR, "Error", response.getBody());
        }

      } else if (accType.getValue().equals("Shelter")) {

        String json = Utility.jsonString("name", name.getText(), "email", email.getText(),
            "password", password.getText(), "location", location.getText(),
            "securityQuestion", sec.getValue(), "answer", answer.getText(), "foundationYear",
            Integer.parseInt(year.getText()), "contact", Integer.parseInt(contact.getText()));

        ApiResponse response = ApiClient.post("/shelters/create", json);

        if (response.isSuccess()) {
          Utility.showAlert(AlertType.INFORMATION, "Success", "Account created with success!");
          System.out.println(response.getBody());
          nav.showMainMenu();
        } else {
          Utility.showAlert(AlertType.ERROR, "Error", response.getBody());
        }
        nav.showMainMenu();
      }

    });

    back.setOnAction(e -> {
      nav.showMainMenu();
    });

    accType.valueProperty().addListener((obs, old, selected) -> {
      vbox.getChildren().clear();
      vbox.getChildren().addAll(type, accType, nameLabel, name, emailLabel, email, passLabel, password, locationLabel,
          location, secLabel, sec,
          answerLabel, answer);

      if (selected.equals("User")) {
        vbox.getChildren().addAll(birthLabel, birthDate, contactLabel, contact);

      } else if (selected.equals("Shelter")) {
        vbox.getChildren().addAll(contactLabel, contact, foundYear, year);

      } else {
        vbox.getChildren().addAll(adminLabel, adminCode);
      }
    });
    scene.addItems(create, back);
  }

}
