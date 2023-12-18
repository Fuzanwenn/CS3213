package sg.edu.nus.se.its.errorlocalizer.utils;

import sg.edu.nus.se.its.model.Program;
import sg.edu.nus.se.its.parser.Parser;
import sg.edu.nus.se.its.parser.ParserServiceImpl;
import sg.edu.nus.se.its.util.TestUtils;

import java.io.File;
import java.util.function.Supplier;

/**
 * Model Programs from json and from parsing source code.
 */
public class ModelProgram {
  public static final String unitTestSourceFilePath =
          System.getProperty("user.dir") + "/src/test" + "/resources/";
  public static final String unitTestModelFilePath = unitTestSourceFilePath + "model/";
  private static final String JSON_FILE_EXTENSION = ".json";

  public static final String SOURCE_TEST1_B_C = "test1_b.c";
  public static final String SOURCE_TEST1_C_C = "test1_c.c";
  public static final String SOURCE_LOOP_C_C = "loop-c.c";
  public static final String SOURCE_LOOP_I_C = "loop-i.c";
  public static final String SOURCE_LOOP_I_DIFF_C = "loop-i-diff.c";
  public static final String SOURCE_LOOP_D_C = "loop-d.c";
  public static final String SOURCE_C1_C = "c1.c";
  public static final String SOURCE_I1_C = "i1.c";
  public static final String SOURCE_TEST2_B_C = "test2_b.c";
  public static final String SOURCE_TEST2_C_C = "test2_c.c";
  public static final String SOURCE_TEST3_B_C = "test3_b.c";
  public static final String SOURCE_TEST3_C_C = "test3_c.c";
  public static final String SOURCE_TEST4_B_C = "test4_b.c";
  public static final String SOURCE_TEST4_C_C = "test4_c.c";
  public static final String SOURCE_TEST5_B_C = "test5_b.c";
  public static final String SOURCE_TEST5_C_C = "test5_c.c";
  public static final String SOURCE_TEST6_B_C = "test6_b.c";
  public static final String SOURCE_TEST6_B_DIFF_IMPORT_STMT_C = "test6_b_diff_import_stmt.c";
  public static final String SOURCE_TEST6_B_EXTRA_IMPORT_STMT_C = "test6_b_extra_import_stmt.c";
  public static final String SOURCE_TEST6_C_C = "test6_c.c";
  public static final String SOURCE_TEST7_B_C = "test7_b.c";
  public static final String SOURCE_TEST7_C_C = "test7_c.c";
  public static final String SOURCE_ERROR_IN_FUNCTION_CALL_TEST1_B_C = "errorInFunctionCall1_b.c";
  public static final String SOURCE_ERROR_IN_FUNCTION_CALL_TEST1_C_C = "errorInFunctionCall1_c.c";
  public static final String SOURCE_ERROR_IN_FUNCTION_CALL_TEST2_B_C = "errorInFunctionCall2_b.c";
  public static final String SOURCE_ERROR_IN_FUNCTION_CALL_TEST2_C_C = "errorInFunctionCall2_c.c";
  public static final String SOURCE_SEQUENTIAL_FUNCTION_CALL_TEST_B_C =
      "sequentialFunctionCall_b.c";
  public static final String SOURCE_SEQUENTIAL_FUNCTION_CALL_TEST_C_C =
      "sequentialFunctionCall_c.c";
  public static final String SOURCE_CONDITION_IN_REVERSE_ORDER_TEST_B_C =
      "conditionInReverseOrder_b.c";
  public static final String SOURCE_CONDITION_IN_REVERSE_ORDER_TEST_C_C =
      "conditionInReverseOrder_c.c";
  public static final String SOURCE_WRONG_FUNCTION_CALL_TEST_TEST_B_C = "wrongFunctionCall_b.c";
  public static final String SOURCE_WRONG_FUNCTION_CALL_TEST_TEST_C_C = "wrongFunctionCall_c.c";
  public static final String SOURCE_OPERATORS_IN_C_TEST_B_C = "operatorsInC_b.c";
  public static final String SOURCE_OPERATORS_IN_C_TEST_C_C = "operatorsInC_c.c";
  public static final String SOURCE_BREAK_CONTINUE_TEST_B_C = "breakContinue_b.c";
  public static final String SOURCE_BREAK_CONTINUE_TEST_C_C = "breakContinue_c.c";
  public static final String SOURCE_C_POINTER_TEST_B_C = "cPointerTest_b.c";
  public static final String SOURCE_C_POINTER_TEST_C_C = "cPointerTest_c.c";
  public static final String SOURCE_INFINITE_LOOP_TEST_TEST_B_C = "infiniteLoop_b.c";
  public static final String SOURCE_INFINITE_LOOP_TEST_TEST_C_C = "infiniteLoop_c.c";
  public static final String SOURCE_BASIC_VARIABLE_TYPE_TEST_B_C = "basicVariableType_b.c";
  public static final String SOURCE_BASIC_VARIABLE_TYPE_TEST_C_C = "basicVariableType_c.c";
  public static final String SOURCE_RETURN_TYPE_TEST_B_C = "returnType_b.c";
  public static final String SOURCE_RETURN_TYPE_TEST_C_C = "returnType_c.c";
  public static final String SOURCE_INFINITE_LOOP_TEST2_B_C = "infiniteLoop2_b.c";
  public static final String SOURCE_INFINITE_LOOP_TEST2_C_C = "infiniteLoop2_c.c";
  public static final String SOURCE_BASIC_PYTHON_TEST_B = "basicPythonTest_b.py";
  public static final String SOURCE_BASIC_PYTHON_TEST_C = "basicPythonTest_c.py";
  public static final String SOURCE_PYTHON_IN_BUILT_FUNCTION_TEST_B = "pythonInBuiltFunction_b.py";
  public static final String SOURCE_PYTHON_IN_BUILT_FUNCTION_TEST_C = "pythonInBuiltFunction_c.py";
  public static final String SOURCE_PYTHON_FUNCTION_CALL_TEST_B = "pythonFunctionCall_b.py";
  public static final String SOURCE_PYTHON_FUNCTION_CALL_TEST_C = "pythonFunctionCall_c.py";
  public static final String SOURCE_PYTHON_LIST_TEST_B = "pythonList_b.py";
  public static final String SOURCE_PYTHON_LIST_TEST_C = "pythonList_c.py";
  public static final String SOURCE_OPERATORS_IN_PYTHON_TEST_B = "operatorsInPython_b.py";
  public static final String SOURCE_OPERATORS_IN_PYTHON_TEST_C = "operatorsInPython_c.py";
  public static final String SOURCE_MISSING_FUNCTION_IN_REFERENCE_B_C =
      "missingFunctionInReference_b.c";
  public static final String SOURCE_MISSING_FUNCTION_IN_REFERENCE_C_C =
      "missingFunctionInReference_c.c";

