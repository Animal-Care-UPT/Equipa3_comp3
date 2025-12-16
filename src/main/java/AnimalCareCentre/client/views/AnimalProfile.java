package AnimalCareCentre.client.views;

import AnimalCareCentre.client.ApiClient;
import AnimalCareCentre.client.ApiResponse;
import AnimalCareCentre.client.Navigator;
import AnimalCareCentre.client.Utility;
import AnimalCareCentre.client.components.*;
import AnimalCareCentre.client.enums.AdoptionType;
import AnimalCareCentre.client.records.Adoption;
import AnimalCareCentre.client.records.ShelterAnimal;
import AnimalCareCentre.client.records.Sponsorship;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import java.util.List;


public class AnimalProfile {

    private Navigator nav;
    private Stage stage;
    private ShelterAnimal animal;
    private ACCPopover popover;

    public AnimalProfile(Navigator nav, Stage stage, ShelterAnimal animal) {
            this.nav = nav;
            this.stage = stage;
            this.animal = animal;
            show();
        }

    private void show() {
        ACCScene scene = new ACCScene(stage, new ACCVBox());
        new NavBar(nav.getLoggedRole(), nav, scene);

        ACCHBox mainBox = new ACCHBox();
        mainBox.setAlignment(Pos.CENTER_LEFT);
        mainBox.setSpacing(30);

        ImageView imageView = new ImageView();
        imageView.setFitWidth(400);
        imageView.setFitHeight(400);
        imageView.setPreserveRatio(true);

        final boolean[] isZoomed = {false};

        imageView.setOnMouseEntered(event -> {
            if (!isZoomed[0]) {
                imageView.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 15, 0, 0, 3); -fx-cursor: hand;");
            }
        });

