package sg.edu.nus.se.its.errorlocalizer;

import java.util.List;
import org.apache.commons.lang3.NotImplementedException;
import sg.edu.nus.se.its.alignment.StructuralMapping;
import sg.edu.nus.se.its.alignment.VariableMapping;
import sg.edu.nus.se.its.interpreter.Interpreter;
import sg.edu.nus.se.its.model.Input;
import sg.edu.nus.se.its.model.Program;

/**
 * The class represents the implementation of an error localizer.
 */
public class ErrorLocalizerImpl implements ErrorLocalizer {

  /**
   * Returns a collection of error locations.
   *
   * @param submittedProgram  - the parsed internal representation of the student's program.
   * @param referenceProgram  - the parsed internal representation of the reference program.
   * @param inputs            - list of String value for the inputs to test for (can be null)
   * @param structuralMapping - the alignment of the reference program with the student's program.
   * @param variableMapping   - the mapping of the variables in both programs
   * @param interpreter       - interpreter object for the execution of the traces
   * @return A collection of error locations.
   */
  @Override
  public ErrorLocalisation localizeErrors(Program submittedProgram, Program referenceProgram,
                                          List<Input> inputs, StructuralMapping structuralMapping,
                                          VariableMapping variableMapping,
                                          Interpreter interpreter) {
    throw new NotImplementedException();
  }
}
