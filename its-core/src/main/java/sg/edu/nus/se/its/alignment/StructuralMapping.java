package sg.edu.nus.se.its.alignment;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Holds the mapping between two program objects representing their structural alignment.
 */
public class StructuralMapping {

  /**
   * Maps a functionName as String to a Map of Integer mappings for the locations in the two
   * programs.
   */
  private Map<String, Map<Integer, Integer>> mapping;

  /**
   * Creates an empty structural mapping.
   */
  public StructuralMapping() {
    this.mapping = new HashMap<>();
  }

  public Map<Integer, Integer> put(String functionName, Map<Integer, Integer> locationMapping) {
    return mapping.put(functionName, locationMapping);
  }

  public Map<String, Map<Integer, Integer>> getAllMappings() {
    return mapping;
  }

  public Map<Integer, Integer> getMapping(String functionName) {
    return mapping.get(functionName);
  }

  public Integer getMatchingLoc(String functionaName, int location) {
    return Optional.ofNullable(mapping.get(functionaName)).map(locMatch -> locMatch.get(location))
        .orElse(null);
  }

  @Override
  public boolean equals(Object other) {
    if (!(other instanceof StructuralMapping)) {
      return false;
    }

    return this.mapping.equals(((StructuralMapping) other).mapping);
  }

  @Override
  public String toString() {
    if (mapping != null) {
      return mapping.toString();
    } else {
      return "{}";
    }
  }
}
