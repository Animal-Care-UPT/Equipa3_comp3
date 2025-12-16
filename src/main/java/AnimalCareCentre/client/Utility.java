package AnimalCareCentre.client;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * Class with helper methods
 *
 */
public class Utility {

  private static final ObjectMapper mapper = new ObjectMapper()
      .registerModule(new JavaTimeModule())
      .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

  /**
   * This method shows an alert
   */
  public static void showAlert(AlertType type, String title, String text) {
    Alert alert = new Alert(type);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(text);
    alert.showAndWait();
  }

  /**
   * This method returns a formatted Json string
   *
   * @return
   */
  public static String jsonString(Object... pairs) {
    Map<String, Object> dataMap = new HashMap<>();
    for (int i = 0; i < pairs.length; i += 2) {
      String key = (String) pairs[i];
      Object value = pairs[i + 1];
      dataMap.put(key, value);
    }
    try {
      return mapper.writeValueAsString(dataMap);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Parse JSON string into a single object
   */
  public static <T> T parseResponse(String json, Class<T> cl) {
    if (json == null || json.isEmpty()) {
      return null;
    }
    try {
      return mapper.readValue(json, cl);
    } catch (Exception e) {
      System.out.println("Failed to parse response: " + e.getMessage());
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Parse JSON string into a list of objects
   */
  public static <T> List<T> parseList(String json, Class<T> cl) {
    if (json == null || json.isEmpty()) {
      return null;
    }
    try {
      return mapper.readValue(json,
          mapper.getTypeFactory().constructCollectionType(List.class, cl));
    } catch (Exception e) {
      System.out.println("Failed to parse list: " + e.getMessage());
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Parse image from Base64 response
   */
  public static Image parseImage(ApiResponse response) {
    if (!response.isSuccess() || response.getBody() == null) {
      return null;
    }
    try {
      byte[] imageBytes = java.util.Base64.getDecoder().decode(response.getBody());
      return new Image(new ByteArrayInputStream(imageBytes));
    } catch (Exception e) {
      System.out.println("Failed to parse image: " + e.getMessage());
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Parses a list of images
   */
  public static List<Image> parseImageList(ApiResponse response) {
    if (!response.isSuccess() || response.getBody() == null) {
      return new ArrayList<>();
    }
    try {
      List<String> base64Images = parseList(response.getBody(), String.class);
      List<Image> images = new ArrayList<>();

      for (String base64Image : base64Images) {
        byte[] imageBytes = java.util.Base64.getDecoder().decode(base64Image);
        images.add(new Image(new ByteArrayInputStream(imageBytes)));
      }

      return images;
    } catch (Exception e) {
      System.out.println("Failed to parse image list: " + e.getMessage());
      e.printStackTrace();
      return new ArrayList<>();
    }
  }

  /**
   * Selects an image file
   */
  public static File selectImageFile(Stage stage) {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Select Image");
    fileChooser.getExtensionFilters().add(
        new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.jpeg", "*.png"));

    return fileChooser.showOpenDialog(stage);
  }
}
