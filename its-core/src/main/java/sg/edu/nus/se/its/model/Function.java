package sg.edu.nus.se.its.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import org.javatuples.Pair;
import sg.edu.nus.se.its.interpreter.AbstractInterpreter;
import sg.edu.nus.se.its.interpreter.Executable;
import sg.edu.nus.se.its.util.JsonSerializable;

/** Represents a function inside a program. */
public class Function implements JsonSerializable, Executable {

  private String name;
  private String rettype;
  private int initloc;
  private int endloc;
  private ArrayList<Pair<String, String>> params;
  private HashMap<Integer, ArrayList<Pair<String, Expression>>> locexprs;
  private HashMap<Integer, HashMap<Boolean, Integer>> loctrans;
  private HashMap<Integer, String> locdescs;
  private HashMap<String, String> types;

  /**
   * Creates new Function object for the given function name, its parameters (tuples of name and
   * type) and the return type.
   */
  public Function(String name, ArrayList<Pair<String, String>> params, String rettype) {
    this.name = name;
    this.params = params;
    this.rettype = rettype;

    this.locexprs = new HashMap<>();
    this.loctrans = new HashMap<>();
    this.locdescs = new HashMap<>();
    this.types = new HashMap<>();
  }

  public HashMap<String, String> getTypes() {
    return types;
  }

  public ArrayList<Pair<String, String>> getParams() {
    return params;
  }

  public String getRettype() {
    return rettype;
  }

  public String getName() {
    return this.name;
  }

  public int getInitloc() {
    return initloc;
  }

  public HashMap<Integer, String> getLocdescs() {
    return locdescs;
  }

  public int getEndloc() {
    return endloc;
  }

  /** Retrieve the expressions at the given code location. */
  public List<Pair<String, Expression>> getExprs(int loc) {
    if (!locexprs.keySet().contains(loc)) {
      throw new RuntimeException(String.format("Unknown location: %d", loc));
    }
    return locexprs.get(loc);
  }

  /** Retrieve Expression object for the given variable at the code location. */
  public Expression getExprs(int loc, Variable var) {
    List<Pair<String, Expression>> exprs = getExprs(loc);
    for (Pair<String, Expression> exprPair : exprs) {
      if (exprPair.getValue0().equals(var.toString())) {
        return exprPair.getValue1();
      }
    }
    throw new RuntimeException(String.format("Unknown var: %s at loc: %d", var.toString(), loc));
  }

  public boolean locExist(int loc) {
    return locexprs.keySet().contains(loc);
  }

  /** Retrieves the number of incoming transitions into a code location. */
  public int getTransCount(int loc) {
    if (!loctrans.containsKey(loc)) {
      throw new RuntimeException(String.format("Unknown location: '%d'", loc));
    }
    int result = 0;
    for (Integer v : loctrans.get(loc).values()) {
      if (v != null) {
        result += 1;
      }
    }
    return result;
  }

  public Set<Integer> getLocations() {
    return locexprs.keySet();
  }

  /**
   * Removing either the true or false branch from locations that are not longer in use. This is
   * akin to removing an edge in the Control Flow Graph.
   *
   * @param loc location to be removed
   * @param value the edge to remove
   */
  public void removeTransitionBranch(int loc, boolean value) {
    assert (loctrans.containsKey(loc));
    HashMap<Boolean, Integer> hashMap = loctrans.get(loc);
    if (hashMap.containsKey(value)) {
      hashMap.put(value, null);
    }
  }

  public HashMap<Integer, ArrayList<Pair<String, Expression>>> getLocexprs() {
    return locexprs;
  }

  public HashMap<Integer, HashMap<Boolean, Integer>> getLoctrans() {
    return loctrans;
  }

  public Integer getTrans(int loc, boolean cond) {
    return loctrans.getOrDefault(loc, new HashMap<>()).get(cond);
  }

  public String getLocdescAt(int loc) {
    return locdescs.get(loc);
  }

