package AnimalCareCentre;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;

import java.time.LocalDate;

import AnimalCareCentre.enums.*;
import AnimalCareCentre.views.*;
import AnimalCareCentre.models.*;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.awt.Toolkit;

/**
 * @author 51084 - Diogo Ferreira
 * @author
 * @author 51297 - Sara Canelas
 * @author
 */
public class App extends Application {

  private static Account loggedAcc;
  private static Stage stage;
  private static final ACCManager manager = new ACCManager();
  private static Scanner sc = new Scanner(System.in);
  private Thread consoleThread;
  // private static Navigator nav; commenting navigator for now

  public void start(Stage stage) {
    App.stage = stage;
    // nav = new Navigator(stage, manager);

    stage.setTitle("AnimalCareCentre");
    stage.setWidth(Toolkit.getDefaultToolkit().getScreenSize().getWidth());
    stage.setHeight(Toolkit.getDefaultToolkit().getScreenSize().getHeight() - 100);
    stage.show();
    showMainMenu();
  }

  public static void main(String[] args) {
    launch(args);
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
      manager.exit();
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
      Account acc = manager.login(email.getText(), password.getText());
      if (acc != null) {
        loggedAcc = acc;
        if (loggedAcc instanceof Shelter) {
          shelterHomepage();
        } else if (loggedAcc instanceof User) {
          userHomepage();
        }
      } else {
        System.out.println("Wrong credentials!");
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
    Button comfirm = new Button("Comfirm");
    Button back = new Button("Back");
    scene.addItems(emailLabel, email, enter, back);
    enter.setOnAction(e -> {
      if (manager.doesEmailExist(email.getText())) {
        scene.clearContent();
        scene.addItems(passwordLabel, password, comfirm, back);
      } else {
        showAlert(AlertType.ERROR, "Invalid Email", "The email is not yet registed");
      }
    });
    comfirm.setOnAction(e -> {
      String passwordValidation = manager.validatePassword(password.getText());
      if (passwordValidation != null) {
        showAlert(AlertType.ERROR, "Invalid Passowrd", passwordValidation);
        return;
      }
      manager.changePassword(email.getText(), password.getText());
      login();
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
    name.setMaxWidth(150);
    Label emailLabel = new Label("Email:");
    TextField email = new TextField();
    email.setMaxWidth(250);
    Label passLabel = new Label("Password:");
    PasswordField password = new PasswordField();
    password.setMaxWidth(150);
    Label locationLabel = new Label("Location:");
    TextField location = new TextField();
    location.setMaxWidth(150);
    Label secLabel = new Label("Security Question:");
    ComboBox<SecurityQuestion> sec = new ComboBox<>();
    sec.getItems().addAll(SecurityQuestion.values());
    Label answerLabel = new Label("Answer:");
    TextField answer = new TextField();
    answer.setMaxWidth(200);

    VBox vbox = new VBox();
    vbox.setAlignment(Pos.CENTER_LEFT);
    vbox.getChildren().addAll(type, accType, nameLabel, name, emailLabel, email, passLabel, password, locationLabel,
        location, secLabel, sec,
        answerLabel, answer);
    vbox.setMaxWidth(250);
    vbox.setSpacing(10);
    scene.addItems(vbox);

    Label birthLabel = new Label("Birthdate:");
    DatePicker birthDate = new DatePicker();
    Label contactLabel = new Label("Contact:");
    TextField contact = new TextField();
    contact.setMaxWidth(150);
    Label foundYear = new Label("Foundation year:");
    TextField year = new TextField();
    year.setMaxWidth(80);

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

      if (manager.doesEmailExist(email.getText())) {
        showAlert(AlertType.ERROR, "Invalid Email!", "This email is already registered!");
        return;
      }

      if (!manager.validateEmail(email.getText())) {
        showAlert(AlertType.ERROR, "Invalid Email!", "Please enter a valid email address!");
        return;
      }

      String passwordValidation = manager.validatePassword(password.getText());
      if (passwordValidation != null) {
        showAlert(AlertType.ERROR, "Invalid Passowrd", passwordValidation);
        return;
      }

      if (accType.getValue().equals("User")) {

        if (sec.getValue() == null
            || !manager.validateFields(name.getText(), email.getText(), password.getText(), location.getText(),
                sec.getValue().toString(), answer.getText(), birthDate.getValue().toString(), contact.getText())) {
          showAlert(AlertType.ERROR, "Empty Fields!", "All fields are required!");
          return;
        }

        if (birthDate.getValue() != null && birthDate.getValue().isAfter(LocalDate.now())) {
          showAlert(AlertType.ERROR, "Invalid Date!", "The birth date cannot be in the future");
          return;
        }

        manager.createUserAccount(name.getText(), email.getText(), password.getText(), location.getText(),
            sec.getValue(), answer.getText(), birthDate.getValue(), Integer.parseInt(contact.getText()));
        showMainMenu();

      } else if (accType.getValue().equals("Admin")) {

        if (sec.getValue() == null
            || !manager.validateFields(name.getText(), email.getText(), password.getText(), location.getText(),
                sec.getValue().toString(), answer.getText())) {
          showAlert(AlertType.ERROR, "Empty Fields!", "All fields are required!");
          return;
        }

        manager.createAdminAccount(name.getText(), email.getText(), password.getText(), location.getText(),
            sec.getValue(), answer.getText());
        showMainMenu();

      } else if (accType.getValue().equals("Shelter")) {

        if (sec.getValue() == null
            || !manager.validateFields(name.getText(), email.getText(), password.getText(), location.getText(),
                sec.getValue().toString(), answer.getText(), year.getText(), contact.getText())) {
          showAlert(AlertType.ERROR, "Empty Fields!", "All fields are required!");
          return;
        }

        if (Integer.parseInt(year.getText()) > LocalDate.now().getYear()) {
          showAlert(AlertType.ERROR, "Invalid Foundation year!", "The foundation year cannot be in the future");
          return;
        }

        manager.createShelterAccount(name.getText(), email.getText(), password.getText(), location.getText(),
            sec.getValue(), answer.getText(), Integer.parseInt(year.getText()),
            Integer.parseInt(contact.getText()));
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
   * This method is used temporarily to change to the terminal screen
   */
  private void showTerminalScreen() {
    ACCScene scene = new ACCScene(stage, new ACCVBox());
    Button logout = new Button("Logout");
    logout.setStyle("-fx-background-color: #333333; -fx-text-fill: white; -fx-font-size: 16px;");
    scene.addItems(logout);

    logout.setOnAction(e -> {
      System.out.println("\n Logout efetuado!");
      loggedAcc = null;
      showMainMenu();
      return;
    });

  }

  public void showAnimal(ShelterAnimal animal) {
    System.out.println(animal);
    System.out.println("Menu: ");
    System.out.println("1 - Sponsor Animal");
    System.out.println("2 - Adopt Animal");
    System.out.println("3 - Foster Animal");
    System.out.println("0 - Back");
    int opc = sc.nextInt();
    sc.nextLine();
    switch (opc) {
      case 1 -> {
        System.out.println("Insert the amount of money you wish to give as a sponsorship");
        float amount = sc.nextFloat();
        sc.nextLine();
        User user = (User) loggedAcc;
        manager.createSponsorship(user, animal, amount);
      }

      case 2 -> {
        manager.adoptAnimal((User) loggedAcc, animal, AdoptionType.FOR_ADOPTION);
        System.out.println("Congratulations! Your request to adopt " + animal.getName() + " has been submitted!");
      }

      case 3 -> {
        manager.adoptAnimal((User) loggedAcc, animal, AdoptionType.FOR_FOSTER);
        System.out.println("Congratulations! Your request to foster " + animal.getName() + " has been submitted!");
      }

      case 0 -> {
        userHomepage();
        return;
      }
    }
  }

  /**
   * This method allows to search for animals
   */
  public void searchAnimalMenu() {
    System.out.println("\n=== SEARCH ANIMAL ===");

    String[] options = { "Search by Keyword", "Search by Type", "Search by Color" };
    String opt = (String) chooseOption(options, "Search Option");
    if (opt == null) {
      Platform.runLater(this::userHomepage);
      return;
    }

    switch (opt) {
      case "Search by Keyword" -> {
        System.out.println("What would you like to search?");
        String search = sc.nextLine();
        List<ShelterAnimal> animals = manager.searchAnimalByKeyword(search);

        if (animals == null || animals.isEmpty()) {
          System.out.println("\nNo matches! Returning...");
        } else {
          ShelterAnimal choice = (ShelterAnimal) chooseOption(animals.toArray(), "Animal");
          if (choice == null) {
            javafx.application.Platform.runLater(this::userHomepage);
            return;
          }
          showAnimal(choice);
        }
        searchAnimalMenu();
        return;
      }

      case "Search by Type" -> {
        AnimalType chosenType = (AnimalType) chooseOption(AnimalType.values(), "Type");
        if (chosenType == null) {
          javafx.application.Platform.runLater(this::userHomepage);
          return;
        }
        List<ShelterAnimal> animals = manager.searchAnimalByParameter("type", chosenType);

        if (animals == null || animals.isEmpty()) {
          System.out.println("\nNo matches! Returning...");
        } else {
          ShelterAnimal choice = (ShelterAnimal) chooseOption(animals.toArray(), "Animal");
          if (choice == null) {
            javafx.application.Platform.runLater(this::userHomepage);
            return;
          }
          showAnimal(choice);
          return;
        }
        searchAnimalMenu();
        return;
      }

      case "Search by Color" -> {
        AnimalColor chosenColor = (AnimalColor) chooseOption(AnimalColor.values(), "Color");
        if (chosenColor == null) {
          javafx.application.Platform.runLater(this::userHomepage);
        }
        List<ShelterAnimal> animals = manager.searchAnimalByParameter("color", chosenColor);

        if (animals == null || animals.isEmpty()) {
          System.out.println("\nNo matches! Returning...");
        } else {
          ShelterAnimal choice = (ShelterAnimal) chooseOption(animals.toArray(), "Animal");
          if (choice == null) {
            javafx.application.Platform.runLater(this::userHomepage);
            return;
          }
          showAnimal(choice);
        }
        searchAnimalMenu();
        return;
      }
    }
  }

  public void showShelter(Shelter shelter) {
    System.out.println(shelter);
    System.out.println("Animals: ");
    List<ShelterAnimal> animals = manager.getAvailableAnimalsByShelter(shelter);
    ShelterAnimal choice = (ShelterAnimal) chooseOption(animals.toArray(), "Animal");
    if (choice == null) {
      javafx.application.Platform.runLater(this::userHomepage);
      return;
    }
    showAnimal(choice);
  }

  public void searchShelter() {
    List<Shelter> shelters = manager.searchShelters();
    Shelter choice = (Shelter) chooseOption(shelters.toArray(), "Shelter");
    if (choice == null) {
      javafx.application.Platform.runLater(this::userHomepage);
      return;
    }
    showShelter(choice);
  }

  /**
   * This method shows user's homepage
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
        System.out.println("3. See My Adoptions");
        System.out.println("4 Lost and Found");
        System.out.println("0. Logout");
        System.out.print("Option: ");
        option = sc.nextInt();
        sc.nextLine();

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
            System.out.println(manager.getUserAdoptions((User) loggedAcc));
            userHomepage();
            return;
          }
          case 4 -> {
            lostAndFoundMenu();
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

    } catch (

    InputMismatchException e) {
      System.out.println("Please pick a valid option!");
      userHomepage();
    }
  }

  private void lostAndFoundMenu() {
    System.out.println("1: See lost animals");
    System.out.println("2: Register lost animal");
    System.out.println("3: Found my animal");
    System.out.println("4: Exit");
    int choice = 0;
    try {
      choice = sc.nextInt();
      sc.nextLine();
    } catch (Exception e) {
      System.out.println("Invalid input");
      lostAndFoundMenu();
      return;
    }
    switch (choice) {
      case 1 -> {
        manager.showLostAnimals();
        lostAndFoundMenu();
        return;
      }
      case 2 -> {

        System.out.println("\n=== REGISTER LOST ANIMAL ===");
        System.out.print("Name: ");
        String name = sc.nextLine();

        // Animal Type
        AnimalType type = (AnimalType) chooseOption(AnimalType.values(), "Type");
        if (type == null) {
          lostAndFoundMenu();
          return;
        }

        // Breeds
        List<String> breeds = type.getBreeds();
        if (breeds == null) {
          lostAndFoundMenu();
          return;
        }
        String race = null;
        while (true) {
          System.out.println("Select breed: ");
          for (int i = 0; i < breeds.size(); i++) {
            System.out.println((i + 1) + ". " + breeds.get(i));
          }
          System.out.print("Enter race: ");
          String input = sc.nextLine();

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

        // Color
        AnimalColor color = (AnimalColor) chooseOption(AnimalColor.values(), "Color");
        if (color == null) {
          lostAndFoundMenu();
          return;
        }

        // Size
        AnimalSize size = (AnimalSize) chooseOption(AnimalSize.values(), "Size");
        if (size == null) {
          lostAndFoundMenu();
          return;
        }

        // Gender
        AnimalGender gender = (AnimalGender) chooseOption(AnimalGender.values(), "Gender");
        if (gender == null) {
          lostAndFoundMenu();
          return;
        }

        System.out.print("Description: ");
        String description = sc.nextLine();

        System.out.print("Contact: ");
        int contact = sc.nextInt();
        sc.nextLine(); // limpa o \n

        System.out.print("Location: ");
        String location = sc.nextLine();

        manager.registerLostAnimal((User) loggedAcc, name, type, race, color, size, gender, description, contact,
            location);
        System.out.println("\nAnimal registered successfully!\n");

        lostAndFoundMenu();
      }
      case 3 -> {
        manager.foundMyAnimal(loggedAcc);
        lostAndFoundMenu();
      }
      case 4 -> {
        userHomepage();
        return;
      }
    }
  }

  /**
   * Method to choose an option from the enum classes
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
      String input = sc.nextLine();

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
        System.out.println("0. Logout");
        System.out.print("Option: ");
        int option = sc.nextInt();
        sc.nextLine();

        switch (option) {
          case 1 -> {
            System.out.println("\n=== REGISTER ANIMAL ===");
            System.out.print("Name: ");
            String name = sc.nextLine();

            // Type
            AnimalType chosenType = (AnimalType) chooseOption(AnimalType.values(), "Type");
            if (chosenType == null) {
              javafx.application.Platform.runLater(this::shelterHomepage);
              return;
            }

            // Breed
            List<String> breeds = chosenType.getBreeds();
            if (breeds == null) {
              shelterHomepage();
            }
            String race = null;

            while (true) {
              System.out.println("Select Breed:");
              for (int i = 0; i < breeds.size(); i++) {
                System.out.println((i + 1) + ". " + breeds.get(i));
              }

              System.out.print("Option: ");
              String input = sc.nextLine();

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

            // Size
            AnimalSize size = (AnimalSize) chooseOption(AnimalSize.values(), "Size");
            if (size == null) {
              javafx.application.Platform.runLater(this::shelterHomepage);
              return;
            }

            // Gender
            AnimalGender gender = (AnimalGender) chooseOption(AnimalGender.values(), "Gender");
            if (gender == null) {
              javafx.application.Platform.runLater(this::shelterHomepage);
              return;
            }

            System.out.print("Age: ");
            int age = sc.nextInt();
            sc.nextLine();

            // Color
            AnimalColor color = (AnimalColor) chooseOption(AnimalColor.values(), "Color");
            if (color == null) {
              javafx.application.Platform.runLater(this::shelterHomepage);
              return;
            }

            System.out.print("Description: ");
            String description = sc.nextLine();

            // Adoption Type
            AdoptionType adoptionType = (AdoptionType) chooseOption(AdoptionType.values(), "Adoption Type");
            if (adoptionType == null) {
              javafx.application.Platform.runLater(this::shelterHomepage);
              return;
            }

            manager.registerAnimal((Shelter) loggedAcc, name, chosenType, race, size, gender, age, color, description,
                adoptionType);
            System.out.println("\nAnimal registered successfully!\n");

            javafx.application.Platform.runLater(this::shelterHomepage);
          }

          case 2 -> {
            List<ShelterAnimal> animals = manager.getAnimalsByShelter((Shelter) loggedAcc);

            if (animals.isEmpty()) {
              System.out.println("You have no registered animals.");
            } else {
              for (ShelterAnimal a : animals) {
                System.out.println("\n" + a.toString());
                System.out.println("Sponsors:");
                if (a.getSponsors().isEmpty()) {
                  System.out.println("  No sponsors yet.");
                } else {
                  for (Sponsorship s : a.getSponsors()) {
                    System.out.println(s);
                  }
                }
                System.out.println("Listed for:");
                System.out.println(a.getListedFor());
                if (!a.getAdoptions().isEmpty()) {
                  System.out.println(a.getAdoptions());
                }
              }
            }

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

    } catch (

    InputMismatchException e) {
      System.out.println("Please pick a valid option!");
      sc.nextLine();
      shelterHomepage();
    }
  }

}
