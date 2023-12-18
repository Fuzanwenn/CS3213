package sg.edu.nus.se.its.errorlocalizer;

import org.junit.jupiter.api.Test;
import sg.edu.nus.se.its.alignment.StructuralMapping;
import sg.edu.nus.se.its.alignment.VariableMapping;
import sg.edu.nus.se.its.errorlocalizer.utils.ModelProgram;
import sg.edu.nus.se.its.interpreter.Interpreter;
import sg.edu.nus.se.its.interpreter.InterpreterServiceImpl;
import sg.edu.nus.se.its.model.Input;
import sg.edu.nus.se.its.model.Program;
import sg.edu.nus.se.its.model.Variable;
import sg.edu.nus.se.its.util.constants.Constants;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static sg.edu.nus.se.its.util.constants.Constants.VAR_COND;
import static sg.edu.nus.se.its.util.constants.Constants.VAR_RET;

/**
 * Test if the error localizer can detect that reverse operands are equivalent.
 */
public class InfiniteLoopTest {
  private static final String FUNC_IS_ODD = "isOdd";
  private static final String FUNC_IS_EVEN = "isEven";

  private StructuralMapping getStructuralMapping() {
    Map<Integer, Integer> commonMapping = new HashMap<>();
    commonMapping.put(1, 1);
    commonMapping.put(2, 2);
    commonMapping.put(3, 3);
    commonMapping.put(4, 4);

    StructuralMapping structuralAlignmentResult = new StructuralMapping();
    structuralAlignmentResult.put(Constants.DEFAULT_ENTRY_FUNCTION_NAME, commonMapping);
    structuralAlignmentResult.put(FUNC_IS_EVEN, commonMapping);
    structuralAlignmentResult.put(FUNC_IS_ODD, commonMapping);
    return structuralAlignmentResult;
  }

  private VariableMapping getVariableMapping() {
    Map<Variable, Variable> mainVarMapping = new HashMap<>();
    mainVarMapping.put(new Variable(VAR_COND), new Variable(VAR_COND));
    mainVarMapping.put(new Variable(Constants.VAR_RET), new Variable(Constants.VAR_RET));
    mainVarMapping.put(new Variable(Constants.VAR_OUT), new Variable(Constants.VAR_OUT));
    mainVarMapping.put(new Variable(Constants.VAR_IN), new Variable(Constants.VAR_IN));
    mainVarMapping.put(new Variable("i"), new Variable("z"));
    mainVarMapping.put(new Variable("r"), new Variable("a"));
    mainVarMapping.put(new Variable(FUNC_IS_EVEN), new Variable(FUNC_IS_EVEN));
    mainVarMapping.put(new Variable(FUNC_IS_ODD), new Variable(FUNC_IS_ODD));
    mainVarMapping.put(new Variable(Constants.DEFAULT_ENTRY_FUNCTION_NAME),
        new Variable(Constants.DEFAULT_ENTRY_FUNCTION_NAME));

    Map<Variable, Variable> isOddVarMapping = new HashMap<>();
    isOddVarMapping.put(new Variable(VAR_COND), new Variable(VAR_COND));
    isOddVarMapping.put(new Variable(Constants.VAR_RET), new Variable(Constants.VAR_RET));
    isOddVarMapping.put(new Variable(Constants.VAR_OUT), new Variable(Constants.VAR_OUT));
    isOddVarMapping.put(new Variable(Constants.VAR_IN), new Variable(Constants.VAR_IN));
    isOddVarMapping.put(new Variable("a"), new Variable("c"));
    isOddVarMapping.put(new Variable("i"), new Variable("y"));
    isOddVarMapping.put(new Variable(FUNC_IS_EVEN), new Variable(FUNC_IS_EVEN));
    isOddVarMapping.put(new Variable(FUNC_IS_ODD), new Variable(FUNC_IS_ODD));
    isOddVarMapping.put(new Variable(Constants.DEFAULT_ENTRY_FUNCTION_NAME),
        new Variable(Constants.DEFAULT_ENTRY_FUNCTION_NAME));

    Map<Variable, Variable> isEvenVarMapping = new HashMap<>();
    isEvenVarMapping.put(new Variable(VAR_COND), new Variable(VAR_COND));
    isEvenVarMapping.put(new Variable(Constants.VAR_RET), new Variable(Constants.VAR_RET));
    isEvenVarMapping.put(new Variable(Constants.VAR_OUT), new Variable(Constants.VAR_OUT));
    isEvenVarMapping.put(new Variable(Constants.VAR_IN), new Variable(Constants.VAR_IN));
    isEvenVarMapping.put(new Variable("a"), new Variable("c"));
    isEvenVarMapping.put(new Variable("i"), new Variable("x"));
    isEvenVarMapping.put(new Variable(FUNC_IS_EVEN), new Variable(FUNC_IS_EVEN));
    isEvenVarMapping.put(new Variable(FUNC_IS_ODD), new Variable(FUNC_IS_ODD));
    isEvenVarMapping.put(new Variable(Constants.DEFAULT_ENTRY_FUNCTION_NAME),
        new Variable(Constants.DEFAULT_ENTRY_FUNCTION_NAME));

    VariableMapping variableAlignmentResult = new VariableMapping();
    variableAlignmentResult.add(FUNC_IS_ODD, isOddVarMapping);
    variableAlignmentResult.add(FUNC_IS_EVEN, isEvenVarMapping);
    variableAlignmentResult.add(Constants.DEFAULT_ENTRY_FUNCTION_NAME, mainVarMapping);
    return variableAlignmentResult;
  }

