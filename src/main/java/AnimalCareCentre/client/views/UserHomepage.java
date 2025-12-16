package AnimalCareCentre.client.views;

import AnimalCareCentre.client.ApiClient;
import AnimalCareCentre.client.ApiResponse;
import AnimalCareCentre.client.Navigator;
import AnimalCareCentre.client.Utility;
import AnimalCareCentre.client.components.*;
import AnimalCareCentre.client.records.Adoption;
import AnimalCareCentre.client.records.ShelterDonation;
import AnimalCareCentre.client.records.Sponsorship;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.scene.control.*;
import java.util.List;
import javafx.stage.Stage;


import java.util.List;

public class UserHomepage {

  private Navigator nav;
  private Stage stage;
  private ACCPopover popover;

  public UserHomepage(Navigator nav, Stage stage) {
    this.nav = nav;
    this.stage = stage;
    show();
  }

  private void show() {
    ACCScene scene = new ACCScene(stage, new ACCVBox());
    new NavBar(nav.getLoggedRole(), nav, scene);
    
      ACCMenuButton pendingRequests = new ACCMenuButton("Pending Requests");
      pendingRequests.setOnAction(e -> pendingRequestPopover(pendingRequests));
      ACCMenuButton adoptionsHistory  = new ACCMenuButton("Adoptions History");
      adoptionsHistory.setOnAction(e -> adoptionsHistoryPopover(adoptionsHistory));
      ACCMenuButton fostersHistory  = new ACCMenuButton("Fosters History");
      fostersHistory.setOnAction(e -> fostersHistoryPopover(fostersHistory));
      ACCMenuButton donationsHistory = new ACCMenuButton("Donation History");
      donationsHistory.setOnAction(e-> donationsHistoryPopover(donationsHistory));
      ACCMenuButton sponsorships = new ACCMenuButton("Sponsorships");
      sponsorships.setOnAction(e-> sponsorshipsPopover(sponsorships));

      scene.addItems(pendingRequests, adoptionsHistory, donationsHistory, sponsorships, fostersHistory);


  }

