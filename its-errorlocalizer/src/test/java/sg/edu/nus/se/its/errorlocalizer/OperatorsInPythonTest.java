package sg.edu.nus.se.its.errorlocalizer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static sg.edu.nus.se.its.util.constants.Constants.VAR_COND;
import static sg.edu.nus.se.its.util.constants.Constants.VAR_OUT;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import sg.edu.nus.se.its.alignment.StructuralMapping;
import sg.edu.nus.se.its.alignment.VariableMapping;
import sg.edu.nus.se.its.errorlocalizer.utils.ModelProgram;
import sg.edu.nus.se.its.interpreter.Interpreter;
import sg.edu.nus.se.its.interpreter.InterpreterServiceImpl;
import sg.edu.nus.se.its.model.Program;
import sg.edu.nus.se.its.model.Variable;
import sg.edu.nus.se.its.util.constants.Constants;

/**
 * This test is to test errors in operators in python.
 */
public class OperatorsInPythonTest {
    private static List<ErrorLocation> errorLocations;
    private static String FUNC_FOO = "foo";
    private static String FUNC_IS_MEMBER = "is_member";
    public final static int firstLoc = 1;


    private static StructuralMapping getStructuralMapping() {
        Map<Integer, Integer> mapping = new HashMap<>();
        mapping.put(1, 1);

        Map<Integer, Integer> mappingForIsMember = new HashMap<>();
        mappingForIsMember.put(1, 1);
        mappingForIsMember.put(2, 2);
        mappingForIsMember.put(3, 3);
        mappingForIsMember.put(4, 4);

        StructuralMapping structuralAlignmentResult = new StructuralMapping();
        structuralAlignmentResult.put(Constants.DEFAULT_ENTRY_FUNCTION_NAME, mapping);
        structuralAlignmentResult.put(FUNC_FOO, mapping);
        structuralAlignmentResult.put(FUNC_IS_MEMBER, mappingForIsMember);
        return structuralAlignmentResult;
    }

