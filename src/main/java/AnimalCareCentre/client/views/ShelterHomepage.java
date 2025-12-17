package AnimalCareCentre.client.views;

import java.io.File;
import java.util.List;
import AnimalCareCentre.client.ApiClient;
import AnimalCareCentre.client.ApiResponse;
import AnimalCareCentre.client.Navigator;
import AnimalCareCentre.client.Utility;
import AnimalCareCentre.client.components.*;
import AnimalCareCentre.client.enums.Status;
import AnimalCareCentre.client.records.Adoption;
import AnimalCareCentre.client.records.ShelterDonation;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.stage.Stage;

public class ShelterHomepage {
  private Navigator nav;
  private Stage stage;

  public ShelterHomepage(Navigator nav, Stage stage) {
    this.nav = nav;
    this.stage = stage;
    show();
  }

  private void show() {
    ACCScene scene = new ACCScene(stage, new ACCVBox());
    new NavBar(Navigator.getLoggedRole(), nav, scene);

    ACCMenuButton register = new ACCMenuButton("Register Animal");
    ACCMenuButton viewPendingRequests = new ACCMenuButton("Pending Requests");
    ACCMenuButton viewAcceptedRequests = new ACCMenuButton("Accepted Requests");
    ACCMenuButton viewDonations = new ACCMenuButton("Donations");
    ACCMenuButton addImage = new ACCMenuButton("Add Image");

    register.setOnAction(e -> nav.registerAnimal());
    viewPendingRequests.setOnAction(e -> viewPendingRequests());
    viewAcceptedRequests.setOnAction(e -> viewAcceptedRequests());
    viewDonations.setOnAction(e -> viewDonations());
    addImage.setOnAction(e -> addImage());

    scene.addItems(register, viewPendingRequests, viewAcceptedRequests, viewDonations, addImage);
  }