    private void pendingRequestPopover(ACCMenuButton button) {
        TableView<Adoption> table = new TableView<>();

        TableColumn<Adoption, String> userColumn = new TableColumn<>("User");
        userColumn.setCellValueFactory(cellData -> {
            String userName = "";
            if (cellData.getValue().user() != null) {
                userName = cellData.getValue().user().name();
            }
            return new javafx.beans.property.SimpleStringProperty(userName);
        });

        TableColumn<Adoption, String> animalColumn = new TableColumn<>("Animal");
        animalColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().animal().getDisplayName()
                )
        );

        TableColumn<Adoption, String> typeColumn = new TableColumn<>("Type");
        typeColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().type() != null ? cellData.getValue().type().toString() : ""
                )
        );

        TableColumn<Adoption, Void> actionColumn = new TableColumn<>("Actions");
        actionColumn.setCellFactory(param -> new TableCell<>() {
            private final ACCButton cancelButton = new ACCButton("Cancel");

            {
                cancelButton.setOnAction(event -> {
                    Adoption adoption = getTableView().getItems().get(getIndex());
                    handleCancelRequest(adoption, table);
                });

            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(cancelButton);
                }
            }
        });

        table.getColumns().addAll(userColumn, animalColumn, typeColumn, actionColumn);
        table.setPrefHeight(300);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        table.widthProperty().addListener((obs, oldVal, newVal) -> {
            double width = newVal.doubleValue();
            double colWidth = width / 4; // Dividido por 4 agora
            userColumn.setMinWidth(colWidth);
            animalColumn.setMinWidth(colWidth);
            typeColumn.setMinWidth(colWidth);
            actionColumn.setMinWidth(colWidth);
        });

        ApiResponse response = ApiClient.get("/adoptions/user/pending");
        if (response.isSuccess()) {
            List<Adoption> adoptions = Utility.parseList(response.getBody(), Adoption.class);
            table.setItems(javafx.collections.FXCollections.observableArrayList(adoptions));
        }

        javafx.scene.control.ScrollPane scrollPane = new javafx.scene.control.ScrollPane(table);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setPrefSize(300, 650);

        popover = new ACCPopover(scrollPane, "Pending Requests");
        popover.setPrefSize(1200, 900);
        popover.show(stage);
    }

    private void handleCancelRequest(Adoption adoption, TableView<Adoption> table) {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Cancel Request");
        confirmAlert.setHeaderText("Cancel Adoption Request");
        confirmAlert.setContentText("Are you sure you want to cancel this adoption request?");

        confirmAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                ApiResponse apiResponse = ApiClient.put("/adoptions/user/cancel", adoption.id().toString());

                if (apiResponse.isSuccess()) {
                    table.getItems().remove(adoption);
                    Utility.showAlert(Alert.AlertType.INFORMATION, "Success", "Request cancelled successfully.");
                } else {
                    Utility.showAlert(Alert.AlertType.ERROR, "Error", apiResponse.getBody());
                }
            }
        });
    }

    private void adoptionsHistoryPopover(ACCMenuButton button) {
        TableView<Adoption> table = new TableView<>();

        TableColumn<Adoption, String> userColumn = new TableColumn<>("User");
        userColumn.setCellValueFactory(cellData -> {
            String userName = "";
            if (cellData.getValue().user() != null) {
                userName = cellData.getValue().user().name();
            }
            return new javafx.beans.property.SimpleStringProperty(userName);
        });

        TableColumn<Adoption, String> animalColumn = new TableColumn<>("Animal");
        animalColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().animal().getDisplayName()
                )
        );

        TableColumn<Adoption, String> dateColumn = new TableColumn<>("Adoption Date");
        dateColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().adoptionDate() != null ? cellData.getValue().adoptionDate().toString() : ""
                )
        );

        table.getColumns().addAll(userColumn, animalColumn, dateColumn);
        table.setPrefHeight(300);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        table.widthProperty().addListener((obs, oldVal, newVal) -> {
            double width = newVal.doubleValue();
            double colWidth = width / 3;
            userColumn.setMinWidth(colWidth);
            animalColumn.setMinWidth(colWidth);
            dateColumn.setMinWidth(colWidth);
        });

        ApiResponse response = ApiClient.get("/adoptions/user/adoptions/historic");
        if (response.isSuccess()) {
            List<Adoption> adoptions = Utility.parseList(response.getBody(), Adoption.class);
            table.setItems(javafx.collections.FXCollections.observableArrayList(adoptions));
        }

        javafx.scene.control.ScrollPane scrollPane = new javafx.scene.control.ScrollPane(table);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setPrefSize(300, 650);

        popover = new ACCPopover(scrollPane, "Adoptions History");
        popover.setPrefSize(1200, 900);
        popover.show(stage);
    }

    private void fostersHistoryPopover(ACCMenuButton button) {
        TableView<Adoption> table = new TableView<>();

        TableColumn<Adoption, String> userColumn = new TableColumn<>("User");
        userColumn.setCellValueFactory(cellData -> {
            String userName = "";
            if (cellData.getValue().user() != null) {
                userName = cellData.getValue().user().name();
            }
            return new javafx.beans.property.SimpleStringProperty(userName);
        });

        TableColumn<Adoption, String> animalColumn = new TableColumn<>("Animal");
        animalColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().animal().getDisplayName()
                )
        );

        TableColumn<Adoption, String> dateColumn = new TableColumn<>("Started Fostering");
        dateColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().adoptionDate() != null ? cellData.getValue().adoptionDate().toString() : ""
                )
        );

        table.getColumns().addAll(userColumn, animalColumn, dateColumn);
        table.setPrefHeight(300);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        table.widthProperty().addListener((obs, oldVal, newVal) -> {
            double width = newVal.doubleValue();
            double colWidth = width / 3;
            userColumn.setMinWidth(colWidth);
            animalColumn.setMinWidth(colWidth);
            dateColumn.setMinWidth(colWidth);
        });

        ApiResponse response = ApiClient.get("/adoptions/user/fosters/historic");
        if (response.isSuccess()) {
            List<Adoption> adoptions = Utility.parseList(response.getBody(), Adoption.class);
            table.setItems(javafx.collections.FXCollections.observableArrayList(adoptions));
        }

        javafx.scene.control.ScrollPane scrollPane = new javafx.scene.control.ScrollPane(table);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setPrefSize(300, 650);

        popover = new ACCPopover(scrollPane, "Fosters History");
        popover.setPrefSize(1200, 900);
        popover.show(stage);
    }

    private void donationsHistoryPopover(ACCMenuButton button) {
        TableView<ShelterDonation> table = new TableView<>();

        TableColumn<ShelterDonation, String> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(cellData -> {
            String date = "";
            if (cellData.getValue().donationDate() != null) {
                date = cellData.getValue().donationDate().toString();
            }
            return new javafx.beans.property.SimpleStringProperty(date);
        });

        TableColumn<ShelterDonation, String> amountColumn = new TableColumn<>("Amount");
        amountColumn.setCellValueFactory(cellData -> {
            float amount = cellData.getValue().amount();
            return new javafx.beans.property.SimpleStringProperty(String.format("%.2f", amount));
        });

        TableColumn<ShelterDonation, String> shelterColumn = new TableColumn<>("Shelter");
        shelterColumn.setCellValueFactory(cellData -> {
            String shelterName = "";
            if (cellData.getValue().shelter() != null) {
                shelterName = cellData.getValue().shelter().name();
            }
            return new javafx.beans.property.SimpleStringProperty(shelterName);
        });

        table.getColumns().addAll(dateColumn, amountColumn, shelterColumn);
        table.setPrefHeight(300);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        table.widthProperty().addListener((obs, oldVal, newVal) -> {
            double width = newVal.doubleValue();
            double colWidth = width / 3;
            dateColumn.setMinWidth(colWidth);
            amountColumn.setMinWidth(colWidth);
            shelterColumn.setMinWidth(colWidth);
        });

        ApiResponse response = ApiClient.get("/donations/user/historic");
        if (response.isSuccess()) {
            List<ShelterDonation> donations = Utility.parseList(response.getBody(), ShelterDonation.class);
            table.setItems(javafx.collections.FXCollections.observableArrayList(donations));
        }

        javafx.scene.control.ScrollPane scrollPane = new javafx.scene.control.ScrollPane(table);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setPrefSize(300, 650);

        popover = new ACCPopover(scrollPane, "Donations History");
        popover.setPrefSize(1200, 900);
        popover.show(stage);
    }

    private void sponsorshipsPopover(ACCMenuButton button) {

        TableView<Sponsorship> table = new TableView<>();

        TableColumn<Sponsorship, String> animalCol = new TableColumn<>("Animal");
        animalCol.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().animalName())
        );

        TableColumn<Sponsorship, String> amountCol = new TableColumn<>("Amount");
        amountCol.setCellValueFactory(c ->
                new SimpleStringProperty(String.format("%.2f", c.getValue().amount())));

        TableColumn<Sponsorship, String> startCol = new TableColumn<>("Start Date");
        startCol.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().startDate() != null ? c.getValue().startDate().toString() : ""));

        TableColumn<Sponsorship, String> endCol = new TableColumn<>("End Date");
        endCol.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().endDate() != null ? c.getValue().endDate().toString() : ""));


        TableColumn<Sponsorship, Void> actionCol = new TableColumn<>("Action");
        actionCol.setCellFactory(col -> new TableCell<>() {

            private final ACCButton cancelButton = new ACCButton("Cancel");


            {
                cancelButton.setOnAction(e -> {
                    Sponsorship s = getTableView().getItems().get(getIndex());

                    if (s.sponsorshipId() == null) {
                        Utility.showAlert(Alert.AlertType.ERROR, "Error", "Invalid sponsorship ID");
                        return;
                    }

                    Alert confirm = new Alert(
                            Alert.AlertType.CONFIRMATION,
                            "Cancel sponsorship for " + s.animalName() + "?",
                            ButtonType.YES,
                            ButtonType.NO
                    );

                    confirm.showAndWait().ifPresent(bt -> {
                        if (bt == ButtonType.YES) {

                            ApiResponse res = ApiClient.delete(
                                    "/sponsorships/cancel?sponsorshipId=" + s.sponsorshipId()
                            );

                            if (res.isSuccess()) {
                                getTableView().getItems().remove(s);
                                Utility.showAlert(Alert.AlertType.INFORMATION, "Success", "Sponsorship cancelled!");
                            } else {
                                Utility.showAlert(Alert.AlertType.ERROR, "Error", res.getBody());
                            }
                        }
                    });
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : cancelButton);
            }
        });

        table.getColumns().addAll(animalCol, amountCol, startCol, endCol, actionCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setPrefHeight(300);

        ApiResponse response = ApiClient.get("/sponsorships/user/historic");

        if (response.isSuccess()) {
            List<Sponsorship> sponsorships =
                    Utility.parseList(response.getBody(), Sponsorship.class);
            table.setItems(FXCollections.observableArrayList(sponsorships));
        }

        ScrollPane scrollPane = new ScrollPane(table);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        popover = new ACCPopover(scrollPane, "Sponsorships History");
        popover.setPrefSize(1200, 900);
        popover.show(stage);
    }


}
