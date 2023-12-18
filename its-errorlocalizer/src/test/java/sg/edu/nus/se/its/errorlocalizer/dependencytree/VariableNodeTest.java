package sg.edu.nus.se.its.errorlocalizer.dependencytree;

import org.junit.jupiter.api.Test;
import sg.edu.nus.se.its.alignment.VariableMapping;
import sg.edu.nus.se.its.errorlocalizer.utils.ModelProgram;
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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static sg.edu.nus.se.its.errorlocalizer.dependencytree.DependencyNodeTest.IGNORE_NODE_ID;
import static sg.edu.nus.se.its.errorlocalizer.dependencytree.DependencyNodeTest.NO_CHILD;
import static sg.edu.nus.se.its.errorlocalizer.dependencytree.ModelTreeRoot.ARG_POS_FIRST;
import static sg.edu.nus.se.its.util.constants.Constants.VAR_RET;

/**
 * Test class for VariableNode class.
 */
public class VariableNodeTest {
  private static final Program PROGRAM_C1 = ModelProgram.MODEL_C1_C.get();
  private static final Function FUNCTION_C1_MAIN =
      PROGRAM_C1.getfnc(Constants.DEFAULT_ENTRY_FUNCTION_NAME);

  /**
   * Test for getFunction, getExpression, getChild methods.
   */
  @Test
  public void testGetFunctionExpressionChild() {

    Variable refVarA = new Variable("a", 4);
    Variable refVarB = new Variable("b", 5);
    Variable refVarRet = new Variable(VAR_RET, 6);

    Variable realVarA = new Variable(FUNCTION_C1_MAIN.getExprs(1).get(0).getValue0(),
        FUNCTION_C1_MAIN.getExprs(1).get(0).getValue1().getLineNumber());
    Variable realVarB = new Variable(FUNCTION_C1_MAIN.getExprs(1).get(1).getValue0(),
        FUNCTION_C1_MAIN.getExprs(1).get(1).getValue1().getLineNumber());
    Variable realVarRet = new Variable(FUNCTION_C1_MAIN.getExprs(1).get(2).getValue0(),
        FUNCTION_C1_MAIN.getExprs(1).get(2).getValue1().getLineNumber());

    VariableNode refVarNodeB =
        TestUtils.createRefVariableNode(
            FUNCTION_C1_MAIN, refVarB, NO_CHILD, ARG_POS_FIRST, 1, IGNORE_NODE_ID);
    VariableNode refVarNodeA =
        TestUtils.createRefVariableNode(
            FUNCTION_C1_MAIN, refVarA, refVarNodeB, ARG_POS_FIRST, 1, IGNORE_NODE_ID);
    VariableNode refVarNodeRet =
        TestUtils.createRefVariableNode(
            FUNCTION_C1_MAIN, refVarRet, NO_CHILD, ARG_POS_FIRST, 1, IGNORE_NODE_ID);


    assertEquals(refVarNodeA.getFunction(), FUNCTION_C1_MAIN);
    assertEquals(refVarNodeB.getFunction(), FUNCTION_C1_MAIN);
    assertEquals(refVarNodeRet.getFunction(), FUNCTION_C1_MAIN);
    assertEquals(refVarNodeA.getExpression(), realVarA);
    assertEquals(refVarNodeB.getExpression(), realVarB);
    assertEquals(refVarNodeRet.getExpression(), realVarRet);
    assertEquals(refVarNodeA.getChild(), refVarNodeB);
  }

  /**
   * Test for getFirstChildVar method.
   */
  @Test
  public void testGetFirstChildVar() {
    VariableNode c1VarARootedLoc1VarA = (VariableNode) ModelTreeRoot.getC1Loc1VarA();
    assertEquals(c1VarARootedLoc1VarA.getFirstChildVar(), c1VarARootedLoc1VarA.getExpression());
  }

