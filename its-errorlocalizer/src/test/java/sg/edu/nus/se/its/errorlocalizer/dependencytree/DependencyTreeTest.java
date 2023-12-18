package sg.edu.nus.se.its.errorlocalizer.dependencytree;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.javatuples.Pair;
import org.junit.jupiter.api.Test;
import sg.edu.nus.se.its.alignment.VariableMapping;
import sg.edu.nus.se.its.errorlocalizer.utils.ModelProgram;
import sg.edu.nus.se.its.model.Function;
import sg.edu.nus.se.its.model.Program;
import sg.edu.nus.se.its.model.Variable;
import sg.edu.nus.se.its.util.constants.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static sg.edu.nus.se.its.errorlocalizer.dependencytree.DependencyTree.getTreesRootedAtRet;
import static sg.edu.nus.se.its.util.constants.Constants.VAR_COND;

/**
 * Test class for DependencyTree class.
 */
public class DependencyTreeTest {

  /**
   * Test constructor of DependencyTree class.
   */
  @Test
  public void testDependencyTreeConstruction() {

    List<Program> programs = new ArrayList<>();
    programs.add(ModelProgram.MODEL_C1_C.get());
    programs.add(ModelProgram.MODEL_I1_C.get());
    programs.add(ModelProgram.MODEL_LOOP_C_C.get());
    programs.add(ModelProgram.MODEL_LOOP_I_C.get());
    programs.add(ModelProgram.MODEL_TEST1_B_C.get());
    programs.add(ModelProgram.MODEL_TEST1_C_C.get());
    programs.add(ModelProgram.MODEL_TEST2_B_C.get());
    programs.add(ModelProgram.MODEL_TEST2_C_C.get());
    programs.add(ModelProgram.MODEL_TEST3_B_C.get());
    programs.add(ModelProgram.MODEL_TEST3_C_C.get());
    programs.add(ModelProgram.MODEL_TEST4_B_C.get());
    programs.add(ModelProgram.MODEL_TEST4_C_C.get());
    programs.add(ModelProgram.MODEL_TEST5_B_C.get());
    programs.add(ModelProgram.MODEL_TEST5_C_C.get());
    programs.add(ModelProgram.MODEL_TEST6_B_C.get());
    programs.add(ModelProgram.MODEL_TEST6_C_C.get());
    programs.add(ModelProgram.MODEL_TEST7_B_C.get());
    programs.add(ModelProgram.MODEL_TEST7_C_C.get());
    programs.add(ModelProgram.MODEL_ERROR_IN_FUNCTION_CALL_TEST1_B_C.get());
    programs.add(ModelProgram.MODEL_ERROR_IN_FUNCTION_CALL_TEST1_C_C.get());
    programs.add(ModelProgram.MODEL_ERROR_IN_FUNCTION_CALL_TEST2_B_C.get());
    programs.add(ModelProgram.MODEL_ERROR_IN_FUNCTION_CALL_TEST2_C_C.get());
    programs.add(ModelProgram.MODEL_SEQUENTIAL_FUNCTION_CALL_TEST_B_C.get());
    programs.add(ModelProgram.MODEL_SEQUENTIAL_FUNCTION_CALL_TEST_C_C.get());
    programs.add(ModelProgram.MODEL_CONDITION_IN_REVERSE_ORDER_TEST_B_C.get());
    programs.add(ModelProgram.MODEL_CONDITION_IN_REVERSE_ORDER_TEST_C_C.get());
    programs.add(ModelProgram.MODEL_WRONG_FUNCTION_CALL_TEST_TEST_B_C.get());
    programs.add(ModelProgram.MODEL_WRONG_FUNCTION_CALL_TEST_TEST_C_C.get());
    programs.add(ModelProgram.MODEL_OPERATORS_IN_C_TEST_B_C.get());
    programs.add(ModelProgram.MODEL_OPERATORS_IN_C_TEST_C_C.get());
    programs.add(ModelProgram.MODEL_INFINITE_LOOP_TEST_TEST_B_C.get());
    programs.add(ModelProgram.MODEL_INFINITE_LOOP_TEST_TEST_C_C.get());
    programs.add(ModelProgram.MODEL_BASIC_PYTHON_TEST_B.get());
    programs.add(ModelProgram.MODEL_BASIC_PYTHON_TEST_C.get());
    programs.add(ModelProgram.MODEL_PYTHON_IN_BUILT_FUNCTION_TEST_B.get());
    programs.add(ModelProgram.MODEL_PYTHON_IN_BUILT_FUNCTION_TEST_C.get());
    programs.add(ModelProgram.MODEL_PYTHON_FUNCTION_CALL_TEST_B.get());
    programs.add(ModelProgram.MODEL_PYTHON_FUNCTION_CALL_TEST_C.get());
    programs.add(ModelProgram.MODEL_PYTHON_LIST_TEST_B.get());
    programs.add(ModelProgram.MODEL_PYTHON_LIST_TEST_C.get());

    programs.parallelStream()
        .flatMap(p -> p.getFncs().keySet().parallelStream().map(s -> new Pair<>(p, p.getfnc(s))))
        .flatMap(pf -> pf.getValue1().getLocations().parallelStream().map(l -> new Pair<>(pf, l)))
        .flatMap(pf_l -> pf_l.getValue0().getValue1().getExprs(pf_l.getValue1()).parallelStream().map(e -> new Pair<>(pf_l.getValue0(), new Pair<>(pf_l.getValue1(), new Variable(e.getValue0())))))
        .map(pf_lv -> new DependencyTree(pf_lv.getValue0().getValue0(), pf_lv.getValue0().getValue1(), pf_lv.getValue1().getValue1(), pf_lv.getValue1().getValue0()))
        .collect(Collectors.toList());
  }

