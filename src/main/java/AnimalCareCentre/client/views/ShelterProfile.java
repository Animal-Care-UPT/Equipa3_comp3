package AnimalCareCentre.client.views;

import AnimalCareCentre.client.ApiClient;
import AnimalCareCentre.client.ApiResponse;
import AnimalCareCentre.client.Navigator;
import AnimalCareCentre.client.Utility;
import AnimalCareCentre.client.components.*;
import AnimalCareCentre.client.records.Shelter;
import AnimalCareCentre.client.records.ShelterDonation;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.List;

public class ShelterProfile {

    private Navigator nav;
    private Stage stage;
    private Shelter shelter;
    private ACCPopover popover;

    public ShelterProfile(Navigator nav, Stage stage, Shelter shelter){
        this.nav = nav;
        this.stage = stage;
        this.shelter = shelter;
        show();
    }

    private void show(){
        ACCScene scene = new ACCScene(stage, new ACCVBox());
        new NavBar(nav.getLoggedRole(), nav, scene);

        ACCHBox box = new ACCHBox();

        Label label = new Label(shelter.toString());
        box.addItems(label);

        ACCMenuButton donationsButton = new ACCMenuButton("Donate");
        donationsButton.setOnAction((event) -> {
            newDonationPopover(donationsButton);
        });

        ACCMenuButton donationsHistoryButton =  new ACCMenuButton("Donations");

        donationsHistoryButton.setOnAction(e -> {
            donationsPopover(donationsHistoryButton);

        });

        if(nav.getLoggedRole().equals("ROLE_ADMIN")){
            scene.addItems(box, donationsHistoryButton);
        }

        else if(nav.getLoggedRole().equals("ROLE_USER")){
            scene.addItems(box, donationsButton);
        }


    }

    private void newDonationPopover(ACCMenuButton button) {
        ACCVBox content = new ACCVBox();
        content.setSpacing(10);


        Label titleLabel = new Label("Make a donation to " + shelter.name());
        titleLabel.setStyle("-fx-font-weight: bold;");

        Label instructionLabel = new Label("Enter donation amount (€):");

        TextField amountField = new TextField();
        amountField.setPromptText("0.00");

        ACCMenuButton submitButton = new ACCMenuButton("Submit");
        ACCMenuButton cancelButton = new ACCMenuButton("Cancel");

        submitButton.setOnAction(e -> {
            String input = amountField.getText();

            try {
                float amount = Float.parseFloat(input);

                if (amount <= 0) {
                    Utility.showAlert(Alert.AlertType.ERROR, "Invalid amount", "Please enter a valid amount");
                    return;
                }

                popover.hide();
                createDonation(amount);

            } catch (NumberFormatException ex) {
                Utility.showAlert(Alert.AlertType.ERROR, "Invalid input", "Please enter a valid number");
            }
        });

        cancelButton.setOnAction(e -> popover.hide());

        content.addItems(titleLabel, instructionLabel, amountField, submitButton, cancelButton);

        popover = new ACCPopover(content, "Donate to Shelter");
        popover.show(button);
    }

    private void createDonation(float amount) {
        String requestBody = Utility.jsonString("Shelter", shelter.name(), "amount", amount);

        ApiResponse response = ApiClient.post("/donations/create", requestBody);

        if (response.isSuccess()) {
            Utility.showAlert(Alert.AlertType.INFORMATION, "Donation Created", "Successfully created donation");
        } else {
            Utility.showAlert(Alert.AlertType.ERROR, "Donation failed", "An error occurred while processing your request.");
        }
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
