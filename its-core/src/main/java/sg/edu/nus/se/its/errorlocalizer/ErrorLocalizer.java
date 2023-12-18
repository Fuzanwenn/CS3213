package sg.edu.nus.se.its.errorlocalizer;

import java.util.List;
import sg.edu.nus.se.its.alignment.StructuralMapping;
import sg.edu.nus.se.its.alignment.VariableMapping;
import sg.edu.nus.se.its.interpreter.Interpreter;
import sg.edu.nus.se.its.model.Input;
import sg.edu.nus.se.its.model.Program;

/**
 * Interface for the error localization.
 */
public interface ErrorLocalizer {

  /**
   * Returns a list of erroneous blocks of the submitted program, given the reference program, the
   * student program and the mapping of aligned blocks.
   *
   * @param submittedProgram - the parsed internal representation of the student's program.
   * @param referenceProgram - the parsed internal representation of the reference program.
   * @param inputs - list of String value for the inputs to test for (can be null)
   * @param structuralMapping - the alignment of the reference program with the student's program.
   * @param variableMapping - the mapping of the variables in both programs
   * @param interpreter - interpreter object for the execution of the traces
   * @return list of error location pairs
   */
  public ErrorLocalisation localizeErrors(Program submittedProgram, Program referenceProgram,
      List<Input> inputs, StructuralMapping structuralMapping,
      VariableMapping variableMapping, Interpreter interpreter);

}