  public static final String JSON_LOOP_C_C = SOURCE_LOOP_C_C + JSON_FILE_EXTENSION;
  public static final String JSON_LOOP_I_C = SOURCE_LOOP_I_C + JSON_FILE_EXTENSION;
  public static final String JSON_LOOP_I_DIFF_C = SOURCE_LOOP_I_DIFF_C + JSON_FILE_EXTENSION;
  public static final String JSON_LOOP_D_C = SOURCE_LOOP_D_C + JSON_FILE_EXTENSION;
  public static final String JSON_C1_C = SOURCE_C1_C + JSON_FILE_EXTENSION;
  public static final String JSON_I1_C = SOURCE_I1_C + JSON_FILE_EXTENSION;
  public static final String JSON_TEST1_B_C = SOURCE_TEST1_B_C + JSON_FILE_EXTENSION;
  public static final String JSON_TEST1_C_C = SOURCE_TEST1_C_C + JSON_FILE_EXTENSION;
  public static final String JSON_TEST2_B_C = SOURCE_TEST2_B_C + JSON_FILE_EXTENSION;
  public static final String JSON_TEST2_C_C = SOURCE_TEST2_C_C + JSON_FILE_EXTENSION;
  public static final String JSON_TEST3_B_C = SOURCE_TEST3_B_C + JSON_FILE_EXTENSION;
  public static final String JSON_TEST3_C_C = SOURCE_TEST3_C_C + JSON_FILE_EXTENSION;
  public static final String JSON_TEST4_B_C = SOURCE_TEST4_B_C + JSON_FILE_EXTENSION;
  public static final String JSON_TEST4_C_C = SOURCE_TEST4_C_C + JSON_FILE_EXTENSION;
  public static final String JSON_TEST5_B_C = SOURCE_TEST5_B_C + JSON_FILE_EXTENSION;
  public static final String JSON_TEST5_C_C = SOURCE_TEST5_C_C + JSON_FILE_EXTENSION;
  public static final String JSON_TEST6_B_C = SOURCE_TEST6_B_C + JSON_FILE_EXTENSION;
  public static final String JSON_TEST6_B_DIFF_IMPORT_STMT_C = SOURCE_TEST6_B_DIFF_IMPORT_STMT_C
      + JSON_FILE_EXTENSION;
  public static final String JSON_TEST6_B_EXTRA_IMPORT_STMT_C = SOURCE_TEST6_B_EXTRA_IMPORT_STMT_C
      + JSON_FILE_EXTENSION;
  public static final String JSON_TEST6_C_C = SOURCE_TEST6_C_C+ JSON_FILE_EXTENSION;
  public static final String JSON_TEST7_B_C = SOURCE_TEST7_B_C + JSON_FILE_EXTENSION;
  public static final String JSON_TEST7_C_C = SOURCE_TEST7_C_C + JSON_FILE_EXTENSION;
  public static final String JSON_ERROR_IN_FUNCTION_CALL_TEST1_B_C =
      SOURCE_ERROR_IN_FUNCTION_CALL_TEST1_B_C + JSON_FILE_EXTENSION;
  public static final String JSON_ERROR_IN_FUNCTION_CALL_TEST1_C_C =
      SOURCE_ERROR_IN_FUNCTION_CALL_TEST1_C_C + JSON_FILE_EXTENSION;
  public static final String JSON_ERROR_IN_FUNCTION_CALL_TEST2_B_C =
      SOURCE_ERROR_IN_FUNCTION_CALL_TEST2_B_C + JSON_FILE_EXTENSION;
  public static final String JSON_ERROR_IN_FUNCTION_CALL_TEST2_C_C =
      SOURCE_ERROR_IN_FUNCTION_CALL_TEST2_C_C + JSON_FILE_EXTENSION;
  public static final String JSON_SEQUENTIAL_FUNCTION_CALL_TEST_B_C =
      SOURCE_SEQUENTIAL_FUNCTION_CALL_TEST_B_C + JSON_FILE_EXTENSION;
  public static final String JSON_SEQUENTIAL_FUNCTION_CALL_TEST_C_C =
      SOURCE_SEQUENTIAL_FUNCTION_CALL_TEST_C_C + JSON_FILE_EXTENSION;
  public static final String JSON_CONDITION_IN_REVERSE_ORDER_TEST_B_C =
      SOURCE_CONDITION_IN_REVERSE_ORDER_TEST_B_C + JSON_FILE_EXTENSION;
  public static final String JSON_CONDITION_IN_REVERSE_ORDER_TEST_C_C =
      SOURCE_CONDITION_IN_REVERSE_ORDER_TEST_C_C + JSON_FILE_EXTENSION;
  public static final String JSON_WRONG_FUNCTION_CALL_TEST_TEST_B_C =
      SOURCE_WRONG_FUNCTION_CALL_TEST_TEST_B_C + JSON_FILE_EXTENSION;
  public static final String JSON_WRONG_FUNCTION_CALL_TEST_TEST_C_C =
      SOURCE_WRONG_FUNCTION_CALL_TEST_TEST_C_C + JSON_FILE_EXTENSION;
  public static final String JSON_OPERATORS_IN_C_TEST_B_C = SOURCE_OPERATORS_IN_C_TEST_B_C
      + JSON_FILE_EXTENSION;
  public static final String JSON_OPERATORS_IN_C_TEST_C_C = SOURCE_OPERATORS_IN_C_TEST_C_C
      + JSON_FILE_EXTENSION;
  public static final String JSON_BREAK_CONTINUE_TEST_B_C = SOURCE_BREAK_CONTINUE_TEST_B_C
      + JSON_FILE_EXTENSION;
  public static final String JSON_BREAK_CONTINUE_TEST_C_C = SOURCE_BREAK_CONTINUE_TEST_C_C
      + JSON_FILE_EXTENSION;
  public static final String JSON_C_POINTER_TEST_B_C = SOURCE_C_POINTER_TEST_B_C
      + JSON_FILE_EXTENSION;
  public static final String JSON_C_POINTER_TEST_C_C = SOURCE_C_POINTER_TEST_C_C
      + JSON_FILE_EXTENSION;
  public static final String JSON_INFINITE_LOOP_TEST_TEST_B_C = SOURCE_INFINITE_LOOP_TEST_TEST_B_C
      + JSON_FILE_EXTENSION;
  public static final String JSON_INFINITE_LOOP_TEST_TEST_C_C = SOURCE_INFINITE_LOOP_TEST_TEST_C_C
      + JSON_FILE_EXTENSION;
  public static final String JSON_BASIC_VARIABLE_TYPE_TEST_B_C = SOURCE_BASIC_VARIABLE_TYPE_TEST_B_C
      + JSON_FILE_EXTENSION;
  public static final String JSON_BASIC_VARIABLE_TYPE_TEST_C_C = SOURCE_BASIC_VARIABLE_TYPE_TEST_C_C
      + JSON_FILE_EXTENSION;
  public static final String JSON_RETURN_TYPE_TEST_B_C = SOURCE_RETURN_TYPE_TEST_B_C
      + JSON_FILE_EXTENSION;
  public static final String JSON_RETURN_TYPE_TEST_C_C = SOURCE_RETURN_TYPE_TEST_C_C
      + JSON_FILE_EXTENSION;
  public static final String JSON_INFINITE_LOOP_TEST2_B_C = SOURCE_INFINITE_LOOP_TEST2_B_C
      + JSON_FILE_EXTENSION;
  public static final String JSON_INFINITE_LOOP_TEST2_C_C = SOURCE_INFINITE_LOOP_TEST2_C_C
      + JSON_FILE_EXTENSION;
  public static final String JSON_BASIC_PYTHON_TEST_B = SOURCE_BASIC_PYTHON_TEST_B
      + JSON_FILE_EXTENSION;
  public static final String JSON_BASIC_PYTHON_TEST_C = SOURCE_BASIC_PYTHON_TEST_C
      + JSON_FILE_EXTENSION;
  public static final String JSON_PYTHON_IN_BUILT_FUNCTION_TEST_B =
      SOURCE_PYTHON_IN_BUILT_FUNCTION_TEST_B + JSON_FILE_EXTENSION;
  public static final String JSON_PYTHON_IN_BUILT_FUNCTION_TEST_C =
      SOURCE_PYTHON_IN_BUILT_FUNCTION_TEST_C + JSON_FILE_EXTENSION;
  public static final String JSON_PYTHON_FUNCTION_CALL_TEST_B = SOURCE_PYTHON_FUNCTION_CALL_TEST_B
      + JSON_FILE_EXTENSION;
  public static final String JSON_PYTHON_FUNCTION_CALL_TEST_C = SOURCE_PYTHON_FUNCTION_CALL_TEST_C
      + JSON_FILE_EXTENSION;
  public static final String JSON_PYTHON_LIST_TEST_B = SOURCE_PYTHON_LIST_TEST_B
      + JSON_FILE_EXTENSION;
  public static final String JSON_PYTHON_LIST_TEST_C = SOURCE_PYTHON_LIST_TEST_C
      + JSON_FILE_EXTENSION;
  public static final String JSON_OPERATORS_IN_PYTHON_TEST_B = SOURCE_OPERATORS_IN_PYTHON_TEST_B
      + JSON_FILE_EXTENSION;
  public static final String JSON_OPERATORS_IN_PYTHON_TEST_C = SOURCE_OPERATORS_IN_PYTHON_TEST_C
      + JSON_FILE_EXTENSION;
  public static final String JSON_MISSING_FUNCTION_IN_REFERENCE_B_C =
      SOURCE_MISSING_FUNCTION_IN_REFERENCE_B_C + JSON_FILE_EXTENSION;
  public static final String JSON_MISSING_FUNCTION_IN_REFERENCE_C_C =
      SOURCE_MISSING_FUNCTION_IN_REFERENCE_C_C + JSON_FILE_EXTENSION;