  /**
   * Test for hasSameExpression method.
   */
  @Test
  public void testHasSameExpression() {
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
    
    VariableNode leftNode11 =
        new VariableNode(FUNCTION_C1_MAIN, leftVar11, NO_CHILD, 1, IGNORE_NODE_ID);
    VariableNode leftNode13 =
        new VariableNode(FUNCTION_C1_MAIN, leftVar13, NO_CHILD, 1, IGNORE_NODE_ID);
    VariableNode leftNode21 =
        new VariableNode(FUNCTION_C1_MAIN, leftVar21, NO_CHILD, 1, IGNORE_NODE_ID);
    VariableNode leftNode23 =
        new VariableNode(FUNCTION_C1_MAIN, leftVar23, NO_CHILD, 1, IGNORE_NODE_ID);
    VariableNode leftNode31 =
        new VariableNode(FUNCTION_C1_MAIN, leftVar31, NO_CHILD, 1, IGNORE_NODE_ID);
    VariableNode leftNode33 =
        new VariableNode(FUNCTION_C1_MAIN, leftVar33, NO_CHILD, 1, IGNORE_NODE_ID);

    VariableNode rightNode11 =
        new VariableNode(FUNCTION_C1_MAIN, rightVar11, NO_CHILD, 1, IGNORE_NODE_ID);
    VariableNode rightNode13 =
        new VariableNode(FUNCTION_C1_MAIN, rightVar13, NO_CHILD, 1, IGNORE_NODE_ID);
    VariableNode rightNode21 =
        new VariableNode(FUNCTION_C1_MAIN, rightVar21, NO_CHILD, 1, IGNORE_NODE_ID);
    VariableNode rightNode23 =
        new VariableNode(FUNCTION_C1_MAIN, rightVar23, NO_CHILD, 1, IGNORE_NODE_ID);
    VariableNode rightNode31 =
        new VariableNode(FUNCTION_C1_MAIN, rightVar31, NO_CHILD, 1, IGNORE_NODE_ID);
    VariableNode rightNode33 =
        new VariableNode(FUNCTION_C1_MAIN, rightVar33, NO_CHILD, 1, IGNORE_NODE_ID);

    ConstantNode constantNode =
        new ConstantNode(FUNCTION_C1_MAIN,
            new Constant("1", 1), NO_CHILD, 1, IGNORE_NODE_ID);

    OperationNode operationNode = new OperationNode(FUNCTION_C1_MAIN,
        new Operation("+", List.of(new Constant("1", 1),
            new Constant("2", 1)), 1),
        NO_CHILD, 1, IGNORE_NODE_ID);

    assertFalse(leftNode11.hasSameExpression(operationNode, variableMapping));
    assertFalse(leftNode11.hasSameExpression(constantNode, variableMapping));

    assertTrue(leftNode11.hasSameExpression(rightNode11, variableMapping));
    assertTrue(leftNode21.hasSameExpression(rightNode21, variableMapping));
    assertTrue(leftNode31.hasSameExpression(rightNode31, variableMapping));

    assertTrue(leftNode13.hasSameExpression(rightNode11, variableMapping));
    assertTrue(leftNode23.hasSameExpression(rightNode21, variableMapping));
    assertTrue(leftNode33.hasSameExpression(rightNode31, variableMapping));

    assertFalse(leftNode13.hasSameExpression(rightNode21, variableMapping));
    assertFalse(leftNode23.hasSameExpression(rightNode31, variableMapping));
    assertFalse(leftNode33.hasSameExpression(rightNode11, variableMapping));
  }

  /**
   * Test for hasSameExpressionAndLineNumber method.
   */
  @Test
  public void testHasSameExpressionAndLineNumber() {
    Map<Variable, Variable> varMapping = new HashMap<>();

    String leftVarName1 = "x";
    String leftVarName2 = "y";

    Variable leftVar11 = new Variable(leftVarName1, 1);
    Variable leftVar12 = new Variable(leftVarName1, 1);
    Variable leftVar13 = new Variable(leftVarName1, 3);
    Variable leftVar21 = new Variable(leftVarName2, 1);
    Variable leftVar22 = new Variable(leftVarName2, 1);
    Variable leftVar23 = new Variable(leftVarName2, 2);

    VariableNode leftNode11 =
        new VariableNode(FUNCTION_C1_MAIN, leftVar11, NO_CHILD, 1, IGNORE_NODE_ID);
    VariableNode leftNode12 =
        new VariableNode(FUNCTION_C1_MAIN, leftVar12, NO_CHILD, 1, IGNORE_NODE_ID);
    VariableNode leftNode13 =
        new VariableNode(FUNCTION_C1_MAIN, leftVar13, NO_CHILD, 1, IGNORE_NODE_ID);
    VariableNode leftNode14 =
        new VariableNode(FUNCTION_C1_MAIN, leftVar13, NO_CHILD, 2, IGNORE_NODE_ID);
    VariableNode leftNode21 =
        new VariableNode(FUNCTION_C1_MAIN, leftVar21, NO_CHILD, 1, IGNORE_NODE_ID);
    VariableNode leftNode22 =
        new VariableNode(FUNCTION_C1_MAIN, leftVar22, NO_CHILD, 1, IGNORE_NODE_ID);
    VariableNode leftNode23 =
        new VariableNode(FUNCTION_C1_MAIN, leftVar23, NO_CHILD, 1, IGNORE_NODE_ID);

    ConstantNode constantNode =
        new ConstantNode(FUNCTION_C1_MAIN,
            new Constant("1", 1), NO_CHILD, 1, IGNORE_NODE_ID);

    OperationNode operationNode = new OperationNode(FUNCTION_C1_MAIN,
        new Operation("+", List.of(new Constant("1", 1),
            new Constant("2", 1)), 1),
        NO_CHILD, 1, IGNORE_NODE_ID);

    assertFalse(leftNode11.hasSameExpressionAndLineNumber(operationNode));
    assertFalse(leftNode11.hasSameExpressionAndLineNumber(constantNode));

    assertTrue(leftNode11.hasSameExpressionAndLineNumber(leftNode12));
    assertTrue(leftNode21.hasSameExpressionAndLineNumber(leftNode22));

    assertFalse(leftNode13.hasSameExpressionAndLineNumber(leftNode11));
    assertFalse(leftNode11.hasSameExpressionAndLineNumber(leftNode14));
    assertFalse(leftNode23.hasSameExpressionAndLineNumber(leftNode21));
  }
}
