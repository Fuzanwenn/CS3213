package sg.edu.nus.se.its.validation;

import java.util.Optional;
import sg.edu.nus.se.its.model.Input;

/**
 * Data class to hold the result of the patch validation.
 */
public class ValidationResult {

  private Optional<Input> counterExample;
  private boolean isValidPatch;

  public ValidationResult(boolean isValidPatch) {
    this(isValidPatch, null);
  }

  public ValidationResult(boolean isValidPatch, Input counterExample) {
    this.isValidPatch = isValidPatch;
    this.counterExample = Optional.ofNullable(counterExample);
  }

  /**
   * Returns whether the repaired program is semantic equivalent to the reference program.
   */
  public boolean isValidPatch() {
    return this.isValidPatch;
  }

  /**
   * If the validation result is negative, this method returns a counterexample that shows the
   * difference semantic of repairedProgram and referenceProgram.
   *
   * @return Optional<Input>
   */
  public Optional<Input> getCounterExample() {
    return this.counterExample;
  }

}
