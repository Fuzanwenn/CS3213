package sg.edu.nus.se.its.errorlocalizer.dependencytree;

import static sg.edu.nus.se.its.errorlocalizer.dependencytree.DependencyNode.NodeIsomerism.CIS;
import static sg.edu.nus.se.its.errorlocalizer.dependencytree.DependencyNode.NodeIsomerism.TRANS;

import java.util.ArrayList;
import java.util.List;
import sg.edu.nus.se.its.alignment.VariableMapping;
import sg.edu.nus.se.its.errorlocalizer.utils.ExpressionUtil;
import sg.edu.nus.se.its.model.Expression;
import sg.edu.nus.se.its.model.Function;
import sg.edu.nus.se.its.model.Variable;


/**
 * An abstract class represents the nodes in Dependency Tree.
 *
 * @author Li Tianze
 */
public abstract class DependencyNode {

  /**
   * Cis Node is the original node in submission tree.
   * Trans node is the node returned by cisNode.getSymmetricOperationNode()
   * which has the equivalent logic.
   */
  enum NodeIsomerism {
    CIS, TRANS
  }

  private NodeIsomerism nodeIsomerism = CIS;
  protected List<List<DependencyNode>> dataParents;
  protected List<DependencyNode> controlParents;
  protected DependencyNode child;
  protected Function function;
  protected int loc;
  protected int id;

  DependencyNode(Function function, DependencyNode child, int loc, int id) {
    this.function = function;
    this.loc = loc;
    this.child = child;
    this.dataParents = new ArrayList<>();
    this.controlParents = new ArrayList<>();
    this.id = id;
  }

  public abstract Expression getExpression();

  public int getLoc() {
    return this.loc;
  }

  public Function getFunction() {
    return this.function;
  }


  /**
   * Returns if the given node is visited.
   *
   * @param dependencyNode The given node
   * @return if the given node is visited
   */
  public boolean isVisited(DependencyNode dependencyNode) {
    return this.hasSameExpressionAndLineNumber(dependencyNode)
        && this.loc == dependencyNode.loc
        && dependencyNode.function.equals(this.function)
        || (this.getChild() != null && this.getChild().isVisited(dependencyNode));
  }

  protected DependencyNode getChild() {
    return this.child;
  }

  protected void setChild(DependencyNode dependencyNode) {
    this.child = dependencyNode;
  }

  protected void extendDataParentsSize(int size) {
    while (this.dataParents.size() < size) {
      this.dataParents.add(new ArrayList<>());
    }
  }

  /**
   * Adds the given data flow parent node to the given argument position of
   * the data parents list of current node.
   *
   * @param argPos The given argument position
   * @param dependencyNode The given data flow parent node
   */
  protected void addDataParent(int argPos, DependencyNode dependencyNode) {
    extendDataParentsSize(argPos + 1);
    this.dataParents.get(argPos).add(dependencyNode);
  }

  /**
   * Adds the given control flow parent node to the control flow parents list
   * of the current node.
   *
   * @param dependencyNode The given control flow parent node
   */
  protected void addControlParent(DependencyNode dependencyNode) {
    if (ExpressionUtil.isControllingVar(dependencyNode.getExpression())) {
      this.controlParents.add(dependencyNode);
    } else {
      throw new RuntimeException(dependencyNode + " is not a controlling node.");
    }
  }

  /**
   * Returns true if the given dependency node has the exactly same expression as the
   * current node.
   *
   * @param dependencyNode The given dependency node
   * @return boolean
   */
  protected abstract boolean hasSameExpressionAndLineNumber(DependencyNode dependencyNode);

  /**
   * Returns true if the given dependency node has the equivalent expression as the
   * current node based on the given variable mapping.
   *
   * @param dependencyNode The given dependency node
   * @param variableMapping The given variable mapping
   * @return boolean
   */
  public abstract boolean hasSameExpression(DependencyNode dependencyNode,
                                            VariableMapping variableMapping);

  /**
   * Returns true if the current node has any child node visiting the given function.
   *
   * @param function The given function
   * @return boolean
   */
  protected boolean hasVisitedFunc(Function function) {
    if (this.function.equals(function)) {
      return true;
    } else if (this.child == null) {
      return false;
    } else {
      return this.child.hasVisitedFunc(function);
    }
  }

  /**
   * Returns true if the current node has any child node
   * visiting the given loc in the given function.
   *
   * @param function The given function
   * @param loc The given loc
   * @return boolean
   */
  protected boolean isFirstReachAtLoc(Function function, int loc) {
    if (!(this.function.equals(function))) {
      return false;
    } else if (this.loc != loc) {
      return false;
    } else if (this.child == null) {
      return true;
    } else {
      return !(this.child.isFirstReachAtLoc(function, loc));
    }
  }

  /**
   * Returns if the current node has the given node as parent.
   *
   * @param dependencyNode The given node
   * @return if the current node has the given node as parent.
   */
  public boolean containsParent(DependencyNode dependencyNode) {
    boolean isDataParent = this.dataParents.stream()
        .anyMatch(d -> d.stream()
            .anyMatch(n -> n.hasSameExpressionAndLineNumber(dependencyNode)));
    boolean isControlParent = this.controlParents.stream()
        .anyMatch(p -> p.hasSameExpressionAndLineNumber(dependencyNode));
    return isDataParent || isControlParent;
  }

  public List<List<DependencyNode>> getDataParents() {
    return this.dataParents;
  }

  public List<DependencyNode> getControlParents() {
    return this.controlParents;
  }

  void setIsomerism(NodeIsomerism nodeIsomerism) {
    this.nodeIsomerism = nodeIsomerism;
  }

  /**
   * Returns true if this is the node is the original node from the tree, not the symmetric one.
   *
   * @return if this is the node is the original node from the tree, not the symmetric one
   */
  public boolean isCis() {
    return CIS.equals(this.nodeIsomerism);
  }

  /**
   * Returns true if this is the node is the symmetric node reverted from the original node from
   * the dependency tree.
   *
   * @return if this is the node is the symmetric node reverted from the original node from
   *     the dependency tree
   */
  public boolean isTrans() {
    return TRANS.equals(this.nodeIsomerism);
  }

  /**
   * Returns the first children variable node.
   *
   * @return the first children variable node
   */
  public abstract Variable getFirstChildVar();

  /**
   * Returns the node with the symmetric operation is the operation is commutable, otherwise
   * returns itself.
   *
   * @return the dependency node
   */
  public abstract DependencyNode getSymmetricOperationNode();

  public int getId() {
    return this.id;
  }
}
