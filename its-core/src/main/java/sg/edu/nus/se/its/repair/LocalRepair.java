package sg.edu.nus.se.its.repair;

import static sg.edu.nus.se.its.util.constants.Constants.ARITH_OPS;
import static sg.edu.nus.se.its.util.constants.Constants.BINARY_OPS_PYTHON;
import static sg.edu.nus.se.its.util.constants.Constants.COMP_OPS;
import static sg.edu.nus.se.its.util.constants.Constants.FUNCS_UNARY_PYTHON;
import static sg.edu.nus.se.its.util.constants.Constants.FUNCTION_CALL;
import static sg.edu.nus.se.its.util.constants.Constants.GET_ELEMENT;
import static sg.edu.nus.se.its.util.constants.Constants.LOGIC_OPS;
import static sg.edu.nus.se.its.util.constants.Constants.SEQ_BINARY_ASSIGN_FUNCS_PYTHON;
import static sg.edu.nus.se.its.util.constants.Constants.SEQ_BINARY_FUNCS_PYTHON;
import static sg.edu.nus.se.its.util.constants.Constants.SEQ_INIT_PYTHON;
import static sg.edu.nus.se.its.util.constants.Constants.SEQ_UNARY_FUNCS_PYTHON;
import static sg.edu.nus.se.its.util.constants.Constants.STRING_APPEND;
import static sg.edu.nus.se.its.util.constants.Constants.STRING_FORMAT;
import static sg.edu.nus.se.its.util.constants.Constants.UNARY_OPS_PYTHON;
import static sg.edu.nus.se.its.util.constants.Constants.VAR_RET;

import java.util.List;
import java.util.Map;
import org.javatuples.Pair;
import org.javatuples.Triplet;
import sg.edu.nus.se.its.model.Constant;
import sg.edu.nus.se.its.model.Expression;
import sg.edu.nus.se.its.model.Operation;
import sg.edu.nus.se.its.model.Variable;

/** Represents one possible repair at one location for a given possible variable mapping. */
public class LocalRepair {

  private Map<Variable, Variable> mapping; // mapping used for the particular repair
  private float cost; // cost associated with that particular fix

  // variable to repair, expression in incorrect program,
  // followed by expression in correct program
  private Triplet<Variable, Expression, Expression> repairedVariable;
  private Pair<Integer, Integer> errorLocation; // (mapping of the erroneous blocks)
  private String funcName; // name of function being fixed

  /**
   * Constructor that represents one possible repair candidate. The repair candidate may not always
   * be valid, or need a sequence of RepairCandidates to fix the problem.
   *
   * @param mapping Mapping of variables for particular repair
   * @param cost incurred for particular repair
   * @param repairedVariable variable to be repaired with the repaired expression
   * @param errorLocation error location repair should take place in
   * @throws Exception when error location or mapping is not valid
   */
  public LocalRepair(
      Map<Variable, Variable> mapping,
      float cost,
      Triplet<Variable, Expression, Expression> repairedVariable,
      String funcName,
      Pair<Integer, Integer> errorLocation)
      throws Exception {
    this.mapping = mapping;
    this.cost = cost;
    this.repairedVariable = repairedVariable;
    this.errorLocation = errorLocation;
    this.funcName = funcName;
  }

  /** Generates pretty printed String representation of the given list of repair candidates. */
  public static String toString(List<LocalRepair> listOfLocalRepairs) {
    StringBuilder prettyPrintRepair = new StringBuilder();
    for (LocalRepair localRepair : listOfLocalRepairs) {
      // todo: temporary
      prettyPrintRepair.append(toString(localRepair));
    }
    String result = prettyPrintRepair.toString();
    // remove trailing \n
    if (result.length() > 1 && result.endsWith("\n")) {
      result = result.substring(0, result.length() - 1);
    }
    return result;
  }

