package AnimalCareCentre.client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

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

}
