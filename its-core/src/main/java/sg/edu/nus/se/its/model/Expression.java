package sg.edu.nus.se.its.model;

import java.util.Set;
import sg.edu.nus.se.its.interpreter.Executable;
import sg.edu.nus.se.its.util.JsonSerializable;

/**
 * Represents an expression in the program.
 */
public abstract class Expression implements Executable, JsonSerializable {

  /**
   * The source line number of the expression.
   */
  private int line;

  /**
   * The original expression before post-processing.
   */
  private Expression originalExpr;

  /**
   * Initiates an expression.
   *
   * @param line -- line number in the source code
   */
  protected Expression(int line) {
    super();
    this.line = line;
  }

  public int getLineNumber() {
    return line;
  }

  public Expression prime() {
    return this;
  }

  /**
   * Method to prime variables given a set of already primed variables.
   *
   * @param name set of expressions to prime
   * @return primed expression if it has to be primed
   */
  public Expression prime(Set<String> name) {
    return this;
  }

  public Expression unprime() {
    return this;
  }

  public void setOriginalExpression(Expression expression) {
    this.originalExpr = expression;
  }

  public Expression getOriginalExpr() {
    return originalExpr;
  }

  /**
   * Method call to replace an expression iff the particular expression matches varName. Note that
   * if this expression object is not called varName, it will just return this expression unchanged.
   *
   * @param varName variable which needs to be replaced
   * @param expression expression to replace variable varName with
   * @return final expression after replacement if necessary
   */
  public Expression replace(String varName, Expression expression) {
    return this;
  }

  @Override
  public Object clone() throws CloneNotSupportedException {
    return this;
  }
}
