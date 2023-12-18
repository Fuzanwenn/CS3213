package sg.edu.nus.se.its.errorlocalizer.dependencytree;

import sg.edu.nus.se.its.model.Constant;
import sg.edu.nus.se.its.model.Expression;
import sg.edu.nus.se.its.model.Function;
import sg.edu.nus.se.its.model.Operation;
import sg.edu.nus.se.its.model.Variable;

import java.util.List;

import static sg.edu.nus.se.its.errorlocalizer.dependencytree.DependencyNodeTest.NO_CHILD;

/**
 * Utils class contains utility methods for testing.
 */
public class TestUtils {

  protected static ConstantNode createRefConstantNode(
      Function function, Constant constant, DependencyNode child, int argPos, int loc, int id) {
    ConstantNode parent = new ConstantNode(function, constant, child, loc, id);
    if (child != NO_CHILD) {
      child.addDataParent(argPos, parent);
    }
    return parent;
  }

  protected static VariableNode createRefVariableNode(
      Function function, Variable variable, DependencyNode child, int argPos, int loc, int id) {
    VariableNode parent = new VariableNode(function, variable, child, loc, id);
    if (child != NO_CHILD) {
      child.addDataParent(argPos, parent);
    }
    return parent;
  }

  protected static OperationNode createRefOperationNode(
      Function function, Operation operation, DependencyNode child, int argPos, int loc, int id) {
    OperationNode parent = new OperationNode(function, operation, child, loc, id);
    if (child != NO_CHILD) {
      child.addDataParent(argPos, parent);
    }
    return parent;
  }

  protected static Operation createRefOperation(String name, int line, Expression... args) {
    return new Operation(name, List.of(args), line);
  }
}
