package sg.edu.nus.se.its.errorlocalizer.checker;

import static sg.edu.nus.se.its.errorlocalizer.checker.DependencyChecker.DfsState.CORRECT;
import static sg.edu.nus.se.its.errorlocalizer.checker.DependencyChecker.DfsState.ERROR_FOUND;
import static sg.edu.nus.se.its.errorlocalizer.utils.StructuralMappingUtil.getCorrespondingSubLoc;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import sg.edu.nus.se.its.alignment.StructuralMapping;
import sg.edu.nus.se.its.alignment.VariableMapping;
import sg.edu.nus.se.its.errorlocalizer.ResultEditor;
import sg.edu.nus.se.its.errorlocalizer.dependencytree.DependencyNode;
import sg.edu.nus.se.its.errorlocalizer.utils.StructuralMappingUtil;
import sg.edu.nus.se.its.model.Function;
import sg.edu.nus.se.its.model.Input;
import sg.edu.nus.se.its.model.Program;


/**
 * Represents a class checking the data and control dependencies of the programs.
 */
public class DependencyChecker extends Checker {
  public static final String MSG_SUB_ROOT_NOT_FOUND =
      "Root of submission tree not found";


  public DependencyChecker(ResultEditor resultEditor, Program referenceProgram,
                           Program submittedProgram, StructuralMapping structuralMapping,
                           VariableMapping variableMapping) {
    super(resultEditor, referenceProgram, submittedProgram, structuralMapping, variableMapping);
  }

  enum DfsState {
    ERROR_FOUND, CORRECT
  }

  /**
   * This method creates two dependency trees for a particular variable in a particular loc of a 
   * particular function of the program for a particular program input. The variables are refVar 
   * (variable in reference program) and subVar (variable in submitted program). They were filled 
   * in the current Configuration object {@code fullyFilledConfig} by the previous checker.<br><br>
   * 
   * <p>The {@code withPrograms} method is first called to create a dependency tree with refVar as
   * the root node {@code referenceRoot}, and a dependency tree with subVar as the root node 
   * {@code submissionRoot}. The two trees are used to fill up {@code fullyFilledConfig}'s 
   * remaining attributes, refTree and subTree, respectively.<br><br>
   * 
   * {@code submissionRoot} is then wrapped in an Optional object. 
   * If {@code submissionRoot} is null, a DueToUnmatchedException error will be recorded in 
   * {@code ResultEditor}. Otherwise, isReturnLineNumber is executed with returned value stored 
   * in the Optional, and dfs is then executed to compare the two trees for the particular input 
   * {@code programInput}.<br><br>
   *
   * @param fullyFilledConfig The configuration from previous stage to be fully filled up here
   * @return A stream of a single Configuration object that is the current configuration with all
   *     attributes filled up. This return result is not used.</p>
   */
  @Override
  public Stream<? extends Configuration> check(Configuration fullyFilledConfig) {

    fullyFilledConfig.withPrograms(referenceProgram, submittedProgram);
    // Get root nodes
    DependencyNode referenceRoot = fullyFilledConfig.getRefTree().getRoot();
    DependencyNode submissionRoot = fullyFilledConfig.getSubTree().getRoot();
    Input programInput = fullyFilledConfig.getProgramInput();
    String refFuncName = fullyFilledConfig.getReferenceFunction().getName();

    Optional.ofNullable(submissionRoot).filter(Objects::isNull)
        .map(n -> resultEditor.isReturnLineNumber())
        .ifPresentOrElse(b -> resultEditor.addUnmatchedException(programInput,
            referenceRoot.getExpression().getLineNumber(),
            getCorrespondingSubLoc(structuralMapping, refFuncName, referenceRoot.getLoc()),
            MSG_SUB_ROOT_NOT_FOUND, refFuncName, variableMapping.getTopMapping(refFuncName)),
            () -> dfs(referenceRoot, submissionRoot, programInput));
    return Stream.of(fullyFilledConfig);
  }

