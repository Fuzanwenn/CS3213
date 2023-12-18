package sg.edu.nus.se.its.refactoring;

import java.util.Optional;
import sg.edu.nus.se.its.model.Program;

/**
 * Refactoring rules interface.
 */
public interface RefactoringRule {
  /**
   * Refactor the provided program at the given function name and location. Returns an empty
   * Optional if the refactoring rule cannot be applied at the given function name and location.
   *
   * @param program -- the program to refactor
   * @param functionName -- the function name to apply refactoring
   * @param location -- the location to apply refactoring
   * @return the refactored program
   */
  public Optional<Program> applyRule(Program program, String functionName, int location);
}
