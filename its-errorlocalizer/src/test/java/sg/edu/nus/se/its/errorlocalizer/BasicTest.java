package sg.edu.nus.se.its.errorlocalizer;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import sg.edu.nus.se.its.alignment.StructuralMapping;
import sg.edu.nus.se.its.alignment.VariableMapping;
import sg.edu.nus.se.its.interpreter.Interpreter;
import sg.edu.nus.se.its.interpreter.InterpreterServiceImpl;
import sg.edu.nus.se.its.model.Input;
import sg.edu.nus.se.its.model.Program;
import sg.edu.nus.se.its.model.Variable;
import sg.edu.nus.se.its.util.TestUtils;
import sg.edu.nus.se.its.util.constants.Constants;

/**
 * Simple test collection for the interpreter.
 */
public class BasicTest {

  static final String unitTestSourceFilePath =
      System.getProperty("user.dir") + "/src/test" + "/resources/";
  static final String unitTestModelFilePath = unitTestSourceFilePath + "model/";

  @Test
  public void testInterpreter() {
    Interpreter cInterpreter =
        new InterpreterServiceImpl("c", Constants.DEFAULT_ENTRY_FUNCTION_NAME);
    Program program = TestUtils.loadProgramByFilePath(unitTestModelFilePath + "arith.c.json");
    assertDoesNotThrow(() -> cInterpreter.executeProgram(program));
  }

  /**
   * Simple basic test.
   */
  @Test
  public void testSimpleErrorLocalizer() {

    StructuralMapping structuralAlignmentResult = new StructuralMapping();
    Map<Integer, Integer> mapping = new HashMap<>();
    mapping.put(1, 1);
    structuralAlignmentResult.put(Constants.DEFAULT_ENTRY_FUNCTION_NAME, mapping);

    Map<Variable, Variable> varMapping = new HashMap<>();
    varMapping.put(new Variable(Constants.VAR_COND), new Variable(Constants.VAR_COND));
    varMapping.put(new Variable(Constants.VAR_RET), new Variable(Constants.VAR_RET));
    varMapping.put(new Variable(Constants.VAR_OUT), new Variable(Constants.VAR_OUT));
    varMapping.put(new Variable(Constants.VAR_IN), new Variable(Constants.VAR_IN));
    varMapping.put(new Variable("a"), new Variable("x"));
    varMapping.put(new Variable("b"), new Variable("y"));

    VariableMapping variableAlignmentResult = new VariableMapping();
    variableAlignmentResult.add(Constants.DEFAULT_ENTRY_FUNCTION_NAME, varMapping);

    Interpreter interpreter =
        new InterpreterServiceImpl("c", Constants.DEFAULT_ENTRY_FUNCTION_NAME);

    Program submittedProgram = TestUtils.loadProgramByFilePath(unitTestModelFilePath + "i1.c.json");
    Program referenceProgram = TestUtils.loadProgramByFilePath(unitTestModelFilePath + "c1.c.json");

    List<Input> inputs = Collections.emptyList();

    ErrorLocalizer errorLocalizer = new ErrorLocalizerImpl();
    ErrorLocalisation errorLocations = errorLocalizer.localizeErrors(submittedProgram,
        referenceProgram, inputs, structuralAlignmentResult, variableAlignmentResult, interpreter);

    assertNotNull(errorLocations);

    List<ErrorLocation> errors =
        errorLocations.getErrorLocations(Constants.DEFAULT_ENTRY_FUNCTION_NAME, varMapping);
    assertNotNull(errors);

    assertFalse(errors.isEmpty());
    assertTrue(errors.size() == 1);
    assertEquals(1, errors.get(0).getLocationInReference());
    assertEquals(1, errors.get(0).getLocationInSubmission());

    List<Variable> erroneousVariables = errors.get(0).getErroneousVariablesInSubmission();
    assertNotNull(erroneousVariables);
    assertFalse(erroneousVariables.isEmpty());
    assertTrue(erroneousVariables.size() == 1);
    assertEquals("y", erroneousVariables.get(0).getName());
  }

  /**
   * Tests a subject where we have an error in a loop, which only occurs in the second loop
   * iteration.
   */
  @Test
  public void testLoopIterationErrorLocalizer() {
    Map<Integer, Integer> mapping = new HashMap<>();
    mapping.put(1, 1);
    mapping.put(2, 2);
    mapping.put(3, 3);
    mapping.put(4, 4);
    mapping.put(5, 5);
    StructuralMapping structuralAlignmentResult = new StructuralMapping();
    structuralAlignmentResult.put(Constants.DEFAULT_ENTRY_FUNCTION_NAME, mapping);

    Map<Variable, Variable> varMapping = new HashMap<>();
    varMapping.put(new Variable(Constants.VAR_COND), new Variable(Constants.VAR_COND));
    varMapping.put(new Variable(Constants.VAR_RET), new Variable(Constants.VAR_RET));
    varMapping.put(new Variable(Constants.VAR_OUT), new Variable(Constants.VAR_OUT));
    varMapping.put(new Variable(Constants.VAR_IN), new Variable(Constants.VAR_IN));
    varMapping.put(new Variable("i"), new Variable("i"));
    varMapping.put(new Variable("result"), new Variable("result"));

    VariableMapping variableAlignmentResult = new VariableMapping();
    variableAlignmentResult.add(Constants.DEFAULT_ENTRY_FUNCTION_NAME, varMapping);

    Interpreter interpreter =
        new InterpreterServiceImpl("c", Constants.DEFAULT_ENTRY_FUNCTION_NAME);

    Program submittedProgram =
        TestUtils.loadProgramByFilePath(unitTestModelFilePath + "loop-c.c.json");

    Program referenceProgram =
        TestUtils.loadProgramByFilePath(unitTestModelFilePath + "loop-i.c.json");

    List<Input> inputs = Collections.emptyList();

    ErrorLocalizer errorLocalizer = new ErrorLocalizerImpl();
    ErrorLocalisation errorLocations = errorLocalizer.localizeErrors(submittedProgram,
        referenceProgram, inputs, structuralAlignmentResult, variableAlignmentResult, interpreter);

    List<ErrorLocation> errors =
        errorLocations.getErrorLocations(Constants.DEFAULT_ENTRY_FUNCTION_NAME, varMapping);
    assertNotNull(errors);

    assertFalse(errors.isEmpty());
    assertTrue(errors.size() == 1);

    assertEquals(4, errors.get(0).getLocationInReference());
    assertEquals(4, errors.get(0).getLocationInSubmission());

    List<Variable> erroneousVariables = errors.get(0).getErroneousVariablesInSubmission();
    assertNotNull(erroneousVariables);
    assertFalse(erroneousVariables.isEmpty());
    assertTrue(erroneousVariables.size() == 1);
    assertEquals("result", erroneousVariables.get(0).getName());
  }

}
