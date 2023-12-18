package sg.edu.nus.se.its.errorlocalizer.dependencytree;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static sg.edu.nus.se.its.errorlocalizer.dependencytree.DependencyNodeTest.IGNORE_NODE_ID;
import static sg.edu.nus.se.its.errorlocalizer.dependencytree.DependencyNodeTest.NO_CHILD;
import static sg.edu.nus.se.its.errorlocalizer.dependencytree.ModelTreeRoot.ARG_POS_FIRST;
import static sg.edu.nus.se.its.errorlocalizer.dependencytree.ModelTreeRoot.ARG_POS_SECOND;

import org.junit.jupiter.api.Test;
import sg.edu.nus.se.its.errorlocalizer.utils.ModelProgram;
import sg.edu.nus.se.its.model.Constant;
import sg.edu.nus.se.its.model.Function;
import sg.edu.nus.se.its.model.Operation;
import sg.edu.nus.se.its.model.Program;
import sg.edu.nus.se.its.model.Variable;
import sg.edu.nus.se.its.util.constants.Constants;

/**
 * This test is to test the ConstantNode class.
 */
public class ConstantNodeTest {
  private static final Program PROGRAM_C1 = ModelProgram.MODEL_C1_C.get();
  private static final Function FUNCTION_C1_MAIN =
      PROGRAM_C1.getfnc(Constants.DEFAULT_ENTRY_FUNCTION_NAME);

  /**
   * Test the getFunction, getExpression, getChild method.
   */
  @Test
  public void testGetFunctionExpressionChild() {
    Constant refConstant1 = new Constant("0", 4);
    Constant refConstant2 = new Constant("1", 5);
    Constant refConstant3 = new Constant("0", 6);

    Constant realConstant1 = (Constant) FUNCTION_C1_MAIN.getExprs(1).get(0).getValue1();
    Constant realConstant2 =
        (Constant) ((Operation) FUNCTION_C1_MAIN
            .getExprs(1).get(1).getValue1()).getArgs().get(0);
    Constant realConstant3 = (Constant) FUNCTION_C1_MAIN.getExprs(1).get(2).getValue1();

    Variable varB = new Variable("b", 5);
    VariableNode nodeB = new VariableNode(FUNCTION_C1_MAIN, varB, NO_CHILD, 1, IGNORE_NODE_ID);

    ConstantNode node1 =
        new ConstantNode(FUNCTION_C1_MAIN, realConstant1, nodeB, 1, IGNORE_NODE_ID);
    ConstantNode node2 =
        new ConstantNode(FUNCTION_C1_MAIN, realConstant2, nodeB, 1, IGNORE_NODE_ID);
    ConstantNode node3 =
        new ConstantNode(FUNCTION_C1_MAIN, realConstant3, NO_CHILD, 1, IGNORE_NODE_ID);

    assertEquals(node1.getFunction(), FUNCTION_C1_MAIN);
    assertEquals(node2.getFunction(), FUNCTION_C1_MAIN);
    assertEquals(node3.getFunction(), FUNCTION_C1_MAIN);
    assertEquals(node1.getExpression(), refConstant1);
    assertEquals(node2.getExpression(), refConstant2);
    assertEquals(node3.getExpression(), refConstant3);

    assertEquals(node1.getChild(), node2.getChild());
  }

  /**
   * Test for the getFirstChildVar method.
   */
  @Test
  public void testGetFirstChildVar1() {
    ConstantNode c1Const1RootedLoc1VarA =
        (ConstantNode) ModelTreeRoot.getC1Loc1VarA().getDataParents().get(ARG_POS_FIRST).get(0);

    assertEquals(ModelTreeRoot.getC1Loc1VarA().getExpression(),
        c1Const1RootedLoc1VarA.getFirstChildVar());
  }

  @Test
  public void testGetFirstChildVar2() {
    ConstantNode c1Const1RootLoc1VarA =
        (ConstantNode) ModelTreeRoot.getC1Loc1VarA().getDataParents().get(ARG_POS_FIRST).get(0);

    //  From  "b := +(1, a')", get "b", get "+(1, a')", get "a'" recursively.
    DependencyNode c1VarARootLoc1VarB = ModelTreeRoot.getC1Loc1VarB()
        .getDataParents().get(ARG_POS_FIRST).get(0).getDataParents().get(ARG_POS_SECOND).get(0);

    assertEquals(c1VarARootLoc1VarB.getExpression(),
        c1Const1RootLoc1VarA.getFirstChildVar());
  }

