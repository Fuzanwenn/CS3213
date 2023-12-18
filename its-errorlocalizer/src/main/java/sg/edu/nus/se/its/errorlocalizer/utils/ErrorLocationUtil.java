package sg.edu.nus.se.its.errorlocalizer.utils;

import static sg.edu.nus.se.its.errorlocalizer.ErrorLocation.ErrorType.TraceEntryMismatch;
import static sg.edu.nus.se.its.errorlocalizer.ErrorLocation.ErrorType.UnmatchedException;
import static sg.edu.nus.se.its.errorlocalizer.ErrorLocation.ErrorType.VariableValueMismatch;

import sg.edu.nus.se.its.errorlocalizer.ErrorLocation;
import sg.edu.nus.se.its.model.Input;

/**
 * The class represents the methods used to process
 * the error location objects.
 */
public class ErrorLocationUtil {
  private static final String MSG_UNSUPPORTED_ERROR_TYPE = "Unsupported error type.";

  /**
   * Returns if the two given error locations are equal.
   * This method can be used to avoid using the equals() method
   * in ErrorLocation class, because it often throws NullPointerException
   * when comparing two locations with different ErrorTypes.
   *
   * @param errorLocation1 The given error location.
   * @param errorLocation2 The given error location.
   * @return if the given two error locations are equal.
   */
  public static boolean areSameErrorLocation(
      ErrorLocation errorLocation1, ErrorLocation errorLocation2) {
    if (errorLocation1 == null && errorLocation2 == null) {
      return true;
    }
    if (errorLocation1 == null || errorLocation2 == null) {
      return false;
    }
    ErrorLocation.ErrorType errorType1 = errorLocation1.getErrorType();
    ErrorLocation.ErrorType errorType2 = errorLocation2.getErrorType();
    if (!errorType1.equals(errorType2)) {
      return false;
    }
    if (errorType1.equals(VariableValueMismatch)) {
      Input input1 = errorLocation1.getTriggeringInput();
      Input input2 = errorLocation2.getTriggeringInput();
      boolean sameInput = (input1 == null && input2 == null)
          || (input1 != null && input1.equals(input2));
      return errorLocation1.getLocationInReference()
          == errorLocation2.getLocationInReference()
          && errorLocation1.getLocationInSubmission()
          == errorLocation2.getLocationInSubmission()
          && errorLocation1.getErroneousVariablesInSubmission().stream()
          .allMatch(errVar1 -> errorLocation2.getErroneousVariablesInSubmission().stream()
              .anyMatch(errVar2 -> errVar1.getUnprimedName().equals(errVar2.getUnprimedName())))
          //                  && errVar1.getType().equals(errVar2)
          //                  && errVar1.getLineNumber() == errVar2.getLineNumber()))
          && sameInput;
    } else if (errorType1.equals(UnmatchedException)) {
      return errorLocation1.getLocationInReference()
          == errorLocation2.getLocationInReference()
          && errorLocation1.getLocationInSubmission()
          == errorLocation2.getLocationInSubmission()
          && errorLocation1.getErroneousVariablesInSubmission()
          .equals(errorLocation2.getErroneousVariablesInSubmission())
          && errorLocation1.getDueToUnmatchedException()
          .equals(errorLocation2.getDueToUnmatchedException());
    } else if (errorType1.equals(TraceEntryMismatch)) {
      return errorLocation1.getLocationInReference()
          == errorLocation2.getLocationInReference()
          && errorLocation1.getLocationInSubmission()
          == errorLocation2.getLocationInSubmission();
    } else {
      throw new RuntimeException(MSG_UNSUPPORTED_ERROR_TYPE);
    }
  }
}