  private void viewPendingRequests() {
    ACCTableView<Adoption> table = new ACCTableView<>();

    TableColumn<Adoption, String> userColumn = new TableColumn<>("User");
    userColumn.setCellValueFactory(cellData -> {
      var user = cellData.getValue().user();
      return new SimpleStringProperty(user != null ? user.name() : "N/A");
    });

    TableColumn<Adoption, String> animalColumn = new TableColumn<>("Animal");
    animalColumn.setCellValueFactory(cellData -> {
      var animal = cellData.getValue().animal();
      return new SimpleStringProperty(animal != null ? animal.name() : "N/A");
    });

    TableColumn<Adoption, String> typeColumn = new TableColumn<>("Type");
    typeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().type())));

    TableColumn<Adoption, String> dateColumn = new TableColumn<>("Request Date");
    dateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(
        cellData.getValue().requestDate() != null ? cellData.getValue().requestDate().toString() : "N/A"));

    TableColumn<Adoption, Void> actionColumn = new TableColumn<>("Action");
    actionColumn.setCellFactory(col -> new TableCell<Adoption, Void>() {
      ACCButton acceptButton = new ACCButton("Accept");
      ACCButton rejectButton = new ACCButton("Reject");
      ACCHBox buttonBox = new ACCHBox();

      {
        buttonBox.setSpacing(10);
        buttonBox.addItems(acceptButton, rejectButton);

        acceptButton.setOnAction(e -> {
          Adoption adoption = getTableRow().getItem();
          if (adoption != null) {
            String jsonBody = Utility.jsonString("adoptionId", adoption.id(), "newStatus", Status.ACCEPTED);
            ApiResponse response = ApiClient.put("/adoptions/change/status", jsonBody);
            if (response.isSuccess()) {
              table.getItems().remove(adoption);
              Utility.showAlert(AlertType.INFORMATION, "Success", "Request accepted successfully.");
            } else {
              Utility.showAlert(AlertType.ERROR, "Error", response.getBody());
            }
          }
        });

        rejectButton.setOnAction(e -> {
          Adoption adoption = getTableRow().getItem();
          if (adoption != null) {
            String jsonBody = Utility.jsonString("adoptionId", adoption.id(), "newStatus", Status.REJECTED);
            ApiResponse response = ApiClient.put("/adoptions/change/status", jsonBody);
            if (response.isSuccess()) {
              table.getItems().remove(adoption);
              Utility.showAlert(AlertType.INFORMATION, "Success", "Request rejected.");
            } else {
              Utility.showAlert(AlertType.ERROR, "Error", response.getBody());
            }
          }
        });
      }

      protected void updateItem(Void item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
          setGraphic(null);
        } else {
          setGraphic(buttonBox);
        }
      }
    });

    table.getColumns().addAll(userColumn, animalColumn, typeColumn, dateColumn, actionColumn);
    table.setPrefHeight(300);
    table.setColumnResizePolicy(ACCTableView.CONSTRAINED_RESIZE_POLICY);

    table.widthProperty().addListener((obs, oldVal, newVal) -> {
      double width = newVal.doubleValue();
      double colWidth = width / 5;
      userColumn.setMinWidth(colWidth);
      animalColumn.setMinWidth(colWidth);
      typeColumn.setMinWidth(colWidth);
      dateColumn.setMinWidth(colWidth);
      actionColumn.setMinWidth(colWidth);
    });

    ApiResponse response = ApiClient.get("/adoptions/shelter/pending");
    if (response.isSuccess()) {
      List<Adoption> adoptions = Utility.parseList(response.getBody(), Adoption.class);
      table.setItems(FXCollections.observableArrayList(adoptions));
    }

    ScrollPane scrollPane = new ScrollPane(table);
    scrollPane.setFitToWidth(true);
    scrollPane.setFitToHeight(true);
    scrollPane.setPrefSize(980, 650);

    ACCPopover popover = new ACCPopover(scrollPane, "Pending Requests");
    popover.setPrefSize(1200, 900);
    popover.show(stage);
  }

  private void viewAcceptedRequests() {
    ACCTableView<Adoption> table = new ACCTableView<>();

    TableColumn<Adoption, String> userColumn = new TableColumn<>("User");
    userColumn.setCellValueFactory(cellData -> {
      var user = cellData.getValue().user();
      return new SimpleStringProperty(user != null ? user.name() : "N/A");
    });

    TableColumn<Adoption, String> animalColumn = new TableColumn<>("Animal");
    animalColumn.setCellValueFactory(cellData -> {
      var animal = cellData.getValue().animal();
      return new SimpleStringProperty(animal != null ? animal.name() : "N/A");
    });

    TableColumn<Adoption, String> typeColumn = new TableColumn<>("Type");
    typeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().type())));

    TableColumn<Adoption, String> adoptionDateColumn = new TableColumn<>("Adoption Date");
    adoptionDateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(
        cellData.getValue().adoptionDate() != null ? cellData.getValue().adoptionDate().toString() : "N/A"));

    table.getColumns().addAll(userColumn, animalColumn, typeColumn, adoptionDateColumn);
    table.setPrefHeight(300);
    table.setColumnResizePolicy(ACCTableView.CONSTRAINED_RESIZE_POLICY);

    table.widthProperty().addListener((obs, oldVal, newVal) -> {
      double width = newVal.doubleValue();
      double colWidth = width / 4;
      userColumn.setMinWidth(colWidth);
      animalColumn.setMinWidth(colWidth);
      typeColumn.setMinWidth(colWidth);
      adoptionDateColumn.setMinWidth(colWidth);
    });

    ApiResponse response = ApiClient.get("/adoptions/shelter/accepted");
    if (response.isSuccess()) {
      List<Adoption> adoptions = Utility.parseList(response.getBody(), Adoption.class);
      table.setItems(FXCollections.observableArrayList(adoptions));
    }

    ScrollPane scrollPane = new ScrollPane(table);
    scrollPane.setFitToWidth(true);
    scrollPane.setFitToHeight(true);
    scrollPane.setPrefSize(980, 650);

    ACCPopover popover = new ACCPopover(scrollPane, "Accepted Requests");
    popover.setPrefSize(1200, 900);
    popover.show(stage);
  }

  private void viewDonations() {
    ACCTableView<ShelterDonation> table = new ACCTableView<>();

    TableColumn<ShelterDonation, String> userColumn = new TableColumn<>("Donor");
    userColumn.setCellValueFactory(cellData -> {
      return new SimpleStringProperty(cellData.getValue().userName());
    });

    TableColumn<ShelterDonation, String> amountColumn = new TableColumn<>("Amount");
    amountColumn
        .setCellValueFactory(cellData -> new SimpleStringProperty(String.format("%.2f", cellData.getValue().amount())));

    TableColumn<ShelterDonation, String> dateColumn = new TableColumn<>("Date");
    dateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(
        cellData.getValue().donationDate() != null ? cellData.getValue().donationDate().toString() : "N/A"));

    table.getColumns().addAll(userColumn, amountColumn, dateColumn);
    table.setPrefHeight(300);
    table.setColumnResizePolicy(ACCTableView.CONSTRAINED_RESIZE_POLICY);

    table.widthProperty().addListener((obs, oldVal, newVal) -> {
      double width = newVal.doubleValue();
      double colWidth = width / 3;
      userColumn.setMinWidth(colWidth);
      amountColumn.setMinWidth(colWidth);
      dateColumn.setMinWidth(colWidth);
    });

    ApiResponse response = ApiClient.get("/donations/shelter/donations");
    if (response.isSuccess()) {
      List<ShelterDonation> donations = Utility.parseList(response.getBody(), ShelterDonation.class);
      table.setItems(FXCollections.observableArrayList(donations));
    }

    ScrollPane scrollPane = new ScrollPane(table);
    scrollPane.setFitToWidth(true);
    scrollPane.setFitToHeight(true);
    scrollPane.setPrefSize(980, 650);

    ACCPopover popover = new ACCPopover(scrollPane, "Donations");
    popover.setPrefSize(1200, 900);
    popover.show(stage);
  }

  private void addImage() {
    File image = Utility.selectImageFile(stage);
    ApiResponse response = ApiClient.postWithFile("/shelters/images/self", image);
    if (response.isSuccess()) {
      Utility.showAlert(AlertType.INFORMATION, "Success", response.getBody());
    } else {
      Utility.showAlert(AlertType.ERROR, "Error", response.getBody());
    }
  }
}