  /** Generates pretty printed String representation of the given repair candidates. */
  public static String toString(LocalRepair localRepair) {
    StringBuilder prettyPrintRepair = new StringBuilder();
    Expression expr1 = localRepair.repairedVariable.getValue1();
    Expression expr2 = localRepair.repairedVariable.getValue2();
    Variable var = localRepair.repairedVariable.getValue0();
    Integer loc1 = localRepair.errorLocation.getValue1();
    if (expr1 != null && expr2 != null) {
      // Optimize ite print by highlighting difference
      if (expr1.getType().equals("Operation")
          && expr2.getType().equals("Operation")
          && ((Operation) expr1).getName().equals("ite")
          && ((Operation) expr2).getName().equals("ite")) {
        Operation exprOp1 = (Operation) expr1;
        Operation exprOp2 = (Operation) expr2;

        Pair<String, String> iteDiff = findDifferenceBetweenTwoIteOperations(exprOp1, exprOp2, var);
        String expr1String = iteDiff.getValue0();
        String expr2String = iteDiff.getValue1();

        // indicating removal of else branch
        if (!expr1String.isEmpty() && expr2String.isEmpty() && expr1String.startsWith("Else:")) {
          prettyPrintRepair
              .append("Remove the else branch: ")
              .append(var)
              .append(" =")
              .append(expr1String.substring(5));
        } else {
          prettyPrintRepair.append("Change ").append(var).append(" = ite(");

          prettyPrintRepair
              .append(expr1String)
              .append(") to ")
              .append(var)
              .append(" = ite(")
              .append(expr2String)
              .append(")");
        }

      } else {
        prettyPrintRepair
            .append("Change ")
            .append(var)
            .append(" = ")
            .append(prettyPrintExpr(expr1, var))
            .append(" to ")
            .append(var)
            .append(" = ")
            .append(prettyPrintExpr(expr2, var));
      }

    } else if (expr1 == null) {
      // need to delete expression
      prettyPrintRepair
          .append("Add ")
          .append(var)
          .append(" = ")
          .append(prettyPrintExpr(expr2, var));
    } else {
      prettyPrintRepair
          .append("Delete ")
          .append(var)
          .append(" = ")
          .append(prettyPrintExpr(expr1, var));
    }
    prettyPrintRepair.append(" at location ").append(loc1);
    prettyPrintRepair.append("\n");
    return prettyPrintRepair.toString();
  }

  private static String prettyPrintStrAppend(Operation operation, Variable variable) {
    StringBuilder prettyPrint = new StringBuilder();
    prettyPrint.append(operation.getName()).append("(");

    for (int i = 1; i < operation.getArgs().size(); i++) {
      Expression arg = operation.getArgs().get(i);

      // Print StrFormat in the form of StrFormat(var, var)
      // Pretty print StrFormat args if they are operations
      if (arg.getType().equals("Operation") && ((Operation) arg).getName().equals(STRING_FORMAT)) {
        Operation strFormatOp = (Operation) arg;
        prettyPrint.append(STRING_FORMAT + "(");
        for (int j = 0; j < strFormatOp.getArgs().size(); j++) {
          Expression strFormatArg = strFormatOp.getArgs().get(j);
          prettyPrint.append(prettyPrintExpr(strFormatArg, variable));

          if (j != strFormatOp.getArgs().size() - 1) {
            prettyPrint.append(", ");
          }
        }
        prettyPrint.append(")");
      } else {
        prettyPrint.append(prettyPrintExpr(arg, variable));
      }

      if (i != operation.getArgs().size() - 1) {
        prettyPrint.append(", ");
      }
    }
    prettyPrint.append(")");
    return prettyPrint.toString();
  }

