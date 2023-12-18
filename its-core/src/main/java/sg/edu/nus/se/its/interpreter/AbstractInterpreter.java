package sg.edu.nus.se.its.interpreter;

import static sg.edu.nus.se.its.util.constants.Constants.UNDEFINED;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import org.javatuples.Pair;
import sg.edu.nus.se.its.model.Constant;
import sg.edu.nus.se.its.model.Expression;
import sg.edu.nus.se.its.model.Function;
import sg.edu.nus.se.its.model.Input;
import sg.edu.nus.se.its.model.Memory;
import sg.edu.nus.se.its.model.Operation;
import sg.edu.nus.se.its.model.Program;
import sg.edu.nus.se.its.model.Variable;
import sg.edu.nus.se.its.util.UtilFunctions;
import sg.edu.nus.se.its.util.constants.Constants;

/** Abstract interpreter as base class for concrete instances. */
public abstract class AbstractInterpreter implements Interpreter {

  private Integer timeout;
  private double startTime;
  private String entryFunctionName;
  private Program program = null;
  private Memory memory = null;
  private Trace trace = null;
  private String functionName = null;
  private int location;

  protected AbstractInterpreter() {
    this.timeout = Constants.DEFAULT_TIMEOUT_INTERPRETATION;
    this.entryFunctionName = Constants.DEFAULT_ENTRY_FUNCTION_NAME;
  }

  protected AbstractInterpreter(Integer timeout, String entryFunctionName) {
    this.timeout = timeout;
    this.entryFunctionName = entryFunctionName;
  }

  private int getLocation() {
    return location;
  }

  private void setLocation(int location) {
    this.location = location;
  }

  private String getFunctionName() {
    return functionName;
  }

  private void setFunctionName(String fnc) {
    this.functionName = fnc;
  }

  /**
   * Sets an internal timeout for the interpretation/execution of the program.
   *
   * @param newTimeout -- timeout in seconds
   */
  public void setTimeout(int newTimeout) {
    this.timeout = newTimeout;
  }

  private Program getProgram() {
    return program;
  }

  private Trace getTrace() {
    return trace;
  }

  public Trace executeProgram(Program program) {
    return executeProgram(program, null, null);
  }

  public Trace executeProgram(Program program, Input inputs) {
    return executeProgram(program, null, inputs);
  }

  /**
   * Executes the given program with regard to the provided parameters.
   *
   * @param theProgram -- the program to execute
   * @param theMemory -- memory for the execution, can be null
   * @param input -- inputs that should be provided to the program during execution, can be null
   * @return Trace object as result of the interpreted execution
   */
  private Trace executeProgram(Program theProgram, Memory theMemory, Input input) {

    this.program = theProgram;

    input = Optional.ofNullable(input).orElseGet(Input::new);

    final Function entryFunction =
        Optional.ofNullable(program.getFunctionForName(entryFunctionName)).orElseThrow(
            () -> new RuntimeException(String.format("Unknown function: '%s'", entryFunctionName)));
    this.trace = new Trace();

    /* Initialize the memory */
    this.memory = Optional.ofNullable(theMemory).orElseGet(Memory::new);

    String[] inputs = input.getInputs();
    memory.put(Constants.VAR_IN, inputs);
    if (!memory.containsKey(Constants.VAR_OUT)) {
      memory.put(Constants.VAR_OUT, "");
    }
    memory.put(Constants.VAR_RET, UNDEFINED);

    String[] args = input.getArgs();
    if (args.length != entryFunction.getParams().size()) {
      throw new RuntimeException(String.format("Wrong number of args: expected %s, got %s",
          entryFunction.getParams().size(), args.length));
    }
    for (String var : entryFunction.getTypes().keySet()) {
      memory.put(var, UNDEFINED);
    }

    for (int i = 0; i < args.length; i++) {
      String var = entryFunction.getParams().get(i).getValue0();
      String type = entryFunction.getParams().get(i).getValue1();
      String arg = args[i];
      memory.put(var, convert(arg, type));
    }

    startTime = UtilFunctions.secondsSinceEpoch();
    Trace result = executeFunction(entryFunction, memory);
    this.program = null;
    return result;
  }

