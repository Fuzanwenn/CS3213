package sg.edu.nus.se.its.errorlocalizer.utils;

import org.junit.jupiter.api.Test;
import sg.edu.nus.se.its.alignment.VariableMapping;
import sg.edu.nus.se.its.model.Constant;
import sg.edu.nus.se.its.model.Function;
import sg.edu.nus.se.its.model.Operation;
import sg.edu.nus.se.its.model.Program;
import sg.edu.nus.se.its.model.Variable;
import sg.edu.nus.se.its.util.constants.Constants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static sg.edu.nus.se.its.util.constants.Constants.VAR_COND;

/**
 * Test class for ExpressionUtil class.
 */
public class ExpressionUtilTest {

  /**
   * Test for getCalledFunc method.
   */
  @Test
  public void testGetCalledFunc() {
    Operation operation = new Operation(Constants.FUNCTION_CALL,
        List.of(new Constant("echo", 1), new Constant("3", 1)),
        1);
  }

  /**
   * Test for getCalledFuncArgs method.
   */
  @Test
  public void testGetCalledFuncArgs() {
    Operation operation = new Operation(Constants.FUNCTION_CALL,
        List.of(new Constant("echo", 1),
            new Constant("3", 1),
            new Constant("4", 2),
            new Constant("5", 1)),
        1);

    assertEquals(
        ExpressionUtil.getCalledFuncArgs(operation),
        List.of(new Constant("3", 1),
            new Constant("4", 2),
            new Constant("5", 1)));
  }

  /**
   * Test for isFuncCall method.
   */
  @Test
  public void testIsFuncCall() {
    Operation operation1 = new Operation(Constants.FUNCTION_CALL,
        List.of(new Constant("echo", 1), new Constant("3", 1)),
        1);
    Operation operation2 = new Operation("+",
        List.of(new Constant("2", 1), new Constant("3", 1)),
        1);

    assertTrue(ExpressionUtil.isFuncCall(operation1));
    assertFalse(ExpressionUtil.isFuncCall(operation2));
  }

  /**
   * Test for areArgsOrdered method.
   */
  @Test
  public void testAreArgsOrdered() {
    Operation operation1 = new Operation(Constants.FUNCTION_CALL,
        List.of(new Constant("echo", 1), new Constant("3", 1)),
        1);
    Operation operation2 = new Operation("+",
        List.of(new Constant("3", 1), new Constant("3", 1)),
        1);
    Operation operation3 = new Operation("-",
        List.of(new Constant("3", 1), new Constant("3", 1)),
        1);
    Operation operation4 = new Operation("*",
        List.of(new Constant("3", 1), new Constant("3", 1)),
        1);
    Operation operation5 = new Operation("/",
        List.of(new Constant("3", 1), new Constant("3", 1)),
        1);
    Operation operation6 = new Operation("==",
        List.of(new Constant("3", 1), new Constant("3", 1)),
        1);
    Operation operation7 = new Operation("!=",
        List.of(new Constant("3", 1), new Constant("3", 1)),
        1);
    Operation operation8 = new Operation(">",
        List.of(new Constant("3", 1), new Constant("3", 1)),
        1);
    Operation operation9 = new Operation("<",
        List.of(new Constant("3", 1), new Constant("3", 1)),
        1);
    Operation operation10 = new Operation(">=",
        List.of(new Constant("3", 1), new Constant("3", 1)),
        1);
    Operation operation11 = new Operation("<=",
        List.of(new Constant("3", 1), new Constant("3", 1)),
        1);
    Operation operation12 = new Operation("&&",
        List.of(new Constant("3", 1), new Constant("3", 1)),
        1);
    Operation operation13 = new Operation("||",
        List.of(new Constant("3", 1), new Constant("3", 1)),
        1);

    assertTrue(ExpressionUtil.areArgsOrdered(operation1));
    assertFalse(ExpressionUtil.areArgsOrdered(operation2));
    assertTrue(ExpressionUtil.areArgsOrdered(operation3));
    assertFalse(ExpressionUtil.areArgsOrdered(operation4));
    assertTrue(ExpressionUtil.areArgsOrdered(operation5));
    assertFalse(ExpressionUtil.areArgsOrdered(operation6));
    assertFalse(ExpressionUtil.areArgsOrdered(operation7));
    assertTrue(ExpressionUtil.areArgsOrdered(operation8));
    assertTrue(ExpressionUtil.areArgsOrdered(operation9));
    assertTrue(ExpressionUtil.areArgsOrdered(operation10));
    assertTrue(ExpressionUtil.areArgsOrdered(operation11));
    assertFalse(ExpressionUtil.areArgsOrdered(operation12));
    assertFalse(ExpressionUtil.areArgsOrdered(operation13));
  }

