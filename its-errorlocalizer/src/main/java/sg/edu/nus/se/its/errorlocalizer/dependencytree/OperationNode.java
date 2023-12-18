package sg.edu.nus.se.its.errorlocalizer.dependencytree;

import static sg.edu.nus.se.its.errorlocalizer.dependencytree.DependencyNode.NodeIsomerism.CIS;
import static sg.edu.nus.se.its.errorlocalizer.dependencytree.DependencyNode.NodeIsomerism.TRANS;

import sg.edu.nus.se.its.alignment.VariableMapping;
import sg.edu.nus.se.its.errorlocalizer.utils.ExpressionUtil;
import sg.edu.nus.se.its.model.Expression;
import sg.edu.nus.se.its.model.Function;
import sg.edu.nus.se.its.model.Operation;
import sg.edu.nus.se.its.model.Variable;

/**
 * A class represents the node containing an operation in a Dependency Tree.
 *
 * @author Li Tianze
 */
public class OperationNode extends DependencyNode {
  public static final int BINARY_OPS_ARG_SIZE = 2;
  private static final String MSG_INVALID_ISOMERISM =
      "Node Isomerism should be either CIS or TRANS.";
  private Operation operation;
  private boolean areArgsOrdered;
  private boolean isCommutable;

  /**
   * The constructor of an operation node.
   *
   * @param function The given function
   * @param operation The given operation
   * @param child The child node
   * @param loc The location
   * @param id The node id
   */
  public OperationNode(Function function, Operation operation,
                       DependencyNode child, int loc, int id) {
    super(function, child, loc, id);
    this.operation = operation;
    this.areArgsOrdered = ExpressionUtil.areArgsOrdered(operation);
    this.isCommutable = ExpressionUtil.isCommutative(operation);
  }

  public boolean areArgsOrdered() {
    return this.areArgsOrdered;
  }

  /**
   * Returns the corresponding operation node such that the direction of operation is reversed,
   * i.e, the getSymmetricOperationNode of <(a, b) is >(b, a).
   *
   * @return The corresponding operation node of this node.
   */
  @Override
  public DependencyNode getSymmetricOperationNode() {
    if (!this.isCommutable) {
      return this;
    }
    if (this.operation.getArgs().size() != BINARY_OPS_ARG_SIZE) {
      return this;
    }
    Operation reversedOp = ExpressionUtil.getSymmetricBinaryOperation(this.operation);
    OperationNode result =
        new OperationNode(this.function, reversedOp, this.child, this.loc, this.id);
    this.controlParents.stream().forEachOrdered(result::addControlParent);
    this.extendDataParentsSize(BINARY_OPS_ARG_SIZE);
    result.extendDataParentsSize(BINARY_OPS_ARG_SIZE);
    this.dataParents.get(0).stream().forEachOrdered(p -> result.addDataParent(1, p));
    this.dataParents.get(1).stream().forEachOrdered(p -> result.addDataParent(0, p));

    if (this.isCis()) {
      result.setIsomerism(TRANS);
    } else if (this.isTrans()) {
      result.setIsomerism(CIS);
    } else {
      throw new RuntimeException(MSG_INVALID_ISOMERISM);
    }
    return result;
  }

  @Override
  public Variable getFirstChildVar() {
    if (this.getChild() == null) {
      return null;
    } else {
      return this.getChild().getFirstChildVar();
    }
  }

  @Override
  public Expression getExpression() {
    return this.operation;
  }

  @Override
  public boolean equals(Object object) {
    if (object instanceof OperationNode) {
      OperationNode operationNode = (OperationNode) object;
      return this.operation.equals(operationNode.operation);
    }
    return false;
  }

  @Override
  protected boolean hasSameExpressionAndLineNumber(DependencyNode dependencyNode) {
    if (dependencyNode == null) {
      return false;
    } else if (dependencyNode instanceof OperationNode) {
      OperationNode operationNode = (OperationNode) dependencyNode;
      Operation operation = (Operation) operationNode.getExpression();
      return operation.getLineNumber() == this.getExpression().getLineNumber()
          && this.loc == dependencyNode.loc
          // meant to use Operation.equals()
          && operation.equals(this.operation);
    }
    return false;
  }


  @Override
  public boolean hasSameExpression(DependencyNode dependencyNode, VariableMapping variableMapping) {
    if (dependencyNode == null) {
      return false;
    } else if (dependencyNode instanceof OperationNode) {
      OperationNode operationNode = (OperationNode) dependencyNode;
      Operation operation = (Operation) operationNode.getExpression();
      return operation.getName().equals(this.operation.getName());
    }
    return false;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();

    sb.append(String.format("[%s: %s (%d, %d)",
            this.function.getName(),
            this.operation.toString(),
            this.loc,
            this.operation.getLineNumber()
    ));

    if (this.dataParents.size() > 0) {
      sb.append(String.format(" <- {%s}",
              this.dataParents.toString().substring(1, this.dataParents.toString().length() - 1)
      ));
    }

    if (this.controlParents.size() > 0) {
      sb.append(String.format(" & {%s}",
              this.controlParents.toString()
                  .substring(1, this.controlParents.toString().length() - 1)
      ));
    }

    sb.append("]");
    
    return sb.toString();
  }
}
