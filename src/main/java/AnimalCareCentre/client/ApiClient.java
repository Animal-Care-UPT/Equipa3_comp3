package AnimalCareCentre.client;

import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

public class ApiClient {

  private static final String BASE_URL = "http://localhost:8080";
  private static final RestTemplate rest = new RestTemplate();
  private static String sessionCookie = null;

  /**
   * Sends a POST request
   *
   */
  public static ApiResponse post(String endpoint, String json) {
    try {

      HttpHeaders headers = createHeadersWithCookie();

      HttpEntity<String> request = new HttpEntity<>(json, headers);

      ResponseEntity<String> response = rest.postForEntity(BASE_URL + endpoint, request, String.class);

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

      HttpHeaders headers = createHeadersWithCookie();
      HttpEntity<String> request = new HttpEntity<>(headers);
      ResponseEntity<String> response = rest.exchange(BASE_URL + endpoint, HttpMethod.GET, request, String.class);

      return new ApiResponse(true, response.getBody(), response.getStatusCode().value());

    } catch (HttpClientErrorException | HttpServerErrorException e) {
      return new ApiResponse(false, e.getResponseBodyAsString(), e.getStatusCode().value());
    }
  }

  /**
   * Sends a PUT request
   *
   */
  public static ApiResponse put(String endpoint, String json) {
    try {

      HttpHeaders headers = createHeadersWithCookie();
      headers.setContentType(MediaType.APPLICATION_JSON);
      HttpEntity<String> request = new HttpEntity<>(json, headers);
      ResponseEntity<String> response = rest.exchange(BASE_URL + endpoint, HttpMethod.PUT, request, String.class);

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

      HttpHeaders headers = createHeadersWithCookie();
      headers.setContentType(MediaType.APPLICATION_JSON);
      HttpEntity<String> request = new HttpEntity<>(headers);
      ResponseEntity<String> response = rest.exchange(BASE_URL + endpoint, HttpMethod.DELETE, request, String.class);
      return new ApiResponse(true, response.getBody(), response.getStatusCode().value());

    } catch (HttpClientErrorException | HttpServerErrorException e) {
      return new ApiResponse(false, e.getResponseBodyAsString(), e.getStatusCode().value());
    }
  }

  /**
   * Sends a POST request for login and captures the session cookie
   */
  public static ApiResponse login(String endpoint, String json) {
    try {

      HttpHeaders headers = createHeadersWithCookie();
      headers.setContentType(MediaType.APPLICATION_JSON);
      HttpEntity<String> request = new HttpEntity<>(json, headers);

      ResponseEntity<String> response = rest.postForEntity(BASE_URL + endpoint, request, String.class);

      sessionCookie = response.getHeaders().getFirst(HttpHeaders.SET_COOKIE);

      return new ApiResponse(true, response.getBody(), response.getStatusCode().value());
    } catch (HttpClientErrorException | HttpServerErrorException e) {
      return new ApiResponse(false, e.getResponseBodyAsString(), e.getStatusCode().value());
    }
  }

  /**
   * Creates headers with the session cookie if available
   */
  private static HttpHeaders createHeadersWithCookie() {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    if (sessionCookie != null) {
      headers.set(HttpHeaders.COOKIE, sessionCookie);
    }
    return headers;
  }

  /**
   * Clears the session cookie
   */
  public static void clearSession() {
    sessionCookie = null;
  }
}
