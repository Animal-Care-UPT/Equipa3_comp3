package AnimalCareCentre.client.views;

import AnimalCareCentre.client.ApiClient;
import AnimalCareCentre.client.ApiResponse;
import AnimalCareCentre.client.Navigator;
import AnimalCareCentre.client.Utility;
import AnimalCareCentre.client.components.ACCMenuButton;
import AnimalCareCentre.client.components.ACCPopover;
import AnimalCareCentre.client.components.ACCScene;
import AnimalCareCentre.client.components.ACCVBox;
import AnimalCareCentre.client.enums.AdoptionType;
import AnimalCareCentre.client.records.ShelterAnimal;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class UserProfile {

    private Navigator nav;
    private Stage stage;
    private ShelterAnimal animal;
    private ACCPopover popover;

    public UserProfile (Navigator nav, Stage stage, ShelterAnimal animal){
        this.nav = nav;
        this.stage = stage;
        this.animal = animal;
        show();
    }

    public void show(){
        ACCVBox root = new ACCVBox();
        ACCScene scene = new ACCScene(stage, root);
        new NavBar(nav.getLoggedRole(), nav, scene);

        Label animalLabel  = new Label(animal.toString());
        root.addItems(animalLabel);

        if(animal.adoptionType().equals(AdoptionType.FOR_ADOPTION)){
            ACCMenuButton adoptionButton = new ACCMenuButton("Adopt");
            adoptionButton.setOnAction(e -> {
                requestAdoptionPopover(adoptionButton, "ADOPTION");
            });
            root.addItems(adoptionButton);
        }
        else if(animal.adoptionType().equals(AdoptionType.FOR_FOSTER)){
            ACCMenuButton fosterButton = new ACCMenuButton("Foster");
            fosterButton.setOnAction(e -> {
                requestAdoptionPopover(fosterButton, "FOSTER");
            });
            root.addItems(fosterButton);
        }

        ACCMenuButton sponsorButton = new ACCMenuButton("Sponsor");
        sponsorButton.setOnAction(e -> {
          sponsorshipPopover(sponsorButton);
        });
        root.addItems(sponsorButton);


    }

    private void requestAdoptionPopover(ACCMenuButton button, String adoptionType){
        ACCVBox content = new ACCVBox();
        content.setSpacing(10);

        Label confirmLabel = new Label("Do you confirm your decision? Animals are a great responsibility");

        ACCMenuButton confirmButton = new ACCMenuButton("Confirm");
        ACCMenuButton cancelButton = new ACCMenuButton("Cancel");

        confirmButton.setOnAction(e -> {
            popover.hide();
            requestAdoption(adoptionType);
        });

        cancelButton.setOnAction(e -> popover.hide());

        content.addItems(confirmLabel, confirmButton, cancelButton);

        popover = new ACCPopover(content, "Confirm " + adoptionType);
        popover.show(button);
    }

    private void requestAdoption(String adoptionType){

        String requestBody = Utility.jsonString("animalId", animal.id(), "type", adoptionType);

        ApiResponse response = ApiClient.post("/adoptions/request", requestBody);

        if(response.isSuccess()){
            String message = "Your request has been submitted successfully!\nThe shelter will review it soon.";
            Utility.showAlert(Alert.AlertType.INFORMATION, adoptionType + " Request Submitted", message);
        }
        else{
            String errorMessage = response.getBody() != null ? response.getBody() : "An error occurred while processing your request.";
            Utility.showAlert(Alert.AlertType.ERROR, "Request Failed", errorMessage);
        }

    }


    private void sponsorshipPopover(ACCMenuButton button){
        ACCVBox content = new ACCVBox();
        content.setSpacing(10);

        Label titleLabel = new Label("Become a sponsor for " + animal.getDisplayName());
        titleLabel.setStyle("-fx-font-weight: bold;");

        Label amountLabel = new Label("Enter sponsorship amount (€):");
        TextField amountField = new TextField();
        amountField.setPromptText("0.00");

        ACCMenuButton submitButton = new ACCMenuButton("Submit");
        ACCMenuButton cancelButton = new ACCMenuButton("Cancel");


        submitButton.setOnAction(e -> {
            String input = amountField.getText();

            try{
                float amount = Float.parseFloat(input);

                if (amount <= 0) {
                    Utility.showAlert(Alert.AlertType.ERROR, "Invalid amount", "Please enter an amount greater than 0.");
                    return;
                }

                popover.hide();
                createSponsorship(amount);
            }
            catch(NumberFormatException ex){
                Utility.showAlert(Alert.AlertType.ERROR, "Invalid amount", "Please enter a valid number");
            }
        });

        cancelButton.setOnAction(e -> {popover.hide();});
        content.addItems(titleLabel, amountLabel, amountField, submitButton, cancelButton);

        popover = new ACCPopover(content, "Confirm ");
        popover.show(button);
    }

    private  void createSponsorship(float amount){
        String requestBody = Utility.jsonString("animalId", animal.id(), "amount", amount);

        ApiResponse response = ApiClient.post("/sponsorships/create", requestBody);

        if(response.isSuccess()){
            Utility.showAlert(Alert.AlertType.INFORMATION, "Sponsorship", "Thank you for becoming a sponsor");
        }
        else{
            Utility.showAlert(Alert.AlertType.ERROR, "Request Failed", response.getBody());
        }

    }

}