  public static final Supplier<Program> MODEL_LOOP_C_C = () -> getProgramFromModel(JSON_LOOP_C_C);
  public static final Supplier<Program> MODEL_LOOP_I_C = () -> getProgramFromModel(JSON_LOOP_I_C);
  public static final Supplier<Program> MODEL_LOOP_I_DIFF_C =
      () -> getProgramFromModel(JSON_LOOP_I_DIFF_C);
  public static final Supplier<Program> MODEL_LOOP_D_C = () -> getProgramFromModel(JSON_LOOP_D_C);
  public static final Supplier<Program> MODEL_C1_C = () -> getProgramFromModel(JSON_C1_C);
  public static final Supplier<Program> MODEL_I1_C = () -> getProgramFromModel(JSON_I1_C);
  public static final Supplier<Program> MODEL_TEST1_B_C = () -> getProgramFromModel(JSON_TEST1_B_C);
  public static final Supplier<Program> MODEL_TEST1_C_C = () -> getProgramFromModel(JSON_TEST1_C_C);
  public static final Supplier<Program> MODEL_TEST2_B_C = () -> getProgramFromModel(JSON_TEST2_B_C);
  public static final Supplier<Program> MODEL_TEST2_C_C = () -> getProgramFromModel(JSON_TEST2_C_C);
  public static final Supplier<Program> MODEL_TEST3_B_C = () -> getProgramFromModel(JSON_TEST3_B_C);
  public static final Supplier<Program> MODEL_TEST3_C_C = () -> getProgramFromModel(JSON_TEST3_C_C);
  public static final Supplier<Program> MODEL_TEST4_B_C = () -> getProgramFromModel(JSON_TEST4_B_C);
  public static final Supplier<Program> MODEL_TEST4_C_C = () -> getProgramFromModel(JSON_TEST4_C_C);
  public static final Supplier<Program> MODEL_TEST5_B_C = () -> getProgramFromModel(JSON_TEST5_B_C);
  public static final Supplier<Program> MODEL_TEST5_C_C = () -> getProgramFromModel(JSON_TEST5_C_C);
  public static final Supplier<Program> MODEL_TEST6_B_C = () -> getProgramFromModel(JSON_TEST6_B_C);
  public static final Supplier<Program> MODEL_TEST6_C_C = () -> getProgramFromModel(JSON_TEST6_C_C);
  public static final Supplier<Program>  MODEL_TEST6_B_DIFF_IMPORT_STMT_C =
          () -> getProgramFromModel(JSON_TEST6_B_DIFF_IMPORT_STMT_C);
  public static final Supplier<Program>  MODEL_TEST6_B_EXTRA_IMPORT_STMT_C =
          () -> getProgramFromModel(JSON_TEST6_B_EXTRA_IMPORT_STMT_C);
  public static final Supplier<Program> MODEL_TEST7_B_C = () -> getProgramFromModel(JSON_TEST7_B_C);
  public static final Supplier<Program> MODEL_TEST7_C_C = () -> getProgramFromModel(JSON_TEST7_C_C);
  public static final Supplier<Program> MODEL_ERROR_IN_FUNCTION_CALL_TEST1_B_C =
      () -> getProgramFromModel(JSON_ERROR_IN_FUNCTION_CALL_TEST1_B_C);
  public static final Supplier<Program> MODEL_ERROR_IN_FUNCTION_CALL_TEST1_C_C =
      () -> getProgramFromModel(JSON_ERROR_IN_FUNCTION_CALL_TEST1_C_C);
  public static final Supplier<Program> MODEL_ERROR_IN_FUNCTION_CALL_TEST2_B_C =
      () -> getProgramFromModel(JSON_ERROR_IN_FUNCTION_CALL_TEST2_B_C);
  public static final Supplier<Program> MODEL_ERROR_IN_FUNCTION_CALL_TEST2_C_C =
      () -> getProgramFromModel(JSON_ERROR_IN_FUNCTION_CALL_TEST2_C_C);
  public static final Supplier<Program> MODEL_SEQUENTIAL_FUNCTION_CALL_TEST_B_C =
      () -> getProgramFromModel(JSON_SEQUENTIAL_FUNCTION_CALL_TEST_B_C);
  public static final Supplier<Program> MODEL_SEQUENTIAL_FUNCTION_CALL_TEST_C_C =
      () -> getProgramFromModel(JSON_SEQUENTIAL_FUNCTION_CALL_TEST_C_C);
  public static final Supplier<Program> MODEL_CONDITION_IN_REVERSE_ORDER_TEST_B_C =
      () -> getProgramFromModel(JSON_CONDITION_IN_REVERSE_ORDER_TEST_B_C);
  public static final Supplier<Program> MODEL_CONDITION_IN_REVERSE_ORDER_TEST_C_C =
      () -> getProgramFromModel(JSON_CONDITION_IN_REVERSE_ORDER_TEST_C_C);
  public static final Supplier<Program> MODEL_WRONG_FUNCTION_CALL_TEST_TEST_B_C =
      () -> getProgramFromModel(JSON_WRONG_FUNCTION_CALL_TEST_TEST_B_C);
  public static final Supplier<Program> MODEL_WRONG_FUNCTION_CALL_TEST_TEST_C_C =
      () -> getProgramFromModel(JSON_WRONG_FUNCTION_CALL_TEST_TEST_C_C);
  public static final Supplier<Program> MODEL_OPERATORS_IN_C_TEST_B_C =
      () -> getProgramFromModel(JSON_OPERATORS_IN_C_TEST_B_C);
  public static final Supplier<Program> MODEL_OPERATORS_IN_C_TEST_C_C =
      () -> getProgramFromModel(JSON_OPERATORS_IN_C_TEST_C_C);
  public static final Supplier<Program> MODEL_BREAK_CONTINUE_TEST_B_C =
      () -> getProgramFromModel(JSON_BREAK_CONTINUE_TEST_B_C);
  public static final Supplier<Program> MODEL_BREAK_CONTINUE_TEST_C_C =
      () -> getProgramFromModel(JSON_BREAK_CONTINUE_TEST_C_C);
  public static final Supplier<Program> MODEL_C_POINTER_TEST_B_C =
      () -> getProgramFromModel(JSON_C_POINTER_TEST_B_C);
  public static final Supplier<Program> MODEL_C_POINTER_TEST_C_C =
      () -> getProgramFromModel(JSON_C_POINTER_TEST_C_C);
  public static final Supplier<Program> MODEL_INFINITE_LOOP_TEST_TEST_B_C =
      () -> getProgramFromModel(JSON_INFINITE_LOOP_TEST_TEST_B_C);
  public static final Supplier<Program> MODEL_INFINITE_LOOP_TEST_TEST_C_C =
      () -> getProgramFromModel(JSON_INFINITE_LOOP_TEST_TEST_C_C);
  public static final Supplier<Program> MODEL_BASIC_VARIABLE_TYPE_TEST_B_C =
      () -> getProgramFromModel(JSON_BASIC_VARIABLE_TYPE_TEST_B_C);
  public static final Supplier<Program> MODEL_BASIC_VARIABLE_TYPE_TEST_C_C =
      () -> getProgramFromModel(JSON_BASIC_VARIABLE_TYPE_TEST_C_C);
  public static final Supplier<Program> MODEL_RETURN_TYPE_TEST_B_C =
      () -> getProgramFromModel(JSON_RETURN_TYPE_TEST_B_C);
  public static final Supplier<Program> MODEL_RETURN_TYPE_TEST_C_C =
      () -> getProgramFromModel(JSON_RETURN_TYPE_TEST_C_C);
  public static final Supplier<Program> MODEL_INFINITE_LOOP_TEST2_B_C =
      () -> getProgramFromModel(JSON_INFINITE_LOOP_TEST2_B_C);
  public static final Supplier<Program> MODEL_INFINITE_LOOP_TEST2_C_C =
      () -> getProgramFromModel(JSON_INFINITE_LOOP_TEST2_C_C);
  public static final Supplier<Program> MODEL_BASIC_PYTHON_TEST_B =
      () -> getProgramFromModel(JSON_BASIC_PYTHON_TEST_B);
  public static final Supplier<Program> MODEL_BASIC_PYTHON_TEST_C =
      () -> getProgramFromModel(JSON_BASIC_PYTHON_TEST_C);
  public static final Supplier<Program> MODEL_PYTHON_IN_BUILT_FUNCTION_TEST_B =
      () -> getProgramFromModel(JSON_PYTHON_IN_BUILT_FUNCTION_TEST_B);
  public static final Supplier<Program> MODEL_PYTHON_IN_BUILT_FUNCTION_TEST_C =
      () -> getProgramFromModel(JSON_PYTHON_IN_BUILT_FUNCTION_TEST_C);
  public static final Supplier<Program> MODEL_PYTHON_FUNCTION_CALL_TEST_B =
      () -> getProgramFromModel(JSON_PYTHON_FUNCTION_CALL_TEST_B);
  public static final Supplier<Program> MODEL_PYTHON_FUNCTION_CALL_TEST_C =
      () -> getProgramFromModel(JSON_PYTHON_FUNCTION_CALL_TEST_C);
  public static final Supplier<Program> MODEL_PYTHON_LIST_TEST_B =
      () -> getProgramFromModel(JSON_PYTHON_LIST_TEST_B);
  public static final Supplier<Program> MODEL_PYTHON_LIST_TEST_C =
      () -> getProgramFromModel(JSON_PYTHON_LIST_TEST_C);
  public static final Supplier<Program> MODEL_OPERATORS_IN_PYTHON_TEST_B =
      () -> getProgramFromModel(JSON_OPERATORS_IN_PYTHON_TEST_B);
  public static final Supplier<Program> MODEL_OPERATORS_IN_PYTHON_TEST_C =
      () -> getProgramFromModel(JSON_OPERATORS_IN_PYTHON_TEST_C);
  public static final Supplier<Program> MODEL_MISSING_FUNCTION_IN_REFERENCE_B_C =
      () -> getProgramFromModel(JSON_MISSING_FUNCTION_IN_REFERENCE_B_C);
  public static final Supplier<Program> MODEL_MISSING_FUNCTION_IN_REFERENCE_C_C =
      () -> getProgramFromModel(JSON_MISSING_FUNCTION_IN_REFERENCE_C_C);

