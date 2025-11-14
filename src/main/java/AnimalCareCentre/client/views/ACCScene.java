package AnimalCareCentre.client.views;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * This class defines the main scene that will be used throughout the app.
 *
 */
public class ACCScene extends Scene {
  private ACCVBox mainVbox;
  private ACCVBox content;
  private ACCHBox header;
  private Stage stage;

  /**
   * Constructor for ACCScene. Defines the default settings.
   *
   * @param stage
   * @param vbox
   */
  public ACCScene(Stage stage, ACCVBox vbox) {
    super(wrapInScroll(vbox));
    mainVbox = vbox;
    mainVbox.setFillWidth(true);
    mainVbox.setStyle("-fx-background-color: #FFFAF1;");
    this.stage = stage;
    content = new ACCVBox();
    header = new ACCHBox();
    createPage();
    stage.setScene(this);
  }

  /**
   * This method creates the Main page and is called on the constructor because it
   * is should be used every time there's a new scene of this type.
   */
  private void createPage() {
    ImageView logo = createLogo();
    ACCHBox body = new ACCHBox();
    ImageView left = createLeftBorder();
    ImageView right = createRightBorder();
    ACCHBox footer = new ACCHBox();
    header.setStyle("-fx-background-color: #69462B;");
    header.setMinHeight(60);
    header.setMaxHeight(60);
    header.setSpacing(40);
    Label footerLabel = new Label("Animal Care Centre - UPT");
    footerLabel.setTextFill(Color.WHITE);
    footer.setStyle("-fx-background-color: #69462B;");
    footer.setMinHeight(30);
    footer.setMaxHeight(30);
    footer.addItems(footerLabel);
    VBox.setVgrow(footer, Priority.NEVER);
    content.setMaxWidth(Double.MAX_VALUE);
    content.setMaxHeight(Double.MAX_VALUE);
    HBox.setHgrow(content, Priority.ALWAYS);
    VBox.setVgrow(body, Priority.ALWAYS);
    logo.setPreserveRatio(true);
    logo.fitWidthProperty().bind(stage.widthProperty().multiply(0.6));
    logo.fitHeightProperty().bind(stage.heightProperty().multiply(0.25));
    logo.setSmooth(true);
    logo.setCache(true);
    left.setPreserveRatio(true);
    right.setPreserveRatio(true);
    left.setFitWidth(150);
    right.setFitWidth(150);
    body.setAlignment(javafx.geometry.Pos.CENTER);
    body.setFillHeight(true);
    body.addItems(left, content, right);
    mainVbox.addItems(logo, header, body, footer);
    VBox.setVgrow(body, Priority.ALWAYS);
  }

  /**
   * This method adds items to the navigation bar of the app.
   */
  public void setHeader(String... items) {
    for (String item : items) {
      ACCNavButton navButton = new ACCNavButton(item);
      header.addItems(navButton);
    }
  }

  /**
   * This method adds items to the main body portion of the page.
   */
  public void addItems(Node... items) {
    content.addItems(items);
  }

  /**
   * This method returns the banner logo.
   *
   */
  private ImageView createLogo() {
    Image img = new Image(getClass().getResourceAsStream("/banner.png"));
    return new ImageView(img);
  }

  /**
   * This method returns the left decorative border.
   *
   */
  private ImageView createLeftBorder() {
    Image img = new Image(getClass().getResourceAsStream("/left.png"));
    return new ImageView(img);
  }

  /**
   * This method returns the right decoration border.
   *
   */
  private ImageView createRightBorder() {
    Image img = new Image(getClass().getResourceAsStream("/right.png"));
    return new ImageView(img);
  }

  /**
   * Clears the main content vbox
   */
  public void clearContent() {
    content.clear();
  }

  private static ScrollPane wrapInScroll(VBox vbox) {
    ScrollPane scroll = new ScrollPane(vbox);
    scroll.setFitToWidth(true);
    scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    scroll.viewportBoundsProperty().addListener((obs, oldB, newB) -> {
      vbox.setMinHeight(newB.getHeight());
    });
    return scroll;
  }
}
