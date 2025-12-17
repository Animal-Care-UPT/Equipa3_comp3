package AnimalCareCentre.client.views;

import AnimalCareCentre.client.ApiClient;
import AnimalCareCentre.client.ApiResponse;
import AnimalCareCentre.client.Navigator;
import AnimalCareCentre.client.components.ACCMenuButton;
import AnimalCareCentre.client.components.ACCScene;
import AnimalCareCentre.client.components.ACCVBox;
import AnimalCareCentre.server.model.LostAnimal;
import javafx.stage.Stage;

import java.util.List;

import static AnimalCareCentre.client.Utility.parseList;

public class LostAndFoundMyPostings {

    private Navigator nav;
    private Stage stage;

    public LostAndFoundMyPostings(Navigator nav, Stage stage) {
        this.nav = nav;
        this.stage = stage;
        show();
    }

    private void show() {
        ACCScene scene = new ACCScene(stage, new ACCVBox());
        new NavBar(nav.getLoggedRole(), nav, scene);




       // scene.addItems(register,homepage,myPosting);
    }
    private List<LostAnimal> myPostings() {

        ApiResponse response = ApiClient.get("/lostandfound/showanimalsbyaccount");
        if (response.isSuccess()) {
            List<LostAnimal> animals = parseList(response.getBody(), LostAnimal.class);
            return animals;
        }
        return null;
    }

}
