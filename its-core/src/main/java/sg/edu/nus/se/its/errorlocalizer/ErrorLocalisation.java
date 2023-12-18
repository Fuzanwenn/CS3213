package sg.edu.nus.se.its.errorlocalizer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import sg.edu.nus.se.its.model.Variable;

/**
 * Class to encapsulate the erroneous locations per function and variable mapping.
 */
public class ErrorLocalisation {

  /*
   * Stores error locations via "function name" (String) and "variable mapping" (Map<Variable,
   * Variable>).
   */
  private Map<String, Map<Map<Variable, Variable>, List<ErrorLocation>>> errorLocations;

  public ErrorLocalisation() {
    this.errorLocations = new HashMap<>();
  }

  public Collection<String> getFunctions() {
    return errorLocations.keySet();
  }

  public List<Map<Variable, Variable>> getVariableMappings(String functionName) {
    return new ArrayList<>(errorLocations.get(functionName).keySet());
  }

  /**
   * Returns the list of error locations for the specific function and the specific variable mapping
   * object.
   *
   * @param functionName - String
   * @param variableMapping - map of Variable objects
   * @return list of error locations or empty list
   */
  public List<ErrorLocation> getErrorLocations(String functionName,
      Map<Variable, Variable> variableMapping) {

    Map<Map<Variable, Variable>, List<ErrorLocation>> q1 = errorLocations.get(functionName);
    if (q1 == null) {
      return Collections.emptyList();
    }

    List<ErrorLocation> q2 = q1.get(variableMapping);
    return q2 == null ? Collections.emptyList() : q2;
  }

  /**
   * Records the error location.
   *
   * @param functionName - String
   * @param variableMapping - variable mapping
   * @param errorLocation - new error location
   */
  public void addLocation(String functionName, Map<Variable, Variable> variableMapping,
      ErrorLocation errorLocation) {

    Map<Map<Variable, Variable>, List<ErrorLocation>> q1 = errorLocations.get(functionName);
    if (q1 == null) {
      q1 = new HashMap<>();
    }

    List<ErrorLocation> q2 = q1.get(variableMapping);
    if (q2 == null) {
      q2 = new ArrayList<>();
    }

    q2.add(errorLocation);

    q1.put(variableMapping, q2);
    errorLocations.put(functionName, q1);
  }

}
