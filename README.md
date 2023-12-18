# its-errorlocalizer-template

## Overview
The error localization is the basis for a well-working repair strategy as it identifies one of its main ingredients, the potential *fix locations*. This project will give you the chance for an in-depth study of existing error localization techniques and to apply these approaches in the context of our intelligent tutoring system. This project requires to:

* Perform a literature study on error/fault localization to make yourself familiar with the different approaches and analysis-based techniques.
* Design and implement your approach, which can work on the CFG-based program structure. Crucial is that you do not only find divergences in the control flow between the reference program and the student's submission but also identify potential *fix locations*.
* Evaluate the techniques and determine the strengths and weaknesses of the chosen techniques.


## Entry Points

* This projects has a dependency to the interpreter. Please use the provided interpreter service implementation [InterpreterServiceImpl](./its-integration-services/src/main/java/sg/edu/nus/se/its/interpreter/InterpreterServiceImpl.java). You can use the interpreter to execute a program model object as illustrated in the test cases in [BasicTest.java](./its-errorlocalizer/src/test/java/sg/edu/nus/se/its/errorlocalizer/BasicTest.java).

* [sg.edu.nus.se.its.errorlocalizer.ErrorLocalizerImpl](./its-errorlocalizer/src/main/java/sg/edu/nus/se/its/errorlocalizer/ErrorLocalizerImpl.java)
```
/**
 * Implements the concrete error localizer based on CFG alignment, with the algorithm you choose.
 */
public class ErrorLocalizerImpl implements ErrorLocalizer {

  @Override
  public ErrorLocalisation localizeErrors(Program submittedProgram, Program referenceProgram,
      List<Input> inputs, StructuralMapping structuralMapping, VariableMapping variableMapping,
      Interpreter interpreter) {
    throw new NotImplementedException();
  }

}
```

* You can use `mvn clean compile test` to build and test your implementation.

## Restrictions
* You are not allowed to change any code in the [sg.edu.nus.its.its-core](./its-core).
* You need to stick to the provided interfaces.
* You are not allowed to change the file/class name or move [sg.edu.nus.se.its.errorlocalizer.ErrorLocalizerImpl](./its-errorlocalizer/src/main/java/sg/edu/nus/se/its/errorlocalizer/ErrorLocalizerImpl.java).
* If you would require any other dependencies or libraries, you first need to seek approval by your mentor.
* You are not allowed to change any file within [.github](./.github).
