// package AnimalCareCentre.client;
//
// import javafx.application.Application;
// import javafx.application.Platform;
// import javafx.stage.Stage;
// import javafx.geometry.Pos;
// import javafx.scene.control.*;
// import javafx.scene.control.Alert.AlertType;
// import javafx.scene.layout.VBox;
//
// import java.time.LocalDate;
//
// import AnimalCareCentre.enums.*;
// import AnimalCareCentre.views.*;
// import AnimalCareCentre.models.*;
//
// import java.util.InputMismatchException;
// import java.util.List;
// import java.util.Scanner;
// import java.awt.Toolkit;
//
// /**
//  * @author 51084 - Diogo Ferreira
//  * @author
//  * @author 51297 - Sara Canelas
//  * @author
//  */
// public class App extends Application {
//
//   private static Account loggedAcc;
//   private static Stage stage;
//   private static final ACCManager manager = new ACCManager();
//   private static Scanner sc = new Scanner(System.in);
//   private Thread consoleThread;
//
//   public void start(Stage stage) {
//     App.stage = stage;
//
//     stage.setTitle("AnimalCareCentre");
//     stage.setWidth(Toolkit.getDefaultToolkit().getScreenSize().getWidth());
//     stage.setHeight(Toolkit.getDefaultToolkit().getScreenSize().getHeight() - 100);
//     stage.show();
//     showMainMenu();
//   }
//
//   public static void main(String[] args) {
//     launch(args);
//   }
//
//   /**
//    * This method shows the main menu, where you can login or create accounts
//    */
//   public void showMainMenu() {
//     ACCScene scene = new ACCScene(stage, new ACCVBox());
//
//     // DELETE THIS COMMENT LATER
//     // only changed these buttons to get feedback for later, this is component 3 as well
//     Button login = new Button("Login");
//     Button create = new Button("Create Account");
//     Button exit = new Button("Exit");
//
//     login.setOnAction(e -> {
//       login();
//     });
//
//     create.setOnAction(e -> {
//       createAccount();
//     });
//
//     exit.setOnAction(e -> {
//       manager.exit();
//       System.exit(0);
//     });
//
//     scene.addItems(login, create, exit);
//
//   }
//
//   public void alternateShelterScreen(Status status) {
//     ACCScene scene = new ACCScene(stage, new ACCVBox());
//     Label label;
//     if (status == Status.PENDING) {
//       label = new Label("The Request to join AnimalCareCentre is pending. Please wait until it's validated!");
//     } else if (status == Status.REJECTED) {
//       label = new Label("We regret to inform you that the request to join AnimalCareCentre was denied!");
//     } else {
//       label = new Label("You have been banned from AnimalCareCentre!");
//     }
//     Button logout = new Button("Logout");
//     logout.setStyle("-fx-background-color: #333333; -fx-text-fill: white; -fx-font-size: 16px;");
//     scene.addItems(label, logout);
//
//     logout.setOnAction(e -> {
//       loggedAcc = null;
//       showMainMenu();
//       return;
//     });
//   }
//
//   /**
//    * This method shows the login screen
//    */
//   public void login() {
//     ACCScene scene = new ACCScene(stage, new ACCVBox());
//     Label emailLabel = new Label("Email:");
//     TextField email = new TextField();
//     email.setMaxWidth(250);
//     Label passLabel = new Label("Password:");
//     PasswordField password = new PasswordField();
//     password.setMaxWidth(250);
//     Button enter = new Button("Enter");
//     Button back = new Button("Back");
//     Button changePassword = new Button("Forgot Password");
//     scene.addItems(emailLabel, email, passLabel, password, enter, back, changePassword);
//
//     enter.setOnAction(e -> {
//       Account acc = manager.login(email.getText(), password.getText());
//       if (acc != null) {
//         loggedAcc = acc;
//         if (loggedAcc instanceof Shelter) {
//           if (((Shelter) loggedAcc).getStatus() == Status.ACCEPTED) {
//             shelterHomepage();
//           } else {
//             alternateShelterScreen(((Shelter) loggedAcc).getStatus());
//           }
//         } else if (loggedAcc instanceof User) {
//           userHomepage();
//         } else {
//           adminHomePage();
//         }
//       } else {
//         System.out.println("Wrong credentials!");
//       }
//     });
//
//     back.setOnAction(e -> {
//       showMainMenu();
//     });
//
//     changePassword.setOnAction(e -> {
//       changePassword();
//     });
//   }
//
//   /**
//    * This method shows the change password screen
//    */
//   public void changePassword() {
//     ACCScene scene = new ACCScene(stage, new ACCVBox());
//     Label emailLabel = new Label("Email:");
//     TextField email = new TextField();
//     Button enter = new Button("Enter");
//     Label passwordLabel = new Label("New password:");
//     PasswordField password = new PasswordField();
//     Button comfirm = new Button("Comfirm");
//     Button back = new Button("Back");
//     scene.addItems(emailLabel, email, enter, back);
//     enter.setOnAction(e -> {
//       if (manager.doesEmailExist(email.getText())) {
//         scene.clearContent();
//         scene.addItems(passwordLabel, password, comfirm, back);
//       } else {
//         showAlert(AlertType.ERROR, "Invalid Email", "The email is not yet registed");
//       }
//     });
//     comfirm.setOnAction(e -> {
//       String passwordValidation = manager.validatePassword(password.getText());
//       if (passwordValidation != null) {
//         showAlert(AlertType.ERROR, "Invalid Passowrd", passwordValidation);
//         return;
//       }
//       manager.changePassword(email.getText(), password.getText());
//       login();
//     });
//     back.setOnAction(e -> {
//       login();
//     });
//   }
//
//   /**
//    * This method shows the create account screen
//    */
//   public void createAccount() {
//     ACCScene scene = new ACCScene(stage, new ACCVBox());
//     Label type = new Label("Account type:");
//     ComboBox<String> accType = new ComboBox<>();
//     accType.getItems().addAll("User", "Admin", "Shelter");
//     Label nameLabel = new Label("Name:");
//     TextField name = new TextField();
//     name.setMaxWidth(250);
//     Label emailLabel = new Label("Email:");
//     TextField email = new TextField();
//     email.setMaxWidth(250);
//     Label passLabel = new Label("Password:");
//     PasswordField password = new PasswordField();
//     password.setMaxWidth(250);
//     Label locationLabel = new Label("Location:");
//     TextField location = new TextField();
//     location.setMaxWidth(250);
//     Label secLabel = new Label("Security Question:");
//     ComboBox<SecurityQuestion> sec = new ComboBox<>();
//     sec.getItems().addAll(SecurityQuestion.values());
//     Label answerLabel = new Label("Answer:");
//     TextField answer = new TextField();
//     answer.setMaxWidth(250);
//
//     VBox vbox = new VBox();
//     vbox.setAlignment(Pos.CENTER_LEFT);
//     vbox.getChildren().addAll(type, accType, nameLabel, name, emailLabel, email, passLabel, password, locationLabel,
//         location, secLabel, sec,
//         answerLabel, answer);
//     vbox.setMaxWidth(250);
//     vbox.setSpacing(10);
//     scene.addItems(vbox);
//
//     PasswordField adminCode = new PasswordField();
//     adminCode.setMaxWidth(250);
//     Label birthLabel = new Label("Birthdate:");
//     DatePicker birthDate = new DatePicker();
//     birthDate.setMaxWidth(250);
//     Label contactLabel = new Label("Contact:");
//     TextField contact = new TextField();
//     contact.setMaxWidth(250);
//     Label foundYear = new Label("Foundation year:");
//     TextField year = new TextField();
//     year.setMaxWidth(250);
//     Label adminLabel = new Label("Admin code:");
//
//     year.setTextFormatter(new TextFormatter<>(change -> {
//       String num = change.getControlNewText();
//       if (num.matches("\\d{0,4}")) {
//         return change;
//       }
//       return null;
//     }));
//
//     contact.setTextFormatter(new TextFormatter<>(change -> {
//       String num = change.getControlNewText();
//       if (num.matches("\\d{0,9}")) {
//         return change;
//       }
//       return null;
//     }));
//
//     Button create = new Button("Create");
//     Button back = new Button("Back");
//
//     create.setOnAction(e -> {
//
//       if (accType.getValue() == null) {
//         showAlert(AlertType.ERROR, "Missing Account Type!", "Please select an account type!");
//         return;
//       }
//
//       if (manager.doesEmailExist(email.getText())) {
//         showAlert(AlertType.ERROR, "Invalid Email!", "This email is already registered!");
//         return;
//       }
//
//       if (!manager.validateEmail(email.getText())) {
//         showAlert(AlertType.ERROR, "Invalid Email!", "Please enter a valid email address!");
//         return;
//       }
//
//       String passwordValidation = manager.validatePassword(password.getText());
//       if (passwordValidation != null) {
//         showAlert(AlertType.ERROR, "Invalid Passowrd", passwordValidation);
//         return;
//       }
//
//       if (accType.getValue().equals("User")) {
//
//         if (sec.getValue() == null
//             || !manager.validateFields(name.getText(), email.getText(), password.getText(), location.getText(),
//                 sec.getValue().toString(), answer.getText(), birthDate.getValue().toString(), contact.getText())) {
//           showAlert(AlertType.ERROR, "Empty Fields!", "All fields are required!");
//           return;
//         }
//
//         if (birthDate.getValue() != null && birthDate.getValue().isAfter(LocalDate.now())) {
//           showAlert(AlertType.ERROR, "Invalid Date!", "The birth date cannot be in the future");
//           return;
//         }
//
//         manager.createUserAccount(name.getText(), email.getText(), password.getText(), location.getText(),
//             sec.getValue(), answer.getText(), birthDate.getValue(), Integer.parseInt(contact.getText()));
//         showMainMenu();
//
//       } else if (accType.getValue().equals("Admin")) {
//
//         if (sec.getValue() == null
//             || !manager.validateFields(name.getText(), email.getText(), password.getText(), location.getText(),
//                 sec.getValue().toString(), answer.getText(), adminCode.getText())) {
//           showAlert(AlertType.ERROR, "Empty Fields!", "All fields are required!");
//           return;
//         }
//
//         if (!adminCode.getText().equals("lasagna")) {
//           showAlert(AlertType.ERROR, "Wrong Admin Code", "The Admin code is wrong! Returning...");
//           showMainMenu();
//           return;
//         }
//
//         manager.createAdminAccount(name.getText(), email.getText(), password.getText(), location.getText(),
//             sec.getValue(), answer.getText());
//         showMainMenu();
//
//       } else if (accType.getValue().equals("Shelter")) {
//
//         if (sec.getValue() == null
//             || !manager.validateFields(name.getText(), email.getText(), password.getText(), location.getText(),
//                 sec.getValue().toString(), answer.getText(), year.getText(), contact.getText())) {
//           showAlert(AlertType.ERROR, "Empty Fields!", "All fields are required!");
//           return;
//         }
//
//         if (Integer.parseInt(year.getText()) > LocalDate.now().getYear()) {
//           showAlert(AlertType.ERROR, "Invalid Foundation year!", "The foundation year cannot be in the future");
//           return;
//         }
//
//         manager.createShelterAccount(name.getText(), email.getText(), password.getText(), location.getText(),
//             sec.getValue(), answer.getText(), Integer.parseInt(year.getText()),
//             Integer.parseInt(contact.getText()));
//         showMainMenu();
//       }
//
//     });
//
//     back.setOnAction(e -> {
//       showMainMenu();
//     });
//
//     accType.valueProperty().addListener((obs, old, selected) -> {
//       vbox.getChildren().clear();
//       vbox.getChildren().addAll(type, accType, nameLabel, name, emailLabel, email, passLabel, password, locationLabel,
//           location, secLabel, sec,
//           answerLabel, answer);
//
//       if (selected.equals("User")) {
//         vbox.getChildren().addAll(birthLabel, birthDate, contactLabel, contact);
//       } else if (selected.equals("Shelter")) {
//         vbox.getChildren().addAll(contactLabel, contact, foundYear, year);
//       } else {
//         vbox.getChildren().addAll(adminLabel, adminCode);
//       }
//     });
//
//     scene.addItems(create, back);
//   }
//
//   /**
//    * This method shows an alert
//    */
//   public void showAlert(AlertType type, String title, String text) {
//     Alert alert = new Alert(type);
//     alert.setTitle(title);
//     alert.setHeaderText(null);
//     alert.setContentText(text);
//     alert.showAndWait();
//   }
//
//   /**
//    * This method is used temporarily to change to the terminal screen
//    */
//   private void showTerminalScreen() {
//     ACCScene scene = new ACCScene(stage, new ACCVBox());
//     Button logout = new Button("Logout");
//     logout.setStyle("-fx-background-color: #333333; -fx-text-fill: white; -fx-font-size: 16px;");
//     scene.addItems(logout);
//
//     // DELETE THIS LATER
//     // This is just temporary to test something, it's gonna be on the commit just so
//     // everyone tries it on their pcs, but delete before component 2 delivery,
//     // it's just preparation for component 3
//     if (loggedAcc instanceof User) {
//       scene.setHeader("Home", "Search Animals", "Search Shelters", "Lost and Found", "Account");
//     } else if (loggedAcc instanceof Shelter) {
//       scene.setHeader("Home", "Animals", "Lost and Found", "Account");
//     } else {
//       scene.setHeader("Home", "Search Animals", "Search Shelters", "Lost and Found", "Account");
//     }
//
//     logout.setOnAction(e -> {
//       System.out.println("\n Logout efetuado!");
//       loggedAcc = null;
//       showMainMenu();
//       return;
//     });
//
//   }
//
//   public void showAnimal(ShelterAnimal animal) {
//     System.out.println(animal);
//     System.out.println("Menu: ");
//     System.out.println("1 - Sponsor Animal");
//     System.out.println("2 - Adopt Animal");
//     System.out.println("3 - Foster Animal");
//     System.out.println("0 - Back");
//     int opc = readInt();
//     switch (opc) {
//       case 1 -> {
//         System.out.println("Insert the amount of money you wish to give as a sponsorship");
//         float amount = readFloat();
//         User user = (User) loggedAcc;
//         manager.createSponsorship(user, animal, amount);
//         userHomepage();
//         return;
//       }
//
//       case 2 -> {
//         manager.adoptAnimal((User) loggedAcc, animal, AdoptionType.FOR_ADOPTION);
//         System.out.println("Congratulations! Your request to adopt " + animal.getName() + " has been submitted!");
//         userHomepage();
//         return;
//       }
//
//       case 3 -> {
//         manager.adoptAnimal((User) loggedAcc, animal, AdoptionType.FOR_FOSTER);
//         System.out.println("Congratulations! Your request to foster " + animal.getName() + " has been submitted!");
//         userHomepage();
//         return;
//       }
//
//       case 0 -> {
//         userHomepage();
//         return;
//       }
//     }
//   }
//
//   /**
//    * This method allows to search for animals
//    */
//   public void searchAnimalMenu() {
//     System.out.println("\n=== SEARCH ANIMAL ===");
//
//     String[] options = { "Search by Keyword", "Search by Type", "Search by Color" };
//     String opt = (String) chooseOption(options, "Search Option");
//     if (opt == null) {
//       Platform.runLater(this::userHomepage);
//       return;
//     }
//
//     switch (opt) {
//       case "Search by Keyword" -> {
//         System.out.println("What would you like to search?");
//         String search = readLine();
//         List<ShelterAnimal> animals = manager.searchAnimalByKeyword(search);
//
//         if (animals == null || animals.isEmpty()) {
//           System.out.println("\nNo matches! Returning...");
//         } else {
//           ShelterAnimal choice = (ShelterAnimal) chooseOption(animals.toArray(), "Animal");
//           if (choice == null) {
//             javafx.application.Platform.runLater(this::userHomepage);
//             return;
//           }
//           showAnimal(choice);
//         }
//         searchAnimalMenu();
//         return;
//       }
//
//       case "Search by Type" -> {
//         AnimalType chosenType = (AnimalType) chooseOption(AnimalType.values(), "Type");
//         if (chosenType == null) {
//           javafx.application.Platform.runLater(this::userHomepage);
//           return;
//         }
//         List<ShelterAnimal> animals = manager.searchAnimalByParameter("type", chosenType);
//
//         if (animals == null || animals.isEmpty()) {
//           System.out.println("\nNo matches! Returning...");
//         } else {
//           ShelterAnimal choice = (ShelterAnimal) chooseOption(animals.toArray(), "Animal");
//           if (choice == null) {
//             javafx.application.Platform.runLater(this::userHomepage);
//             return;
//           }
//           showAnimal(choice);
//           return;
//         }
//         searchAnimalMenu();
//         return;
//       }
//
//       case "Search by Color" -> {
//         AnimalColor chosenColor = (AnimalColor) chooseOption(AnimalColor.values(), "Color");
//         if (chosenColor == null) {
//           javafx.application.Platform.runLater(this::userHomepage);
//         }
//         List<ShelterAnimal> animals = manager.searchAnimalByParameter("color", chosenColor);
//
//         if (animals == null || animals.isEmpty()) {
//           System.out.println("\nNo matches! Returning...");
//         } else {
//           ShelterAnimal choice = (ShelterAnimal) chooseOption(animals.toArray(), "Animal");
//           if (choice == null) {
//             javafx.application.Platform.runLater(this::userHomepage);
//             return;
//           }
//           showAnimal(choice);
//         }
//         searchAnimalMenu();
//         return;
//       }
//     }
//   }
//
//   public void showShelter(Shelter shelter) {
//     System.out.println(shelter);
//     System.out.println("Animals: ");
//     List<ShelterAnimal> animals = manager.getAvailableAnimalsByShelter(shelter);
//     ShelterAnimal choice = (ShelterAnimal) chooseOption(animals.toArray(), "Animal");
//     if (choice == null) {
//       javafx.application.Platform.runLater(this::userHomepage);
//       return;
//     }
//     showAnimal(choice);
//   }
//
//   public void searchShelter() {
//     List<Shelter> shelters = manager.searchShelters();
//     Shelter choice = (Shelter) chooseOption(shelters.toArray(), "Shelter");
//     if (choice == null) {
//       javafx.application.Platform.runLater(this::userHomepage);
//       return;
//     }
//     showShelter(choice);
//   }
//
//   /**
//    * This method shows admin's homepage
//    */
//   private void adminHomePage() {
//     javafx.application.Platform.runLater(() -> showTerminalScreen());
//
//     try {
//
//       if (consoleThread != null && consoleThread.isAlive()) {
//         consoleThread.interrupt();
//       }
//       consoleThread = new Thread(() -> {
//         int option;
//
//         System.out.println("=== ADMIN MENU ===");
//         System.out.println("1. View Shelter Requests");
//         System.out.println("2. View Available Shelters");
//         System.out.println("3. View All Sponsorships");
//         System.out.println("4. View All Animals");
//         System.out.println("5. View All Adoptions");
//         System.out.println("6. View All Fosters");
//         System.out.println("7 Lost and Found");
//         System.out.println("0. Logout");
//         System.out.print("Option: ");
//         option = readInt();
//
//         switch (option) {
//           case 1 -> {
//             List<Shelter> shelters = manager.showShelterRequests();
//             Shelter choice = (Shelter) chooseOption(shelters.toArray(), "Shelter Request");
//             if (choice == null) {
//               javafx.application.Platform.runLater(this::adminHomePage);
//               return;
//             }
//             String[] requestAction = { "Accept", "Reject" };
//             String action = (String) chooseOption(requestAction, "Shelter Request");
//             if (action == null) {
//               javafx.application.Platform.runLater(this::adminHomePage);
//               return;
//             }
//             if (action.equals("Accept")) {
//               manager.changeShelterStatus(choice, Status.ACCEPTED);
//               System.out.println("Shelter accepted");
//             } else {
//               manager.changeShelterStatus(choice, Status.REJECTED);
//               System.out.println("Shelter rejected");
//             }
//             adminHomePage();
//             return;
//           }
//
//           case 2 -> {
//             List<Shelter> shelters = manager.searchShelters();
//             Shelter choice = (Shelter) chooseOption(shelters.toArray(), "Shelter");
//             if (choice == null) {
//               javafx.application.Platform.runLater(this::adminHomePage);
//               return;
//             }
//             String[] requestAction = { "Ban Shelter, View info" };
//             String action = (String) chooseOption(requestAction, "Shelter");
//             if (action == null) {
//               javafx.application.Platform.runLater(this::adminHomePage);
//               return;
//             }
//             if (action.equals("Ban Shelter")) {
//               manager.changeShelterStatus(choice, Status.BANNED);
//             } else {
//               System.out.println(choice); // for now it only prints shelter info
//             }
//             adminHomePage();
//             return;
//           }
//
//           case 3 -> {
//             System.out.println(manager.viewAllSponsorships());
//             adminHomePage();
//             return;
//           }
//
//           case 4 -> {
//             System.out.println(manager.viewAllAnimals());
//             adminHomePage();
//             return;
//           }
//
//           case 5 -> {
//             System.out.println(manager.viewAllAdoptions(AdoptionType.FOR_ADOPTION));
//             adminHomePage();
//             return;
//           }
//
//           case 6 -> {
//             System.out.println(manager.viewAllAdoptions(AdoptionType.FOR_FOSTER));
//             adminHomePage();
//             return;
//           }
//
//           case 7 -> {
//             lostAndFoundMenu();
//             return;
//           }
//
//           case 0 -> {
//             System.out.println("Exiting terminal menu...");
//             Platform.runLater(() -> showMainMenu());
//             return;
//           }
//           default -> System.out.println("Invalid option!");
//         }
//       });
//       consoleThread.start();
//
//     } catch (
//
//     InputMismatchException e) {
//       System.out.println("Please pick a valid option!");
//       userHomepage();
//     }
//   }
//
//   /**
//    * This method shows user's homepage
//    */
//   private void userHomepage() {
//     javafx.application.Platform.runLater(() -> showTerminalScreen());
//
//     try {
//
//       if (consoleThread != null && consoleThread.isAlive()) {
//         consoleThread.interrupt();
//       }
//       consoleThread = new Thread(() -> {
//         int option;
//
//         System.out.println("=== USER MENU ===");
//         System.out.println("1. Search Animal");
//         System.out.println("2. Search Shelter");
//         System.out.println("3. See My Adoptions Requests");
//         System.out.println("4. See My Foster Requests");
//         System.out.println("5. Lost and Found");
//         System.out.println("0. Logout");
//         System.out.print("Option: ");
//         option = readInt();
//
//         switch (option) {
//           case 1 -> {
//             searchAnimalMenu();
//             return;
//           }
//
//           case 2 -> {
//             searchShelter();
//             return;
//           }
//
//           case 3 -> {
//             List<Adoption> adoptions = manager.getAdoptionsByUser((User) loggedAcc, AdoptionType.FOR_ADOPTION);
//
//             if (adoptions.size() == 0) {
//               System.out.println("Sorry, no adopt requests found!");
//               userHomepage();
//               return;
//             }
//
//             else {
//               for (Adoption adopt : adoptions) {
//                 System.out.println(adopt.toString() + "\n");
//               }
//               userHomepage();
//               return;
//             }
//           }
//           case 4 -> {
//             List<Adoption> fosters = manager.getAdoptionsByUser((User) loggedAcc, AdoptionType.FOR_FOSTER);
//
//             if (fosters.size() == 0) {
//               System.out.println("Sorry, no foster requests found!");
//               userHomepage();
//               return;
//             }
//
//             else {
//               for (Adoption f : fosters) {
//                 System.out.println(f.toString() + "\n");
//               }
//               userHomepage();
//               return;
//             }
//           }
//
//           case 5 -> {
//             lostAndFoundMenu();
//             return;
//           }
//
//           case 0 -> {
//             System.out.println("Exiting terminal menu...");
//             Platform.runLater(() -> showMainMenu());
//             return;
//           }
//           default -> System.out.println("Invalid option!");
//         }
//       });
//       consoleThread.start();
//
//     } catch (
//
//     InputMismatchException e) {
//       System.out.println("Please pick a valid option!");
//       userHomepage();
//     }
//   }
//
//   private void lostAndFoundMenu() {
//     System.out.println("1: See lost animals");
//     System.out.println("2: Register lost animal");
//     System.out.println("3: Found my animal");
//     System.out.println("4: Exit");
//     int choice = 0;
//     try {
//       choice = readInt();
//     } catch (Exception e) {
//       System.out.println("Invalid input");
//       lostAndFoundMenu();
//       return;
//     }
//     switch (choice) {
//       case 1 -> {
//         manager.showLostAnimals();
//         lostAndFoundMenu();
//         return;
//       }
//       case 2 -> {
//
//         System.out.println("\n=== REGISTER LOST ANIMAL ===");
//         System.out.print("Name: ");
//         String name = readLine();
//
//         // Animal Type
//         AnimalType type = (AnimalType) chooseOption(AnimalType.values(), "Type");
//         if (type == null) {
//           lostAndFoundMenu();
//           return;
//         }
//
//         // Breeds
//         List<String> breeds = type.getBreeds();
//         if (breeds == null) {
//           lostAndFoundMenu();
//           return;
//         }
//         String race = null;
//         while (true) {
//           System.out.println("Select breed: ");
//           for (int i = 0; i < breeds.size(); i++) {
//             System.out.println((i + 1) + ". " + breeds.get(i));
//           }
//           System.out.print("Enter race: ");
//           String input = readLine();
//
//           try {
//             int breedOption = Integer.parseInt(input);
//             if (breedOption >= 1 && breedOption <= breeds.size()) {
//               race = breeds.get(breedOption - 1);
//               break;
//             }
//           } catch (NumberFormatException ignored) {
//           }
//
//           System.out.println("Invalid option, please try again.");
//
//         }
//
//         // Color
//         AnimalColor color = (AnimalColor) chooseOption(AnimalColor.values(), "Color");
//         if (color == null) {
//           lostAndFoundMenu();
//           return;
//         }
//
//         // Size
//         AnimalSize size = (AnimalSize) chooseOption(AnimalSize.values(), "Size");
//         if (size == null) {
//           lostAndFoundMenu();
//           return;
//         }
//
//         // Gender
//         AnimalGender gender = (AnimalGender) chooseOption(AnimalGender.values(), "Gender");
//         if (gender == null) {
//           lostAndFoundMenu();
//           return;
//         }
//
//         System.out.print("Description: ");
//         String description = readLine();
//
//         System.out.print("Contact: ");
//         int contact = readInt(); // limpa o \n
//
//         System.out.print("Location: ");
//         String location = readLine();
//
//         manager.registerLostAnimal((User) loggedAcc, name, type, race, color, size, gender, description, contact,
//             location);
//         System.out.println("\nAnimal registered successfully!\n");
//
//         lostAndFoundMenu();
//       }
//       case 3 -> {
//         manager.foundMyAnimal(loggedAcc);
//         lostAndFoundMenu();
//       }
//       case 4 -> {
//         userHomepage();
//         return;
//       }
//     }
//   }
//
//   /**
//    * Method to choose an option from the enum classes
//    */
//   private Object chooseOption(Object[] values, String title) {
//     while (true) {
//       System.out.println("\n\n\n");
//       System.out.println("Select " + title + ":");
//       System.out.println("\n");
//
//       for (int i = 0; i < values.length; i++) {
//         System.out.println((i + 1) + ". " + values[i] + "\n");
//       }
//
//       System.out.println("0. Back");
//       String input = readLine();
//
//       try {
//         int option = Integer.parseInt(input);
//         if (option == 0)
//           return null;
//         if (option >= 1 && option <= values.length) {
//           return values[option - 1];
//         }
//       } catch (NumberFormatException ignored) {
//       }
//
//       System.out.println("Invalid option, please try again.");
//     }
//   }
//
//   /**
//    * This method shows shelter's homepage
//    */
//   private void shelterHomepage() {
//     javafx.application.Platform.runLater(() -> showTerminalScreen());
//
//     try {
//       if (consoleThread != null && consoleThread.isAlive()) {
//         consoleThread.interrupt();
//       }
//       consoleThread = new Thread(() -> {
//         System.out.println("=== SHELTER MENU ===");
//         System.out.println("1. Register Animal");
//         System.out.println("2. View My Animals");
//         System.out.println("3. View Pending Adoption Requests");
//         System.out.println("4. View Pending Foster Requests");
//         System.out.println("5. View Adoptions");
//         System.out.println("6. View Fosters");
//         System.out.println("0. Logout");
//         System.out.print("Option: ");
//         int option = readInt();
//
//         switch (option) {
//           case 1 -> {
//             System.out.println("\n=== REGISTER ANIMAL ===");
//             System.out.print("Name: ");
//             String name = readLine();
//
//             // Type
//             AnimalType chosenType = (AnimalType) chooseOption(AnimalType.values(), "Type");
//             if (chosenType == null) {
//               javafx.application.Platform.runLater(this::shelterHomepage);
//               return;
//             }
//
//             // Breed
//             List<String> breeds = chosenType.getBreeds();
//             if (breeds == null) {
//               shelterHomepage();
//             }
//             String race = null;
//
//             while (true) {
//               System.out.println("Select Breed:");
//               for (int i = 0; i < breeds.size(); i++) {
//                 System.out.println((i + 1) + ". " + breeds.get(i));
//               }
//
//               System.out.print("Option: ");
//               String input = readLine();
//
//               try {
//                 int breedOption = Integer.parseInt(input);
//                 if (breedOption >= 1 && breedOption <= breeds.size()) {
//                   race = breeds.get(breedOption - 1);
//                   break;
//                 }
//               } catch (NumberFormatException ignored) {
//               }
//
//               System.out.println("Invalid option, please try again.");
//             }
//
//             // Size
//             AnimalSize size = (AnimalSize) chooseOption(AnimalSize.values(), "Size");
//             if (size == null) {
//               javafx.application.Platform.runLater(this::shelterHomepage);
//               return;
//             }
//
//             // Gender
//             AnimalGender gender = (AnimalGender) chooseOption(AnimalGender.values(), "Gender");
//             if (gender == null) {
//               javafx.application.Platform.runLater(this::shelterHomepage);
//               return;
//             }
//
//             System.out.print("Age: ");
//             int age = readInt();
//
//             // Color
//             AnimalColor color = (AnimalColor) chooseOption(AnimalColor.values(), "Color");
//             if (color == null) {
//               javafx.application.Platform.runLater(this::shelterHomepage);
//               return;
//             }
//
//             System.out.print("Description: ");
//             String description = readLine();
//
//             // Adoption Type
//             AdoptionType adoptionType = (AdoptionType) chooseOption(AdoptionType.values(), "Adoption Type");
//             if (adoptionType == null) {
//               javafx.application.Platform.runLater(this::shelterHomepage);
//               return;
//             }
//
//             manager.registerAnimal((Shelter) loggedAcc, name, chosenType, race, size, gender, age, color, description,
//                 adoptionType);
//             System.out.println("\nAnimal registered successfully!\n");
//
//             javafx.application.Platform.runLater(this::shelterHomepage);
//           }
//
//           case 2 -> {
//             List<ShelterAnimal> animals = manager.getAnimalsByShelter((Shelter) loggedAcc);
//
//             if (animals.isEmpty()) {
//               System.out.println("You have no registered animals.");
//             } else {
//               for (ShelterAnimal a : animals) {
//                 System.out.println("\n" + a.toString());
//                 System.out.println("Sponsors:");
//                 if (a.getSponsors().isEmpty()) {
//                   System.out.println("  No sponsors yet.");
//                 } else {
//                   for (Sponsorship s : a.getSponsors()) {
//                     System.out.println(s);
//                   }
//                 }
//                 System.out.println("Listed for:");
//                 System.out.println(a.getListedFor());
//                 if (!a.getAdoptions().isEmpty()) {
//                   System.out.println(a.getAdoptions());
//                 }
//               }
//             }
//
//             shelterHomepage();
//             return;
//           }
//
//           case 3 -> {
//               // View Pending Adoption Requests
//               List<Adoption> pendingAdoptions = manager.getPendingRequestsByShelter((Shelter) loggedAcc, AdoptionType.FOR_ADOPTION);
//               if (pendingAdoptions.isEmpty()) {
//                   System.out.println("No pending adoption requests.");
//               } else {
//                   Adoption choice = (Adoption) chooseOption(pendingAdoptions.toArray(), "Adoption Request");
//                   if (choice == null) {
//                       javafx.application.Platform.runLater(this::shelterHomepage);
//                       return;
//                   }
//
//                   System.out.println("1. Accept");
//                   System.out.println("2. Reject");
//                   System.out.println("0. Back");
//                   System.out.print("Option: ");
//                   int action = readInt();
//                   if (action == 0) {
//                         javafx.application.Platform.runLater(this::shelterHomepage);
//                         return;
//                     }
//
//                   if (action == 1) {
//                       manager.changeAdoptionStatus(choice, Status.ACCEPTED);
//                       System.out.println("Adoption request accepted!");
//                   } else {
//                       manager.changeAdoptionStatus(choice, Status.REJECTED);
//                       System.out.println("Adoption request rejected.");
//                   }
//               }
//               shelterHomepage();
//               return;
//             }
//
//           case 4 -> {
//               // View Pending Foster Requests (similar to case 3)
//               List<Adoption> pendingFosters = manager.getPendingRequestsByShelter((Shelter) loggedAcc, AdoptionType.FOR_FOSTER);
//               if (pendingFosters.isEmpty()) {
//                   System.out.println("No pending foster requests.");
//               } else {
//                   Adoption choice = (Adoption) chooseOption(pendingFosters.toArray(), "Foster Request");
//                   if (choice == null) {
//                       javafx.application.Platform.runLater(this::shelterHomepage);
//                       return;
//                   }
//
//                   System.out.println("1. Accept");
//                   System.out.println("2. Reject");
//                   System.out.println("0. Back");
//                   System.out.print("Option: ");
//                   int action = readInt();
//                   if (action == 0) {
//                       javafx.application.Platform.runLater(this::shelterHomepage);
//                        return;
//                   }
//
//                   if (action == 1) {
//                       manager.changeAdoptionStatus(choice, Status.ACCEPTED);
//                       System.out.println("Foster request accepted!");
//                   } else {
//                       manager.changeAdoptionStatus(choice, Status.REJECTED);
//                       System.out.println("Foster request rejected.");
//                   }
//               }
//               shelterHomepage();
//               return;
//             }
//
//           case 5 -> {
//               // View Completed Adoptions
//               List<Adoption> completedAdoptions = manager.getCompletedAdoptionsByShelter((Shelter) loggedAcc, AdoptionType.FOR_ADOPTION);
//               if (completedAdoptions.isEmpty()) {
//                   System.out.println("No completed adoptions until now.");
//               } else {
//                   System.out.println("\n=== COMPLETED ADOPTIONS ===");
//                   for (Adoption adoption : completedAdoptions) {
//                       System.out.println(adoption.toString() + "\n");
//                   }
//               }
//               shelterHomepage();
//               return;
//             }
//
//           case 6 -> {
//               // View Completed Fosters (NOVO)
//               List<Adoption> completedFosters = manager.getCompletedAdoptionsByShelter((Shelter) loggedAcc, AdoptionType.FOR_FOSTER);
//               if (completedFosters.isEmpty()) {
//                   System.out.println("No completed fosters yet.");
//               } else {
//                   System.out.println("\n=== COMPLETED FOSTERS ===");
//                   for (Adoption foster : completedFosters) {
//                       System.out.println(foster.toString() + "\n");
//                   }
//               }
//               shelterHomepage();
//               return;
//           }
//
//           case 0 -> {
//             System.out.println("Exiting terminal menu...");
//             javafx.application.Platform.runLater(this::showMainMenu);
//           }
//
//           default -> {
//             System.out.println("Invalid option!");
//             shelterHomepage();
//             return;
//           }
//         }
//
//       });
//       consoleThread.start();
//
//     } catch (
//
//     InputMismatchException e) {
//       System.out.println("Please pick a valid option!");
//       readLine();
//       shelterHomepage();
//     }
//   }
//
//   private int readInt() {
//     synchronized (sc) {
//       int value = sc.nextInt();
//       sc.nextLine();
//       return value;
//     }
//   }
//
//   private String readLine() {
//     synchronized (sc) {
//       return sc.nextLine();
//     }
//   }
//
//   private float readFloat() {
//     synchronized (sc) {
//       float value = sc.nextFloat();
//       sc.nextLine();
//       return value;
//     }
//   }
//
// }
