package sg.edu.nus.se.its.errorlocalizer.dependencytree;

import org.junit.jupiter.api.Test;
import sg.edu.nus.se.its.errorlocalizer.utils.ModelProgram;
import sg.edu.nus.se.its.model.Function;
import sg.edu.nus.se.its.model.Variable;
import sg.edu.nus.se.its.util.constants.Constants;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static sg.edu.nus.se.its.errorlocalizer.dependencytree.ModelTreeRoot.ARG_POS_FIRST;
import static sg.edu.nus.se.its.util.constants.Constants.VAR_COND;

/**
 * Test class for DependencyNode class.
 */
public class DependencyNodeTest {
  public static final DependencyNode NO_CHILD = null;
  public static final int IGNORE_NODE_ID = -1;

  /**
   * Test the getLoc method.
   */
  @Test
  public void testGetLoc() {
    assertEquals(ModelTreeRoot.getC1Loc1VarB().getLoc(), 1);
  }

  /**
   * Test the getFunction method.
   */
  @Test
  public void testGetFunction() {
    Function function = ModelProgram.MODEL_C1_C.get()
        .getFunctionForName(Constants.DEFAULT_ENTRY_FUNCTION_NAME);
    DependencyNode dependencyNode =
        new VariableNode(function, new Variable("foo"), NO_CHILD, 5, IGNORE_NODE_ID);
    DependencyNode dependencyNode1 =
        TestUtils.createRefVariableNode(function,
            new Variable("goo"), dependencyNode, ARG_POS_FIRST, 5, IGNORE_NODE_ID);
    assertEquals(dependencyNode.getFunction(), function);
  }

  /**
   * Test the getId method.
   */
  @Test
  public void testGetId() {
    Function function = ModelProgram.MODEL_C1_C.get()
        .getFunctionForName(Constants.DEFAULT_ENTRY_FUNCTION_NAME);
    DependencyNode dependencyNode =
        new VariableNode(function, new Variable("foo"), NO_CHILD, 5, 3);
    DependencyNode dependencyNode1 =
        TestUtils.createRefVariableNode(function,
            new Variable("goo"), dependencyNode, ARG_POS_FIRST, 5, 4);
    assertEquals(dependencyNode1.getId(), 4);
  }

  /**
   * Test the getExpression method.
   */
  @Test
  public void testGetExpression() {
    Function function = ModelProgram.MODEL_C1_C.get()
        .getFunctionForName(Constants.DEFAULT_ENTRY_FUNCTION_NAME);
    DependencyNode dependencyNode =
        new VariableNode(function, new Variable("foo"), NO_CHILD, 5, IGNORE_NODE_ID);
    DependencyNode dependencyNode1 =
        TestUtils.createRefVariableNode(function,
            new Variable("goo"), dependencyNode, ARG_POS_FIRST, 5, IGNORE_NODE_ID);
    assertEquals(dependencyNode1.getExpression(), new Variable("goo"));
  }

  /**
   * Test the getChild method.
   */
  @Test
  public void testGetChild() {
    Function function = ModelProgram.MODEL_C1_C.get()
        .getFunctionForName(Constants.DEFAULT_ENTRY_FUNCTION_NAME);
    DependencyNode dependencyNode =
        new VariableNode(function, new Variable("foo"), NO_CHILD, 5, IGNORE_NODE_ID);
    DependencyNode dependencyNode1 =
        TestUtils.createRefVariableNode(function,
            new Variable("goo"), dependencyNode, ARG_POS_FIRST, 5, IGNORE_NODE_ID);
    assertEquals(dependencyNode1.getChild(), dependencyNode);
  }

  /**
   * Test the setChild method.
   */
  @Test
  public void testSetChild() {
    Function function = ModelProgram.MODEL_C1_C.get()
        .getFunctionForName(Constants.DEFAULT_ENTRY_FUNCTION_NAME);
    DependencyNode dependencyNode =
        new VariableNode(function, new Variable("foo"), NO_CHILD, 5, IGNORE_NODE_ID);
    DependencyNode dependencyNode1 =
        TestUtils.createRefVariableNode(function,
            new Variable("goo"), NO_CHILD, ARG_POS_FIRST, 5, IGNORE_NODE_ID);
    dependencyNode1.setChild(dependencyNode);
    assertEquals(dependencyNode1.getChild(), dependencyNode);
  }

  /**
   * Test the addDataParent method.
   */
  @Test
  public void testAddParentData() {
    Function function = ModelProgram.MODEL_C1_C.get()
        .getFunctionForName(Constants.DEFAULT_ENTRY_FUNCTION_NAME);
    DependencyNode dependencyNode =
        new VariableNode(function, new Variable("foo"), NO_CHILD, 5, IGNORE_NODE_ID);
    DependencyNode dependencyNode1 =
        TestUtils.createRefVariableNode(function,
            new Variable("goo"), NO_CHILD, ARG_POS_FIRST, 5, IGNORE_NODE_ID);
    dependencyNode.addDataParent(0, dependencyNode1);
    assertFalse(dependencyNode.getDataParents().isEmpty());
    assertEquals(dependencyNode.getDataParents().size(), 1);
    assertEquals(dependencyNode.getDataParents().get(ARG_POS_FIRST).get(0), dependencyNode1);
    assertTrue(dependencyNode.getControlParents().isEmpty());
  }

