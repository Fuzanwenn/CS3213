package sg.edu.nus.se.its.errorlocalizer.dependencytree;

import sg.edu.nus.se.its.alignment.VariableMapping;
import sg.edu.nus.se.its.model.Constant;
import sg.edu.nus.se.its.model.Expression;
import sg.edu.nus.se.its.model.Function;
import sg.edu.nus.se.its.model.Variable;

/**
 * A class represents the node containing a Constant in a Dependency Tree.
 *
 * @author Li Tianze
 */
public class ConstantNode extends DependencyNode {
  private Constant constant;

  public ConstantNode(Function function, Constant constant,
                      DependencyNode child, int loc, int id) {
    super(function, child, loc, id);
    this.constant = constant;
  }

  @Override
  public Variable getFirstChildVar() {
    if (this.getChild() == null) {
      return null;
    }
    return this.getChild().getFirstChildVar();
  }

  @Override
  public DependencyNode getSymmetricOperationNode() {
    return this;
  }

  @Override
  public Expression getExpression() {
    return this.constant;
  }

  @Override
  public boolean equals(Object object) {
    if (object instanceof ConstantNode) {
      ConstantNode constantNode = (ConstantNode) object;
      return this.constant.equals(constantNode.constant);
    }
    return false;
  }

  @Override
  protected boolean hasSameExpressionAndLineNumber(DependencyNode dependencyNode) {
    if (dependencyNode == null) {
      return false;
    } else if (dependencyNode instanceof ConstantNode) {
      ConstantNode variableNode = (ConstantNode) dependencyNode;
      Constant constant = (Constant) variableNode.getExpression();

      return constant.getLineNumber() == this.getExpression().getLineNumber()
          && this.loc == dependencyNode.loc
          && constant.getValue().equals(this.constant.getValue());
    }
    return false;
  }

  @Override
  public boolean hasSameExpression(DependencyNode dependencyNode, VariableMapping variableMapping) {
    if (dependencyNode == null) {
      return false;
    } else if (dependencyNode instanceof ConstantNode) {
      ConstantNode variableNode = (ConstantNode) dependencyNode;
      Constant constant = (Constant) variableNode.getExpression();
      return constant.getValue().equals(this.constant.getValue());
    }
    return false;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    
    sb.append(String.format("[%s: %s (%d, %d)", 
            this.function.getName(), 
            this.constant, 
            this.loc, 
            this.constant.getLineNumber()
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