  private static String prettyPrintExpr(Expression expr, Variable variable) {
    if (expr instanceof Constant) {
      return ((Constant) expr).getValue();
    } else if (expr instanceof Variable) {
      String name = expr.toString();
      return Variable.isPrimedName(name) ? Variable.asUnprimedVariableName(name) : name;
    } else if (expr instanceof Operation) {
      if (((Operation) expr).getName().equals("ite")) {
        return prettyPrintIteExpr((Operation) expr, variable);
      }

      List<Expression> args = ((Operation) expr).getArgs();
      String operationName = ((Operation) expr).getName();
      StringBuilder prettyPrint = new StringBuilder();
      if (operationName.equals(STRING_APPEND)) {
        prettyPrint.append(prettyPrintStrAppend((Operation) expr, variable));
      } else if (operationName.equals(STRING_FORMAT)) {
        prettyPrint.append(STRING_FORMAT + "(");
        prettyPrint = buildOperationString(prettyPrint, args, variable, 0);
        prettyPrint.append(")");
      } else if (operationName.equals("Slice")) {
        boolean notNone = false;
        for (int i = 0; i < args.size(); i++) {
          if (!(args.get(i) instanceof Constant
              && ((Constant) args.get(i)).getValue().equals("None"))) {
            prettyPrint.append(prettyPrintExpr(args.get(i), variable));
            notNone = true;
          }

          if (i != args.size() - 1) {
            prettyPrint.append(":");
          } else {
            if (notNone) {
              prettyPrint.deleteCharAt(prettyPrint.length() - 1);
            }
          }
        }
      } else if (SEQ_INIT_PYTHON.containsKey(operationName)) {
        String op = SEQ_INIT_PYTHON.get(operationName);
        prettyPrint.append(op.charAt(0));
        for (int i = 0; i < args.size(); i++) {
          prettyPrint.append(prettyPrintExpr(args.get(i), variable));
          if (i != args.size() - 1) {
            prettyPrint.append(", ");
          }
        }
        prettyPrint.append(op.charAt(1));
      } else if (FUNCS_UNARY_PYTHON.contains(operationName)
          || operationName.equals("enumerate")
          || operationName.equals("range")
          || operationName.equals("print")) {
        prettyPrint.append(operationName).append("(");
        prettyPrint = buildOperationString(prettyPrint, args, variable, 0);
        prettyPrint.append(")");
      } else if (UNARY_OPS_PYTHON.containsKey(operationName)) {
        prettyPrint.append(UNARY_OPS_PYTHON.getOrDefault(operationName, operationName)).append("(");
        for (int i = 0; i < args.size(); i++) {
          prettyPrint.append(prettyPrintExpr(args.get(i), variable));
          if (i != args.size() - 1) {
            prettyPrint.append(", ");
          }
        }
        prettyPrint.append(")");
      } else if (SEQ_UNARY_FUNCS_PYTHON.contains(operationName)
          || SEQ_BINARY_ASSIGN_FUNCS_PYTHON.contains(operationName)
          || SEQ_BINARY_FUNCS_PYTHON.contains(operationName)) {
        prettyPrint
            .append(prettyPrintExpr(args.get(0), variable))
            .append(".")
            .append(operationName)
            .append("(");
        prettyPrint = buildOperationString(prettyPrint, args, variable, 1);
        prettyPrint.append(")");
      } else if (operationName.equals(GET_ELEMENT)) {
        // for python
        prettyPrint.append(prettyPrintExpr(args.get(0), variable)).append("[");
        // negative index in GetElement for python
        if (args.get(1) instanceof Operation
            && ((Operation) args.get(1)).getName().equals("USub")) {
          prettyPrint.append("-").append(((Operation) args.get(1)).getArgs().get(0)).append("]");
        } else {
          prettyPrint.append(prettyPrintExpr(args.get(1), variable)).append("]");
        }
      } else if (operationName.equals(FUNCTION_CALL)) {
        prettyPrint.append(prettyPrintExpr(args.get(0), variable)).append("(");
        for (int i = 1; i < args.size(); i++) {
          prettyPrint.append(prettyPrintExpr(args.get(i), variable));
          if (i != args.size() - 1) {
            prettyPrint.append(", ");
          }
        }
        prettyPrint.append(")");
      } else {
        for (int i = 0; i < args.size(); i++) {
          Expression arg = args.get(i);
          prettyPrint.append(prettyPrintExpr(arg, variable));

          if ((LOGIC_OPS.contains(operationName)
              || ARITH_OPS.contains(operationName)
              || COMP_OPS.contains(operationName)
              || BINARY_OPS_PYTHON.containsKey(operationName)
              || SEQ_UNARY_FUNCS_PYTHON.contains(operationName)
              || FUNCS_UNARY_PYTHON.contains(operationName))
              && i == args.size() - 1) {
            continue;
          }

          // to pretty print python operators
          prettyPrint
              .append(" ")
              .append(BINARY_OPS_PYTHON.getOrDefault(operationName, operationName))
              .append(" ");
        }
      }

      if (prettyPrint.length() == 0) {
        return expr.toString();
      }
      return prettyPrint.toString();
    } else {
      return expr == null ? "FuncCall" : expr.getType();
    }
  }

  private static StringBuilder buildOperationString(StringBuilder builder, List<Expression> args,
                                                    Variable variable, int index) {
    StringBuilder newBuilder = new StringBuilder(builder.toString());
    for (int i = index; i < args.size(); i++) {
      newBuilder.append(prettyPrintExpr(args.get(i), variable));
      if (i != args.size() - 1) {
        newBuilder.append(", ");
      }
    }
    return newBuilder;
  }

  /**
   * Print out special case of if-then statements.
   *
   * @param expr Operation 'ite'
   * @return Representative version of
   */
  private static String prettyPrintIteExpr(Operation expr, Variable variable) {
    List<Expression> args = expr.getArgs();
    StringBuilder prettyPrint = new StringBuilder();
    prettyPrint.append("ite(");
    for (int i = 0; i < args.size(); i++) {
      String prefix;
      if (i == 0) {
        prefix = " ";
      } else if (i == 1) {
        prefix = "If { ";
      } else {
        prefix = "Else { ";
        // to remove else branch that does not exist
        if (args.get(i).equals(variable)) {
          prettyPrint.delete(prettyPrint.length() - 2, prettyPrint.length() - 1);
          break;
        }
      }
      if (args.get(i).getType().equals("Constant")
          || (args.get(i) instanceof Operation
          && ARITH_OPS.contains(((Operation) args.get(i)).getName()))) {
        prefix += variable.getUnprimedName() + " = ";
      }

      prettyPrint.append(prefix).append(prettyPrintExpr(args.get(i), variable));
      if (i != 0) {
        prettyPrint.append(" } ");
      }
      if (i != args.size() - 1) {
        prettyPrint.append(", ");
      }
    }

    prettyPrint.append(")");

    return prettyPrint.toString();
  }

