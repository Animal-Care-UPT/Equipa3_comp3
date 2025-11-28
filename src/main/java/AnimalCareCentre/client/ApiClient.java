package AnimalCareCentre.client;

import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

public class ApiClient {

  private static final String BASE_URL = "http://localhost:8080";
  private static final RestTemplate rest = new RestTemplate();

  /**
   * Sends a POST request
   *
   */
  public static ApiResponse post(String endpoint, String jsonBody) {
    try {

      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON);

      HttpEntity<String> request = new HttpEntity<>(jsonBody, headers);

      ResponseEntity<String> response = rest.postForEntity(
          BASE_URL + endpoint,
          request,
          String.class);

      return new ApiResponse(true, response.getBody(), response.getStatusCode().value());

    } catch (HttpClientErrorException | HttpServerErrorException e) {
      return new ApiResponse(false, e.getResponseBodyAsString(), e.getStatusCode().value());
    }
  }

  /**
   * Sends a GET request
   *
   */
  public static ApiResponse get(String endpoint) {
    try {
      ResponseEntity<String> response = rest.getForEntity(
          BASE_URL + endpoint,
          String.class);

      return new ApiResponse(true, response.getBody(), response.getStatusCode().value());

    } catch (HttpClientErrorException | HttpServerErrorException e) {
      return new ApiResponse(false, e.getResponseBodyAsString(), e.getStatusCode().value());
    }
  }

  /**
   * Sends a PUT request
   *
   */
  public static ApiResponse put(String endpoint, String jsonBody) {
    try {

      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON);
      HttpEntity<String> request = new HttpEntity<>(jsonBody, headers);
      ResponseEntity<String> response = rest.exchange(
          BASE_URL + endpoint,
          HttpMethod.PUT,
          request,
          String.class);

      return new ApiResponse(true, response.getBody(), response.getStatusCode().value());

    } catch (HttpClientErrorException | HttpServerErrorException e) {
      return new ApiResponse(false, e.getResponseBodyAsString(), e.getStatusCode().value());
    }
  }

  /**
   * Sends a DELETE request
   *
   */
  public static ApiResponse delete(String endpoint) {
    try {

      ResponseEntity<String> response = rest.exchange(
          BASE_URL + endpoint,
          HttpMethod.DELETE,
          null,
          String.class);
      return new ApiResponse(true, response.getBody(), response.getStatusCode().value());

    } catch (HttpClientErrorException | HttpServerErrorException e) {
      return new ApiResponse(false, e.getResponseBodyAsString(), e.getStatusCode().value());
    }
  }
}
