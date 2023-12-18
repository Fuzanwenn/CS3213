package sg.edu.nus.se.its.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.javatuples.Pair;
import org.javatuples.Triplet;
import sg.edu.nus.se.its.alignment.StructuralMapping;
import sg.edu.nus.se.its.alignment.VariableMapping;
import sg.edu.nus.se.its.errorlocalizer.ErrorLocalisation;
import sg.edu.nus.se.its.interpreter.Trace;
import sg.edu.nus.se.its.model.*;
import sg.edu.nus.se.its.repair.LocalRepair;
import sg.edu.nus.se.its.repair.RepairCandidate;
import sg.edu.nus.se.its.util.constants.Constants;


/**
 * File loading utility method to load inputs and intermediate representations for testing purposes.
 */
public class TestUtils {

  public static final String COMMON_TEST_PATH =
      System.getProperty("user.dir") + "/../common-tests/";

  /**
   * Loads the internal program representation from a stored .json file (old format).
   *
   * @param filePath -- full file path for the json model file, e.g., "arith.c.json"
   * @return Program object
   */
  @Deprecated
  public static Program loadProgramByFilePathOld(String filePath) {
    GsonBuilder builder = new GsonBuilder();
    builder.registerTypeAdapter(Expression.class, new JsonSerializerWithInheritance<Expression>());
    builder.registerTypeAdapter(new TypeToken<Pair<String, Expression>>() {}.getType(),
        new PairDeserializer<>(String.class, Expression.class));
    builder.registerTypeAdapter(new TypeToken<Pair<String, String>>() {}.getType(),
        new PairDeserializer<>(String.class, String.class));
    Gson gson = builder.create();

    File modelFile = new File(filePath);
    try {
      return gson.fromJson(new FileReader(modelFile), Program.class);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Converts a JSON file that represents a Program object in the old format, to a JSON file with
   * the new format.
   *
   * @param filePathOldJsonFormatFile - file path of the JSON file in the old format
   * @param newFilePath - file path for the JSON file in the new format
   * @return success flag
   */
  public static boolean convertProgramJsonFile(String filePathOldJsonFormatFile,
      String newFilePath) {
    Program program = loadProgramByFilePathOld(filePathOldJsonFormatFile);
    return storeProgramAsJsonFile(program, newFilePath);
  }

  /**
   * Stores given program in the JSON format.
   *
   * @param program - Program
   * @param filePath - String
   * @return success
   */
  public static boolean storeProgramAsJsonFile(Program program, String filePath) {
    GsonBuilder builder = new GsonBuilder();
    builder.registerTypeAdapter(Expression.class, new JsonSerializerWithInheritance<Expression>());
    builder.setPrettyPrinting();
    Gson gson = builder.create();
    String value = gson.toJson(program);

    try {
      FileWriter myWriter = new FileWriter(filePath);
      myWriter.write(value);
      myWriter.close();
      return true;
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
  }

  /**
   * convert given program in the JSON format.
   *
   * @param program - Program
   * @return Program in JSON
   */
  public static String convertProgramAsJson(Program program) {
    GsonBuilder builder = new GsonBuilder();
    builder.registerTypeAdapter(Expression.class, new JsonSerializerWithInheritance<Expression>());
    builder.setPrettyPrinting();
    Gson gson = builder.create();
    return gson.toJson(program);
  }

  /**
   * convert given trace in the JSON format.
   *
   * @param trace - Trace
   * @return trace in JSON
   */
  public static String convertTraceAsJson(Trace trace) {
    GsonBuilder builder = new GsonBuilder();
    builder.registerTypeAdapter(Expression.class, new JsonSerializerWithInheritance<Expression>());
    builder.setPrettyPrinting();
    Gson gson = builder.create();
    return gson.toJson(trace);
  }

  /**
   * convert given error location in the JSON format.
   *
   * @param el - ErrorLocalisation
   * @return ErrorLocalisation in JSON
   */
  public static String convertErrorLocalationsAsJson(ErrorLocalisation el) {
    GsonBuilder builder = new GsonBuilder();
    builder.registerTypeAdapter(Expression.class, new JsonSerializerWithInheritance<Expression>());
    builder.setPrettyPrinting();
    Gson gson = builder.create();
    return gson.toJson(el);
  }

  /**
   * convert given repair candidate in the JSON format.
   *
   * @param rc - RepairCandidate
   * @return RepairCandidate in JSON
   */
  public static String convertRepairCandidateAsJson(RepairCandidate rc) {
    GsonBuilder builder = new GsonBuilder().enableComplexMapKeySerialization();;
    RuntimeTypeAdapterFactory<Expression> expressionAdapter = RuntimeTypeAdapterFactory.of(Expression.class, "tokentype")//
            .registerSubtype(Variable.class, "Variable")
            .registerSubtype(Operation.class, "Operation")
            .registerSubtype(Constant.class, "Constant");
    builder.registerTypeAdapterFactory(expressionAdapter);
    builder.setPrettyPrinting();
    Gson gson = builder.create();
    return gson.toJson(rc);
  }

  /**
   * Loads the Program model from the JSON format into the Program object. This version of loading
   * does not match Clara's format.
   *
   * @param filePath - String
   * @return Program object
   */
  public static Program loadProgramByFilePath(String filePath) {
    GsonBuilder builder = new GsonBuilder();
    builder.registerTypeAdapter(Expression.class, new JsonSerializerWithInheritance<Expression>());
    Gson gson = builder.create();
    File modelFile = new File(filePath);
    try {
      return gson.fromJson(new FileReader(modelFile), Program.class);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Loads the Program model from the JSON format into the Program object. This version of loading
   * does not match Clara's format.
   *
   * @param name - String
   * @return Program object
   */
  public static Program loadProgramByName(String name) {
    GsonBuilder builder = new GsonBuilder();
    builder.registerTypeAdapter(Expression.class, new JsonSerializerWithInheritance<Expression>());
    Gson gson = builder.create();

    File modelFile = new File(COMMON_TEST_PATH + name + ".json");

    try {
      return gson.fromJson(new FileReader(modelFile), Program.class);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Loads a file by the provided file name.
   *
   * @param name the file name
   * @return the file with the provided name
   */
  public static File loadFileResourceByName(String name) {
    // return new File("../its-core/src/test/resources/source/" + name);
    return new File(COMMON_TEST_PATH + name);
  }

  /**
   * Loads the inputs for a specific test case.
   *
   * @param name -- source file name
   * @return List of inputs as String objects
   */
  public static List<Input> loadInputsByProgramName(String name) {
    return loadInputsByProgramName(name, false);
  }

  /**
   * Loads the inputs for a specific test case.
   *
   * @param name -- source file name
   * @param ignoreException -- boolean flag
   * @return List of inputs as String objects
   */
  public static List<Input> loadInputsByProgramName(String name, boolean ignoreException) {
    // File inputFile = new File("../its-core/src/test/resources/input/" + name + ".in");
    File inputFile = new File(COMMON_TEST_PATH + name + ".in");
    try {
      Scanner reader = new Scanner(inputFile);
      List<Input> result = new ArrayList<>();
      while (reader.hasNextLine()) {
        Input i = new Input(reader.nextLine().split(" "), null);
        result.add(i);
      }
      reader.close();

      if (result.isEmpty()) {
        return Arrays.asList(new Input(null, null));
      } else {
        return result;
      }
    } catch (FileNotFoundException e) {
      if (ignoreException) {
        return Arrays.asList(new Input(null, null));
      } else {
        e.printStackTrace();
        return null;
      }
    }
  }

  /**
   * Loads the inputs arguments for a specific test case.
   *
   * @param name -- source file name
   * @return List of inputs as String objects
   */
  public static List<Input> loadArgumentsByProgramName(String name, boolean ignoreException) {
    // Map<Integer, String>>
    File inputFile = new File(COMMON_TEST_PATH + name);

    try {
      Scanner reader = new Scanner(inputFile);
      List<Input> result = new ArrayList<>();
      while (reader.hasNextLine()) {
        String[] args = reader.nextLine().split(" ");
        Input i = new Input(null, args);
        result.add(i);
      }
      reader.close();

      if (result.isEmpty()) {
        return Arrays.asList(new Input(null, null));
      } else {
        return result;
      }
    } catch (FileNotFoundException e) {
      if (ignoreException) {
        return Arrays.asList(new Input(null, null));
      } else {
        e.printStackTrace();
        return null;
      }
    }
  }



  /**
   * Load structural mapping from file.
   *
   * @param filePath the file path
   * @return the structural mapping
   */
  public static StructuralMapping loadStructuralMappingFromFile(String filePath) {
    GsonBuilder builder = new GsonBuilder();
    Gson gson = builder.create();
    File modelFile = new File(filePath);
    try {
      return gson.fromJson(new FileReader(modelFile), StructuralMapping.class);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Load variable mapping from file.
   *
   * @param filePath the file path
   * @return the variable mapping
   */
  public static VariableMapping loadVariableMappingFromFile(String filePath) {
    GsonBuilder builder = new GsonBuilder().enableComplexMapKeySerialization();
    builder.registerTypeAdapter(Expression.class, new JsonSerializerWithInheritance<Expression>());
    Gson gson = builder.create();
    File modelFile = new File(filePath);
    try {
      return gson.fromJson(new FileReader(modelFile), VariableMapping.class);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Store structural mapping as json file.
   *
   * @param mapping the mapping
   * @param filePath the file path
   * @return success
   */
  public static boolean storeStructuralMappingAsJsonFile(StructuralMapping mapping,
      String filePath) {
    GsonBuilder builder = new GsonBuilder();
    builder.registerTypeAdapter(Expression.class, new JsonSerializerWithInheritance<Expression>());
    builder.setPrettyPrinting();
    Gson gson = builder.create();
    String value = gson.toJson(mapping);

    try {
      FileWriter myWriter = new FileWriter(filePath);
      myWriter.write(value);
      myWriter.close();
      return true;
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
  }

  /**
   * Store variable mapping as json file.
   *
   * @param mapping the mapping
   * @param filePath the file path
   * @return success
   */
  public static boolean storeVariableMappingAsJsonFile(VariableMapping mapping, String filePath) {
    // Enable complex map-key serialization as VariableMapping contains Map<Variable, Variable>
    GsonBuilder builder = new GsonBuilder().enableComplexMapKeySerialization();
    builder.registerTypeAdapter(Expression.class, new JsonSerializerWithInheritance<Expression>());
    builder.setPrettyPrinting();
    Gson gson = builder.create();
    String value = gson.toJson(mapping);

    try {
      FileWriter myWriter = new FileWriter(filePath);
      myWriter.write(value);
      myWriter.close();
      return true;
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
  }

  /**
   * Stub method to generate variable mapping.
   *
   * @param index Denotes the test cases index for repair test cases.
   */
  public static VariableMapping hardcodeVariableMapping(int index) {
    sg.edu.nus.se.its.alignment.VariableMapping hardcodedMappings =
        new sg.edu.nus.se.its.alignment.VariableMapping();

    if (index == 1) {

      Map<Variable, Variable> mapping1 = new HashMap<>();
      mapping1.put(getVarFromRef(Constants.VAR_RET), getVarFromIncorrect(Constants.VAR_RET));
      mapping1.put(getVarFromRef("-"), getVarFromIncorrect("*"));
      mapping1.put(getVarFromRef("a"), getVarFromIncorrect("x"));
      mapping1.put(getVarFromRef("b"), getVarFromIncorrect("y"));
      hardcodedMappings.add(Constants.DEFAULT_ENTRY_FUNCTION_NAME, mapping1);

      Map<Variable, Variable> mapping2 = new HashMap<>();
      mapping2.put(getVarFromRef(Constants.VAR_RET), getVarFromIncorrect(Constants.VAR_RET));
      mapping2.put(getVarFromRef("-"), getVarFromIncorrect("*"));
      mapping2.put(getVarFromRef("a"), getVarFromIncorrect("y"));
      mapping2.put(getVarFromRef("b"), getVarFromIncorrect("x"));
      hardcodedMappings.add(Constants.DEFAULT_ENTRY_FUNCTION_NAME, mapping2);

    } else if (index == 2) {

      Map<Variable, Variable> mapping1 = new HashMap<>();
      mapping1.put(getVarFromRef(Constants.VAR_RET), getVarFromIncorrect(Constants.VAR_RET));
      mapping1.put(getVarFromRef("-"), getVarFromIncorrect("*"));
      mapping1.put(getVarFromRef("a"), getVarFromIncorrect("a"));
      mapping1.put(getVarFromRef("b"), getVarFromIncorrect("b"));
      hardcodedMappings.add(Constants.DEFAULT_ENTRY_FUNCTION_NAME, mapping1);

      Map<Variable, Variable> mapping2 = new HashMap<>();
      mapping2.put(getVarFromRef(Constants.VAR_RET), getVarFromIncorrect(Constants.VAR_RET));
      mapping2.put(getVarFromRef("-"), getVarFromIncorrect("*"));
      mapping2.put(getVarFromRef("a"), getVarFromIncorrect("b"));
      mapping2.put(getVarFromRef("b"), getVarFromIncorrect("a"));
      hardcodedMappings.add(Constants.DEFAULT_ENTRY_FUNCTION_NAME, mapping2);

    } else if (index == 3) {

      Map<Variable, Variable> mapping1 = new HashMap<>();
      mapping1.put(getVarFromRef(Constants.VAR_RET), getVarFromIncorrect(Constants.VAR_RET));
      mapping1.put(getVarFromRef("-"), getVarFromIncorrect("*"));
      mapping1.put(getVarFromRef("a"), getVarFromIncorrect("x"));
      mapping1.put(getVarFromRef("b"), getVarFromIncorrect("y"));
      mapping1.put(getVarFromRef("c"), getVarFromIncorrect("z"));
      hardcodedMappings.add(Constants.DEFAULT_ENTRY_FUNCTION_NAME, mapping1);

      Map<Variable, Variable> mapping2 = new HashMap<>();
      mapping2.put(getVarFromRef(Constants.VAR_RET), getVarFromIncorrect(Constants.VAR_RET));
      mapping2.put(getVarFromRef("-"), getVarFromIncorrect("*"));
      mapping2.put(getVarFromRef("a"), getVarFromIncorrect("y"));
      mapping2.put(getVarFromRef("b"), getVarFromIncorrect("x"));
      mapping1.put(getVarFromRef("c"), getVarFromIncorrect("z"));
      hardcodedMappings.add(Constants.DEFAULT_ENTRY_FUNCTION_NAME, mapping2);

    } else if (index == 4) {

      Map<Variable, Variable> mapping1 = new HashMap<>();
      mapping1.put(getVarFromRef(Constants.VAR_RET), getVarFromIncorrect(Constants.VAR_RET));
      mapping1.put(getVarFromRef("-"), getVarFromIncorrect("*"));
      mapping1.put(getVarFromRef("a1"), getVarFromIncorrect("a1"));
      mapping1.put(getVarFromRef("b1"), getVarFromIncorrect("b1"));
      mapping1.put(getVarFromRef("a2"), getVarFromIncorrect("a2"));
      mapping1.put(getVarFromRef("b2"), getVarFromIncorrect("b2"));
      mapping1.put(getVarFromRef("X"), getVarFromIncorrect("X"));
      mapping1.put(getVarFromRef("Y"), getVarFromIncorrect("Y"));
      mapping1.put(getVarFromRef(Constants.VAR_OUT), getVarFromIncorrect(Constants.VAR_OUT));
      mapping1.put(getVarFromRef(Constants.VAR_IN), getVarFromIncorrect(Constants.VAR_IN));
      hardcodedMappings.add(Constants.DEFAULT_ENTRY_FUNCTION_NAME, mapping1);

    } else if (index == 5) {

      Map<Variable, Variable> mapping1 = new HashMap<>();
      mapping1.put(getVarFromRef(Constants.VAR_IN), getVarFromIncorrect(Constants.VAR_IN));
      mapping1.put(getVarFromRef(Constants.VAR_OUT), getVarFromIncorrect(Constants.VAR_OUT));
      mapping1.put(getVarFromRef("a"), getVarFromIncorrect("a"));
      hardcodedMappings.add(Constants.DEFAULT_ENTRY_FUNCTION_NAME, mapping1);

    } else if (index == 6) {

      Map<Variable, Variable> mapping1 = new HashMap<>();
      mapping1.put(getVarFromRef("a"), getVarFromIncorrect("a"));
      mapping1.put(getVarFromRef("b"), getVarFromIncorrect("b"));
      mapping1.put(getVarFromRef("i"), getVarFromIncorrect("i"));
      mapping1.put(getVarFromRef(Constants.VAR_COND), getVarFromIncorrect(Constants.VAR_COND));
      hardcodedMappings.add(Constants.DEFAULT_ENTRY_FUNCTION_NAME, mapping1);

    } else if (index == 7) {

      Map<Variable, Variable> mapping1 = new HashMap<>();
      mapping1.put(getVarFromRef("i"), getVarFromIncorrect("i"));
      mapping1.put(getVarFromRef("j"), getVarFromIncorrect("j"));
      mapping1.put(getVarFromRef("k"), getVarFromIncorrect("k"));
      mapping1.put(getVarFromRef("N"), getVarFromIncorrect("N"));
      mapping1.put(getVarFromRef("count"), getVarFromIncorrect("count"));
      mapping1.put(getVarFromRef(Constants.VAR_IN), getVarFromIncorrect(Constants.VAR_IN));
      hardcodedMappings.add(Constants.DEFAULT_ENTRY_FUNCTION_NAME, mapping1);

    } else if (index == 8) {

      Map<Variable, Variable> mapping1 = new HashMap<>();
      mapping1.put(getVarFromRef("a"), getVarFromIncorrect("a"));
      mapping1.put(getVarFromRef("b"), getVarFromIncorrect("b"));
      mapping1.put(getVarFromRef("c"), getVarFromIncorrect("c"));
      mapping1.put(getVarFromRef(Constants.VAR_OUT), getVarFromIncorrect(Constants.VAR_OUT));
      mapping1.put(getVarFromRef(Constants.VAR_IN), getVarFromIncorrect(Constants.VAR_IN));
      hardcodedMappings.add(Constants.DEFAULT_ENTRY_FUNCTION_NAME, mapping1);

    } else if (index == 9) {

      Map<Variable, Variable> mapping1 = new HashMap<>();
      mapping1.put(getVarFromRef("i"), getVarFromIncorrect("i"));
      mapping1.put(getVarFromRef("number"), getVarFromIncorrect("number"));
      mapping1.put(getVarFromRef(Constants.VAR_OUT), getVarFromIncorrect(Constants.VAR_OUT));
      mapping1.put(getVarFromRef(Constants.VAR_IN), getVarFromIncorrect(Constants.VAR_IN));
      mapping1.put(getVarFromRef(Constants.VAR_RET), getVarFromIncorrect(Constants.VAR_RET));
      hardcodedMappings.add("check_prime", mapping1);

    } else if (index == 10) {

      Map<Variable, Variable> mapping1 = new HashMap<>();
      mapping1.put(getVarFromRef("i"), getVarFromIncorrect("i"));
      mapping1.put(getVarFromRef("n"), getVarFromIncorrect("n"));
      mapping1.put(getVarFromRef(Constants.VAR_OUT), getVarFromIncorrect(Constants.VAR_OUT));
      mapping1.put(getVarFromRef(Constants.VAR_IN), getVarFromIncorrect(Constants.VAR_IN));
      mapping1.put(getVarFromRef("T"), getVarFromIncorrect("T"));
      mapping1.put(getVarFromRef(Constants.VAR_RET), getVarFromIncorrect(Constants.VAR_RET));
      hardcodedMappings.add(Constants.DEFAULT_ENTRY_FUNCTION_NAME, mapping1);

    } else if (index == 11) {

      Map<Variable, Variable> mapping1 = new HashMap<>();
      mapping1.put(getVarFromRef("a"), getVarFromIncorrect("a"));
      mapping1.put(getVarFromRef("b"), getVarFromIncorrect("b"));
      mapping1.put(getVarFromRef("i"), getVarFromIncorrect("i"));
      mapping1.put(getVarFromRef(Constants.VAR_OUT), getVarFromIncorrect(Constants.VAR_OUT));
      mapping1.put(getVarFromRef(Constants.VAR_IN), getVarFromIncorrect(Constants.VAR_IN));
      mapping1.put(getVarFromRef("rem"), getVarFromIncorrect("rem"));
      mapping1.put(getVarFromRef(Constants.VAR_RET), getVarFromIncorrect(Constants.VAR_RET));
      hardcodedMappings.add(Constants.DEFAULT_ENTRY_FUNCTION_NAME, mapping1);

    } else if (index == 12) {

      Map<Variable, Variable> mapping1 = new HashMap<>();
      mapping1.put(getVarFromRef("C"), getVarFromIncorrect("C"));
      mapping1.put(getVarFromRef(Constants.VAR_OUT), getVarFromIncorrect(Constants.VAR_OUT));
      mapping1.put(getVarFromRef(Constants.VAR_IN), getVarFromIncorrect(Constants.VAR_IN));
      mapping1.put(getVarFromRef(Constants.VAR_RET), getVarFromIncorrect(Constants.VAR_RET));
      hardcodedMappings.add(Constants.DEFAULT_ENTRY_FUNCTION_NAME, mapping1);
    }
    return hardcodedMappings;
  }

  public static Variable getVarFromIncorrect(String varRet) {
    return new Variable(varRet);
  }

  public static Variable getVarFromRef(String varRet) {
    return new Variable(varRet);
  }

  /**
   * Checks whether the variables are included in the given mapping.
   *
   * @param mappings - map of variables
   * @param variables - one or many variable names
   * @return boolean
   */
  public static boolean containsVariableMappings(Map<Variable, Variable> mappings,
      String... variables) {
    assert variables.length % 2 == 0;
    for (int i = 0; i < variables.length; i += 2) {
      boolean containsCurrMapping =
          containsVariableMapping(mappings, variables[i], variables[i + 1]);
      if (!containsCurrMapping) {
        return false;
      }
    }
    return true;
  }

  /**
   * Checks whether the two variables are included in the given mapping.
   *
   * @param mappings - map of variables
   * @param varA - variable name
   * @param varB - variable name
   * @return boolean
   */
  public static boolean containsVariableMapping(Map<Variable, Variable> mappings, String varA,
      String varB) {
    for (Map.Entry<Variable, Variable> mapping : mappings.entrySet()) {
      if (mapping.getKey().getUnprimedName().equals(varA)
          && mapping.getValue().getUnprimedName().equals(varB)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Helper method used if reference function and expected function have different location mapping.
   *
   * @param expectedFunction expected function from CLARA
   * @param actualFunction actual function from implemented c-parser
   * @param locationMappings From expectedFunction --> generated function
   */
  private static void locationEquivalenceCheck(Function expectedFunction, Function actualFunction,
      HashMap<Integer, Integer> locationMappings) {

    // checking number of locations
    assertEquals(expectedFunction.getLocations().size(), actualFunction.getLocations().size());

    // check every location expression
    for (Integer expectedLocation : locationMappings.keySet()) {
      int actualLocation = locationMappings.get(expectedLocation);
      // check location description
      assert (isMatch(expectedFunction.getLocationDesc(expectedLocation),
          actualFunction.getLocationDesc(actualLocation)));

      // checking expressions for each location
      List<Pair<String, Expression>> expectedExprList = expectedFunction.getExprs(expectedLocation);
      List<Pair<String, Expression>> actualExprList = actualFunction.getExprs(actualLocation);
      HashMap<String, Expression> expectedExprMap = getExprMap(expectedExprList);
      HashMap<String, Expression> actualExprMap = getExprMap(actualExprList);
      assertEquals(expectedExprMap, actualExprMap);
    }

    // check loc trans, bfs through the 'correct' locations and check it against the 'incorrect'
    // locations
    assertEquals(locationMappings.get(expectedFunction.getInitloc()), actualFunction.getInitloc());
    HashSet<Integer> visitedExpected = new HashSet<>();
    Stack<Integer> toVisitExpected = new Stack<>();

    toVisitExpected.add(expectedFunction.getInitloc());
    while (toVisitExpected.size() > 0) {
      int locationExpected = toVisitExpected.pop();

      // getting mapped location to actual program
      int locationActual = locationMappings.get(locationExpected);
      if (visitedExpected.contains(locationExpected)) {
        continue;
      }
      visitedExpected.add(locationExpected);

      Integer trueLocation = expectedFunction.getTrans(locationExpected, true);
      assertEquals(locationMappings.get(trueLocation),
          actualFunction.getTrans(locationActual, true));
      if (trueLocation != null) {
        toVisitExpected.add(trueLocation);
      }
      Integer falseLocation = expectedFunction.getTrans(locationExpected, false);
      assertEquals(locationMappings.get(falseLocation),
          actualFunction.getTrans(locationActual, false));
      if (falseLocation != null) {
        toVisitExpected.add(falseLocation);
      }
    }
  }

  /**
   * Converts a list of pairs (String, Expression) to a map.
   *
   * @param exprList - list of pairs
   * @return mapping
   */
  private static HashMap<String, Expression> getExprMap(List<Pair<String, Expression>> exprList) {
    HashMap<String, Expression> exprMap = new HashMap<>();
    for (Pair<String, Expression> exprPair : exprList) {
      Expression expr = exprPair.getValue1();
      expr = maxTwoArguments(expr);
      exprMap.put(exprPair.getValue0(), expr);
    }
    return exprMap;
  }

  /**
   * Helper method to post process operations to only have a maximum of two arguments for certain
   * operations. This is just for testing purposes, to ensure other semantically equivalent output
   * from the parser is still valid.
   *
   * @return Operation with only a max of two arguments
   */
  public static Expression maxTwoArguments(Expression expression) {
    if (!(expression instanceof Operation)) {
      return expression;
    }
    Operation operation = (Operation) expression;
    if (!isOperator(operation.getName())) {
      // still recursively child operations
      List<Expression> args = operation.getArgs();
      List<Expression> newArgs = new ArrayList<>();

      for (Expression arg : args) {
        arg = maxTwoArguments(arg);
        newArgs.add(arg);
      }
      return new Operation(operation.getName(), newArgs, operation.getLineNumber());
    }
    int numOfArguments = operation.getArgs().size();
    List<Expression> arguments = new ArrayList<>();
    if (numOfArguments < 3) {
      return operation;
    }

    arguments.add(operation.getArgs().get(0));
    arguments.add(operation.getArgs().get(1)); // append first two arguments
    int lineNumber = operation.getLineNumber();
    String operationName = operation.getName();
    Operation currentOperation = new Operation(operationName, arguments, lineNumber);

    for (int i = 2; i < numOfArguments; i++) {
      Expression argument = operation.getArgs().get(i);
      currentOperation =
          new Operation(operationName, Arrays.asList(currentOperation, argument), lineNumber);
    }
    return currentOperation;
  }

  /**
   * Helper method to test if operation name is a unary or binary operator.
   *
   * @param s operation name
   * @return true if operator is an binary/unary operator
   */
  private static boolean isOperator(String s) {
    if (s == null || s.trim().isEmpty()) {
      return false;
    }
    Pattern p = Pattern.compile("[^A-Za-z0-9]");
    Matcher m = p.matcher(s);
    return m.find();
  }

  /**
   * Helper method to for an abstract equivalence check of program models. It checks whether both
   * programs contain the same functions with regard to their names.
   *
   * @param expectedProgram - Program
   * @param actualProgram - Program
   */
  public static void programEquivalenceCheck(Program expectedProgram, Program actualProgram) {
    assertNotNull(expectedProgram);
    assertNotNull(actualProgram);
    Map<String, Function> expectedFuncList = expectedProgram.getFncs();
    Map<String, Function> actualFuncList = actualProgram.getFncs();
    assertEquals(expectedFuncList.keySet(), actualFuncList.keySet());
    assert actualFuncList.keySet().size() > 0;
    for (String name : expectedFuncList.keySet()) {
      HashMap<Integer, Integer> locationMapping =
          blockMappingGenerator(expectedProgram, actualProgram, name);
      locationEquivalenceCheck(expectedProgram.getFunctionForName(name),
          actualProgram.getFunctionForName(name), locationMapping);
    }
  }

  /**
   * Performs trivial block alignment by comparing the descriptions between the two programs.
   *
   * @param expectedProgram reference program
   * @param actualProgram actual program output by C Parser
   * @param functionName name of function to be fixed
   * @return Mapping of reference program location ~ submitted program location
   */
  private static HashMap<Integer, Integer> blockMappingGenerator(Program expectedProgram,
      Program actualProgram, String functionName) {
    Function expectedFunction = expectedProgram.getFunctionForName(functionName);
    Function actualFunction = actualProgram.getFunctionForName(functionName);
    HashMap<Integer, Integer> mapping = new HashMap<>();
    Set<Integer> expectedFunctionLocations = new HashSet<>(expectedFunction.getLocations());
    Set<Integer> actualFunctionLocations = new HashSet<>(actualFunction.getLocations());
    assertEquals(expectedFunctionLocations.size(), actualFunctionLocations.size());

    for (Integer expectedLocation : expectedFunctionLocations) {
      for (Iterator<Integer> actualLocationIterator =
          actualFunctionLocations.iterator(); actualLocationIterator.hasNext();) {
        Integer actualLocation = actualLocationIterator.next();
        if (isMatch(expectedFunction.getLocationDesc(expectedLocation),
            (actualFunction.getLocationDesc(actualLocation)))) {
          mapping.put(expectedLocation, actualLocation);
          actualFunctionLocations.remove(actualLocation);
          break;
        }
      }
    }
    assertEquals(0, actualFunctionLocations.size());
    return mapping;
  }

  private static boolean isMatch(String s1, String s2) {
    if (new LevenshteinDistance().apply(s1, s2) < 3) {
      return true;
    }
    return false;
  }

  /**
   * Repair the program given a set of consistent repairs.
   *
   * @param repairCandidate set of consistent local repairs
   * @param incorrectProgram incorrect program
   * @param functionName function to be repaired
   * @return repaired program
   */
  public static Program repairProgram(RepairCandidate repairCandidate, Program incorrectProgram,
      String functionName) {
    List<LocalRepair> localRepairs = repairCandidate.getLocalRepairs();

    Function repairedFunction = incorrectProgram.getFunctionForName(functionName);
    for (LocalRepair localRepair : localRepairs) {
      Integer errorLocation = localRepair.getErrorLocation().getValue1();
      Triplet<Variable, Expression, Expression> repair = localRepair.getRepairedVariable();
      Pair<String, Expression> newExpression =
          Pair.with(repair.getValue0().getUnprimedName(), repair.getValue2());
      ArrayList<Pair<String, Expression>> currentList =
          repairedFunction.getLocexprs().get(errorLocation);

      for (int i = 0; i < currentList.size(); i++) {
        if (currentList.get(i).getValue0().equals(newExpression.getValue0())) {
          currentList.remove(i);
          currentList.add(newExpression);
          break;
        }
      }
      repairedFunction.replaceLocExpressions(errorLocation, currentList);
    }
    incorrectProgram.addfnc(repairedFunction);
    return incorrectProgram;
  }

  public static String readFileAsString(String file) throws Exception {
    return new String(Files.readAllBytes(Paths.get(file)));
  }
}
