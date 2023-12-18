package sg.edu.nus.se.its.util.constants;

import static java.util.Map.entry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Constant values used in the intermediate representation of the Intelligent Tutoring System (ITS).
 */
public class Constants {

  /**
   * Custom label for the program's input stream.
   */
  public static final String VAR_IN = "$in";

  /**
   * Custom label for the program's output stream.
   */
  public static final String VAR_OUT = "$out";

  /**
   * Custom label for the program's return value.
   */
  public static final String VAR_RET = "$ret";

  /**
   * Custom label for the expression in an if condition.
   */
  public static final String VAR_COND = "$cond";

  /**
   * Custom label for break.
   */
  public static final String VAR_BREAK = "$break";

  /**
   * Custom label for continue.
   */
  public static final String VAR_CONTINUE = "$continue";

  /**
   * Undefined value during interpretation.
   */
  public static final String UNDEFINED = "<undef>";

  /**
   * Constant String to represent "End of File".
   */
  public static final String EOF = "EOF";

  /**
   * Constant integer to represent the default timeout for interpretation.
   */
  public static final int DEFAULT_TIMEOUT_INTERPRETATION = 5;

  /**
   * Constant String to represent the default entry function to a C program.
   */
  public static final String DEFAULT_ENTRY_FUNCTION_NAME = "main";

  /**
   * Constant ArrayList of computational operators.
   */
  public static final List<String> COMP_OPS =
      new ArrayList<>(Arrays.asList("<=", "<", ">", ">=", "==", "!="));

  /**
   * Constant ArrayList of arithmetic operators.
   */
  public static final List<String> ARITH_OPS =
      new ArrayList<>(Arrays.asList("+", "-", "*", "/", "%"));


  /**
   * Constant ArrayList of logical operators.
   */
  public static final List<String> LOGIC_OPS = new ArrayList<>(Arrays.asList("&&", "||"));

  /**
   * Constant ArrayList of specifiers in printf statements.
   */
  public static final List<String> SPECIFIER_LIST = new ArrayList<>(Arrays.asList("%c", "%.*d",
      "%e", "%E", "%.*f", "%g", "%G", "%hi", "%hu", "%i", "%l", "%ld", "%li", "%lf", "%Lf", "%lu",
      "%lli", "%lld", "%llu", "%o", "%p", "%s", "%u", "%x", "%X", "%n", "%%"));

  public static final List<String> FUNCS = new ArrayList<>(
      Arrays.asList("floor", "ceil", "pow", "abs", "sqrt", "log2", "log10", "log", "exp"));

  public static final List<String> FUNCS_UNARY_PYTHON = new ArrayList<>(
      Arrays.asList("pow", "abs", "len", "float", "int", "str", "input"));

  public static final List<String> FUNCS_NO_ARGS_PYTHON = new ArrayList<>(Arrays.asList("input"));
  public static final Map<String, String> BINARY_OPS_PYTHON =
      new HashMap<>(Map.ofEntries(entry("And", "and"), entry("Or", "or"), entry("Add", "+"),
          entry("AssAdd", "+="), entry("Sub", "-"), entry("Mult", "*"), entry("Div", "/"),
          entry("Mod", "%"), entry("Pow", "**"), entry("LShift", "<<"), entry("RShift", ">>"),
          entry("BitOr", "|"), entry("BitAnd", "&"), entry("BitXor", "^"), entry("FloorDiv", "//"),
          entry("Eq", "=="), entry("NotEq", "!="), entry("Lt", "<"), entry("LtE", "<="),
          entry("Gt", ">"), entry("GtE", ">="), entry("Is", "is"), entry("IsNot", "is not"),
          entry("In", "in"), entry("NotIn", "not in")));

  public static final List<String> SEQ_BINARY_FUNCS_PYTHON = new ArrayList<>(Arrays.asList("append",
      "extend", "pop", "remove", "insert", "add", "union", "discard", "pop"));

  public static final List<String> SEQ_BINARY_ASSIGN_FUNCS_PYTHON = new ArrayList<>(Arrays.asList(
      "get", "isdisjoint", "issubset", "issuperset", "difference", "intersection",
      "symmetric_difference", "count", "index"));

  public static final List<String> SEQ_TERNARY_FUNCS_PYTHON = new ArrayList<>(Arrays.asList(
      "insert"));

  public static final List<String> SEQ_UNARY_FUNCS_PYTHON = new ArrayList<>(Arrays.asList("append",
      "clear", "reverse", "sort", "pop"));

  public static final List<String> SEQ_UNARY_ASSIGN_FUNCS_PYTHON =
      new ArrayList<>(Arrays.asList("items", "keys", "values", "popitem", "copy"));

  public static final Map<String, String> SEQ_INIT_PYTHON = new HashMap<>(Map.ofEntries(
      entry("ListInit", "[]"), entry("list", "[]"), entry("SetInit", "{}"),
      entry("set", "{}"), entry("TupleInit", "()"), entry("tuple", "()"),
      entry("DictInit", "{}"), entry("dict", "{}")));

  public static final Map<String, String> UNARY_OPS_PYTHON = new HashMap<>(Map.ofEntries(
      entry("Invert", "~"), entry("Not", "not"), entry("UAdd", "+"),
      entry("USub", "-")));

  /**
   * Custom label to represent String Append.
   */
  public static final String STRING_APPEND = "StrAppend";

  /**
   * Custom label to represent String format.
   */
  public static final String STRING_FORMAT = "StrFormat";

  /**
   * Custom label to represent conditional branching.
   */
  public static final String CONDITIONAL_OPERATOR = "ite";

  /**
   * Custom label to represent function call.
   */
  public static final String FUNCTION_CALL = "FuncCall";

  /**
   * Custom label to represent `creation of array.
   */
  public static final String ARRAY_CREATE = "ArrayCreate";

  /**
   * Custom label to represent appending of elements into array.
   */
  public static final String ARRAY_ASSIGN = "ArrayAssign";

  /**
   * Custom label to represent array declaration.
   */
  public static final String ARRAY_DECLARATION = "ArrayDeclaration";

  /**
   * Custom label to represent void function calls.
   */
  public static final String VOID_FUNCTION_CALLS = "VoidFunctionCalls";

  /**
   * Custom label to represent python retrieving array/list element by index.
   */
  public static final String GET_ELEMENT = "GetElement";
}
