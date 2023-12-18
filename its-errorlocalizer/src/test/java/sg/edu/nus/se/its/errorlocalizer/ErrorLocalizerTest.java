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
import sg.edu.nus.se.its.errorlocalizer.utils.ModelProgram;
import sg.edu.nus.se.its.interpreter.Interpreter;
import sg.edu.nus.se.its.interpreter.InterpreterServiceImpl;
import sg.edu.nus.se.its.model.Input;
import sg.edu.nus.se.its.model.Program;
import sg.edu.nus.se.its.model.Variable;
import sg.edu.nus.se.its.util.constants.Constants;

/**
 * Test class for ErrorLocalizer.
 */
public class ErrorLocalizerTest {

  /**
   * Simple basic test.
   */
  @Test
  public void testSimpleNoErrorLocalizer() {

    StructuralMapping structuralAlignmentResult = new StructuralMapping();
    Map<Integer, Integer> mapping = new HashMap<>();
    mapping.put(1, 1);
    structuralAlignmentResult.put(Constants.DEFAULT_ENTRY_FUNCTION_NAME, mapping);

    Map<Variable, Variable> varMapping = new HashMap<>();
    varMapping.put(new Variable(Constants.VAR_COND), new Variable(Constants.VAR_COND));
    varMapping.put(new Variable(Constants.VAR_RET), new Variable(Constants.VAR_RET));
    varMapping.put(new Variable(Constants.VAR_OUT), new Variable(Constants.VAR_OUT));
    varMapping.put(new Variable(Constants.VAR_IN), new Variable(Constants.VAR_IN));
    varMapping.put(new Variable("a"), new Variable("a"));
    varMapping.put(new Variable("b"), new Variable("b"));

    VariableMapping variableAlignmentResult = new VariableMapping();
    variableAlignmentResult.add(Constants.DEFAULT_ENTRY_FUNCTION_NAME, varMapping);

    Interpreter interpreter =
        new InterpreterServiceImpl("c", Constants.DEFAULT_ENTRY_FUNCTION_NAME);

    Program referenceProgram = ModelProgram.MODEL_C1_C.get();
    Program submittedProgram = ModelProgram.MODEL_C1_C.get();
    List<Input> inputs = Collections.emptyList();

    ErrorLocalizer errorLocalizer = new ErrorLocalizerImpl();
    ErrorLocalisation errorLocations = errorLocalizer.localizeErrors(submittedProgram,
        referenceProgram, inputs, structuralAlignmentResult, variableAlignmentResult, interpreter);

    assertNotNull(errorLocations);

    List<ErrorLocation> errors =
        errorLocations.getErrorLocations(Constants.DEFAULT_ENTRY_FUNCTION_NAME, varMapping);
    assertNotNull(errors);

    assertTrue(errors.isEmpty());
  }

  /**
   * Tests a subject where we have a mismatch in import statements
   */
  @Test
  public void testErrorLocalizerDifferentImportStatements() {
    Map<Integer, Integer> mapping = new HashMap<>();
    mapping.put(1, 1);
    mapping.put(2, 2);
    mapping.put(3, 3);
    mapping.put(4, 5);
    mapping.put(5, 4);
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

    Program referenceProgram = ModelProgram.MODEL_LOOP_I_C.get();

    Program submittedProgram = ModelProgram.MODEL_LOOP_D_C.get();

    List<Input> inputs = Collections.emptyList();

    ErrorLocalizer errorLocalizer = new ErrorLocalizerImpl();
    ErrorLocalisation errorLocations = errorLocalizer.localizeErrors(submittedProgram,
        referenceProgram, inputs, structuralAlignmentResult, variableAlignmentResult, interpreter);

    List<ErrorLocation> errors =
        errorLocations.getErrorLocations(Constants.DEFAULT_ENTRY_FUNCTION_NAME, varMapping);
    assertTrue(errors.size() == 3);
    assertNotNull(errors);
  }

  @Test
  public void testLoopSameProgramErrorLocalizer() {
    Map<Integer, Integer> mapping = new HashMap<>();
    mapping.put(1, 1);
    mapping.put(2, 2);
    mapping.put(3, 3);
    mapping.put(4, 5);
    mapping.put(5, 4);
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

    Program submittedProgram = ModelProgram.MODEL_LOOP_I_C.get();

    Program referenceProgram = ModelProgram.MODEL_LOOP_I_DIFF_C.get();

    List<Input> inputs = Collections.emptyList();

    ErrorLocalizer errorLocalizer = new ErrorLocalizerImpl();
    ErrorLocalisation errorLocations = errorLocalizer.localizeErrors(submittedProgram,
        referenceProgram, inputs, structuralAlignmentResult, variableAlignmentResult, interpreter);

    List<ErrorLocation> errors =
        errorLocations.getErrorLocations(Constants.DEFAULT_ENTRY_FUNCTION_NAME, varMapping);
    assertNotNull(errors);

    /**
     * Same Programs
     */
    assertTrue(errors.isEmpty());
  }
}
