package sg.edu.nus.se.its.validation;

import sg.edu.nus.se.its.model.Program;

/**
 * Interface for the patch validator.
 */
public interface PatchValidator {

  /**
   * Determines whether the repaired program (i.e., the patch) is semantically equivalent with
   * reference program.
   *
   * @param repairedProgram - the parsed internal representation of the repaired program.
   * @param referenceProgram - the parsed internal representation of the reference program.
   * @return the validation result, which holds a boolean value and an optional Input object
   *         representing a counter example
   */
  public ValidationResult validate(Program repairedProgram, Program referenceProgram);

}