    private static VariableMapping getVariableMapping() {
        Map<Variable, Variable> varMapping = new HashMap<>();
        varMapping.put(new Variable(VAR_COND), new Variable(VAR_COND));
        varMapping.put(new Variable(Constants.VAR_RET), new Variable(Constants.VAR_RET));
        varMapping.put(new Variable(VAR_OUT), new Variable(VAR_OUT));
        varMapping.put(new Variable(Constants.VAR_IN), new Variable(Constants.VAR_IN));
        varMapping.put(new Variable(FUNC_FOO), new Variable(FUNC_FOO));
        varMapping.put(new Variable(FUNC_IS_MEMBER), new Variable(FUNC_IS_MEMBER));
        varMapping.put(new Variable("a"), new Variable("a"));
        varMapping.put(new Variable("b"), new Variable("b"));
        varMapping.put(new Variable("c"), new Variable("c"));
        varMapping.put(new Variable("d"), new Variable("d"));
        varMapping.put(new Variable("e"), new Variable("e"));
        varMapping.put(new Variable("t"), new Variable("t"));
        varMapping.put(new Variable("f"), new Variable("f"));
        varMapping.put(new Variable("g"), new Variable("g"));
        varMapping.put(new Variable("i11"), new Variable("i11"));
        varMapping.put(new Variable("i21"), new Variable("i21"));
        varMapping.put(new Variable("i31"), new Variable("i31"));
        varMapping.put(new Variable("i41"), new Variable("i41"));
        varMapping.put(new Variable("i51"), new Variable("i51"));
        varMapping.put(new Variable("i12"), new Variable("i12"));
        varMapping.put(new Variable("i22"), new Variable("i22"));
        varMapping.put(new Variable("i32"), new Variable("i32"));
        varMapping.put(new Variable("i42"), new Variable("i42"));
        varMapping.put(new Variable("i52"), new Variable("i52"));
        varMapping.put(new Variable("i13"), new Variable("i13"));
        varMapping.put(new Variable("i23"), new Variable("i23"));
        varMapping.put(new Variable("i33"), new Variable("i33"));
        varMapping.put(new Variable("i43"), new Variable("i43"));
        varMapping.put(new Variable("i53"), new Variable("i53"));
        varMapping.put(new Variable("d11"), new Variable("d11"));
        varMapping.put(new Variable("d12"), new Variable("d12"));

        varMapping.put(new Variable("b11"), new Variable("b11"));
        varMapping.put(new Variable("b21"), new Variable("b21"));
        varMapping.put(new Variable("b31"), new Variable("b31"));
        varMapping.put(new Variable("b41"), new Variable("b41"));
        varMapping.put(new Variable("b51"), new Variable("b51"));
        varMapping.put(new Variable("b61"), new Variable("b61"));
        varMapping.put(new Variable("b71"), new Variable("b71"));
        varMapping.put(new Variable("b81"), new Variable("b81"));

        varMapping.put(new Variable("b12"), new Variable("b12"));
        varMapping.put(new Variable("b22"), new Variable("b22"));
        varMapping.put(new Variable("b32"), new Variable("b32"));
        varMapping.put(new Variable("b42"), new Variable("b42"));
        varMapping.put(new Variable("b52"), new Variable("b52"));
        varMapping.put(new Variable("b62"), new Variable("b62"));
        varMapping.put(new Variable("b72"), new Variable("b72"));
        varMapping.put(new Variable("b82"), new Variable("b82"));

        varMapping.put(new Variable("b13"), new Variable("b13"));
        varMapping.put(new Variable("b23"), new Variable("b23"));
        varMapping.put(new Variable("b33"), new Variable("b33"));
        varMapping.put(new Variable("b43"), new Variable("b43"));
        varMapping.put(new Variable("b53"), new Variable("b53"));
        varMapping.put(new Variable("b63"), new Variable("b63"));
        varMapping.put(new Variable("b73"), new Variable("b73"));
        varMapping.put(new Variable("b83"), new Variable("b83"));

        varMapping.put(new Variable("op11"), new Variable("op11"));
        varMapping.put(new Variable("op12"), new Variable("op12"));
        varMapping.put(new Variable("op13"), new Variable("op13"));

        varMapping.put(new Variable("op21"), new Variable("op21"));
        varMapping.put(new Variable("op22"), new Variable("op22"));
        varMapping.put(new Variable("op23"), new Variable("op23"));
        varMapping.put(new Variable("op24"), new Variable("op24"));

        varMapping.put(new Variable("l11"), new Variable("l11"));
        varMapping.put(new Variable("l12"), new Variable("l12"));

        varMapping.put(new Variable("list"), new Variable("list"));
        varMapping.put(new Variable("i55"), new Variable("i55"));
        varMapping.put(new Variable("i56"), new Variable("i56"));
        varMapping.put(new Variable("i57"), new Variable("i57"));
        varMapping.put(new Variable("i58"), new Variable("i58"));

        varMapping.put(new Variable("r11"), new Variable("r11"));
        varMapping.put(new Variable("r12"), new Variable("r12"));
        varMapping.put(new Variable("r21"), new Variable("r21"));
        varMapping.put(new Variable("r22"), new Variable("r22"));

        varMapping.put(new Variable("bit11"), new Variable("bit11"));
        varMapping.put(new Variable("bit12"), new Variable("bit12"));
        varMapping.put(new Variable("bit13"), new Variable("bit13"));
        varMapping.put(new Variable("bit21"), new Variable("bit21"));
        varMapping.put(new Variable("bit22"), new Variable("bit22"));
        varMapping.put(new Variable("bit23"), new Variable("bit23"));

        varMapping.put(new Variable("a11"), new Variable("a11"));
        varMapping.put(new Variable("a12"), new Variable("a12"));
        varMapping.put(new Variable("a21"), new Variable("a21"));
        varMapping.put(new Variable("a22"), new Variable("a22"));

        VariableMapping variableAlignmentResult = new VariableMapping();
        variableAlignmentResult.add(Constants.DEFAULT_ENTRY_FUNCTION_NAME, varMapping);
        variableAlignmentResult.add(FUNC_FOO, varMapping);
        variableAlignmentResult.add(FUNC_IS_MEMBER, varMapping);
        return variableAlignmentResult;
    }

