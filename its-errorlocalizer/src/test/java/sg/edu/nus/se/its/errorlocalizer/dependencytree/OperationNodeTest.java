package sg.edu.nus.se.its.errorlocalizer.dependencytree;

import org.junit.jupiter.api.Test;
import sg.edu.nus.se.its.errorlocalizer.utils.ModelProgram;
import sg.edu.nus.se.its.model.Constant;
import sg.edu.nus.se.its.model.Function;
import sg.edu.nus.se.its.model.Operation;
import sg.edu.nus.se.its.model.Program;
import sg.edu.nus.se.its.model.Variable;
import sg.edu.nus.se.its.util.constants.Constants;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static sg.edu.nus.se.its.errorlocalizer.dependencytree.DependencyNodeTest.IGNORE_NODE_ID;
import static sg.edu.nus.se.its.errorlocalizer.dependencytree.DependencyNodeTest.NO_CHILD;
import static sg.edu.nus.se.its.errorlocalizer.dependencytree.ModelTreeRoot.ARG_POS_FIRST;
import static sg.edu.nus.se.its.errorlocalizer.dependencytree.ModelTreeRoot.ARG_POS_SECOND;

/**
 * Test class for OperationNode class.
 */
public class OperationNodeTest {


  private static final Program PROGRAM_C1 = ModelProgram.MODEL_C1_C.get();
  private static final Function FUNCTION_C1_MAIN =
      PROGRAM_C1.getfnc(Constants.DEFAULT_ENTRY_FUNCTION_NAME);

  /**
   * Test method getFunction, getExpression, getChild methods.
   */
  @Test
  public void testFunctionExpressionChild() {

    Constant refConstant1 = new Constant("0", 4);
    Constant refConstant2 = new Constant("1", 5);

    Variable varA = new Variable("a", 4);
    Variable varB = new Variable("b", 5);
    Variable varAprimed = (Variable) new Variable("a", 5).prime();
    Operation refOpPlus =
        TestUtils.createRefOperation("+", 5, refConstant2, varAprimed);

    VariableNode nodeB =
        new VariableNode(FUNCTION_C1_MAIN, varB, NO_CHILD, 1, IGNORE_NODE_ID);
    OperationNode nodePlus = TestUtils
        .createRefOperationNode(FUNCTION_C1_MAIN, refOpPlus, nodeB, ARG_POS_FIRST, 1, IGNORE_NODE_ID);
    ConstantNode nodeConst2 =
        TestUtils.createRefConstantNode(
            FUNCTION_C1_MAIN, refConstant2, nodePlus, ARG_POS_FIRST, 1, IGNORE_NODE_ID);
    VariableNode nodeA = TestUtils
        .createRefVariableNode(FUNCTION_C1_MAIN, varA, nodePlus, ARG_POS_SECOND, 1, IGNORE_NODE_ID);
    ConstantNode nodeConst1 =
        TestUtils.createRefConstantNode(
            FUNCTION_C1_MAIN, refConstant1, nodeA, ARG_POS_FIRST, 1, IGNORE_NODE_ID);

    OperationNode realOpNode =
        (OperationNode) ModelTreeRoot.getC1Loc1VarB().getDataParents().get(ARG_POS_FIRST).get(0);

    assertEquals(nodePlus.getFunction(), FUNCTION_C1_MAIN);
    assertEquals(nodePlus.getExpression(), realOpNode.getExpression());
    assertEquals(nodePlus.getChild(), realOpNode.getChild());
    assertEquals(nodePlus, realOpNode);
    assertEquals(realOpNode.getExpression(), refOpPlus);
  }

  /**
   * Test method getFirstChildVar.
   */
  @Test
  public void testGetFirstChildVar1() {
    OperationNode c1OpPlusRootVarB =
        (OperationNode) ModelTreeRoot.getC1Loc1VarB().getDataParents().get(ARG_POS_FIRST).get(0);

    Variable variable = new Variable("b");
    assertEquals(variable,
        c1OpPlusRootVarB.getFirstChildVar());
  }

  /**
   * Test method getFirstChildVar.
   */
  @Test
  public void testGetFirstChildVar2() {
    Constant refConstant2 = new Constant("1", 5);
    Variable varAprimed = (Variable) new Variable("a", 5).prime();
    Operation refOpPlus =
        TestUtils.createRefOperation("+", 5, refConstant2, varAprimed);
    OperationNode operationNode =
        TestUtils.createRefOperationNode(
            FUNCTION_C1_MAIN, refOpPlus, NO_CHILD, ARG_POS_FIRST, 1, IGNORE_NODE_ID);

    assertNull(operationNode.getFirstChildVar());
  }