  //DFS
  // Compare trees, each difference recorded as an error location
  // (and for VariableValueMismatch, an erroneous variable as well)
  private DfsState dfs(DependencyNode referenceNode,
                                          DependencyNode submissionNode,
                                          Input programInput) {

    Function refFunction = referenceNode.getFunction();

    // Visit node
    if (!referenceNode.hasSameExpression(submissionNode,
        this.variableMapping) && referenceNode.isCis()) {
      resultEditor.addVariableValueMismatch(programInput, referenceNode, submissionNode,
          refFunction.getName(),
          this.variableMapping.getTopMapping(refFunction.getName()));
      return ERROR_FOUND;
    }

    // Check if next level of parents nodes are comparable in the first place before recursing
    boolean areControlParentsMatched = referenceNode.getControlParents()
        .stream()
        .allMatch(refNode -> submissionNode.getControlParents().stream()
            .anyMatch(subNode -> refNode.hasSameExpression(subNode, this.variableMapping)
                && StructuralMappingUtil.areMappedLoc(
                this.structuralMapping,
                refNode.getFunction().getName(),
                refNode.getLoc(),
                subNode.getLoc())));

    boolean areDataArgumentSameSize = referenceNode.getDataParents().size()
        == submissionNode.getDataParents().size();
    boolean areDataParentsMatched = areDataArgumentSameSize
        && IntStream.range(0, referenceNode.getDataParents().size())
        .allMatch(i -> referenceNode.getDataParents().get(i).stream()
            .allMatch(refNode -> submissionNode.getDataParents().get(i).stream()
                .anyMatch(subNode ->
                    areNodesEquivalent(refNode, subNode))));

    if (!(areControlParentsMatched && areDataParentsMatched)) {
      if ((referenceNode.getSymmetricOperationNode().isCis())
          || referenceNode.isTrans()) {
        resultEditor.addVariableValueMismatch(programInput, referenceNode, submissionNode,
            refFunction.getName(),
            this.variableMapping.getTopMapping(refFunction.getName()));
      }
      return ERROR_FOUND;
    }
    referenceNode.getControlParents().forEach(refNode -> submissionNode.getControlParents()
        .forEach(subNode ->
            compareNodes(refNode, subNode, programInput)));

    IntStream.range(0, referenceNode.getDataParents().size())
        .forEach(i -> referenceNode.getDataParents().get(i)
            .forEach(refNode -> submissionNode.getDataParents().get(i)
                .forEach(subNode ->
                    compareNodes(refNode, subNode, programInput))));
    return CORRECT;
  }

  private void compareNodes(DependencyNode refNode, DependencyNode subNode, Input programInput) {
    String funcName = refNode.getFunction().getName();
    if (!StructuralMappingUtil
        .areMappedLoc(structuralMapping, funcName, refNode.getLoc(), subNode.getLoc())) {
      return;
    }
    DependencyNode refTransNode = refNode.getSymmetricOperationNode();
    DfsState cisResult = null;
    boolean refSubSameExpression = refNode.hasSameExpression(subNode, variableMapping);
    if (refSubSameExpression) {
      cisResult = dfs(refNode, subNode, programInput);
    }
    if ((ERROR_FOUND.equals(cisResult) || !refSubSameExpression) && refTransNode.isTrans()) {
      dfs(refTransNode, subNode, programInput);
    }
  }

  private boolean areNodesEquivalent(DependencyNode refNode, DependencyNode subNode) {
    String funcName = refNode.getFunction().getName();
    boolean sameLoc = StructuralMappingUtil.areMappedLoc(
        structuralMapping,
        funcName,
        refNode.getLoc(),
        subNode.getLoc());
    DependencyNode transRefNode = refNode.getSymmetricOperationNode();
    boolean cis = refNode.hasSameExpression(subNode, variableMapping);
    boolean trans = transRefNode != null
        && transRefNode.hasSameExpression(subNode, variableMapping);

    return sameLoc && (cis || trans);
  }
}
