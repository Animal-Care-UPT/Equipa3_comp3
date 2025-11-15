package AnimalCareCentre.server.util;

public class ACCPasswordEncryption {

  private static final String ABC = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890!#@_$-";
  private static final String KEY = "lAsAgNa";

  public static String encrypt(String text) {
    StringBuilder result = new StringBuilder(text.length());
    int k = 0;
    int sum;

    for (int i = 0; i < text.length(); i++) {
      int textValue = ABC.indexOf(text.charAt(i));
      int keyValue = ABC.indexOf(KEY.charAt(k));
      sum = textValue + keyValue + 1;

      if (sum >= ABC.length()) {
        sum -= ABC.length();
      }

      k++;
      if (k >= KEY.length()) {
        k = 0;
      }

      result.append(ABC.charAt(sum));
    }

    return result.toString();
  }

  public static String decrypt(String text) {
    StringBuilder result = new StringBuilder();
    int k = 0;

    for (int i = 0; i < text.length(); i++) {
      int textValue = ABC.indexOf(text.charAt(i));
      int keyValue = ABC.indexOf(KEY.charAt(k));
      int sum = textValue - keyValue - 1;

      if (sum < 0) {
        sum += ABC.length();
      }

      result.append(ABC.charAt(sum));

      k++;
      if (k >= KEY.length()) {
        k = 0;
      }
    }

    return result.toString();
  }
}
