package sg.edu.nus.se.its.interpreter;

import sg.edu.nus.se.its.model.Memory;

/**
 * Interface description to describe objects that can be executed by an interpreter.
 */
public interface Executable {

  public Object execute(Memory memory, AbstractInterpreter withInterpreter);

}
