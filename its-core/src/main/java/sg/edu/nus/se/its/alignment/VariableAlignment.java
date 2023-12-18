package sg.edu.nus.se.its.alignment;

import sg.edu.nus.se.its.model.Program;

/**
 * The interface for any variable alignment between two structural aligned program.
 */
public interface VariableAlignment {

  /**
   * Generates variable alignment.
   *
   * @param reference - the reference program
   * @param submission - the submitted/incorrect program
   * @param strucAlignment - the structural alignment
   * @return the resulting variable alignment
   * @throws AlignmentException - a custom alignment exception
   */
  VariableMapping generateVariableAlignment(Program reference, Program submission,
      StructuralMapping strucAlignment) throws AlignmentException;
}
