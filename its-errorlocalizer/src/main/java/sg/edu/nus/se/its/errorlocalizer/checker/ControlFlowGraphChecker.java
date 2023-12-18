package sg.edu.nus.se.its.errorlocalizer.checker;

import static sg.edu.nus.se.its.errorlocalizer.ErrorLocalizerImpl.LOC_FUNCTION_OR_VARIABLE;
import static sg.edu.nus.se.its.errorlocalizer.utils.StructuralMappingUtil.getCorrespondingSubLoc;

import java.util.Objects;
import java.util.stream.Stream;
import org.javatuples.Pair;
import sg.edu.nus.se.its.alignment.StructuralMapping;
import sg.edu.nus.se.its.alignment.VariableMapping;
import sg.edu.nus.se.its.errorlocalizer.ResultEditor;
import sg.edu.nus.se.its.errorlocalizer.utils.StructuralMappingUtil;
import sg.edu.nus.se.its.model.Function;
import sg.edu.nus.se.its.model.Input;
import sg.edu.nus.se.its.model.Program;

/**
 * Represents a class checking the CFG of the programs.
 */
public class ControlFlowGraphChecker extends Checker {
  public static final String MSG_OUTGOING_EDGE_UNMATCHED =
      "The outgoing edges at loc %d are unmatched.";
  public static final String MSG_SUB_LOC_NOT_FOUND =
      "No matching submission loc for loc %d.";

  public ControlFlowGraphChecker(ResultEditor resultEditor, Program referenceProgram,
                                 Program submittedProgram, StructuralMapping structuralMapping,
                                 VariableMapping variableMapping) {
    super(resultEditor, referenceProgram, submittedProgram, structuralMapping, variableMapping);
  }

  /**
   * For each loc for the current reference function for the current input, 
   * compares next locs based on transition locations stored in the 2 function and 
   * based on the structuralMapping. Also checks for locs in reference function that 
   * are not found in the matching submission function, and excludes them.
   *
   * @param configFunc The configuration from previous stage
   * @return A stream of configurations the next stage in error localizer
   */
  @Override
  public Stream<? extends Configuration> check(Configuration configFunc) {
    Function refFunc = configFunc.getReferenceFunction();
    Function subFunc = configFunc.getSubmittedFunction();
    return refFunc.getLocations().stream()
        .map(refLoc -> new Pair<>(refLoc,
            getCorrespondingSubLoc(structuralMapping, refFunc.getName(), refLoc)))
        .map(p -> checkSubLocsExist(configFunc.getProgramInput(), refFunc, p))
        .filter(Objects::nonNull)
        .map(p -> checkOutgoingEdges(configFunc.getProgramInput(), p, refFunc, subFunc))
        .filter(Objects::nonNull)
        .map(refSubLocPair ->
            configFunc.withLocs(refSubLocPair.getValue0(), refSubLocPair.getValue1()));
  }

  private Pair<? extends Integer, ? extends Integer>
      checkOutgoingEdges(Input programInput, Pair<? extends Integer, ? extends Integer> locPair,
                     Function refFunc, Function subFunc) {
    int refLoc = locPair.getValue0();
    int subLoc = locPair.getValue1();
    Integer refTrueDest = refFunc.getTrans(refLoc, true);
    Integer refFalseDest = refFunc.getTrans(refLoc, false);
    Integer subTrueDest = subFunc.getTrans(subLoc, true);
    Integer subFalseDest = subFunc.getTrans(subLoc, false);

    boolean trueDestMapped = refTrueDest == subTrueDest
        || (refTrueDest != null && subTrueDest != null
        && StructuralMappingUtil.areMappedLoc(structuralMapping,
        refFunc.getName(), refTrueDest, subTrueDest));
    boolean falseDestMapped = refFalseDest == subFalseDest
        || (refFalseDest != null && subFalseDest != null
        && StructuralMappingUtil.areMappedLoc(structuralMapping,
        subFunc.getName(), refFalseDest, subFalseDest));

    if (!trueDestMapped || !falseDestMapped) {
      String msg = String.format(MSG_OUTGOING_EDGE_UNMATCHED, refLoc);
      resultEditor.addUnmatchedException(programInput, refLoc, subLoc, msg,
          refFunc.getName(), this.variableMapping.getTopMapping(refFunc.getName()));
      return null;
    }
    return locPair;
  }

  private Pair<? extends Integer, ? extends Integer>
      checkSubLocsExist(Input input, Function refFunc,
                        Pair<? extends Integer, ? extends Integer> p) {
    int refLoc = p.getValue0();
    if (p.getValue1() == null) {
      resultEditor.addUnmatchedException(input, refLoc, LOC_FUNCTION_OR_VARIABLE,
          String.format(MSG_SUB_LOC_NOT_FOUND, refLoc),
          refFunc.getName(), this.variableMapping.getTopMapping(refFunc.getName()));
      return null;
    }
    return p;
  }
}
