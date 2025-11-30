package AnimalCareCentre.client;

import java.awt.Toolkit;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;

import com.fasterxml.jackson.databind.ObjectMapper;

import AnimalCareCentre.client.views.*;
import AnimalCareCentre.server.enums.AnimalType;
import AnimalCareCentre.server.enums.SecurityQuestion;
import javafx.application.Application;
import javafx.application.Platform;
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

public class App extends Application {

  private static Stage stage;
  private static Scanner sc = new Scanner(System.in);
  private Thread consoleThread;
  private static String loggedRole;

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
    ACCScene scene = new ACCScene(stage, new ACCVBox());
    Label emailLabel = new Label("Email:");
    TextField email = new TextField();
    email.setMaxWidth(250);
    Label passLabel = new Label("Password:");
    PasswordField password = new PasswordField();
    password.setMaxWidth(250);
    Button enter = new Button("Enter");
    Button back = new Button("Back");
    Button changePassword = new Button("Forgot Password");
    scene.addItems(emailLabel, email, passLabel, password, enter, back, changePassword);

    enter.setOnAction(e -> {
      String json = jsonString("email", email.getText(), "password", password.getText());

      ApiResponse response = ApiClient.login("/accounts/login", json);

      if (!response.isSuccess()) {
        showAlert(AlertType.ERROR, "Error", response.getBody());
        return;
      }

      if (response.getBody().equals("ROLE_USER")) {
        userHomepage();
      } else if (response.getBody().equals("ROLE_SHELTER")) {
        shelterHomepage();
      } else {
        adminHomepage();
      }
    });

    back.setOnAction(e -> {
      showMainMenu();
    });

