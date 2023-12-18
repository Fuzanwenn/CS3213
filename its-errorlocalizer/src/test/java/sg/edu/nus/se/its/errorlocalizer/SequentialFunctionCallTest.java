package sg.edu.nus.se.its.errorlocalizer;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Test if the error localizer can detect the error in sequential function calls.
 */
public class SequentialFunctionCallTest {

  private static final String FUNC_ECHO = "echo";
  private static final String FUNC_ECHO_ECHO = "echoEcho";
  private static final String FUNC_ECHO_ECHO_ECHO = "echoEchoEcho";

  /**
   * Get the structural mapping for the program.
   *
   * @return Structural mapping for the program.
   */
  private StructuralMapping getStructuralMapping() {
    Map<Integer, Integer> commonMapping = new HashMap<>();
    commonMapping.put(1, 1);

    StructuralMapping structuralAlignmentResult = new StructuralMapping();
    structuralAlignmentResult.put(Constants.DEFAULT_ENTRY_FUNCTION_NAME, commonMapping);
    structuralAlignmentResult.put(FUNC_ECHO, commonMapping);
    structuralAlignmentResult.put(FUNC_ECHO_ECHO, commonMapping);
    structuralAlignmentResult.put(FUNC_ECHO_ECHO_ECHO, commonMapping);
    return structuralAlignmentResult;
  }

  /**
   * Get the variable mapping for the program.
   *
   * @return Variable mapping for the program.
   */
  private VariableMapping getVariableMapping() {
    Map<Variable, Variable> commonVarMapping = new HashMap<>();
    commonVarMapping.put(new Variable(Constants.VAR_COND), new Variable(Constants.VAR_COND));
    commonVarMapping.put(new Variable(Constants.VAR_RET), new Variable(Constants.VAR_RET));
    commonVarMapping.put(new Variable(Constants.VAR_OUT), new Variable(Constants.VAR_OUT));
    commonVarMapping.put(new Variable(Constants.VAR_IN), new Variable(Constants.VAR_IN));

    VariableMapping variableAlignmentResult = new VariableMapping();
    variableAlignmentResult.add(FUNC_ECHO, commonVarMapping);
    variableAlignmentResult.add(FUNC_ECHO_ECHO, commonVarMapping);
    variableAlignmentResult.add(FUNC_ECHO_ECHO_ECHO, commonVarMapping);
    variableAlignmentResult.add(Constants.DEFAULT_ENTRY_FUNCTION_NAME, commonVarMapping);
    return variableAlignmentResult;
  }

  /**
   * Get the interpreter for the program.
   *
   * @return Interpreter for the program.
   */
  private Interpreter getInterpreter() {
    Interpreter interpreter =
        new InterpreterServiceImpl("c", Constants.DEFAULT_ENTRY_FUNCTION_NAME);
    return interpreter;
  }

  /**
   * Get the reference program.
   *
   * @return Reference program.
   */
  private Program getReferenceProgram() {
    Program referenceProgram = ModelProgram.MODEL_SEQUENTIAL_FUNCTION_CALL_TEST_C_C.get();
    return referenceProgram;
  }

  /**
   * Get the submitted program.
   *
   * @return Submitted program.
   */
  private Program getSubmittedProgram() {
    Program submittedProgram = ModelProgram.MODEL_SEQUENTIAL_FUNCTION_CALL_TEST_B_C.get();
    return submittedProgram;
  }

  /**
   * Get the model inputs.
   *
   * @return Model inputs.
   */
  private List<Input> getModelInputs() {
    return Collections.emptyList();
  }

  /**
   * Test if the error localizer can detect the error in sequential function calls.
   */
  @Test
  public void testSequentialFunctionCall() {
    ErrorLocalizer errorLocalizer = new ErrorLocalizerImpl();
    ErrorLocalisation errorLocations = errorLocalizer.localizeErrors(getSubmittedProgram(),
        getReferenceProgram(),
        getModelInputs(),
        getStructuralMapping(),
        getVariableMapping(),
        getInterpreter());

    List<ErrorLocation> errors = errorLocations
        .getErrorLocations(FUNC_ECHO_ECHO_ECHO,
            getVariableMapping().getTopMapping(FUNC_ECHO_ECHO_ECHO));

    assertNotNull(errors);
    assertEquals(1, errors.size());

    assertEquals(1, errors.get(0).getLocationInReference());
    assertEquals(1, errors.get(0).getLocationInSubmission());

    List<Variable> erroneousVariables = errors.get(0).getErroneousVariablesInSubmission();
    assertNotNull(erroneousVariables);
    assertEquals(1, erroneousVariables.size());
    assertEquals(Constants.VAR_RET, erroneousVariables.get(0).getName());
  }
}
