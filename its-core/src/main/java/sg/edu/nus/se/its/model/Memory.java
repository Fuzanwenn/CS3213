package sg.edu.nus.se.its.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import org.apache.commons.lang3.SerializationUtils;


/**
 * Maps variable names to their values.
 */
public class Memory extends HashMap<String, Object> {

  private static final long serialVersionUID = 1L;

  public Memory() {
    super();
  }

  public Object getValueForVariable(String variableName) {
    return this.get(variableName);
  }

  @Override
  public Memory clone() {
    Memory clone = null;
    try {
      clone = SerializationUtils.clone(this);
      for (Map.Entry e : this.entrySet()) {
        // Clone set value separately to maintain original set order
        if (e.getValue() instanceof HashSet) {
          clone.put(e.getKey().toString(), ((HashSet<?>) e.getValue()).clone());
        }
      }
    } catch (Exception e) {
      System.out.println(this);
      e.printStackTrace();
    }
    return clone;
  }
}
