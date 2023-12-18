package sg.edu.nus.se.its.errorlocalizer;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static sg.edu.nus.se.its.util.constants.Constants.VAR_COND;
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
 * Test if the error localizer can deem conditions with operands and operators in reverse order are same.
 */
public class ConditionInReverseOrderTest {

    /**
     * Get the structural mapping for the program.
     *
     * @return Structural mapping for the program.
     */
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
        mapping.put(10, 10);
        mapping.put(11, 11);

        StructuralMapping structuralAlignmentResult = new StructuralMapping();
        structuralAlignmentResult.put(Constants.DEFAULT_ENTRY_FUNCTION_NAME, mapping);
        return structuralAlignmentResult;
    }

    /**
     * Get the variable mapping for the program.
     *
     * @return Variable mapping for the program.
     */
    private VariableMapping getVariableMapping() {
        Map<Variable, Variable> varMapping = new HashMap<>();
        varMapping.put(new Variable(VAR_COND), new Variable(VAR_COND));
        varMapping.put(new Variable(Constants.VAR_RET), new Variable(Constants.VAR_RET));
        varMapping.put(new Variable(VAR_OUT), new Variable(VAR_OUT));
        varMapping.put(new Variable(Constants.VAR_IN), new Variable(Constants.VAR_IN));
        varMapping.put(new Variable("a"), new Variable("a"));
        varMapping.put(new Variable("b"), new Variable("b"));

        VariableMapping variableAlignmentResult = new VariableMapping();
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
        Program referenceProgram = ModelProgram.MODEL_CONDITION_IN_REVERSE_ORDER_TEST_C_C.get();
        return referenceProgram;
    }

    /**
     * Get the submitted program.
     *
     * @return Submitted program.
     */
    private Program getSubmittedProgram() {
        Program submittedProgram = ModelProgram.MODEL_CONDITION_IN_REVERSE_ORDER_TEST_B_C.get();
        return submittedProgram;
    }

    /**
     * Get the model inputs.
     *
     * @return Model inputs.
     */
    private List<Input> getModelInputs() {
        List<Input> inputs = ModelInput.MODEL_INPUT_CONDITION_IN_REVERSE_ORDER_TEST;
        return inputs;
    }

    /**
     * Test if the error localizer can deem conditions with operands and operators in reverse order are same.
     */
    @Test
    public void testConditionInReverseOrder() {
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
        assertTrue(errors.size() == 1);

        assertEquals(4, errors.get(0).getLocationInReference());
        assertEquals(4, errors.get(0).getLocationInSubmission());

        List<Variable> erroneousVariables = errors.get(0).getErroneousVariablesInSubmission();
        assertNotNull(erroneousVariables);
        assertFalse(erroneousVariables.isEmpty());
        assertTrue(erroneousVariables.size() == 1);
        assertEquals(VAR_COND, erroneousVariables.get(0).getName());
    }
}
