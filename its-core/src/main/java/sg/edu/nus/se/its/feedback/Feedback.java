package sg.edu.nus.se.its.feedback;

import sg.edu.nus.se.its.errorlocalizer.ErrorLocalisation;
import sg.edu.nus.se.its.model.Program;
import sg.edu.nus.se.its.repair.RepairCandidate;

/**
 * Interface for the feedback module.
 */
public interface Feedback {

  /**
   * Generates feedback for the student based on the identified list of repairs.
   *
   * @param repairCandidate -- one set of consistent local repairs
   * @param submittedProgram -- the submitted program
   * @return feedback in form of a String object
   */
  public String provideFeedback(RepairCandidate repairCandidate, Program submittedProgram,
                                ErrorLocalisation errorLocations);

}