    private static Interpreter getInterpreter() {
        Interpreter interpreter =
                new InterpreterServiceImpl("py", Constants.DEFAULT_ENTRY_FUNCTION_NAME);
        return interpreter;
    }

    private static Program getReferenceProgram() {
        Program referenceProgram = ModelProgram.MODEL_OPERATORS_IN_PYTHON_TEST_C.get();
        return referenceProgram;
    }

    private static Program getSubmittedProgram() {
        Program submittedProgram = ModelProgram.MODEL_OPERATORS_IN_PYTHON_TEST_B.get();
        return submittedProgram;
    }

    @BeforeAll
    public static void pythonOperatorsTestPrepare() {
        ErrorLocalizer errorLocalizer = new ErrorLocalizerImpl();
        ErrorLocalisation errorLocations1 = errorLocalizer.localizeErrors(getSubmittedProgram(),
                getReferenceProgram(),
                null,
                getStructuralMapping(),
                getVariableMapping(),
                getInterpreter());

        errorLocations = errorLocations1
                .getErrorLocations(Constants.DEFAULT_ENTRY_FUNCTION_NAME,
                        getVariableMapping().getTopMapping(Constants.DEFAULT_ENTRY_FUNCTION_NAME));

        assertNotNull(errorLocations);
        assertFalse(errorLocations.isEmpty());
    }

    @BeforeAll
    public static void pythonOperatorsTestReflective() {
        ErrorLocalizer errorLocalizer = new ErrorLocalizerImpl();
        ErrorLocalisation errorLocations = errorLocalizer.localizeErrors(getReferenceProgram(),
                getReferenceProgram(),
                null,
                getStructuralMapping(),
                getVariableMapping(),
                getInterpreter());

        List<ErrorLocation> errorLocations1 = errorLocations
                .getErrorLocations(Constants.DEFAULT_ENTRY_FUNCTION_NAME,
                        getVariableMapping().getTopMapping(Constants.DEFAULT_ENTRY_FUNCTION_NAME));

        assertNotNull(errorLocations1);
        assertTrue(errorLocations1.isEmpty());

        errorLocations = errorLocalizer.localizeErrors(getSubmittedProgram(),
                getSubmittedProgram(),
                null,
                getStructuralMapping(),
                getVariableMapping(),
                getInterpreter());

        errorLocations1 = errorLocations
                .getErrorLocations(Constants.DEFAULT_ENTRY_FUNCTION_NAME,
                        getVariableMapping().getTopMapping(Constants.DEFAULT_ENTRY_FUNCTION_NAME));

        assertNotNull(errorLocations1);
        assertTrue(errorLocations1.isEmpty());
    }

    /**
     * Assert the equivalence checks on arithmetic operations are correct.
     */
    @Test
    public void testPythonOperatorsArithmaticOperation() {
        // Ensure the variable used in correct.
        assertVarCorrect("a");
        assertVarCorrect("b");
        assertVarCorrect("c");

        assertVarCorrect("i11");
        assertVarCorrect("i21");
        assertVarCorrect("i31");
        assertVarCorrect("i41");
        assertVarCorrect("i51");

        assertVarCorrect("i12");
        assertVarError("i22");
        assertVarCorrect("i32");
        assertVarError("i42");
        assertVarError("i52");

        assertVarError("i13");
        assertVarError("i23");
        assertVarError("i33");
        assertVarError("i43");
        assertVarError("i53");
    }


    /**
     * Assert the variable types are matched equivalence checks on arithmetic operations are correct.
     */
    @Test
    public void testPythonOperatorsDeclaredVariableTypes() {
        // Ensure the variable used in correct.
        assertVarCorrect("d1");
        assertVarCorrect("e1");

        assertVarCorrect("d11");
        assertVarError("d12");
    }

