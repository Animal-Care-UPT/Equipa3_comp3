package AnimalCareCentre.client;

import java.awt.Toolkit;

import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {

  private static Navigator nav;

  public void start(Stage stage) {
    nav = new Navigator(stage);
    stage.setTitle("AnimalCareCentre");
    stage.setWidth(Toolkit.getDefaultToolkit().getScreenSize().getWidth());
    stage.setHeight(Toolkit.getDefaultToolkit().getScreenSize().getHeight() - 100);
    stage.show();
    nav.showMainMenu();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
