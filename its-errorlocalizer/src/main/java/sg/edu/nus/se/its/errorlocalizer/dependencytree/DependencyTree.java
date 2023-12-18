package sg.edu.nus.se.its.errorlocalizer.dependencytree;

import static sg.edu.nus.se.its.errorlocalizer.utils.FunctionUtil.getVariableWithCorrectLineNumber;
import static sg.edu.nus.se.its.errorlocalizer.utils.FunctionUtil.hasVariable;
import static sg.edu.nus.se.its.util.constants.Constants.VAR_COND;
import static sg.edu.nus.se.its.util.constants.Constants.VAR_RET;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import sg.edu.nus.se.its.errorlocalizer.utils.ExpressionUtil;
import sg.edu.nus.se.its.errorlocalizer.utils.FunctionUtil;
import sg.edu.nus.se.its.model.Constant;
import sg.edu.nus.se.its.model.Expression;
import sg.edu.nus.se.its.model.Function;
import sg.edu.nus.se.its.model.Operation;
import sg.edu.nus.se.its.model.Program;
import sg.edu.nus.se.its.model.Variable;

/**
 * The class represents the data dependency structure in a function.
 *
 * @author Li Tianze
 */
public class DependencyTree {
  public static final String MSG_UNSUPPORTED_EXPRESSION = "Unsupported subtype of Expression.";
  private static final Variable CONDITIONAL_VARIABLE = new Variable(VAR_COND);
  private final Program program;
  private final Function function;
  private final Variable variable;
  private final DependencyNode root;
  private boolean isDebugging = false;
  private int nodeCount;
  private List<DependencyNode> frontierNodes;

  /**
   * The constructor of the class if the root of this tree have a children node from a FuncCall
   * operation node from other trees.
   *
   * @param program  The given parsed program
   * @param function The given function from the given parsed program
   * @param variable The given variable in the given function
   * @param initLoc  The given initial line of the variable
   */
  public DependencyTree(Program program, Function function,
                        Variable variable, int initLoc, DependencyNode child) {
    this.program = program;
    this.function = function;

    if (FunctionUtil.hasVariable(function, variable, initLoc)) {
      this.variable = getVariableWithCorrectLineNumber(function, variable, initLoc);
      this.root = new VariableNode(function, this.variable, child, initLoc, 0);
      this.nodeCount = 1;
      construct();
    } else {
      this.variable = variable;
      this.root = null;
      this.nodeCount = 0;
    }
  }

  /**
   * The constructor of the class.
   *
   * @param program  The given parsed program
   * @param function The given function from the given parsed program
   * @param variable The given variable in the given function
   * @param initLoc  The given initial line of the variable
   */
  public DependencyTree(Program program, Function function, Variable variable, int initLoc) {
    this(program, function, variable, initLoc, null);
  }

  /**
   * Constructs the entire structure of the dependency tree when is called.
   * The starting point includes program, function, loc, and the starting variable.
   * The attributes are supposed to be filled by the constructor.
   */
  private void construct() {
    this.frontierNodes = new ArrayList<>();
    this.frontierNodes.add(this.root);

    while (!this.frontierNodes.isEmpty()) {
      DependencyNode curr = this.frontierNodes.remove(0);
      int loc = curr.getLoc();

      List<Expression> relativeExprs =
          FunctionUtil.findRelativeExpressions(this.function, curr.getExpression(), loc);

      // Remove the expression in curr.
      relativeExprs.remove(curr.getExpression());

      // For all variables that affecting the current Variable:
      for (int argPos = 0; argPos < relativeExprs.size(); argPos++) {
        Expression e = relativeExprs.get(argPos);
        if (e instanceof Constant) {
          iterateConstant(argPos, curr, (Constant) e, loc);
        } else if (e instanceof Operation) {
          iterateOperation(argPos, curr, (Operation) e, loc);
        } else if (e instanceof Variable) {
          iterateVariable(argPos, curr, (Variable) e, loc);
        } else {
          throw new RuntimeException(MSG_UNSUPPORTED_EXPRESSION);
        }
        if (curr.isFirstReachAtLoc(this.function, loc)) {
          addParentCondNodesToFrontier(curr, loc);
        }
      }
    }

    if (isDebugging) {
      System.out.println(this.nodeCount);
      System.out.println(this.function.toString());
      System.out.println(toString());
    }
  }

  private void iterateConstant(int argPos, DependencyNode curr, Constant constant, int loc) {
    DependencyNode newNode = new ConstantNode(this.function, constant, curr, loc, nodeCount);
    // Deliberately use curr.addDataParent()
    // as constant node are not supposed to add to frontierNodes.
    curr.addDataParent(argPos, newNode);
    nodeCount++;
  }

  private void iterateOperation(int argPos, DependencyNode curr, Operation operation, int loc) {
    final DependencyNode newNode =
        new OperationNode(this.function, operation, curr, loc, nodeCount);
    newNode.extendDataParentsSize(operation.getArgs().size());
    addDataParentNode(argPos, curr, newNode);
    if (!(ExpressionUtil.isFuncCall(operation))) {
      return;
    }
    Function calledFunction = ExpressionUtil.getCalledFunc(this.program, operation);
    if (curr.hasVisitedFunc(calledFunction)) {
      return;
    }
    getTreesRootedAtRet(this.program, calledFunction, curr).stream()
        .map(t -> {
          this.nodeCount += t.getNodeCount();
          return t.getRoot();
        })
        .filter(p -> p != null)
        .forEach(node -> {
          Expression calledFuncExpr = operation.getArgs().get(0);
          Variable funcNameVar;
          if (calledFuncExpr instanceof Constant) {
            // In python, the func name is stored as Constant
            funcNameVar = new Variable(((Constant) calledFuncExpr).getValue(),
                calledFuncExpr.getLineNumber());
          } else {
            // In c, the func name is stored as Variable
            funcNameVar = (Variable) operation.getArgs().get(0);
          }
          this.nodeCount++;
          DependencyNode funcNameNode = new VariableNode(
              this.function, funcNameVar, newNode, loc, this.nodeCount);
          funcNameNode.addDataParent(0, node);
          node.setChild(funcNameNode);
          newNode.addDataParent(0, funcNameNode);
        });
  }

