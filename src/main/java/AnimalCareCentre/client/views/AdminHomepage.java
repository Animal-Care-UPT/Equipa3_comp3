package AnimalCareCentre.client.views;

import java.util.List;

import AnimalCareCentre.client.ApiClient;
import AnimalCareCentre.client.ApiResponse;
import AnimalCareCentre.client.Navigator;
import AnimalCareCentre.client.Utility;
import AnimalCareCentre.client.components.ACCMenuButton;
import AnimalCareCentre.client.components.ACCPopover;
import AnimalCareCentre.client.components.ACCScene;
import AnimalCareCentre.client.components.ACCVBox;
import AnimalCareCentre.client.enums.Status;
import AnimalCareCentre.server.model.Adoption;
import AnimalCareCentre.server.model.Shelter;
import AnimalCareCentre.server.model.Sponsorship;
import javafx.collections.FXCollections;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
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
    new NavBar(nav.getLoggedRole(), nav, scene);

    ACCMenuButton viewShelterRequests = new ACCMenuButton("Shelter Requests");
    ACCMenuButton viewAllSponsorships = new ACCMenuButton("Sponsorships");
    ACCMenuButton viewAllAdoptions = new ACCMenuButton("Adoptions");
    ACCMenuButton viewAllFosters = new ACCMenuButton("Fosters");

    viewShelterRequests.setOnAction(e -> {
      viewShelterRequests();
    });

    viewAllSponsorships.setOnAction(e -> {
      viewSponsorships();
    });

    viewAllAdoptions.setOnAction(e -> {
      seeAdoptions();
    });

    viewAllFosters.setOnAction(e -> {
      seeFosters();
    });

    scene.addItems(viewShelterRequests,viewAllSponsorships,viewAllAdoptions,viewAllFosters);
  }
  
  public void viewShelterRequests(){
    TableView<Shelter> table = new TableView<>();
    TableColumn<Shelter,String> shelterIDColumn = new TableColumn<>("Shelter ID");
    shelterIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

    TableColumn<Shelter,String> shelterColumn = new TableColumn<>("Shelter Name");
    shelterColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

    TableColumn<Shelter,String> localColumn = new TableColumn<>("Location");
    localColumn.setCellValueFactory(new PropertyValueFactory<>("location"));

    TableColumn<Shelter,String> yearColumn = new TableColumn<>("Foundation Year");
    yearColumn.setCellValueFactory(new PropertyValueFactory<>("foundationYear"));

    TableColumn<Shelter,String> contactColumn = new TableColumn<>("Contact");
    contactColumn.setCellValueFactory(new PropertyValueFactory<>("contact"));
    
    TableColumn<Shelter,Void> buttonColumn = new TableColumn<>("Action");
    
    buttonColumn.setCellFactory(col -> new TableCell<Shelter,Void>(){
      ACCMenuButton button = new ACCMenuButton("Aceitar");
      {
        button.setOnAction(e ->{
          Shelter shelter = getTableRow().getItem();
          String jsonStatus = Utility.jsonString("status",Status.AVAILABLE);
          ApiResponse acptResponse = ApiClient.put("/shelters/status?id="+ shelter.getId(), jsonStatus);
        });
      }

      protected void updateItem(Void item, boolean empty){
        super.updateItem(item,empty);
        if(empty){
          setGraphic(null);
        }
        else{
          setGraphic(button);
        }
      }
    });

    table.getColumns().addAll(shelterIDColumn,shelterColumn,localColumn,yearColumn,contactColumn,buttonColumn);
    table.setPrefHeight(300);
    table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

    table.widthProperty().addListener((obs,oldVal,newVal) -> {
      double width = newVal.doubleValue();
      double colWidth = width/6;
      shelterIDColumn.setMinWidth(colWidth);
      shelterColumn.setMinWidth(colWidth);
      localColumn.setMinWidth(colWidth);
      yearColumn.setMinWidth(colWidth);
      contactColumn.setMinWidth(colWidth);
      buttonColumn.setMinWidth(colWidth);
    });

    ApiResponse response = ApiClient.get("/shelters/pending");
    if(response.isSuccess()){
      List<Shelter> shelters = Utility.parseList(response.getBody(),Shelter.class);
      table.setItems(FXCollections.observableArrayList(shelters));
    }

    ScrollPane scrollPane = new ScrollPane (table);
    scrollPane.setFitToWidth(true);
    scrollPane.setFitToHeight(true);
    scrollPane.setPrefSize(980, 650);

    ACCPopover popover = new ACCPopover(scrollPane, "Pending Shelters");
    popover.setPrefSize(1200, 900);
    popover.show(stage);
  }

  public void viewSponsorships(){
    TableView<Sponsorship> table = new TableView<>();

    TableColumn<Sponsorship,String> userIDColumn = new TableColumn<>("User ID");
    userIDColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));

    TableColumn<Sponsorship,String> userColumn = new TableColumn<>("User");
    userColumn.setCellValueFactory(new PropertyValueFactory<>("userName"));

    TableColumn<Sponsorship,String> animalIDColumn = new TableColumn<>("Animal ID");
    animalIDColumn.setCellValueFactory(new PropertyValueFactory<>("animalId"));

    TableColumn<Sponsorship,String> animalColumn = new TableColumn<>("Animal");
    animalColumn.setCellValueFactory(new PropertyValueFactory<>("animalName"));

    TableColumn<Sponsorship,String> amountColumn = new TableColumn<>("Amount");
    amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));

    TableColumn<Sponsorship,String> dateColumn = new TableColumn<>("Start Date");
    dateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
    
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
    
    ApiResponse response = ApiClient.get("/sponsorships/all");
    if(response.isSuccess()){
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
  
  public void seeAdoptions(){
    TableView<Adoption> table = new TableView<>();

    TableColumn<Adoption,String> userColumn = new TableColumn<>("User");
    userColumn.setCellValueFactory(new PropertyValueFactory<>("user"));

    TableColumn<Adoption,String> shelterColumn = new TableColumn<>("Shelter");
    shelterColumn.setCellValueFactory(new PropertyValueFactory<>("shelter"));

    TableColumn<Adoption,String> animalColumn = new TableColumn<>("Animal");
    animalColumn.setCellValueFactory(new PropertyValueFactory<>("animal"));

    table.getColumns().addAll(userColumn,shelterColumn,animalColumn);
    table.setPrefHeight(300);
    table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

    table.widthProperty().addListener((obs,oldVal,newVal) ->{
    double width = newVal.doubleValue();
    double colWidth = width / 3;
    userColumn.setMinWidth(colWidth);
    shelterColumn.setMinWidth(colWidth);
    animalColumn.setMinWidth(colWidth);
    });

    ApiResponse response = ApiClient.get("/adoptions/all");
    if(response.isSuccess()){
      List<Adoption> adoptions = Utility.parseList(response.getBody(), Adoption.class);
      table.setItems(FXCollections.observableArrayList(adoptions));
    }

    ScrollPane scrollPane = new ScrollPane(table);
    scrollPane.setFitToWidth(true);
    scrollPane.setFitToHeight(true);
    scrollPane.setPrefSize(980,650);

    ACCPopover popover = new ACCPopover(scrollPane, "Adoptions");
    popover.setPrefSize(1200, 900);
    popover.show(stage);
  }

  public void seeFosters(){
   TableView<Adoption> table = new TableView<>();

    TableColumn<Adoption,String> userColumn = new TableColumn<>("User");
    userColumn.setCellValueFactory(new PropertyValueFactory<>("user"));

    TableColumn<Adoption,String> shelterColumn = new TableColumn<>("Shelter");
    shelterColumn.setCellValueFactory(new PropertyValueFactory<>("shelter"));

    TableColumn<Adoption,String> animalColumn = new TableColumn<>("Animal");
    animalColumn.setCellValueFactory(new PropertyValueFactory<>("animal"));

    table.getColumns().addAll(userColumn,shelterColumn,animalColumn);
    table.setPrefHeight(300);
    table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

    table.widthProperty().addListener((obs,oldVal,newVal) ->{
    double width = newVal.doubleValue();
    double colWidth = width / 3;
    userColumn.setMinWidth(colWidth);
    shelterColumn.setMinWidth(colWidth);
    animalColumn.setMinWidth(colWidth);
    });

    ApiResponse response = ApiClient.get("/adoptions/fosters/all");
    if(response.isSuccess()){
      List<Adoption> adoptions = Utility.parseList(response.getBody(), Adoption.class);
      table.setItems(FXCollections.observableArrayList(adoptions));
    }

    ScrollPane scrollPane = new ScrollPane(table);
    scrollPane.setFitToWidth(true);
    scrollPane.setFitToHeight(true);
    scrollPane.setPrefSize(980,650);

    ACCPopover popover = new ACCPopover(scrollPane, "Adoptions");
    popover.setPrefSize(1200, 900);
    popover.show(stage);

  }

}
