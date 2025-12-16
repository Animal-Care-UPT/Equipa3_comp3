package AnimalCareCentre.client.views;

import java.util.List;
import AnimalCareCentre.client.ApiClient;
import AnimalCareCentre.client.ApiResponse;
import AnimalCareCentre.client.Navigator;
import AnimalCareCentre.client.Utility;
import AnimalCareCentre.client.components.ACCMenuButton;
import AnimalCareCentre.client.components.ACCTableView;
import AnimalCareCentre.client.components.ACCPopover;
import AnimalCareCentre.client.components.ACCScene;
import AnimalCareCentre.client.components.ACCVBox;
import AnimalCareCentre.client.enums.Status;
import AnimalCareCentre.client.records.Adoption;
import AnimalCareCentre.client.records.Shelter;
import AnimalCareCentre.client.records.Sponsorship;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.stage.Stage;

public class AdminHomepage {

  private Navigator nav;
  private Stage stage;

  public AdminHomepage(Navigator nav, Stage stage) {
    this.nav = nav;
    this.stage = stage;
    show();
  }

  private void show() {
    ACCScene scene = new ACCScene(stage, new ACCVBox());
    new NavBar(Navigator.getLoggedRole(), nav, scene);

    ACCMenuButton viewShelterRequests = new ACCMenuButton("Shelter Requests");
    ACCMenuButton viewAllSponsorships = new ACCMenuButton("Sponsorships");
    ACCMenuButton viewAllAdoptions = new ACCMenuButton("Adoptions");
    ACCMenuButton viewAllFosters = new ACCMenuButton("Fosters");

    viewShelterRequests.setOnAction(e -> viewShelterRequests());
    viewAllSponsorships.setOnAction(e -> viewSponsorships());
    viewAllAdoptions.setOnAction(e -> seeAdoptions());
    viewAllFosters.setOnAction(e -> seeFosters());

    scene.addItems(viewShelterRequests, viewAllSponsorships, viewAllAdoptions, viewAllFosters);
  }

  public void viewShelterRequests() {
    ACCTableView<Shelter> table = new ACCTableView<>();

    TableColumn<Shelter, String> shelterIDColumn = new TableColumn<>("Shelter ID");
    shelterIDColumn.setCellValueFactory(cellData -> {
      Shelter shelter = cellData.getValue();
      String value = (shelter != null) ? String.valueOf(shelter.id()) : "";
      return new SimpleStringProperty(value);
    });

    TableColumn<Shelter, String> shelterColumn = new TableColumn<>("Shelter Name");
    shelterColumn.setCellValueFactory(cellData -> {
      Shelter shelter = cellData.getValue();
      String value = (shelter != null) ? shelter.name() : "";
      return new SimpleStringProperty(value);
    });

    TableColumn<Shelter, String> localColumn = new TableColumn<>("Location");
    localColumn.setCellValueFactory(cellData -> {
      Shelter shelter = cellData.getValue();
      String value = (shelter != null) ? shelter.location() : "";
      return new SimpleStringProperty(value);
    });

    TableColumn<Shelter, String> yearColumn = new TableColumn<>("Foundation Year");
    yearColumn.setCellValueFactory(cellData -> {
      Shelter shelter = cellData.getValue();
      String value = (shelter != null) ? String.valueOf(shelter.foundationYear()) : "";
      return new SimpleStringProperty(value);
    });

    TableColumn<Shelter, String> contactColumn = new TableColumn<>("Contact");
    contactColumn.setCellValueFactory(cellData -> {
      Shelter shelter = cellData.getValue();
      String value = (shelter != null) ? shelter.contact() : "";
      return new SimpleStringProperty(value);
    });

    TableColumn<Shelter, Void> buttonColumn = new TableColumn<>("Action");
    buttonColumn.setCellFactory(col -> new TableCell<Shelter, Void>() {
      ACCMenuButton button = new ACCMenuButton("Aceitar");
      {
        button.setOnAction(e -> {
          Shelter shelter = getTableRow().getItem();
          if (shelter != null) {
            String jsonStatus = Utility.jsonString("status", Status.AVAILABLE);
            ApiResponse acptResponse = ApiClient.put("/shelters/status?id=" + shelter.id(), jsonStatus);
          }
        });
      }

      protected void updateItem(Void item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
          setGraphic(null);
        } else {
          setGraphic(button);
        }
      }
    });

    table.getColumns().addAll(shelterIDColumn, shelterColumn, localColumn, yearColumn, contactColumn, buttonColumn);
    table.setPrefHeight(300);
    table.setColumnResizePolicy(ACCTableView.CONSTRAINED_RESIZE_POLICY);

