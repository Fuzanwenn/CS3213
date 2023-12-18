package sg.edu.nus.se.its.errorlocalizer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static sg.edu.nus.se.its.util.constants.Constants.VAR_COND;
import static sg.edu.nus.se.its.util.constants.Constants.VAR_OUT;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import sg.edu.nus.se.its.alignment.StructuralMapping;
import sg.edu.nus.se.its.alignment.VariableMapping;
import sg.edu.nus.se.its.errorlocalizer.utils.ModelInput;
import sg.edu.nus.se.its.errorlocalizer.utils.ModelProgram;
import sg.edu.nus.se.its.interpreter.Interpreter;
import sg.edu.nus.se.its.interpreter.InterpreterServiceImpl;
import sg.edu.nus.se.its.model.Input;
import sg.edu.nus.se.its.model.Program;
import sg.edu.nus.se.its.model.Variable;
import sg.edu.nus.se.its.util.constants.Constants;

/**
 * Test class for Test7 program.
 */
public class Test7 {

  private StructuralMapping getStructuralMapping() {
    Map<Integer, Integer> mapping = new HashMap<>();
    mapping.put(1, 1);
    mapping.put(2, 2);
    mapping.put(3, 3);
    mapping.put(4, 4);
    mapping.put(5, 5);
    mapping.put(6, 6);
    mapping.put(7, 7);
    mapping.put(8, 8);
    mapping.put(9, 9);

    StructuralMapping structuralAlignmentResult = new StructuralMapping();
    structuralAlignmentResult.put(Constants.DEFAULT_ENTRY_FUNCTION_NAME, mapping);
    return structuralAlignmentResult;
  }

  private VariableMapping getVariableMapping() {

    Map<Variable, Variable> varMapping = new HashMap<>();
    varMapping.put(new Variable(VAR_COND), new Variable(VAR_COND));
    varMapping.put(new Variable(Constants.VAR_RET), new Variable(Constants.VAR_RET));
    varMapping.put(new Variable(VAR_OUT), new Variable(VAR_OUT));
    varMapping.put(new Variable(Constants.VAR_IN), new Variable(Constants.VAR_IN));
    varMapping.put(new Variable("i"), new Variable("j"));
    varMapping.put(new Variable("j"), new Variable("i"));
    varMapping.put(new Variable("N"), new Variable("N"));
    varMapping.put(new Variable("dummy1"), new Variable("a"));
    varMapping.put(new Variable("dummy2"), new Variable("n"));


    VariableMapping variableAlignmentResult = new VariableMapping();
    variableAlignmentResult.add(Constants.DEFAULT_ENTRY_FUNCTION_NAME, varMapping);
    return variableAlignmentResult;
  }

  private Interpreter getInterpreter() {
    Interpreter interpreter =
        new InterpreterServiceImpl("c", Constants.DEFAULT_ENTRY_FUNCTION_NAME);
    return interpreter;
  }

  private Program getReferenceProgram() {
    Program referenceProgram = ModelProgram.MODEL_TEST7_C_C.get();
    return referenceProgram;
  }

  private Program getSubmittedProgram() {
    Program submittedProgram = ModelProgram.MODEL_TEST7_B_C.get();
    return submittedProgram;
  }

  private List<Input> getModelInputs() {
    List<Input> inputs = ModelInput.MODEL_INPUT_TEST_7;
    return inputs;
  }

  /**
   * Tests a subject where we have an error in a loop, which only occurs in the second loop
   * iteration.
   */
  @Test
  public void testSeven() {
      int inputNo = 1;
      ErrorLocalizer errorLocalizer = new ErrorLocalizerImpl();
      ErrorLocalisation errorLocations = errorLocalizer.localizeErrors(getSubmittedProgram(),
              getReferenceProgram(),
              getModelInputs().subList(inputNo - 1, inputNo),
              getStructuralMapping(),
              getVariableMapping(),
              getInterpreter());

      List<ErrorLocation> errors = errorLocations
              .getErrorLocations(Constants.DEFAULT_ENTRY_FUNCTION_NAME,
                      getVariableMapping().getTopMapping(Constants.DEFAULT_ENTRY_FUNCTION_NAME));

      assertNotNull(errors);
      assertTrue(errors.size() == 4);

      assertEquals(6, errors.get(0).getLocationInReference());
      assertEquals(6, errors.get(0).getLocationInSubmission());
      assertEquals(5, errors.get(1).getLocationInReference());
      assertEquals(5, errors.get(1).getLocationInSubmission());
      assertEquals(7, errors.get(2).getLocationInReference());
      assertEquals(7, errors.get(2).getLocationInSubmission());
      assertEquals(9, errors.get(3).getLocationInReference());
      assertEquals(9, errors.get(3).getLocationInSubmission());

      List<Variable> firstErroneousVariables = errors.get(0).getErroneousVariablesInSubmission();
      assertFalse(firstErroneousVariables.isEmpty());
      assertTrue(firstErroneousVariables.size() == 1);
      assertEquals(VAR_COND, firstErroneousVariables.get(0).getName());

      List<Variable> secondErroneousVariables = errors.get(1).getErroneousVariablesInSubmission();
      assertFalse(secondErroneousVariables.isEmpty());
      assertTrue(secondErroneousVariables.size() == 1);
      assertEquals("i", secondErroneousVariables.get(0).getName());

      List<Variable> thirdErroneousVariables = errors.get(2).getErroneousVariablesInSubmission();
      assertFalse(thirdErroneousVariables.isEmpty());
      assertTrue(thirdErroneousVariables.size() == 1);
      assertEquals("i", thirdErroneousVariables.get(0).getName());

      List<Variable> fourthErroneousVariables = errors.get(3).getErroneousVariablesInSubmission();
      assertFalse(fourthErroneousVariables.isEmpty());
      assertTrue(fourthErroneousVariables.size() == 1);
      assertEquals(VAR_OUT, fourthErroneousVariables.get(0).getName());
  }

  @Test
  public void testSevenAllInputsTogether() {
    ErrorLocalizer errorLocalizer = new ErrorLocalizerImpl();
    ErrorLocalisation errorLocations = errorLocalizer.localizeErrors(getSubmittedProgram(),
        getReferenceProgram(),
        getModelInputs(),
        getStructuralMapping(),
        getVariableMapping(),
        getInterpreter());

    List<ErrorLocation> errors = errorLocations
        .getErrorLocations(Constants.DEFAULT_ENTRY_FUNCTION_NAME,
        getVariableMapping().getTopMapping(Constants.DEFAULT_ENTRY_FUNCTION_NAME));

    assertNotNull(errors);
    assertTrue(errors.size() == 20);
  }
}
