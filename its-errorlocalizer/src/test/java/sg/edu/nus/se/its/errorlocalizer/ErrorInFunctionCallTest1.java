package sg.edu.nus.se.its.errorlocalizer;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static sg.edu.nus.se.its.util.constants.Constants.VAR_RET;

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
 * Test if the error localizer can detect the error in the return statement of function call.
 */
public class ErrorInFunctionCallTest1 {
    public static final String FUNC_FACTORIAL = "factorial";

    /**
     * Get the structural mapping for the program.
     *
     * @return Structural mapping for the program.
     */
    private StructuralMapping getStructuralMapping() {
        Map<Integer, Integer> mapping = new HashMap<>();
        mapping.put(1, 1);

        StructuralMapping structuralAlignmentResult = new StructuralMapping();
        structuralAlignmentResult.put(Constants.DEFAULT_ENTRY_FUNCTION_NAME, mapping);
        structuralAlignmentResult.put(FUNC_FACTORIAL, mapping);
        return structuralAlignmentResult;
    }

    /**
     * Get the variable mapping for the program.
     *
     * @return Variable mapping for the program.
     */
    private VariableMapping getVariableMapping() {
        Map<Variable, Variable> varMapping = new HashMap<>();
        varMapping.put(new Variable(Constants.VAR_COND), new Variable(Constants.VAR_COND));
        varMapping.put(new Variable(VAR_RET), new Variable(VAR_RET));
        varMapping.put(new Variable(Constants.VAR_OUT), new Variable(Constants.VAR_OUT));
        varMapping.put(new Variable(Constants.VAR_IN), new Variable(Constants.VAR_IN));
        varMapping.put(new Variable("a"), new Variable("a"));
        varMapping.put(new Variable("b"), new Variable("b"));

        Map<Variable, Variable> varMappingForFactorial = new HashMap<>();
        varMappingForFactorial.put(new Variable(Constants.VAR_COND), new Variable(Constants.VAR_COND));
        varMappingForFactorial.put(new Variable(VAR_RET), new Variable(VAR_RET));
        varMappingForFactorial.put(new Variable(Constants.VAR_OUT), new Variable(Constants.VAR_OUT));
        varMappingForFactorial.put(new Variable(Constants.VAR_IN), new Variable(Constants.VAR_IN));
        varMappingForFactorial.put(new Variable("x"), new Variable("x"));

        VariableMapping variableAlignmentResult = new VariableMapping();
        variableAlignmentResult.add(FUNC_FACTORIAL, varMappingForFactorial);
        variableAlignmentResult.add(Constants.DEFAULT_ENTRY_FUNCTION_NAME, varMapping);
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
        Program referenceProgram = ModelProgram.MODEL_ERROR_IN_FUNCTION_CALL_TEST1_C_C.get();
        return referenceProgram;
    }

    /**
     * Get the submitted program.
     *
     * @return Submitted program.
     */
    private Program getSubmittedProgram() {
        Program submittedProgram = ModelProgram.MODEL_ERROR_IN_FUNCTION_CALL_TEST1_B_C.get();
        return submittedProgram;
    }

    /**
     * Get the model inputs.
     *
     * @return Model inputs.
     */
    private List<Input> getModelInputs() {
        List<Input> inputs = ModelInput.MODEL_INPUT_ERROR_IN_FUNCTION_CALL_TEST1;
        return inputs;
    }


    /**
     * Test if the error localizer can get correct error size in the program.
     */
    @Test
    public void testErrorInFunctionCallSize() {
        int inputNo = 1;
        ErrorLocalizer errorLocalizer = new ErrorLocalizerImpl();
        ErrorLocalisation errorLocations = errorLocalizer.localizeErrors(getSubmittedProgram(),
                getReferenceProgram(),
                getModelInputs().subList(inputNo - 1, inputNo),
                getStructuralMapping(),
                getVariableMapping(),
                getInterpreter());

        List<ErrorLocation> errors = errorLocations
                .getErrorLocations(FUNC_FACTORIAL,
                        getVariableMapping().getTopMapping(FUNC_FACTORIAL));

        assertNotNull(errors);
        assertTrue(errors.size() == 1);

        assertEquals(1, errors.get(0).getLocationInReference());
        assertEquals(1, errors.get(0).getLocationInSubmission());
    }

    /**
     * Test if the error localizer can get correct error type in the program.
     */
    @Test
    public void testErrorInFunctionCallErroneousVariables() {
        int inputNo = 1;
        ErrorLocalizer errorLocalizer = new ErrorLocalizerImpl();
        ErrorLocalisation errorLocations = errorLocalizer.localizeErrors(getSubmittedProgram(),
                getReferenceProgram(),
                getModelInputs().subList(inputNo - 1, inputNo),
                getStructuralMapping(),
                getVariableMapping(),
                getInterpreter());

        List<ErrorLocation> errors = errorLocations
                .getErrorLocations(FUNC_FACTORIAL,
                        getVariableMapping().getTopMapping(FUNC_FACTORIAL));

        List<Variable> erroneousVariables = errors.get(0).getErroneousVariablesInSubmission();
        assertFalse(erroneousVariables.isEmpty());
        assertTrue(erroneousVariables.size() == 1);
        assertEquals(VAR_RET, erroneousVariables.get(0).getName());
    }

    /**
     * Test if the error localizer can get all expected errors given all inputs together.
     */
    @Test
    public void testErrorInFunctionCallAllInputsTogether() {
        ErrorLocalizer errorLocalizer = new ErrorLocalizerImpl();
        ErrorLocalisation errorLocations = errorLocalizer.localizeErrors(getSubmittedProgram(),
                getReferenceProgram(),
                getModelInputs(),
                getStructuralMapping(),
                getVariableMapping(),
                getInterpreter());

        List<ErrorLocation> errors = errorLocations
                .getErrorLocations(FUNC_FACTORIAL,
                        getVariableMapping().getTopMapping(FUNC_FACTORIAL));

        assertNotNull(errors);
        assertTrue(errors.size() == 4);
    }
}
