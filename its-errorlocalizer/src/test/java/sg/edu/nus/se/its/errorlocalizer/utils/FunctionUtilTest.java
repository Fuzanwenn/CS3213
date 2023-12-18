package sg.edu.nus.se.its.errorlocalizer.utils;

import org.junit.jupiter.api.Test;
import sg.edu.nus.se.its.model.Constant;
import sg.edu.nus.se.its.model.Expression;
import sg.edu.nus.se.its.model.Function;
import sg.edu.nus.se.its.model.Operation;
import sg.edu.nus.se.its.model.Program;
import sg.edu.nus.se.its.model.Variable;
import sg.edu.nus.se.its.util.constants.Constants;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static sg.edu.nus.se.its.errorlocalizer.utils.FunctionUtil.findIncomingLocs;
import static sg.edu.nus.se.its.errorlocalizer.utils.FunctionUtil.findRelativeExpressions;
import static sg.edu.nus.se.its.errorlocalizer.utils.FunctionUtil.getFuncExitLoc;
import static sg.edu.nus.se.its.errorlocalizer.utils.FunctionUtil.getVariableWithCorrectLineNumber;
import static sg.edu.nus.se.its.errorlocalizer.utils.FunctionUtil.hasVariable;
import static sg.edu.nus.se.its.util.constants.Constants.VAR_COND;

/**
 * Test class for FunctionUtil class.
 */
public class FunctionUtilTest {
  private static final Program c1 = ModelProgram.MODEL_C1_C.get();
  private static final Program loop_i = ModelProgram.MODEL_LOOP_I_C.get();
  private static final Program loop_c = ModelProgram.MODEL_LOOP_C_C.get();

  /**
   * Test for getVariableAssigningExpr method.
   */
  @Test
  public void test_getVariableAssigningExpr() {
    Expression result = FunctionUtil
        .getVariableAssigningExpr(loop_c.getFunctionForName(
            Constants.DEFAULT_ENTRY_FUNCTION_NAME), new Variable("i"), 3);
    Operation operation = new Operation("+", List.of(new Variable("i"),
        new Constant("1", 1)), 2);
    assertEquals(result, operation);
  }

  /**
   * Test for findIncomingLocs method.
   */
  @Test
  public void testFindIncomingLocs() {
    Function function = loop_c.getFunctionForName(Constants.DEFAULT_ENTRY_FUNCTION_NAME);

    assertTrue(findIncomingLocs(function, 5).contains(2) && findIncomingLocs(function, 5).size() == 1);
    assertTrue(findIncomingLocs(function, 4).contains(2) && findIncomingLocs(function, 4).size() == 1);
    assertTrue(findIncomingLocs(function, 3).contains(4) && findIncomingLocs(function, 4).size() == 1);
    assertTrue(findIncomingLocs(function, 2).contains(1)
        && findIncomingLocs(function, 2).contains(3)
        && findIncomingLocs(function, 2).size() == 2);
    assertTrue(findIncomingLocs(function, 1).size() == 0);
  }

  /**
   * Test for hasVariable method.
   */
  @Test
  public void testHasVariable() {
    Function function = loop_c.getFunctionForName(Constants.DEFAULT_ENTRY_FUNCTION_NAME);
    assertTrue(hasVariable(function, new Variable("result"), 1));
    assertTrue(hasVariable(function, new Variable("i"), 1));
    assertFalse(hasVariable(function, new Variable(VAR_COND), 1));

    assertTrue(hasVariable(function, new Variable(VAR_COND), 2));
    assertFalse(hasVariable(function, new Variable("i"), 2));

    assertTrue(hasVariable(function, new Variable("i"), 3));
    assertFalse(hasVariable(function, new Variable(VAR_COND), 3));

    assertTrue(hasVariable(function, new Variable("result"), 4));
    assertFalse(hasVariable(function, new Variable("i"), 4));
  }

  /**
   * Test for findRelativeExpressions method when the expression is a constant.
   */
  @Test
  public void testFindRelativeExpressionsConstant() {
    // Returns constant itself
    Function function = loop_c.getFunctionForName(Constants.DEFAULT_ENTRY_FUNCTION_NAME);
    Constant constant = new Constant("2", 1);
    assertEquals(findRelativeExpressions(function, constant, 1), List.of(constant));
  }

  /**
   * Test for findRelativeExpressions method when the expression is of Variable type.
   */
  @Test
  public void testFindRelativeExpressionsVariableUnprimed() {
    // Returns the expression assigned to the variable
    Function function = loop_c.getFunctionForName(Constants.DEFAULT_ENTRY_FUNCTION_NAME);
    Variable variable = new Variable("i", 5);
    Operation operation =
        new Operation("+",
            List.of(new Variable("i", 5),
                new Constant("1", 1)), 5);
    assertEquals(findRelativeExpressions(function, variable, 3), List.of(operation));
  }

  /**
   * Test for findRelativeExpressions method when the expression is of Variable type.
   */
  @Test
  public void testFindRelativeExpressionsVariablePrimed() {
    // Returns the expression assigned to the variable
    Function function = c1.getFunctionForName(Constants.DEFAULT_ENTRY_FUNCTION_NAME);
    Variable variable = new Variable("b", 5);
    Operation operation =
        new Operation("+",
            List.of(new Constant("1", 5),
                new Variable("a", 5).prime()), 5);
    assertEquals(findRelativeExpressions(function, variable, 1), List.of(operation));
  }

  /**
   * Test for findRelativeExpressions method when the expression is of Operation type.
   */
  @Test
  public void testFindRelativeExpressionsOperation() {
    Function function = loop_c.getFunctionForName(Constants.DEFAULT_ENTRY_FUNCTION_NAME);

    Operation operation =
        new Operation("+",
            List.of(new Variable("i", 5),
                new Constant("1", 1)), 5);

    assertEquals(findRelativeExpressions(function, operation, 3),
        List.of(new Variable("i", 5),
        new Constant("1", 1)));

    Operation operation2 =
        new Operation("+",
            List.of(new Constant("1", 5),
                new Variable("a", 5).prime()), 5);
    assertEquals(findRelativeExpressions(function, operation2, 1),
        List.of(new Constant("1", 5),
            new Variable("a", 5).prime()));
  }

  /**
   * Test for getFuncExitLoc method.
   */
  @Test
  public void testGetFuncExitLoc() {
    Function function = loop_c.getFunctionForName(Constants.DEFAULT_ENTRY_FUNCTION_NAME);
    assertTrue(getFuncExitLoc(function).size() == 1
        && getFuncExitLoc(function).get(0).equals(5));
  }

  /**
   * Test for getVariableWithCorrectLineNumber method.
   */
  @Test
  public void testGetVariableWithCorrectLineNumber() {
    Function function = loop_c.getFunctionForName(Constants.DEFAULT_ENTRY_FUNCTION_NAME);
    
    assertEquals(getVariableWithCorrectLineNumber(
        function, new Variable("result"), 1).getLineNumber(), 4);
    assertEquals(getVariableWithCorrectLineNumber(
        function, new Variable("i"), 1).getLineNumber(), 5);
    assertEquals(getVariableWithCorrectLineNumber(
        function, new Variable(VAR_COND), 2).getLineNumber(), 5);
    assertEquals(getVariableWithCorrectLineNumber(
        function, new Variable("i"), 3).getLineNumber(), 5);
    assertEquals(getVariableWithCorrectLineNumber(
        function, new Variable("result"), 4).getLineNumber(), 6);
  }
}
