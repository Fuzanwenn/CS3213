package sg.edu.nus.se.its.concretization;

import java.io.File;
import sg.edu.nus.se.its.model.Program;

/**
 * Interface for the concretization of Program objects.
 */
public interface Concretization {

  /**
   * Returns the file object of the repaired program in the internal representation.
   *
   * @param repairedProgram - the repaired program in its internal representation.
   * @return the source code representation of the given Program object
   */
  public File concretize(Program repairedProgram, String fileName) throws Exception;

}