  /**
   * Test for getOppositeOperation method.
   */
  @Test
  public void testGetOppositeOperation() {
    Operation operation1 = new Operation(Constants.FUNCTION_CALL,
        List.of(new Constant("echo", 1), new Constant("3", 1)),
        1);
    Operation operation2 = new Operation("+",
        List.of(new Constant("3", 1), new Constant("3", 1)),
        1);
    Operation operation3 = new Operation("-",
        List.of(new Constant("3", 1), new Constant("3", 1)),
        1);
    Operation operation4 = new Operation("*",
        List.of(new Constant("3", 1), new Constant("3", 1)),
        1);
    Operation operation5 = new Operation("/",
        List.of(new Constant("3", 1), new Constant("3", 1)),
        1);
    Operation operation6 = new Operation("==",
        List.of(new Constant("3", 1), new Constant("3", 1)),
        1);
    Operation operation7 = new Operation("!=",
        List.of(new Constant("3", 1), new Constant("3", 1)),
        1);
    Operation operation8 = new Operation(">",
        List.of(new Constant("3", 1), new Constant("3", 1)),
        1);
    Operation operation9 = new Operation("<",
        List.of(new Constant("3", 1), new Constant("3", 1)),
        1);
    Operation operation10 = new Operation(">=",
        List.of(new Constant("3", 1), new Constant("3", 1)),
        1);
    Operation operation11 = new Operation("<=",
        List.of(new Constant("3", 1), new Constant("3", 1)),
        1);
    Operation operation12 = new Operation("&&",
        List.of(new Constant("3", 1), new Constant("3", 1)),
        1);
    Operation operation13 = new Operation("||",
        List.of(new Constant("3", 1), new Constant("3", 1)),
        1);

    assertNull(ExpressionUtil.getSymmetricBinaryOperationName(operation1));
    assertEquals(ExpressionUtil.getSymmetricBinaryOperationName(operation2), "+");
    assertNull(ExpressionUtil.getSymmetricBinaryOperationName(operation3));
    assertEquals(ExpressionUtil.getSymmetricBinaryOperationName(operation4), "*");
    assertNull(ExpressionUtil.getSymmetricBinaryOperationName(operation5));
    assertEquals(ExpressionUtil.getSymmetricBinaryOperationName(operation6), "==");
    assertEquals(ExpressionUtil.getSymmetricBinaryOperationName(operation7), "!=");
    assertEquals(ExpressionUtil.getSymmetricBinaryOperationName(operation8), "<");
    assertEquals(ExpressionUtil.getSymmetricBinaryOperationName(operation9), ">");
    assertEquals(ExpressionUtil.getSymmetricBinaryOperationName(operation10), "<=");
    assertEquals(ExpressionUtil.getSymmetricBinaryOperationName(operation11), ">=");
    assertEquals(ExpressionUtil.getSymmetricBinaryOperationName(operation12), "&&");
    assertEquals(ExpressionUtil.getSymmetricBinaryOperationName(operation13), "||");
  }

