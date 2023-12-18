package sg.edu.nus.se.its.errorlocalizer;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static sg.edu.nus.se.its.util.constants.Constants.VAR_COND;
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

/**
 * Test if the error localizer can detect break and continue.
 */
public class BreakContinueTest {
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
        varMapping.put(new Variable("i"), new Variable("i"));

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
        Program referenceProgram = ModelProgram.MODEL_BREAK_CONTINUE_TEST_C_C.get();
        return referenceProgram;
    }

    private Program getSubmittedProgram() {
        Program submittedProgram = ModelProgram.MODEL_BREAK_CONTINUE_TEST_B_C.get();
        return submittedProgram;
    }

    private List<Input> getModelInputs() {
        return Collections.emptyList();
    }

    /**
     * Test if the error localizer can detect break and continue.
     */
    @Test
    public void breakContinueTest() {
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
        assertTrue(errors.size() == 2);

        assertEquals(3, errors.get(0).getLocationInReference());
        assertEquals(3, errors.get(0).getLocationInSubmission());
        assertEquals(6, errors.get(1).getLocationInReference());
        assertEquals(6, errors.get(1).getLocationInSubmission());

        List<Variable> erroneousVariables = errors.get(0).getErroneousVariablesInSubmission();
        assertNotNull(erroneousVariables);
        assertFalse(erroneousVariables.isEmpty());
        assertTrue(erroneousVariables.size() == 1);
        assertEquals(VAR_RET, erroneousVariables.get(0).getName());
    }
}
