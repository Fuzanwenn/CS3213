package sg.edu.nus.se.its.errorlocalizer.checker;

import static sg.edu.nus.se.its.errorlocalizer.ErrorLocalizerImpl.LOC_FUNCTION_OR_VARIABLE;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.javatuples.Pair;
import sg.edu.nus.se.its.alignment.StructuralMapping;
import sg.edu.nus.se.its.alignment.VariableMapping;
import sg.edu.nus.se.its.errorlocalizer.ResultEditor;
import sg.edu.nus.se.its.model.Function;
import sg.edu.nus.se.its.model.Input;
import sg.edu.nus.se.its.model.Program;

/**
 * Represents a class checking the function mappings of the programs.
 */
public class FunctionChecker extends Checker {

  public static final String MSG_SUB_FUNC_NOT_FOUND =
      "No matching submission function <%s>";
  public static final String MSG_RETURN_TYPE_UNMATCHED =
      "Function return types do not match for <%s>";
  public static final String MSG_PARAM_TYPES_UNMATCHED =
      "Function params do not match for <%s>";

  /**
   * Constructs a checker to compare the different aspects of the functions of both programs.
   *
   * @param resultEditor contains the results of the checks
   * @param referenceProgram is the correct program answer
   * @param submittedProgram is the program submitted by students
   * @param structuralMapping is the mapping of the structure between the two programs
   * @param variableMapping is the mapping of the variables between the two programs
   */
  public FunctionChecker(ResultEditor resultEditor, Program referenceProgram,
                         Program submittedProgram, StructuralMapping structuralMapping,
                         VariableMapping variableMapping) {
    super(resultEditor, referenceProgram, submittedProgram, structuralMapping, variableMapping);
  }

  /**
   * Checks the two programs for the differences in the function mappings.
   *
   * @param configInput The configuration from previous stage
   * @return the configuration to be used for the next stage
   */
  @Override
  public Stream<? extends Configuration> check(Configuration configInput) {
    return referenceProgram.getFncs().keySet().stream()
        .map(s -> new Pair<>(referenceProgram.getFunctionForName(s),
            submittedProgram.getFunctionForName(s)))
        .map(p -> checkSubFuncExist(configInput.getProgramInput(), p))
        .filter(Objects::nonNull)
        .map(p -> checkFuncParams(configInput.getProgramInput(), p))
        .map(p -> checkFuncReturnType(configInput.getProgramInput(), p))
        .filter(Objects::nonNull)
        .map(p -> configInput.withFunctions(p.getValue0(), p.getValue1()));
  }

  /**
   * Checks if the functions in the reference program exists in the submitted program.
   *
   * @param input is the inputs to the source program
   * @param p Pair of corresponding functions from the reference and submission programs
   * @return the same pair if the function exists, otherwise return null
   */
  private Pair<? extends Function, ? extends Function>
      checkSubFuncExist(Input input, Pair<? extends Function, ? extends Function> p) {
    Function refFunc = p.getValue0();
    Function subFunc = p.getValue1();
    if (subFunc == null) {
      resultEditor.addUnmatchedException(input,
          LOC_FUNCTION_OR_VARIABLE, LOC_FUNCTION_OR_VARIABLE,
          String.format(MSG_SUB_FUNC_NOT_FOUND, refFunc.getName()),
          refFunc.getName(), this.variableMapping.getTopMapping(refFunc.getName()));
      return null;
    }
    return p;
  }

  /**
   * Checks if the return type of the functions are the same for both programs.
   *
   * @param input is the inputs to the source program
   * @param p Pair containing the result from the previous check, and another pair of the
   *          corresponding functions from the reference and submission programs
   * @return the pair of functions if the function return types are the same, otherwise return null
   */
  private Pair<? extends Function, ? extends Function>
      checkFuncReturnType(Input input,
                      Pair<? extends Boolean,
                          ? extends Pair<? extends Function, ? extends Function>> p) {
    boolean drop = p.getValue0();
    Function refFunc = p.getValue1().getValue0();
    Function subFunc = p.getValue1().getValue1();
    String refFuncRetType = refFunc.getRettype();
    String subFuncRetType = subFunc.getRettype();
    if (!(refFuncRetType.equals(subFuncRetType))) {
      resultEditor.addUnmatchedException(input,
          refFunc.getInitloc(), subFunc.getInitloc(),
          String.format(MSG_RETURN_TYPE_UNMATCHED, refFunc.getName()),
          refFunc.getName(), this.variableMapping.getTopMapping(refFunc.getName()));
      return null;
    }
    if (drop) {
      return null;
    }
    return p.getValue1();
  }

  /**
   * Checks if the function parameters are of the same type and order.
   *
   * @param input is the inputs to the source program
   * @param p Pair of corresponding functions from the reference and submission programs
   * @return A Pair with a boolean indicating the result, and the same pair of functions
   */
  private Pair<? extends Boolean, ? extends Pair<? extends Function, ? extends Function>>
      checkFuncParams(Input input, Pair<? extends Function, ? extends Function> p) {
    Function refFunc = p.getValue0();
    Function subFunc = p.getValue1();
    List<String> refFuncParamsTypes = refFunc.getParams().stream()
        .map(Pair::getValue1).collect(Collectors.toList());
    List<String> subFuncParamsTypes = subFunc.getParams().stream()
        .map(Pair::getValue1).collect(Collectors.toList());
    if (!(refFuncParamsTypes.equals(subFuncParamsTypes))) {
      resultEditor.addUnmatchedException(input,
          refFunc.getInitloc(), subFunc.getInitloc(),
          String.format(MSG_PARAM_TYPES_UNMATCHED, refFunc.getName()),
          refFunc.getName(), this.variableMapping.getTopMapping(refFunc.getName()));
      return new Pair<>(true, p);
    }
    return new Pair<>(false, p);
  }
}