  /**
   * Test for isControllingVar method.
   */
  @Test
  public void testIsControllingVar() {
    Variable variable1 = new Variable(VAR_COND);
    Variable variable2 = new Variable("a");
    Constant constant = new Constant("1111", 1);
    Operation operation1 = new Operation(Constants.FUNCTION_CALL,
        List.of(new Constant("echo", 1), new Constant("3", 1)),
        1);


    assertTrue(ExpressionUtil.isControllingVar(variable1));
    assertFalse(ExpressionUtil.isControllingVar(variable2));
    assertFalse(ExpressionUtil.isControllingVar(constant));
    assertFalse(ExpressionUtil.isControllingVar(operation1));
  }

  /**
   * Test for areVarMapped method.
   */
  @Test
  public void testAreVarMapped() {
    Function FUNCTION_C1_MAIN =
        ModelProgram.MODEL_C1_C.get().getFunctionForName(Constants.DEFAULT_ENTRY_FUNCTION_NAME);
    Map<Variable, Variable> varMapping = new HashMap<>();

    String leftVarName1 = "x";
    String leftVarName2 = "y";
    String leftVarName3 = "he";

    String rightVarName1 = "a";
    String rightVarName2 = "b";
    String rightVarName3 = "llo";

    varMapping.put(new Variable(leftVarName1), new Variable(rightVarName1));
    varMapping.put(new Variable(leftVarName2), new Variable(rightVarName2));
    varMapping.put(new Variable(leftVarName3), new Variable(rightVarName3));

    VariableMapping variableMapping = new VariableMapping();
    variableMapping.add(Constants.DEFAULT_ENTRY_FUNCTION_NAME, varMapping);

    Variable leftVar11 = new Variable(leftVarName1, 1);
    Variable leftVar13 = new Variable(leftVarName1, 2);
    Variable leftVar21 = new Variable(leftVarName2, 1);
    Variable leftVar23 = new Variable(leftVarName2, 2);
    Variable leftVar31 = new Variable(leftVarName3, 1);
    Variable leftVar33 = new Variable(leftVarName3, 2);

    Variable rightVar11 = new Variable(rightVarName1, 9);
    Variable rightVar13 = new Variable(rightVarName1, 10);
    Variable rightVar21 = new Variable(rightVarName2, 9);
    Variable rightVar23 = new Variable(rightVarName2, 10);
    Variable rightVar31 = new Variable(rightVarName3, 9);
    Variable rightVar33 = new Variable(rightVarName3, 10);

    assertTrue(
        ExpressionUtil.areMappedVariable(leftVar11, rightVar11, variableMapping, FUNCTION_C1_MAIN));
    assertTrue(
        ExpressionUtil.areMappedVariable(leftVar21, rightVar21, variableMapping, FUNCTION_C1_MAIN));
    assertTrue(
        ExpressionUtil.areMappedVariable(leftVar31, rightVar31, variableMapping, FUNCTION_C1_MAIN));

    assertTrue(
        ExpressionUtil.areMappedVariable(leftVar13, rightVar11, variableMapping, FUNCTION_C1_MAIN));
    assertTrue(
        ExpressionUtil.areMappedVariable(leftVar23, rightVar21, variableMapping, FUNCTION_C1_MAIN));
    assertTrue(
        ExpressionUtil.areMappedVariable(leftVar33, rightVar31, variableMapping, FUNCTION_C1_MAIN));

    assertFalse(
        ExpressionUtil.areMappedVariable(leftVar13, rightVar23, variableMapping, FUNCTION_C1_MAIN));
    assertFalse(
        ExpressionUtil.areMappedVariable(leftVar23, rightVar33, variableMapping, FUNCTION_C1_MAIN));
    assertFalse(
        ExpressionUtil.areMappedVariable(leftVar33, rightVar13, variableMapping, FUNCTION_C1_MAIN));

    assertFalse(
        ExpressionUtil.areMappedVariable(rightVar11, leftVar11, variableMapping, FUNCTION_C1_MAIN));
    assertFalse(
        ExpressionUtil.areMappedVariable(rightVar21, leftVar21, variableMapping, FUNCTION_C1_MAIN));
    assertFalse(
        ExpressionUtil.areMappedVariable(rightVar31, leftVar31, variableMapping, FUNCTION_C1_MAIN));
  }

