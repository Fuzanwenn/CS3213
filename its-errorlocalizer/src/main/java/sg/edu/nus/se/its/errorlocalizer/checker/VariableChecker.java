package sg.edu.nus.se.its.errorlocalizer.checker;

import java.util.Objects;
import java.util.stream.Stream;
import org.javatuples.Pair;
import sg.edu.nus.se.its.alignment.StructuralMapping;
import sg.edu.nus.se.its.alignment.VariableMapping;
import sg.edu.nus.se.its.errorlocalizer.ResultEditor;
import sg.edu.nus.se.its.errorlocalizer.utils.FunctionUtil;
import sg.edu.nus.se.its.model.Program;
import sg.edu.nus.se.its.model.Variable;

/**
 * Represents a class to check the variable mappings of the programs.
 */
public class VariableChecker extends Checker {
  public static final String MSG_SUB_VAR_NOT_FOUND =
      "No matching submission variable <%s> at loc %d.";

  /**
   * Constructs a checker to check for differences in the variable mappings of the programs.
   *
   * @param resultEditor contains the results of the checks
   * @param referenceProgram is the correct program answer
   * @param submittedProgram is the program submitted by students
   * @param structuralMapping is the mapping of the structure between the two programs
   * @param variableMapping is the mapping of the variables between the two programs
   */
  public VariableChecker(ResultEditor resultEditor, Program referenceProgram,
                         Program submittedProgram, StructuralMapping structuralMapping,
                         VariableMapping variableMapping) {
    super(resultEditor, referenceProgram, submittedProgram, structuralMapping, variableMapping);
  }

  /**
   * Checks the two programs for the differences in the variable mappings.
   *
   * @param configLoc The configuration from the previous stage
   * @return the configuration to be used for the next stage
   */
  @Override
  public Stream<? extends Configuration> check(Configuration configLoc) {
    return configLoc.getReferenceFunction().getExprs(configLoc.getRefLoc()).stream()
        .map(refStrExpPair -> new Pair<>(new Variable(refStrExpPair.getValue0()),
            variableMapping.getTopMapping(configLoc.getReferenceFunction().getName())
                .get(new Variable(refStrExpPair.getValue0()))))
        .map(p -> checkSubVarExist(configLoc, p))
        .filter(Objects::nonNull)
        .map(varPair -> configLoc.withVariables(varPair.getValue0(), varPair.getValue1()));
  }

  /**
   * Checks if the submission program has the corresponding variable, which is taken from the
   * reference program.
   *
   * @param configuration The configuration from the previous stage
   * @param p Pair of corresponding variables from the reference and submission programs
   * @return the same pair if the variable exists, otherwise return null
   */
  private Pair<? extends Variable, ? extends Variable>
      checkSubVarExist(Configuration configuration,
                   Pair<? extends Variable, ? extends Variable> p) {
    Variable refVar = p.getValue0();
    Variable subVar = p.getValue1();
    if (subVar == null || !FunctionUtil.hasVariable(
        configuration.getSubmittedFunction(), subVar, configuration.getSubLoc())) {
      resultEditor.addUnmatchedException(configuration.getProgramInput(),
          configuration.getRefLoc(), configuration.getSubLoc(),
          String.format(MSG_SUB_VAR_NOT_FOUND, subVar, configuration.getSubLoc()),
          configuration.getReferenceFunction().getName(),
          variableMapping.getTopMapping(configuration.getReferenceFunction().getName()));
      return null;
    }
    return p;
  }
}