  /**
   * Test for the method haveSameExpression.
   */
  @Test
  public void testHaveSameExpression() {

    Constant refConstant1 = new Constant("0", 4);
    Constant refConstant2 = new Constant("1", 4);
    Constant refConstant3 = new Constant("0", 6);
    Constant refConstant4 = new Constant("1", 5);

    Variable varB = new Variable("b", 5);
    VariableNode nodeB = new VariableNode(FUNCTION_C1_MAIN, varB, NO_CHILD, 1, IGNORE_NODE_ID);

    Operation opPlus = TestUtils.createRefOperation("+", 5, refConstant2, refConstant1);
    OperationNode nodeOp = new OperationNode(FUNCTION_C1_MAIN, opPlus, NO_CHILD, 1, IGNORE_NODE_ID);

    ConstantNode node1 =
        new ConstantNode(FUNCTION_C1_MAIN, refConstant1, nodeB, 1, IGNORE_NODE_ID);
    ConstantNode node2 =
        new ConstantNode(FUNCTION_C1_MAIN, refConstant2, nodeB, 1, IGNORE_NODE_ID);
    ConstantNode node3 =
        new ConstantNode(FUNCTION_C1_MAIN, refConstant3, NO_CHILD, 1, IGNORE_NODE_ID);
    ConstantNode node4 =
        new ConstantNode(FUNCTION_C1_MAIN, refConstant4, NO_CHILD, 1, IGNORE_NODE_ID);

    // Variable mapping does not matter constant node
    assertTrue(node1.hasSameExpression(node1, null));
    assertTrue(node2.hasSameExpression(node2, null));
    assertTrue(node3.hasSameExpression(node3, null));
    assertTrue(node4.hasSameExpression(node4, null));

    assertTrue(node1.hasSameExpression(node3, null));
    assertTrue(node3.hasSameExpression(node1, null));
    assertTrue(node2.hasSameExpression(node4, null));
    assertTrue(node4.hasSameExpression(node2, null));

    assertFalse(node1.hasSameExpression(node2, null));
    assertFalse(node2.hasSameExpression(node1, null));
    assertFalse(node3.hasSameExpression(node4, null));
    assertFalse(node4.hasSameExpression(node3, null));

    assertFalse(node1.hasSameExpression(node4, null));
    assertFalse(node4.hasSameExpression(node1, null));
    assertFalse(node3.hasSameExpression(node2, null));
    assertFalse(node2.hasSameExpression(node3, null));

    assertFalse(node1.hasSameExpression(nodeB, null));
    assertFalse(node2.hasSameExpression(nodeB, null));
    assertFalse(node3.hasSameExpression(nodeB, null));
    assertFalse(node4.hasSameExpression(nodeB, null));

    assertFalse(node1.hasSameExpression(nodeOp, null));
    assertFalse(node2.hasSameExpression(nodeOp, null));
    assertFalse(node3.hasSameExpression(nodeOp, null));
    assertFalse(node4.hasSameExpression(nodeOp, null));
  }

  /**
   * Test for the method haveSameExpressionAndLineNumber.
   */
  @Test
  public void testHaveSameExpressionAndLineNumber() {
    Constant refConstant1 = new Constant("0", 4);
    Constant refConstant2 = new Constant("0", 4);
    Constant refConstant3 = new Constant("0", 6);
    Constant refConstant4 = new Constant("1", 4);
    Constant refConstant5 = new Constant("1", 4);

    Variable varB = new Variable("b", 4);
    VariableNode nodeB = new VariableNode(FUNCTION_C1_MAIN, varB, NO_CHILD, 1, IGNORE_NODE_ID);

    Operation opPlus = TestUtils.createRefOperation("+", 4, refConstant2, refConstant1);
    OperationNode nodeOp = new OperationNode(FUNCTION_C1_MAIN, opPlus, NO_CHILD, 1, IGNORE_NODE_ID);

    ConstantNode node1 =
        new ConstantNode(FUNCTION_C1_MAIN, refConstant1, nodeB, 1, IGNORE_NODE_ID);
    ConstantNode node2 =
        new ConstantNode(FUNCTION_C1_MAIN, refConstant2, nodeB, 1, IGNORE_NODE_ID);
    ConstantNode node3 =
        new ConstantNode(FUNCTION_C1_MAIN, refConstant3, NO_CHILD, 1, IGNORE_NODE_ID);
    ConstantNode node4 =
        new ConstantNode(FUNCTION_C1_MAIN, refConstant4, NO_CHILD, 1, IGNORE_NODE_ID);
    ConstantNode node5 =
        new ConstantNode(FUNCTION_C1_MAIN, refConstant5, NO_CHILD, 1, IGNORE_NODE_ID);
    ConstantNode node6 =
        new ConstantNode(FUNCTION_C1_MAIN, refConstant2, nodeB, 2, IGNORE_NODE_ID);

    // Variable mapping does not matter constant node
    // same expression, same line number
    assertTrue(node1.hasSameExpressionAndLineNumber(node1));
    assertTrue(node2.hasSameExpressionAndLineNumber(node2));
    assertTrue(node3.hasSameExpressionAndLineNumber(node3));
    assertTrue(node4.hasSameExpressionAndLineNumber(node4));
    assertTrue(node5.hasSameExpressionAndLineNumber(node5));

    // same expression, same line number
    assertTrue(node1.hasSameExpressionAndLineNumber(node2));
    assertTrue(node2.hasSameExpressionAndLineNumber(node1));
    assertTrue(node5.hasSameExpressionAndLineNumber(node4));
    assertTrue(node4.hasSameExpressionAndLineNumber(node5));

    // same expression, diff line number
    assertFalse(node1.hasSameExpressionAndLineNumber(node3));
    assertFalse(node2.hasSameExpressionAndLineNumber(node3));
    assertFalse(node3.hasSameExpressionAndLineNumber(node4));
    assertFalse(node4.hasSameExpressionAndLineNumber(node3));

    // same line number, diff expression
    assertFalse(node2.hasSameExpressionAndLineNumber(node4));
    assertFalse(node5.hasSameExpressionAndLineNumber(node1));

    // same line number, different types of expression
    assertFalse(node1.hasSameExpressionAndLineNumber(nodeB));
    assertFalse(node4.hasSameExpressionAndLineNumber(nodeB));
    assertFalse(node1.hasSameExpressionAndLineNumber(nodeOp));
    assertFalse(node4.hasSameExpressionAndLineNumber(nodeOp));

    // different loc -> false
    assertFalse(node1.hasSameExpressionAndLineNumber(node6));
  }
}
