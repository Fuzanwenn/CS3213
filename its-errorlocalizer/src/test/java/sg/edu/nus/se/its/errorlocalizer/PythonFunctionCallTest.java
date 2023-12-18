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

/**
 * This test is to test if error localizer can detect the error in the function call in python program.
 */
public class PythonFunctionCallTest {
    public static final String FUNC_ECHO_1 = "echo";
    public static final String FUNC_ECHO_2 = "echoecho";
    public static final String FUNC_ECHO_3 = "echoechoecho";
    public static final String FUNC_ECHO_4 = "echoechoechoecho";


    private StructuralMapping getStructuralMapping() {
        Map<Integer, Integer> mapping = new HashMap<>();
        mapping.put(1, 1);

        StructuralMapping structuralAlignmentResult = new StructuralMapping();
        structuralAlignmentResult.put(Constants.DEFAULT_ENTRY_FUNCTION_NAME, mapping);
        structuralAlignmentResult.put(FUNC_ECHO_1, mapping);
        structuralAlignmentResult.put(FUNC_ECHO_2, mapping);
        structuralAlignmentResult.put(FUNC_ECHO_3, mapping);
        structuralAlignmentResult.put(FUNC_ECHO_4, mapping);
        return structuralAlignmentResult;
    }

    private VariableMapping getVariableMapping() {
        Map<Variable, Variable> varMapping = new HashMap<>();
        varMapping.put(new Variable(Constants.VAR_COND), new Variable(Constants.VAR_COND));
        varMapping.put(new Variable(VAR_RET), new Variable(VAR_RET));
        varMapping.put(new Variable(Constants.VAR_OUT), new Variable(Constants.VAR_OUT));
        varMapping.put(new Variable(Constants.VAR_IN), new Variable(Constants.VAR_IN));
        varMapping.put(new Variable(Constants.DEFAULT_ENTRY_FUNCTION_NAME),
                new Variable(Constants.DEFAULT_ENTRY_FUNCTION_NAME));
        varMapping.put(new Variable(FUNC_ECHO_1), new Variable(FUNC_ECHO_1));
        varMapping.put(new Variable(FUNC_ECHO_2), new Variable(FUNC_ECHO_2));
        varMapping.put(new Variable(FUNC_ECHO_3), new Variable(FUNC_ECHO_3));
        varMapping.put(new Variable(FUNC_ECHO_4), new Variable(FUNC_ECHO_4));


        VariableMapping variableAlignmentResult = new VariableMapping();
        variableAlignmentResult.add(Constants.DEFAULT_ENTRY_FUNCTION_NAME, varMapping);
        variableAlignmentResult.add(FUNC_ECHO_1, varMapping);
        variableAlignmentResult.add(FUNC_ECHO_2, varMapping);
        variableAlignmentResult.add(FUNC_ECHO_3, varMapping);
        variableAlignmentResult.add(FUNC_ECHO_4, varMapping);
        return variableAlignmentResult;
    }

    private Interpreter getInterpreter() {
        Interpreter interpreter =
                new InterpreterServiceImpl("py", Constants.DEFAULT_ENTRY_FUNCTION_NAME);
        return interpreter;
    }

    private Program getReferenceProgram() {
        Program referenceProgram = ModelProgram.MODEL_PYTHON_FUNCTION_CALL_TEST_C.get();
        return referenceProgram;
    }

    private Program getSubmittedProgram() {
        Program submittedProgram = ModelProgram.MODEL_PYTHON_FUNCTION_CALL_TEST_B.get();
        return submittedProgram;
    }

    private List<Input> getModelInputs() {
        return Collections.emptyList();
    }

    /**
     * Test if error localizer can detect the error in the function call in python program.
     */
    @Test
    public void testPythonFunctionCall() {
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
        List<ErrorLocation> errorsEcho1 = errorLocations
                .getErrorLocations(FUNC_ECHO_1,
                        getVariableMapping().getTopMapping(FUNC_ECHO_1));
        List<ErrorLocation> errorsEcho2 = errorLocations
                .getErrorLocations(FUNC_ECHO_2,
                        getVariableMapping().getTopMapping(FUNC_ECHO_2));
        List<ErrorLocation> errorsEcho3 = errorLocations
                .getErrorLocations(FUNC_ECHO_3,
                        getVariableMapping().getTopMapping(FUNC_ECHO_3));
        List<ErrorLocation> errorsEcho4 = errorLocations
                .getErrorLocations(FUNC_ECHO_4,
                        getVariableMapping().getTopMapping(FUNC_ECHO_4));

        /**
         * def main()
         * Submission: printf("%d", 2 <= echoecho());
         * Reference: printf("%d", echo() >= 2);
         */
        assertNotNull(errorsMain);
        assertFalse(errorsMain.isEmpty());
        assertTrue(errorsMain.size() == 1);

        assertEquals(1, errorsMain.get(0).getLocationInReference());
        assertEquals(1, errorsMain.get(0).getLocationInSubmission());

        List<Variable> erroneousMainVariables = errorsMain.get(0).getErroneousVariablesInSubmission();
        assertFalse(erroneousMainVariables.isEmpty());
        assertTrue(erroneousMainVariables.size() == 1);
        assertEquals(VAR_OUT, erroneousMainVariables.get(0).getName());

        /**
         * def echo()
         * Submission: return echoechoecho();
         * Reference: return echoecho();
         */
        assertNotNull(errorsEcho1);
        assertFalse(errorsEcho1.isEmpty());
        assertTrue(errorsEcho1.size() == 1);

        assertEquals(1, errorsEcho1.get(0).getLocationInReference());
        assertEquals(1, errorsEcho1.get(0).getLocationInSubmission());

        List<Variable> erroneousEcho1Variables = errorsEcho1.get(0).getErroneousVariablesInSubmission();
        assertFalse(erroneousEcho1Variables.isEmpty());
        assertTrue(erroneousEcho1Variables.size() == 1);
        assertEquals(VAR_RET, erroneousEcho1Variables.get(0).getName());

        /**
         * def echoecho()
         * Submission: return echo();
         * Reference: return echoechoecho();
         */
        assertNotNull(errorsEcho2);
        assertFalse(errorsEcho2.isEmpty());
        assertTrue(errorsEcho2.size() == 1);

        assertEquals(1, errorsEcho2.get(0).getLocationInReference());
        assertEquals(1, errorsEcho2.get(0).getLocationInSubmission());

        List<Variable> erroneousEcho2Variables = errorsEcho2.get(0).getErroneousVariablesInSubmission();
        assertFalse(erroneousEcho2Variables.isEmpty());
        assertTrue(erroneousEcho2Variables.size() == 1);
        assertEquals(VAR_RET, erroneousEcho2Variables.get(0).getName());

        /**
         * def echoechoecho()
         * Submission: return echoechoechoecho();
         * Reference: return echoechoechoecho();
         */
        assertNotNull(errorsEcho3);
        assertTrue(errorsEcho3.isEmpty());

        /**
         * def echoechoechoecho()
         * Submission: return 2 == 3;
         * Reference: return 3 == 2;
         */
        assertNotNull(errorsEcho4);
        assertTrue(errorsEcho4.isEmpty());
    }
}
