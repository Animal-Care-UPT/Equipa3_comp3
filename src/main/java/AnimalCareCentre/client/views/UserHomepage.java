package AnimalCareCentre.client.views;

import java.lang.module.ModuleDescriptor.Requires;

import org.checkerframework.checker.units.qual.A;

import AnimalCareCentre.client.ApiClient;
import AnimalCareCentre.client.ApiResponse;
import AnimalCareCentre.client.Navigator;
import AnimalCareCentre.client.components.*;
import AnimalCareCentre.server.dto.AdoptionDTO;
import AnimalCareCentre.server.model.Adoption;
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

    ACCMenuButton seePendingRequests = new ACCMenuButton("1. My Pending Requests");
    ACCMenuButton seeMyRequests = new ACCMenuButton("2. My Requests");
    ACCMenuButton seeMySponsorships = new ACCMenuButton("3. My Sponsorships");

    seePendingRequests.setOnAction(e -> {
      seePendingRequests();
    });

    seeMyRequests.setOnAction(e -> {

    });

    seeMySponsorships.setOnAction(e -> {

    });
    scene.addItems(seePendingRequests,seeMyRequests,seeMySponsorships);
  }
  
  public void seePendingRequests (){
    TableView<AdoptionDTO> table = new TableView<>();
    
    TableColumn<AdoptionDTO,String> animalColumn = new TableColumn<>("Animal");
    animalColumn.setCellValueFactory(new PropertyValueFactory<>("animalName"));

    TableColumn<AdoptionDTO,String> animalTypeColumn = new TableColumn<>("AnimalType");
    animalTypeColumn.setCellValueFactory(new PropertyValueFactory<>("animalType"));

    TableColumn<AdoptionDTO,String> nameColumn = new TableColumn<>("Name");
    nameColumn.setCellValueFactory(new PropertyValueFactory<>("userName"));

    table.getColumns().addAll(animalColumn,animalTypeColumn,nameColumn);
    table.setPrefHeight(300);
    table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    
    ApiResponse requestResponse = ApiClient.get("/adoptions/user/pending");
    if (requestResponse.isSuccess()){
      List <Adoption> requests = parseList(requestResponse.getBody(), Adoption.class);
      table.setItems(FXCollections.observableArrayList(requests));
    }
    ACCBigPopover popover = new ACCBigPopover(table, "Pending Requests");
    popover.setPrefSize(500, 400);
    popover.show(seePendingRequests);
    
  }
}