  public static final Supplier<Program> PARSED_LOOP_C_C =
      () -> getProgramFromSourceCode(SOURCE_LOOP_C_C);
  public static final Supplier<Program> PARSED_LOOP_D_C =
      () -> getProgramFromSourceCode(SOURCE_LOOP_D_C);
  public static final Supplier<Program> PARSED_LOOP_I_C =
      () -> getProgramFromSourceCode(SOURCE_LOOP_I_C);
  public static final Supplier<Program> PARSED_C1_C = () -> getProgramFromSourceCode(SOURCE_C1_C);
  public static final Supplier<Program> PARSED_I1_C = () -> getProgramFromSourceCode(SOURCE_I1_C);
  public static final Supplier<Program> PARSED_TEST1_B_C =
      () -> getProgramFromSourceCode(SOURCE_TEST1_B_C);
  public static final Supplier<Program> PARSED_TEST1_C_C =
      () -> getProgramFromSourceCode(SOURCE_TEST1_C_C);
  public static final Supplier<Program> PARSED_TEST2_B_C =
      () -> getProgramFromSourceCode(SOURCE_TEST2_B_C);
  public static final Supplier<Program> PARSED_TEST2_C_C =
      () -> getProgramFromSourceCode(SOURCE_TEST2_C_C);
  public static final Supplier<Program> PARSED_TEST3_B_C =
      () -> getProgramFromSourceCode(SOURCE_TEST3_B_C);
  public static final Supplier<Program> PARSED_TEST3_C_C =
      () -> getProgramFromSourceCode(SOURCE_TEST3_C_C);
  public static final Supplier<Program> PARSED_TEST4_B_C =
      () -> getProgramFromSourceCode(SOURCE_TEST4_B_C);
  public static final Supplier<Program> PARSED_TEST4_C_C =
      () -> getProgramFromSourceCode(SOURCE_TEST4_C_C);
  public static final Supplier<Program> PARSED_TEST5_B_C =
      () -> getProgramFromSourceCode(SOURCE_TEST5_B_C);
  public static final Supplier<Program> PARSED_TEST5_C_C =
      () -> getProgramFromSourceCode(SOURCE_TEST5_C_C);
  public static final Supplier<Program> PARSED_TEST6_B_C =
      () -> getProgramFromSourceCode(SOURCE_TEST6_B_C);
  public static final Supplier<Program> PARSED_TEST6_C_C =
      () -> getProgramFromSourceCode(SOURCE_TEST6_C_C);
  public static final Supplier<Program>  PARSED_TEST6_B_DIFF_IMPORT_STMT_C =
          () -> getProgramFromSourceCode(SOURCE_TEST6_B_DIFF_IMPORT_STMT_C);
  public static final Supplier<Program>  PARSED_TEST6_B_EXTRA_IMPORT_STMT_C =
          () -> getProgramFromSourceCode(SOURCE_TEST6_B_EXTRA_IMPORT_STMT_C);
  public static final Supplier<Program> PARSED_TEST7_B_C =
      () -> getProgramFromSourceCode(SOURCE_TEST7_B_C);
  public static final Supplier<Program> PARSED_TEST7_C_C =
      () -> getProgramFromSourceCode(SOURCE_TEST7_C_C);
  public static final Supplier<Program> PARSED_ERROR_IN_FUNCTION_CALL_TEST1_B_C =
      () -> getProgramFromSourceCode(SOURCE_ERROR_IN_FUNCTION_CALL_TEST1_B_C);
  public static final Supplier<Program> PARSED_ERROR_IN_FUNCTION_CALL_TEST1_C_C =
      () -> getProgramFromSourceCode(SOURCE_ERROR_IN_FUNCTION_CALL_TEST1_C_C);
  public static final Supplier<Program> PARSED_ERROR_IN_FUNCTION_CALL_TEST2_B_C =
      () -> getProgramFromSourceCode(SOURCE_ERROR_IN_FUNCTION_CALL_TEST2_B_C);
  public static final Supplier<Program> PARSED_ERROR_IN_FUNCTION_CALL_TEST2_C_C =
      () -> getProgramFromSourceCode(SOURCE_ERROR_IN_FUNCTION_CALL_TEST2_C_C);
  public static final Supplier<Program> PARSED_SEQUENTIAL_FUNCTION_CALL_TEST_B_C =
      () -> getProgramFromSourceCode(SOURCE_SEQUENTIAL_FUNCTION_CALL_TEST_B_C);
  public static final Supplier<Program> PARSED_SEQUENTIAL_FUNCTION_CALL_TEST_C_C =
      () -> getProgramFromSourceCode(SOURCE_SEQUENTIAL_FUNCTION_CALL_TEST_C_C);
  public static final Supplier<Program> PARSED_CONDITION_IN_REVERSE_ORDER_TEST_B_C =
      () -> getProgramFromSourceCode(SOURCE_CONDITION_IN_REVERSE_ORDER_TEST_B_C);
  public static final Supplier<Program> PARSED_CONDITION_IN_REVERSE_ORDER_TEST_C_C =
      () -> getProgramFromSourceCode(SOURCE_CONDITION_IN_REVERSE_ORDER_TEST_C_C);
  public static final Supplier<Program> PARSED_WRONG_FUNCTION_CALL_TEST_TEST_B_C =
      () -> getProgramFromSourceCode(SOURCE_WRONG_FUNCTION_CALL_TEST_TEST_B_C);
  public static final Supplier<Program> PARSED_WRONG_FUNCTION_CALL_TEST_TEST_C_C =
      () -> getProgramFromSourceCode(SOURCE_WRONG_FUNCTION_CALL_TEST_TEST_C_C);
  public static final Supplier<Program> PARSED_OPERATORS_IN_C_TEST_B_C =
      () -> getProgramFromSourceCode(SOURCE_OPERATORS_IN_C_TEST_B_C);
  public static final Supplier<Program> PARSED_OPERATORS_IN_C_TEST_C_C =
      () -> getProgramFromSourceCode(SOURCE_OPERATORS_IN_C_TEST_C_C);
  public static final Supplier<Program> PARSED_BREAK_CONTINUE_TEST_B_C =
      () -> getProgramFromSourceCode(SOURCE_BREAK_CONTINUE_TEST_B_C);
  public static final Supplier<Program> PARSED_BREAK_CONTINUE_TEST_C_C =
      () -> getProgramFromSourceCode(SOURCE_BREAK_CONTINUE_TEST_C_C);
  public static final Supplier<Program> PARSED_C_POINTER_TEST_B_C =
      () -> getProgramFromSourceCode(SOURCE_C_POINTER_TEST_B_C);
  public static final Supplier<Program> PARSED_C_POINTER_TEST_C_C =
      () -> getProgramFromSourceCode(SOURCE_C_POINTER_TEST_C_C);
  public static final Supplier<Program> PARSED_INFINITE_LOOP_TEST_TEST_B_C =
      () -> getProgramFromSourceCode(SOURCE_INFINITE_LOOP_TEST_TEST_B_C);
  public static final Supplier<Program> PARSED_INFINITE_LOOP_TEST_TEST_C_C =
      () -> getProgramFromSourceCode(SOURCE_INFINITE_LOOP_TEST_TEST_C_C);
  public static final Supplier<Program> PARSED_BASIC_VARIABLE_TYPE_TEST_B_C =
      () -> getProgramFromSourceCode(SOURCE_BASIC_VARIABLE_TYPE_TEST_B_C);
  public static final Supplier<Program> PARSED_BASIC_VARIABLE_TYPE_TEST_C_C =
      () -> getProgramFromSourceCode(SOURCE_BASIC_VARIABLE_TYPE_TEST_C_C);
  public static final Supplier<Program> PARSED_RETURN_TYPE_TEST_B_C =
      () -> getProgramFromSourceCode(SOURCE_RETURN_TYPE_TEST_B_C);
  public static final Supplier<Program> PARSED_RETURN_TYPE_TEST_C_C =
      () -> getProgramFromSourceCode(SOURCE_RETURN_TYPE_TEST_C_C);
  public static final Supplier<Program> PARSED_INFINITE_LOOP_TEST2_B_C =
      () -> getProgramFromSourceCode(SOURCE_INFINITE_LOOP_TEST2_B_C);
  public static final Supplier<Program> PARSED_INFINITE_LOOP_TEST2_C_C =
      () -> getProgramFromSourceCode(SOURCE_INFINITE_LOOP_TEST2_C_C);
  public static final Supplier<Program> PARSED_BASIC_PYTHON_TEST_B =
      () -> getProgramFromSourceCode(SOURCE_BASIC_PYTHON_TEST_B);
  public static final Supplier<Program> PARSED_BASIC_PYTHON_TEST_C =
      () -> getProgramFromSourceCode(SOURCE_BASIC_PYTHON_TEST_C);
  public static final Supplier<Program> PARSED_PYTHON_IN_BUILT_FUNCTION_TEST_B =
      () -> getProgramFromSourceCode(SOURCE_PYTHON_IN_BUILT_FUNCTION_TEST_B);
  public static final Supplier<Program> PARSED_PYTHON_IN_BUILT_FUNCTION_TEST_C =
      () -> getProgramFromSourceCode(SOURCE_PYTHON_IN_BUILT_FUNCTION_TEST_C);
  public static final Supplier<Program> PARSED_PYTHON_FUNCTION_CALL_TEST_B =
      () -> getProgramFromSourceCode(SOURCE_PYTHON_FUNCTION_CALL_TEST_B);
  public static final Supplier<Program> PARSED_PYTHON_FUNCTION_CALL_TEST_C =
      () -> getProgramFromSourceCode(SOURCE_PYTHON_FUNCTION_CALL_TEST_C);
  public static final Supplier<Program> PARSED_PYTHON_LIST_TEST_B =
      () -> getProgramFromSourceCode(SOURCE_PYTHON_LIST_TEST_B);
  public static final Supplier<Program> PARSED_PYTHON_LIST_TEST_C =
      () -> getProgramFromSourceCode(SOURCE_PYTHON_LIST_TEST_C);
  public static final Supplier<Program> PARSED_OPERATORS_IN_PYTHON_TEST_B =
      () -> getProgramFromSourceCode(SOURCE_OPERATORS_IN_PYTHON_TEST_B);
  public static final Supplier<Program> PARSED_OPERATORS_IN_PYTHON_TEST_C =
      () -> getProgramFromSourceCode(SOURCE_OPERATORS_IN_PYTHON_TEST_C);
  public static final Supplier<Program> PARSED_MISSING_FUNCTION_IN_REFERENCE_B_C =
      () -> getProgramFromSourceCode(SOURCE_MISSING_FUNCTION_IN_REFERENCE_B_C);
  public static final Supplier<Program> PARSED_MISSING_FUNCTION_IN_REFERENCE_C_C =
      () -> getProgramFromSourceCode(SOURCE_MISSING_FUNCTION_IN_REFERENCE_C_C);

  private static Program getProgramFromModel(String modelJsonFileName) {
    return TestUtils.loadProgramByFilePath(unitTestModelFilePath + modelJsonFileName);
  }

  public static Program getProgramFromSourceCode(String sourceCodeFileName) {
    Parser parser = new ParserServiceImpl();
    try {
      File programFile = new File(unitTestSourceFilePath
              + "source/" + sourceCodeFileName).getAbsoluteFile();
      return parser.parse(programFile);
    } catch (Exception exception) {
      return null;
    }
  }
}