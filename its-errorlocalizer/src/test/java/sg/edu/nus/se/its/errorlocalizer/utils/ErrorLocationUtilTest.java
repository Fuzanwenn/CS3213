package sg.edu.nus.se.its.errorlocalizer.utils;

import org.junit.jupiter.api.Test;
import sg.edu.nus.se.its.errorlocalizer.ErrorLocation;
import sg.edu.nus.se.its.errorlocalizer.UnmatchedException;
import sg.edu.nus.se.its.model.Input;
import sg.edu.nus.se.its.model.Variable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static sg.edu.nus.se.its.errorlocalizer.utils.ErrorLocationUtil.areSameErrorLocation;

/**
 * Test class for ErrorLocationUtil class.
 */
public class ErrorLocationUtilTest {

  /**
   * Test for areSameErrorLocation method when error type is variable value mismatch.
   */
  @Test
  public void test_areSameErrorLocation_variableValueMismatch() {
    ErrorLocation varValMismatch1 =
        new ErrorLocation(1, 1, List.of(new Variable("a", 1)));
    ErrorLocation varValMismatch2 =
        new ErrorLocation(1, 1, List.of(new Variable("a", 1)));
    ErrorLocation varValMismatch3 =
        new ErrorLocation(1, 1, List.of(new Variable("x", 1)));
    ErrorLocation varValMismatch4 =
        new ErrorLocation(1, 2, List.of(new Variable("a", 1)));
    ErrorLocation varValMismatch5 =
        new ErrorLocation(1, 1, List.of(new Variable("a", 2)));
    ErrorLocation varValMismatch6 =
        new ErrorLocation(1, 1, List.of(new Variable("a", 2)));
    ErrorLocation varValMismatch7 =
        new ErrorLocation(1, 1, List.of(new Variable("a", 2)));

    Input input1 = new Input(new String[]{"1 2 3"}, new String[]{});
    Input input2 = new Input(new String[]{"1 2 3 4"}, new String[]{});
    varValMismatch5.setTriggeringInput(input1);
    varValMismatch6.setTriggeringInput(input1);
    varValMismatch7.setTriggeringInput(input2);

    assertTrue(areSameErrorLocation(varValMismatch1, varValMismatch1));
    assertTrue(areSameErrorLocation(varValMismatch1, varValMismatch2));
    assertFalse(areSameErrorLocation(varValMismatch1, varValMismatch3));
    assertFalse(areSameErrorLocation(varValMismatch1, varValMismatch4));
    assertFalse(areSameErrorLocation(varValMismatch1, varValMismatch5));
    assertTrue(areSameErrorLocation(varValMismatch5, varValMismatch6));
    assertFalse(areSameErrorLocation(varValMismatch5, varValMismatch7));
  }

  /**
   * Test for areSameErrorLocation method when error type is trace entry mismatch.
   */
  @Test
  public void testAreSameErrorLocationTraceEntryMismatch() {
    ErrorLocation traceMismatch1 =
        new ErrorLocation(1, 1);
    ErrorLocation traceMismatch2 =
        new ErrorLocation(1, 1);
    ErrorLocation traceMismatch3 =
        new ErrorLocation(1, 2);
    ErrorLocation traceMismatch4 =
        new ErrorLocation(2, 1);
    ErrorLocation traceMismatch5 =
        new ErrorLocation(2, 2);

    assertTrue(areSameErrorLocation(traceMismatch1, traceMismatch1));
    assertTrue(areSameErrorLocation(traceMismatch1, traceMismatch2));
    assertFalse(areSameErrorLocation(traceMismatch1, traceMismatch3));
    assertFalse(areSameErrorLocation(traceMismatch1, traceMismatch4));
    assertFalse(areSameErrorLocation(traceMismatch1, traceMismatch5));
  }

  /**
   * Test for areSameErrorLocation method when error type is unmatchedException.
   */
  @Test
  public void testAreSameErrorLocationUnmatchedException() {
    UnmatchedException unmatchedException = new UnmatchedException("msg1");
    ErrorLocation unmatched1 =
        new ErrorLocation(1, 1, unmatchedException);
    ErrorLocation unmatched2 =
        new ErrorLocation(1, 1, unmatchedException);
    ErrorLocation unmatched3 =
        new ErrorLocation(1, 1, new UnmatchedException("msg2"));
    ErrorLocation unmatched4 =
        new ErrorLocation(2, 1, unmatchedException);
    ErrorLocation unmatched5 =
        new ErrorLocation(1, 2, unmatchedException);

    assertTrue(areSameErrorLocation(unmatched1, unmatched1));
    assertTrue(areSameErrorLocation(unmatched1, unmatched2));
    assertFalse(areSameErrorLocation(unmatched1, unmatched3));
    assertFalse(areSameErrorLocation(unmatched1, unmatched4));
    assertFalse(areSameErrorLocation(unmatched1, unmatched5));
  }

  /**
   * Test for areSameErrorLocation method when error is because of wrong types.
   */
  @Test
  public void testAreSameErrorLocationWrongType() {
    UnmatchedException unmatchedException = new UnmatchedException("msg1");
    ErrorLocation unmatched =
        new ErrorLocation(1, 1, unmatchedException);
    ErrorLocation varValMismatch =
        new ErrorLocation(1, 1, List.of(new Variable("a")));
    ErrorLocation traceMismatch =
        new ErrorLocation(1, 1);

    assertFalse(areSameErrorLocation(unmatched, varValMismatch));
    assertFalse(areSameErrorLocation(unmatched, traceMismatch));
    assertFalse(areSameErrorLocation(traceMismatch, varValMismatch));
  }

}
