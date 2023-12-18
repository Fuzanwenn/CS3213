package sg.edu.nus.se.its.errorlocalizer.utils;

import static sg.edu.nus.se.its.errorlocalizer.ErrorLocalizerImpl.LOC_ROOT_NOT_FOUND;

import sg.edu.nus.se.its.alignment.StructuralMapping;

/**
 * The utils for structural mappings used by the error localizer.
 */
public class StructuralMappingUtil {

  /**
   * Returns the reference loc of the submission loc in the reference function.
   *
   * @param structuralMapping The given structural mapping
   * @param funcName The function name
   * @param submissionLoc The loc in submission function
   * @return The loc in reference function
   */
  public static int getCorrespondingRefLoc(
      StructuralMapping structuralMapping, String funcName, int submissionLoc) {
    return structuralMapping.getMapping(funcName).entrySet().stream()
        .filter(e -> e.getValue() == submissionLoc)
        .map(e -> e.getKey()).findAny()
        .orElse(LOC_ROOT_NOT_FOUND);
  }

  /**
   * Returns the submission loc of the reference loc in the submission function.
   *
   * @param structuralMapping The given structural mapping
   * @param funcName The function name
   * @param referenceLoc The loc in reference function
   * @return The loc in submission function
   */
  public static int getCorrespondingSubLoc(
      StructuralMapping structuralMapping, String funcName, int referenceLoc) {
    return structuralMapping.getMatchingLoc(funcName, referenceLoc);
  }

  public static boolean areMappedLoc(StructuralMapping structuralMapping,
                                     String funcName,
                                     int referenceLoc,
                                     int submittedLoc) {
    return getCorrespondingSubLoc(structuralMapping, funcName, referenceLoc) == submittedLoc;
  }
}