        imageView.setOnMouseExited(event -> {
            if (!isZoomed[0]) {
                imageView.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0, 0, 2);");
            }
        });

        imageView.setOnMouseClicked(event -> {
            if (!isZoomed[0]) {
                imageView.setFitWidth(600);
                imageView.setFitHeight(600);
                imageView.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.6), 20, 0, 0, 5); -fx-cursor: hand;");
                isZoomed[0] = true;
            } else {
                imageView.setFitWidth(400);
                imageView.setFitHeight(400);
                imageView.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0, 0, 2);");
                isZoomed[0] = false;
            }
        });

        String imageUrl = animal.getImagePath();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            ApiResponse response = ApiClient.get(imageUrl);
            Image image = Utility.parseImage(response);
            if (image != null) {
                imageView.setImage(image);
                imageView.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0, 0, 2);");
            }
        }

        ACCVBox rightContainer = new ACCVBox();
        rightContainer.setSpacing(15);

        Label animalProfile = new Label(animal.toString());
        animalProfile.setStyle("-fx-font-size: 16px; -fx-line-spacing: 5px;");

        ACCHBox buttonsBox = new ACCHBox();
        buttonsBox.setAlignment(Pos.CENTER_RIGHT);
        buttonsBox.setSpacing(15);


        if(nav.getLoggedRole().equals("ROLE_ADMIN")){
            ACCMenuButton sponsorshipsButton = new ACCMenuButton("Sponsorships");
            sponsorshipsButton.setPrefWidth(120);
            sponsorshipsButton.setPrefHeight(35);
            sponsorshipsButton.setStyle("-fx-font-size: 12px;");

            ACCMenuButton historyButton = new ACCMenuButton("History");
            historyButton.setPrefWidth(120);
            historyButton.setPrefHeight(35);
            historyButton.setStyle("-fx-font-size: 12px;");

            sponsorshipsButton.setOnAction(e -> sponsorshipPopover(sponsorshipsButton));
            historyButton.setOnAction(e -> adoptionHistory(historyButton));

            buttonsBox.addItems(sponsorshipsButton, historyButton);
        }
        else if(nav.getLoggedRole().equals("ROLE_SHELTER")){
            ACCMenuButton sponsorshipsButton = new ACCMenuButton("Sponsorships");
            sponsorshipsButton.setPrefWidth(120);
            sponsorshipsButton.setPrefHeight(35);
            sponsorshipsButton.setStyle("-fx-font-size: 12px;");

            ACCMenuButton historyButton = new ACCMenuButton("History");
            historyButton.setPrefWidth(120);
            historyButton.setPrefHeight(35);
            historyButton.setStyle("-fx-font-size: 12px;");

            sponsorshipsButton.setOnAction(e -> sponsorshipPopover(sponsorshipsButton));
            historyButton.setOnAction(e -> adoptionHistory(historyButton));

            buttonsBox.addItems(sponsorshipsButton, historyButton);
        }
        else if(nav.getLoggedRole().equals("ROLE_USER")){
            ACCMenuButton sponsorButton = new ACCMenuButton("Sponsor");
            sponsorButton.setMinWidth(80);
            sponsorButton.setPrefWidth(80);
            sponsorButton.setMaxWidth(80);
            sponsorButton.setPrefHeight(35);
            sponsorButton.setStyle("-fx-font-size: 11px;");
            sponsorButton.setOnAction(e -> newSponsorshipPopover(sponsorButton));

            if(animal.adoptionType().equals(AdoptionType.FOR_ADOPTION)){
                ACCMenuButton adoptButton = new ACCMenuButton("Adopt");
                adoptButton.setMinWidth(80);
                adoptButton.setPrefWidth(80);
                adoptButton.setMaxWidth(80);
                adoptButton.setPrefHeight(35);
                adoptButton.setStyle("-fx-font-size: 11px;");
                adoptButton.setOnAction(e -> requestAdoptionPopover(adoptButton, "ADOPTION"));
                buttonsBox.addItems(sponsorButton, adoptButton);
            }
            else if(animal.adoptionType().equals(AdoptionType.FOR_FOSTER)){
                ACCMenuButton fosterButton = new ACCMenuButton("Foster");
                fosterButton.setMinWidth(80);
                fosterButton.setPrefWidth(80);
                fosterButton.setMaxWidth(80);
                fosterButton.setPrefHeight(35);
                fosterButton.setStyle("-fx-font-size: 11px;");
                fosterButton.setOnAction(e -> requestAdoptionPopover(fosterButton, "FOSTER"));
                buttonsBox.addItems(sponsorButton, fosterButton);
            }
            else{
                buttonsBox.addItems(sponsorButton);
            }
        }

        mainBox.addItems(imageView, animalProfile, buttonsBox);
        scene.addItems(mainBox);


    }

    private void sponsorshipPopover(ACCMenuButton button){

        ApiResponse response = ApiClient.get("/sponsorships/animal/" + animal.id());

        ACCVBox content = new ACCVBox();
        content.setSpacing(8);

        if(!response.isSuccess()){
            Utility.showAlert(Alert.AlertType.ERROR, "Failed to load sponsorships", response.getBody());
            return;
        }
        else{
            List<Sponsorship> sponsors = Utility.parseList(response.getBody(), Sponsorship.class);

            if(sponsors.isEmpty()){
                content.addItems(new Label("This animal has no sponsors :( \n Become a sponsor today and help an animal :)"));
            }
            else{
                for(Sponsorship sponsor : sponsors){
                    Label label = new Label( sponsor.userName() + "\nTotal: " + sponsor.amount() + "€\nSince: " + sponsor.startDate());
                    label.setStyle("-fx-padding: 5; -fx-background-color: #f0f0f0; -fx-background-radius: 5;");
                    content.addItems(label);
                }
            }
        }
        ScrollPane scroll = new ScrollPane(content);
        scroll.setFitToWidth(true);
        scroll.setPrefHeight(200);

        popover = new ACCPopover(scroll, "Sponsorships");
        popover.show(button);

    }


    private void adoptionHistory (ACCMenuButton button){

        ApiResponse response = ApiClient.get("/adoptions/animal/" + animal.id());

        ACCVBox content = new ACCVBox();
        content.setSpacing(12);

        if(!response.isSuccess()){
            Utility.showAlert(Alert.AlertType.ERROR, "Failed to load adoptions", response.getBody());
            return;
        }
            List<Adoption> adoptions = Utility.parseList(response.getBody(), Adoption.class);
            if(adoptions==null ||adoptions.isEmpty()){
                content.addItems(new Label("No record of adoptions or fosterings"));
            }
            else{
                for(Adoption adoption : adoptions){
                    Label label = new Label(adoption.animal().getDisplayName() + "\n " + adoption.type() + "\n at " + adoption.adoptionDate());
                    label.setStyle("-fx-padding: 5; -fx-background-color: #f0f0f0; -fx-background-radius: 5;");
                    content.addItems(label);
                }
            }


        ScrollPane scroll = new ScrollPane(content);
        scroll.setFitToWidth(true);
        scroll.setPrefHeight(200);
        popover = new ACCPopover(scroll, "Adoptions/Fosters History");
        popover.show(button);
    }

    private void requestAdoptionPopover(ACCMenuButton button, String adoptionType){
        ACCVBox content = new ACCVBox();
        content.setSpacing(10);

        Label confirmLabel = new Label("Do you confirm your decision? Animals are a great responsibility");
        confirmLabel.setStyle("-fx-font-size: 12px;");

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

        String url = "/adoptions/request?animalId=" + animal.id() + "&type=" + adoptionType;

        ApiResponse response = ApiClient.post(url, "");

        if(response.isSuccess()){
            String message = "Your request has been submitted successfully!\nThe shelter will review it soon.";
            Utility.showAlert(Alert.AlertType.INFORMATION, adoptionType + " Request Submitted", message);
        }
        else{
            String errorMessage = response.getBody() != null ? response.getBody() : "An error occurred while processing your request.";
            Utility.showAlert(Alert.AlertType.ERROR, "Request Failed", errorMessage);
        }

    }


    private void newSponsorshipPopover(ACCMenuButton button){
        ACCVBox content = new ACCVBox();
        content.setSpacing(10);

        Label titleLabel = new Label("Become a sponsor for " + animal.getDisplayName());
        titleLabel.setStyle("-fx-font-weight: bold;");

        Label amountLabel = new Label("Enter sponsorship amount (€):");
        amountLabel.setStyle("-fx-font-size: 12px;");
        ACCTextField amountField = new ACCTextField();
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
        String url = "/sponsorships/create?animalId=" + animal.id() + "&amount=" + amount;

        ApiResponse response = ApiClient.post(url, "");

        if(response.isSuccess()){
            Utility.showAlert(Alert.AlertType.INFORMATION, "Sponsorship", "Thank you for becoming a sponsor");
        }
        else{
            Utility.showAlert(Alert.AlertType.ERROR, "Request Failed", response.getBody());
        }

    }



}