  private Interpreter getInterpreter() {
    Interpreter interpreter =
        new InterpreterServiceImpl("c", Constants.DEFAULT_ENTRY_FUNCTION_NAME);
    return interpreter;
  }

  private Program getReferenceProgram() {
    Program referenceProgram = ModelProgram.MODEL_INFINITE_LOOP_TEST_TEST_C_C.get();
    return referenceProgram;
  }

  private Program getSubmittedProgram() {
    Program submittedProgram = ModelProgram.MODEL_INFINITE_LOOP_TEST_TEST_B_C.get();
    return submittedProgram;
  }

  private List<Input> getModelInputs() {
    return Collections.emptyList();
  }

  /**
   * This test aims at testing if the error localizer can correctly find the
   * error locations in a program having function calls which may cause infinite
   * loops between functions.
   */
  @Test
  public void infiniteLoopTest() {
    ErrorLocalizer errorLocalizer = new ErrorLocalizerImpl();
    ErrorLocalisation errorLocations = errorLocalizer.localizeErrors(getSubmittedProgram(),
        getReferenceProgram(),
        getModelInputs(),
        getStructuralMapping(),
        getVariableMapping(),
        getInterpreter());

    assertNotNull(errorLocations);
    List<ErrorLocation> errorsMain = errorLocations
            .getErrorLocations(Constants.DEFAULT_ENTRY_FUNCTION_NAME,
                getVariableMapping().getTopMapping(Constants.DEFAULT_ENTRY_FUNCTION_NAME));
    List<ErrorLocation> errorsOdd = errorLocations
        .getErrorLocations(FUNC_IS_ODD, getVariableMapping().getTopMapping(FUNC_IS_ODD));
    List<ErrorLocation> errorsEven = errorLocations
        .getErrorLocations(FUNC_IS_EVEN, getVariableMapping().getTopMapping(FUNC_IS_EVEN));

    assertNotNull(errorsMain);
    assertNotNull(errorsEven);
    assertNotNull(errorsOdd);

    assertTrue(errorsMain.isEmpty());
    assertFalse(errorsEven.isEmpty());
    assertFalse(errorsOdd.isEmpty());

    assertEquals(1, errorsEven.size());
    assertEquals(2, errorsEven.get(0).getLocationInSubmission());
    assertEquals(2, errorsEven.get(0).getLocationInReference());
    assertEquals(VAR_COND, errorsEven.get(0).getErroneousVariablesInSubmission().get(0).getName());

    assertEquals(1, errorsOdd.size());
    assertEquals(1, errorsOdd.get(0).getLocationInReference());
    assertEquals(1, errorsOdd.get(0).getLocationInReference());
    assertEquals(VAR_RET, errorsOdd.get(0).getErroneousVariablesInSubmission().get(0).getName());
  }
}
