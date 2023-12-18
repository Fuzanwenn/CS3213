package sg.edu.nus.se.its.model;

import java.util.Arrays;

/**
 * Data class representing the input to a program. It consists of function arguments and I/O inputs.
 * Therefore, one Input object represents the complete list of inputs needed to run a program.
 */
public class Input {

  /**
   * I/O input to the Program.
   */
  private String[] inputs;

  /**
   * Function arguments.
   */
  private String[] args;

  public Input(String[] inputs, String[] args) {
    this.inputs = inputs == null ? new String[0] : inputs;
    this.args = args == null ? new String[0] : args;
  }

  public Input() {
    this.inputs = new String[0];
    this.args = new String[0];
  }

  public String[] getInputs() {
    return inputs;
  }

  public String[] getArgs() {
    return args;
  }

  @Override
  public String toString() {
    return "Input{" + "inputs=" + Arrays.toString(inputs) + ", args=" + Arrays.toString(args) + '}';
  }
}