    changePassword.setOnAction(e -> {
      // changePassword();
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

        ApiResponse response = ApiClient.post("/users/create", json);

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
            "securityQuestion", sec.getValue(), "answer", answer.getText(), "secret", adminCode.getText());

        ApiResponse response = ApiClient.post("/accounts/create", json);

        if (response.isSuccess()) {
          showAlert(AlertType.INFORMATION, "Success", "Account created with success!");
          System.out.println(response.getBody());
          showMainMenu();
        } else {
          showAlert(AlertType.ERROR, "Error", response.getBody());
        }

      } else if (accType.getValue().equals("Shelter")) {

        String json = jsonString("name", name.getText(), "email", email.getText(),
            "password", password.getText(), "location", location.getText(),
            "securityQuestion", sec.getValue(), "answer", answer.getText(), "foundationYear",
            Integer.parseInt(year.getText()), "contact", Integer.parseInt(contact.getText()));

        ApiResponse response = ApiClient.post("/shelters/create", json);

        if (response.isSuccess()) {
          showAlert(AlertType.INFORMATION, "Success", "Account created with success!");
          System.out.println(response.getBody());
          showMainMenu();
        } else {
          showAlert(AlertType.ERROR, "Error", response.getBody());
        }
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
   * This method shows the search animals menu
   */
  public void searchAnimalMenu() {
    System.out.println("\n=== SEARCH ANIMAL ===");

    String[] options = { "Search by Keyword", "Search by Type", "Search by Color", "Search by Gender" };
    String opt = (String) chooseOption(options, "Search Option");
    if (opt == null) {
      Platform.runLater(this::userHomepage);
      return;
    }

    switch (opt) {
      case "Search by Keyword" -> {
        System.out.println("What would you like to search?");
        String search = readLine();
        String encodedSearch = URLEncoder.encode(search, StandardCharsets.UTF_8);
        ApiResponse response = ApiClient.get("/shelteranimals/search?keyword=" + encodedSearch);

        System.out.println(response.getBody());

        // if (animals == null || animals.isEmpty()) {
        // System.out.println("\nNo matches! Returning...");
        // } else {
        // ShelterAnimal choice = (ShelterAnimal) chooseOption(animals.toArray(),
        // "Animal");
        // if (choice == null) {
        // javafx.application.Platform.runLater(this::userHomepage);
        // return;
        // }
        // showAnimal(choice);
        // }
        searchAnimalMenu();
        return;
      }

      case "Search by Type" -> {
        AnimalType chosenType = (AnimalType) chooseOption(AnimalType.values(), "Type");
        if (chosenType == null) {
          javafx.application.Platform.runLater(this::userHomepage);
          return;
        }

        ApiResponse response = ApiClient.get("/shelteranimals/search/type?type=" + chosenType.name());
        System.out.println(response.getBody());

        // System.out.println(response.getBody());
        //
        // if (animals == null || animals.isEmpty()) {
        // System.out.println("\nNo matches! Returning...");
        // } else {
        // ShelterAnimal choice = (ShelterAnimal) chooseOption(animals.toArray(),
        // "Animal");
        // if (choice == null) {
        // javafx.application.Platform.runLater(this::userHomepage);
        // return;
        // }
        // showAnimal(choice);
        // return;
        // }
        searchAnimalMenu();
        return;
      }

      case "Search by Color" -> {
        // AnimalColor chosenColor = (AnimalColor) chooseOption(AnimalColor.values(),
        // "Color");
        // if (chosenColor == null) {
        // javafx.application.Platform.runLater(this::userHomepage);
        // }
        // List<ShelterAnimal> animals = manager.searchAnimalByParameter("color",
        // chosenColor);
        //
        // if (animals == null || animals.isEmpty()) {
        // System.out.println("\nNo matches! Returning...");
        // } else {
        // ShelterAnimal choice = (ShelterAnimal) chooseOption(animals.toArray(),
        // "Animal");
        // if (choice == null) {
        // javafx.application.Platform.runLater(this::userHomepage);
        // return;
        // }
        // showAnimal(choice);
        // }
        searchAnimalMenu();
        return;
      }

      case "Search by Gender" -> {
        searchAnimalMenu();
        return;
      }
    }
  }

  /**
   * This method shows shelter's homepage
   */
  private void shelterHomepage() {
    javafx.application.Platform.runLater(() -> showTerminalScreen());

    try {
      if (consoleThread != null && consoleThread.isAlive()) {
        consoleThread.interrupt();
      }
      consoleThread = new Thread(() -> {
        System.out.println("=== SHELTER MENU ===");
        System.out.println("1. Register Animal");
        System.out.println("2. View My Animals");
        System.out.println("3. View Pending Adoption Requests");
        System.out.println("4. View Pending Foster Requests");
        System.out.println("5. View Adoptions");
        System.out.println("6. View Fosters");
        System.out.println("0. Logout");
        System.out.print("Option: ");
        int option = readInt();

        switch (option) {
          case 1 -> {
            shelterHomepage();
            return;
          }

          case 2 -> {
            shelterHomepage();
            return;
          }

          case 3 -> {
            shelterHomepage();
            return;
          }

          case 4 -> {
            shelterHomepage();
            return;
          }

          case 5 -> {
            shelterHomepage();
            return;
          }

          case 6 -> {
            shelterHomepage();
            return;
          }

          case 0 -> {
            System.out.println("Exiting terminal menu...");
            javafx.application.Platform.runLater(this::showMainMenu);
          }

          default -> {
            System.out.println("Invalid option!");
            shelterHomepage();
            return;
          }
        }

      });
      consoleThread.start();

    } catch (InputMismatchException e) {
      System.out.println("Please pick a valid option!");
      shelterHomepage();
    }
  }

  /**
   * This method shows User's homepage
   */
  private void userHomepage() {
    javafx.application.Platform.runLater(() -> showTerminalScreen());

    try {

      if (consoleThread != null && consoleThread.isAlive()) {
        consoleThread.interrupt();
      }
      consoleThread = new Thread(() -> {
        int option;

        System.out.println("=== USER MENU ===");
        System.out.println("1. Search Animal");
        System.out.println("2. Search Shelter");
        System.out.println("3. See My Adoptions Requests");
        System.out.println("4. See My Foster Requests");
        System.out.println("5. Lost and Found");
        System.out.println("0. Logout");
        System.out.print("Option: ");
        option = readInt();

        switch (option) {
          case 1 -> {
            searchAnimalMenu();
            userHomepage();
            return;
          }

          case 2 -> {
            // searchShelter();
            userHomepage();
            return;
          }

          case 3 -> {
            userHomepage();
            return;
          }

          case 4 -> {
            userHomepage();
            return;
          }

          case 5 -> {
            // lostAndFoundMenu();
            userHomepage();
            return;
          }

          case 0 -> {
            System.out.println("Exiting terminal menu...");
            Platform.runLater(() -> showMainMenu());
            return;
          }
          default -> System.out.println("Invalid option!");
        }
      });
      consoleThread.start();

    } catch (InputMismatchException e) {
      System.out.println("Please pick a valid option!");
      userHomepage();
    }
  }

  /**
   * This method shows admin's homepage
   */
  private void adminHomepage() {
    javafx.application.Platform.runLater(() -> showTerminalScreen());

    try {

      if (consoleThread != null && consoleThread.isAlive()) {
        consoleThread.interrupt();
      }
      consoleThread = new Thread(() -> {
        int option;

        System.out.println("=== ADMIN MENU ===");
        System.out.println("1. View Shelter Requests");
        System.out.println("2. View Available Shelters");
        System.out.println("3. View All Sponsorships");
        System.out.println("4. View All Animals");
        System.out.println("5. View All Adoptions");
        System.out.println("6. View All Fosters");
        System.out.println("7 Lost and Found");
        System.out.println("0. Logout");
        System.out.print("Option: ");
        option = readInt();

        switch (option) {
          case 1 -> {
            adminHomepage();
            return;
          }

          case 2 -> {
            adminHomepage();
            return;
          }

          case 3 -> {
            adminHomepage();
            return;
          }

          case 4 -> {
            adminHomepage();
            return;
          }

          case 5 -> {
            adminHomepage();
            return;
          }

          case 6 -> {
            adminHomepage();
            return;
          }

          case 7 -> {
            // lostAndFoundMenu();
            adminHomepage();
            return;
          }

          case 0 -> {
            System.out.println("Exiting terminal menu...");
            Platform.runLater(() -> showMainMenu());
            return;
          }
          default -> System.out.println("Invalid option!");
        }
      });
      consoleThread.start();

    } catch (InputMismatchException e) {
      System.out.println("Please pick a valid option!");
      userHomepage();
    }
  }

  /**
   * This method is used temporarily to change to the terminal screen
   */
  private void showTerminalScreen() {
    ACCScene scene = new ACCScene(stage, new ACCVBox());
    Button logout = new Button("Logout");
    logout.setStyle("-fx-background-color: #333333; -fx-text-fill: white; -fx-font-size: 16px;");
    scene.addItems(logout);

    logout.setOnAction(e -> {
      ApiResponse response = ApiClient.post("/accounts/logout", "");
      System.out.println(response.getBody());
      ApiClient.clearSession();
      showMainMenu();
      return;
    });

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

  /**
   * Method to choose an option
   */
  private Object chooseOption(Object[] values, String title) {
    while (true) {
      System.out.println("\n\n\n");
      System.out.println("Select " + title + ":");
      System.out.println("\n");

      for (int i = 0; i < values.length; i++) {
        System.out.println((i + 1) + ". " + values[i] + "\n");
      }

      System.out.println("0. Back");
      String input = readLine();

      try {
        int option = Integer.parseInt(input);
        if (option == 0)
          return null;
        if (option >= 1 && option <= values.length) {
          return values[option - 1];
        }
      } catch (NumberFormatException ignored) {
      }

      System.out.println("Invalid option, please try again.");
    }
  }

  private int readInt() {
    synchronized (sc) {
      int value = sc.nextInt();
      sc.nextLine();
      return value;
    }
  }

  private String readLine() {
    synchronized (sc) {
      return sc.nextLine();
    }
  }

  private float readFloat() {
    synchronized (sc) {
      float value = sc.nextFloat();
      sc.nextLine();
      return value;
    }
  }

  public static void main(String[] args) {
    launch(args);
  }
}