    /**
     * Assert the equivalence checks on boolean operations are correct.
     */
    @Test
    public void testPythonOperatorsBooleanOperation() {
        assertVarCorrect("f");
        assertVarCorrect("g");
        assertVarCorrect("h");

        assertVarCorrect("b11");
        assertVarCorrect("b21");
        assertVarCorrect("b31");
        assertVarCorrect("b41");
        assertVarCorrect("b51");
        assertVarCorrect("b61");
        assertVarCorrect("b71");
        assertVarCorrect("b81");

        assertVarCorrect("b12");
        assertVarCorrect("b22");
        assertVarCorrect("b32");
        assertVarCorrect("b42");
        assertVarCorrect("b52");
        assertVarCorrect("b62");
        assertVarCorrect("b72");
        assertVarCorrect("b82");

        assertVarError("b13");
        assertVarError("b23");
        assertVarError("b33");
        assertVarError("b43");
        assertVarError("b53");
        assertVarError("b63");
        assertVarError("b73");
        assertVarError("b83");
    }

    /**
     * Assert the equivalence checks on argument order sensitive operations are correct.
     */
    @Test
    public void testPythonOperatorsArrayInitialization() {
        assertVarCorrect("op11"); // Same expression
        assertVarError("op12"); // elements different order
        assertVarError("op13"); // different array size
    }

    /**
     * Assert the equivalence checks on function call are correct.
     */
    @Test
    public void testPythonOperatorsFunctionCall() {
        assertVarCorrect("op21"); // Same expression
        assertVarError("op22"); // two arguments different positions
        assertVarError("op23"); // three arguments different positions
        assertVarError("op24"); // different arguments
    }

    /**
     * Assert the equivalence checks on shift operators are correct.
     */
    @Test
    public void testPythonOperatorsShift() {
        assertVarCorrect("r11");
        assertVarCorrect("r12");
        assertVarError("r21");
        assertVarError("r22");
    }

    /**
     * Assert the equivalence checks on insert function are correct.
     */
    @Test
    public void testPythonOperatorsInsert() {
        assertVarCorrect("l11");
        assertVarError("l12");
    }

    /**
     * Assert the equivalence checks on append function are correct.
     */
    @Test
    public void testPythonOperatorsIsMember() {
        assertVarCorrect("i55");
        assertVarCorrect("i56");
        assertVarError("i57");
        assertVarError("i58");
    }

    /**
     * Assert the equivalence checks on operators on bits are correct.
     */
    @Test
    public void testPythonOperatorsBitOperation() {
        assertVarCorrect("bit11");
        assertVarCorrect("bit12");
        assertVarCorrect("bit13");
        assertVarError("bit21");
        assertVarError("bit22");
        assertVarError("bit23");
    }

    /**
     * Assert the equivalence checks on "is" and "isNot" operators are correct.
     */
    @Test
    public void testPythonOperatorsIsOperation() {
        assertVarError("a11");
        assertVarError("a12");
        assertVarCorrect("a21");
        assertVarCorrect("a22");
    }

    private void assertVarCorrect(String varName) {
        assertFalse(this.errorLocations.stream().anyMatch(
                el -> el.getErroneousVariablesInSubmission().stream()
                        .map(v -> v.getUnprimedName()).anyMatch(s -> s.equals(varName))));
    }

    private void assertVarError(String varName) {
        ErrorLocation el = this.errorLocations.stream()
                .filter(e -> e.getErroneousVariablesInSubmission().stream()
                        .map(v -> v.getUnprimedName()).anyMatch(s -> s.equals(varName)))
                .findAny()
                .orElse(null);

        assertNotNull(el);
        assertEquals(el.getErrorType(), ErrorLocation.ErrorType.VariableValueMismatch);
        assertEquals(el.getLocationInReference(), firstLoc);
        assertEquals(el.getLocationInSubmission(), firstLoc);
        assertNull(el.getDueToUnmatchedException());
    }
}
