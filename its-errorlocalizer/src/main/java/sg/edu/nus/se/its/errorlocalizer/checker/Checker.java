package sg.edu.nus.se.its.errorlocalizer.checker;

import java.util.stream.Stream;
import sg.edu.nus.se.its.alignment.StructuralMapping;
import sg.edu.nus.se.its.alignment.VariableMapping;
import sg.edu.nus.se.its.errorlocalizer.ResultEditor;
import sg.edu.nus.se.its.model.Program;

/**
 * Represents the sequential checking constraints when the error localizer runs.
 */
public abstract class Checker {
  final ResultEditor resultEditor;
  final Program referenceProgram;
  final Program submittedProgram;
  final StructuralMapping structuralMapping;
  final VariableMapping variableMapping;

  /**
   * The constructor of a checker.
   *
   * @param resultEditor The given result editor
   * @param referenceProgram The given program
   * @param submittedProgram The given program
   * @param structuralMapping The given structural mappings
   * @param variableMapping The given variable mappings
   */
  public Checker(ResultEditor resultEditor, Program referenceProgram, Program submittedProgram,
                 StructuralMapping structuralMapping, VariableMapping variableMapping) {
    this.resultEditor = resultEditor;
    this.referenceProgram = referenceProgram;
    this.submittedProgram = submittedProgram;
    this.structuralMapping = structuralMapping;
    this.variableMapping = variableMapping;
  }

  /**
   * Returns a stream of configurations for the next stage in error localizer.
   * This is implemented by five types of checkers. Each fills up attributes of a Configuration 
   * object. Additionally, the first four checkers add DueToUnmatchedException errors for, and 
   * exclude, components of a program that do not match between the reference and submission 
   * programs such as import statements, functions, locs and variables. The fifth checker 
   * compares dependency trees instead and adds DueToUnmatchedException and VariableValueMismatch 
   * errors where appropriate.
   * These checkers are run sequentially in {@code ErrorLocalizerImpl#runErrorLocalizer} to ensure 
   * dependency trees created are based only on existing and comparable components of a program.
   *
   * @param configuration The configuration from previous stage
   * @return The configuration for next stage
   */
  public abstract Stream<? extends Configuration> check(Configuration configuration);
}
