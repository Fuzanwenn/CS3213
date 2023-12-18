package sg.edu.nus.se.its.errorlocalizer;

/**
 * Custom exception to track matching errors.
 */
public class UnmatchedException extends Exception {

  private static final long serialVersionUID = 6609854096461951020L;

  public UnmatchedException(String message) {
    super(message);
  }
}
