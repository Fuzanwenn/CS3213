package sg.edu.nus.se.its.errorlocalizer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static sg.edu.nus.se.its.errorlocalizer.ErrorLocalizerImpl.LOC_IMPORT_STATEMENT;
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
 * Test class for Test3 program.
 */
public class Test3 {

  private StructuralMapping getStructuralMapping() {
    Map<Integer, Integer> mapping = new HashMap<>();
    mapping.put(1, 1);
    mapping.put(2, 2);
    mapping.put(3, 3);
    mapping.put(4, 4);

    StructuralMapping structuralAlignmentResult = new StructuralMapping();
    structuralAlignmentResult.put(Constants.DEFAULT_ENTRY_FUNCTION_NAME, mapping);
    return structuralAlignmentResult;
  }

  private VariableMapping getVariableMapping() {
    Map<Variable, Variable> varMapping = new HashMap<>();
    varMapping.put(new Variable(Constants.VAR_COND), new Variable(Constants.VAR_COND));
    varMapping.put(new Variable(Constants.VAR_RET), new Variable(Constants.VAR_RET));
    varMapping.put(new Variable(VAR_OUT), new Variable(VAR_OUT));
    varMapping.put(new Variable(Constants.VAR_IN), new Variable(Constants.VAR_IN));
    varMapping.put(new Variable(Constants.VAR_IN), new Variable(Constants.VAR_IN));
    varMapping.put(new Variable("n"), new Variable("a"));
    varMapping.put(new Variable("m"), new Variable("c"));
    varMapping.put(new Variable("old"), new Variable("e"));
    varMapping.put(new Variable("dummy1"), new Variable("b"));

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
    Program referenceProgram = ModelProgram.MODEL_TEST3_C_C.get();
    return referenceProgram;
  }

  private Program getSubmittedProgram() {
    Program submittedProgram = ModelProgram.MODEL_TEST3_B_C.get();
    return submittedProgram;
  }

  private List<Input> getModelInputs() {
    List<Input> inputs = ModelInput.MODEL_INPUT_TEST_3;
    return inputs;
  }

  @Test
  public void testThreeSize() {
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
    assertTrue(errors.size() == 5);
  }

  @Test
  public void testThreeVariable() {
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

    assertEquals(LOC_IMPORT_STATEMENT, errors.get(0).getLocationInReference());
    assertEquals(LOC_IMPORT_STATEMENT, errors.get(0).getLocationInSubmission());
    assertEquals(LOC_IMPORT_STATEMENT, errors.get(1).getLocationInReference());
    assertEquals(LOC_IMPORT_STATEMENT, errors.get(1).getLocationInSubmission());
    assertEquals(1, errors.get(2).getLocationInReference());
    assertEquals(1, errors.get(2).getLocationInSubmission());
    assertEquals(3, errors.get(3).getLocationInReference());
    assertEquals(3, errors.get(3).getLocationInSubmission());
    assertEquals(4, errors.get(4).getLocationInReference());
    assertEquals(4, errors.get(4).getLocationInSubmission());

    List<Variable> fisrtErroneousVariables = errors.get(3).getErroneousVariablesInSubmission();
    assertNotNull(fisrtErroneousVariables);
    assertFalse(fisrtErroneousVariables.isEmpty());
    assertTrue(fisrtErroneousVariables.size() == 1);
    assertEquals(VAR_OUT, fisrtErroneousVariables.get(0).getName());

    List<Variable> erroneousVariables = errors.get(4).getErroneousVariablesInSubmission();
    assertNotNull(erroneousVariables);
    assertFalse(erroneousVariables.isEmpty());
    assertTrue(erroneousVariables.size() == 1);
    assertEquals("c", erroneousVariables.get(0).getName());
  }

  @Test
  public void testThreeAllInputsTogether() {
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
    assertTrue(errors.size() == 25);
  }
}
