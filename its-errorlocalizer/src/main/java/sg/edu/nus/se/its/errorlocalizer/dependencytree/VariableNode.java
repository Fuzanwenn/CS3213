package sg.edu.nus.se.its.errorlocalizer.dependencytree;

import sg.edu.nus.se.its.alignment.VariableMapping;
import sg.edu.nus.se.its.errorlocalizer.utils.ExpressionUtil;
import sg.edu.nus.se.its.model.Expression;
import sg.edu.nus.se.its.model.Function;
import sg.edu.nus.se.its.model.Variable;

/**
 * A class represents the node containing a Variable in a Dependency Tree.
 *
 * @author Li Tianze
 */
public class VariableNode extends DependencyNode {
  private Variable variable;

  public VariableNode(Function function, Variable variable,
                      DependencyNode child, int loc, int id) {
    super(function, child, loc, id);
    this.variable = variable;
  }

  public String getVarType() {
    return this.function.getTypes().get(this.variable.getName());
  }

  @Override
  public Variable getFirstChildVar() {
    return this.variable;
  }

  @Override
  public DependencyNode getSymmetricOperationNode() {
    return this;
  }

  @Override
  public Expression getExpression() {
    return this.variable;
  }

  @Override
  protected boolean hasSameExpressionAndLineNumber(DependencyNode dependencyNode) {
    if (dependencyNode == null) {
      return false;
    } else if (dependencyNode instanceof VariableNode) {
      VariableNode variableNode = (VariableNode) dependencyNode;
      Variable variable = (Variable) variableNode.getExpression();

      boolean areVarTypeMapped;
      String thisVarType = this.getVarType();
      String thatVarType = variableNode.getVarType();
      areVarTypeMapped = ((thisVarType == null && thatVarType == null)
          || (thisVarType != null && thisVarType.equals(thatVarType)));

      boolean areParsedLocsMapped = this.loc == dependencyNode.loc;
      boolean areRealLocsMapped =
          variable.getLineNumber() == this.getExpression().getLineNumber();

      return areParsedLocsMapped
          && areRealLocsMapped
          && variable.getUnprimedName().equals(this.variable.getUnprimedName())
          && areVarTypeMapped;
    }
    return false;
  }

  @Override
  public boolean hasSameExpression(DependencyNode dependencyNode, VariableMapping variableMapping) {
    if (dependencyNode == null) {
      return false;
    } else if (dependencyNode instanceof VariableNode) {
      VariableNode variableNode = (VariableNode) dependencyNode;
      Variable variable = (Variable) variableNode.getExpression();

      boolean areVarTypeMapped;
      String thisVarType = this.getVarType();
      String thatVarType = variableNode.getVarType();
      //      Deliberately easing type checks
      //      as some variable types are not included in Function.getTypes()
      areVarTypeMapped = ((thisVarType == null || thatVarType == null)
          || (thisVarType != null && thisVarType.equals(thatVarType)));
      return ExpressionUtil
          .areMappedVariable(this.variable, variable, variableMapping, this.function)
          && areVarTypeMapped;
    }
    return false;
  }

  @Override
  public boolean equals(Object object) {
    if (object instanceof VariableNode) {
      VariableNode variableNode = (VariableNode) object;
      return this.variable.getLineNumber() == variableNode.variable.getLineNumber()
          && this.variable.getName().equals(variableNode.variable.getName());
    }
    return false;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();

    sb.append(String.format("[%s: %s (%d, %d)",
            this.function.getName(),
            this.variable.getName(),
            this.loc,
            this.variable.getLineNumber()
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

