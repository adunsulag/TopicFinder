/** Testing class that holds method assertions. */
public class Assert {

  /** 
   * Asserts that the passed in condition is true and displays the message if it's not. 
   * @param message The message to display if the condition is false
   * @param condition The boolean condition to evaluate if it's true or not
   */
  public static void assertTrue(String message, boolean condition) {
    if (!condition) {
      System.err.println("Test failed: " + message);
    }
  }
}
