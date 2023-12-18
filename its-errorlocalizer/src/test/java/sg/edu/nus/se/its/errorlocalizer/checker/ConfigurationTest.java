package sg.edu.nus.se.its.errorlocalizer.checker;

import org.junit.jupiter.api.Test;

import sg.edu.nus.se.its.alignment.StructuralMapping;
import sg.edu.nus.se.its.alignment.VariableMapping;
import sg.edu.nus.se.its.errorlocalizer.checker.exception.IllegalTransformationException;
import sg.edu.nus.se.its.errorlocalizer.utils.ModelInput;
import sg.edu.nus.se.its.errorlocalizer.utils.ModelProgram;
import sg.edu.nus.se.its.interpreter.Interpreter;
import sg.edu.nus.se.its.interpreter.InterpreterServiceImpl;
import sg.edu.nus.se.its.model.Function;
import sg.edu.nus.se.its.model.Input;
import sg.edu.nus.se.its.model.Program;
import sg.edu.nus.se.its.model.Variable;
import sg.edu.nus.se.its.util.constants.Constants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static sg.edu.nus.se.its.util.constants.Constants.VAR_OUT;

/**
 * Test for configuration class.
 */
public class ConfigurationTest {
  private Function refFunc = getReferenceProgram().getfnc(Constants.DEFAULT_ENTRY_FUNCTION_NAME);
  private Function subFunc = getSubmittedProgram().getfnc(Constants.DEFAULT_ENTRY_FUNCTION_NAME);

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
    varMapping.put(new Variable(Constants.VAR_COND), new Variable(Constants.VAR_COND));
    varMapping.put(new Variable(Constants.VAR_RET), new Variable(Constants.VAR_RET));
    varMapping.put(new Variable(VAR_OUT), new Variable(VAR_OUT));
    varMapping.put(new Variable(Constants.VAR_IN), new Variable(Constants.VAR_IN));
    varMapping.put(new Variable("N"), new Variable("N"));
    varMapping.put(new Variable("width"), new Variable("w"));
    varMapping.put(new Variable("height"), new Variable("h"));
    varMapping.put(new Variable("i"), new Variable("b"));
    varMapping.put(new Variable("j"), new Variable("a"));

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
    Program referenceProgram = ModelProgram.MODEL_TEST1_C_C.get();
    return referenceProgram;
  }

  private Program getSubmittedProgram() {
    Program submittedProgram = ModelProgram.MODEL_TEST1_B_C.get();
    return submittedProgram;
  }

  private List<Input> getModelInputs() {
    List<Input> inputs = ModelInput.MODEL_INPUT_TEST_1;
    return inputs;
  }

  /**
   * Test of method, of class Configuration.
   */
  @Test
  public void testOf() {
    Input input = getModelInputs().get(0);
    Configuration configuration = Configuration.of(input);
    assertEquals(configuration.getProgramInput(), input);
  }

  /**
   * Test for the first stage of configuration.
   */
  @Test
  public void stageOne() {
    Input input = getModelInputs().get(0);
    Configuration configuration = Configuration.of(input);
    assertThrows(IllegalTransformationException.class,
        () -> configuration.withLocs(1, 1));
    assertThrows(IllegalTransformationException.class,
        () -> configuration.withPrograms(getReferenceProgram(), getSubmittedProgram()));
    assertThrows(IllegalTransformationException.class,
        () -> configuration.withVariables(new Variable("a"), new Variable("x")));
    assertDoesNotThrow(() -> configuration.withFunctions(refFunc, subFunc));
  }

  /**
   * Test for the second stage of configuration.
   */
  @Test
  public void stageTwo() {
    Input input = getModelInputs().get(0);
    Configuration configuration = Configuration.of(input).withFunctions(refFunc, subFunc);
    assertThrows(IllegalTransformationException.class,
        () -> configuration.withPrograms(getReferenceProgram(), getSubmittedProgram()));
    assertThrows(IllegalTransformationException.class,
        () -> configuration.withVariables(new Variable("a"), new Variable("x")));
    assertDoesNotThrow(() -> configuration.withLocs(1, 1));
  }

  /**
   * Test for the third stage of configuration.
   */
  @Test
  public void stageThree() {
    Input input = getModelInputs().get(0);
    Configuration configuration = Configuration.of(input)
        .withFunctions(refFunc, subFunc).withLocs(1, 1);
    assertThrows(IllegalTransformationException.class,
        () -> configuration.withPrograms(getReferenceProgram(), getSubmittedProgram()));
    assertDoesNotThrow(
        () -> configuration.withVariables(new Variable("a"), new Variable("x")));
    assertDoesNotThrow(() -> configuration.withLocs(1, 1));
  }

  /**
   * Test for the fourth stage of configuration.
   */
  @Test
  public void stageFour() {
    Input input = getModelInputs().get(0);
    Variable refVar = new Variable("i");
    Variable subVar = new Variable("b");
    int refLoc = 1;
    int subLoc = 9999;

    Configuration configuration = Configuration.of(input)
        .withFunctions(refFunc, subFunc).withLocs(refLoc, subLoc)
        .withVariables(refVar, subVar);
//    Forward to stage 5
    assertDoesNotThrow(() -> configuration
        .withPrograms(getReferenceProgram(), getSubmittedProgram()));
    assertNotNull(configuration.getRefTree());
    assertNotNull(configuration.getSubTree());
    assertEquals(configuration.getReferenceFunction(), refFunc);
    assertEquals(configuration.getSubmittedFunction(), subFunc);
    assertEquals(configuration.getRefLoc(), refLoc);
    assertEquals(configuration.getSubLoc(), subLoc);
    assertEquals(configuration.getReferenceVariable(), refVar);
    assertEquals(configuration.getSubmittedVariable(), subVar);

    assertThrows(IllegalTransformationException.class,
        () -> configuration.withPrograms(getReferenceProgram(), null));
    assertThrows(IllegalTransformationException.class,
        () -> configuration.withPrograms(null, getSubmittedProgram()));
  }
}