    table.widthProperty().addListener((obs, oldVal, newVal) -> {
      double width = newVal.doubleValue();
      double colWidth = width / 6;
      shelterIDColumn.setMinWidth(colWidth);
      shelterColumn.setMinWidth(colWidth);
      localColumn.setMinWidth(colWidth);
      yearColumn.setMinWidth(colWidth);
      contactColumn.setMinWidth(colWidth);
      buttonColumn.setMinWidth(colWidth);
    });

    ApiResponse response = ApiClient.get("/shelters/pending");
    if (response.isSuccess()) {
      List<Shelter> shelters = Utility.parseList(response.getBody(), Shelter.class);
      table.setItems(FXCollections.observableArrayList(shelters));
    }

    ScrollPane scrollPane = new ScrollPane(table);
    scrollPane.setFitToWidth(true);
    scrollPane.setFitToHeight(true);
    scrollPane.setPrefSize(980, 650);

    ACCPopover popover = new ACCPopover(scrollPane, "Pending Shelters");
    popover.setPrefSize(1200, 900);
    popover.show(stage);
  }

  public void viewSponsorships() {
    ACCTableView<Sponsorship> table = new ACCTableView<>();

    TableColumn<Sponsorship, String> userIDColumn = new TableColumn<>("User ID");
    userIDColumn.setCellValueFactory(cellData -> {
      Sponsorship sponsorship = cellData.getValue();
      String value = (sponsorship != null) ? String.valueOf(sponsorship.userId()) : "";
      return new SimpleStringProperty(value);
    });

    TableColumn<Sponsorship, String> userColumn = new TableColumn<>("User");
    userColumn.setCellValueFactory(cellData -> {
      Sponsorship sponsorship = cellData.getValue();
      String value = (sponsorship != null) ? sponsorship.userName() : "";
      return new SimpleStringProperty(value);
    });

    TableColumn<Sponsorship, String> animalIDColumn = new TableColumn<>("Animal ID");
    animalIDColumn.setCellValueFactory(cellData -> {
      Sponsorship sponsorship = cellData.getValue();
      String value = (sponsorship != null) ? String.valueOf(sponsorship.animalId()) : "";
      return new SimpleStringProperty(value);
    });

    TableColumn<Sponsorship, String> animalColumn = new TableColumn<>("Animal");
    animalColumn.setCellValueFactory(cellData -> {
      Sponsorship sponsorship = cellData.getValue();
      String value = (sponsorship != null) ? sponsorship.animalName() : "";
      return new SimpleStringProperty(value);
    });

    TableColumn<Sponsorship, String> amountColumn = new TableColumn<>("Amount");
    amountColumn.setCellValueFactory(cellData -> {
      Sponsorship sponsorship = cellData.getValue();
      String value = (sponsorship != null) ? String.valueOf(sponsorship.amount()) : "";
      return new SimpleStringProperty(value);
    });

    TableColumn<Sponsorship, String> dateColumn = new TableColumn<>("Start Date");
    dateColumn.setCellValueFactory(cellData -> {
      Sponsorship sponsorship = cellData.getValue();
      String value = (sponsorship != null) ? String.valueOf(sponsorship.startDate()) : "";
      return new SimpleStringProperty(value);
    });

    table.getColumns().addAll(userIDColumn, userColumn, animalIDColumn, animalColumn, amountColumn, dateColumn);
    table.setPrefHeight(300);
    table.setColumnResizePolicy(ACCTableView.CONSTRAINED_RESIZE_POLICY);

    table.widthProperty().addListener((obs, oldVal, newVal) -> {
      double width = newVal.doubleValue();
      double colWidth = width / 6;
      userIDColumn.setMinWidth(colWidth);
      userColumn.setMinWidth(colWidth);
      animalIDColumn.setMinWidth(colWidth);
      animalColumn.setMinWidth(colWidth);
      amountColumn.setMinWidth(colWidth);
      dateColumn.setMinWidth(colWidth);
    });

    ApiResponse response = ApiClient.get("/sponsorships/all");
    if (response.isSuccess()) {
      List<Sponsorship> sponsors = Utility.parseList(response.getBody(), Sponsorship.class);
      table.setItems(FXCollections.observableArrayList(sponsors));
    }

    ScrollPane scrollPane = new ScrollPane(table);
    scrollPane.setFitToWidth(true);
    scrollPane.setFitToHeight(true);
    scrollPane.setPrefSize(980, 650);

    ACCPopover popover = new ACCPopover(scrollPane, "Sponsorships");
    popover.setPrefSize(1200, 900);
    popover.show(stage);
  }

  public void seeAdoptions() {
    ACCTableView<Adoption> table = new ACCTableView<>();

    TableColumn<Adoption, String> userColumn = new TableColumn<>("User");
    userColumn.setCellValueFactory(cellData -> {
      Adoption adoption = cellData.getValue();
      String value = (adoption != null && adoption.user() != null) ? adoption.user().name() : "";
      return new SimpleStringProperty(value);
    });

    TableColumn<Adoption, String> shelterColumn = new TableColumn<>("Shelter");
    shelterColumn.setCellValueFactory(cellData -> {
      Adoption adoption = cellData.getValue();
      String value = (adoption != null && adoption.shelter() != null) ? adoption.shelter().name() : "";
      return new SimpleStringProperty(value);
    });

    TableColumn<Adoption, String> animalColumn = new TableColumn<>("Animal");
    animalColumn.setCellValueFactory(cellData -> {
      Adoption adoption = cellData.getValue();
      String value = (adoption != null && adoption.animal() != null) ? adoption.animal().name() : "";
      return new SimpleStringProperty(value);
    });

    table.getColumns().addAll(userColumn, shelterColumn, animalColumn);
    table.setPrefHeight(300);
    table.setColumnResizePolicy(ACCTableView.CONSTRAINED_RESIZE_POLICY);

    table.widthProperty().addListener((obs, oldVal, newVal) -> {
      double width = newVal.doubleValue();
      double colWidth = width / 3;
      userColumn.setMinWidth(colWidth);
      shelterColumn.setMinWidth(colWidth);
      animalColumn.setMinWidth(colWidth);
    });

    ApiResponse response = ApiClient.get("/adoptions/all");
    if (response.isSuccess()) {
      List<Adoption> adoptions = Utility.parseList(response.getBody(), Adoption.class);
      table.setItems(FXCollections.observableArrayList(adoptions));
    }

    ScrollPane scrollPane = new ScrollPane(table);
    scrollPane.setFitToWidth(true);
    scrollPane.setFitToHeight(true);
    scrollPane.setPrefSize(980, 650);

    ACCPopover popover = new ACCPopover(scrollPane, "Adoptions");
    popover.setPrefSize(1200, 900);
    popover.show(stage);
  }

  public void seeFosters() {
    ACCTableView<Adoption> table = new ACCTableView<>();

    TableColumn<Adoption, String> userColumn = new TableColumn<>("User");
    userColumn.setCellValueFactory(cellData -> {
      Adoption adoption = cellData.getValue();
      String value = (adoption != null && adoption.user() != null) ? adoption.user().name() : "";
      return new SimpleStringProperty(value);
    });

    TableColumn<Adoption, String> shelterColumn = new TableColumn<>("Shelter");
    shelterColumn.setCellValueFactory(cellData -> {
      Adoption adoption = cellData.getValue();
      String value = (adoption != null && adoption.shelter() != null) ? adoption.shelter().name() : "";
      return new SimpleStringProperty(value);
    });

    TableColumn<Adoption, String> animalColumn = new TableColumn<>("Animal");
    animalColumn.setCellValueFactory(cellData -> {
      Adoption adoption = cellData.getValue();
      String value = (adoption != null && adoption.animal() != null) ? adoption.animal().name() : "";
      return new SimpleStringProperty(value);
    });

    table.getColumns().addAll(userColumn, shelterColumn, animalColumn);
    table.setPrefHeight(300);
    table.setColumnResizePolicy(ACCTableView.CONSTRAINED_RESIZE_POLICY);

    table.widthProperty().addListener((obs, oldVal, newVal) -> {
      double width = newVal.doubleValue();
      double colWidth = width / 3;
      userColumn.setMinWidth(colWidth);
      shelterColumn.setMinWidth(colWidth);
      animalColumn.setMinWidth(colWidth);
    });

    ApiResponse response = ApiClient.get("/adoptions/fosters/all");
    if (response.isSuccess()) {
      List<Adoption> adoptions = Utility.parseList(response.getBody(), Adoption.class);
      table.setItems(FXCollections.observableArrayList(adoptions));
    }

    ScrollPane scrollPane = new ScrollPane(table);
    scrollPane.setFitToWidth(true);
    scrollPane.setFitToHeight(true);
    scrollPane.setPrefSize(980, 650);

    ACCPopover popover = new ACCPopover(scrollPane, "Fosters");
    popover.setPrefSize(1200, 900);
    popover.show(stage);
  }
}