  /**
   * Test the addControlParent method.
   */
  @Test
  public void testAddParentControl() {
    Function function = ModelProgram.MODEL_C1_C.get()
        .getFunctionForName(Constants.DEFAULT_ENTRY_FUNCTION_NAME);
    DependencyNode dependencyNode =
        new VariableNode(function, new Variable("foo"), NO_CHILD, 5, IGNORE_NODE_ID);
    DependencyNode dependencyNode1 =
        TestUtils.createRefVariableNode(function,
            new Variable(VAR_COND), NO_CHILD, ARG_POS_FIRST, 5, IGNORE_NODE_ID);
    dependencyNode.addControlParent(dependencyNode1);
    assertFalse(dependencyNode.getControlParents().isEmpty());
    assertEquals(dependencyNode.getControlParents().size(), 1);
    assertEquals(dependencyNode.getControlParents().get(0), dependencyNode1);
    assertTrue(dependencyNode.getDataParents().isEmpty());
  }

  /**
   * Test the addControlParent and addDataParent method.
   */
  @Test
  public void testAddParentHybridised() {
    Function function = ModelProgram.MODEL_C1_C.get()
        .getFunctionForName(Constants.DEFAULT_ENTRY_FUNCTION_NAME);
    DependencyNode dependencyNode =
        new VariableNode(function, new Variable("foo"), NO_CHILD, 5, IGNORE_NODE_ID);
    DependencyNode dependencyNode1 =
        TestUtils.createRefVariableNode(function,
            new Variable(VAR_COND), NO_CHILD, ARG_POS_FIRST, 5, IGNORE_NODE_ID);
    DependencyNode dependencyNode2 =
        TestUtils.createRefVariableNode(function,
            new Variable("goo"), NO_CHILD, ARG_POS_FIRST, 5, IGNORE_NODE_ID);

    dependencyNode.addControlParent(dependencyNode1);
    dependencyNode.addDataParent(0, dependencyNode2);
    assertFalse(dependencyNode.getDataParents().isEmpty());
    assertFalse(dependencyNode.getControlParents().isEmpty());
    assertEquals(dependencyNode.getDataParents().size(), 1);
    assertEquals(dependencyNode.getControlParents().size(), 1);
    assertEquals(dependencyNode.getControlParents().get(0), dependencyNode1);
    assertEquals(dependencyNode.getDataParents().get(ARG_POS_FIRST).get(0), dependencyNode2);
  }

  /**
   * Test the hasVisitedFunc method.
   */
  @Test
  public void testHasVisitedFunc() {
    Function function = ModelProgram.MODEL_C1_C.get()
        .getFunctionForName(Constants.DEFAULT_ENTRY_FUNCTION_NAME);
    DependencyNode dependencyNode =
        new VariableNode(function, new Variable("foo"), NO_CHILD, 5, IGNORE_NODE_ID);
    DependencyNode dependencyNode1 =
        TestUtils.createRefVariableNode(function,
            new Variable(VAR_COND), NO_CHILD, ARG_POS_FIRST, 5, IGNORE_NODE_ID);
    assertTrue(dependencyNode1.hasVisitedFunc(function));
  }

  /**
   * Test the isFistReachAtLoc method.
   */
  @Test
  public void testIsFirstReachAtLoc() {
    Function function = ModelProgram.MODEL_C1_C.get()
        .getFunctionForName(Constants.DEFAULT_ENTRY_FUNCTION_NAME);
    DependencyNode dependencyNode =
        new VariableNode(function, new Variable("foo"), NO_CHILD, 1, IGNORE_NODE_ID);
    DependencyNode dependencyNode1 =
        TestUtils.createRefVariableNode(function,
            new Variable(VAR_COND), dependencyNode, ARG_POS_FIRST, 2, IGNORE_NODE_ID);
    DependencyNode dependencyNode2 =
        TestUtils.createRefVariableNode(function,
            new Variable(VAR_COND), dependencyNode1, ARG_POS_FIRST, 3, IGNORE_NODE_ID);
    DependencyNode dependencyNode3 =
        TestUtils.createRefVariableNode(function,
            new Variable(VAR_COND), dependencyNode1, ARG_POS_FIRST, 2, IGNORE_NODE_ID);

    assertTrue(dependencyNode.isFirstReachAtLoc(function, 1));
    assertFalse(dependencyNode.isFirstReachAtLoc(function, 3));
    assertTrue(dependencyNode1.isFirstReachAtLoc(function, 2));
    assertTrue(dependencyNode2.isFirstReachAtLoc(function, 3));
    assertFalse(dependencyNode2.isFirstReachAtLoc(function, 2));
    assertFalse(dependencyNode3.isFirstReachAtLoc(function, 2));
  }

  /**
   * Test the containsParent method.
   */
  @Test
  public void testContainsParent() {
    Function function = ModelProgram.MODEL_C1_C.get()
        .getFunctionForName(Constants.DEFAULT_ENTRY_FUNCTION_NAME);
    DependencyNode dependencyNode =
        new VariableNode(function, new Variable("foo"), NO_CHILD, 5, IGNORE_NODE_ID);
    DependencyNode dependencyNode1 =
        TestUtils.createRefVariableNode(function,
            new Variable(VAR_COND), NO_CHILD, ARG_POS_FIRST, 5, IGNORE_NODE_ID);
    DependencyNode dependencyNode2 =
        TestUtils.createRefVariableNode(function,
            new Variable("goo"), NO_CHILD, ARG_POS_FIRST, 5, IGNORE_NODE_ID);
    DependencyNode dependencyNode3 =
        TestUtils.createRefVariableNode(function,
            new Variable("koo"), NO_CHILD, ARG_POS_FIRST, 9, IGNORE_NODE_ID);

    dependencyNode.addControlParent(dependencyNode1);
    dependencyNode.addDataParent(0, dependencyNode2);

    assertTrue(dependencyNode.containsParent(dependencyNode1));
    assertTrue(dependencyNode.containsParent(dependencyNode2));
    assertFalse(dependencyNode.containsParent(dependencyNode3));
  }
}