  /**
   * Test getC1Loc1VarB() location 1 of test c1.
   */
  @Test
  public void testC1Loc1() {
    Program program = ModelProgram.MODEL_C1_C.get();
    Function function = program.getFunctionForName(Constants.DEFAULT_ENTRY_FUNCTION_NAME);

    Map<Variable, Variable> varMapping = new HashMap<>();
    varMapping.put(new Variable(Constants.VAR_COND), new Variable(Constants.VAR_COND));
    varMapping.put(new Variable(Constants.VAR_RET), new Variable(Constants.VAR_RET));
    varMapping.put(new Variable(Constants.VAR_OUT), new Variable(Constants.VAR_OUT));
    varMapping.put(new Variable(Constants.VAR_IN), new Variable(Constants.VAR_IN));
    varMapping.put(new Variable("a"), new Variable("a"));
    varMapping.put(new Variable("b"), new Variable("b"));

    VariableMapping variableAlignmentResult = new VariableMapping();
    variableAlignmentResult.add(Constants.DEFAULT_ENTRY_FUNCTION_NAME, varMapping);

    DependencyNode actualLoc1VarA =
        new DependencyTree(program, function, new Variable("a"), 1).getRoot();
    DependencyNode actualLoc1VarB =
        new DependencyTree(program, function, new Variable("b"), 1).getRoot();
    DependencyNode actualLoc1VarRet =
        new DependencyTree(program, function, new Variable(VAR_COND), 1).getRoot();

    assertTrue(
        areExactlyEquals(ModelTreeRoot.getC1Loc1VarB(), actualLoc1VarB, variableAlignmentResult));

    assertFalse(
        areExactlyEquals(ModelTreeRoot.getC1Loc1VarB(), actualLoc1VarA, variableAlignmentResult));

    assertFalse(
        areExactlyEquals(ModelTreeRoot.getC1Loc1VarB(), actualLoc1VarRet, variableAlignmentResult));
  }

  /**
   * Test getLoopiLoc1varI() location 1 of test loop i.
   */
  @Test
  public void testLoopiLoc1() {
    Program program = ModelProgram.MODEL_LOOP_I_C.get();
    Function function = program.getFunctionForName(Constants.DEFAULT_ENTRY_FUNCTION_NAME);

    Map<Variable, Variable> varMapping = new HashMap<>();
    varMapping.put(new Variable(Constants.VAR_COND), new Variable(Constants.VAR_COND));
    varMapping.put(new Variable(Constants.VAR_RET), new Variable(Constants.VAR_RET));
    varMapping.put(new Variable(Constants.VAR_OUT), new Variable(Constants.VAR_OUT));
    varMapping.put(new Variable(Constants.VAR_IN), new Variable(Constants.VAR_IN));
    varMapping.put(new Variable("i"), new Variable("i"));
    varMapping.put(new Variable("result"), new Variable("result"));

    VariableMapping variableAlignmentResult = new VariableMapping();
    variableAlignmentResult.add(Constants.DEFAULT_ENTRY_FUNCTION_NAME, varMapping);

    DependencyNode actualLoc1VarResult =
        new DependencyTree(program, function, new Variable("result"), 1).getRoot();
    DependencyNode actualLoc1VarI =
        new DependencyTree(program, function, new Variable("i"), 1).getRoot();

    assertTrue(
        areExactlyEquals(
            ModelTreeRoot.getLoopiLoc1varRet(), actualLoc1VarResult, variableAlignmentResult));
    assertTrue(
        areExactlyEquals(
            ModelTreeRoot.getLoopiLoc1varI(), actualLoc1VarI, variableAlignmentResult));

    assertFalse(
        areExactlyEquals(
            ModelTreeRoot.getLoopiLoc1varRet(), actualLoc1VarI, variableAlignmentResult));

    assertFalse(
        areExactlyEquals(
            ModelTreeRoot.getLoopiLoc1varI(), actualLoc1VarResult, variableAlignmentResult));
  }

