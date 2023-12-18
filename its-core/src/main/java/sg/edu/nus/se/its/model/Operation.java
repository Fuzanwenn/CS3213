package sg.edu.nus.se.its.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import sg.edu.nus.se.its.interpreter.AbstractInterpreter;

/**
 * Represents an operation as an expression.
 */
public class Operation extends Expression {

  private String name;

  private List<Expression> args;

  /**
   * Initializes an Operation object with the pre-setting the isStatement value to false and the
   * original to null.
   *
   * @param name -- the name of the operation
   * @param args -- the list of arguments, as Expression objects
   * @param line -- the source line number
   */
  public Operation(String name, List<Expression> args, int line) {
    super(line);
    this.name = name;
    this.args = args;
  }

  public String getName() {
    return name;
  }

  public List<Expression> getArgs() {
    return args;
  }

  @Override
  public String toString() {
    List<String> argsList = new ArrayList<>();
    for (Expression argument : args) {
      if (argument != null) {
        argsList.add(argument.toString());
      }
    }
    String argString = argsList.stream().collect(Collectors.joining(", "));
    return String.format("%s(%s)", name, argString);
  }

  @Override
  public String getType() {
    return getClass().getSimpleName();
  }

  @Override
  public Object execute(Memory memory, AbstractInterpreter withInterpreter) {
    return withInterpreter.executeOperation(this, memory);
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof Operation) {
      Operation operation = (Operation) o;
      return Objects.equals(name, operation.name) && Objects.equals(args, operation.args);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, args);
  }

  @Override
  public Expression prime() {
    List<Expression> newArguments = new ArrayList<>();
    for (Expression expression : args) {
      newArguments.add(expression.prime());
    }
    args = newArguments;
    return this;
  }

  @Override
  public Expression prime(Set<String> name) {
    List<Expression> newArgs = new ArrayList<>();
    for (Expression argument : this.args) {
      newArgs.add(argument.prime(name));
    }
    this.args = newArgs;
    return this;
  }

  @Override
  public Expression unprime() {
    List<Expression> newArguments = new ArrayList<>();
    for (Expression expression : args) {
      newArguments.add(expression.unprime());
    }
    args = newArguments;
    return this;
  }

  @Override
  public Expression replace(String varName, Expression expr) {
    List<Expression> newArguments = new ArrayList<>();
    for (Expression expression : args) {
      newArguments.add(expression.replace(varName, expr));
    }
    args = newArguments;
    return this;
  }

  @Override
  public Object clone() throws CloneNotSupportedException {
    List<Expression> newArguments = new ArrayList<>();
    for (Expression expression : args) {
      newArguments.add((Expression) expression.clone());
    }
    return new Operation(name, newArguments, getLineNumber());
  }
}
