package sg.edu.nus.se.its.errorlocalizer;

import java.util.Collections;
import java.util.List;
import sg.edu.nus.se.its.model.Input;
import sg.edu.nus.se.its.model.Variable;

/**
 * Internal class to represent one specific error location and its erroneous variables in the
 * submission program.
 */
public class ErrorLocation {

  /**
   * Default: VariableValueMismatch, i.e., there is a list of variables that denotes the variables
   * in the submitted program, for which the values were not matching.
   * 
   * <p>TraceEntryMismatch, i.e., there was a mismatch between blocks, i.e., there is a trace entry 
   * for the reference program, but no remaining matching trace entry in the submitted program.
   *
   * <p>UnmatchedException, i.e., the error localizer throws an exception, either for function name
   * mismatch, or a general structural mismatch.
   */
  public enum ErrorType {
    VariableValueMismatch, TraceEntryMismatch, UnmatchedException
  }

  private ErrorType errorType;
  private UnmatchedException dueToUnmatchedException = null;
  private int locationInReference = -1;
  private int locationInSubmission = -1;
  private List<Variable> erroneousVariablesInSubmission = Collections.emptyList();
  private Input triggeringInput;

  /**
   * Error location where values of at least on variable was mismatching at the specific location.
   *
   * @param locationInReference - int
   * @param locationInSubmission - int
   * @param erroneousVariablesInSubmission - list of variables
   */
  public ErrorLocation(int locationInReference, int locationInSubmission,
      List<Variable> erroneousVariablesInSubmission) {
    this.errorType = ErrorType.VariableValueMismatch;
    this.locationInReference = locationInReference;
    this.locationInSubmission = locationInSubmission;
    this.erroneousVariablesInSubmission = erroneousVariablesInSubmission;
  }

  /**
   * ErrorLocation where there was no unique location left in the submission program to explore,
   * hence, there is a mismatch between the traces.
   *
   * @param locationInReference - int
   * @param locationInSubmission - int
   */
  public ErrorLocation(int locationInReference, int locationInSubmission) {
    this.errorType = ErrorType.TraceEntryMismatch;
    this.locationInReference = locationInReference;
    this.locationInSubmission = locationInSubmission;
  }

  /**
   * Error location where there was a mismatching of locations or functions between the traces.
   *
   * @param locationInReference - int
   * @param locationInSubmission - int
   * @param exception - UnmatchedException
   */
  public ErrorLocation(int locationInReference, Integer locationInSubmission,
      UnmatchedException exception) {
    this.errorType = ErrorType.UnmatchedException;
    this.locationInReference = locationInReference;
    this.locationInSubmission = locationInSubmission;
    this.dueToUnmatchedException = exception;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();

    sb.append("errorType=" + errorType + "\n");

    sb.append("dueToUnmatchedException=");
    if (dueToUnmatchedException == null) {
      sb.append("null");
    } else {
      sb.append(dueToUnmatchedException.getMessage());
    }
    sb.append("\n");

    sb.append("locationInReference=" + locationInReference + "\n");
    sb.append("locationInSubmission=" + locationInSubmission + "\n");

    sb.append("erroneousVariablesInSubmission=" + erroneousVariablesInSubmission);

    return sb.toString();
  }

  @Override
  public boolean equals(Object o) {
    if (! (o instanceof ErrorLocation)) {
      return false;
    }
    ErrorLocation obj = (ErrorLocation) o;
    return errorType.equals(obj.errorType)
        && dueToUnmatchedException.equals(obj.dueToUnmatchedException)
        && locationInReference == obj.locationInReference
        && locationInSubmission == obj.locationInSubmission
        && erroneousVariablesInSubmission.equals(obj.erroneousVariablesInSubmission)
        && triggeringInput.equals(obj.triggeringInput);
  }

  public Input getTriggeringInput() {
    return triggeringInput;
  }

  public void setTriggeringInput(Input triggeringInput) {
    this.triggeringInput = triggeringInput;
  }

  public ErrorType getErrorType() {
    return errorType;
  }

  public UnmatchedException getDueToUnmatchedException() {
    return dueToUnmatchedException;
  }

  public int getLocationInReference() {
    return locationInReference;
  }

  public int getLocationInSubmission() {
    return locationInSubmission;
  }

  public List<Variable> getErroneousVariablesInSubmission() {
    return erroneousVariablesInSubmission;
  }

}