  /**
   * Test getLoopiLoc2Cond() location 2 of test loop i.
   */
  @Test
  public void testLoopiLoc2() {
    Program program = ModelProgram.MODEL_LOOP_I_C.get();
    Function function = program.getFunctionForName(Constants.DEFAULT_ENTRY_FUNCTION_NAME);

    Map<Variable, Variable> varMapping = new HashMap<>();
    varMapping.put(new Variable(Constants.VAR_COND), new Variable(Constants.VAR_COND));
    varMapping.put(new Variable(Constants.VAR_RET), new Variable(Constants.VAR_RET));
    varMapping.put(new Variable(Constants.VAR_OUT), new Variable(Constants.VAR_OUT));
    varMapping.put(new Variable(Constants.VAR_IN), new Variable(Constants.VAR_IN));
    varMapping.put(new Variable("i"), new Variable("i"));
    varMapping.put(new Variable("result"), new Variable("result"));

    VariableMapping variableAlignmentResult = new VariableMapping();
    variableAlignmentResult.add(Constants.DEFAULT_ENTRY_FUNCTION_NAME, varMapping);

    DependencyNode actualLoc2VarCond =
        new DependencyTree(program, function, new Variable(VAR_COND), 2).getRoot();

    assertTrue(
        areExactlyEquals(
            ModelTreeRoot.getLoopiLoc2Cond(), actualLoc2VarCond, variableAlignmentResult));
  }

  /**
   * Check equality of two dependency nodes.
   *
   * @param node1
   * @param node2
   * @param variableMapping
   * @return
   */
  public boolean areExactlyEquals(DependencyNode node1,
                                  DependencyNode node2,
                                  VariableMapping variableMapping) {
    if (node1 == null && node2 == null) {
      return true;
    } else if (node1 == null || node2 == null) {
      return false;
    }
    if (!(node1.hasSameExpression(node2, variableMapping)
        && node1.getLoc() == node2.getLoc()
        && node1.getExpression().getLineNumber() == node2.getExpression().getLineNumber())) {
      return false;
    }
    boolean result = true;
    if (node1.getDataParents().size() == node2.getDataParents().size()) {
      for (int i = 0; i < node1.getDataParents().size(); i++) {
        List<DependencyNode> arg1 = node1.getDataParents().get(i);
        List<DependencyNode> arg2 = node2.getDataParents().get(i);
        if (arg1.size() == arg2.size()) {
          for (int j = 0; j < arg1.size(); j++) {
            DependencyNode p1 = arg1.get(j);
            DependencyNode p2 = arg2.get(j);
            boolean r = areExactlyEquals(p1, p2, variableMapping);
            result = result && r;
            if (!r) {
              System.out.println(i + " " + j);
            }
          }
        } else {
          return false;
        }
      }
    } else {
      return false;
    }
    if (node1.getControlParents().size() == node2.getControlParents().size()) {
      result = result && IntStream.range(0, node1.getControlParents().size())
          .allMatch(i -> areExactlyEquals(node1.getControlParents().get(i),
              node2.getControlParents().get(i), variableMapping));
    } else {
      return false;
    }
    return result;
  }

  /**
   * Test for getNodeCount method.
   */
  @Test
  public void testGetNodeCount() {
    Program program = ModelProgram.MODEL_LOOP_I_C.get();
    Function function = program.getFunctionForName(Constants.DEFAULT_ENTRY_FUNCTION_NAME);

    DependencyTree actualLoc2VarCond =
        new DependencyTree(program, function, new Variable(VAR_COND), 2);
    assertEquals(actualLoc2VarCond.getNodeCount(), 10);
  }

  /**
   * Test for getTreeRootAtRet method.
   */
  @Test
  public void testGetTreeRootAtRet() {
    Program program = ModelProgram.MODEL_C1_C.get();
    Function function = program.getFunctionForName(Constants.DEFAULT_ENTRY_FUNCTION_NAME);

    Map<Variable, Variable> varMapping = new HashMap<>();
    varMapping.put(new Variable(Constants.VAR_COND), new Variable(Constants.VAR_COND));
    varMapping.put(new Variable(Constants.VAR_RET), new Variable(Constants.VAR_RET));
    varMapping.put(new Variable(Constants.VAR_OUT), new Variable(Constants.VAR_OUT));
    varMapping.put(new Variable(Constants.VAR_IN), new Variable(Constants.VAR_IN));
    varMapping.put(new Variable("a"), new Variable("a"));
    varMapping.put(new Variable("b"), new Variable("b"));

    VariableMapping variableAlignmentResult = new VariableMapping();
    variableAlignmentResult.add(Constants.DEFAULT_ENTRY_FUNCTION_NAME, varMapping);

    DependencyNode actual = getTreesRootedAtRet(program, function, null).get(0).getRoot();
    DependencyNode expected = ModelTreeRoot.getC1Loc1VarRet();
    assertTrue(areExactlyEquals(actual, expected, variableAlignmentResult));
  }
}
