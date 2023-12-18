package sg.edu.nus.se.its.errorlocalizer.checker.exception;

/**
 * Represents an exception when the Configuration.with***() is called under an incorrect state.
 */
public class IllegalTransformationException extends RuntimeException {
  public IllegalTransformationException(String message) {
    super(message);
  }
}
