package sg.edu.nus.se.its.errorlocalizer.checker;

import sg.edu.nus.se.its.errorlocalizer.checker.exception.IllegalTransformationException;
import sg.edu.nus.se.its.errorlocalizer.dependencytree.DependencyTree;
import sg.edu.nus.se.its.model.Function;
import sg.edu.nus.se.its.model.Input;
import sg.edu.nus.se.its.model.Program;
import sg.edu.nus.se.its.model.Variable;

/**
 * The class represents the state of the progress on the programs being error-localized.
 * When the error localizer finishes a state, it provides new information by calling
 * with***() methods. As such the Configuration transforms to the next state.
 * Stage 1: Input,
 * Stage 2: Input, Functions,
 * Stage 3: Input, Functions, Locs,
 * Stage 4: Input, Functions, Locs, Vars
 * Stage 5: Input, Functions, Locs, Vars, Programs
 */
public class Configuration {
  public static final String MSG_NO_LOC_WHEN_ADD_VAR =
      "The configuration cannot be transformed to next stage with variables <ref:%s, sub:%s>,"
          + " because the reference loc or submitted loc is null.";
  public static final String MSG_NO_FUNC_WHEN_ADD_VAR =
      "The configuration cannot be transformed to next stage with variables <ref:%s, sub:%s>,"
          + " because the functions are null.";
  public static final String MSG_NO_FUNC_WHEN_ADD_LOC =
      "The configuration cannot be transformed to next stage with locs <ref:%d, sub:%d>,"
          + " because the functions are null.";
  public static final String MSG_INCOMPLETE_ATTRIBUTE_WHEN_BUILD_TREE =
      "The configuration cannot build trees because the attributes of %s are incomplete.";
  public static final String WORD_REFERENCE = "reference";
  public static final String WORD_SUBMITTED = "submitted";

  private Input programInput;
  private Function referenceFunction;
  private Function submittedFunction;
  private Integer refLoc;
  private Integer subLoc;
  private Variable referenceVariable;
  private Variable submittedVariable;
  private DependencyTree refTree;
  private DependencyTree subTree;

  public Input getProgramInput() {
    return programInput;
  }

  public Function getReferenceFunction() {
    return referenceFunction;
  }

  public Function getSubmittedFunction() {
    return submittedFunction;
  }

  public Integer getRefLoc() {
    return refLoc;
  }

  public Integer getSubLoc() {
    return subLoc;
  }

  public Variable getReferenceVariable() {
    return referenceVariable;
  }

  public Variable getSubmittedVariable() {
    return submittedVariable;
  }

  private Configuration(Input programInput,
                        Function referenceFunction, Function submittedFunction,
                        Integer refLoc, Integer subLoc,
                        Variable referenceVariable, Variable submittedVariable) {
    this.programInput = programInput;
    this.referenceFunction = referenceFunction;
    this.submittedFunction = submittedFunction;
    this.refLoc = refLoc;
    this.subLoc = subLoc;
    this.referenceVariable = referenceVariable;
    this.submittedVariable = submittedVariable;
  }

  public static Configuration of(Input input) {
    return new Configuration(input, null, null, null, null, null, null);
  }

  public Configuration withFunctions(Function referenceFunction, Function submittedFunction) {
    return new Configuration(this.programInput, referenceFunction, submittedFunction,
        null, null, null, null);
  }

  /**
   * Returns a configuration with given locs filled, the current configuration should have
   * non-null functions.
   *
   * @param refLoc The given loc
   * @param subLoc The given loc
   * @return The configuration for the next stage
   */
  public Configuration withLocs(Integer refLoc, Integer subLoc) {
    if (this.referenceFunction == null || this.submittedFunction == null) {
      throw new IllegalTransformationException(
          String.format(MSG_NO_FUNC_WHEN_ADD_LOC, refLoc, subLoc));
    }
    return new Configuration(this.programInput, this.referenceFunction, this.submittedFunction,
        refLoc, subLoc, null, null);
  }

  /**
   * Returns a configuration with given variables filled, the current configuration should have
   * non-null locs.
   *
   * @param refVar The given variable
   * @param subVar The given variable
   * @return The configuration for the next stage
   */
  public Configuration withVariables(Variable refVar, Variable subVar) {
    if (this.referenceFunction == null || this.submittedFunction == null) {
      throw new IllegalTransformationException(
          String.format(MSG_NO_FUNC_WHEN_ADD_VAR, refVar, subVar));
    }
    if (this.refLoc == null || this.subLoc == null) {
      throw new IllegalTransformationException(
          String.format(MSG_NO_LOC_WHEN_ADD_VAR, refVar, subVar));
    }
    return new Configuration(this.programInput, this.referenceFunction, this.submittedFunction,
        this.refLoc, this.subLoc, refVar, subVar);
  }

  public DependencyTree getSubTree() {
    return subTree;
  }

  public DependencyTree getRefTree() {
    return refTree;
  }

  /**
   * Returns itself with Dependency Tree built based on the given programs.
   * The tree will be build only when all information (except input) are provided.
   *
   * @param referenceProgram The given program
   * @param submittedProgram The given program
   * @return itself
   */
  public Configuration withPrograms(Program referenceProgram, Program submittedProgram) {
    if (referenceProgram != null && referenceFunction != null
        && refLoc != null && referenceVariable != null) {
      this.refTree = new DependencyTree(referenceProgram,
          referenceFunction, referenceVariable, refLoc);
    } else {
      throw new IllegalTransformationException(
          String.format(MSG_INCOMPLETE_ATTRIBUTE_WHEN_BUILD_TREE, WORD_REFERENCE));
    }

    if (submittedProgram != null && submittedFunction != null
        && subLoc != null && submittedVariable != null) {
      this.subTree = new DependencyTree(submittedProgram, submittedFunction,
          submittedVariable, subLoc);
    } else {
      throw new IllegalTransformationException(
          String.format(MSG_INCOMPLETE_ATTRIBUTE_WHEN_BUILD_TREE, WORD_SUBMITTED));
    }
    return this;
  }
}
