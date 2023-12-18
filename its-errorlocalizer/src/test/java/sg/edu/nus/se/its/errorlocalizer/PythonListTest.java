package sg.edu.nus.se.its.errorlocalizer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static sg.edu.nus.se.its.util.constants.Constants.VAR_OUT;

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
 * This test is to test errors in list objects in python.
 */
public class PythonListTest {
    public static final String FUNC_NAME = "sort_list";

    private StructuralMapping getStructuralMapping() {
        Map<Integer, Integer> mapping = new HashMap<>();
        mapping.put(1, 1);

        StructuralMapping structuralAlignmentResult = new StructuralMapping();
        structuralAlignmentResult.put(FUNC_NAME, mapping);
        return structuralAlignmentResult;
    }

    private VariableMapping getVariableMapping() {
        Map<Variable, Variable> varMapping = new HashMap<>();
        varMapping.put(new Variable(Constants.VAR_COND), new Variable(Constants.VAR_COND));
        varMapping.put(new Variable(Constants.VAR_RET), new Variable(Constants.VAR_RET));
        varMapping.put(new Variable(VAR_OUT), new Variable(VAR_OUT));
        varMapping.put(new Variable(Constants.VAR_IN), new Variable(Constants.VAR_IN));
        varMapping.put(new Variable("list"), new Variable("list"));

        VariableMapping variableAlignmentResult = new VariableMapping();
        variableAlignmentResult.add(FUNC_NAME, varMapping);
        return variableAlignmentResult;
    }

    private Interpreter getInterpreter() {
        Interpreter interpreter =
                new InterpreterServiceImpl("py", FUNC_NAME);
        return interpreter;
    }

    private Program getReferenceProgram() {
        Program referenceProgram = ModelProgram.MODEL_PYTHON_LIST_TEST_C.get();
        return referenceProgram;
    }

    private Program getSubmittedProgram() {
        Program submittedProgram = ModelProgram.MODEL_PYTHON_LIST_TEST_B.get();
        return submittedProgram;
    }

    private List<Input> getModelInputs() {
        return Collections.emptyList();
    }

    /**
     * This test is to test errors in list objects in python.
     */
    @Test
    public void testPythonList() {
        ErrorLocalizer errorLocalizer = new ErrorLocalizerImpl();
        StructuralMapping structuralMapping = getStructuralMapping();
        VariableMapping variableMapping = getVariableMapping();
        ErrorLocalisation errorLocations = errorLocalizer.localizeErrors(getSubmittedProgram(),
                getReferenceProgram(),
                getModelInputs(),
                structuralMapping,
                variableMapping,
                getInterpreter());

        List<ErrorLocation> errors = errorLocations
                .getErrorLocations(FUNC_NAME,
                        getVariableMapping().getTopMapping(FUNC_NAME));

        assertNotNull(errors);
        assertTrue(errors.size() == 1);

        assertEquals(1, errors.get(0).getLocationInReference());
        assertEquals(1, errors.get(0).getLocationInSubmission());

        List<Variable> erroneousVariables = errors.get(0).getErroneousVariablesInSubmission();
        assertNotNull(erroneousVariables);
        assertEquals(1, erroneousVariables.size());
        assertEquals("list", erroneousVariables.get(0).getName());
    }
}
