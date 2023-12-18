package sg.edu.nus.se.its.alignment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.javatuples.Pair;
import sg.edu.nus.se.its.model.Expression;
import sg.edu.nus.se.its.model.Operation;
import sg.edu.nus.se.its.model.Variable;

/**
 * Represents a basic block in the CFG.
 */
public class Block {
  private final Integer loc;
  private final List<Pair<String, Expression>> exprs;
  private final List<Pair<Boolean, Integer>> incomingTrans = new ArrayList<>();
  private final Integer outgoingTransTrue;
  private final Integer outgoingTransFalse;
  private final List<String> variableTypes = new ArrayList<>();
  private final int numChildren;
  private final int numOutgoingTrans;

  /**
   * Constructor for Block object.
   *
   * @param loc The location of the block
   * @param exprs The blocks of the CFG
   * @param trans The transitions of the CFG
   * @param types The types of the variables in the block
   */
  public Block(Integer loc, List<Pair<String, Expression>> exprs,
               Map<Integer, HashMap<Boolean, Integer>> trans,
               Map<String, String> types, int outgoingTrans) {
    this.loc = loc;
    this.exprs = new ArrayList<>(exprs);
    if (trans.containsKey(loc)) {
      outgoingTransTrue = trans.get(loc).get(true);
      outgoingTransFalse = trans.get(loc).get(false);
      numChildren = parseNumberOfUniqueChildren(trans, loc);
      numOutgoingTrans = outgoingTrans;
    } else {
      outgoingTransTrue = null;
      outgoingTransFalse = null;
      numChildren = 0;
      numOutgoingTrans = 0;
    }

    for (var entry : trans.entrySet()) {
      if (loc.equals(entry.getValue().get(true))) {
        incomingTrans.add(new Pair<>(true, entry.getKey()));
      }

      if (loc.equals(entry.getValue().get(false))) {
        incomingTrans.add(new Pair<>(false, entry.getKey()));
      }
    }

    parseExprTypes(exprs, types);
  }

  private void parseExprTypes(List<Pair<String, Expression>> exprs,
                              Map<String, String> types) {
    LinkedList<Expression> stack = new LinkedList<>();
    HashSet<String> found = new HashSet<>();
    for (Pair<String, Expression> expr : exprs) {
      stack.add(expr.getValue1());
    }

    while (!stack.isEmpty()) {
      Expression expr = stack.pollFirst();
      if ("Constant".equals(expr.getType())) {
        continue;
      }

      if ("Operation".equals(expr.getType())) {
        Operation op = (Operation) expr;
        stack.addAll(op.getArgs());
        continue;
      }

      if ("Variable".equals(expr.getType())) {
        Variable v = (Variable) expr;
        String name = v.getName();
        if (!found.contains(name) && types.containsKey(name)) {
          variableTypes.add(types.get(name));
          found.add(name);
        }
      }
    }
  }

  private int parseNumberOfUniqueChildren(Map<Integer, HashMap<Boolean, Integer>> trans,
                                          Integer loc) {
    Set<Integer> visited = new HashSet<>();
    visited.add(loc);

    LinkedList<Integer> stack = new LinkedList<>();
    Map<Boolean, Integer> out = trans.get(loc);

    if (out != null) {
      stack.add(out.get(true));
      stack.add(out.get(false));
    }

    int count = 0;
    while (!stack.isEmpty()) {
      Integer next = stack.pollFirst();
      if (next == null || visited.contains(next)) {
        continue;
      }
      count += 1;
      visited.add(next);
      out = trans.get(next);
      if (out != null) {
        stack.add(out.get(true));
        stack.add(out.get(false));
      }
    }

    return count;
  }

  public Integer getLoc() {
    return loc;
  }

  public List<Pair<String, Expression>> getExprs() {
    return Collections.unmodifiableList(exprs);
  }

  public List<String> getVariableTypes() {
    return Collections.unmodifiableList(variableTypes);
  }

  public List<Pair<Boolean, Integer>> getIncomingTrans() {
    return Collections.unmodifiableList(incomingTrans);
  }

  public Integer getOutgoingTransTrue() {
    return outgoingTransTrue;
  }

  public Integer getOutgoingTransFalse() {
    return outgoingTransFalse;
  }

  /**
   * Returns the number of child blocks.
   *
   * @return the number of child blocks.
   */
  public Integer getNumChildren() {
    return numChildren;
  }

  /**
   * Returns the number of incoming transitions.
   *
   * @return the number of incoming transitions.
   */
  public Integer getIncomingTransCount() {
    return getIncomingTrans().size();
  }


  /**
   * Returns true if the other block has the same types of variables.
   *
   * @param other Other block to compare
   * @return True if both blocks satisfy the condition, false otherwise
   */
  public boolean hasSameVariableTypes(Block other) {
    if (other == null) {
      return this == null;
    }

    return getVariableTypes().containsAll(other.getVariableTypes())
        && other.getVariableTypes().containsAll(getVariableTypes());
  }

  /**
   * Returns true if the other blocks has the same number of incoming and outgoing transitions.
   *
   * @param other Other block to compare
   * @return True if both blocks satisfy the condition, false otherwise
   */
  public boolean hasEqualNumberOfTransitions(Block other) {
    if (other == null) {
      return this == null;
    }

    return getIncomingAndOutgoingTransCount().equals(other.getIncomingAndOutgoingTransCount());
  }

  /**
   * Returns true if the other block has the same number of child blocks.
   *
   * @param other Other block to compare
   * @return True if both blocks satisfy the condition, false otherwise
   */
  public boolean hasSameNumberOfChildren(Block other) {
    if (other == null) {
      return this == null;
    }

    return getNumChildren().equals(other.getNumChildren());
  }

  /**
   * Returns the number of incoming and outgoing transitions as a Pair.
   *
   * @return the number of incoming and outgoing transitions.
   */
  public Pair<Integer, Integer> getIncomingAndOutgoingTransCount() {
    return new Pair<>(getIncomingTransCount(), numOutgoingTrans);
  }
}
