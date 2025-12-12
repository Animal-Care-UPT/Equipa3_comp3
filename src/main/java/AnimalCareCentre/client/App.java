package AnimalCareCentre.client;

import java.awt.Toolkit;

import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {

  public void start(Stage stage) {
    stage.setTitle("AnimalCareCentre");
    stage.setWidth(Toolkit.getDefaultToolkit().getScreenSize().getWidth());
    stage.setHeight(Toolkit.getDefaultToolkit().getScreenSize().getHeight() - 100);
    stage.show();
    new Navigator(stage);
  }

  public static void main(String[] args) {
    launch(args);
  }
}
