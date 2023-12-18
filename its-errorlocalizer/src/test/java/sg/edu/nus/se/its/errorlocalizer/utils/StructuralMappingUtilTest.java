package sg.edu.nus.se.its.errorlocalizer.utils;

import org.junit.jupiter.api.Test;
import sg.edu.nus.se.its.alignment.StructuralMapping;
import sg.edu.nus.se.its.util.constants.Constants;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test class for StructuralMappingUtil class.
 */
public class StructuralMappingUtilTest {

  /**
   * Test for getCorrespondingRefLoc method.
   */
  @Test
  public void testGetCorrespondingRefLoc() {
    Map<Integer, Integer> mapping = new HashMap<>();
    mapping.put(1, 1);
    mapping.put(4, 2);
    mapping.put(9, 3);
    mapping.put(16, 4);
    mapping.put(25, 5);
    mapping.put(36, 6);
    mapping.put(49, 7);
    mapping.put(64, 8);
    mapping.put(81, 9);

    StructuralMapping structuralAlignmentResult = new StructuralMapping();
    structuralAlignmentResult.put(Constants.DEFAULT_ENTRY_FUNCTION_NAME, mapping);

    for (int i = 1; i < 10; i++) {
      assertEquals(StructuralMappingUtil
          .getCorrespondingRefLoc(structuralAlignmentResult,
              Constants.DEFAULT_ENTRY_FUNCTION_NAME, i), i * i);
    }
  }

  /**
   * Test for getCorrespondingSubLoc method.
   */
  @Test
  public void testGetCorrespondingSubLoc() {
    Map<Integer, Integer> mapping = new HashMap<>();
    mapping.put(1, 1);
    mapping.put(4, 2);
    mapping.put(9, 3);
    mapping.put(16, 4);
    mapping.put(25, 5);
    mapping.put(36, 6);
    mapping.put(49, 7);
    mapping.put(64, 8);
    mapping.put(81, 9);

    StructuralMapping structuralAlignmentResult = new StructuralMapping();
    structuralAlignmentResult.put(Constants.DEFAULT_ENTRY_FUNCTION_NAME, mapping);

    for (int i = 1; i < 10; i++) {
      assertEquals(StructuralMappingUtil
          .getCorrespondingSubLoc(structuralAlignmentResult,
              Constants.DEFAULT_ENTRY_FUNCTION_NAME, i * i), i);
    }
  }
}