  /**
   * Executes the given block with regard to the provided parameters.
   *
   * @param function -- the function to execute
   * @param memory -- memory for the execution, can be null
   * @param loc -- the location of the function to execute
   * @return TraceEntry object as result of the interpreted execution
   */
  public TraceEntry executeBlock(Function function, Memory memory, int loc) {
    setFunctionName(function.getName());
    setLocation(loc);
    for (Pair<String, Expression> p : function.getExprs(getLocation())) {
      String var = p.getValue0();
      Expression expr = p.getValue1();
      Object val = execute(expr, memory);
      String varp = Variable.asPrimedVariableName(var);
      String vtype;
      if (Objects.equals(var, Constants.VAR_RET)) {
        vtype = function.getRettype();
      } else {
        vtype = Optional.ofNullable(function.getTypes().get(var)).orElse("*");
      }
      memory.put(varp, convert(val, vtype));
      if (Objects.equals(var, Constants.VAR_RET) && !UtilFunctions.isUndefined(val)) {
        break;
      }
    }

    Pair<Memory, Memory> memPair = processMemory(memory);
    return new TraceEntry(getFunctionName(), getLocation(), memPair.getValue1());
  }


  /**
   * Executes the given block with regard to the provided parameters.
   *
   * @param function -- the function to execute
   * @param block -- the block to execute
   * @param memory -- memory for the execution, can be null
   * @return TraceEntry object as result of the interpreted execution
   */
  public TraceEntry executeBlock(Function function, List<Pair<String, Expression>> block,
      Memory memory) {
    for (Pair<String, Expression> p : block) {
      String var = p.getValue0();
      Expression expr = p.getValue1();
      Object val = execute(expr, memory);
      String varp = Variable.asPrimedVariableName(var);
      String vtype;
      if (Objects.equals(var, Constants.VAR_RET)) {
        vtype = function.getRettype();
      } else {
        vtype = Optional.ofNullable(function.getTypes().get(var)).orElse("*");
      }
      memory.put(varp, convert(val, vtype));
      if (Objects.equals(var, Constants.VAR_RET) && !UtilFunctions.isUndefined(val)) {
        break;
      }
    }

    Pair<Memory, Memory> memPair = processMemory(memory);
    return new TraceEntry(getFunctionName(), getLocation(), memPair.getValue1());
  }

  /**
   * Executes the provided program element with respect to the given memory.
   *
   * @param executable -- executable Object
   * @param memory -- Memory object
   * @return result of execution
   */
  public Object execute(Executable executable, Memory memory) {
    /* Check for timeout. */
    double nowTime = UtilFunctions.secondsSinceEpoch();
    if (timeout != null && startTime + timeout < nowTime) {
      throw new RuntimeException(String.format("Timeout (%.3f)", nowTime - startTime));
    }

    return executable.execute(memory, this);
  }

  public abstract Object convert(Object value, String type);


