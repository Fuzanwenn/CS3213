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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * This tests if the error localizer can correctly find the
 * error locations in an incorrect program that never terminates (infiniteLoop2_c.c)
 */
public class InfiniteLoopTest2 {
  @Test
  public void infiniteLoopTest2() {
    Program submittedProgram = ModelProgram.MODEL_INFINITE_LOOP_TEST2_B_C.get();
    Program referenceProgram = ModelProgram.MODEL_INFINITE_LOOP_TEST2_C_C.get();

    List<Input> inputs = new ArrayList<>();

    StructuralMapping structuralMapping = new StructuralMapping();
    Map<Integer, Integer> locationMapping = new HashMap<>();
    locationMapping.put(1,1);
    locationMapping.put(2,2);
    locationMapping.put(3,3);
    locationMapping.put(4,4);
    structuralMapping.put(Constants.DEFAULT_ENTRY_FUNCTION_NAME, locationMapping);

    VariableMapping variableMapping = new VariableMapping();
    Map<Variable, Variable> varMapping = new HashMap<>();
    varMapping.put(new Variable("numTimes"), new Variable("numTimes"));
    varMapping.put(new Variable("count"), new Variable("count"));
    varMapping.put(new Variable(Constants.VAR_COND), new Variable(Constants.VAR_COND));
    varMapping.put(new Variable(Constants.VAR_RET), new Variable(Constants.VAR_RET));
    variableMapping.add(Constants.DEFAULT_ENTRY_FUNCTION_NAME, varMapping);

    Interpreter interpreter = 
            new InterpreterServiceImpl("c", Constants.DEFAULT_ENTRY_FUNCTION_NAME);

    ErrorLocalizerImpl errorLocalizer = new ErrorLocalizerImpl();
    ErrorLocalisation errorLocalisation = errorLocalizer.localizeErrors(submittedProgram, 
            referenceProgram, inputs, structuralMapping, variableMapping, interpreter);
    
    assertNotNull(errorLocalisation);

    List<ErrorLocation> errorLocations = 
            errorLocalisation.getErrorLocations(Constants.DEFAULT_ENTRY_FUNCTION_NAME, varMapping);
    assertEquals(2, errorLocations.size());
    
    // missing line to update numTimes in body of while-loop, the cause of the infinite loop
    List<ErrorLocation>  errorsDueToUnmatchedException = errorLocations.stream()
            .filter(errLoc -> errLoc.getErrorType() == ErrorLocation.ErrorType.UnmatchedException)
            .collect(Collectors.toList());
    assertEquals(1, errorsDueToUnmatchedException.size());
    assertEquals(4, errorsDueToUnmatchedException.get(0).getLocationInSubmission());
    
    // while condition itself is also reported as error 
    // (missing parents in submission dependency tree of $cond since no dependency on numTimes)
    List<ErrorLocation>  errorsDueToVariableMismatch = errorLocations.stream()
            .filter(errLoc -> errLoc.getErrorType() == ErrorLocation.ErrorType.VariableValueMismatch)
            .collect(Collectors.toList());
    assertEquals(1, errorsDueToVariableMismatch.size());
    assertEquals(2, errorsDueToVariableMismatch.get(0).getLocationInReference());
    assertEquals(Constants.VAR_COND, 
            errorsDueToVariableMismatch.get(0).getErroneousVariablesInSubmission().get(0).getName());
  }
}
