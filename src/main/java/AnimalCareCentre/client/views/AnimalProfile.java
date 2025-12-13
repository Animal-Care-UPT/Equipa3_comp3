package AnimalCareCentre.client.views;

import AnimalCareCentre.client.ApiClient;
import AnimalCareCentre.client.ApiResponse;
import AnimalCareCentre.client.Navigator;
import AnimalCareCentre.client.Utility;
import AnimalCareCentre.client.components.ACCMenuButton;
import AnimalCareCentre.client.components.ACCPopover;
import AnimalCareCentre.client.components.ACCScene;
import AnimalCareCentre.client.components.ACCVBox;
import AnimalCareCentre.client.records.Adoption;
import AnimalCareCentre.client.records.ShelterAnimal;
import AnimalCareCentre.client.records.Sponsorship;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import java.util.List;


public class AnimalProfile {

    private Navigator nav;
    private Stage stage;
    private ShelterAnimal animal;
    ACCPopover popover;

    public AnimalProfile(Navigator nav, Stage stage, ShelterAnimal animal) {
            this.nav = nav;
            this.stage = stage;
            this.animal = animal;
            show();
        }

    private void show() {
          ACCVBox root = new ACCVBox();
          ACCScene scene = new ACCScene(stage, root);
          new NavBar(nav.getLoggedRole(), nav, scene);

          Label animalProfile = new Label(animal.toString());

          ACCMenuButton sponsorshipsButton = new ACCMenuButton("Sponsorships");

          sponsorshipsButton.setOnAction(e ->{
              sponsorshipPopover(sponsorshipsButton);
          });

          ACCMenuButton historyButton = new ACCMenuButton("History");
          historyButton.setOnAction(e ->{
              adoptionHistory(historyButton);
          });

        root.addItems(animalProfile, sponsorshipsButton, historyButton);

    }

    public void sponsorshipPopover(Button button){
        ApiResponse response = ApiClient.get("/sponsorships/animal/" + animal.id());

        ACCVBox content = new ACCVBox();
        content.setSpacing(8);

        if(!response.isSuccess()){
            content.addItems(new Label("Error loading sponsorships!"));
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

    public void adoptionHistory (Button button){
        ApiResponse response = ApiClient.get("/adoptions/animal/" + animal.id());

        ACCVBox content = new ACCVBox();
        content.setSpacing(8);

        if(!response.isSuccess()){
            content.addItems(new Label("Error loading adoptions history!"));
        }
        else{
            List<Adoption> adoptions = Utility.parseList(response.getBody(), Adoption.class);
            if(adoptions.isEmpty()){
                content.addItems(new Label("No record of adoptions or fosterings"));
            }
            else{
                for(Adoption adoption : adoptions){
                    Label label = new Label(adoption.animal().getDisplayName() + "\n " + adoption.type() + "\n at " + adoption.adoptionDate());
                    label.setStyle("-fx-padding: 5; -fx-background-color: #f0f0f0; -fx-background-radius: 5;");
                    content.addItems(label);
                }
            }
        }

        ScrollPane scroll = new ScrollPane(content);
        scroll.setFitToWidth(true);
        scroll.setPrefHeight(200);
        popover = new ACCPopover(scroll, "Adoptions/Fosters History");
        popover.show(button);
    }







}