  /**
   * Executes a function and produces a Trace object.
   *
   * @param function -- Function object to execute
   * @param memory -- Memory object
   * @return result of execution
   */
  public Trace executeFunction(Function function, Memory memory) {
    setFunctionName(function.getName());
    setLocation(function.getInitloc());
    while (true) {
      for (Pair<String, Expression> p : function.getExprs(getLocation())) {
        String var = p.getValue0();
        Expression expr = p.getValue1();
        Object val = execute(expr, memory);
        String varp = Variable.asPrimedVariableName(var);
        String vtype;
        if (Objects.equals(var, Constants.VAR_RET)) {
          vtype = function.getRettype();
        } else {
          vtype = Optional.ofNullable(function.getTypes().get(var)).orElse("*");
        }
        memory.put(varp, convert(val, vtype));
        if (Objects.equals(var, Constants.VAR_RET) && !UtilFunctions.isUndefined(val)) {
          break;
        }
      }
      Pair<Memory, Memory> memPair = processMemory(memory);
      getTrace().add(getFunctionName(), getLocation(), memPair.getValue1());
      memory = memPair.getValue0();
      Object ret = Optional.ofNullable(memory.get(Constants.VAR_RET)).orElse(UNDEFINED);
      if (!UtilFunctions.isUndefined(ret)) {
        break;
      }
      int numtrans = function.getTransCount(getLocation());
      if (numtrans == 0) {
        break;
      } else if (numtrans == 1) {
        setLocation(function.getTrans(getLocation(), true));
      } else {
        Object cond = memory.get(Constants.VAR_COND);
        UtilFunctions.assertType(cond, Boolean.class);
        setLocation(function.getTrans(getLocation(), (boolean) cond));
      }
    }
    return getTrace();
  }

  private Pair<Memory, Memory> processMemory(Memory mem) {
    Memory newMem = new Memory();
    Set<String> keySet = new HashSet<>(mem.keySet());
    for (String variableName : keySet) {
      Object value = mem.get(variableName);
      if (Variable.isPrimedName(variableName)) {
        String varup = Variable.asUnprimedVariableName(variableName);
        newMem.put(varup, value);
      } else {
        String varp = Variable.asPrimedVariableName(variableName);
        if (!mem.containsKey(varp)) {
          newMem.put(variableName, value);
          mem.put(varp, value);
        }
      }
    }
    return Pair.with(newMem, mem);
  }

  /**
   * Executes the given operation with regard to the memory instance.
   *
   * @param operation -- Operation object
   * @param memory -- Memory object
   * @return the result of the execution
   */
  public Object executeOperation(Operation operation, Memory memory) {
    List<Expression> args = operation.getArgs();
    String operationName = operation.getName();
    Memory clone = memory.clone();
    if (getUnaryOps().contains(operationName)) {
      if (args.size() != 1 && !getBinaryOps().contains(operationName)) {
        throw new RuntimeException(
            String.format("Got <>1 args for unary op in '%s'", operationName));
      }
      if (args.size() == 1) {
        return executeUnaryOp(operationName, args.get(0), clone);
      }
    }

    if (getBinaryOps().contains(operationName)) {
      if (args.size() != 2) {
        throw new RuntimeException(
            String.format("Got <>2 args for binary op in '%s'", operationName));
      }

      return executeBinaryOp(operationName, args.get(0), args.get(1), clone);
    }

    if (getSpecialOps().contains(operationName)) {
      return executeSpecialOp(operation, clone);
    }

    if (getSpecialFunctions().contains(operationName)) {
      List<Object> argList = executeOpArgs(operation, clone);
      return executeSpecialFunction(operationName, argList, clone);
    }

    switch (operationName) {
      case "ite":
        return executeIte(operation, clone);
      case "ListHead":
        return executeListHead(operation, clone);
      case "ListTail":
        return executeListTail(operation, clone);
      case "FuncCall":
        return executeFuncCall(operation, clone);
      default:
        throw new RuntimeException(String.format("Unknown operator: '%s'", operationName));
    }
  }

  private List<Object> executeFuncCallArgs(Operation op, Memory mem) {
    List<Object> args = new ArrayList<>();
    for (int i = 1; i < op.getArgs().size(); i++) {
      args.add(execute(op.getArgs().get(i), mem));
    }
    return args;
  }

  private List<Object> executeOpArgs(Operation op, Memory mem) {
    List<Object> args = new ArrayList<>();
    for (int i = 0; i < op.getArgs().size(); i++) {
      args.add(execute(op.getArgs().get(i), mem));
    }
    return args;
  }

