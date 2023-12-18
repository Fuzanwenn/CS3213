package sg.edu.nus.se.its.alignment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import sg.edu.nus.se.its.model.Variable;

/**
 * Stores the variable mapping between two programs. Assumes that a structural mapping does exist.
 */
public class VariableMapping {

  /**
   * Maps a functionName as String to a List of Variable mappings.
   */
  private Map<String, List<Map<Variable, Variable>>> mappingByFunctionName;

  /**
   * Creates an empty variable mapping.
   */
  public VariableMapping() {
    this.mappingByFunctionName = new HashMap<>();
  }

  /**
   * Adds array of variable mappings for the give function name.
   *
   * @param functionName - String
   * @param variableMapping - one variable mappings
   * @return list of variable mappings
   */
  public List<Map<Variable, Variable>> add(String functionName,
      Map<Variable, Variable> variableMapping) {
    List<Map<Variable, Variable>> listOfMappings = getMappings(functionName);
    if (listOfMappings == null) {
      listOfMappings = new ArrayList<>();
    }
    listOfMappings.add(variableMapping);
    return mappingByFunctionName.put(functionName, listOfMappings);
  }

  /**
   * Adds list of variable mappings for the give function name.
   *
   * @param functionName - String
   * @param variableMappings - list of variable mappings
   * @return list of variable mappings
   */
  public List<Map<Variable, Variable>> put(String functionName,
      List<Map<Variable, Variable>> variableMappings) {
    return mappingByFunctionName.put(functionName, variableMappings);
  }

  public Map<String, List<Map<Variable, Variable>>> getAllMappings() {
    return mappingByFunctionName;
  }

  public List<Map<Variable, Variable>> getMappings(String functionName) {
    return mappingByFunctionName.get(functionName);
  }

  /**
   * Returns the top variable mapping, if any.
   *
   * @param functionName the function name
   * @return the top variable mapping
   */
  public Map<Variable, Variable> getTopMapping(String functionName) {
    List<Map<Variable, Variable>> mappings = getMappings(functionName);
    assert mappings.size() > 0;
    return mappings.get(0);
  }

  /**
   * Returns all matching variables in the given function for the given variable name.
   *
   * @param functionName - String
   * @param variable - Variable object
   * @return list of matching variables
   */
  public List<Variable> getMatchingVariables(String functionName, Variable variable) {
    List<Map<Variable, Variable>> listOfMappings = mappingByFunctionName.get(functionName);

    if (listOfMappings == null || listOfMappings.isEmpty()) {
      return Collections.emptyList();
    }

    List<Variable> matchingVariables = new ArrayList<>();
    for (Map<Variable, Variable> mapping : listOfMappings) {
      Variable matchingVar = mapping.get(variable);
      if (matchingVar != null) {
        matchingVariables.add(matchingVar);
      }
    }
    return matchingVariables;
  }

  /**
   * Responsible to getting the variable from the incorrect program given the variable from the
   * reference program. Note: If exactMatching=true then it checks for an "exact" match of the
   * variable name without taking into account the primes.
   *
   * @param functionName - String
   * @param variableName - String
   * @param exactMatching - boolean
   * @return list of matching variables
   */
  public List<Variable> getMatchingVariables(String functionName, String variableName,
      boolean exactMatching) {
    List<Map<Variable, Variable>> listOfMappings = mappingByFunctionName.get(functionName);

    if (listOfMappings == null || listOfMappings.isEmpty()) {
      return Collections.emptyList();
    }

    List<Variable> matchingVariables = new ArrayList<>();
    for (Map<Variable, Variable> mapping : listOfMappings) {
      for (Map.Entry<Variable, Variable> entry : mapping.entrySet()) {
        Variable matchingVariable = exactMatching ? getExactMatchingVariable(mapping, variableName)
            : getMatchingVariable(mapping, variableName);
        if (matchingVariable != null) {
          matchingVariables.add(entry.getValue());
        }
      }
    }
    return matchingVariables;
  }

  /**
   * Returns matching variable in given variable mapping.
   *
   * @param mapping - Map of variables
   * @param variableName - variable name
   * @return Variable object or null
   */
  public static Variable getMatchingVariable(Map<Variable, Variable> mapping, String variableName) {
    String unprimedVariableName =
        Variable.isPrimedName(variableName) ? Variable.asUnprimedVariableName(variableName)
            : variableName;

    for (Map.Entry<Variable, Variable> entry : mapping.entrySet()) {
      if (entry.getKey().toString().equals(unprimedVariableName) || entry.getKey().toString()
          .equals(Variable.asPrimedVariableName(unprimedVariableName))) {
        return entry.getValue();
      }
    }

    return null;
  }

  /**
   * Returns exact matching variable in given variable mapping, without taking into account the
   * primes.
   *
   * @param mapping - Map of variables
   * @param variableName - variable name
   * @return Variable object or null
   */
  public static Variable getExactMatchingVariable(Map<Variable, Variable> mapping,
      String variableName) {
    for (Map.Entry<Variable, Variable> entry : mapping.entrySet()) {
      if (entry.getKey().toString().equals(variableName)) {
        return entry.getValue();
      }
    }
    return null;
  }

  @Override
  public String toString() {
    if (mappingByFunctionName != null) {
      return mappingByFunctionName.toString();
    } else {
      return "{}";
    }
  }
}
