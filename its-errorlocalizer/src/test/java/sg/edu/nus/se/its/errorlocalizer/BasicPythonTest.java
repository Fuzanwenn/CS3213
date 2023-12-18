package sg.edu.nus.se.its.errorlocalizer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
 * Basic test for python program.
 */
public class BasicPythonTest {
    public static final String FUNC_IS_ODD = "is_odd";

    private StructuralMapping getStructuralMapping() {
        Map<Integer, Integer> mapping = new HashMap<>();
        mapping.put(1, 1);

        StructuralMapping structuralAlignmentResult = new StructuralMapping();
        structuralAlignmentResult.put(FUNC_IS_ODD, mapping);
        return structuralAlignmentResult;
    }

    private VariableMapping getVariableMapping() {
        Map<Variable, Variable> varMapping = new HashMap<>();
        varMapping.put(new Variable(Constants.VAR_COND), new Variable(Constants.VAR_COND));
        varMapping.put(new Variable(Constants.VAR_RET), new Variable(Constants.VAR_RET));
        varMapping.put(new Variable(VAR_OUT), new Variable(VAR_OUT));
        varMapping.put(new Variable(Constants.VAR_IN), new Variable(Constants.VAR_IN));
        varMapping.put(new Variable("x"), new Variable("x"));

        VariableMapping variableAlignmentResult = new VariableMapping();
        variableAlignmentResult.add(FUNC_IS_ODD, varMapping);
        return variableAlignmentResult;
    }

    private Interpreter getInterpreter() {
        Interpreter interpreter =
                new InterpreterServiceImpl("py", FUNC_IS_ODD);
        return interpreter;
    }

    private Program getReferenceProgram() {
        Program referenceProgram = ModelProgram.MODEL_BASIC_PYTHON_TEST_C.get();
        return referenceProgram;
    }

    private Program getSubmittedProgram() {
        Program submittedProgram = ModelProgram.MODEL_BASIC_PYTHON_TEST_B.get();
        return submittedProgram;
    }

    private List<Input> getModelInputs() {
        List<Input> inputs = ModelInput.MODEL_INPUT_BASIC_PYTHON_TEST;
        return inputs;
    }

    /**
     * Test for basic python program.
     */
    @Test
    public void basicPythonTest() {
        int inputNo = 1;
        ErrorLocalizer errorLocalizer = new ErrorLocalizerImpl();
        StructuralMapping structuralMapping = getStructuralMapping();
        VariableMapping variableMapping = getVariableMapping();
        ErrorLocalisation errorLocations = errorLocalizer.localizeErrors(getSubmittedProgram(),
                getReferenceProgram(),
                getModelInputs().subList(inputNo - 1, inputNo),
                structuralMapping,
                variableMapping,
                getInterpreter());

        List<ErrorLocation> errors = errorLocations
                .getErrorLocations(FUNC_IS_ODD,
                        getVariableMapping().getTopMapping(FUNC_IS_ODD));

        assertNotNull(errors);
        assertEquals(1, errors.size());

        assertEquals(1, errors.get(0).getLocationInReference());
        assertEquals(1, errors.get(0).getLocationInSubmission());

        List<Variable> erroneousVariables = errors.get(0).getErroneousVariablesInSubmission();
        assertNotNull(erroneousVariables);
        assertEquals(1, erroneousVariables.size());
        assertEquals(Constants.VAR_RET, erroneousVariables.get(0).getName());
    }
}
