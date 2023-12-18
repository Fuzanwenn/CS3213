package sg.edu.nus.se.its.errorlocalizer.utils;

import static sg.edu.nus.se.its.util.constants.Constants.FUNCTION_CALL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import sg.edu.nus.se.its.alignment.VariableMapping;
import sg.edu.nus.se.its.model.Expression;
import sg.edu.nus.se.its.model.Function;
import sg.edu.nus.se.its.model.Operation;
import sg.edu.nus.se.its.model.Program;
import sg.edu.nus.se.its.model.Variable;
import sg.edu.nus.se.its.util.constants.Constants;

/**
 * The utils for expressions used by the error localizer.
 */
public class ExpressionUtil {
  private static final String MSG_NOT_BINARY_OPERATION = "Only binary operations can be reversed.";
  private static Map<String, String> symmetricOperators = new HashMap<>();

  static {
    // The operators in c programs and pythons programs are parsed differently.
    // This section includes the operators of the C programs.
    symmetricOperators.put("+", "+");
    symmetricOperators.put("*", "*");
    symmetricOperators.put("&&", "&&");
    symmetricOperators.put("||", "||");
    symmetricOperators.put("==", "==");
    symmetricOperators.put("!=", "!=");
    symmetricOperators.put("<", ">");
    symmetricOperators.put(">", "<");
    symmetricOperators.put(">=", "<=");
    symmetricOperators.put("<=", ">=");

    // This section includes the operators of the python programs.
    symmetricOperators.put("And", "And");
    symmetricOperators.put("Or", "Or");
    symmetricOperators.put("Add", "Add");
    symmetricOperators.put("Mult", "Mult");
    symmetricOperators.put("BitOr", "BitOr");
    symmetricOperators.put("BitAnd", "BitAnd");
    symmetricOperators.put("BitXor", "BitXor");
    symmetricOperators.put("Eq", "Eq");
    symmetricOperators.put("NotEq", "NotEq");
    symmetricOperators.put("Lt", "Gt");
    symmetricOperators.put("LtE", "GtE");
    symmetricOperators.put("Gt", "Lt");
    symmetricOperators.put("GtE", "LtE");
    symmetricOperators.put("Is", "Is");
    symmetricOperators.put("IsNot", "IsNot");
  }

  /**
   * Returned the non-void function being called
   * by the given operation in the given program.
   *
   * @param program   The given program
   * @param operation The given operation
   * @return the non-void function being called
   */
  public static Function getCalledFunc(Program program, Operation operation) {
    if (isFuncCall(operation)) {
      return program.getfnc((operation.getArgs().get(0)).toString());
    }
    return null;
  }

  /**
   * Returns the arguments of the non-void function being called
   * in the given operation.
   *
   * @param operation The given operation.
   * @return the arguments of the function being called
   */
  public static List<Expression> getCalledFuncArgs(Operation operation) {
    List<Expression> results = new ArrayList<>();
    if (isFuncCall(operation) && operation.getArgs().size() > 1) {
      Stream.iterate(1, i -> i + 1)
          .limit(operation.getArgs().size() - 1)
          .map(i -> operation.getArgs().get(i))
          .forEachOrdered(e -> results.add(e));
    }
    return results;
  }

  /**
   * Returns if the given operation calls another non-void function.
   *
   * @param operation The given operation.
   * @return if the given operation calls another non-void function
   */
  public static boolean isFuncCall(Operation operation) {
    return operation.getName().equals(FUNCTION_CALL);
  }

  /**
   * Returns true if the order of arguments in the given operation matters.
   *
   * @param operation The given operation
   * @return if the order of arguments in the given operation matters
   */
  public static boolean areArgsOrdered(Operation operation) {
    return getSymmetricBinaryOperationName(operation) == null
        || !(operation.getName().equals(getSymmetricBinaryOperationName(operation)));
  }

  /**
   * Returns true if the order of arguments in the given operation matters.
   *
   * @param operation The given operation
   * @return if the order of arguments in the given operation matters
   */
  public static boolean isCommutative(Operation operation) {
    return getSymmetricBinaryOperationName(operation) != null;
  }



  /**
   * Returns the name of operation that has the opposite direction of the given
   * operation.
   *
   * @param operation The given operation
   * @return the boolean
   */
  public static String getSymmetricBinaryOperationName(Operation operation) {
    return symmetricOperators.get(operation.getName());
  }

  /**
   * Returns the reversed operation of given binary operation, returns null if not commutative.
   *
   * @param binaryOperation The given binary operation
   * @return The reversed operation
   */
  public static Operation getSymmetricBinaryOperation(Operation binaryOperation) {
    String opName = getSymmetricBinaryOperationName(binaryOperation);
    if (binaryOperation.getArgs().size() != 2) {
      throw new RuntimeException(MSG_NOT_BINARY_OPERATION);
    }
    Expression arg1 = binaryOperation.getArgs().get(0);
    Expression arg2 = binaryOperation.getArgs().get(1);
    Operation result = new Operation(opName, List.of(arg2, arg1), binaryOperation.getLineNumber());
    return result;
  }

  /**
   * Returns if the given expression is a controlling variable.
   *
   * @param expression The given expression
   * @return if the given expression is a controlling variable
   */
  public static boolean isControllingVar(Expression expression) {
    if (expression instanceof Variable) {
      Variable variable = (Variable) expression;
      return Constants.VAR_COND.equals(variable.getUnprimedName());
    }
    return false;
  }

  /**
   * Returns if the two given variables are mapped in the given function.
   *
   * @param varInReference The given variable
   * @param varInSubmission The given variable
   * @param variableMapping The given variable mapping
   * @param function The given function
   * @return if the two given variables are mapped in the given function
   */
  public static boolean areMappedVariable(Variable varInReference, Variable varInSubmission,
                                          VariableMapping variableMapping, Function function) {

    boolean areVarMapped;

    Map<String, String> varNameMap = new HashMap<>();
    Map<Variable, Variable> varMap = variableMapping
        .getTopMapping(function.getName());
    varMap.keySet().stream().forEach(key -> varNameMap
        .put(key.getUnprimedName(), varMap.get(key).getUnprimedName()));

    if (varNameMap.get(varInReference.getUnprimedName()) != null) {
      areVarMapped = varNameMap.get(varInReference
          .getUnprimedName()).equals(varInSubmission.getUnprimedName());
    } else {
      areVarMapped = false;
    }
    return areVarMapped;
  }
}
