package sg.edu.nus.se.its.alignment;

/**
 * Custom exception for exceptions during structural alignment.
 */
public class AlignmentException extends Exception {

  private static final long serialVersionUID = 1L;

  public AlignmentException(String message) {
    super(message);
  }

  public static final AlignmentException FUNC_SIZES_NOT_EQUAL =
      new AlignmentException("Alignment failed, function sizes not equal");

  public static final AlignmentException BASIC_BLOCK_SIZES_NOT_EQUAL =
      new AlignmentException("Alignment failed. basic block sizes not equal");

  public static final AlignmentException SAME_FUNC_NOT_FOUND =
      new AlignmentException("Alignment failed, same function not found");

  public static final AlignmentException FAILED = new AlignmentException("Alignment failed");
}