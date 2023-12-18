package sg.edu.nus.se.its.interpreter;

import sg.edu.nus.se.its.model.Input;
import sg.edu.nus.se.its.model.Program;

/**
 * Interfaces for the interpretation of the program execution.
 */
public interface Interpreter {

  /**
   * Executes a program and produces an execution trace.
   *
   * @param program -- Program object
   * @return execution trace
   */
  public Trace executeProgram(Program program);

  /**
   * Executes a program with the given input and produces an execution trace.
   *
   * @param program -- Program object
   * @param input -- program's input
   * @return execution trace
   */
  public Trace executeProgram(Program program, Input input);

}
