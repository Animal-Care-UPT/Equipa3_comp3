package AnimalCareCentre.client;

public class ApiResponse {

  private boolean success;
  private String body;
  private int statusCode;

  public ApiResponse(boolean success, String body, int statusCode) {
    this.success = success;
    this.body = body;
    this.statusCode = statusCode;
  }

  public boolean isSuccess() {
    return success;
  }

  public String getBody() {
    return body;
  }

  public int getStatusCode() {
    return statusCode;
  }
}
