package AnimalCareCentre.client.views;
import AnimalCareCentre.client.ApiClient;
import AnimalCareCentre.client.ApiResponse;
import AnimalCareCentre.client.Navigator;
import AnimalCareCentre.client.Utility;
import AnimalCareCentre.client.components.ACCMenuButton;
import AnimalCareCentre.client.components.ACCPopover;
import AnimalCareCentre.client.components.ACCScene;
import AnimalCareCentre.client.components.ACCVBox;
import AnimalCareCentre.client.records.Shelter;
import AnimalCareCentre.server.model.ShelterDonation;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

import java.util.List;


public class AdminShelterProfile {

    private Navigator nav;
    private Stage stage;
    private Shelter shelter;
    private ACCPopover popover;

    public AdminShelterProfile(Navigator nav, Stage stage, Shelter shelter) {
        this.nav = nav;
        this.stage = stage;
        this.shelter = shelter;
        show();
    }

    private void show() {
        ACCVBox root = new ACCVBox();
        ACCScene scene = new ACCScene(stage, root);
        new NavBar(nav.getLoggedRole(), nav, scene);

        Label shelterProfile = new Label(shelter.toString());

        ACCMenuButton donationsButton =  new ACCMenuButton("Donations");

        donationsButton.setOnAction(e -> {
            donationsPopover(donationsButton);

        });

        root.addItems(shelterProfile, donationsButton);

    }

    public void donationsPopover(ACCMenuButton button){
        ApiResponse response = ApiClient.get("/donations/admin/" + shelter.id());


        ACCVBox content = new ACCVBox();
        content.setSpacing(8);

        if(!response.isSuccess()){
            Utility.showAlert(Alert.AlertType.ERROR, "Error loading donations", response.getBody());
            return;
        }
            List<ShelterDonation> donations = Utility.parseList(response.getBody(), ShelterDonation.class);

            if(donations==null || donations.isEmpty()){
                content.addItems(new Label("No donations found!"));
            }
            else{
                for(ShelterDonation donation : donations){
                    Label label = new Label(donation.toString());
                    label.setStyle("-fx-padding: 5; -fx-background-color: #f0f0f0; -fx-background-radius: 5;");
                    content.addItems(label);
                }
            }


        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(200);

        popover = new ACCPopover(scrollPane, "Donations");
        popover.show(button);

    }

}
