package sg.edu.nus.se.its.errorlocalizer;

import org.junit.jupiter.api.Test;
import sg.edu.nus.se.its.alignment.StructuralMapping;
import sg.edu.nus.se.its.alignment.VariableMapping;
import sg.edu.nus.se.its.errorlocalizer.utils.ModelInput;
import sg.edu.nus.se.its.errorlocalizer.utils.ModelProgram;
import sg.edu.nus.se.its.interpreter.Interpreter;
import sg.edu.nus.se.its.interpreter.InterpreterServiceImpl;
import sg.edu.nus.se.its.util.constants.Constants;
import sg.edu.nus.se.its.model.Input;
import sg.edu.nus.se.its.model.Program;
import sg.edu.nus.se.its.model.Variable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ErrorLocalizerImplTest {
  
  private StructuralMapping getCommonStructuralMapping() {
    Map<Integer, Integer> locationMapping = new HashMap<>();
    locationMapping.put(1, 1);
    StructuralMapping structuralAlignmentResult = new StructuralMapping();
    structuralAlignmentResult.put(Constants.DEFAULT_ENTRY_FUNCTION_NAME, locationMapping);
    
    return structuralAlignmentResult;
  }
  
  private Map<Variable, Variable> getCommonVariableMapping() {
    Map<Variable, Variable> varMapping = new HashMap<>(); //reference,submission
    varMapping.put(new Variable(Constants.VAR_IN), new Variable(Constants.VAR_IN));
    varMapping.put(new Variable(Constants.VAR_OUT), new Variable(Constants.VAR_OUT));
    varMapping.put(new Variable(Constants.VAR_RET), new Variable(Constants.VAR_RET));
    
    return varMapping;
  }
  
  // Run localizeErrors() on c1.c and i1.c: 
  // their json files do not record the import statements for some reason
  public List<ErrorLocation> localizeErrorsForC1I1() {
    Program referenceProgram = ModelProgram.MODEL_C1_C.get();
    Program submissionProgram = ModelProgram.MODEL_I1_C.get();
    List<Input> programInputs = new ArrayList<>();

    Map<Variable, Variable> varMapping = getCommonVariableMapping();
    varMapping.put(new Variable("a"), new Variable("x"));
    varMapping.put(new Variable("b"), new Variable("y"));
    VariableMapping variableAlignmentResult = new VariableMapping();
    variableAlignmentResult.add(Constants.DEFAULT_ENTRY_FUNCTION_NAME, varMapping);

    Interpreter interpreter =
            new InterpreterServiceImpl("c", Constants.DEFAULT_ENTRY_FUNCTION_NAME);

    ErrorLocalizer errorLocalizer = new ErrorLocalizerImpl();
    ErrorLocalisation errorLocalisation =
            errorLocalizer.localizeErrors(submissionProgram, referenceProgram, programInputs,
                    getCommonStructuralMapping(), variableAlignmentResult, interpreter);

    return errorLocalisation.getErrorLocations(Constants.DEFAULT_ENTRY_FUNCTION_NAME, varMapping);
  }

  // Run localizeErrors() on c1.c and c1.c
  public List<ErrorLocation> localizeErrorsForC1C1() {
    Program referenceProgram = ModelProgram.MODEL_C1_C.get();
    Program submissionProgram = ModelProgram.MODEL_C1_C.get();
    List<Input> programInputs = new ArrayList<>();

    Map<Variable, Variable> varMapping = getCommonVariableMapping();
    varMapping.put(new Variable("a"), new Variable("a"));
    varMapping.put(new Variable("b"), new Variable("b"));
    VariableMapping variableAlignmentResult = new VariableMapping();
    variableAlignmentResult.add(Constants.DEFAULT_ENTRY_FUNCTION_NAME, varMapping);

    Interpreter interpreter =
            new InterpreterServiceImpl("c", Constants.DEFAULT_ENTRY_FUNCTION_NAME);

    ErrorLocalizer errorLocalizer = new ErrorLocalizerImpl();
    ErrorLocalisation errorLocalisation =
            errorLocalizer.localizeErrors(submissionProgram, referenceProgram, programInputs,
                    getCommonStructuralMapping(), variableAlignmentResult, interpreter);

    return errorLocalisation.getErrorLocations(Constants.DEFAULT_ENTRY_FUNCTION_NAME, varMapping);
  }

  // Run localizeErrors() on one of the test6_b.c versions and test6_c_c.c: 
  // the fifth program input gives 1 variable mismatch error and 
  // no unmatched exception errors caused by tree root node not found
  public List<ErrorLocation> localizeErrorsForTest6(Program submissionProgram) {    
    Program referenceProgram = ModelProgram.MODEL_TEST6_C_C.get();
    int fifthProgramInputIndex = 4;
    List<Input> programInput = 
            Arrays.asList(ModelInput.MODEL_INPUT_TEST_6.get(fifthProgramInputIndex));

    Map<Variable, Variable> varMapping = getCommonVariableMapping();
    varMapping.put(new Variable("y"), new Variable("y"));
    VariableMapping variableAlignmentResult = new VariableMapping();
    variableAlignmentResult.add(Constants.DEFAULT_ENTRY_FUNCTION_NAME, varMapping);

    Interpreter interpreter =
            new InterpreterServiceImpl("c", Constants.DEFAULT_ENTRY_FUNCTION_NAME);

    ErrorLocalizer errorLocalizer = new ErrorLocalizerImpl();
    ErrorLocalisation errorLocalisation =
            errorLocalizer.localizeErrors(submissionProgram, referenceProgram, programInput,
                    getCommonStructuralMapping(), variableAlignmentResult, interpreter);
    List<ErrorLocation> errorLocations = 
            errorLocalisation.getErrorLocations(Constants.DEFAULT_ENTRY_FUNCTION_NAME, varMapping);

    return errorLocations;
  }

  @Test
  public void sameOrNoImportStatementCreatesNoUnmatchedException() {
    // same import statement: #include<stdio.h>
    List<ErrorLocation> errorLocations = 
            localizeErrorsForTest6(ModelProgram.MODEL_TEST6_B_C.get());

    assertNotNull(errorLocations);
    errorLocations.forEach(errLoc -> assertNull(errLoc.getDueToUnmatchedException()));

    // no import statements
    errorLocations = localizeErrorsForC1I1();

    assertNotNull(errorLocations);
    errorLocations.forEach(errLoc -> assertNull(errLoc.getDueToUnmatchedException()));
  }

  @Test
  public void differentImportStatementCreatesTwoUnmatchedExceptions() {
    String expectedFirstErrorString = 
            "Import Statement '#include<stdio.h>' does not appear in submitted program.";
    String expectedSecondErrorString = 
            "Import Statement '#include <stdio.h>' does not appear in reference program.";
    
    List<ErrorLocation> errorLocations = 
            localizeErrorsForTest6(ModelProgram.MODEL_TEST6_B_DIFF_IMPORT_STMT_C.get());

    assertNotNull(errorLocations);
    List<ErrorLocation> errorsDueToUnmatchedException = errorLocations.stream()
            .filter(err -> err.getErrorType() == ErrorLocation.ErrorType.UnmatchedException)
            .collect(Collectors.toList());
    assertEquals(2, errorsDueToUnmatchedException.size());
    assertTrue(errorLocations.get(0).toString().contains(expectedFirstErrorString));
    assertTrue(errorLocations.get(1).toString().contains(expectedSecondErrorString));
  }

  @Test
  public void extraImportStatementCreatesOneUnmatchedException() {
    String expectedErrorString = 
            "Import Statement '#include<math.h>' does not appear in reference program.";
    
    List<ErrorLocation> errorLocations = 
            localizeErrorsForTest6(ModelProgram.MODEL_TEST6_B_EXTRA_IMPORT_STMT_C.get());

    assertNotNull(errorLocations);
    List<ErrorLocation>  errorsDueToUnmatchedException = errorLocations.stream()
            .filter(err -> err.getErrorType() == ErrorLocation.ErrorType.UnmatchedException)
            .collect(Collectors.toList());
    assertEquals(1, errorsDueToUnmatchedException.size());
    assertTrue(errorLocations.get(0).toString().contains(expectedErrorString));
  }
  
  @Test
  public void missingFunctionInReferenceCreatesNoErrors() {
    Program submissionProgram = ModelProgram.MODEL_MISSING_FUNCTION_IN_REFERENCE_B_C.get();
    Program referenceProgram = ModelProgram.MODEL_MISSING_FUNCTION_IN_REFERENCE_C_C.get();
    
    List<Input> inputs = new ArrayList<>();

    StructuralMapping structuralMapping = new StructuralMapping();
    Map<Integer, Integer> mainLocationMapping = new HashMap<>();
    mainLocationMapping.put(1,1);
    structuralMapping.put(Constants.DEFAULT_ENTRY_FUNCTION_NAME, mainLocationMapping);
    Map<Integer, Integer> addNumsLocationMapping = new HashMap<>();
    addNumsLocationMapping.put(null,1); //ref,sub
    structuralMapping.put("addNums", addNumsLocationMapping);

    VariableMapping variableMapping = new VariableMapping();
    Map<Variable, Variable> mainVarMapping = new HashMap<>();
    mainVarMapping.put(new Variable("x"), new Variable("x"));
    mainVarMapping.put(new Variable("y"), new Variable("y"));
    mainVarMapping.put(new Variable(Constants.VAR_OUT), new Variable(Constants.VAR_OUT));
    mainVarMapping.put(new Variable(Constants.VAR_RET), new Variable(Constants.VAR_RET));
    variableMapping.add(Constants.DEFAULT_ENTRY_FUNCTION_NAME, mainVarMapping);
    Map<Variable, Variable> addNumsVarMapping = new HashMap<>();
    addNumsVarMapping.put(new Variable("dummyn1"), new Variable("n1")); //ref, sub
    addNumsVarMapping.put(new Variable("dummyn2"), new Variable("n2"));
    addNumsVarMapping.put(new Variable("dummyret"), new Variable(Constants.VAR_RET));
    variableMapping.add("addNums", addNumsVarMapping);

    Interpreter interpreter =
            new InterpreterServiceImpl("c", Constants.DEFAULT_ENTRY_FUNCTION_NAME);

    ErrorLocalizerImpl errorLocalizer = new ErrorLocalizerImpl();
    ErrorLocalisation errorLocalisation = errorLocalizer.localizeErrors(submissionProgram,
            referenceProgram, inputs, structuralMapping, variableMapping, interpreter);

    //false positive error in main as $out is considered different:
    //printf("%d", addNums(n1, n2)) and printf("%d", x+y)
    List<ErrorLocation> mainErrorLocations = errorLocalisation
            .getErrorLocations(Constants.DEFAULT_ENTRY_FUNCTION_NAME, mainVarMapping);
    assertEquals(1, mainErrorLocations.size());

    //no errors as addNums is not in reference program
    List<ErrorLocation> addNumsErrorLocations = errorLocalisation
            .getErrorLocations("addNums", addNumsVarMapping);
    assertEquals(0, addNumsErrorLocations.size());
  }

  @Test
  public void noDivergenceReturnsEmptyErrorLocalization() {
    List<ErrorLocation> errorLocations = localizeErrorsForC1C1();

    assertEquals(0, errorLocations.size());
  }
}
