package sg.edu.nus.se.its.errorlocalizer;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static sg.edu.nus.se.its.util.constants.Constants.VAR_OUT;
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
public class ErrorInFunctionCallTest2 {
    public static final String FUNC_ADD = "add";

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
        structuralAlignmentResult.put(FUNC_ADD, mapping);
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

        Map<Variable, Variable> varMappingForAdd = new HashMap<>();
        varMappingForAdd.put(new Variable(Constants.VAR_COND), new Variable(Constants.VAR_COND));
        varMappingForAdd.put(new Variable(VAR_RET), new Variable(VAR_RET));
        varMappingForAdd.put(new Variable(Constants.VAR_OUT), new Variable(Constants.VAR_OUT));
        varMappingForAdd.put(new Variable(Constants.VAR_IN), new Variable(Constants.VAR_IN));
        varMappingForAdd.put(new Variable("x"), new Variable("x"));
        varMappingForAdd.put(new Variable("y"), new Variable("y"));

        VariableMapping variableAlignmentResult = new VariableMapping();
        variableAlignmentResult.add(Constants.DEFAULT_ENTRY_FUNCTION_NAME, varMapping);
        variableAlignmentResult.add(FUNC_ADD, varMappingForAdd);
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
        Program referenceProgram = ModelProgram.MODEL_ERROR_IN_FUNCTION_CALL_TEST2_C_C.get();
        return referenceProgram;
    }

    /**
     * Get the submitted program.
     *
     * @return Submitted program.
     */
    private Program getSubmittedProgram() {
        Program submittedProgram = ModelProgram.MODEL_ERROR_IN_FUNCTION_CALL_TEST2_B_C.get();
        return submittedProgram;
    }

    /**
     * Get the model inputs.
     *
     * @return Model inputs.
     */
    private List<Input> getModelInputs() {
        List<Input> inputs = ModelInput.MODEL_INPUT_ERROR_IN_FUNCTION_CALL_TEST2;
        return inputs;
    }

    /**
     * Test if the error localizer can get correct errors in the program.
     */
    @Test
    public void testErrorInFunctionCall() {
        int inputNo = 1;
        ErrorLocalizer errorLocalizer = new ErrorLocalizerImpl();
        ErrorLocalisation errorLocations = errorLocalizer.localizeErrors(getSubmittedProgram(),
                getReferenceProgram(),
                getModelInputs().subList(inputNo - 1, inputNo),
                getStructuralMapping(),
                getVariableMapping(),
                getInterpreter());

        List<ErrorLocation> errorsMain = errorLocations
                .getErrorLocations(Constants.DEFAULT_ENTRY_FUNCTION_NAME,
                        getVariableMapping().getTopMapping(Constants.DEFAULT_ENTRY_FUNCTION_NAME));
        List<ErrorLocation> errorsAdd = errorLocations
            .getErrorLocations(FUNC_ADD,
                getVariableMapping().getTopMapping(FUNC_ADD));

        assertNotNull(errorsMain);
        assertFalse(errorsMain.isEmpty());
        assertTrue(errorsMain.size() == 1);

        assertEquals(1, errorsMain.get(0).getLocationInReference());
        assertEquals(1, errorsMain.get(0).getLocationInSubmission());

        List<Variable> erroneousMainVariables = errorsMain.get(0).getErroneousVariablesInSubmission();
        assertFalse(erroneousMainVariables.isEmpty());
        assertTrue(erroneousMainVariables.size() == 1);
        assertEquals(VAR_OUT, erroneousMainVariables.get(0).getName());

        assertNotNull(errorsAdd);
        assertFalse(errorsAdd.isEmpty());
        assertTrue(errorsAdd.size() == 1);

        assertEquals(1, errorsAdd.get(0).getLocationInReference());
        assertEquals(1, errorsAdd.get(0).getLocationInSubmission());

        List<Variable> erroneousAddVariables = errorsAdd.get(0).getErroneousVariablesInSubmission();
        assertFalse(erroneousAddVariables.isEmpty());
        assertTrue(erroneousAddVariables.size() == 1);
        assertEquals(VAR_RET, erroneousAddVariables.get(0).getName());
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

        List<ErrorLocation> errorsMain = errorLocations
                .getErrorLocations(Constants.DEFAULT_ENTRY_FUNCTION_NAME,
                        getVariableMapping().getTopMapping(Constants.DEFAULT_ENTRY_FUNCTION_NAME));

        List<ErrorLocation> errorsAdd = errorLocations
            .getErrorLocations(FUNC_ADD,
                getVariableMapping().getTopMapping(FUNC_ADD));

        assertNotNull(errorsMain);
        assertNotNull(errorsAdd);
        assertTrue(errorsMain.size() == 4);
        assertTrue(errorsAdd.size() == 4);
    }
}
