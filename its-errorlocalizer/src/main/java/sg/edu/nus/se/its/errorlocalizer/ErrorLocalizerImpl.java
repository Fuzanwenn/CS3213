package sg.edu.nus.se.its.errorlocalizer;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;
import sg.edu.nus.se.its.alignment.StructuralMapping;
import sg.edu.nus.se.its.alignment.VariableMapping;
import sg.edu.nus.se.its.errorlocalizer.checker.Checker;
import sg.edu.nus.se.its.errorlocalizer.checker.Configuration;
import sg.edu.nus.se.its.errorlocalizer.checker.ControlFlowGraphChecker;
import sg.edu.nus.se.its.errorlocalizer.checker.DependencyChecker;
import sg.edu.nus.se.its.errorlocalizer.checker.FunctionChecker;
import sg.edu.nus.se.its.errorlocalizer.checker.ImportStatementChecker;
import sg.edu.nus.se.its.errorlocalizer.checker.VariableChecker;
import sg.edu.nus.se.its.interpreter.Interpreter;
import sg.edu.nus.se.its.model.Input;
import sg.edu.nus.se.its.model.Program;

/**
 * Implements the concrete error localizer based on CFG alignment, with the algorithm you choose.
 */
public class ErrorLocalizerImpl implements ErrorLocalizer {
  public static final int LOC_FUNCTION_OR_VARIABLE = -3;
  public static final int LOC_IMPORT_STATEMENT = -2;
  public static final int LOC_ROOT_NOT_FOUND = -1;
  public static final Input NO_INPUT = null;

  private Program referenceProgram;
  private Program submittedProgram;
  private Optional<List<Input>> inputs;
  private StructuralMapping structuralMapping;
  private VariableMapping variableMapping;
  private Interpreter interpreter;

  private Checker importStatementChecker;
  private Checker functionChecker;
  private Checker controlFlowGraphChecker;
  private Checker variableChecker;
  private Checker dependencyChecker;

  private ResultEditor resultEditor;

  private boolean isDebugging = false;

  /**
   * Constructor of the class.
   */
  public ErrorLocalizerImpl() {
    this.resultEditor = new ResultEditor();
  }

  /**
   * Implementation.
   * Calls {@link #runErrorLocalizer runErrorLocalizer} on each input, if any, to the programs.
   * Currently records same errors among different inputs as separate error locations 
   * each with their corresponding triggering inputs.
   *
   * @return list of error location pairs
   */
  @Override
  public ErrorLocalisation localizeErrors(Program submittedProgram, Program referenceProgram,
                                          List<Input> inputs, StructuralMapping structuralMapping,
                                          VariableMapping variableMapping,
                                          Interpreter interpreter) {

    this.inputs = Optional.ofNullable(inputs);
    this.interpreter = interpreter;
    this.referenceProgram = Objects.requireNonNull(referenceProgram);
    this.submittedProgram = Objects.requireNonNull(submittedProgram);
    this.structuralMapping = Objects.requireNonNull(structuralMapping);
    this.variableMapping = Objects.requireNonNull(variableMapping);

    this.inputs
        .filter(inputList -> inputList.size() > 0)
        .ifPresentOrElse(
            inputList -> inputList.forEach(this::runErrorLocalizer),
            () -> runErrorLocalizer(NO_INPUT));

    if (isDebugging) {
      printResult();
    }
    return this.resultEditor.getResult();
  }

  private void initializeCheckers() {
    this.importStatementChecker =
        new ImportStatementChecker(resultEditor, referenceProgram,
            submittedProgram, structuralMapping, variableMapping);
    this.functionChecker =
        new FunctionChecker(resultEditor, referenceProgram,
            submittedProgram, structuralMapping, variableMapping);
    this.controlFlowGraphChecker =
        new ControlFlowGraphChecker(resultEditor, referenceProgram,
            submittedProgram, structuralMapping, variableMapping);
    this.variableChecker =
        new VariableChecker(resultEditor, referenceProgram,
            submittedProgram, structuralMapping, variableMapping);
    this.dependencyChecker =
        new DependencyChecker(resultEditor, referenceProgram,
            submittedProgram, structuralMapping, variableMapping);
  }

  private void printResult() {
    this.resultEditor.getResult().getFunctions().stream().parallel()
        .flatMap(s -> this.resultEditor.getResult()
            .getErrorLocations(s, variableMapping.getTopMapping(s))
            .stream().map(el -> String.format("%s: %s\n", s, el)))
        .forEach(System.out::println);
  }

  /**
   * Builds up Configuration objects by cumulatively filling up each of their attributes through 
   * a sequence of checks/stages. This culminates in {@code dependencyChecker.check()} which 
   * creates and compares two dependency trees for each of the Configuration objects 
   * generated by the previous stages.<br><br>
   *
   * <p>0: A stream of a single Configuration object of a single attribute filled up.<br>
   *   Example:<br>
   *   (input)
   *   <br><br>
   * 1: Same as before, unchanged<br><br>
   * 2: The Configuration object is transformed by {@code functionChecker} into a stream of 
   * Configuration objects with refFunc, subFunc filled up.<br>
   *   Example:<br>
   *   (input, refFunc1, subFunc1)<br>
   *   (input, refFunc2, subFunc2)
   *   <br><br>
   * 3: The stream of Configuration objects is transformed by {@code controlFlowGraphChecker} 
   * into a stream of modified Configuration objects with refLoc, subLoc filled up.<br>
   *   Example:<br>
   *   (input, refFunc1, subFunc1, refLoc1, subLoc1)<br>
   *   (input, refFunc1, subFunc1, refLoc2, subLoc2)<br>
   *   (input, refFunc2, subFunc2, refLoc1, subLoc1)
   *   <br><br>
   * 4: The stream of modified Configuration objects is transformed by {@code variableChecker} 
   * into a stream of further modified Configuration objects with refVar, subVar filled up.<br>
   *   Example:<br>
   *   (input, refFunc1, subFunc1, refLoc1, subLoc1, refVar1, subVar1)<br>
   *   etc
   *   <br><br>
   * 5: The previous stream is transformed by {@code dependencyChecker} into a stream of finalised 
   * Configuration objects with refTree, subTree filled up. Each of these Configuration objects 
   * is then used by {@code dependencyChecker} to run the rest of the error localizer, 
   * namely {@code DependencyChecker#dfs}, to compare the trees.<br>
   *   Example:<br>
   *   (input, refFunc1, subFunc1, refLoc1, subLoc1, refVar1, subVar1, refTree1, subTree1)<br>
   *   etc
   *   <br><br></p>
   *
   * @param programInput The current input to the program for which all of these will be done
   */
  private void runErrorLocalizer(Input programInput) {
    initializeCheckers();
    Stream.of(Configuration.of(programInput))     //0
        .flatMap(importStatementChecker::check)   //1
        .flatMap(functionChecker::check)          //2
        .flatMap(controlFlowGraphChecker::check)  //3
        .flatMap(variableChecker::check)          //4
        .forEach(dependencyChecker::check);       //5
  }

  /**
   * Returns itself with returning line numbers.
   *
   * @return itself
   */
  public ErrorLocalizerImpl withReturnLineNumber() {
    this.resultEditor.setReturnLineNumber(true);
    return this;
  }

  /**
   * Returns itself with returning loc numbers (default).
   *
   * @return itself
   */
  public ErrorLocalizerImpl withReturnLoc() {
    this.resultEditor.setReturnLineNumber(false);
    return this;
  }
}