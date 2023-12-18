package sg.edu.nus.se.its.errorlocalizer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import sg.edu.nus.se.its.errorlocalizer.dependencytree.DependencyNode;
import sg.edu.nus.se.its.errorlocalizer.utils.ErrorLocationUtil;
import sg.edu.nus.se.its.model.Input;
import sg.edu.nus.se.its.model.Variable;

/**
 * Represents a class editing the resulting error localization.
 * It acts as a wrapper over the error localisation and is constantly mutated as error locations
 * are added
 */
public class ResultEditor {
  private ErrorLocalisation errorLocalisation;
  private List<Variable> erroneousVariablesInSubmission;
  private boolean returnLineNumber;

  /**
   * Creates a ResultEditor object.
   */
  public ResultEditor() {
    this.errorLocalisation = new ErrorLocalisation();
    this.erroneousVariablesInSubmission = new ArrayList<>();
    this.returnLineNumber = false;
  }

  /**
   * Returns the result of error localisation.
   *
   * @return error localisation results
   */
  public ErrorLocalisation getResult() {
    return this.errorLocalisation;
  }

  /**
   * Returns the flag to indicate if line number or loc number should be returned.
   *
   * @return returnLineNumber flag
   */
  public boolean isReturnLineNumber() {
    return this.returnLineNumber;
  }

  /**
   * Sets flag to indicate if line number or loc number should be returned.
   *
   * @param bool the value to set
   */
  public void setReturnLineNumber(boolean bool) {
    this.returnLineNumber = bool;
  }

  /**
   * Adds an error location with error type variable value mismatch.
   *
   * @param programInput The given input
   * @param refNode The dependency node from reference program
   * @param subNode The dependency node from submission program
   * @param refFuncName The function name of error location
   * @param varMapping The variable mapping
   */
  public void addVariableValueMismatch(Input programInput,
                                        DependencyNode refNode,
                                        DependencyNode subNode,
                                        String refFuncName,
                                        Map<Variable, Variable> varMapping) {

    int referenceLoc = -1;
    int submissionLoc = -1;

    if (returnLineNumber) {
      referenceLoc = refNode.getExpression().getLineNumber();
      submissionLoc = subNode.getExpression().getLineNumber();
    } else {
      referenceLoc = refNode.getLoc();
      submissionLoc = subNode.getLoc();
    }
    Variable errVar = subNode.getFirstChildVar();

    if (erroneousVariablesInSubmission.size() == 0
        || !erroneousVariablesInSubmission.contains(errVar)) {
      erroneousVariablesInSubmission.add(errVar);
    }

    List<Variable> vars = new ArrayList<>();
    vars.add(errVar);
    ErrorLocation errLoc = new ErrorLocation(referenceLoc,
        submissionLoc, vars);
    errLoc.setTriggeringInput(programInput);

    List<ErrorLocation> existingErrLocs =
        errorLocalisation.getErrorLocations(refFuncName, varMapping);

    boolean isNewError = (existingErrLocs.size() == 0)
        || existingErrLocs.stream()
            .noneMatch(existing -> ErrorLocationUtil.areSameErrorLocation(errLoc, existing));

    if (isNewError) {
      errorLocalisation.addLocation(refFuncName, varMapping, errLoc);
    }
  }

  /**
   * Adds an error location with error type Unmatched Exception.
   *
   * @param programInput The given input
   * @param refLoc The reference loc
   * @param subLoc The submission loc
   * @param msg The message
   * @param functionName The function name
   * @param varMapping The variable mapping in this function
   */
  public void addUnmatchedException(Input programInput, int refLoc, int subLoc, String msg,
                                     String functionName,
                                     Map<Variable, Variable> varMapping) {

    ErrorLocation errLoc = new ErrorLocation(refLoc, subLoc, new UnmatchedException(msg));
    errLoc.setTriggeringInput(programInput);

    errorLocalisation.addLocation(functionName, varMapping, errLoc);
  }
}
