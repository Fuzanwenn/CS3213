package sg.edu.nus.se.its.errorlocalizer.checker;

import static sg.edu.nus.se.its.errorlocalizer.ErrorLocalizerImpl.LOC_IMPORT_STATEMENT;

import java.util.stream.Stream;
import sg.edu.nus.se.its.alignment.StructuralMapping;
import sg.edu.nus.se.its.alignment.VariableMapping;
import sg.edu.nus.se.its.errorlocalizer.ResultEditor;
import sg.edu.nus.se.its.model.Input;
import sg.edu.nus.se.its.model.Program;
import sg.edu.nus.se.its.util.constants.Constants;

/**
 * Represents a class to check the import statements of the programs.
 */
public class ImportStatementChecker extends Checker {
  public static final String MSG_IMPORT_STATEMENT_UNMATCHED =
      "Import Statement '%s' does not appear in %s program.";
  public static final String WORD_REFERENCE = "reference";
  public static final String WORD_SUBMITTED = "submitted";

  /**
   * Constructs a checker to check for differences in the import statements of the program.
   *
   * @param resultEditor contains the results of the checks
   * @param referenceProgram is the correct program answer
   * @param submittedProgram is the program submitted by students
   * @param structuralMapping is the mapping of the structure between the two programs
   * @param variableMapping is the mapping of the variables between the two programs
   */
  public ImportStatementChecker(ResultEditor resultEditor, Program referenceProgram,
                                Program submittedProgram, StructuralMapping structuralMapping,
                                VariableMapping variableMapping) {
    super(resultEditor, referenceProgram, submittedProgram, structuralMapping, variableMapping);
  }

  /**
   * Checks the two programs for the differences in import statements.
   * Since import statements of a program cannot be mapped to a specific function, the error
   * location is currently mapped to the main function and variable mapping.
   * It assumes that every program has a main function.
   *
   * @param configuration The configuration from previous stage
   * @return the configuration to be used for the next stage
   */
  @Override
  public Stream<? extends Configuration> check(Configuration configuration) {
    referenceProgram.getImportStatements().stream()
        .filter(refIs -> submittedProgram.getImportStatements().stream()
            .noneMatch(refIs::equals))
        .forEach(refIs ->
            addImportStatementMismatch(configuration.getProgramInput(), submittedProgram, refIs));
    submittedProgram.getImportStatements().stream()
        .filter(subIs -> referenceProgram.getImportStatements().stream()
            .noneMatch(subIs::equals))
        .forEach(subIs ->
            addImportStatementMismatch(configuration.getProgramInput(), referenceProgram, subIs));
    return Stream.of(configuration);
  }


  /**
   * Adds the error location to the result editor.
   *
   * @param programInput is the inputs to the source program
   * @param programDoesNotHaveTheStatement is the program that is missing the import statement
   * @param importStatement is the import statement that is missing
   */
  private void addImportStatementMismatch(Input programInput,
                                          Program programDoesNotHaveTheStatement,
                                          String importStatement) {
    String defaultFuncName = Constants.DEFAULT_ENTRY_FUNCTION_NAME;
    String programString;

    if (programDoesNotHaveTheStatement == referenceProgram) {
      programString = WORD_REFERENCE;
    } else {
      programString = WORD_SUBMITTED;
    }
    String message =  String.format(MSG_IMPORT_STATEMENT_UNMATCHED, importStatement, programString);

    this.resultEditor.addUnmatchedException(programInput,
            LOC_IMPORT_STATEMENT, LOC_IMPORT_STATEMENT,
            message, defaultFuncName, variableMapping.getTopMapping(defaultFuncName));
  }
}
