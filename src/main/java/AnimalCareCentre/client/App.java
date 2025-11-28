package AnimalCareCentre.client;

import java.awt.Toolkit;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import com.fasterxml.jackson.databind.ObjectMapper;

import AnimalCareCentre.client.views.*;
import AnimalCareCentre.server.enums.SecurityQuestion;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

public class App extends Application {

  private static Stage stage;
  private static Scanner sc = new Scanner(System.in);
  private Thread consoleThread;
  private static final String BASE_URL = "http://localhost:8080";
  private static final RestTemplate rest = new RestTemplate();

  public void start(Stage stage) {
    App.stage = stage;
    stage.setTitle("AnimalCareCentre");
    stage.setWidth(Toolkit.getDefaultToolkit().getScreenSize().getWidth());
    stage.setHeight(Toolkit.getDefaultToolkit().getScreenSize().getHeight() - 100);
    stage.show();
    showMainMenu();
  }

  /**
   * This method shows the main menu, where you can login or create accounts
   */
  public void showMainMenu() {
    ACCScene scene = new ACCScene(stage, new ACCVBox());

    Button login = new Button("Login");
    Button create = new Button("Create Account");
    Button exit = new Button("Exit");

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
    TextField name = new TextField();
    name.setMaxWidth(250);
    Label emailLabel = new Label("Email:");
    TextField email = new TextField();
    email.setMaxWidth(250);
    Label passLabel = new Label("Password:");
    PasswordField password = new PasswordField();
    password.setMaxWidth(250);
    Label locationLabel = new Label("Location:");
    TextField location = new TextField();
    location.setMaxWidth(250);
    Label secLabel = new Label("Security Question:");
    ComboBox<SecurityQuestion> sec = new ComboBox<>();
    sec.getItems().addAll(SecurityQuestion.values());
    Label answerLabel = new Label("Answer:");
    TextField answer = new TextField();
    answer.setMaxWidth(250);

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
    TextField contact = new TextField();
    contact.setMaxWidth(250);
    Label foundYear = new Label("Foundation year:");
    TextField year = new TextField();
    year.setMaxWidth(250);
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
        showAlert(AlertType.ERROR, "Missing Account Type!", "Please select an account type!");
        return;
      }

      if (accType.getValue().equals("User")) {

        String json = jsonString("name", name.getText(), "email", email.getText(),
            "password", password.getText(), "location", location.getText(),
            "securityQuestion", sec.getValue(), "answer", answer.getText(), "birthDate",
            birthDate.getValue().toString(), "contact", Integer.parseInt(contact.getText()));

        ApiResponse response = ApiClient.post("users/create", json);

        if (response.isSuccess()) {
          showAlert(AlertType.INFORMATION, "Success", "Account created with success!");
          System.out.println(response.getBody());
          showMainMenu();
        } else {
          showAlert(AlertType.ERROR, "Error", response.getBody());
        }


      } else if (accType.getValue().equals("Admin")) {

        String json = jsonString("name", name.getText(), "email", email.getText(),
            "password", password.getText(), "location", location.getText(),
            "securityQuestion", sec.getValue(), "answer", answer.getText());

        String secret = jsonString("secret", adminCode.getText());

        showMainMenu();

      } else if (accType.getValue().equals("Shelter")) {

        String json = jsonString("name", name.getText(), "email", email.getText(),
            "password", password.getText(), "location", location.getText(),
            "securityQuestion", sec.getValue(), "answer", answer.getText(), "foundationYear",
            Integer.parseInt(year.getText()), "contact", Integer.parseInt(contact.getText()));

        showMainMenu();
      }

    });

    back.setOnAction(e -> {
      showMainMenu();
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

  /**
   * This method shows an alert
   */
  public void showAlert(AlertType type, String title, String text) {
    Alert alert = new Alert(type);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(text);
    alert.showAndWait();
  }

  /**
   * This method returns a formatted Json string
   *
   * @return
   */
  public String jsonString(Object... pairs) {
    Map<String, Object> dataMap = new HashMap<>();
    for (int i = 0; i < pairs.length; i += 2) {
      String key = (String) pairs[i];
      Object value = pairs[i + 1];
      dataMap.put(key, value);
    }
    try {
      ObjectMapper objectMapper = new ObjectMapper();
      return objectMapper.writeValueAsString(dataMap);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public static void main(String[] args) {
    launch(args);
  }
}
