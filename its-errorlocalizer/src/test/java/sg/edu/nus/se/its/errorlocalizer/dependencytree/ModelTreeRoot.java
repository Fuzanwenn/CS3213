package sg.edu.nus.se.its.errorlocalizer.dependencytree;

import sg.edu.nus.se.its.errorlocalizer.utils.ModelProgram;
import sg.edu.nus.se.its.model.Constant;
import sg.edu.nus.se.its.model.Expression;
import sg.edu.nus.se.its.model.Function;
import sg.edu.nus.se.its.model.Operation;
import sg.edu.nus.se.its.model.Program;
import sg.edu.nus.se.its.model.Variable;
import sg.edu.nus.se.its.util.constants.Constants;

import java.util.ArrayList;
import java.util.List;

import static sg.edu.nus.se.its.errorlocalizer.dependencytree.DependencyNodeTest.IGNORE_NODE_ID;
import static sg.edu.nus.se.its.errorlocalizer.dependencytree.DependencyNodeTest.NO_CHILD;
import static sg.edu.nus.se.its.errorlocalizer.dependencytree.TestUtils.createRefConstantNode;
import static sg.edu.nus.se.its.errorlocalizer.dependencytree.TestUtils.createRefOperation;
import static sg.edu.nus.se.its.errorlocalizer.dependencytree.TestUtils.createRefOperationNode;
import static sg.edu.nus.se.its.errorlocalizer.dependencytree.TestUtils.createRefVariableNode;
import static sg.edu.nus.se.its.util.constants.Constants.VAR_COND;
import static sg.edu.nus.se.its.util.constants.Constants.VAR_RET;

/**
 * Create ModelTreeRoot for testing.
 */
public class ModelTreeRoot {
  public static final int ARG_POS_FIRST = 0;
  public static final int ARG_POS_SECOND = 1;
  public static final int ARG_POS_THIRD = 2;
  public static final int ARG_POS_FOURTH = 3;

  /**
   * Returns the first location of variable B in the dependency tree for model C1.
   *
   * @return
   */
  public static DependencyNode getC1Loc1VarB() {
    Program program = ModelProgram.MODEL_C1_C.get();
    Function function = program.getFunctionForName(Constants.DEFAULT_ENTRY_FUNCTION_NAME);

    Constant refConstant1 = new Constant("0", 4);
    Constant refConstant2 = new Constant("1", 5);
    Constant refConstant3 = new Constant("0", 6);

    Variable refVarA = new Variable("a", 4);
    Variable refVarAPrimed = (Variable) new Variable("a", 5).prime();
    Variable refVarB = new Variable("b", 5);
    Variable refVarRet = new Variable(VAR_RET, 6);

    List<Expression> args = new ArrayList<>();
    args.add(refConstant2);
    args.add(refVarAPrimed);
    Operation refOperationPlus =
        createRefOperation("+", 5, refConstant2, refVarAPrimed);

    VariableNode refVarNodeB =
        createRefVariableNode(function, refVarB, NO_CHILD, ARG_POS_FIRST, 1, IGNORE_NODE_ID);
    OperationNode refOpNodePlus =
        createRefOperationNode(function, refOperationPlus, refVarNodeB, ARG_POS_FIRST,1, IGNORE_NODE_ID);
    ConstantNode refConstNode2 =
        createRefConstantNode(function, refConstant2, refOpNodePlus, ARG_POS_FIRST, 1, IGNORE_NODE_ID);
    VariableNode refVarNodeA =
        createRefVariableNode(function, refVarA, refOpNodePlus, ARG_POS_SECOND, 1, IGNORE_NODE_ID);
    ConstantNode refConstNode1 =
        createRefConstantNode(function, refConstant1, refVarNodeA, ARG_POS_FIRST, 1, IGNORE_NODE_ID);

    return refVarNodeB;
  }

  /**
   * Returns the second location of variable A in the dependency tree for model C1.
   *
   * @return
   */
  public static DependencyNode getC1Loc1VarA() {
    Program program = ModelProgram.MODEL_C1_C.get();
    Function function = program.getFunctionForName(Constants.DEFAULT_ENTRY_FUNCTION_NAME);

    Constant refConstant1 = new Constant("0", 4);

    Variable refVarA = new Variable("a", 4);

    VariableNode refVarNodeA =
        createRefVariableNode(function, refVarA, NO_CHILD, ARG_POS_FIRST, 1, IGNORE_NODE_ID);
    ConstantNode refConstNode1 =
        createRefConstantNode(function, refConstant1, refVarNodeA, ARG_POS_FIRST, 1, IGNORE_NODE_ID);

    return refVarNodeA;
  }