  private void iterateVariable(int argPos, DependencyNode curr, Variable variable, int loc) {
    if (variable.isPrimed()) {
      // If this variable has been primed in this loc,
      // add the assignment of the variable to the tree.
      Variable correctVariable =
          getVariableWithCorrectLineNumber(this.function, variable, loc);
      DependencyNode newNode = new VariableNode(this.function,
          correctVariable, curr, loc, nodeCount);
      addDataParentNode(argPos, curr, newNode);
    } else if (!variable.isPrimed()) {
      // If this variable is not primed in this loc:
      Set<Integer> relativeLocs = FunctionUtil.findIncomingLocs(this.function, loc);
      List<Integer> frontierLocs = new ArrayList<>();
      frontierLocs.addAll(relativeLocs);

      // Use BFS to find assigning expressions from neighbours
      while (frontierLocs.size() > 0) {
        Integer nextLoc = frontierLocs.remove(0);
        if (hasVariable(this.function, variable, nextLoc)) {
          //When a frontier loc has the assigning expression, add this expression to the tree.
          DependencyNode newNode = new VariableNode(function,
              getVariableWithCorrectLineNumber(this.function, variable, nextLoc),
              curr, nextLoc, nodeCount);
          addDataParentNode(argPos, curr, newNode);
        } else {
          // When a frontier loc does not have the assigning expression,
          // add the next-layer neighbours to the frontier.
          Set<Integer> nextFrontier = FunctionUtil.findIncomingLocs(this.function, nextLoc);
          frontierLocs.addAll(nextFrontier.stream()
              .filter(i -> !(relativeLocs.contains(i) || i == loc)).collect(Collectors.toSet()));
          relativeLocs.addAll(nextFrontier);
        }
      }
    }
  }

  private void addParentCondNodesToFrontier(DependencyNode curr, int loc) {
    Set<Integer> relativeLocs = FunctionUtil.findIncomingLocs(this.function, loc);
    List<Integer> frontierLocs = new ArrayList<>();
    frontierLocs.addAll(relativeLocs);

    // Use BFS to find assigning expressions from neighbours
    while (frontierLocs.size() > 0) {
      Integer nextLoc = frontierLocs.remove(0);
      // When a frontier loc does not have the assigning expression,
      // add the next-layer neighbours to the frontier.
      if (hasVariable(this.function, CONDITIONAL_VARIABLE, nextLoc)) {
        DependencyNode newNode = new VariableNode(this.function,
            getVariableWithCorrectLineNumber(this.function,
                CONDITIONAL_VARIABLE, nextLoc), curr, nextLoc, nodeCount);
        addControlParentNode(curr, newNode);
      } else {
        Set<Integer> nextFrontier = FunctionUtil.findIncomingLocs(this.function, nextLoc);
        frontierLocs.addAll(nextFrontier.stream()
            .filter(i -> !(relativeLocs.contains(i) || i == loc)).collect(Collectors.toSet()));
        relativeLocs.addAll(nextFrontier);
      }
    }
  }

  public Variable getVariable() {
    return this.variable;
  }

  public DependencyNode getRoot() {
    return this.root;
  }

  public int getNodeCount() {
    return this.nodeCount;
  }

  private void addDataParentNode(int argPos, DependencyNode curr, DependencyNode next) {
    if (curr.isVisited(next) || curr.containsParent(next)) {
      return;
    }
    next.setChild(curr);
    curr.addDataParent(argPos, next);
    this.frontierNodes.add(next);
    this.nodeCount++;
  }

  private void addControlParentNode(DependencyNode curr, DependencyNode next) {
    if (curr.isVisited(next) || curr.containsParent(next)) {
      return;
    }
    next.setChild(curr);
    curr.addControlParent(next);
    this.frontierNodes.add(next);
    this.nodeCount++;
  }


  /**
   * Returns a data dependency tree of the given function
   * in given program which rooted at the returned value.
   * The iteration of the tree terminates if it meets the
   * given terminating function.
   *
   * @param program The given program
   * @param function The given function
   * @param child The children node of the ret node
   * @return a data dependency tree
   */
  public static List<DependencyTree> getTreesRootedAtRet(Program program,
                                                         Function function,
                                                         DependencyNode child) {
    Variable retVariable = new Variable(VAR_RET);
    return FunctionUtil.getFuncExitLoc(function).stream()
        .filter(exitLoc -> hasVariable(function, retVariable, exitLoc))
        .map(exitLoc -> new DependencyTree(program, function,
            new Variable(VAR_RET), exitLoc, child))
        .collect(Collectors.toList());
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();

    sb.append("Dependency Tree" + "\n");
    sb.append("Root:" + "\n");
    sb.append(this.root.toString());
    
    return sb.toString();
  }
}
