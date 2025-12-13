package AnimalCareCentre.client.views;

import AnimalCareCentre.client.ApiClient;
import AnimalCareCentre.client.ApiResponse;
import AnimalCareCentre.client.Navigator;
import AnimalCareCentre.client.components.ACCMenuButton;
import AnimalCareCentre.client.components.ACCPopover;
import AnimalCareCentre.client.components.ACCScene;
import AnimalCareCentre.client.components.ACCVBox;
import AnimalCareCentre.client.enums.AdoptionType;
import AnimalCareCentre.client.records.Shelter;
import AnimalCareCentre.client.records.ShelterAnimal;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
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
               requestAdoption("ADOPT");
            });
            root.addItems(adoptionButton);
        }
        else if(animal.adoptionType().equals(AdoptionType.FOR_FOSTER)){
            ACCMenuButton fosterButton = new ACCMenuButton("Foster");
            fosterButton.setOnAction(e -> {
                requestAdoption("FOSTER");
            });
        }

    }

    private void requestAdoption(String adoptionType){
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm request");
        confirmAlert.setHeaderText("Are you sure you want to " + adoptionType.toLowerCase() + " this animal?");
        confirmAlert.setContentText("Animal: " + animal.getDisplayName());

        ButtonType result = confirmAlert.showAndWait().orElse(ButtonType.CANCEL);

        if(result == ButtonType.OK){
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
