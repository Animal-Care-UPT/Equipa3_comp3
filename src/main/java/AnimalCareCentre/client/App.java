package AnimalCareCentre.client;

import java.awt.Toolkit;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import AnimalCareCentre.client.views.*;
import AnimalCareCentre.client.enums.*;
import AnimalCareCentre.client.records.*;

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
  private static final ObjectMapper mapper = new ObjectMapper()
      .registerModule(new JavaTimeModule())
      .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

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
      changePassword();
    });
  }

  /**
   * This method shows the change password screen
   */
  public void changePassword() {
    ACCScene scene = new ACCScene(stage, new ACCVBox());
    Label emailLabel = new Label("Email:");
    TextField email = new TextField();
    Button enter = new Button("Enter");
    Label passwordLabel = new Label("New password:");
    PasswordField password = new PasswordField();
    Button confirm = new Button("Confirm");
    Button back = new Button("Back");
    TextField answer = new TextField();

    scene.addItems(emailLabel, email, enter, back);
    enter.setOnAction(e -> {
      ApiResponse response = ApiClient.get("/accounts/secquestion?email=" + email.getText());
      if (response.isSuccess()) {
        scene.clearContent();
        Label question = new Label(response.getBody());
        scene.addItems(question, answer, passwordLabel, password, confirm, back);
      } else {
        showAlert(AlertType.ERROR, "Error", response.getBody());
      }
    });

    confirm.setOnAction(e -> {
      String json = jsonString("email", email.getText(), "answer", answer.getText(), "newPassword", password.getText());
      ApiResponse response = ApiClient.put("/accounts/changepw", json);
      if (response.isSuccess()) {
        showAlert(AlertType.INFORMATION, "Success", response.getBody());
        login();
        return;
      } else {
        showAlert(AlertType.ERROR, "Error", response.getBody());
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
   * This method shows the profile of an animal from a shelter
   *
   */
  public void showAnimal(ShelterAnimal animal) {
    System.out.println(animal);
    String listed = animal.adoptionType() == AdoptionType.FOR_ADOPTION ? "Adopt" : "Foster";
    System.out.println("Menu: ");
    System.out.println("1 - Sponsor Animal");
    System.out.println("2 - " + listed + " Animal");
    System.out.println("0 - Back");
    int opc = readInt();
    switch (opc) {
      case 1 -> {
        System.out.println("Insert the amount of money you wish to give as a sponsorship");
        Float amount = readFloat();
        String json = jsonString("id", animal.id(), "amount", amount);
        ApiResponse response = ApiClient.post("/sponsorships/create", json);
        System.out.println(response.getBody());
        userHomepage();
        return;
      }

      case 2 -> {
        System.out.println("You can " + listed + " this animal.");
        AdoptionType type = (listed.equals("Adopt")) ? AdoptionType.FOR_ADOPTION : AdoptionType.FOR_FOSTER;

        String json = jsonString("animalId", animal.id(), "adoptionType", type.name());

        ApiResponse response = ApiClient.post("/adoptions/request", json);

        if (response.isSuccess()) {
          System.out.println("Your request to " + listed + " " + animal.name() + " has been submitted!");
        } else {
          System.out.println("Error: " + response.getBody());
        }

        userHomepage();
        return;
      }

      case 0 -> {
        userHomepage();
        return;
      }
    }
  }

  /**
   * This method shows the search animals menu
   */
  public void searchAnimalMenu() {

    System.out.println("\n=== SEARCH ANIMAL ===");
    String[] options = { "Search by Keyword", "Search by Type", "Search by Color", "Search by Gender",
        "Search by Size", "Search Animals for Adoption", "Search Animals for Foster" };
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

        if (response.isSuccess()) {
          List<ShelterAnimal> animals = parseList(response.getBody(), ShelterAnimal.class);

          ShelterAnimal choice = (ShelterAnimal) chooseOption(animals.toArray(),
              "Animal");
          if (choice == null) {
            javafx.application.Platform.runLater(this::userHomepage);
            return;
          }

          showAnimal(choice);
        } else {
          System.out.println(response.getBody());
          searchAnimalMenu();
        }
      }

      case "Search by Type" -> {
        AnimalType chosenType = (AnimalType) chooseOption(AnimalType.values(), "Type");
        if (chosenType == null) {
          javafx.application.Platform.runLater(this::userHomepage);
          return;
        }

        ApiResponse response = ApiClient.get("/shelteranimals/search/type?type=" + chosenType.name());

        if (response.isSuccess()) {
          List<ShelterAnimal> animals = parseList(response.getBody(), ShelterAnimal.class);

          ShelterAnimal choice = (ShelterAnimal) chooseOption(animals.toArray(),
              "Animal");
          if (choice == null) {
            javafx.application.Platform.runLater(this::userHomepage);
            return;
          }
          showAnimal(choice);
        } else {
          System.out.println(response.getBody());
          searchAnimalMenu();
        }
      }

      case "Search by Color" -> {
        AnimalColor chosenType = (AnimalColor) chooseOption(AnimalColor.values(), "Color");
        if (chosenType == null) {
          javafx.application.Platform.runLater(this::userHomepage);
          return;
        }

        ApiResponse response = ApiClient.get("/shelteranimals/search/color?color=" + chosenType.name());
        if (response.isSuccess()) {

          List<ShelterAnimal> animals = parseList(response.getBody(), ShelterAnimal.class);

          ShelterAnimal choice = (ShelterAnimal) chooseOption(animals.toArray(),
              "Animal");
          if (choice == null) {
            javafx.application.Platform.runLater(this::userHomepage);
            return;
          }
          showAnimal(choice);
        } else {
          System.out.println(response.getBody());
          searchAnimalMenu();

        }

      }

      case "Search by Gender" -> {
        AnimalGender chosenType = (AnimalGender) chooseOption(AnimalGender.values(), "Gender");
        if (chosenType == null) {
          javafx.application.Platform.runLater(this::userHomepage);
          return;
        }

        ApiResponse response = ApiClient.get("/shelteranimals/search/gender?gender=" + chosenType.name());

        if (response.isSuccess()) {

          List<ShelterAnimal> animals = parseList(response.getBody(), ShelterAnimal.class);

          ShelterAnimal choice = (ShelterAnimal) chooseOption(animals.toArray(),
              "Animal");
          if (choice == null) {
            javafx.application.Platform.runLater(this::userHomepage);
            return;
          }
          showAnimal(choice);
        } else {
          System.out.println(response.getBody());
          searchAnimalMenu();

        }
      }

      case "Search by Size" -> {
        AnimalSize chosenType = (AnimalSize) chooseOption(AnimalSize.values(), "Size");
        if (chosenType == null) {
          javafx.application.Platform.runLater(this::userHomepage);
          return;
        }

        ApiResponse response = ApiClient.get("/shelteranimals/search/size?size=" + chosenType.name());

        if (response.isSuccess()) {

          List<ShelterAnimal> animals = parseList(response.getBody(), ShelterAnimal.class);

          ShelterAnimal choice = (ShelterAnimal) chooseOption(animals.toArray(),
              "Animal");
          if (choice == null) {
            javafx.application.Platform.runLater(this::userHomepage);
            return;
          }
          showAnimal(choice);
        } else {
          System.out.println(response.getBody());
          searchAnimalMenu();

        }
      }

      case "Search Animals for Adoption" -> {

        ApiResponse response = ApiClient.get("/shelteranimals/search/adoption");

        if (response.isSuccess()) {

          List<ShelterAnimal> animals = parseList(response.getBody(), ShelterAnimal.class);

          ShelterAnimal choice = (ShelterAnimal) chooseOption(animals.toArray(),
              "Animal");
          if (choice == null) {
            javafx.application.Platform.runLater(this::userHomepage);
            return;
          }
          showAnimal(choice);
        } else {
          System.out.println(response.getBody());
          searchAnimalMenu();

        }
      }

      case "Search Animals for Foster" -> {

        ApiResponse response = ApiClient.get("/shelteranimals/search/foster");

        if (response.isSuccess()) {

          List<ShelterAnimal> animals = parseList(response.getBody(), ShelterAnimal.class);

          ShelterAnimal choice = (ShelterAnimal) chooseOption(animals.toArray(),
              "Animal");
          if (choice == null) {
            javafx.application.Platform.runLater(this::userHomepage);
            return;
          }
          showAnimal(choice);
        } else {
          System.out.println(response.getBody());
          searchAnimalMenu();
        }
      }
    }
  }

  public void manageAnimal(ShelterAnimal animal) {
    System.out.println(animal);
    System.out.println(animal.status());
    System.out.println("Menu: ");
    System.out.println("1 - Change Vacination Status");
    System.out.println("2 - Change Age");
    System.out.println("3 - Change Adoption Type");
    System.out.println("4 - Change Status");
    System.out.println("0 - Back");
    int opc = readInt();
    switch (opc) {

      case 1 -> {
        ApiResponse response = ApiClient.put("/vacination?id=" + animal.id(), "");
        System.out.println(response.getBody());
        manageAnimal(animal);
      }

      case 2 -> {
        Integer age;
        System.out.println("What is the age?");

        while (true) {
          age = readInt();
          if (age < 0 || age > 30) {
            System.out.println("Pick a valid age!");
          } else {
            break;
          }
        }

        ApiResponse response = ApiClient.put("/age?id=" + animal.id() + "&age=" + age, "");
        System.out.println(response.getBody());
        manageAnimal(animal);
      }

      case 3 -> {
        ApiResponse response = ApiClient.put("/adoptiontype?id=" + animal.id(), "");
        System.out.println(response.getBody());
        manageAnimal(animal);
      }

      case 4 -> {
        ApiResponse response = ApiClient.put("/status?id=" + animal.id(), "");
        System.out.println(response.getBody());
        manageAnimal(animal);
      }

      case 0 -> {
        shelterHomepage();
        return;
      }

      default -> {
        System.out.println("Please pick a valid option!");
        manageAnimal(animal);
        return;
      }
    }
  }

  /**
   * This method allows the shelter to view his animals
   */
  public void shelterViewAnimals() {

    ApiResponse response = ApiClient.get("/shelteranimals/search/own");

    if (response.isSuccess()) {

      List<ShelterAnimal> animals = parseList(response.getBody(), ShelterAnimal.class);

      ShelterAnimal choice = (ShelterAnimal) chooseOption(animals.toArray(),
          "Animal");
      if (choice == null) {
        javafx.application.Platform.runLater(this::shelterHomepage);
        return;
      }
      manageAnimal(choice);
    } else {
      System.out.println(response.getBody());
      shelterViewAnimals();
    }
  }

  /**
   * This method shows shelter's homepage
   */
  private void shelterHomepage() {
    javafx.application.Platform.runLater(() -> showTerminalScreen());

    if (consoleThread != null && consoleThread.isAlive()) {
      consoleThread.interrupt();
    }

    consoleThread = new Thread(() -> {
      try {

        System.out.println("=== SHELTER MENU ===");
        System.out.println("1. Register Animal");
        System.out.println("2. View My Animals");
        System.out.println("3. View Pending Requests And Change their status");
        System.out.println("4. View Historic");
        System.out.println("5. Change Security Answer");
        System.out.println("0. Logout");
        System.out.print("Option: ");
        int option = readInt();

        switch (option) {
          case 1 -> {
            System.out.println("\n=== REGISTER ANIMAL ===");

            System.out.print("Name: ");
            String name = readLine();

            AnimalType type = (AnimalType) chooseOption(AnimalType.values(), "Type");
            if (type == null)
              return;

            List<String> breeds = type.getBreeds();
            String race = null;
            if (breeds != null) {
              while (true) {
                System.out.println("Select Breed:");
                for (int i = 0; i < breeds.size(); i++) {
                  System.out.println((i + 1) + ". " + breeds.get(i));
                }
                System.out.print("Option: ");
                String input = readLine();
                try {
                  int breedOption = Integer.parseInt(input);
                  if (breedOption >= 1 && breedOption <= breeds.size()) {
                    race = breeds.get(breedOption - 1);
                    break;
                  }
                } catch (NumberFormatException ignored) {
                }
                System.out.println("Invalid option, please try again.");
              }
            }

            AnimalSize size = (AnimalSize) chooseOption(AnimalSize.values(), "Size");
            if (size == null)
              return;

            AnimalGender gender = (AnimalGender) chooseOption(AnimalGender.values(), "Gender");
            if (gender == null)
              return;

            System.out.print("Age: ");
            int age = readInt();

            AnimalColor color = (AnimalColor) chooseOption(AnimalColor.values(), "Color");
            if (color == null)
              return;

            System.out.print("Description: ");
            String description = readLine();

            AdoptionType adoptionType = (AdoptionType) chooseOption(AdoptionType.values(), "Adoption Type");
            if (adoptionType == null)
              return;

            String json = jsonString(
                "name", name,
                "type", type.name(),
                "race", race,
                "age", age,
                "color", color.name(),
                "size", size.name(),
                "gender", gender.name(),
                "adoptionType", adoptionType.name(),
                "description", description);

            ApiResponse response = ApiClient.post("/shelteranimals/register", json);

            if (response.isSuccess()) {
              System.out.println("Animal successfully registered!");
            } else {
              System.out.println("Error: " + response.getBody());
            }

            shelterHomepage();
            return;
          }

          case 2 -> {
            shelterViewAnimals();
            return;
          }

          case 3 -> {
            System.out.println("\n=== PENDING REQUESTS ===");

            ApiResponse changeStatusResponse = ApiClient.get("/adoptions/pending");
            if (changeStatusResponse.isSuccess()) {
              List<Adoption> requests = parseList(changeStatusResponse.getBody(), Adoption.class);

              if (requests.isEmpty()) {
                System.out.println("No pending requests at the moment.");
                shelterHomepage();
                return;
              }

              System.out.println("Total: " + requests.size() + " Requests ");

              for (Adoption req : requests) {
                System.out.println(req.animal().name() + " - " + req.adoptionType() + " - " + req.user().name());
              }

              Adoption choice = (Adoption) chooseOption(requests.toArray(), "Pending Request");
              if (choice == null) {
                javafx.application.Platform.runLater(this::shelterHomepage);
                return;
              }

              System.out.println("\n=== Request Details ===");
              System.out.println("Animal: " + choice.animal().name());
              System.out.println("Requested by: " + choice.user().name());
              System.out.println("Type: " + choice.adoptionType());
              System.out.println("Request Date: " + choice.requestDate());
              System.out.println("========================\n");

              String[] requestActions = { "Accept", "Reject" };
              String action = (String) chooseOption(requestActions, "Action");
              if (action == null) {
                javafx.application.Platform.runLater(this::shelterHomepage);
                return;
              }

              Status newStatus = action.equals("Accept") ? Status.ACCEPTED : Status.REJECTED;

              String json = jsonString(
                  "adoptionId", choice.adoptionId(),
                  "newStatus", newStatus.name());

              ApiResponse statusResponse = ApiClient.put("/adoptions/change/status", json);

              if (statusResponse.isSuccess()) {
                System.out.println("Request " + newStatus.name().toLowerCase() + " successfully!");
              } else {
                System.out.println("Error: " + statusResponse.getBody());
              }

            } else {
              System.out.println("Error loading requests: " + changeStatusResponse.getBody());
            }

            shelterHomepage();
            return;
          }

          case 4 -> {

            shelterHomepage();
            return;
          }

          case 5 -> {

            SecurityQuestion question = (SecurityQuestion) chooseOption(SecurityQuestion.values(), "SecurityQuestion");
            System.out.println("Answer: ");
            String answer = readLine();
            String json = jsonString("securityQuestion", question, "answer", answer);
            ApiResponse response = ApiClient.put("/accounts/changesq", json);
            System.out.println(response.getBody());
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
      } catch (RuntimeException e) {
        if (e.getMessage().equals("Thread Interrupted")) {
          return;
        }
        throw e;
      }
    });
    consoleThread.setDaemon(true);
    consoleThread.start();
  }

  /**
   * This method shows a shelter's available animals
   */
  public void showShelterAnimals(Shelter shelter) {
    System.out.println(shelter);
    System.out.println("Animals: ");
    ApiResponse response = ApiClient.get("/shelteranimals/search/shelter/available?id=" + shelter.id());
    if (response.isSuccess()) {

      List<ShelterAnimal> animals = parseList(response.getBody(), ShelterAnimal.class);
      ShelterAnimal choice = (ShelterAnimal) chooseOption(animals.toArray(), "Animal");
      if (choice == null) {
        javafx.application.Platform.runLater(this::userHomepage);
        return;
      }
      showAnimal(choice);
    } else {
      System.out.println(response.getBody());
      showShelter(shelter);
    }
  }

  /**
   * This method shows a shelter's profile
   *
   */
  public void showShelter(Shelter shelter) {
    System.out.println(shelter);
    System.out.println("\n");
    String[] options = { "Donate to Shelter", "View Shelter Animals" };
    String opt = (String) chooseOption(options, "Search Option");
    if (opt == null) {
      Platform.runLater(this::userHomepage);
      return;
    }

    switch (opt) {
      case "Donate to Shelter" -> {
        // donation stuff
        showShelter(shelter);
        return;
      }

      case "View Shelter Animals" -> {
        showShelterAnimals(shelter);
        return;
      }

      default -> {
        System.out.println("Invalid option!");
        showShelter(shelter);
        return;
      }
    }
  }

  /**
   * This method displays all the available shelters
   */
  public void searchShelter() {

    ApiResponse response = ApiClient.get("/shelters/");

    if (response.isSuccess()) {
      List<Shelter> shelters = parseList(response.getBody(), Shelter.class);

      Shelter choice = (Shelter) chooseOption(shelters.toArray(), "Shelter");
      if (choice == null) {
        javafx.application.Platform.runLater(this::userHomepage);
        return;
      }
      showShelter(choice);
    } else {
      System.out.println(response.getBody());
      userHomepage();
    }
  }

  /**
   * This method shows User's homepage
   */
  private void userHomepage() {
    javafx.application.Platform.runLater(() -> showTerminalScreen());

    if (consoleThread != null && consoleThread.isAlive()) {
      consoleThread.interrupt();
    }

    consoleThread = new Thread(() -> {
      try {

        int option;

        System.out.println("=== USER MENU ===");
        System.out.println("1. Search Animal");
        System.out.println("2. Search Shelter");
        System.out.println("3. See My Pending Requests");
        System.out.println("4. See My Requests");
        System.out.println("5. Lost and Found");
        System.out.println("6. See My Sponsorships");
        System.out.println("7. Change Security Answer");
        System.out.println("0. Logout");
        System.out.print("Option: ");
        option = readInt();

        switch (option) {
          case 1 -> {
            searchAnimalMenu();
            return;
          }

          case 2 -> {
            searchShelter();
            return;
          }

          case 3 -> {
            System.out.println("\n=== PENDING REQUESTS ===");

            ApiResponse requestResponse = ApiClient.get("/adoptions/user/pending");
            if (requestResponse.isSuccess()) {
              List<Adoption> requests = parseList(requestResponse.getBody(), Adoption.class);

              System.out.println("Total: " + requests.size() + " Requests ");

              for (Adoption req : requests) {
                System.out.println(req.animal().name() + " - " + req.adoptionType() + " - " + req.user().name());
              }

            } else {
              System.out.println("Error: " + requestResponse.getBody());
            }
            userHomepage();
            return;
          }

          case 4 -> {
            System.out.println("\n=== ACCEPTED REQUESTS ===");

            ApiResponse acceptedResponse = ApiClient.get("/adoptions/user/accepted");
            if (acceptedResponse.isSuccess()) {
              List<Adoption> requests = parseList(acceptedResponse.getBody(), Adoption.class);

              System.out.println("Total: " + requests.size() + " Requests ");

              for (Adoption req : requests) {
                System.out.println(req.animal().name() + " - " + req.adoptionType() + " - " + req.user().name());
              }

            } else {
              System.out.println("Error: " + acceptedResponse.getBody());
            }
            userHomepage();
            return;
          }

          case 5 -> {
            lostAndFoundHomePage();
            return;
          }

          case 6 -> {
            ApiResponse response = ApiClient.get("/sponsorships/usersponsor");
            if (response.isSuccess()) {
              List<Sponsorship> sponsorships = parseList(response.getBody(), Sponsorship.class);
              System.out.println(sponsorships);
            } else {
              System.out.println(response.getBody());
            }
            userHomepage();
            return;
          }

          case 7 -> {
            SecurityQuestion question = (SecurityQuestion) chooseOption(SecurityQuestion.values(), "SecurityQuestion");
            System.out.println("Answer: ");
            String answer = readLine();
            String json = jsonString("securityQuestion", question, "answer", answer);
            ApiResponse response = ApiClient.put("/accounts/changesq", json);
            System.out.println(response.getBody());
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
      } catch (RuntimeException e) {
        if (e.getMessage().equals("Thread Interrupted")) {
          return;
        }
        throw e;
      }

    });
    consoleThread.setDaemon(true);
    consoleThread.start();

  }

  /**
   * This method allows the admin to do multiple operations on the shelter
   */
  public void displayShelterForAdmin(Shelter shelter) {
    System.out.println(shelter);
    System.out.println("\n");
    String[] options = { "Change Shelter Status", "View All Shelter Animals" };
    String opt = (String) chooseOption(options, "Search Option");
    if (opt == null) {
      Platform.runLater(this::adminHomepage);
      return;
    }

    switch (opt) {
      case "Change Shelter Status" -> {
        String[] requestAction = { "Set as Available", "Ban Shelter" };
        String action = (String) chooseOption(requestAction, "Shelter Request");
        if (action == null) {
          javafx.application.Platform.runLater(this::adminHomepage);
          return;
        }

        if (action.equals("Set as Available")) {
          String jsonStatus = jsonString("status", Status.AVAILABLE);
          ApiResponse acptResponse = ApiClient.put("/shelters/status?id=" + shelter.id(), jsonStatus);
          if (acptResponse.isSuccess()) {
            System.out.println("Shelter accepted with success!");
          } else {
            System.out.println(acptResponse.getBody());
          }

        } else {
          String jsonStatus = jsonString("status", Status.BANNED);
          ApiResponse banResponse = ApiClient.put("/shelters/status?id=" + shelter.id(), jsonStatus);
          if (banResponse.isSuccess()) {
            System.out.println("Shelter rejected with success!");
          } else {
            System.out.println(banResponse.getBody());
          }
        }
        adminHomepage();
        return;
      }

      case "View All Shelter Animals" -> {
        System.out.println(shelter);
        System.out.println("Animals: ");
        ApiResponse response = ApiClient.get("/shelteranimals/search/shelter?id=" + shelter.id());
        if (response.isSuccess()) {

          List<ShelterAnimal> animals = parseList(response.getBody(), ShelterAnimal.class);
          System.out.println(animals);
          adminHomepage();
          return;

        } else {
          System.out.println(response.getBody());
          displayShelterForAdmin(shelter);
        }
      }

      default -> {
        System.out.println("Invalid option!");
        showShelter(shelter);
        return;
      }
    }
  }

  /**
   * This method shows admin's homepage
   */
  private void adminHomepage() {
    javafx.application.Platform.runLater(() -> showTerminalScreen());

    if (consoleThread != null && consoleThread.isAlive()) {
      consoleThread.interrupt();
    }

    consoleThread = new Thread(() -> {
      try {

        int option;

        System.out.println("=== ADMIN MENU ===");
        System.out.println("1. View Shelter Requests");
        System.out.println("2. View Shelters");
        System.out.println("3. View All Sponsorships");
        System.out.println("4. View All Animals");
        System.out.println("5. View All Adoptions");
        System.out.println("6. View All Fosters");
        System.out.println("7 Lost and Found");
        System.out.println("8. Change Security Answer");
        System.out.println("0. Logout");
        System.out.print("Option: ");
        option = readInt();

        switch (option) {
          case 1 -> {
            ApiResponse response = ApiClient.get("/shelters/pending");
            Shelter choice;

            if (response.isSuccess()) {
              List<Shelter> shelters = parseList(response.getBody(), Shelter.class);
              choice = (Shelter) chooseOption(shelters.toArray(), "Shelter Request");
              if (choice == null) {
                javafx.application.Platform.runLater(this::adminHomepage);
                return;
              }

            } else {
              System.out.println(response.getBody());
              adminHomepage();
              return;
            }

            String[] requestAction = { "Accept", "Reject" };
            String action = (String) chooseOption(requestAction, "Shelter Request");
            if (action == null) {
              javafx.application.Platform.runLater(this::adminHomepage);
              return;
            }

            if (action.equals("Accept")) {
              String jsonStatus = jsonString("status", Status.AVAILABLE);
              ApiResponse acptResponse = ApiClient.put("/shelters/status?id=" + choice.id(), jsonStatus);
              if (acptResponse.isSuccess()) {
                System.out.println("Shelter accepted with success!");
              } else {
                System.out.println(acptResponse.getBody());
              }

            } else {
              String jsonStatus = jsonString("status", Status.REJECTED);
              ApiResponse rejResponse = ApiClient.put("/shelters/status?id=" + choice.id(), jsonStatus);
              if (rejResponse.isSuccess()) {
                System.out.println("Shelter rejected with success!");
              } else {
                System.out.println(rejResponse.getBody());
              }
            }
            adminHomepage();
            return;

          }

          case 2 -> {
            ApiResponse response = ApiClient.get("/shelters/all");

            if (response.isSuccess()) {
              List<Shelter> shelters = parseList(response.getBody(), Shelter.class);

              Shelter choice = (Shelter) chooseOption(shelters.toArray(), "Shelter");
              if (choice == null) {
                javafx.application.Platform.runLater(this::userHomepage);
                return;
              }
              displayShelterForAdmin(choice);
            } else {
              System.out.println(response.getBody());
              userHomepage();
            }

            return;
          }

          case 3 -> {
            ApiResponse response = ApiClient.get("/sponsorships/all");
            if (response.isSuccess()) {
              List<Sponsorship> sponsors = parseList(response.getBody(), Sponsorship.class);
              System.out.println(sponsors);
            } else {
              System.out.println(response.getBody());
            }
            adminHomepage();
            return;
          }

          case 4 -> {
            ApiResponse response = ApiClient.get("/shelteranimals/all");
            if (response.isSuccess()) {
              List<ShelterAnimal> animals = parseList(response.getBody(), ShelterAnimal.class);
              System.out.println(animals);
              adminHomepage();
              return;
            }
          }

          case 5 -> {
            ApiResponse response = ApiClient.get("/adoptions/all");
            if (response.isSuccess()) {
              List<Adoption> adoptions = parseList(response.getBody(), Adoption.class);
              for (Adoption a : adoptions) {
                System.out.println("User: " + a.user().name());
                System.out.println("Shelter: " + a.animal().shelter().name());
                System.out.println("Animal: " + a.animal().name());
              }
              adminHomepage();
              return;
            }
          }

          case 6 -> {
            ApiResponse response = ApiClient.get("/adoptions/fosters/all");
            if (response.isSuccess()) {
              List<Adoption> fosters = parseList(response.getBody(), Adoption.class);
              for (Adoption f : fosters) {
                System.out.println("User: " + f.user().name());
                System.out.println("Shelter: " + f.animal().shelter().name());
                System.out.println("Animal: " + f.animal().name());
              }

              adminHomepage();
              return;
            }
          }

          case 7 -> {
            // lostAndFoundMenu();
            adminHomepage();
            return;
          }

          case 8 -> {
            SecurityQuestion question = (SecurityQuestion) chooseOption(SecurityQuestion.values(), "SecurityQuestion");
            System.out.println("Answer: ");
            String answer = readLine();
            String json = jsonString("securityQuestion", question, "answer", answer);
            ApiResponse response = ApiClient.put("/accounts/changesq", json);
            System.out.println(response.getBody());
            shelterHomepage();
            return;
          }

          case 0 -> {
            System.out.println("Exiting terminal menu...");
            Platform.runLater(() -> showMainMenu());
            return;
          }
          default -> System.out.println("Invalid option!");
        }
      } catch (RuntimeException e) {
        if (e.getMessage().equals("Thread Interrupted")) {
          return;
        }
        throw e;
      }
    });
    consoleThread.setDaemon(true);
    consoleThread.start();

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
   * This method shows the lost and found menu
   */
  private void lostAndFoundHomePage() {
    javafx.application.Platform.runLater(() -> showTerminalScreen());

    if (consoleThread != null && consoleThread.isAlive()) {
      consoleThread.interrupt();
    }

    consoleThread = new Thread(() -> {
      try {

        System.out.println("=== LOST AND FOUND MENU===");
        System.out.println("1. Register Lost Animal");
        System.out.println("2. View Lost Animals");
        System.out.println("4. Remove Posting");
        System.out.println("5. View my lost animals");
        System.out.println("0. Logout");
        System.out.print("Option: ");
        int option = readInt();

        switch (option) {
          case 1 -> {
            System.out.println("\n=== REGISTER LOST ANIMAL ===");
            System.out.print("Name :");
            String name = readLine();

            AnimalType type = (AnimalType) chooseOption(AnimalType.values(), "Type");
            if (type == null)
              return;

            List<String> breeds = type.getBreeds();
            String race = null;
            if (breeds != null) {
              while (true) {
                System.out.println("Select Breed:");
                for (int i = 0; i < breeds.size(); i++) {
                  System.out.println((i + 1) + ". " + breeds.get(i));
                }
                System.out.print("Option: ");
                String input = readLine();
                try {
                  int breedOption = Integer.parseInt(input);
                  if (breedOption >= 1 && breedOption <= breeds.size()) {
                    race = breeds.get(breedOption - 1);
                    break;
                  }
                } catch (NumberFormatException ignored) {
                }
                System.out.println("Invalid option, please try again.");
              }
            }

            AnimalSize size = (AnimalSize) chooseOption(AnimalSize.values(), "Size");
            if (size == null)
              return;

            AnimalGender gender = (AnimalGender) chooseOption(AnimalGender.values(), "Gender");
            if (gender == null)
              return;

            System.out.print("Age: ");
            int age = readInt();

            AnimalColor color = (AnimalColor) chooseOption(AnimalColor.values(), "Color");
            if (color == null)
              return;

            System.out.println("Location");
            String location = readLine();

            System.out.print("Description: ");
            String description = readLine();

            System.out.println("Contact: ");
            int contact = readInt();

            String json = jsonString(
                "name", name,
                "type", type.name(),
                "race", race,
                "age", age,
                "color", color.name(),
                "size", size.name(),
                "gender", gender.name(),
                "contact", contact,
                "description", description,
                "location", location,
                "isLost", true);

            ApiResponse response = ApiClient.post("/lostandfound/create", json);

            if (response.isSuccess()) {
              System.out.println("Lost animal successfully registered");
            } else {
              System.out.println("Error: " + response.getBody());
            }

            lostAndFoundHomePage();
            return;
          }

          case 2 -> {
            ApiResponse response = ApiClient.get("/lostandfound/showlostanimals");
            List<LostAnimal> animals = parseList(response.getBody(), LostAnimal.class);
            for (LostAnimal animal : animals) {
              System.out.println(animal.toString() + "\n");

            }
            lostAndFoundHomePage();
            return;
          }

          case 3 -> {
            ApiResponse response = ApiClient.get("/lostandfound/showrescuedanimals");
            List<LostAnimal> animals = parseList(response.getBody(), LostAnimal.class);
            for (LostAnimal animal : animals) {
              System.out.println(animal.toString() + "\n");

            }
            shelterHomepage();
            return;
          }

          case 4 -> {
            ApiResponse response = ApiClient.get("/lostandfound/showanimalsbyaccount");
            List<LostAnimal> animals = parseList(response.getBody(), LostAnimal.class);
            LostAnimal choice = (LostAnimal) chooseOption(animals.toArray(), "animal");

            ApiResponse request = ApiClient.delete("/lostandfound/delete/" + choice.id());
            if(request.isSuccess()){
                System.out.println("Removed posting successfully!");
            }
            lostAndFoundHomePage();
            return;
          }

          case 5 -> {
            ApiResponse response = ApiClient.get("/lostandfound/showanimalsbyaccount");
            System.out.println(response.getBody());
            List<LostAnimal> animals = parseList(response.getBody(), LostAnimal.class);
            for (LostAnimal animal : animals) {
              System.out.println(animal.toString() + "\n");

            }
            lostAndFoundHomePage();
            return;

          }

          case 6 -> {
            System.out.println("\n=== REGISTER LOST ANIMAL ===");
            System.out.print("Name (if available) :");
            String name = readLine();

            AnimalType type = (AnimalType) chooseOption(AnimalType.values(), "Type");
            if (type == null)
              return;

            List<String> breeds = type.getBreeds();
            String race = null;
            if (breeds != null) {
              while (true) {
                System.out.println("Select Breed:");
                for (int i = 0; i < breeds.size(); i++) {
                  System.out.println((i + 1) + ". " + breeds.get(i));
                }
                System.out.print("Option: ");
                String input = readLine();
                try {
                  int breedOption = Integer.parseInt(input);
                  if (breedOption >= 1 && breedOption <= breeds.size()) {
                    race = breeds.get(breedOption - 1);
                    break;
                  }
                } catch (NumberFormatException ignored) {
                }
                System.out.println("Invalid option, please try again.");
              }
            }

            AnimalSize size = (AnimalSize) chooseOption(AnimalSize.values(), "Size");
            if (size == null)
              return;

            AnimalGender gender = (AnimalGender) chooseOption(AnimalGender.values(), "Gender");
            if (gender == null)
              return;

            System.out.print("Age: ");
            int age = readInt();

            AnimalColor color = (AnimalColor) chooseOption(AnimalColor.values(), "Color");
            if (color == null)
              return;

            System.out.println("Location");
            String location = readLine();

            System.out.print("Description: ");
            String description = readLine();

            System.out.println("Contact: ");
            int contact = readInt();

            String json = jsonString(
                "name", name,
                "type", type.name(),
                "race", race,
                "age", age,
                "color", color.name(),
                "size", size.name(),
                "gender", gender.name(),
                "contact", contact,
                "description", description,
                "location", location,
                "isLost", false);

            ApiResponse response = ApiClient.post("/lostandfound/create", json);

            if (response.isSuccess()) {
              System.out.println("Lost animal successfully registered");
            } else {
              System.out.println("Error: " + response.getBody());
            }

            lostAndFoundHomePage();
          }

          case 0 -> {
            System.out.println("Exiting terminal menu...");
            javafx.application.Platform.runLater(this::showMainMenu);
          }

          default -> {
            System.out.println("Invalid option!");
            lostAndFoundHomePage();
            return;
          }
        }
      } catch (RuntimeException e) {
        if (e.getMessage().equals("Thread Interrupted")) {
          return;
        }
        throw e;
      }
    });
    consoleThread.setDaemon(true);
    consoleThread.start();
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
      return mapper.writeValueAsString(dataMap);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Parse JSON string into a single object
   */
  public <T> T parseResponse(String json, Class<T> cl) {
    if (json == null || json.isEmpty()) {
      return null;
    }
    try {
      return mapper.readValue(json, cl);
    } catch (Exception e) {
      System.out.println("Failed to parse response: " + e.getMessage());
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Parse JSON string into a list of objects
   */
  public <T> List<T> parseList(String json, Class<T> cl) {
    if (json == null || json.isEmpty()) {
      return null;
    }
    try {
      return mapper.readValue(json,
          mapper.getTypeFactory().constructCollectionType(List.class, cl));
    } catch (Exception e) {
      System.out.println("Failed to parse list: " + e.getMessage());
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Method to choose an option from the enum classes
   */
  private Object chooseOption(Object[] values, String title) {
    while (true) {
      System.out.println("\n");
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
      while (true) {
        if (Thread.currentThread().isInterrupted()) {
          throw new RuntimeException("Thread Interrupted");
        }
        try {
          int value = sc.nextInt();
          sc.nextLine();
          return value;
        } catch (InputMismatchException e) {
          sc.nextLine();
          System.out.println("Please pick a valid number!");
        }
      }
    }
  }

  private String readLine() {
    synchronized (sc) {
      if (Thread.currentThread().isInterrupted()) {
        throw new RuntimeException("Thread Interrupted");
      }
      return sc.nextLine();
    }
  }

  private float readFloat() {
    synchronized (sc) {
      while (true) {
        if (Thread.currentThread().isInterrupted()) {
          throw new RuntimeException("Thread Interrupted");
        }
        try {
          float value = sc.nextFloat();
          sc.nextLine();
          return value;
        } catch (InputMismatchException e) {
          System.out.println("Please pick a valid number!");
          sc.nextLine();
        }
      }
    }
  }

  public static void main(String[] args) {
    launch(args);
  }
}
