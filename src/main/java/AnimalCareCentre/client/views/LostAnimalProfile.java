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
import AnimalCareCentre.client.records.LostAnimal;
import AnimalCareCentre.client.records.ShelterAnimal;
import AnimalCareCentre.client.records.Sponsorship;
import io.github.makbn.jlmap.listener.event.ClickEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

import java.util.List;


public class LostAnimalProfile {

    private Navigator nav;
    private Stage stage;
    private LostAnimal animal;
    private ACCPopover popover;

    public LostAnimalProfile(Navigator nav, Stage stage, LostAnimal animal) {
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



          scene.addItems(animalProfile);

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





}


