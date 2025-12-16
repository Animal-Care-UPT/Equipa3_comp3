package AnimalCareCentre.client.views;

import java.lang.module.ModuleDescriptor.Requires;
import java.util.List;

import org.checkerframework.checker.units.qual.A;

import AnimalCareCentre.client.ApiClient;
import AnimalCareCentre.client.ApiResponse;
import AnimalCareCentre.client.Navigator;
import AnimalCareCentre.client.components.*;
import AnimalCareCentre.client.Utility;
import AnimalCareCentre.server.dto.AdoptionDTO;
import AnimalCareCentre.server.model.Adoption;
import AnimalCareCentre.server.model.Sponsorship;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class UserHomepage {

  private Navigator nav;
  private Stage stage;

  public UserHomepage(Navigator nav, Stage stage) {
    this.nav = nav;
    this.stage = stage;
    show();
  }

  private void show() {
    ACCScene scene = new ACCScene(stage, new ACCVBox());
    new NavBar(nav.getLoggedRole(), nav, scene);

    ACCMenuButton seePendingRequests = new ACCMenuButton("My Pending Requests");
    ACCMenuButton seeMyRequests = new ACCMenuButton("My Requests");
    ACCMenuButton seeMySponsorships = new ACCMenuButton("My Sponsorships");

    seePendingRequests.setOnAction(e -> {
      seePendingRequests(seePendingRequests);
    });

    seeMyRequests.setOnAction(e -> {
      seeMyRequests(seeMyRequests);

    });

    seeMySponsorships.setOnAction(e -> {
      seeMySponsorships(seeMySponsorships);
    });
    scene.addItems(seePendingRequests,seeMyRequests,seeMySponsorships);
  }
  
  public void seePendingRequests (ACCMenuButton button){
    TableView<Adoption> table = new TableView<>();
    
    table.setPrefSize(950, 650);

    TableColumn<Adoption,String> animalColumn = new TableColumn<>("Animal");
    animalColumn.setCellValueFactory(new PropertyValueFactory<>("animalName"));

    TableColumn<Adoption,String> animalTypeColumn = new TableColumn<>("AnimalType");
    animalTypeColumn.setCellValueFactory(new PropertyValueFactory<>("animalType"));

    TableColumn<Adoption,String> nameColumn = new TableColumn<>("Name");
    nameColumn.setCellValueFactory(new PropertyValueFactory<>("userName"));

    table.getColumns().addAll(animalColumn,animalTypeColumn,nameColumn);
    table.setPrefHeight(300);
    table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    
    table.widthProperty().addListener((obs, oldVal, newVal) -> {
    double width = newVal.doubleValue();
    double colWidth = width / 3;
    animalColumn.setMinWidth(colWidth);
    animalTypeColumn.setMinWidth(colWidth);
    nameColumn.setMinWidth(colWidth);
    });

    ApiResponse requestResponse = ApiClient.get("/adoptions/user/pending");
    if (requestResponse.isSuccess()){
      List <Adoption> requests = Utility.parseList(requestResponse.getBody(), Adoption.class);
      table.setItems(FXCollections.observableArrayList(requests));
    }

    ACCBigPopover popover = new ACCBigPopover(table, "Pending Requests");
    popover.setPrefSize(1000, 800);
    popover.show(button);
    
  }

  public void seeMyRequests(ACCMenuButton button){
    TableView<Adoption> table = new TableView<>();    
    
    table.setPrefSize(950, 650);

    TableColumn<Adoption,String> animalColumn = new TableColumn<>("Animal");
    animalColumn.setCellValueFactory(new PropertyValueFactory<>("animalName"));

    TableColumn<Adoption,String> animalTypeColumn = new TableColumn<>("AnimalType");
    animalTypeColumn.setCellValueFactory(new PropertyValueFactory<>("animalType"));

    TableColumn<Adoption,String> nameColumn = new TableColumn<>("Name");
    nameColumn.setCellValueFactory(new PropertyValueFactory<>("userName"));

    table.getColumns().addAll(animalColumn,animalTypeColumn,nameColumn);
    table.setPrefHeight(300);
    table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    
    table.widthProperty().addListener((obs, oldVal, newVal) -> {
    double width = newVal.doubleValue();
    double colWidth = width / 3;
    animalColumn.setMinWidth(colWidth);
    animalTypeColumn.setMinWidth(colWidth);
    nameColumn.setMinWidth(colWidth);
    });

    ApiResponse acceptedResponse = ApiClient.get("/adoptions/user/accepted");
    if (acceptedResponse.isSuccess()){
      List <Adoption> requests = Utility.parseList(acceptedResponse.getBody(), Adoption.class);
      table.setItems(FXCollections.observableArrayList(requests));
    }

    ACCBigPopover popover = new ACCBigPopover(table, "Accepted Requests");
    popover.setPrefSize(1000, 800);
    popover.show(button);
    
  }

  public void seeMySponsorships(ACCMenuButton button){
    TableView<Sponsorship> table = new TableView<>();
    
    table.setPrefSize(950, 650);

    TableColumn<Sponsorship,String> userIDColumn = new TableColumn<>("User ID");
    userIDColumn.setCellValueFactory(new PropertyValueFactory<>("userID"));

    TableColumn<Sponsorship,String> userColumn = new TableColumn<>("User");
    userColumn.setCellValueFactory(new PropertyValueFactory<>("user"));

    TableColumn<Sponsorship,String> animalIDColumn = new TableColumn<>("Animal ID");
    animalIDColumn.setCellValueFactory(new PropertyValueFactory<>("animalID"));

    TableColumn<Sponsorship,String> animalColumn = new TableColumn<>("Animal");
    animalColumn.setCellValueFactory(new PropertyValueFactory<>("animal"));

    TableColumn<Sponsorship,String> amountColumn = new TableColumn<>("Amount");
    amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));

    TableColumn<Sponsorship,String> dateColumn = new TableColumn<>("Start Date");
    dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
    
    table.getColumns().addAll(userIDColumn,userColumn,animalIDColumn,animalColumn,amountColumn,dateColumn);
    table.setPrefHeight(300);
    table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

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

    ApiResponse sponsorResponse = ApiClient.get("/sponsorships/usersponsor");
    if (sponsorResponse.isSuccess()){
      List<Sponsorship> sponsorships = Utility.parseList(sponsorResponse.getBody(),Sponsorship.class);
      table.setItems(FXCollections.observableArrayList(sponsorships));  
    }

    ACCBigPopover popover = new ACCBigPopover(table, "My Sponsorships");
    popover.setPrefSize(1000, 800);
    popover.show(button);


  }
}