  /**
   * Similar to the toString() method, just with or without the 'original' attribute.
   *
   * @return string representation of function
   */
  public String toString() {
    String functionInfo = String.format("fun %s (%s) : %s", this.name, "", this.rettype);
    String functionParameters = String.join("\n", "Params:", params.toString());
    if (params.size() == 0) {
      functionParameters = "";
    }
    String splitter = String.join("", Collections.nCopies(40, "-"));
    String initloc = String.format("initloc : %d", this.initloc);
    String head = String.join("\n", functionInfo, functionParameters, splitter, initloc);

    StringBuilder body = new StringBuilder();
    for (int loc : this.locexprs.keySet()) {
      body.append(String.format("\nLoc %s (%s)", loc, this.locdescs.get(loc)));
      body.append("\n" + splitter);

      for (Pair<String, Expression> pair : this.locexprs.get(loc)) {
        if (pair.getValue1() != null) {
          body.append(String.format("\n  %s := %s", pair.getValue0(), pair.getValue1().toString()));
        }

      }
      body.append("\n" + splitter);
      body.append(String.format("\n  True -> %s   False -> %s", this.loctrans.get(loc).get(true),
          this.loctrans.get(loc).get(false)));
    }
    return head + body;
  }

  @Override
  public String getType() {
    return getClass().getSimpleName();
  }

  @Override
  public Object execute(Memory memory, AbstractInterpreter withInterpreter) {
    return withInterpreter.executeFunction(this, memory);
  }

  /**
   * Add expression to the function.
   *
   * @param loc current location
   * @param var variable to be assigned
   * @param exprs assigned expression
   * @param idx refers to the index of expression to be added to the list of expressions
   */
  public void addExpr(int loc, String var, Expression exprs, int idx) {

    assert this.locexprs.containsKey(loc);
    assert var != null;
    assert exprs != null;
    this.locexprs.get(loc).add(idx, new Pair<>(var, exprs));
  }

  /**
   * Add expression to the function.
   *
   * @param loc current location
   * @param var variable to be assigned
   * @param exprs assigned expression
   */
  public void addExpr(int loc, String var, Expression exprs) {

    assert this.locexprs.containsKey(loc);
    assert var != null;
    assert exprs != null;
    this.locexprs.get(loc).add(new Pair<>(var, exprs));
  }

  /**
   * Method to add location trans.
   *
   * @param currentLocation location to update
   * @param trueLocation of type Integer rather than int as trueLocation is nullable
   * @param falseLocation of type Integer rather than int as falseLocation is nullable
   */
  public void addLocationTrans(int currentLocation, Integer trueLocation, Integer falseLocation) {
    assert (locdescs.containsKey(currentLocation));

    HashMap<Boolean, Integer> locationTrans = new HashMap<>();
    locationTrans.put(true, trueLocation);
    locationTrans.put(false, falseLocation);
    this.loctrans.put(currentLocation, locationTrans);
  }

  /**
   * Adding a location with a particular description.
   *
   * @param loc current location
   * @param desc description of locations
   * @return updated current location
   */
  public int addLocation(int loc, String desc) {
    if (loc == 0) {
      if (this.loctrans.size() > 0) {
        loc = Collections.max(this.loctrans.keySet()) + 1;
      } else {
        loc = 1;
      }
    }

    if (this.initloc == 0) {
      this.initloc = loc;
    }

    this.locexprs.put(loc, new ArrayList<>());
    HashMap<Boolean, Integer> trans = new HashMap<>();
    trans.put(true, null);
    trans.put(false, null);
    this.loctrans.put(loc, trans);
    this.locdescs.put(loc, desc);
    return loc;
  }

  /**
   * Add type of variable.
   *
   * @param var variable name
   * @param type variable type
   */
  public void addType(String var, String type) {
    assert var != null;
    assert type != null;

    if (this.types.containsKey(var)) {
      return;
    }
    this.types.put(var, type);
  }

  /**
   * Remove locations that are no longer needed. This method will throw an error if the location to
   * be removed does not exist in the first place.
   *
   * @param loc location to be removed
   */
  public void removeLocation(int loc) {
    assert this.locexprs.containsKey(loc);
    this.locexprs.remove(loc);
    assert this.locdescs.containsKey(loc);
    this.locdescs.remove(loc);
    assert this.loctrans.containsKey(loc);
    this.loctrans.remove(loc);
  }

  public void replaceLocExpressions(int location,
      ArrayList<Pair<String, Expression>> locationExprs) {
    locexprs.put(location, locationExprs);
  }

  public String getLocationDesc(int location) {
    assert (locdescs.containsKey(location));
    return locdescs.get(location);
  }
}