  private VariableMapping getVariableMapping() {
    Map<Variable, Variable> varMapping = new HashMap<>();
    varMapping.put(new Variable(Constants.VAR_COND), new Variable(Constants.VAR_COND));
    varMapping.put(new Variable(Constants.VAR_RET), new Variable(Constants.VAR_RET));
    varMapping.put(new Variable(Constants.VAR_OUT), new Variable(Constants.VAR_OUT));
    varMapping.put(new Variable(Constants.VAR_IN), new Variable(Constants.VAR_IN));
    varMapping.put(new Variable("N"), new Variable("N"));
    varMapping.put(new Variable("width"), new Variable("w"));
    varMapping.put(new Variable("height"), new Variable("h"));
    varMapping.put(new Variable("i"), new Variable("b"));
    varMapping.put(new Variable("j"), new Variable("a"));

    VariableMapping variableAlignmentResult = new VariableMapping();
    variableAlignmentResult.add(Constants.DEFAULT_ENTRY_FUNCTION_NAME, varMapping);
    return variableAlignmentResult;
  }

  private Program getSubmittedProgram() {
    Program submittedProgram = ModelProgram.MODEL_TEST1_B_C.get();
    return submittedProgram;
  }

  /**
   * Test is two variable mappings are the same.
   */
  @Test
  public void testSameVariableMappings() {
    Function function = getSubmittedProgram().getfnc(Constants.DEFAULT_ENTRY_FUNCTION_NAME);

    Variable variableN1 = new Variable("N");
    Variable variableN2 = new Variable("N");
    assertTrue(ExpressionUtil.areMappedVariable(variableN1, variableN2, getVariableMapping(), function));

    Variable variableI = new Variable("i");
    Variable variableB = new Variable("b");
    assertTrue(ExpressionUtil.areMappedVariable(variableI, variableB, getVariableMapping(), function));

    Variable variableJ = new Variable("j");
    Variable variableA = new Variable("a");
    assertTrue(ExpressionUtil.areMappedVariable(variableJ, variableA, getVariableMapping(), function));

    Variable variableWidth = new Variable("width");
    Variable variableW = new Variable("w");
    assertTrue(ExpressionUtil.areMappedVariable(variableWidth, variableW, getVariableMapping(), function));

    Variable variableHeight = new Variable("height");
    Variable variableH = new Variable("h");
    assertTrue(ExpressionUtil.areMappedVariable(variableHeight, variableH, getVariableMapping(), function));
  }

  @Test
  public void testGetSymmetricBinaryOperation() {
    Operation pyGtOp = new Operation("Gt",
        List.of(new Constant("1", 5), new Constant("2", 5)),
        5);

    Operation pyFuncCallOp = new Operation("FuncCall",
        List.of(new Constant("foo", 5),
            new Constant("2", 5), new Constant("3", 5)),
        5);

    assertEquals(ExpressionUtil.getSymmetricBinaryOperation(pyGtOp).getName(), "Lt");
    assertThrows(RuntimeException.class,
        () -> ExpressionUtil.getSymmetricBinaryOperation(pyFuncCallOp));
  }

  @Test
  public void testIsCommutative() {
    Operation pyGtOp = new Operation("Gt",
        List.of(new Constant("1", 5), new Constant("2", 5)),
        5);

    Operation pyFuncCallOp = new Operation("FuncCall",
        List.of(new Constant("foo", 5),
            new Constant("2", 5), new Constant("3", 5)),
        5);
    assertTrue(ExpressionUtil.isCommutative(pyGtOp));
    assertFalse(ExpressionUtil.isCommutative(pyFuncCallOp));
  }
}
