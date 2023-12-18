package sg.edu.nus.se.its.errorlocalizer.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import sg.edu.nus.se.its.model.Constant;
import sg.edu.nus.se.its.model.Expression;
import sg.edu.nus.se.its.model.Function;
import sg.edu.nus.se.its.model.Operation;
import sg.edu.nus.se.its.model.Variable;

/**
 * The utils for functions used by the error localizer.
 */
public class FunctionUtil {
  private static final String MSG_UNSUPPORTED_EXPRESSION = "Unsupported subclass of Expression.";

  /**
   * Returns the assignment expression to the given variable.
   * in the given loc of given function
   *
   * @param function The given function
   * @param variable The given variable
   * @param loc      The given loc
   * @return the assignment expression
   */
  public static Expression getVariableAssigningExpr(Function function, Variable variable, int loc) {
    return function.getExprs(loc, variable);
  }

  /**
   * Returns a set of locs that can go to the given loc in the given function.
   *
   * @param function The given function
   * @param initLoc  The given destination loc
   * @return A set of locs that can go to the given loc
   */
  public static Set<Integer> findIncomingLocs(Function function, int initLoc) {
    Set<Integer> results = new HashSet<>();
    Map<Integer, HashMap<Boolean, Integer>> transitionMap = function.getLoctrans();
    for (Integer curr : transitionMap.keySet()) {
      HashMap<Boolean, Integer> transitions = transitionMap.get(curr);
      Integer trueDestinationLoc = transitions.get(true);
      Integer falseDestinationLoc = transitions.get(false);
      if (trueDestinationLoc != null) {
        if (trueDestinationLoc == initLoc) {
          results.add(curr);
        }
      }
      if (falseDestinationLoc != null) {
        if (falseDestinationLoc == initLoc) {
          results.add(curr);
        }
      }
    }
    return results;
  }

  /**
   * Returns if the given function has the given variable
   * which is assigned to a new value at the given location.
   *
   * @param function The given function
   * @param variable The given variable
   * @param loc      The given location
   * @return if the given function has the given variable
   *         which is assigned to a new value at the given location.
   */
  public static boolean hasVariable(Function function, Variable variable, int loc) {
    return function.locExist(loc)
        && function.getExprs(loc).stream()
        .anyMatch(x -> x.getValue0().equals(variable.getUnprimedName()));
  }


  /**
   * Returns all first layer expressions from the given loc
   * in the given function constituting the given expression.
   *
   * @param function   The given function
   * @param expression The given expression
   * @param loc        The given loc
   * @return List of all first layer expressions
   */
  public static List<Expression> findRelativeExpressions(
      Function function, Expression expression, int loc) {
    List<Expression> results = new ArrayList<>();

    if (expression instanceof Constant) {
      results.add(expression);
    } else if (expression instanceof Variable) {
      results.add(FunctionUtil.getVariableAssigningExpr(function, (Variable) expression, loc));
    } else if (expression instanceof Operation) {
      // If the expression is an operation, the arguments must be in ordered.
      List<Expression> args = ((Operation) expression).getArgs();
      for (int i = 0; i < args.size(); i++) {
        results.add(args.get(i));
      }
    } else {
      System.out.println(MSG_UNSUPPORTED_EXPRESSION);
    }
    return results;
  }

  /**
   * Returns a list of loc where the given function can return.
   *
   * @param function The given function
   * @return a list of loc where the given function can return
   */
  public static List<Integer> getFuncExitLoc(Function function) {
    List<Integer> results = new ArrayList<>();
    Map<Integer, HashMap<Boolean, Integer>> transitionMap = function.getLoctrans();
    for (Integer curr : transitionMap.keySet()) {
      HashMap<Boolean, Integer> transitions = transitionMap.get(curr);
      Integer trueDestinationLoc = transitions.get(true);
      Integer falseDestinationLoc = transitions.get(false);
      if (trueDestinationLoc == null && falseDestinationLoc == null) {
        results.add(curr);
      }
    }
    return results;
  }

  /**
   * Returns the line number of the given variable
   * of given loc of given function in the original code.
   *
   * @param function The given function
   * @param variable The given variable
   * @param loc      The given loc
   * @return The line number of the given variable in the original code
   */
  public static int getRealLineNumber(
      Function function, Variable variable, int loc) {
    Variable v = variable.isPrimed()
        ? Variable.createUnprimedClone(variable)
        : variable;
    return function.getExprs(loc, v).getLineNumber();
  }

  /**
   * Returns a variable with correct original line number
   * of the given variable in given location of given function.
   *
   * @param function The given function
   * @param variable The given variable
   * @param loc      The given location
   * @return The variable with correct original line number
   */
  public static Variable getVariableWithCorrectLineNumber(
      Function function, Variable variable, int loc) {
    return new Variable(variable.getUnprimedName(),
        FunctionUtil.getRealLineNumber(function, variable, loc));
  }
}
