package sg.edu.nus.se.its.concretization;

import sg.edu.nus.se.its.model.Expression;

/**
 * Evaluates ITS expression into its string equivalent in the given programming
 * language.
 */
public interface ExpressionEvaluator {
  /**
   * Evaluates and ITS {@code Expression} into its string equivalent in its given programming
   * language.
   *
   * @param expr the {@code Expression} to be evaluated.
   * @return the string equivalent in its given programming language.
   */
  String evaluate(Expression expr);
}
