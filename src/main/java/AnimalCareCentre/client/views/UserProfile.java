package AnimalCareCentre.client.views;

import AnimalCareCentre.client.ApiClient;
import AnimalCareCentre.client.ApiResponse;
import AnimalCareCentre.client.Navigator;
import AnimalCareCentre.client.components.ACCMenuButton;
import AnimalCareCentre.client.components.ACCPopover;
import AnimalCareCentre.client.components.ACCScene;
import AnimalCareCentre.client.components.ACCVBox;
import AnimalCareCentre.client.enums.AdoptionType;
import AnimalCareCentre.client.records.ShelterAnimal;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
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

        String requestBody = String.format(
                "{\"animalId\": %d, \"type\": \"%s\"}",
                animal.id(),
                adoptionType
        );

        ApiResponse response = ApiClient.post("/adoptions/request", requestBody);

        if(response.isSuccess()){
            showSuccessAlert(adoptionType);
        }
        else{
            showErrorAlert(response.getBody());
        }

    }


    private void sponsorshipPopover(ACCMenuButton button){
        ACCVBox content = new ACCVBox();
        content.setSpacing(10);

        Label titleLabel = new Label("Become a sponsor for " + animal.getDisplayName());
        titleLabel.setStyle("-fx-font-weight: bold;");

        ApiResponse  response = ApiClient.get("/sponsorships/create" + animal.id());

        Label label = new Label("Are you sure you want to become a sponsor for " + animal.getDisplayName());
        titleLabel.setStyle("-fx-font-weight: bold;");

        Label amountLabel = new Label("Enter sponsorship amount (€):");
        TextField amountField = new TextField();
        amountField.setPromptText("0.00");

        Button submitButton = new Button("Submit");
        Button cancelButton = new Button("Cancel");


        submitButton.setOnAction(e -> {
            String input = amountField.getText();

            try{
                float amount = Float.parseFloat(input);

                if (amount <= 0) {
                    showErrorAlert("Invalid Amount");
                    return;
                }

                popover.hide();
                createSponsorship(amount);
            }
            catch(NumberFormatException ex){
                showErrorAlert("Invalid Amount");
            }
        });

        cancelButton.setOnAction(e -> {popover.hide();});
        content.addItems(titleLabel, amountLabel, amountField, submitButton, cancelButton);

        popover = new ACCPopover(content, "Confirm ");
        popover.show(button);
    }

    private  void createSponsorship(float amount){
        String url = String.format("/sponsorships/create?animalId=%d&amount=%.2f", animal.id(), amount);

        ApiResponse response = ApiClient.post(url, "");

        if(response.isSuccess()){
            showSuccessAlert("Thank you for becoming a sponsor for " + animal.getDisplayName());
        }
        else{
            showErrorAlert(response.getBody());
        }

    }

    private void showSuccessAlert(String type) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(type + " Request Submitted");
        alert.setContentText("Your " + type.toLowerCase() + " request has been submitted successfully!\nThe shelter will review it soon.");
        alert.showAndWait();
    }

    private void showErrorAlert(String errorMessage){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Request Failed");
        alert.setContentText(errorMessage != null ? errorMessage : "An error occurred while processing your request.");
        alert.showAndWait();
    }
}
