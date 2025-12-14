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
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class UserShelterProfile {

    private Navigator nav;
    private Stage stage;
    private Shelter shelter;
    private ACCPopover popover;

    public UserShelterProfile(Navigator nav, Stage stage, Shelter shelter){
        this.nav = nav;
        this.stage = stage;
        this.shelter = shelter;
        show();
    }

    private void show(){
        ACCVBox root = new ACCVBox();
        ACCScene scene = new ACCScene(stage, root);
        new NavBar(nav.getLoggedRole(), nav, scene);

        Label label = new Label(shelter.toString());

        ACCMenuButton donationsButton = new ACCMenuButton("Donations");
        donationsButton.setOnMouseClicked((event) -> {
            donationPopover(donationsButton);
        });

        root.addItems(label, donationsButton);
    }

    private void donationPopover(ACCMenuButton button) {
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

}
