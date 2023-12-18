package sg.edu.nus.se.its.refactoring;

import java.util.Optional;
import sg.edu.nus.se.its.model.Program;

/**
 * Refactor interface for refactoring programs.
 */
public interface Refactor {

  /**
   * Refactor a program using provided refactoring rules.
   *
   * @param program -- the program to refactor
   * @param rule -- the refactoring rule to apply
   * @param functionName -- the function name to apply refactoring rule
   * @param location -- the location to apply refactoring rule
   * @return the refactored program
   */
  public Optional<Program> refactorProgram(Program program, RefactoringRule rule,
      String functionName, int location);
}