  /**
   * Returns the first location of return statement in the dependency tree for model C1.
   *
   * @return
   */
  public static DependencyNode getC1Loc1VarRet() {
    Program program = ModelProgram.MODEL_C1_C.get();
    Function function = program.getFunctionForName(Constants.DEFAULT_ENTRY_FUNCTION_NAME);

    Constant refConstant3 = new Constant("0", 6);

    Variable refVarRet = new Variable(VAR_RET, 6);

    VariableNode refVarNodeRet =
        createRefVariableNode(function, refVarRet, NO_CHILD, ARG_POS_FIRST, 1, IGNORE_NODE_ID);
    ConstantNode refConstNode3 =
        createRefConstantNode(function, refConstant3, refVarNodeRet, ARG_POS_FIRST, 1, IGNORE_NODE_ID);

    return refVarNodeRet;
  }

  /**
   * Returns the first location of return statement in the dependency tree for model loop i.
   *
   * @return
   */
  public static DependencyNode getLoopiLoc1varRet() {
    Program program =ModelProgram.MODEL_LOOP_I_C.get();
    Function function = program.getFunctionForName(Constants.DEFAULT_ENTRY_FUNCTION_NAME);

    Constant constant1 = new Constant("0", 4);
    Variable varResult = new Variable("result", 4);
    VariableNode varNodeResult =
        createRefVariableNode(function, varResult, NO_CHILD, ARG_POS_FIRST, 1, IGNORE_NODE_ID);
    ConstantNode constantNode =
        createRefConstantNode(function, constant1, varNodeResult, ARG_POS_FIRST, 1, IGNORE_NODE_ID);
    return varNodeResult;
  }

  /**
   * Returns the first location of variable i in the dependency tree for model loop i.
   *
   * @return
   */
  public static DependencyNode getLoopiLoc1varI() {
    Program program =ModelProgram.MODEL_LOOP_I_C.get();
    Function function = program.getFunctionForName(Constants.DEFAULT_ENTRY_FUNCTION_NAME);

    Constant constant1 = new Constant("0", 5);
    Variable varI = new Variable("i", 5);
    VariableNode varNodeI =
        createRefVariableNode(function, varI, NO_CHILD, ARG_POS_FIRST,1, IGNORE_NODE_ID);
    ConstantNode constantNode =
        createRefConstantNode(function, constant1, varNodeI, ARG_POS_FIRST, 1, IGNORE_NODE_ID);
    return varNodeI;
  }

  /**
   * Returns the second location of conditional statement in the dependency tree for model loop i.
   *
   * @return
   */
  public static DependencyNode getLoopiLoc2Cond() {
    Program program =ModelProgram.MODEL_LOOP_I_C.get();
    Function function = program.getFunctionForName(Constants.DEFAULT_ENTRY_FUNCTION_NAME);

    Constant constant1 = new Constant("0", 4);
    Variable varLoc1Result = new Variable("result", 4);
    Constant constant2 = new Constant("0", 5);
    Variable varLoc1I = new Variable("i", 5);

    Variable varLoc2Cond = new Variable(VAR_COND, 5);
    Constant constant3 = new Constant("2", 5);
    Variable varLoc2I = new Variable("i", 5);
    Operation opLoc2Lt = new Operation("<", List.of(varLoc2I, constant3), 5);

    Variable varLoc3I = new Variable("i", 5);
    Constant constant4 = new Constant("1", 5);
    Operation opLoc3Plus = new Operation("+", List.of(varLoc3I, constant4), 5);

    Variable varLoc4Result = new Variable("result", 6);
    Variable varLoc4I = new Variable("i", 6);


    VariableNode varNodeCond =
        new VariableNode(function, varLoc2Cond, NO_CHILD, 2, IGNORE_NODE_ID);
    OperationNode opNodeLoc2Lt = createRefOperationNode(function, opLoc2Lt, varNodeCond, ARG_POS_FIRST, 2, IGNORE_NODE_ID);
    VariableNode varNodeLoc1I = createRefVariableNode(function, varLoc1I, opNodeLoc2Lt, ARG_POS_FIRST, 1, IGNORE_NODE_ID);
    createRefConstantNode(function, constant2, varNodeLoc1I, ARG_POS_FIRST, 1, IGNORE_NODE_ID);

    VariableNode varNodeLoc3I = createRefVariableNode(function, varLoc3I, opNodeLoc2Lt, ARG_POS_FIRST, 3, IGNORE_NODE_ID);
    OperationNode opNodeLoc3Plus = createRefOperationNode(function, opLoc3Plus, varNodeLoc3I, ARG_POS_FIRST, 3, IGNORE_NODE_ID);
    VariableNode variableNode1I2 = createRefVariableNode(function, varLoc1I, opNodeLoc3Plus, ARG_POS_FIRST, 1, IGNORE_NODE_ID);
    createRefConstantNode(function, constant2, variableNode1I2, ARG_POS_FIRST, 1, IGNORE_NODE_ID);
    createRefConstantNode(function, constant4, opNodeLoc3Plus, ARG_POS_SECOND, 3, IGNORE_NODE_ID);

    createRefConstantNode(function, constant3, opNodeLoc2Lt, ARG_POS_SECOND, 2, IGNORE_NODE_ID);

    return varNodeCond;
  }
}
