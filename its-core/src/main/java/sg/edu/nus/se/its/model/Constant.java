package sg.edu.nus.se.its.model;

import java.util.Objects;
import sg.edu.nus.se.its.interpreter.AbstractInterpreter;

/**
 * Represents a constant expression.
 */
public class Constant extends Expression {

  private String value;

  public Constant(String value, int line) {
    super(line);
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  @Override
  public String toString() {
    return value;
  }

  @Override
  public String getType() {
    return getClass().getSimpleName();
  }

  @Override
  public Object execute(Memory memory, AbstractInterpreter withInterpreter) {
    return withInterpreter.executeConstant(this, memory);
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof Constant) {
      Constant constant = (Constant) o;
      return Objects.equals(value, constant.value);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }

}