  protected Object executeFuncCall(Operation op, Memory mem) {
    Expression funcObject = op.getArgs().get(0);
    UtilFunctions.assertType(funcObject, Variable.class);
    String funcName = ((Variable) funcObject).getName();
    Function fnc = Optional.ofNullable(getProgram().getFunctionForName(funcName))
        .orElseThrow(() -> new RuntimeException(String.format("Unknown function: '%s'", funcName)));
    if (fnc.getParams().size() != op.getArgs().size() - 1) {
      throw new RuntimeException(String.format("Wrong number of args: expected %s, got %s",
          fnc.getParams().size(), op.getArgs().size() - 1));
    }
    List<Object> args = executeFuncCallArgs(op, mem);
    Memory newMem = new Memory();
    newMem.put(Constants.VAR_IN, UNDEFINED);
    newMem.put(Constants.VAR_OUT, UNDEFINED);

    for (int i = 0; i < args.size(); i++) {
      String varName = fnc.getParams().get(i).getValue0();
      newMem.put(varName, args.get(i));
    }
    String oldFnc = getFunctionName();
    int oldLoc = getLocation();
    Trace trace = executeFunction(fnc, newMem);
    setFunctionName(oldFnc);
    setLocation(oldLoc);
    Memory lastTraceMem = trace.getLastEntry().getMem();
    String retp = Variable.asPrimedVariableName(Constants.VAR_RET);
    return Optional.ofNullable(lastTraceMem.get(retp)).orElse(UNDEFINED);
  }

  private Object executeIte(Operation op, Memory mem) {
    Object cond = execute(op.getArgs().get(0), mem);
    UtilFunctions.assertType(cond, Boolean.class);
    if ((boolean) cond) {
      return execute(op.getArgs().get(1), mem);
    } else {
      return execute(op.getArgs().get(2), mem);
    }
  }

  private Object executeListHead(Operation op, Memory mem) {
    Object typeObject = op.getArgs().get(0);
    UtilFunctions.assertType(typeObject, Constant.class);
    Object listObject = execute(op.getArgs().get(1), mem);
    UtilFunctions.assertType(listObject, String[].class);
    String[] list = (String[]) listObject;
    String type = typeObject.toString();
    if (list.length < 1) {
      throw new RuntimeException("ListHead on empty list");
    }
    return convert(list[0], type);
  }

  private Object executeListTail(Operation op, Memory mem) {
    Object listObject = execute(op.getArgs().get(0), mem);
    UtilFunctions.assertType(listObject, String[].class);
    String[] list = (String[]) listObject;
    if (list.length < 1) {
      throw new RuntimeException("ListHead on empty list");
    }
    return Arrays.copyOfRange(list, 1, list.length);
  }

  /**
   * Executes the variable, i.e., retrieves the variable for the current memory instance.
   *
   * @param variable - Variable object
   * @param memory - Memory object
   * @return value of variable in memory
   */
  public Object executeVariable(Variable variable, Memory memory) {
    if (!memory.containsKey(variable.getName())) {
      return UNDEFINED;
    }

    return memory.get(variable.getName());
  }

  /**
   * Executes a constant, i.e., determines its value and the correct data type.
   *
   * @param constant -- Constant object
   * @param memory -- Memory object
   * @return value of Constant object
   */
  public abstract Object executeConstant(Constant constant, Memory memory);

  public abstract List<String> getSpecialOps();

  public abstract List<String> getUnaryOps();

  public abstract List<String> getBinaryOps();

  public abstract List<String> getSpecialFunctions();

  public abstract Object executeSpecialOp(Operation op, Memory mem);

  public abstract Object executeUnaryOp(String opname, Expression arg, Memory mem);

  public abstract Object executeBinaryOp(String opname, Expression arg1, Expression arg2,
      Memory mem);

  public abstract Object executeSpecialFunction(String fncname, List<Object> args, Memory mem);
}
