package sg.edu.nus.se.its.errorlocalizer.utils;

import sg.edu.nus.se.its.model.Input;
import sg.edu.nus.se.its.util.TestUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Model class contains the inputs for the test cases.
 */
public class ModelInput {
  public static final String unitTestSourceFilePath =
      System.getProperty("user.dir") + "/src/test" + "/resources/";
  public static final String unitTestInputFilePath = unitTestSourceFilePath + "input/";

  public static final List<Input> MODEL_INPUT_TEST_1 = loadInputsByProgramName("test1.c");
  public static final List<Input> MODEL_INPUT_TEST_2 = loadInputsByProgramName("test2.c");
  public static final List<Input> MODEL_INPUT_TEST_3 = loadInputsByProgramName("test3.c");
  public static final List<Input> MODEL_INPUT_TEST_4 = loadInputsByProgramName("test4.c");
  public static final List<Input> MODEL_INPUT_TEST_5 = loadInputsByProgramName("test5.c");
  public static final List<Input> MODEL_INPUT_TEST_6 = loadInputsByProgramName("test6.c");
  public static final List<Input> MODEL_INPUT_TEST_7 = loadInputsByProgramName("test7.c");

  public static final List<Input> MODEL_INPUT_ERROR_IN_FUNCTION_CALL_TEST1 =
      loadInputsByProgramName("errorInFunctionCall1.c");
  public static final List<Input> MODEL_INPUT_ERROR_IN_FUNCTION_CALL_TEST2 =
      loadInputsByProgramName("errorInFunctionCall2.c");
  public static final List<Input> MODEL_INPUT_CONDITION_IN_REVERSE_ORDER_TEST =
      loadInputsByProgramName("conditionInReverseOrder.c");
  public static final List<Input> MODEL_INPUT_BASIC_PYTHON_TEST =
      loadInputsByProgramName("basicPythonTest.py");
  public static final List<Input> MODEL_INPUT_PYTHON_IN_BUILT_FUNCTION_TEST =
      loadInputsByProgramName("pythonInBuiltFunction.py");

  /**
   * Loads the inputs for a specific test case.
   *
   * @param name -- source file name
   * @return List of inputs as String objects
   *
   * @author its-core/TestUtils.java
   */
  public static List<Input> loadInputsByProgramName(String name) {
    File inputFile = new File(unitTestInputFilePath + "/" + name + ".in");
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
      e.printStackTrace();
      return null;
    }
  }
}