  /**
   * Find the strings that are different in an ite operation split into cond, if and else.
   *
   * @param expr the original ite expression
   * @param expr2 the repaired ite expression
   * @param var the variable to repair
   * @return a pair of strings that represent the difference for the two ite expression
   */
  private static Pair<String, String> findDifferenceBetweenTwoIteOperations(
      Operation expr, Operation expr2, Variable var) {
    List<Expression> expr1ArgList = expr.getArgs();
    List<Expression> expr2ArgList = expr2.getArgs();
    StringBuilder original = new StringBuilder();
    StringBuilder repaired = new StringBuilder();
    for (int i = 0; i < 3; i++) {
      Expression expr1Arg = expr1ArgList.get(i);
      Expression expr2Arg = expr2ArgList.get(i);
      if (i == 0) {
        original.append(prettyPrintExpr(expr1Arg, var)).append(" ");
        repaired.append(prettyPrintExpr(expr2Arg, var)).append(" ");
        continue;
      }
      if (!expr1Arg.equals(expr2Arg)) {
        if (i == 1) {
          original.append("If{ ");
          repaired.append("If{ ");
          if (isIte(expr1Arg) && isIte(expr2Arg)) {
            Pair<String, String> iteString =
                findDifferenceBetweenTwoIteOperations(
                    (Operation) expr1Arg, (Operation) expr2Arg, var);
            original.append(iteString.getValue0()).append(" } ");
            repaired.append(iteString.getValue1()).append(" } ");
          } else {
            String prefix1 = getClangPrefix(expr1Arg, var);
            String prefix2 = getClangPrefix(expr2Arg, var);

            original.append(prefix1).append(prettyPrintExpr(expr1Arg, var)).append(" } ");
            repaired.append(prefix2).append(prettyPrintExpr(expr2Arg, var)).append(" } ");
          }
        } else {
          // same var return indicates no else branch
          if (expr2Arg.equals(var)) {
            original.append("Else{ ").append(prettyPrintExpr(expr1Arg, var)).append(" } ");
            continue;
          }
          original.append("Else { ");
          repaired.append("Else { ");
          if (isIte(expr1Arg) && isIte(expr2Arg)) {
            Pair<String, String> iteString =
                findDifferenceBetweenTwoIteOperations(
                    (Operation) expr1Arg, (Operation) expr2Arg, var);
            original.append(iteString.getValue0()).append(" }");
            repaired.append(iteString.getValue1()).append(" }");
          } else {
            String prefix1 = getClangPrefix(expr1Arg, var);
            String prefix2 = getClangPrefix(expr2Arg, var);
            original.append(prefix1).append(prettyPrintExpr(expr1Arg, var)).append(" } ");
            repaired.append(prefix2).append(prettyPrintExpr(expr2Arg, var)).append(" } ");
          }
        }
      }
    }
    return Pair.with(original.toString(), repaired.toString());
  }

  private static boolean isIte(Expression expr) {
    return expr.getType().equals("Operation") && ((Operation) expr).getName().equals("ite");
  }

  private static String getClangPrefix(Expression expression, Variable var) {
    if (expression instanceof Constant
        || (expression instanceof Operation
        && ARITH_OPS.contains(((Operation) expression).getName()))) {
      return var.getUnprimedName().equals(VAR_RET) ? " return " : (var.getUnprimedName() + " = ");
    } else {
      return "";
    }
  }

  public Map<Variable, Variable> getMapping() {
    return mapping;
  }

  public float getCost() {
    return cost;
  }

  public void setCost(float cost) {
    this.cost = cost;
  }

  public Triplet<Variable, Expression, Expression> getRepairedVariable() {
    return repairedVariable;
  }

  public void setRepairedVariable(Triplet<Variable, Expression, Expression> repairedVariable) {
    this.repairedVariable = repairedVariable;
  }

  public Pair<Integer, Integer> getErrorLocation() {
    return errorLocation;
  }

  public String getFuncName() {
    return funcName;
  }
}