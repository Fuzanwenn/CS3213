package sg.edu.nus.se.its.interpreter;

import sg.edu.nus.se.its.model.Memory;

/**
 * Entry to a trace object.
 */
public class TraceEntry {

  private String functionName;

  /**
   * Control flow block ID.
   */
  private int location;

  private Memory mem;

  /**
   * Flag to determine whether this trace entry was already checked during error localization.
   */
  private boolean isChecked;

  /**
   * Creates a new trace entry for the given function name, memory, and code location.
   */
  public TraceEntry(String functionName, int loc, Memory mem) {
    this.functionName = functionName;
    this.location = loc;
    this.mem = mem;
    this.isChecked = false;
  }

  public String getFunctionName() {
    return functionName;
  }

  /**
   * Get the ID of the first control-flow block in the trace.
   */
  public int getLocation() {
    return location;
  }

  public Memory getMem() {
    return mem;
  }

  /**
   * Sets the "isChecked" flag to true and returns whether there was any change in its value.
   *
   * @return true if the trace entry was previously unchecked, otherwise false
   */
  public boolean setChecked() {
    boolean returnValue = !isChecked;
    isChecked = true;
    return returnValue;
  }

  public void resetCheck() {
    this.isChecked = false;
  }

  public boolean getCheck() {
    return isChecked;
  }

  @Override
  public String toString() {
    return String.format("(fnc=%s, loc=%d, mem=%s)", this.functionName, this.location, this.mem);
  }
}
