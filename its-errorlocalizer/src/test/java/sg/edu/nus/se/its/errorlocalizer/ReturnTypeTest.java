package sg.edu.nus.se.its.errorlocalizer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static sg.edu.nus.se.its.util.constants.Constants.VAR_OUT;
import static sg.edu.nus.se.its.util.constants.Constants.VAR_RET;

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

public class ReturnTypeTest {
    public static final String FUNC_ADD = "add";

    private StructuralMapping getStructuralMapping() {
        Map<Integer, Integer> mapping = new HashMap<>();
        mapping.put(1, 1);

        StructuralMapping structuralAlignmentResult = new StructuralMapping();
        structuralAlignmentResult.put(Constants.DEFAULT_ENTRY_FUNCTION_NAME, mapping);
        structuralAlignmentResult.put(FUNC_ADD, mapping);
        return structuralAlignmentResult;
    }

    private VariableMapping getVariableMapping() {
        Map<Variable, Variable> varMapping = new HashMap<>();
        varMapping.put(new Variable(Constants.VAR_COND), new Variable(Constants.VAR_COND));
        varMapping.put(new Variable(Constants.VAR_RET), new Variable(Constants.VAR_RET));
        varMapping.put(new Variable(VAR_OUT), new Variable(VAR_OUT));
        varMapping.put(new Variable(Constants.VAR_IN), new Variable(Constants.VAR_IN));
        varMapping.put(new Variable(FUNC_ADD), new Variable(FUNC_ADD));
        varMapping.put(new Variable(Constants.DEFAULT_ENTRY_FUNCTION_NAME),
            new Variable(Constants.DEFAULT_ENTRY_FUNCTION_NAME));
        varMapping.put(new Variable("i"), new Variable("i"));

        Map<Variable, Variable> varMappingForAdd = new HashMap<>();
        varMappingForAdd.put(new Variable(Constants.VAR_COND), new Variable(Constants.VAR_COND));
        varMappingForAdd.put(new Variable(VAR_RET), new Variable(VAR_RET));
        varMappingForAdd.put(new Variable(Constants.VAR_OUT), new Variable(Constants.VAR_OUT));
        varMappingForAdd.put(new Variable(Constants.VAR_IN), new Variable(Constants.VAR_IN));
        varMapping.put(new Variable(Constants.DEFAULT_ENTRY_FUNCTION_NAME),
            new Variable(Constants.DEFAULT_ENTRY_FUNCTION_NAME));
        varMappingForAdd.put(new Variable("x"), new Variable("x"));
        varMappingForAdd.put(new Variable("i"), new Variable("i"));

        VariableMapping variableAlignmentResult = new VariableMapping();
        variableAlignmentResult.add(Constants.DEFAULT_ENTRY_FUNCTION_NAME, varMapping);
        variableAlignmentResult.add(FUNC_ADD, varMappingForAdd);
        return variableAlignmentResult;
    }

    private Interpreter getInterpreter() {
        Interpreter interpreter =
                new InterpreterServiceImpl("c", Constants.DEFAULT_ENTRY_FUNCTION_NAME);
        return interpreter;
    }

    private Program getReferenceProgram() {
        Program referenceProgram = ModelProgram.MODEL_RETURN_TYPE_TEST_C_C.get();
        return referenceProgram;
    }

    private Program getSubmittedProgram() {
        Program submittedProgram = ModelProgram.MODEL_RETURN_TYPE_TEST_B_C.get();
        return submittedProgram;
    }

    private List<Input> getModelInputs() {
        return Collections.emptyList();
    }

    /**
     * This test is to test errors in argument type
     */
    @Test
    public void testReturnTypeMain() {
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

        assertNotNull(errorsMain);
        assertTrue(errorsMain.size() == 2);

        assertEquals(1, errorsMain.get(0).getLocationInReference());
        assertEquals(1, errorsMain.get(0).getLocationInSubmission());

        assertEquals(1, errorsMain.get(1).getLocationInReference());
        assertEquals(1, errorsMain.get(1).getLocationInSubmission());

        List<Variable> firstErroneousVariables = errorsMain.get(0).getErroneousVariablesInSubmission();
        List<Variable> secondErroneousVariables = errorsMain.get(1).getErroneousVariablesInSubmission();
        assertFalse(firstErroneousVariables.isEmpty());
        assertTrue(firstErroneousVariables.size() == 1);
        assertEquals("i", firstErroneousVariables.get(0).getName());

        // type of var i is different, may cause an unexpected overloaded function called.
        assertFalse(secondErroneousVariables.isEmpty());
        assertTrue(secondErroneousVariables.size() == 1);
        assertEquals(VAR_RET, secondErroneousVariables.get(0).getName());
    }

    /**
     * This test is to test errors in argument type.
     */
    @Test
    public void testReturnTypeAdd() {
        ErrorLocalizer errorLocalizer = new ErrorLocalizerImpl();
        ErrorLocalisation errorLocations = errorLocalizer.localizeErrors(getSubmittedProgram(),
                getReferenceProgram(),
                getModelInputs(),
                getStructuralMapping(),
                getVariableMapping(),
                getInterpreter());

        List<ErrorLocation> errorsAdd = errorLocations
                .getErrorLocations(FUNC_ADD,
                        getVariableMapping().getTopMapping(FUNC_ADD));
        assertNotNull(errorsAdd);
        assertTrue(errorsAdd.size() == 2);

        assertEquals(1, errorsAdd.get(0).getLocationInReference());
        assertEquals(1, errorsAdd.get(0).getLocationInSubmission());

        assertEquals(1, errorsAdd.get(1).getLocationInReference());
        assertEquals(1, errorsAdd.get(1).getLocationInSubmission());

        ErrorLocation.ErrorType firstErrorType = errorsAdd.get(0).getErrorType();
        ErrorLocation.ErrorType secondErrorType = errorsAdd.get(1).getErrorType();

        assertEquals(ErrorLocation.ErrorType.UnmatchedException, firstErrorType);
        assertEquals(ErrorLocation.ErrorType.UnmatchedException, secondErrorType);
    }
}