  /**
   * Test method hasSameExpression.
   */
  @Test
  public void testHasSameExpression() {
    Constant arg1 = new Constant("1", 1);
    Constant arg2 = new Constant("2", 2);
    Constant arg3 = new Constant("3", 1);
    Constant arg4 = new Constant("4", 2);
    Constant arg5 = new Constant("5", 1);

    Operation operation1 = TestUtils.createRefOperation("+", 1, arg1, arg2);
    Operation operation2 = TestUtils.createRefOperation("+", 1, arg3, arg4);
    Operation operation3 = TestUtils.createRefOperation("-", 1, arg4, arg5);

    ConstantNode constantNode =
        TestUtils.createRefConstantNode(
            FUNCTION_C1_MAIN, arg1, NO_CHILD, ARG_POS_FIRST, 1, IGNORE_NODE_ID);

    VariableNode variableNode =
        TestUtils.createRefVariableNode(
            FUNCTION_C1_MAIN, new Variable("a"), NO_CHILD, ARG_POS_FIRST, 1, IGNORE_NODE_ID);

    OperationNode operationNode1 =
        TestUtils.createRefOperationNode(
            FUNCTION_C1_MAIN, operation1, NO_CHILD, ARG_POS_FIRST, 1, IGNORE_NODE_ID);
    OperationNode operationNode2 =
        TestUtils.createRefOperationNode(
            FUNCTION_C1_MAIN, operation2, NO_CHILD, ARG_POS_FIRST, 1, IGNORE_NODE_ID);
    OperationNode operationNode3 =
        TestUtils.createRefOperationNode(
            FUNCTION_C1_MAIN, operation3, NO_CHILD, ARG_POS_FIRST, 1, IGNORE_NODE_ID);

    assertTrue(operationNode1.hasSameExpression(operationNode2, null));
    assertTrue(operationNode1.hasSameExpression(operationNode1, null));
    assertFalse(operationNode1.hasSameExpression(operationNode3, null));
    assertFalse(operationNode1.hasSameExpression(constantNode, null));
    assertFalse(operationNode1.hasSameExpression(variableNode, null));
  }

  /**
   * Test method hasSameExpressionAndLineNumber.
   */
  @Test
  public void testHasSameExpressionAndLineNumber() {
    Constant arg1 = new Constant("1", 1);
    Constant arg2 = new Constant("2", 2);
    Constant arg3 = new Constant("3", 1);
    Constant arg4 = new Constant("4", 2);
    Constant arg5 = new Constant("5", 1);

    Operation operation1 = TestUtils.createRefOperation("+", 1, arg1, arg2);
    Operation operation2 = TestUtils.createRefOperation("+", 1, arg1, arg2);
    Operation operation3 = TestUtils.createRefOperation("+", 2, arg1, arg2);
    Operation operation4 = TestUtils.createRefOperation("-", 1, arg4, arg5);
    Operation operation5 = TestUtils.createRefOperation("+", 1, arg2, arg3);

    ConstantNode constantNode =
        TestUtils.createRefConstantNode(
            FUNCTION_C1_MAIN, arg1, NO_CHILD, ARG_POS_FIRST, 1, IGNORE_NODE_ID);

    VariableNode variableNode =
        TestUtils.createRefVariableNode(
            FUNCTION_C1_MAIN, new Variable("a"), NO_CHILD, ARG_POS_FIRST, 1, IGNORE_NODE_ID);

    OperationNode operationNode1 =
        TestUtils.createRefOperationNode(
            FUNCTION_C1_MAIN, operation1, NO_CHILD, ARG_POS_FIRST, 1, IGNORE_NODE_ID);
    OperationNode operationNode2 =
        TestUtils.createRefOperationNode(
            FUNCTION_C1_MAIN, operation2, NO_CHILD, ARG_POS_FIRST, 1, IGNORE_NODE_ID);
    OperationNode operationNode3 =
        TestUtils.createRefOperationNode(
            FUNCTION_C1_MAIN, operation3, NO_CHILD, ARG_POS_FIRST, 1, IGNORE_NODE_ID);
    OperationNode operationNode4 =
        TestUtils.createRefOperationNode(
            FUNCTION_C1_MAIN, operation4, NO_CHILD, ARG_POS_FIRST,1, IGNORE_NODE_ID);
    OperationNode operationNode5 =
        TestUtils.createRefOperationNode(
            FUNCTION_C1_MAIN, operation5, NO_CHILD, ARG_POS_FIRST, 1, IGNORE_NODE_ID);
    OperationNode operationNode6 =
        TestUtils.createRefOperationNode(
            FUNCTION_C1_MAIN, operation1, NO_CHILD, ARG_POS_FIRST, 2, IGNORE_NODE_ID);

    assertTrue(operationNode1.hasSameExpressionAndLineNumber(operationNode2));
    assertTrue(operationNode1.hasSameExpressionAndLineNumber(operationNode1));
    assertFalse(operationNode1.hasSameExpressionAndLineNumber(operationNode3));
    assertFalse(operationNode1.hasSameExpressionAndLineNumber(operationNode4));
    assertFalse(operationNode1.hasSameExpressionAndLineNumber(operationNode5));
    assertFalse(operationNode1.hasSameExpressionAndLineNumber(operationNode6));
    assertFalse(operationNode1.hasSameExpressionAndLineNumber(constantNode));
    assertFalse(operationNode1.hasSameExpressionAndLineNumber(variableNode));
  }
}
