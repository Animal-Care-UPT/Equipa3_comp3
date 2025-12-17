
package AnimalCareCentre.client.views;

import AnimalCareCentre.client.ApiClient;
import AnimalCareCentre.client.ApiResponse;
import AnimalCareCentre.client.Navigator;
import AnimalCareCentre.client.Utility;
import AnimalCareCentre.client.components.*;
import AnimalCareCentre.client.records.LostAnimal;
import AnimalCareCentre.client.records.ShelterAnimal;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.scene.control.Label;

import java.io.File;

public class MyPostingView {

    private Navigator nav;
    private Stage stage;
    private LostAnimal animal;

    public MyPostingView(Navigator nav, Stage stage, LostAnimal animal) {
        this.nav = nav;
        this.stage = stage;
        this.animal = animal;
        show();
    }

    private void show() {
        ACCVBox root = new ACCVBox();
        ACCScene scene = new ACCScene(stage, root);
        new NavBar(nav.getLoggedRole(), nav, scene);

        ACCHBox mainBox = new ACCHBox();
        mainBox.setSpacing(0);

        ACCVBox imgContainer = new ACCVBox();
        imgContainer.setMinWidth(420);
        imgContainer.setMinHeight(420);
        ImageView imageView = new ImageView();
        imageView.setFitWidth(400);
        imageView.setFitHeight(400);
        imageView.setPreserveRatio(true);
        imgContainer.addItems(imageView);

        imageView.setOnMouseEntered(event -> {
            imageView.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 15, 0, 0, 3); -fx-cursor: hand;");

            imageView.setFitWidth(420);
            imageView.setFitHeight(420);
        });

        imageView.setOnMouseExited(event -> {
            imageView.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0, 0, 2);");
            imageView.setFitWidth(400);
            imageView.setFitHeight(400);
            imageView.setCursor(Cursor.HAND);
        });

        imageView.setOnMouseClicked(event -> {
            //imgCarousel();
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

        Label animalProfile = new Label(animal.toString());
        animalProfile.setStyle("-fx-font-size: 16px; -fx-line-spacing: 5px;");

        ACCHBox buttonsBox = new ACCHBox();

        ACCMenuButton addImg = new ACCMenuButton("Add Image");
        ACCMenuButton manage = new ACCMenuButton("Remove Posting");
        buttonsBox.addItems(addImg,manage);

        addImg.setOnAction(e -> addImage());
        manage.setOnAction(e -> removePosting());



       mainBox.addItems(imgContainer, animalProfile,buttonsBox);
        scene.addItems(mainBox );
    }

    private void removePosting(){

            ApiResponse response = ApiClient.delete("/lostandfound/delete/"+animal.getId());
            if(!response.isSuccess()){
                Utility.showAlert(AlertType.ERROR,"Unable to remove posting","");
            }
            nav.lostAndFoundMenu();
    }

   /* private void imgCarousel() {
        ApiResponse response = ApiClient.get("/lostandfound/" + animal.id() + "/images");
        if (!response.isSuccess()) {
            Utility.showAlert(Alert.AlertType.ERROR, "Error", response.getBody());
            return;
        }
        List<Image> images = Utility.parseImageList(response);

        ACCCarousel carousel = new ACCCarousel(images);
        carousel.setPadding(new Insets(20));

        popover = new ACCPopover(carousel, animal.name() + " - Images");
        popover.show(stage);
    }*/


    private void addImage() {
        File image = Utility.selectImageFile(stage);
        ApiResponse response = ApiClient.postWithFile("/shelteranimals/" + animal.id() + "/images", image);
        if (response.isSuccess()) {
            Utility.showAlert(Alert.AlertType.INFORMATION, "Success", response.getBody());
        } else {
            Utility.showAlert(Alert.AlertType.ERROR, "Error", response.getBody());
        }

    }
}

